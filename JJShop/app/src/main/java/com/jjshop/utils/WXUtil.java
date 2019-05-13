package com.jjshop.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.google.gson.Gson;
import com.jjshop.bean.WxPayBean;
import com.jjshop.ui.LoginActivity;
import com.jjshop.ui.activity.home.SubmitOrderActivity;
import com.jjshop.utils.httputil.HttpUrl;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 作者：张国庆
 * 时间：2018/7/25
 */

public class WXUtil {

    private static volatile WXUtil wxUtil;
    private WXUtil(){}

    public static WXUtil getWxUtil(){
        if(null == wxUtil){
            synchronized (WXUtil.class){
                if(null == wxUtil){
                    wxUtil = new WXUtil();
                }
            }
        }
        return wxUtil;
    }

    /**
     * 微信支付（主要是h5调用此方法）
     * @param context
     * @param jsonObject    支付数据的json
     */
    public void pay(Context context, JSONObject jsonObject){
        if(null == context || null == jsonObject){
            return;
        }
        if (!Tools.isWeixinAvilible(context)) {
            JjToast.getInstance().toast("您还没有安装微信");
            return;
        }
        JjLog.e("支付的数据 = " + jsonObject.toString());
        WxPayBean.WxPayData bean = new Gson().fromJson(jsonObject.toString(), WxPayBean.WxPayData.class);
        if(jsonObject.has("package")){
            try {
                bean.setPackages(jsonObject.getString("package"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        pay(context, bean);
    }

    /**
     * 微信支付（原生调用此方法）
     * @param context
     * @param data      支付数据的实体bean
     */
    public void pay(Context context, WxPayBean.WxPayData data){
        if(null == context || null == data){
            return;
        }
        if (!Tools.isWeixinAvilible(context)) {
            JjToast.getInstance().toast("您还没有安装微信");
            return;
        }
        String appid = data.getAppid(), partnerid = data.getPartnerid(), prepayid = data.getPrepayid();
        String noncestr = data.getNoncestr(), timestamp = data.getTimestamp();
        String package1 = data.getPackages(), sign = data.getSign();
        // 判空处理
        if(StringUtil.isEmpty(appid) || StringUtil.isEmpty(partnerid) ||
                StringUtil.isEmpty(prepayid) || StringUtil.isEmpty(noncestr) ||
                StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(package1) ||
                StringUtil.isEmpty(sign)){
            JjToast.getInstance().toast("支付数据有问题");
            return;
        }
        // 显示加载框
        showHideLoading(context, View.VISIBLE);
        IWXAPI api = WXAPIFactory.createWXAPI(context, HttpUrl.WECHAT_APP_ID);
        api.registerApp(HttpUrl.WECHAT_APP_ID);
        try {
            PayReq req = new PayReq();
            req.appId = appid;
            req.partnerId = partnerid;
            req.prepayId = prepayid;
            req.nonceStr = noncestr;
            req.timeStamp = timestamp;
            req.packageValue = package1;
            req.sign = sign;
            req.extData = "app data"; // optional
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            api.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 微信登录
     * @param context
     */
    public void login(final Context context){
        if (!Tools.isWeixinAvilible(context)) {
            JjToast.getInstance().toast("您还没有安装微信");
            return;
        }
        if(null == context){
            return;
        }
        if (context instanceof Activity && ((Activity)context).isFinishing()) {
            return;
        }
        UMShareAPI shareAPI = UMShareAPI.get(context);
        shareAPI.getPlatformInfo((Activity) context, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                // 开始
                if(context instanceof LoginActivity){

                }
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                JjToast.getInstance().toast(map.toString());
                // 成功
                if(context instanceof LoginActivity){

                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                JjToast.getInstance().toast("失败了");
                // 失败
                if(context instanceof LoginActivity){

                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                JjToast.getInstance().toast("取消了");
                // 取消
                if(context instanceof LoginActivity){

                }
            }
        });
    }

    /**
     * 跳转到微信
     */
    public void invokeWX(Context context){
        if(null == context){
            return;
        }
        if (!Tools.isWeixinAvilible(context)) {
            JjToast.getInstance().toast("您还没有安装微信");
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);
        } catch (Exception e) {
            JjLog.e("打开微信异常 = " + e.getMessage());
        }
    }

    private void showHideLoading(Context context, int visi){
        if(null == context){
            return;
        }
        if(context instanceof SubmitOrderActivity){
            ((SubmitOrderActivity)context).showHideLoading(visi);
        }
    }
}
