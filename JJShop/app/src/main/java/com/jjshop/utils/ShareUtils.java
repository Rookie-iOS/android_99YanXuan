package com.jjshop.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.gson.Gson;
import com.jjshop.bean.JJShareBean;
import com.jjshop.dialog.JJmailImageSaveDialog;
import com.jjshop.dialog.JJmailImageShareDialog;
import com.jjshop.dialog.OpenWXDialog;
import com.jjshop.listener.OnJjShareCallBackAdapter;
import com.jjshop.ui.activity.home.DetailActivity;
import com.jjshop.ui.activity.home.HomeActivity;
import com.jjshop.ui.activity.home.SearchActivity;
import com.jjshop.ui.WebViewActivity;
import com.jjshop.ui.activity.home.HotZhuanquActivity;
import com.jjshop.ui.activity.home.RushBuyActivity;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.json.JSONObject;

import java.util.List;

public class ShareUtils {
    private static volatile ShareUtils mShareUtils;
    private ShareUtils(){}
    
    public static ShareUtils build(){
        if(null == mShareUtils){
            synchronized (ShareUtils.class){
                if(null == mShareUtils){
                    mShareUtils = new ShareUtils();
                }
            }
        }
        return mShareUtils;
    }

    /**
     * 获取分享需要数据----默认无回调监听
     * @param context
     * @param good_id       商品id
     */
    public void loadShare(final Context context, String good_id, boolean isHaveImg){
        loadShare(context, good_id, isHaveImg, null);
    }

    /**
     * 获取分享需要数据----默认显示二维码、复制图片按钮，无回调监听
     * @param context
     * @param good_id       商品id
     */
    public void loadShare(final Context context, String good_id){
        loadShare(context, good_id, true, null);
    }

    /**
     * 获取分享需要数据----默认显示二维码、复制图片按钮，有回调监听
     * @param context
     * @param good_id       商品id
     * @param callBack      分享监听
     */
    public void loadShare(final Context context, String good_id, OnJjShareCallBackAdapter callBack){
        loadShare(context, good_id, true, callBack);
    }

    /**
     * 获取分享需要数据
     * @param context
     * @param good_id       商品id
     * @param isHaveImg     是否显示 二维码、复制图片按钮
     * @param callBack      分享监听
     */
    public void loadShare(final Context context, String good_id, final boolean isHaveImg, final OnJjShareCallBackAdapter callBack){
        if(null == context || StringUtil.isEmpty(good_id)){
            return;
        }
        shareLoading(context, View.VISIBLE);
        HttpHelper.bulid().getRequest(HttpUrl.build().getShare(good_id, PreUtils.getString(context, PreUtils.SHOP_ID)),
                JJShareBean.class, new OnRequestCallBack<JJShareBean>() {
                    @Override
                    public void onSuccess(JJShareBean data) {
                        shareLoading(context, View.GONE);
                        shareDialog(context, data, isHaveImg, callBack);
                    }

                    @Override
                    public void onError(String msg) {
                        shareLoading(context, View.GONE);
                    }
                });
    }

    /**
     * 显示分享的弹框-----(H5分享直接传json)
     * @param context
     * @param json          分享数据 (h5传过来的json数据)
     * @param isHaveImg     是否添加二维码、复制图片按钮
     * @param listener      分享回调监听
     */
    public void shareDialog(final Context context, String json, final boolean isHaveImg, final OnJjShareCallBackAdapter listener){
        if(StringUtil.isEmpty(json)){
            return;
        }
        JjLog.e("h5分享数据：" + json);
        try {
            JSONObject object = new JSONObject(json);
            if(null == object){
                return;
            }
            JJShareBean.DataBean bean = new Gson().fromJson(json, JJShareBean.DataBean.class);
            if(bean == null){
                return;
            }
            setUrl(bean.getUrl());
            final JJShareBean shareBean = new JJShareBean();
            shareBean.setData(bean);
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    shareDialog(context, shareBean, isHaveImg, listener);
                }
            });
        }catch (Exception e){

        }

    }
    /**
     * 显示分享的弹框-----(原生分享直接传bean)
     * @param context
     * @param bean          分享数据 bean
     * @param isHaveImg     是否添加二维码、复制图片按钮
     * @param callBack      分享回调监听
     */
    public void shareDialog(final Context context, JJShareBean bean, boolean isHaveImg, final OnJjShareCallBackAdapter callBack){
        if(null == context || null == bean){
            return;
        }
        JJShareBean.DataBean dataBean = bean.getData();
        if(null == dataBean){
            return;
        }
        final String title = dataBean.getTitle(),// 标题
              content = dataBean.getIntro(), // 内容
              img = dataBean.getThumb(), // 缩略图
              url = dataBean.getUrl(), // 链接
              qcodeimg = dataBean.getQrcodeimg(), // 带二维码的图片
              xcx_share_url = dataBean.getXcx_share_url();// 小程序的路径
        final List<String> shareImgs = dataBean.getShare_imgs();// 多图

        final String WX_MIN = "share_wechat_xiaochengxu";
        final String CODE_IMG = "save_img";
        final String CROP_IMG = "share_img";
        final String CROP_URL = "share_url";
        // 实例化分享模板
        ShareAction mShareAction = new ShareAction((Activity) context);
        // 添加微信、朋友圈分享按钮
        mShareAction.setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE);
        // 第一个参数是显示的名称，第二个参数是点击时判断的mKeyword的值，第三个参数是显示图片的名称
//        if(!StringUtil.isEmpty(xcx_share_url)){// 添加小程序分享按钮
//            mShareAction.addButton("小程序", WX_MIN, WX_MIN, "");
//        }
        if (isHaveImg){// 添加二维码、复制图片分享按钮
            mShareAction.addButton("二维码", CODE_IMG, CODE_IMG, "");
            mShareAction.addButton("复制图片", CROP_IMG, CROP_IMG, "");
        }
        // 添加复制链接分享按钮
        mShareAction.addButton("复制链接", CROP_URL, CROP_URL, "");
        // 设置分享按钮点击监听
        mShareAction.setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        // 自定义按钮的点击事件
                        if (share_media == null){
                            if (CROP_URL.equals(snsPlatform.mKeyword)){// 复制链接
                                Tools.clipContent(context, url);
                                JjToast.getInstance().toast("复制成功");
                            }else if (CROP_IMG.equals(snsPlatform.mKeyword)){// 复制图片（显示dialog, 有分享到微信朋友圈和微信好友功能）
                                copyImg(context, qcodeimg, callBack);
                            }else if (CODE_IMG.equals(snsPlatform.mKeyword)){// 二维码
                                showSaveDialog(context, qcodeimg);
                            }/*else if (WX_MIN.equals(snsPlatform.mKeyword)){// 小程序
                                shareLoading(context, View.VISIBLE);
                                UShareUtils.shareUtils().shareUMMin((Activity) context, title, content, img, url, xcx_share_url,
                                        SHARE_MEDIA.WEIXIN, umShareListener(context, callBack));
                            }*/
                            return;
                        }
                        //判断是否安装微信，如果没有安装微信 又没有判断就直达微信分享是会挂掉的
                        if (!Tools.isWeixinAvilible(context)) {
                            JjToast.getInstance().toast("您还没有安装微信");
                            return;
                        }
                        // 多图分享到朋友圈
                        if (null != shareImgs && shareImgs.size() > 0 && share_media == SHARE_MEDIA.WEIXIN_CIRCLE){
                            Tools.clipContent(context, title + "\n" + url);
                            shareMoreImage(context, shareImgs);
                            return;
                        }
                        Tools.clipContent(context, title);
                        shareLoading(context, View.VISIBLE);
                        if(!StringUtil.isEmpty(xcx_share_url)){// 小程序
                            UShareUtils.shareUtils().shareUMMin((Activity) context, title, content, img, url, xcx_share_url,
                                    SHARE_MEDIA.WEIXIN, umShareListener(context, callBack));
                            return;
                        }
                        // 其他正常分享
                        UShareUtils.shareUtils().shareWeb((Activity) context, title, content, img, url,
                                share_media, umShareListener(context, callBack));
                    }
                });
        ShareBoardConfig config = new ShareBoardConfig();
        config.setIndicatorVisibility(false);
        mShareAction.open(config);
    }

    /**
     * 分享回调监听
     * @param context
     * @param callBackAdapter
     * @return
     */
    private UMShareListener umShareListener(final Context context, final OnJjShareCallBackAdapter callBackAdapter){
        UMShareListener umShareListener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                JjLog.e("分享开始");
                if(null != callBackAdapter){
                    callBackAdapter.onStart(share_media);
                }
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                JjLog.e("分享成功");
                if(null != callBackAdapter){
                    callBackAdapter.onSuccess(share_media);
                }
                shareLoading(context, View.GONE);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                JjLog.e("分享异常：" + throwable.getMessage());
                if(null != callBackAdapter){
                    callBackAdapter.onError(share_media);
                }
                shareLoading(context, View.GONE);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                JjLog.e("分享取消");
                if(null != callBackAdapter){
                    callBackAdapter.onCancel(share_media);
                }
                shareLoading(context, View.GONE);
            }
        };
        return umShareListener;
    }

    /**
     * 复制图片弹框
     * @param activity
     * @param img
     * @param callBack
     */
    private void copyImg(final Context activity, final String img, final OnJjShareCallBackAdapter callBack){
        if(null == activity || !(activity instanceof Activity) || StringUtil.isEmpty(img)){
            return;
        }
        JJmailImageShareDialog dialog = new JJmailImageShareDialog();
        Bundle bundle = new Bundle();
        bundle.putString("img", img);
        dialog.setArguments(bundle);
        dialog.setOnJJmailShareClickListener(new JJmailImageShareDialog.OnJJmailShareClickListener() {
            @Override
            public void left() {
                if (!Tools.isWeixinAvilible(activity)) {
                    JjToast.getInstance().toast("您还没有安装微信");
                    return;
                }
                shareLoading(activity, View.VISIBLE);
                UShareUtils.shareUtils().shareImg((Activity) activity, img, SHARE_MEDIA.WEIXIN, umShareListener(activity, callBack));
            }

            @Override
            public void right() {
                if (!Tools.isWeixinAvilible(activity)) {
                    JjToast.getInstance().toast("您还没有安装微信");
                    return;
                }
                shareLoading(activity, View.VISIBLE);
                UShareUtils.shareUtils().shareImg((Activity) activity, img, SHARE_MEDIA.WEIXIN_CIRCLE, umShareListener(activity, callBack));
            }
        });
        dialog.show(((FragmentActivity)activity).getSupportFragmentManager(), "JJmailImageShareDialog");
    }

    /**
     * 二维码的弹框
     * @param activity
     * @param img
     */
    private void showSaveDialog(final Context activity, final String img){
        if(null == activity || !(activity instanceof FragmentActivity) || StringUtil.isEmpty(img)){
            return;
        }
        JJmailImageSaveDialog dialog = new JJmailImageSaveDialog();
        Bundle bundle = new Bundle();
        bundle.putString("img", img);
        dialog.setArguments(bundle);
        dialog.setOnSaveImgListener(new JJmailImageSaveDialog.OnSaveImgListener() {
            @Override
            public void saveSuccess(boolean b) {
                if(b){
                    JjToast.getInstance().toast("保存成功");
                    return;
                }
                JjToast.getInstance().toast("保存失败，再试一次");
            }
        });

        dialog.show(((FragmentActivity)activity).getSupportFragmentManager(), "JJmailImageSaveDialog");
    }

    /**
     * 分享多图到朋友圈
     * @param shareImgs 图片集合
     */
    private void shareMoreImage(final Context activity, final List<String> shareImgs) {
        if(null == activity || !(activity instanceof Activity) || null == shareImgs || shareImgs.size() <= 0){
            return;
        }
        // 判断是否有权限
        if(!PermissionUtil.build().checkPermission(activity, PermissionUtil.WRITE_EXTERNAL_STORAGE)){
            return;
        }
        if(!PermissionUtil.build().checkPermission(activity, PermissionUtil.READ_EXTERNAL_STORAGE)){
            return;
        }
        //判断是否安装微信，如果没有安装微信 又没有判断就直达微信分享是会挂掉的
        if (!Tools.isWeixinAvilible(activity)) {
            JjToast.getInstance().toast("您还没有安装微信");
            return;
        }
        OpenWXDialog.build().showView(((FragmentActivity) activity).getSupportFragmentManager(), shareImgs);

    }

    /**
     * 分享时，显示隐藏加载框
     * @param activity
     * @param visible
     */
    private void shareLoading(Context activity, int visible){
        if(null == activity){
            return;
        }
        if(activity instanceof HomeActivity){// 主页
            ((HomeActivity)activity).shareShowHideLoading(visible);
        }else if(activity instanceof HotZhuanquActivity){// 新品、人气
            ((HotZhuanquActivity)activity).shareShowHideLoading(visible);
        }else if(activity instanceof WebViewActivity){// webview页面
            ((WebViewActivity)activity).shareShowHideLoading(visible);
        }else if(activity instanceof SearchActivity){// 搜索
            ((SearchActivity)activity).shareShowHideLoading(visible);
        }else if(activity instanceof RushBuyActivity){// 限时抢购
            ((RushBuyActivity)activity).shareShowHideLoading(visible);
        }else if(activity instanceof DetailActivity){// 商品详情
            ((DetailActivity)activity).shareShowHideLoading(visible);
        }
    }

    private String url;
    private void setUrl(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    /**
     * 获取当前分享的类型
     * @param share_media
     * @return
     */
    public static int getType(SHARE_MEDIA share_media) {
        if (share_media == SHARE_MEDIA.WEIXIN){
            return 1;
        }else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE){
            return 2;
        }else if (share_media == SHARE_MEDIA.QQ){
            return 3;
        }else if(share_media == SHARE_MEDIA.QZONE){
            return 4;
        }else{
            return 0;
        }
    }
}