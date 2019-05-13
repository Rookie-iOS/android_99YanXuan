package com.jjshop.template_view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 模板基类
 */

public abstract class BaseTemplateView extends RelativeLayout
        implements View.OnClickListener, BaseTemplateIm{
    /** 上下文 */
    public Context mContext;

    public BaseTemplateView(Context context) {
        super(context);
        mContext = context;
    }

    public BaseTemplateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public BaseTemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(isInEditMode()){
            return;
        }
        initView();
    }

    @Override
    protected void onDetachedFromWindow() {
        onDetached();
        super.onDetachedFromWindow();
    }
    /** 初始化控件 */
    public abstract void initView();

    /** 退出或者销毁 */
    public abstract void onDetached();

    @Override
    public void onClick(View v) {

    }

    @Override
    public void getDate(Object data, Bundle bundle) {

    }

    @Override
    public void getDate(Object data, Object data2, Bundle bundle) {

    }

    @Override
    public void getDate(Object data, int position) {

    }
}
