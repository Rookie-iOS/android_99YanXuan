package com.jjshop.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class X5WebView extends WebView {

	private Context mContext;

	public X5WebView(Context context) {
		super(context);
		mContext = context;
		// this.setWebChromeClient(chromeClient);
		// WebStorage webStorage = WebStorage.getInstance();
		initWebViewSettings();
		this.setClickable(true);
	}

	public X5WebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		// this.setWebChromeClient(chromeClient);
		// WebStorage webStorage = WebStorage.getInstance();
		initWebViewSettings();
		this.setClickable(true);
	}

	public X5WebView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		// this.setWebChromeClient(chromeClient);
		// WebStorage webStorage = WebStorage.getInstance();
		initWebViewSettings();
		this.setClickable(true);
	}

	private void initWebViewSettings() {
		WebSettings webSetting = this.getSettings();
		webSetting.setJavaScriptEnabled(true);
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(true);

		webSetting.setBuiltInZoomControls(true);
		webSetting.setDisplayZoomControls(true);

		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(true);
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(false);
		webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		webSetting.setLoadsImagesAutomatically(true);
		webSetting.setBlockNetworkImage(false);
		webSetting.setAppCacheMaxSize(1024*1024*8);
		String appCachePath = mContext.getCacheDir().getAbsolutePath();
		webSetting.setAppCachePath(appCachePath);
		webSetting.setAllowFileAccess(true);    // 可以读取文件缓存
		webSetting.setAppCacheEnabled(true);    //开启H5(APPCache)缓存功能
		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		// webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
			webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);//MIXED_CONTENT_ALWAYS_ALLAW

		// 抓包调试
//		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//			this.setWebContentsDebuggingEnabled(true);
//		}
		// this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
		// settings 的设计
	}

}
