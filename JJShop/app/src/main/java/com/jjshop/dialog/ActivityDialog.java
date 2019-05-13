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
import android.widget.RelativeLayout;

import com.jjshop.R;
import com.jjshop.utils.UIUtils;
import com.jjshop.utils.UrlInvokeUtil;
import com.jjshop.utils.glide_img.GlideUtil;

import java.util.Map;

public class ActivityDialog extends AppCompatDialogFragment implements View.OnClickListener{
	private static volatile ActivityDialog commonDialog;
	public static ActivityDialog build(){
		synchronized (ActivityDialog.class){
			commonDialog = new ActivityDialog();
		}
		return commonDialog;
	}

	private Context mContext;
	private ImageView mIvColse, mIvActivityImg;
	private String mInvokeUrl = "", mImgUrl = "";


	public ActivityDialog showView(FragmentManager manager, String img, String url){
		Bundle bundle = new Bundle();
		bundle.putString("img", img);
		bundle.putString("url", url);
		this.setArguments(bundle);
		// 解决 Can not perform this action after onSaveInstanceState 这个异常
		FragmentTransaction ft = manager.beginTransaction();
		ft.add(commonDialog, "activityDialog");
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
		dialog.setContentView(R.layout.dialog_activity_layout);
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

	private void init(Dialog dialog){
		if(null != getArguments()){
			mImgUrl = getArguments().getString("img");
			mInvokeUrl = getArguments().getString("url");
		}
		mIvColse = dialog.findViewById(R.id.m_iv_close);
		mIvActivityImg = dialog.findViewById(R.id.m_iv_activity_img);
		// 设置点击事件
		mIvColse.setOnClickListener(this);
		mIvActivityImg.setOnClickListener(this);
		// 设置图片宽高
		int w = UIUtils.getWidth();
		int margin = (int) (w * 0.14 * 2);
		w = w - margin;
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIvActivityImg.getLayoutParams();
		params.width = w;
		params.height = w * 54 / 50;
		mIvActivityImg.setLayoutParams(params);
		// 加载图片
		GlideUtil.getInstence().loadRoundImage(mContext, mIvActivityImg, mImgUrl);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.m_iv_close:
				dismiss();
				break;
			case R.id.m_iv_activity_img:
				if(null == mContext){
					return;
				}
				Map<String, String> map = UrlInvokeUtil.build().pushData(mInvokeUrl);
				UrlInvokeUtil.build().pushInvoke(mContext, map);
				dismiss();
				break;
		}
	}
}
