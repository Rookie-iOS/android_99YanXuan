package com.jjshop.template_view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jjshop.R;

/**
 * 作者：张国庆
 * 时间：2018/7/17
 */

public class TemplateLoadingView extends BaseTemplateView{
    public TemplateLoadingView(Context context) {
        super(context);
    }

    public TemplateLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    ImageView imageView;
    @Override
    public void initView() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        imageView = findViewById(R.id.m_iv_loading);
        Glide.with(mContext).load(R.mipmap.img_loading).into(imageView);
    }

    @Override
    public void getDate(Object data, Bundle bundle) {

    }

    @Override
    public void onDetached() {
        imageView = null;
        removeAllViews();
    }
}
