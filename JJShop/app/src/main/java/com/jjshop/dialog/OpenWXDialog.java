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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.listener.OnCommonCallBackIm;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.WXUtil;

import java.io.Serializable;
import java.util.List;

public class OpenWXDialog extends AppCompatDialogFragment implements View.OnClickListener{
	private static volatile OpenWXDialog commonDialog;
	public static OpenWXDialog build(){
		synchronized (OpenWXDialog.class){
			commonDialog = new OpenWXDialog();
		}
		return commonDialog;
	}

	private Context mContext;
	private RelativeLayout mLayoutDownload;
	private LinearLayout mLayoutFinish;
	private TextView mTvDownload;
	private List<String> shareImgs;

	public OpenWXDialog showView(FragmentManager manager, List<String> shareImgs){
		Bundle bundle = new Bundle();
		bundle.putSerializable("list", (Serializable) shareImgs);
		this.setArguments(bundle);
		// 解决 Can not perform this action after onSaveInstanceState 这个异常
		FragmentTransaction ft = manager.beginTransaction();
		ft.add(commonDialog, "openwxDialog");
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
		dialog.setContentView(R.layout.dialog_open_wx_layout);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		setCancelable(false);
		// 设置宽度为屏宽, 靠近屏幕底部。
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
		window.setAttributes(lp);
		init(dialog);

		return dialog;
	}

	private void init(Dialog dialog){
		mLayoutDownload = dialog.findViewById(R.id.m_layout_download);
		mLayoutFinish = dialog.findViewById(R.id.m_layout_finish);
		mTvDownload = dialog.findViewById(R.id.m_tv_download);
		dialog.findViewById(R.id.m_tv_openwx).setOnClickListener(this);
		dialog.findViewById(R.id.m_tv_cancel).setOnClickListener(this);
		if(null != getArguments()){
			shareImgs = (List<String>) getArguments().getSerializable("list");
			downLoadImg(mContext, shareImgs, 0);
		}else {
			dismiss();
		}
	}

	public void downloadImgNum(int position, int allNum){
		if(null != mTvDownload){
			position = position + 1;
			mTvDownload.setText("正在下载 （ " + position + " / " + allNum + " ）");
		}

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.m_tv_openwx:
				WXUtil.getWxUtil().invokeWX(mContext);
				dismiss();
				break;
			case R.id.m_tv_cancel:
				dismiss();
				break;
		}
	}

	// 下载图片
	private void downLoadImg(final Context activity, final List<String> shareImgs, final int position){
		if(null == shareImgs){
			return;
		}
		final int size = shareImgs.size();
		if(shareImgs.size() <= 0 || shareImgs.size() <= position){
			return;
		}
		downloadImgNum(position, size);
		CommonUtils.build().downLoadImgToAlbum(activity, shareImgs.get(position), new OnCommonCallBackIm() {
			@Override
			public void onSuccess(Object o) {
				if(null == o || !(o instanceof Boolean)){
					return;
				}
				boolean b = (boolean) o;
				if(b){
					if(position == size - 1){
						mLayoutDownload.setVisibility(View.GONE);
						mLayoutFinish.setVisibility(View.VISIBLE);
						return;
					}
					downLoadImg(activity, shareImgs, position + 1);
				}
			}

			@Override
			public void onError(String msg) {
				mLayoutDownload.setVisibility(View.GONE);
				mLayoutFinish.setVisibility(View.VISIBLE);
			}
		});
	}
}
