package com.jjshop.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.adapter.CommentRecycleAdapter;
import com.jjshop.template_view.TemplateUtil;
import com.jjshop.utils.UIUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;

public class DiscountQuanDialog extends AppCompatDialogFragment{
	private Context mContext;
	private static volatile DiscountQuanDialog commonDialog;
	public static DiscountQuanDialog build(){
		synchronized (DiscountQuanDialog.class){
			commonDialog = new DiscountQuanDialog();
		}
		return commonDialog;
	}

	private SwipeMenuRecyclerView recyclerView;
	private TextView tvClose;
	private ArrayList<Object> listData;

	public DiscountQuanDialog showView(FragmentManager manager, ArrayList<Object> list){
		Bundle bundle = new Bundle();
		bundle.putSerializable("quanList", list);
		this.setArguments(bundle);
		// 解决 Can not perform this action after onSaveInstanceState 这个异常
		FragmentTransaction ft = manager.beginTransaction();
		ft.add(commonDialog, "discountQuan");
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
		dialog.setContentView(R.layout.dialog_discount_quan_layout);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		setCancelable(false);

		Bundle bundle = getArguments();
		if (bundle != null){
			listData = (ArrayList<Object>) bundle.getSerializable("quanList");
		}
		initView(dialog);

		// 设置宽度为屏宽, 靠近屏幕底部。
		Window window = dialog.getWindow();
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.gravity = Gravity.BOTTOM;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
		lp.height = UIUtils.getHeight() / 10 * 7; // 宽度持平
		window.setAttributes(lp);

		return dialog;
	}

	private void initView(Dialog view){
		if(null == listData || listData.size() <= 0){
			return;
		}
		recyclerView = view.findViewById(R.id.m_recycleview);
		tvClose = view.findViewById(R.id.m_tv_close);

		tvClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
		recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
		recyclerView.useDefaultLoadMore();

		CommentRecycleAdapter adapter = new CommentRecycleAdapter(mContext, listData, TemplateUtil.TEMPLATE_1004);
		recyclerView.setAdapter(adapter);

	}
}
