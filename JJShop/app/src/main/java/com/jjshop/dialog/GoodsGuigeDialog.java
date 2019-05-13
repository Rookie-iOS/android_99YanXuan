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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.bean.DetailBean;
import com.jjshop.ui.activity.home.DetailActivity;
import com.jjshop.ui.activity.home.ShowBigImgActivity;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.Tools;
import com.jjshop.utils.UIUtils;
import com.jjshop.utils.glide_img.GlideUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GoodsGuigeDialog extends AppCompatDialogFragment implements View.OnClickListener{
	private Context mContext;
	private static volatile GoodsGuigeDialog commonDialog;
	public static GoodsGuigeDialog build(){
		synchronized (GoodsGuigeDialog.class){
			commonDialog = new GoodsGuigeDialog();
		}
		return commonDialog;
	}

	private ArrayList<Object> listColorData;
	private ArrayList<Object> listSizeData;
	private TagFlowLayout colorFlow;
	private TagFlowLayout sizeFlow;
	private TextView tvSize;
	private TextView tvColor;
	private TextView tvPrice;
	private TextView tvGuige;
	private EditText tvNum;
	private ScrollView scrollView;
	private TextView mTvXianBuy;
	private LinearLayout mLayoutBottomDjs, mLayouBottom;
	private TextView mTvBottomDjs;
	private TextView mTvBottomDjsTishi;

	// 颜色名称、尺寸名称
	private String mColorName = "", mSizeName = "";
	// 颜色id、尺寸id
	private int mColorId;
	private String mSizeId = "";
	private int mColorPosition = 0, mSizePosition = 0;
	private boolean isSelectColor = false, isSelectSize = false;
	// 图片地址
	private String mImgUrl = "";
	// 库存
	private int stocks = 1;
	// 限购
	private DetailBean.LimitBuyData limitBuyData;
	private int limitBuyNum = -1;
	private long previewData = 0;

	public GoodsGuigeDialog showView(FragmentManager manager, ArrayList<DetailBean.DataBeanX.DataBean.SpecData> list,
									 int colorPosition, int sizePosition, DetailBean.LimitBuyData limitBuyData,
									 long isPreviewData){
		Bundle bundle = new Bundle();
		bundle.putSerializable("guige", list);
		bundle.putInt("colorPosition", colorPosition);
		bundle.putInt("sizePosition", sizePosition);
		bundle.putSerializable("limitBuyData", limitBuyData);
		bundle.putDouble("previewData", isPreviewData);
		this.setArguments(bundle);
		// 解决 Can not perform this action after onSaveInstanceState 这个异常
		FragmentTransaction ft = manager.beginTransaction();
		ft.add(commonDialog, "goodsGuige");
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
		Window window = dialog.getWindow();
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		dialog.setContentView(R.layout.dialog_goods_guige_layout);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);

		Bundle bundle = getArguments();
		if (bundle != null){
			listColorData = new ArrayList<>();
			listSizeData = new ArrayList<>();
			ArrayList<DetailBean.DataBeanX.DataBean.SpecData> list = (ArrayList<DetailBean.DataBeanX.DataBean.SpecData>) bundle.getSerializable("guige");
			listColorData.addAll(list);
			mColorPosition = bundle.getInt("colorPosition", 0);
			mSizePosition = bundle.getInt("sizePosition", 0);
			limitBuyData = (DetailBean.LimitBuyData) bundle.getSerializable("limitBuyData");
			previewData = (long) bundle.getDouble("previewData");
		}
		initView(dialog);

		// 设置宽度为屏宽, 靠近屏幕底部。
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.gravity = Gravity.BOTTOM;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
		lp.height = UIUtils.getHeight() / 10 * 7; // 宽度持平
		window.setAttributes(lp);

		return dialog;
	}

	private void initView(Dialog view){
		final ImageView ivGoods = view.findViewById(R.id.m_iv_goods);
		scrollView = view.findViewById(R.id.m_layout_scroll);
		tvPrice = view.findViewById(R.id.m_tv_price);
		tvGuige = view.findViewById(R.id.m_tv_select_guige);
		colorFlow = view.findViewById(R.id.m_color_flow);
		tvColor = view.findViewById(R.id.m_tv_color);
		sizeFlow = view.findViewById(R.id.m_size_flow);
		tvSize = view.findViewById(R.id.m_tv_size);

		ImageView ivDel = view.findViewById(R.id.m_tv_del);
		tvNum = view.findViewById(R.id.m_tv_num);
		ImageView ivAdd = view.findViewById(R.id.m_tv_add);
		mTvXianBuy = view.findViewById(R.id.m_tv_xian_buy);
		setCursorVisible(false);

		// 未开始时间倒计时
		mLayoutBottomDjs = view.findViewById(R.id.m_ll_bottom_djs);
		mTvBottomDjs = view.findViewById(R.id.m_tv_bottom_djs);
		mTvBottomDjsTishi = view.findViewById(R.id.m_tv_bottom_djs_tishi);
		mLayoutBottomDjs.setOnClickListener(this);

		TextView tvAddCart = view.findViewById(R.id.m_tv_add_cart);
		TextView tvBuy = view.findViewById(R.id.m_tv_buy);
		mLayouBottom = view.findViewById(R.id.layout_bottom);

		ImageView ivClose = view.findViewById(R.id.m_iv_close);

		ivDel.setOnClickListener(this);
		tvNum.setOnClickListener(this);
		ivAdd.setOnClickListener(this);
		tvAddCart.setOnClickListener(this);
		tvBuy.setOnClickListener(this);
		ivClose.setOnClickListener(this);

		setLayoutDjs(previewData);

		if(null != limitBuyData){
			limitBuyNum = limitBuyData.getLimit_num();
			if(limitBuyNum >= 1){
				mTvXianBuy.setVisibility(View.VISIBLE);
				mTvXianBuy.setText("每人限购"+limitBuyNum+"件");
			}
		}

		// 图片点击监听
		ivGoods.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ShowBigImgActivity.invoke(mContext, mImgUrl);
			}
		});

		// 第一次进来默认选中一个
		if(null != listColorData && listColorData.size() > mColorPosition && mColorPosition >= 0){
			Object o = listColorData.get(mColorPosition);
			if(o != null && o instanceof DetailBean.DataBeanX.DataBean.SpecData){
				DetailBean.DataBeanX.DataBean.SpecData data = (DetailBean.DataBeanX.DataBean.SpecData) o;
				tvColor.setText(data.getAttr_name());
				mColorName = data.getName();
				mColorId = data.getItem_id();
				mImgUrl = data.getImg();
				GlideUtil.getInstence().loadImageNoFix(mContext, ivGoods, data.getImg());
				listSizeData.clear();
				listSizeData.addAll(data.getList());
				if(getStocks(data.getList()) <= 0){// 设置选中的这个库存为0时，则循环拿到其他库存数大于0的，设置选中
					mColorPosition = -1;
					mColorName = "";
					mColorId = -1;
					mSizePosition = -1;
					for(int i =0; i < listColorData.size(); i++){
						o = listColorData.get(i);
						if(o != null && o instanceof DetailBean.DataBeanX.DataBean.SpecData){
							data = (DetailBean.DataBeanX.DataBean.SpecData) o;
							if(getStocks(data.getList()) > 0){
								mColorPosition = i;
								tvColor.setText(data.getAttr_name());
								mColorName = data.getName();
								mColorId = data.getItem_id();
								mImgUrl = data.getImg();
								GlideUtil.getInstence().loadImageNoFix(mContext, ivGoods, data.getImg());
								listSizeData.clear();
								listSizeData.addAll(data.getList());
								mSizePosition = 0;
								break;
							}
						}
					}
				}

				initColorFlow();
				initSizeFlow();
			}

		}

		// 颜色选中监听
		colorFlow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
			@Override
			public boolean onTagClick(View view, int position, FlowLayout parent) {
				if(null == listColorData || listColorData.size() <= 0){
					return true;
				}

				isSelectSize = false;
				isSelectColor = true;
				setCursorVisible(false);
				Object o = listColorData.get(position);
				if(o != null && o instanceof DetailBean.DataBeanX.DataBean.SpecData) {

					DetailBean.DataBeanX.DataBean.SpecData colorBean = (DetailBean.DataBeanX.DataBean.SpecData) o;
					if (null != colorBean) {

						if(getStocks(colorBean.getList()) <= 0){
							if(mColorPosition >= 0){
								colorFlow.getAdapter().setSelectedList(mColorPosition);
							}else{
								colorFlow.getAdapter().notifyDataChanged();
							}
							return true;
						}
//						if(mColorPosition == position){
////							return true;
////						}

						mImgUrl = colorBean.getImg();
						GlideUtil.getInstence().loadImageNoFix(mContext, ivGoods, colorBean.getImg());
						mColorId = colorBean.getItem_id();
						mColorName = colorBean.getName();
						mColorPosition = position;
						mSizePosition = 0;
						// 显示对应的尺寸数据
						listSizeData.clear();
						listSizeData.addAll(colorBean.getList());
						initSizeFlow();
					}
				}
				return true;
			}
		});


		colorFlow.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
			@Override
			public void onSelected(Set<Integer> selectPosSet) {

				if(mColorPosition == -1){
					return;
				}
				if(null == selectPosSet || selectPosSet.size() <= 0){
					colorFlow.getAdapter().setSelectedList(mColorPosition);
				}
			}
		});

		// 尺寸选中监听
		sizeFlow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
			@Override
			public boolean onTagClick(View view, int position, FlowLayout parent) {

				if(null == listSizeData || listSizeData.size() <= 0){
					return true;
				}

				isSelectSize = true;
				setCursorVisible(false);
				Object o = listSizeData.get(position);
				if(null != o && o instanceof DetailBean.DataBeanX.DataBean.SpecData.SpecSizeData){
					DetailBean.DataBeanX.DataBean.SpecData.SpecSizeData sizeBean = (DetailBean.DataBeanX.DataBean.SpecData.SpecSizeData) o;
					if(null != sizeBean){
						if(sizeBean.getStocks() <= 0){
							if(mSizePosition >= 0){
								sizeFlow.getAdapter().setSelectedList(mSizePosition);
							}else{
								sizeFlow.getAdapter().notifyDataChanged();
							}
							return true;
						}
						if(mSizePosition == position){
							return true;
						}
						tvPrice.setText("¥" + sizeBean.getPrice());
						mSizeId = sizeBean.getSku_idcode();
						stocks = sizeBean.getStocks();
						mSizePosition = position;
						selectName(mColorName, sizeBean.getName());
						tvNum.setText(String.valueOf(stocks <= 0 ? 0 : 1));
					}
				}
				return true;
			}
		});

		sizeFlow.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
			@Override
			public void onSelected(Set<Integer> selectPosSet) {
				if(mSizePosition == -1){
					return;
				}

				if(null == selectPosSet || selectPosSet.size() <= 0){
					sizeFlow.getAdapter().setSelectedList(mSizePosition);
				}
			}
		});
	}

	private void selectName(String colorName, String sizeName){
		mColorName = colorName;
		mSizeName = sizeName;
		tvGuige.setText("已选：" + mColorName + "、" + mSizeName);
	}

	// 颜色
	private void initColorFlow(){
		if(null == colorFlow){
			return;
		}
		colorFlow.setAdapter(new MyTagAdapter(listColorData));
		if(mColorPosition >= 0){
			if (isSelectColor){
				colorFlow.getAdapter().setSelectedList(mColorPosition);
			}
		}
	}
	MyTagAdapter sizeAdapter;
	// 尺寸
	private void initSizeFlow(){
		if(null == sizeFlow){
			return;
		}
		if(null == listSizeData){
			listSizeData = new ArrayList<>();
		}
		mSizePosition = listSizeData.size() > mSizePosition ? mSizePosition : 0;
		if(listSizeData.size() > 0){
			Object o = listSizeData.get(mSizePosition);
			if(null != o && o instanceof DetailBean.DataBeanX.DataBean.SpecData.SpecSizeData ){
				DetailBean.DataBeanX.DataBean.SpecData.SpecSizeData defaultData = (DetailBean.DataBeanX.DataBean.SpecData.SpecSizeData) o;
				if(defaultData.getStocks() > 0){// 库存大于0，直接选中
					tvSize.setText(defaultData.getAttr_name());
					mSizeName = defaultData.getName();
					mSizeId = defaultData.getSku_idcode();
					stocks = defaultData.getStocks();
					tvPrice.setText("¥" + defaultData.getPrice());
					selectName(mColorName, defaultData.getName());
				}else { // 库存小于0，则循环获取到一个库存大于0的直接选中
					mSizeName = "";
					mSizeId = "";
					mSizePosition = -1;
					for(int i = 0; i < listSizeData.size(); i++){
						Object object = listSizeData.get(i);
						if(null != object && object instanceof DetailBean.DataBeanX.DataBean.SpecData.SpecSizeData ) {
							DetailBean.DataBeanX.DataBean.SpecData.SpecSizeData bean = (DetailBean.DataBeanX.DataBean.SpecData.SpecSizeData) object;
							if(bean.getStocks() > 0){
								tvSize.setText(bean.getAttr_name());
								mSizeName = bean.getName();
								mSizeId = bean.getSku_idcode();
								stocks = bean.getStocks();
								tvPrice.setText("¥" + bean.getPrice());
								selectName(mColorName, bean.getName());
								mSizePosition = i;
								break;
							}
						}
					}
				}
			}
		}
		if(null == sizeAdapter){
			sizeAdapter = new MyTagAdapter(listSizeData);
			sizeFlow.setAdapter(sizeAdapter);
		}else{
			sizeAdapter.notifyDataChanged();
		}
//		if(mSizePosition >= 0){
//			sizeAdapter.setSelectedList(mSizePosition);
//		}

		sizeAdapter.setSelectedList();
		sizeAdapter.notifyDataChanged();
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id == R.id.m_iv_close){// 关闭弹框
			Tools.hideSoftInput(view);
			dismiss();
			return;
		}
		if(mColorId == -1 && id != R.id.m_iv_close && id != R.id.m_tv_num){// 颜色id
			JjToast.getInstance().toast("请选择" + tvColor.getText().toString());
			return;
		}
		if(StringUtil.isEmpty(mSizeId) && id != R.id.m_iv_close && id != R.id.m_tv_num){// 尺寸id
			if(StringUtil.isEmpty(tvSize.getText().toString())){
				JjToast.getInstance().toast("商品已售罄");
				return;
			}
			JjToast.getInstance().toast("请选择" + tvSize.getText().toString());
			return;
		}
		if(null == mContext && !(mContext instanceof DetailActivity)){
			return;
		}
		switch (id){
			case R.id.m_tv_add_cart:// 加入购物车
				addCart(true);
				break;
			case R.id.m_tv_buy:// 立即购买
				addCart(false);
				break;
			case R.id.m_tv_add:// 增加数量
				addOrDelNum(true);
				break;
			case R.id.m_tv_del:// 减少数量
				addOrDelNum(false);
				break;
			case R.id.m_tv_num:// 修改数量
				if(null == tvNum){
					return;
				}
				setCursorVisible(true);
				tvNum.setSelection(tvNum.getText().toString().length());
				scrollView.fullScroll(ScrollView.FOCUS_DOWN);
				break;
		}
	}


	/**
	 *
	 * 加入购物车、立即购买
	 * @param isAddCart		true：加入购物车  false：立即购买
	 */
	private void addCart(boolean isAddCart){
		if(null == tvNum){
			return;
		}
		setCursorVisible(false);
		String content = tvNum.getText().toString();
		if((StringUtil.isEmpty(content) || Integer.parseInt(content) <= 0 ) && stocks > 0) {
			tvNum.setText("1");
			JjToast.getInstance().toast("至少购买一件商品");
			return;
		}
		int addCartnum = Integer.parseInt(content);
		if(addCartnum > stocks && addCartnum > 0){
			addCartnum = stocks;
			if(limitBuyNum >= 1 && addCartnum > limitBuyNum){
				addCartnum = limitBuyNum;
				tvNum.setText(String.valueOf(addCartnum));
				JjToast.getInstance().toast("每人限购：" + limitBuyNum + "件");
				return;
			}
			tvNum.setText(String.valueOf(stocks));
			JjToast.getInstance().toast("库存最多有：" + stocks);
			return;
		}
		if(limitBuyNum >= 1 && addCartnum > limitBuyNum){
			tvNum.setText(String.valueOf(limitBuyNum));
			JjToast.getInstance().toast("每人限购：" + limitBuyNum + "件");
			return;
		}

		// 选择颜色
		if (listColorData.size()<=0){
			return;
		}

		DetailBean.DataBeanX.DataBean.SpecData data = (DetailBean.DataBeanX.DataBean.SpecData) listColorData.get(0);
		if (!isSelectColor) {
			JjToast.getInstance().toast("请选择：" + data.getAttr_name());
			return;
		}

		// 选择尺寸
		if (data.getList().size()<=0){
			return;
		}

		DetailBean.DataBeanX.DataBean.SpecData.SpecSizeData sizeData = data
				.getList().get(0);
		if (!isSelectSize){
			JjToast.getInstance().toast("请选择：" + sizeData.getAttr_name());
			return;
		}

		showColorSize();

		if(isAddCart){
			((DetailActivity)mContext).addCart(mSizeId, addCartnum);
		}else{
			((DetailActivity)mContext).lijiBuy(mSizeId, addCartnum);
		}

		dismiss();
	}

	// 显示选中的颜色和尺码
	private void showColorSize(){
		if(null == mContext && !(mContext instanceof DetailActivity)){
			return;
		}
		String selectContent = "已选：" + mColorName;
		if(!StringUtil.isEmpty(mSizeName)){
			selectContent = selectContent +  "、" + mSizeName;
		}
		if(mColorPosition == -1){
			selectContent = "商品已售罄";
			mColorPosition = 0;
		}
		((DetailActivity)mContext).selectGuige(selectContent, mColorPosition, mSizePosition);
	}

	/**
	 * 添加和减少数量
	 * @param isAdd		true：添加  false：减少
	 */
	private void addOrDelNum(boolean isAdd){
		if(null == tvNum){
			return;
		}
		setCursorVisible(false);
		if(StringUtil.isEmpty(tvNum.getText().toString())) {
			tvNum.setText("0");
		}
		int num = Integer.parseInt(tvNum.getText().toString());
		if(num <= 1 && isAdd == false && stocks > 0){
			JjToast.getInstance().toast("至少购买一件商品");
			return;
		}

		if(isAdd){
			if(num >= stocks){
				num = stocks;
				if(limitBuyNum >= 1 && num >= limitBuyNum){
					num = limitBuyNum;
					JjToast.getInstance().toast("每人限购：" + limitBuyNum + "件");
				}else{
					JjToast.getInstance().toast("库存最多有：" + stocks);
				}
			}else{
				if(limitBuyNum >= 1 && num >= limitBuyNum){
					num = limitBuyNum;
					JjToast.getInstance().toast("每人限购：" + limitBuyNum + "件");
				}else{
					num = num + 1;
				}
			}
		}else{
			num = num -1;
		}
		tvNum.setText(String.valueOf(num));
		tvNum.setSelection(tvNum.getText().toString().length());
	}

	// 颜色、尺寸适配器
	private class MyTagAdapter extends TagAdapter<Object>{
		final int paddingTb = UIUtils.dip2px(5);
		final int paddingLr = UIUtils.dip2px(15);
		private String defaultName = "";
		private List<Object> datas;

		public MyTagAdapter(List<Object> datas) {
			super(datas);
			this.datas = datas;
		}

		@Override
		public View getView(FlowLayout parent, int position, Object o) {
			TextView textView = new TextView(mContext);
			textView.setPadding(paddingLr, paddingTb, paddingLr, paddingTb);
			textView.setBackgroundResource(R.drawable.yuanjiao_f4f4f4_select);
			textView.setTextColor(mContext.getResources().getColor(R.color.color_1b1b1b));
			textView.setTextSize(15f);
			if(null != o){
				if(o instanceof DetailBean.DataBeanX.DataBean.SpecData){
					DetailBean.DataBeanX.DataBean.SpecData colorBean = (DetailBean.DataBeanX.DataBean.SpecData)o;
					defaultName = colorBean.getName();
					if(getStocks(colorBean.getList()) <= 0){
						textView.setTextColor(mContext.getResources().getColor(R.color.color_cccccc));
					}
				}else if(o instanceof DetailBean.DataBeanX.DataBean.SpecData.SpecSizeData){
					DetailBean.DataBeanX.DataBean.SpecData.SpecSizeData sizeBean = (DetailBean.DataBeanX.DataBean.SpecData.SpecSizeData)o;
					defaultName = sizeBean.getName();
					if(sizeBean.getStocks() <= 0){
						textView.setTextColor(mContext.getResources().getColor(R.color.color_cccccc));
					}
				}
			}
			textView.setText(defaultName);
			return textView;
		}

		@Override
		public void onSelected(int position, View view) {
			if(null != view && view instanceof TextView){
				((TextView) view).setBackgroundResource(R.drawable.yuanjiao_d6004f_select);
				((TextView) view).setTextColor(mContext.getResources().getColor(R.color.white));
			}
		}

		@Override
		public void unSelected(int position, View view) {
			if(null != view && view instanceof TextView){
				((TextView) view).setBackgroundResource(R.drawable.yuanjiao_f4f4f4_select);
				((TextView) view).setTextColor(mContext.getResources().getColor(R.color.color_1b1b1b));
				if(null != datas && datas.size() > 0){
					Object o = datas.get(position);
					if(null != o && o instanceof DetailBean.DataBeanX.DataBean.SpecData.SpecSizeData) {
						DetailBean.DataBeanX.DataBean.SpecData.SpecSizeData sizeBean = (DetailBean.DataBeanX.DataBean.SpecData.SpecSizeData) o;
						if(sizeBean.getStocks() <= 0){
							((TextView) view).setTextColor(mContext.getResources().getColor(R.color.color_cccccc));
						}
					}
				}
			}
		}
	}

	// 获取第二个规格的库存总数
	private int getStocks(ArrayList<DetailBean.DataBeanX.DataBean.SpecData.SpecSizeData> list){
		int stock = 0;
		if(null != mContext && mContext instanceof DetailActivity){
			return ((DetailActivity)mContext).getStocks(list);
		}
		return stock;
	}

	private void setCursorVisible(boolean b){
		if(null != tvNum){
			boolean curr = tvNum.isCursorVisible();
			if(b && curr == b){
				return;
			}
			if(b == false && curr == b){
				return;
			}
			tvNum.setCursorVisible(b);
		}
	}

	/**
	 * 倒计时
	 * @param time	时间（秒）
	 */
	public void setLayoutDjs(long time){
		if(null != mLayoutBottomDjs && null != mTvBottomDjs && null != mTvBottomDjsTishi
				&& null != mLayoutBottomDjs){
			if(time <= 0){
				mLayouBottom.setVisibility(View.VISIBLE);
				if(mLayoutBottomDjs.getVisibility() != View.GONE){// 设置加入购物车、立即购买隐藏
					mLayoutBottomDjs.setVisibility(View.GONE);
				}
				JjLog.e("小于0，可以购买");
				return;
			}
			if(mLayoutBottomDjs.getVisibility() != View.VISIBLE){// 设置倒计时显示
				mLayoutBottomDjs.setVisibility(View.VISIBLE);
				JjLog.e("倒计时状态");
			}
			if(mLayouBottom.getVisibility() != View.GONE){// 设置加入购物车、立即购买隐藏
				mLayouBottom.setVisibility(View.GONE);
				JjLog.e("购物车状态");
			}
			mTvBottomDjsTishi.setText((Tools.getDay(time) >= 1) ? "距离开始还有 " : "距离开始仅剩 ");
			mTvBottomDjs.setText(Tools.goodsDetailsDaojishi(time));
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		mLayoutBottomDjs = null;
		mTvBottomDjsTishi = null;
		mTvBottomDjs = null;
		JjLog.e("关闭了");
		super.onDismiss(dialog);
	}
}
