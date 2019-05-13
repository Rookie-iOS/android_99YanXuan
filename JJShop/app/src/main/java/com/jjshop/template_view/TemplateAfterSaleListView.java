package com.jjshop.template_view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.bean.AfterSaleRefundBean;
import com.jjshop.bean.MyOrderProductList;
import com.jjshop.ui.activity.person.RefundDetailActivity;

/**
 * 作者：张国庆
 * 时间：2018/7/25
 */

public class TemplateAfterSaleListView extends BaseTemplateView{

    private TextView mTvOrderNum, mTvOrderStatus, mTvLookDetail;
    private LinearLayout mLayoutGoods;
    private AfterSaleRefundBean.AfterSaleRefundData bean;

    private View view;

    public TemplateAfterSaleListView(Context context) {
        super(context);
    }

    public TemplateAfterSaleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateAfterSaleListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        findViewById(R.id.template_after_sale_list).setOnClickListener(this);
        mTvOrderNum = findViewById(R.id.m_tv_order_no);
        mTvOrderStatus = findViewById(R.id.m_tv_order_status);
        mLayoutGoods = findViewById(R.id.m_layout_goods);
        mTvLookDetail = findViewById(R.id.m_tv_look_detail);
        mTvLookDetail.setOnClickListener(this);
        view = LayoutInflater.from(mContext).inflate(R.layout.template_order_item_view_layout, null);
        mLayoutGoods.addView(view);
    }

    @Override
    public void getDate(Object data, Bundle bundle) {
        if(null == mContext || null == data || !(data instanceof AfterSaleRefundBean.AfterSaleRefundData)){
            return;
        }
        bean = (AfterSaleRefundBean.AfterSaleRefundData) data;
        // 订单状态
        mTvOrderStatus.setText(bean.getStatus_str());
        // 订单号
        mTvOrderNum.setText("订单号:" + bean.getOrder_no());
        // 购买的商品列表
        MyOrderProductList item = bean.getItem();
        if(null == item){
            return;
        }
        if(null != view && view instanceof TemplateOrderItemView){
            ((TemplateOrderItemView)view).getDate(item, null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.m_tv_look_detail:
            case R.id.template_after_sale_list:
                // 1: 退货   3：退款
                if(null == bean){
                    return;
                }
                int status = bean.getStatus() == 1 ? RefundDetailActivity.STATUS_1004 : RefundDetailActivity.STATUS_1003;
                RefundDetailActivity.invoke(mContext, status, bean.getId());
                break;
        }
    }

    @Override
    public void onDetached() {

    }
}
