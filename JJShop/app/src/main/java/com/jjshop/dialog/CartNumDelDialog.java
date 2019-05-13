package com.jjshop.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.Tools;
import com.jjshop.utils.UIUtils;

public class CartNumDelDialog extends AppCompatDialogFragment implements View.OnClickListener{
	public static final int SURE_CODE = 101;
	public static final int CANCEL_CODE = 102;
	public static final int DEL_GOODS = 103;// 删除商品
	public static final int EXIT_CODE = 104;// 退出登录
	public static final int TONGZHI_CODE = 105;// 通知权限
	public static final int CANCEL_ORDER_CODE = 106;// 取消订单、确定收货
	private Context mContext;
	private static volatile CartNumDelDialog commonDialog;
	public static CartNumDelDialog build(){
		synchronized (CartNumDelDialog.class){
			commonDialog = new CartNumDelDialog();
		}
		return commonDialog;
	}

	private OnCommonClickCalllBack calllBack;
	private int del;
	private String msg;

	public CartNumDelDialog showView(FragmentManager manager, int num){
		return showView(manager, num, -1);
	}
	public CartNumDelDialog showView(FragmentManager manager, int num, int del){
		return showView(manager, num, del, "");
	}
	public CartNumDelDialog showView(FragmentManager manager, int num, int del, String msg){
		Bundle bundle = new Bundle();
		bundle.putInt("num", num);
		bundle.putInt("del", del);
		bundle.putString("msg", msg);
		this.setArguments(bundle);
		// 解决 Can not perform this action after onSaveInstanceState 这个异常
		FragmentTransaction ft = manager.beginTransaction();
		ft.add(commonDialog, "updateCartNum");
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
		dialog.setContentView(R.layout.dialog_cart_num_del_layout);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		setCancelable(false);

		Bundle bundle = getArguments();
		int num = 1;
		if (bundle !=null){
			num = bundle.getInt("num");
			del = bundle.getInt("del");
			msg = bundle.getString("msg");
		}
		initView(dialog, num);

		// 设置宽度为屏宽, 靠近屏幕底部。
		Window window = dialog.getWindow();
		if(del != DEL_GOODS && del != EXIT_CODE && del != TONGZHI_CODE && del != CANCEL_ORDER_CODE){
			window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		}
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
		window.setAttributes(lp);

		return dialog;
	}

	EditText editText;
	private void initView(Dialog view, int num){
		TextView tvTitle = view.findViewById(R.id.tv_title);
		TextView tvExit = view.findViewById(R.id.m_tv_exit);
		editText = view.findViewById(R.id.m_edit_num);
		TextView tvSure = view.findViewById(R.id.m_tv_sure);
		TextView tvCancel = view.findViewById(R.id.m_tv_cancel);

		tvCancel.setOnClickListener(this);
		tvSure.setOnClickListener(this);
		int margin = UIUtils.dip2px(20);
		if(del == DEL_GOODS){
			editText.setVisibility(View.GONE);
			tvTitle.setText("确认删除这个商品");
			tvTitle.setPadding(0, margin, 0, margin);
			return;
		}
		if(del == EXIT_CODE){
			editText.setVisibility(View.GONE);
			tvTitle.setText("提示");
			tvTitle.setPadding(0, margin, 0, 0);
			tvExit.setVisibility(View.VISIBLE);
			return;
		}
		if(del == TONGZHI_CODE){
			editText.setVisibility(View.GONE);
			tvTitle.setText(msg == null ? "" : msg);
			tvTitle.setPadding(margin, margin, margin, margin);
			tvSure.setText("去开启");
			return;
		}
		if(del == CANCEL_ORDER_CODE){
			editText.setVisibility(View.GONE);
			tvTitle.setText("提示");
			tvTitle.setPadding(0, margin, 0, 0);
			tvExit.setVisibility(View.VISIBLE);
			tvExit.setText("更改订单，您确认进行此操作？");
			return;
		}
		String strNum = String.valueOf(num);
		editText.setText(strNum);
		editText.setSelection(strNum.length());
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.m_tv_sure:
				if(null != calllBack){
					if(del == DEL_GOODS){// 删除
						calllBack.callBack(DEL_GOODS, 0);
						dismiss();
						return;
					}
					if(del == EXIT_CODE){// 退出
						calllBack.callBack(EXIT_CODE, 0);
						dismiss();
						return;
					}
					if(del == TONGZHI_CODE){// 通知权限
						calllBack.callBack(TONGZHI_CODE, 0);
						dismiss();
						return;
					}
					if(del == CANCEL_ORDER_CODE){// 取消订单
						calllBack.callBack(CANCEL_ORDER_CODE, 0);
						dismiss();
						return;
					}
					int num;
					if(null == editText){
						return;
					}
					if(StringUtil.isEmpty(editText.getText().toString())){
						JjToast.getInstance().toast("数量最少为1");
						editText.setText("1");
						return;
					}
					num = Integer.parseInt(editText.getText().toString());
					Tools.hideSoftInput(view);
					calllBack.callBack(SURE_CODE, num);
					dismiss();
				}
				break;
			case R.id.m_tv_cancel:
				Tools.hideSoftInput(view);
				if(null != calllBack){
					calllBack.callBack(CANCEL_CODE, -1);
				}
				dismiss();
				break;
		}
	}
	public void setOnCommonClickCalllBack(OnCommonClickCalllBack calllBack){
		this.calllBack = calllBack;
	}
	public interface OnCommonClickCalllBack{
		void callBack(int typeCode, int num);
	}
}
