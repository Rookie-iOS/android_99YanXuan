package com.jjshop.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.jjshop.R;
import com.jjshop.app.JJShopApplication;
import com.jjshop.base.BaseActivity;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.JJEvent;
import com.jjshop.listener.OnJjShareCallBackAdapter;
import com.jjshop.ui.activity.home.DetailActivity;
import com.jjshop.ui.activity.home.HomeActivity;
import com.jjshop.ui.activity.home.SearchActivity;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.UrlInvokeUtil;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.ShareUtils;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.WXUtil;
import com.jjshop.widget.X5WebView;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;

public class WebViewActivity extends BaseActivity {
    @BindView(R.id.root_view)
    RelativeLayout root_view;
    @BindView(R.id.webview)
    X5WebView mWebView;
    @BindView(R.id.m_view_loading)
    View mViewLoading;

    private String webUrl;
    /**
     * 微信支付结果的url
     */
    private String payResultUrl;

    public ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;
    public final static int FILECHOOSER_RESULTCODE = 1;
    public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;

    public static void invoke(Context context, String url){
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    /**
     * 点击push消息进来
     * @param context
     * @param map
     */

    public static void invokePush(Context context, Map<String, String> map){
        if(StringUtil.isEmpty(HttpUrl.getCookies(context))){
            LoginActivity.invokePush(context, map);
            return;
        }

        if(null == HomeActivity.homeInstance){
            HomeActivity.invokePush(context, map);
            return;
        }

        if(null == map || map.size() <= 0 || !map.containsKey(UrlInvokeUtil.KEY_WEB)){
            return;
        }

        Intent intent = new Intent(context, WebViewActivity.class);
        if(!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            JjLog.e("不是activity");
        }
        intent.putExtra("url", map.get(UrlInvokeUtil.KEY_WEB));
        context.startActivity(intent);
        map.clear();
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_webview_layout;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        if(null != getIntent()){
            webUrl = getIntent().getStringExtra("url");
        }
        mViewLoading.setVisibility(View.VISIBLE);
        setListener();
    }

    @Override
    protected void initData() {
        String ua = mWebView.getSettings().getUserAgentString();
        mWebView.getSettings().setUserAgentString(ua + " YT-Android/" + JJShopApplication.sVersion + " YT-LOGIN-TOKEN:99yanxuanapp");
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");

        CookieManager.getInstance().setCookie(
                HttpUrl.WEB_COOKIE_KEY, PreUtils.getString(this, PreUtils.USER_COOKIE_ID));

        CookieManager.getInstance().setCookie(
                HttpUrl.WEB_COOKIE_KEY, PreUtils.getString(this, PreUtils.USER_COOKIE_MOBILE));

        CookieManager.getInstance().setCookie(
                HttpUrl.WEB_COOKIE_KEY, PreUtils.getString(this, PreUtils.DEVICE));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager.createInstance(this).sync();
        }
        mWebView.addJavascriptInterface(this, "gcw");
        mWebView.loadUrl(webUrl);

    }

    /*
     * call javascript
     */
    public void runJs(String method, String... params) {
        StringBuffer jsBuffer = new StringBuffer();
        jsBuffer.append("javascript:");
        jsBuffer.append(method);
        jsBuffer.append("(");
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                if (i == 0) {
                    jsBuffer.append("'" + params[i] + "'");
                } else {
                    jsBuffer.append(",'" + params[i] + "'");
                }
            }
        }
        jsBuffer.append(")");
        mWebView.loadUrl(jsBuffer.toString());
    }


    protected void setListener() {

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                JjLog.e("onPageStarted = " + s);
                mViewLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                JjLog.e("onPageFinished = " + s);
                mViewLoading.setVisibility(View.GONE);
//                writeData();
            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {

            }

            /**
             * 防止加载网页时调起系统浏览器
             */
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                JjLog.e("shouldOverrideUrlLoading = " + url);
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int progress) {
                if (progress == 100) {
                    mViewLoading.setVisibility(View.GONE);
                }
            }

            //扩展浏览器上传文件
            //3.0++版本
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                openFileChooserImpl(uploadMsg);
            }

            //3.0--版本
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooserImpl(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooserImpl(uploadMsg);
            }

            // For Android > 5.0
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, WebChromeClient.FileChooserParams fileChooserParams) {
                openFileChooserImplForAndroid5(uploadMsg);
                return true;
            }
        });
    }

    @JavascriptInterface
    public void goBack() {// 返回
       finish();
    }

    @JavascriptInterface
    public void productShopCart(){// 跳转到购物车
        HomeActivity.invoke(this, HomeActivity.SELECT_CART);
    }

    @JavascriptInterface
    public void goUserIndex(){// 跳转到个人中心
        HomeActivity.invoke(this, HomeActivity.SELECT_PERSON);
    }

    @JavascriptInterface
    public void goSearchIndex(){// 跳转到搜索
        SearchActivity.invoke(this);
    }

    @JavascriptInterface
    public void goHomePage(){// 跳转到首页
        HomeActivity.invoke(this, HomeActivity.SELECT_HOME);
    }

    @JavascriptInterface
    public void call_weixin_app_pay(String json) {// 微信支付
        try {
            JSONObject jsonObject = new JSONObject(json);
            if(null == jsonObject){
                return;
            }
            if(jsonObject.has("pay_result_url")){
                payResultUrl = jsonObject.getString("pay_result_url");
            }
            WXUtil.getWxUtil().pay(this, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void productShopShare(String json) {// 分享-包括 二维码、复制图片
        ShareUtils.build().shareDialog(this, json, true, umShareListener);
    }

    @JavascriptInterface
    public void productShopShareNoImg(String json) {// 分享-不包括 二维码、复制图片
        ShareUtils.build().shareDialog(this, json, false, umShareListener);
    }

    OnJjShareCallBackAdapter umShareListener = new OnJjShareCallBackAdapter() {

        @Override
        public void onSuccess(SHARE_MEDIA share_media) {
            int type = ShareUtils.getType(share_media);
            String urls = "javascript:shareMailComplete('success','" + type + "','" + ShareUtils.build().getUrl() + "')";
            mWebView.loadUrl(urls);
        }

        @Override
        public void onError(SHARE_MEDIA share_media) {
            int type = ShareUtils.getType(share_media);
            runJs("shareError", new String[]{"error", "" + type, "0"});
        }
    };

    @JavascriptInterface
    public void addCartSuccess() {// 加入购物车成功
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 通知刷新购物车
                EventBus.getDefault().post(new JJEvent(JJEvent.REFRESH_SHOPPING_CART));
            }
        });
    }

    @JavascriptInterface
    public void trainingWechatIdPaste(String wxNum){

        if (StringUtil.isEmpty(wxNum)){
            JjToast.getInstance().toast("复制微信号失败");
            return;
        }
        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(wxNum);
        JjToast.getInstance().toast("微信号已复制成功");
    }

    @JavascriptInterface
    public void invokeGoodsDetail(final String json) {// 跳转商品详情
        JjLog.e("json = " + json);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(StringUtil.isEmpty(json)){
                    return;
                }
                try {
                    JSONObject object = new JSONObject(json);
                    if(null == object){
                        return;
                    }
                    if(object.has("id") && object.has("id_code")){
                        DetailActivity.invoke(WebViewActivity.this,
                                object.getString("id_code"), object.getString("id"));
                    }
                    if(object.has("_fc")){
                        JJShopApplication.yxfc = object.getString("_fc");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        shareShowHideLoading(View.GONE);
    }

    public void shareShowHideLoading(int visible){
        if(!(View.GONE == visible || View.VISIBLE == visible) || null == mViewLoading){
            return;
        }
        mViewLoading.setVisibility(visible);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }
        UMShareAPI.get(WebViewActivity.this).onActivityResult(requestCode, resultCode, intent);
    }

    @Subscribe
    public void onEvent(JJEvent event){
        if(null == event){
            return;
        }
        int id = event.getEventId();
        if(id == JJEvent.PAY_SUCCESS || id == JJEvent.PAY_CANCEL || id == JJEvent.PAY_FAIL){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(payResultUrl)) {
                        mWebView.loadUrl(payResultUrl);
                    }
                }
            });
        }else if(id == JJEvent.FINISH_ACTIVITY){
            finish();
        }
    }

    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
    }

    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        mUploadMessageForAndroid5 = uploadMsg;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }

    @Override
    public void onDestroy() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();

        EventBus.getDefault().unregister(this);
        if(null != root_view){
            root_view.removeAllViews();
        }
        try {
            if(null != mWebView){
                if(null != root_view){
                    root_view.removeView(mWebView);
                }
                mWebView.removeAllViews();
                mWebView.destroy();
                mWebView = null;
            }
        }catch (Exception e){

        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(null != mWebView && mWebView.canGoBack()){
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }
}


