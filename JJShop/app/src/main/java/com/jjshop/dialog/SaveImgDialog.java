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

import com.jjshop.R;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.PermissionUtil;
import com.jjshop.utils.StringUtil;

public class SaveImgDialog extends AppCompatDialogFragment{

	private static volatile SaveImgDialog commonDialog;
	public static SaveImgDialog build(){
		synchronized (SaveImgDialog.class){
			commonDialog = new SaveImgDialog();
		}
		return commonDialog;
	}

	private Context mContext;
	private String mImgUrl = "";

	public SaveImgDialog showView(FragmentManager manager, String img){
		Bundle bundle = new Bundle();
		bundle.putString("img", img);
		this.setArguments(bundle);
		// 解决 Can not perform this action after onSaveInstanceState 这个异常
		FragmentTransaction ft = manager.beginTransaction();
		ft.add(commonDialog, "saveImgDialog");
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
		dialog.setContentView(R.layout.dialog_save_img);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		setCancelable(false);
		// 设置宽度为屏宽, 靠近屏幕底部。
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.gravity = Gravity.BOTTOM;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
		window.setAttributes(lp);
		init(dialog);

		return dialog;
	}

	private void init(Dialog dialog){
		if(null != getArguments()){
			mImgUrl = getArguments().getString("img");
		}else {
			dismiss();
		}

		dialog.findViewById(R.id.m_tv_save).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// 判断是否有权限
				if(!PermissionUtil.build().checkPermission(mContext, PermissionUtil.WRITE_EXTERNAL_STORAGE)){
					return;
				}
				if(!PermissionUtil.build().checkPermission(mContext, PermissionUtil.READ_EXTERNAL_STORAGE)){
					return;
				}
				dismiss();
				if(null == mContext || StringUtil.isEmpty(mImgUrl)){
					return;
				}
				CommonUtils.build().downLoadImgToAlbum(mContext, mImgUrl, null);
			}
		});
		dialog.findViewById(R.id.m_tv_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
		dialog.findViewById(R.id.view_bg).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
	}

}
