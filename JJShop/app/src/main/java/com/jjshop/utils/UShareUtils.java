package com.jjshop.utils;

import android.app.Activity;

import com.jjshop.R;
import com.jjshop.utils.httputil.HttpUrl;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMWeb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 作者：张国庆
 * 时间：2018/7/18
 */

public class UShareUtils {
    private static volatile UShareUtils shareUtils = null;
    private UShareUtils(){}

    public static UShareUtils shareUtils(){
        if(null == shareUtils){
            synchronized (UShareUtils.class){
                if(null == shareUtils){
                    shareUtils = new UShareUtils();
                }
            }
        }
        return shareUtils;
    }

    /**
     * 分享图片
     * @param activity
     * @param img           图片
     * @param share_media   分享类型（微信、朋友圈等）
     * @param listener      分享回调监听
     */
    public void shareImg(Activity activity, String img, SHARE_MEDIA share_media, UMShareListener listener){
        if(null == activity || activity.isFinishing() || StringUtil.isEmpty(img) || null == share_media){
            JjToast.getInstance().toast("分享内容为空");
            listener.onCancel(share_media);
            return;
        }
        if (!Tools.isWeixinAvilible(activity)) {
            JjToast.getInstance().toast("您还没有安装微信");
            return;
        }
        ShareAction shareAction = new ShareAction(activity);
        UMImage imageurl = new UMImage(activity, img);
        imageurl.setThumb(imageurl);
        shareAction.withMedia(imageurl)
                .setPlatform(share_media);
        if(null != listener){// 分享回调监听
            shareAction.setCallback(listener);
        }
        shareAction.share();
    }

    /**
     * 分享链接
     * @param activity
     * @param title         标题
     * @param info          描述
     * @param img           图片
     * @param url           链接
     * @param share_media   分享类型（微信、朋友圈等）
     * @param listener      分享回调监听
     */
    public void shareWeb(Activity activity, String title, String info, String img, String url,
                         SHARE_MEDIA share_media, UMShareListener listener){
        JjLog.e("title = " + title);
        JjLog.e("info = " + info);
        JjLog.e("img = " + img);
        JjLog.e("url = " + url);
        JjLog.e("share_media = " + share_media);
        if(null == activity || activity.isFinishing() || StringUtil.isEmpty(title) || StringUtil.isEmpty(info) ||
                StringUtil.isEmpty(url) || null == share_media){
            JjToast.getInstance().toast("分享内容为空");
            listener.onCancel(share_media);
            return;
        }
        if (!Tools.isWeixinAvilible(activity)) {
            JjToast.getInstance().toast("您还没有安装微信");
            return;
        }
        ShareAction shareAction = new ShareAction(activity);
        UMWeb web = new UMWeb(url);
        web.setTitle(info);
        web.setDescription(title);
        UMImage umImage;
        if(StringUtil.isEmpty(img)){
            umImage = new UMImage(activity, R.mipmap.ic_launcher);
        }else{
            umImage = new UMImage(activity, img);
        }
        umImage.setThumb(umImage);
        web.setThumb(umImage);
        shareAction.withMedia(web)
                .setPlatform(share_media);
        if(null != listener){// 分享回调监听
            shareAction.setCallback(listener);
        }
        shareAction.share();
    }


    /**
     * 分享小程序
     * @param activity
     * @param title         标题
     * @param info          描述
     * @param img           图片
     * @param url           链接
     * @param xcx_share_url 小程序路径
     * @param share_media   分享类型（微信、朋友圈等）
     * @param listener      分享回调监听
     */
    public void shareUMMin(final Activity activity, final String title, final String info, final String img, final String url, final String xcx_share_url,
                           SHARE_MEDIA share_media, UMShareListener listener){
        JjLog.e("title = " + title);
        JjLog.e("info = " + info);
        JjLog.e("img = " + img);
        JjLog.e("url = " + url);
        JjLog.e("xcx_share_url = " + xcx_share_url);
        JjLog.e("share_media = " + share_media);
        if(null == activity || activity.isFinishing() || StringUtil.isEmpty(title) || StringUtil.isEmpty(info) ||
                StringUtil.isEmpty(url) || StringUtil.isEmpty(xcx_share_url) || null == share_media){
            JjToast.getInstance().toast("分享内容为空");
            listener.onCancel(share_media);
            return;
        }
        if (!Tools.isWeixinAvilible(activity)) {
            JjToast.getInstance().toast("您还没有安装微信");
            return;
        }
        ShareAction shareAction = new ShareAction(activity);
        //兼容低版本的网页链接
        UMMin umMin = new UMMin(url);
        // 小程序消息封面图片
        UMImage umImage;
        if(StringUtil.isEmpty(img)){
            umImage = new UMImage(activity, R.mipmap.ic_launcher);
        }else{
            umImage = new UMImage(activity, img);
        }
        umMin.setThumb(umImage);
        // 小程序消息title
        umMin.setTitle(title);
        // 小程序消息描述
        umMin.setDescription(info);
        //小程序页面路径
        umMin.setPath(xcx_share_url);
        // 小程序原始id,在微信平台查询
        umMin.setUserName(HttpUrl.WECHAT_MIN_ID);
        shareAction.withMedia(umMin)
                .setPlatform(share_media);
        if(null != listener){// 分享回调监听
            shareAction.setCallback(listener);
        }
        shareAction.share();

        // 小程序处于开发版
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final byte[] bytes = thumbData(img);
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        IWXAPI api = WXAPIFactory.createWXAPI(activity, HttpUrl.WECHAT_APP_ID);
//                        WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
//                        miniProgramObj.webpageUrl = url; // 兼容低版本的网页链接
//                        miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPROGRAM_TYPE_TEST;// 正式版:0，测试版:1，体验版:2
//                        miniProgramObj.userName = HttpUrl.WECHAT_MIN_ID;     // 小程序原始id
//                        miniProgramObj.path = xcx_share_url;            //小程序页面路径
//
//                        WXMediaMessage msg = new WXMediaMessage(miniProgramObj);
//                        msg.title = title;                    // 小程序消息title
//                        msg.description = info;               // 小程序消息desc
//                        msg.thumbData = bytes;                      // 小程序消息封面图片，小于128k
//
//                        SendMessageToWX.Req req = new SendMessageToWX.Req();
//                        req.transaction = buildTransaction("webpage");
//                        req.message = msg;
//                        req.scene = SendMessageToWX.Req.WXSceneSession;  // 目前支持会话
//                        api.sendReq(req);
//                    }
//                });
//            }
//        }).start();
    }

//    private static String buildTransaction(final String type) {
//        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
//    }
//
//    private static byte[] thumbData(final String url){
//        URL htmlUrl = null;
//        InputStream inStream = null;
//        try {
//            htmlUrl = new URL(url);
//            URLConnection connection = htmlUrl.openConnection();
//            HttpURLConnection httpConnection = (HttpURLConnection)connection;
//            int responseCode = httpConnection.getResponseCode();
//            if(responseCode == HttpURLConnection.HTTP_OK){
//                inStream = httpConnection.getInputStream();
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        byte[] data = inputStreamToByte(inStream);
//
//        return data;
//    }
//
//    private static byte[] inputStreamToByte(InputStream is) {
//        try{
//            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
//            int ch;
//            while ((ch = is.read()) != -1) {
//                bytestream.write(ch);
//            }
//            byte imgdata[] = bytestream.toByteArray();
//            bytestream.close();
//            return imgdata;
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//
//        return null;
//    }
}
