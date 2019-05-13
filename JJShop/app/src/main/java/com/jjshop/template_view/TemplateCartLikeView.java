package com.jjshop.template_view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.bean.ProductListBean;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/8/3
 */

public class TemplateCartLikeView extends BaseTemplateView{
    private LinearLayout mLayoutLeft, mLayoutRight;
    private View mViewLeft, mViewRight;
    private TextView mTvLike;

    public TemplateCartLikeView(Context context) {
        super(context);
    }

    public TemplateCartLikeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateCartLikeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        mTvLike = findViewById(R.id.m_tv_like);
        mLayoutLeft = findViewById(R.id.m_layout_left);
        mLayoutRight = findViewById(R.id.m_layout_right);
        mViewLeft = LayoutInflater.from(mContext).inflate(R.layout.template_goods_like_view_layout, null);
        mViewRight = LayoutInflater.from(mContext).inflate(R.layout.template_goods_like_view_layout, null);
        mLayoutLeft.addView(mViewLeft);
        mLayoutRight.addView(mViewRight);
    }

    @Override
    public void getDate(Object data, Bundle bundle) {
        if(null == mContext || null == data || !(data instanceof ProductListBean)){
            return;
        }
        ProductListBean bean = (ProductListBean) data;
        if(null == bean){
            mLayoutLeft.setVisibility(GONE);
            mLayoutRight.setVisibility(GONE);
            return;
        }
        // 是否显示顶部的“你可能喜欢字样”
        mTvLike.setVisibility(bean.isFirstShow() ? VISIBLE : GONE);

        ArrayList<ProductListBean> list = bean.getCustomBlock();
        if(null == list || list.size() <= 0){
            return;
        }
        int size = list.size();
        // 左边的item
        if(mViewLeft instanceof BaseTemplateView && size >= 1){
            mViewLeft.setVisibility(VISIBLE);
            ((BaseTemplateView)mViewLeft).getDate(list.get(0), bundle);
        }else{
            mViewLeft.setVisibility(GONE);
        }
        // 右边的item
        if(mViewRight instanceof BaseTemplateView && size >= 2){
            mViewRight.setVisibility(VISIBLE);
            ((BaseTemplateView)mViewRight).getDate(list.get(1), bundle);
        }else{
            mViewRight.setVisibility(GONE);
        }
    }

    @Override
    public void onDetached() {

    }
}
