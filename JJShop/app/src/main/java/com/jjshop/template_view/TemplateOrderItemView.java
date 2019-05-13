package com.jjshop.template_view;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.bean.MyOrderActionData;
import com.jjshop.bean.MyOrderProductList;
import com.jjshop.ui.activity.home.DetailActivity;
import com.jjshop.ui.activity.person.MyOrderActivity;
import com.jjshop.ui.activity.person.OrderDetailActivity;
import com.jjshop.ui.activity.person.RefundActivity;
import com.jjshop.ui.activity.person.RefundDetailActivity;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.glide_img.GlideUtil;

/**
 * 作者：张国庆
 * 时间：2018/7/25
 */

public class TemplateOrderItemView extends BaseTemplateView{

    private ImageView mIvImg;
    private TextView mTvTitle, mTvGuige, mTvPrice, mTvOldPrice, mTvNum, mTvTui;
    private MyOrderProductList bean;
    private MyOrderActionData actionData;
    private int service_id;

    public TemplateOrderItemView(Context context) {
        super(context);
    }

    public TemplateOrderItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateOrderItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        if(mContext instanceof OrderDetailActivity
                || mContext instanceof MyOrderActivity){// 订单详情页，初始化点击事件
            findViewById(R.id.template_order_item).setOnClickListener(this);
        }
        mIvImg = findViewById(R.id.m_iv_img);
        mTvTitle = findViewById(R.id.m_tv_title);
        mTvGuige = findViewById(R.id.m_tv_guige);
        mTvPrice = findViewById(R.id.m_tv_price);
        mTvOldPrice = findViewById(R.id.m_tv_old_price);
        mTvNum = findViewById(R.id.m_tv_num);
        mTvTui = findViewById(R.id.m_tv_order_tui);
        mTvTui.setOnClickListener(this);
    }

    @Override
    public void getDate(Object data, Bundle bundle) {
        if(null == mContext || null == data || !(data instanceof MyOrderProductList)){
            return;
        }
        bean = (MyOrderProductList) data;
        service_id = bean.getService_id();
        // 图片
        String[] img = bean.getImg();
        if(null != img && img.length >= 1){
            GlideUtil.getInstence().loadImageNoFix(mContext, mIvImg, img[0]);
        }
        // 商品名称
        mTvTitle.setText(bean.getName());
        // 规格
        mTvGuige.setText(bean.getColor_name() + "  " + bean.getSize());
        // 价格
        double price = bean.getPrice();// 当前价格
        double oldPrice = bean.getOrg_price();// 原价
        mTvPrice.setText("¥" + price);
        if(mContext instanceof OrderDetailActivity){
            if(price >= oldPrice){// 当前价格大于等于原价，不显示原价
                mTvOldPrice.setVisibility(GONE);
            }else{
                mTvOldPrice.setVisibility(VISIBLE);
                mTvOldPrice.setText("¥" + oldPrice);
                mTvOldPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
            }
        }else{
            mTvOldPrice.setVisibility(GONE);
        }
        // 数量
        int num = bean.getNum();
        num = num <= 0 ? 1 : num;
        mTvNum.setText("x" + num);
        // 退款状态
        actionData = bean.getAction();
        if(null != actionData){
            String actionName = actionData.getName();
            if(StringUtil.isEmpty(actionName)){
                mTvTui.setVisibility(INVISIBLE);
            }else{
                mTvTui.setVisibility(VISIBLE);
                mTvTui.setText(actionName);
                if(actionData.getApp_status() == RefundActivity.STATUS_1001 ||
                        actionData.getApp_status() == RefundActivity.STATUS_1002){
                    mTvTui.setTextColor(getResources().getColor(R.color.color_333333));
                }else{
                    mTvTui.setTextColor(getResources().getColor(R.color.color_d6004f));
                }
            }
        }else{
            mTvTui.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.template_order_item:
                if(null == mContext || null == bean){
                    return;
                }
                if(mContext instanceof MyOrderActivity){
                    OrderDetailActivity.invoke(mContext, bean.getOrder_no());
                    return;
                }
                if(mContext instanceof OrderDetailActivity){
                    DetailActivity.invoke(mContext, bean.getIdcode(), String.valueOf(bean.getProduct_id()));
                }
                break;
            case R.id.m_tv_order_tui:
                invoke();
                break;
        }
    }

    private void invoke(){
        if(null == mContext || null == actionData){
            return;
        }
        int status = actionData.getApp_status();
        switch (status){
            case RefundActivity.STATUS_1001:// 申请退款
            case RefundActivity.STATUS_1002:// 申请退货（需要上传图片）
                MyOrderActionData.MyOrderActionParameter parameter = actionData.getParameter();
                if(null != parameter){
                    RefundActivity.invoke(mContext, status, parameter.getOrder_no(), parameter.getItem_id());
                }
                break;
            case RefundDetailActivity.STATUS_1003:// 退款详情页
            case RefundDetailActivity.STATUS_1004:// 退货详情页
                RefundDetailActivity.invoke(mContext, status, service_id);
                break;
        }
    }

    @Override
    public void onDetached() {

    }
}
