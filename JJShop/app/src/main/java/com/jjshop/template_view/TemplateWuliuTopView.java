package com.jjshop.template_view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.bean.WuliuTopBean;

/**
 * 作者：张国庆
 * 时间：2018/7/17
 */

public class TemplateWuliuTopView extends BaseTemplateView{

    public TemplateWuliuTopView(Context context) {
        super(context);
    }

    public TemplateWuliuTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateWuliuTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private TextView mTvKdName, mTvKdOrder;

    @Override
    public void initView() {
        mTvKdName = findViewById(R.id.m_tv_kd_name);
        mTvKdOrder = findViewById(R.id.m_tv_kd_order);
    }

    @Override
    public void getDate(Object data, Bundle bundle) {
        if(null == mContext || null == data || !(data instanceof WuliuTopBean)){
            return;
        }
        WuliuTopBean bean = (WuliuTopBean) data;
        mTvKdName.setText("快递公司：" + bean.getKdName());
        mTvKdOrder.setText("快递单号：" + bean.getKdNo());
    }

    @Override
    public void onDetached() {
    }
}
