package com.jjshop.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.bean.KoulingBean;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.UrlInvokeUtil;
import com.jjshop.utils.glide_img.GlideUtil;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

import java.util.Map;

public class KoulingDialog extends AppCompatDialogFragment implements View.OnClickListener{
	private static volatile KoulingDialog commonDialog;
	public static KoulingDialog build(){
		synchronized (KoulingDialog.class){
			commonDialog = new KoulingDialog();
		}
		return commonDialog;
	}

	private Context mContext;
	private TextView tvName, tvLook, tvTitle, tvError;
	private ImageView ivImg, ivClose;
	private View layoutLoading;
	private String invokeUrl = "", kouling = "";


	public KoulingDialog showView(FragmentManager manager, String kouling){
		Bundle bundle = new Bundle();
		bundle.putString("kouling", kouling);
		this.setArguments(bundle);
		// 解决 Can not perform this action after onSaveInstanceState 这个异常
		FragmentTransaction ft = manager.beginTransaction();
		ft.add(commonDialog, "koulingDialog");
		ft.commitAllowingStateLoss();
		return commonDialog;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getContext(), R.style.bottomDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_kouling_layout);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		setCancelable(false);
		// 设置宽度为屏宽, 靠近屏幕底部。
		Window window = dialog.getWindow();
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
		window.setAttributes(lp);
		init(dialog);

		return dialog;
	}

	private void init(Dialog view){
		tvTitle = view.findViewById(R.id.m_tv_title);
		tvName = view.findViewById(R.id.m_tv_name);
		ivImg = view.findViewById(R.id.m_iv_img);
		tvLook = view.findViewById(R.id.m_tv_look);
		ivClose = view.findViewById(R.id.m_iv_close);
		layoutLoading = view.findViewById(R.id.m_layout_loading);
		tvError = view.findViewById(R.id.m_tv_error);
		// 设置点击事件
		ivClose.setOnClickListener(this);
		tvLook.setOnClickListener(this);

		if(null != getArguments()){
			kouling = getArguments().getString("kouling");
			// 加载口令所包含的内容
			loadKoulingData(PreUtils.getString(mContext, PreUtils.SHOP_ID), kouling);
		}

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.m_iv_close:
				dismiss();
				break;
			case R.id.m_tv_look:
				if(null == mContext){
					return;
				}
				if(tvLook.getText().toString().equals("点击重试")){
					loadKoulingData(PreUtils.getString(mContext, PreUtils.SHOP_ID), kouling);
					return;
				}
				if(StringUtil.isEmpty(invokeUrl)){
					return;
				}
				Map<String, String> map = UrlInvokeUtil.build().pushData(invokeUrl);
				UrlInvokeUtil.build().pushInvoke(mContext, map);
				dismiss();
				break;
		}
	}

	/**
	 * 加载口令包含的内容
	 * @param shopId
	 * @param kouling
	 */
	public void loadKoulingData(final String shopId, final String kouling){
		if(null == mContext || StringUtil.isEmpty(shopId) || StringUtil.isEmpty(kouling)){
			dismiss();
			return;
		}
		if(null == tvTitle || null == tvLook || null == tvError || null == layoutLoading){
			dismiss();
			return;
		}
		tvTitle.setText("检测到99口令");
		tvLook.setText("数据加载中，请稍后···");
		tvLook.setEnabled(false);
		tvLook.setClickable(false);
		layoutLoading.setVisibility(View.VISIBLE);
		tvError.setVisibility(View.GONE);
		JjLog.e("剪切板的口令 = " + kouling);
		HttpHelper.bulid().getRequest(HttpUrl.build().getKouling(shopId, kouling), KoulingBean.class,
				new OnRequestCallBack<KoulingBean>() {
					@Override
					public void onSuccess(KoulingBean data) {
						if(null == tvTitle || null == tvLook || null == ivClose || null == ivImg
								|| null == tvName || null == layoutLoading){
							dismiss();
							return;
						}
						KoulingBean.KouLingBeanData bean = data.getData();
						if(null == bean){
							dismiss();
							return;
						}
						layoutLoading.setVisibility(View.GONE);
						ivClose.setVisibility(View.VISIBLE);
						tvTitle.setText("99口令");
						tvLook.setText("立即查看");
						tvLook.setEnabled(true);
						tvLook.setClickable(true);
						// 显示图片
						GlideUtil.getInstence().loadImage(mContext, ivImg, bean.getImg());
						// 显示标题
						tvName.setText(bean.getTitle());
						// 跳转url
						invokeUrl = bean.getUrl();
					}

					@Override
					public void onError(String msg) {
						if(null == tvLook || null == ivClose || null == layoutLoading || null == tvError){
							dismiss();
							return;
						}
						ivClose.setVisibility(View.VISIBLE);
						tvLook.setText("点击重试");
						tvLook.setClickable(true);
						tvLook.setEnabled(true);
						tvError.setVisibility(View.VISIBLE);
						layoutLoading.setVisibility(View.GONE);

					}
				});
	}
}
