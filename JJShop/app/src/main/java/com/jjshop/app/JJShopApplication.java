package com.jjshop.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Process;

import com.bumptech.glide.request.target.ViewTarget;
import com.jjshop.R;
import com.jjshop.utils.AppCallBack;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.XNUtil;
import com.jjshop.utils.httputil.HttpUrl;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.zhy.http.okhttp.OkHttpUtils;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class JJShopApplication extends Application {

    private static Context mContext;
    public static String sChannel = "ERROR_JJ";
    public static String sVersion = "0.0.0";
    public static String sPackageName = "com.jjshop";
    public static String yxfc = "";// 统计活动转化率
    public static String mCookie = "";
    public static String mUser_agent = "";

    @Override
    public void onCreate() {
        mContext = getApplicationContext();
        // 获取版本号、渠道名称
        getVersionChannel();
        super.onCreate();
        // 初始化友盟
        initUmeng();
        // 初始化okhttp
        initOkhttp();
        // Glide加载配置
        ViewTarget.setTagId(R.id.glide_tag);
        //解决android N（>=24）系统以上分享 路径为file://时的 android.os.FileUriExposedException异常
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        // 初始化小能客服
        XNUtil.getXNUtil().initSDK(getContext());
        XNUtil.getXNUtil().setXpush(getContext());
        XNUtil.getXNUtil().debug();
        // 小米推送
        initMiPush();
        // bugly
        initBugly();
        // 注册切换前后台的监听
        AppCallBack.build().register(this);
        // 数据库初始化
        LitePal.initialize(this);
    }

    private void initUmeng() {
        UMConfigure.init(getContext(), "5b13670ba40fa312b80000ea"
                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");//58edcfeb310c93091c000be2
        // 5965ee00734be40b580001a0
        PlatformConfig.setWeixin(HttpUrl.WECHAT_APP_ID, HttpUrl.WECHAT_APP_SECRET);
        UMConfigure.setLogEnabled(false);

    }

    private void initOkhttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("JJHttpData", true))// 打包时需要注释
                .addNetworkInterceptor(new Interceptor() {//自定义拦截器
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request request = chain.request();
                        //配置统一的头
                        Request.Builder newRequest = request.newBuilder();
                        newRequest.addHeader(HttpUrl.COOKIE, mCookie);// cookie
                        newRequest.addHeader(HttpUrl.User_Agent, mUser_agent);// 设备信息
                        if (!StringUtil.isEmpty(yxfc)) {
                            newRequest.addHeader(HttpUrl.YXFC, yxfc);// 统计转化率
                            JjLog.e("JJHttpData-yxfc = " + yxfc);
                        }
                        JjLog.e("JJHttpData-cookie = " + HttpUrl.getCookies(getContext()));
                        JjLog.e("JJHttpData-userAgent = " + HttpUrl.getUserAgent(getContext()));
                        return chain.proceed(newRequest.build());
                    }
                })
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                //其他配置
                .build();
        //使用自定义OkHttpClient
        OkHttpUtils.initClient(okHttpClient);
    }

    private void getVersionChannel() {
        try {
            Bundle bundle = mContext.getPackageManager().getApplicationInfo(mContext
                            .getPackageName(),
                    PackageManager.GET_META_DATA).metaData;
            if (bundle == null) {
                sChannel = "ERROR_JJ";
                sVersion = "0.0.0";
                sPackageName = "com.jjshop";
            } else {
                sChannel = bundle.getString("JJ_CHANNEL");
                sVersion = bundle.getString("JJ_VERSION");
                sPackageName = getContext().getPackageName();
            }
        } catch (PackageManager.NameNotFoundException e) {
            sChannel = "ERROR_JJ";
            sVersion = "0.0.0";
            sPackageName = "com.jjshop";
        }
    }

    private void initMiPush() {
        //初始化push推送服务
        if (shouldInit()) {
            MiPushClient.registerPush(getContext(), HttpUrl.MI_APP_ID, HttpUrl.MI_APP_KEY);
        }
        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                JjLog.e("content1 = " + content);
                JjLog.e("Throwable = " + t.getMessage());
            }

            @Override
            public void log(String content) {
                JjLog.e("content2 = " + content);
            }
        };
        Logger.setLogger(this, newLogger);
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    private void initBugly() {
        CrashReport.initCrashReport(getContext()); // 初始化SDK  BuildConfig.LOG_DEBUG
        // true代表App处于调试阶段，false代表App发布阶段
    }

    public static Context getContext() {
        return mContext;
    }

}
