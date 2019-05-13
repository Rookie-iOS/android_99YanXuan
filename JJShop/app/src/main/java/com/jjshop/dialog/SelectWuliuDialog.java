package com.jjshop.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.contrarywind.view.WheelView;
import com.jjshop.R;
import com.jjshop.bean.ExpressBean;
import com.jjshop.ui.activity.person.RefundDetailActivity;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.Tools;

import java.io.Serializable;
import java.util.List;

public class SelectWuliuDialog extends AppCompatDialogFragment implements View.OnClickListener{
	private static volatile SelectWuliuDialog commonDialog;
	public static SelectWuliuDialog build(){
		synchronized (SelectWuliuDialog.class){
			commonDialog = new SelectWuliuDialog();
		}
		return commonDialog;
	}
	private Context mContext;
	private OptionsPickerView mPickerView;
	private List<ExpressBean> mPickerData;
	private String mSellectId = "";

	private RelativeLayout mLayoutWuliu;
	private TextView mTvWuliu, mTvSubmit;
	private EditText mEtContent;
	private View mViewtop;


	public SelectWuliuDialog showView(FragmentManager manager, List<ExpressBean> list){
		Bundle bundle = new Bundle();
		bundle.putSerializable("list", (Serializable) list);
		this.setArguments(bundle);
		// 解决 Can not perform this action after onSaveInstanceState 这个异常
		FragmentTransaction ft = manager.beginTransaction();
		ft.add(commonDialog, "sellectWuliu");
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
		dialog.setContentView(R.layout.dialog_select_wuliu_layout);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		setCancelable(false);
		// 设置宽度为屏宽, 靠近屏幕底部。
		Window window = dialog.getWindow();
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.gravity = Gravity.BOTTOM;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
		window.setAttributes(lp);
		init(dialog);

		return dialog;
	}

	private void init(Dialog dialog){
		if(null != getArguments()){
			mPickerData = (List<ExpressBean>) getArguments().getSerializable("list");
		}
		mLayoutWuliu = dialog.findViewById(R.id.m_layout_wuliu);
		mTvWuliu = dialog.findViewById(R.id.m_tv_wuliu);
		mEtContent = dialog.findViewById(R.id.m_et_order_num);
		mTvSubmit = dialog.findViewById(R.id.m_tv_submit);
		mViewtop = dialog.findViewById(R.id.m_view_top);

		mLayoutWuliu.setOnClickListener(this);
		mTvSubmit.setOnClickListener(this);
		mViewtop.setOnClickListener(this);

	}

	private void showSelectData(){
		if(null == mPickerData || mPickerData.size() <= 0 || null == mContext){
			return;
		}
		if(null != mPickerView){
			mPickerView.show();
			return;
		}
		mPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
			@Override
			public void onOptionsSelect(int options1, int options2, int options3, View v) {
				ExpressBean express = mPickerData.get(options1);
				if(null != express && null != mTvWuliu){
					mSellectId = express.getXiaoyatong_code();
					mTvWuliu.setText(express.getName());
				}
			}
		}).setLayoutRes(R.layout.pickerview_tui_layout, new CustomListener() {
			@Override
			public void customLayout(View v) {
				//自定义布局中的控件初始化及事件处理
				final TextView tvTitle = v.findViewById(R.id.m_tv_select_title);
				final TextView tvSubmit = v.findViewById(R.id.m_tv_select_sure);
				tvTitle.setText("请选择物流公司");
				tvSubmit.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mPickerView.returnData();
						mPickerView.dismiss();
					}
				});
			}
		})
				.setDividerColor(Color.RED)
				.setTextColorCenter(Color.BLACK) //设置选中项文字颜色
				.setDividerType(WheelView.DividerType.WRAP)
				.setContentTextSize(20)
				.isDialog(true)
				.build();
		mPickerView.setPicker(mPickerData);//添加数据
		mPickerView.show();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.m_view_top:
				Tools.hideSoftInput(view);
				dismiss();
				break;
			case R.id.m_layout_wuliu:
				Tools.hideSoftInput(view);
				showSelectData();
				break;
			case R.id.m_tv_submit:
				Tools.hideSoftInput(view);
				if(StringUtil.isEmpty(mSellectId) || null == mEtContent){
					JjToast.getInstance().toast("请选择快递公司");
					return;
				}
				String danhao = mEtContent.getText().toString().trim();
				if(StringUtil.isEmpty(danhao)){
					JjToast.getInstance().toast("请选择物流单号");
					return;
				}
				if(null != mContext && mContext instanceof RefundDetailActivity){
					((RefundDetailActivity)mContext).loadSubmitData(mSellectId, danhao);
				}
				dismiss();
				break;
		}
	}
}
