package com.jjshop.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.jjshop.R;
import com.jjshop.app.JJShopApplication;
import com.jjshop.base.BaseActivity;
import com.jjshop.base.BasePresenter;
import com.jjshop.ui.activity.home.HomeActivity;
import com.jjshop.ui.LoginActivity;
import com.jjshop.utils.AppCallBack;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.PermissionUtil;
import com.jjshop.utils.StringUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 启动页
 */

public class WelcomeActivity extends BaseActivity {

    private Handler handler;
    private Runnable runnable;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_welcome;
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashScreen"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initData() {
//        // 有定位权限，直接等待1s直接跳转首页
//        if(PermissionUtil.build().checkPermission(this, PermissionUtil.LOCATION[0], false) &&
//                PermissionUtil.build().checkPermission(this, PermissionUtil.LOCATION[1], false)){
            if(null != handler){
                handler.removeCallbacks(runnable);
            }
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    initCookieUserAgent();
                }
            };
            handler.postDelayed(runnable, 1000);
//            return;
//        }
//        // 请求定位权限
//        PermissionUtil.build().requestPermission(this, PermissionUtil.LOCATION);
    }

    // 获取位置信息
    public void initCookieUserAgent(){
        // 刷新cookie、userAgent
        CommonUtils.build().initUserAgentAndCookie(this);
        // 跳转首页、登录页
        invoke();
    }

    private void invoke(){
        if(StringUtil.isEmpty(JJShopApplication.mCookie)){
            goLogin();
            return;
        }
        goMain();
    }

    private void goMain(){
        HomeActivity.invoke(this);
        finish();
    }

    private void goLogin(){
        LoginActivity.invoke(this);
        finish();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PermissionUtil.PERMISSION_REQUEST_CODE){
            if(null == grantResults || grantResults.length <= 1){
                initCookieUserAgent();
                return;
            }
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] == PackageManager.PERMISSION_GRANTED){
                // 同意
                initCookieUserAgent();
            }else{
                if(PermissionUtil.build().showPermission(this, permissions[0]) ||
                        PermissionUtil.build().showPermission(this, permissions[1])){
                    // 拒绝、没有选中不再提示
                    PermissionUtil.build().requestPermission(this, PermissionUtil.LOCATION);
                }else{
                    // 拒绝且选中不再提示
                    PermissionUtil.build().openAppSetting(this, "检测到你没有开启定位权限，为了更好的使用，请你去开启定位权限");
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PermissionUtil.PERMISSION_ALL){
            initCookieUserAgent();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != handler){
            handler.removeCallbacks(runnable);
        }
        AppCallBack.build().removeLocationUpdatesListener();
    }
}
