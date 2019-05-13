package com.jjshop.template_view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.bean.MyOrderTopBean;
import com.jjshop.ui.activity.person.OrderDetailActivity;

/**
 * 作者：张国庆
 * 时间：2018/7/25
 */

public class TemplateOrderTopView extends BaseTemplateView{

    private TextView mTvShopName, mTvOrderStatus;
    private  MyOrderTopBean bean;

    public TemplateOrderTopView(Context context) {
        super(context);
    }

    public TemplateOrderTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateOrderTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        findViewById(R.id.template_order_top).setOnClickListener(this);
        mTvShopName = findViewById(R.id.m_tv_shop_name);
        mTvOrderStatus = findViewById(R.id.m_tv_order_status);
    }

    @Override
    public void getDate(Object data, Bundle bundle) {
        if(null == mContext || null == data || !(data instanceof MyOrderTopBean)){
            return;
        }
        bean = (MyOrderTopBean) data;
        // 店铺名称
        mTvShopName.setText(bean.getShopName());
        // 订单状态
        mTvOrderStatus.setText(bean.getStatus_str());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.template_order_top:
                if(null == mContext || null == bean){
                    return;
                }
                OrderDetailActivity.invoke(mContext, bean.getOrder_no());
                break;
        }
    }

    @Override
    public void onDetached() {

    }
}
