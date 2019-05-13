package com.jjshop.template_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.jjshop.R;
import com.jjshop.ui.activity.home.HomeActivity;

/**
 * 作者：张国庆
 * 时间：2018/8/4
 */

public class TemplateCartNoDataView extends BaseTemplateView{
    public TemplateCartNoDataView(Context context) {
        super(context);
    }

    public TemplateCartNoDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateCartNoDataView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        findViewById(R.id.m_tv_go_shopping).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != mContext && mContext instanceof HomeActivity){
                    ((HomeActivity)mContext).selectPosition(HomeActivity.SELECT_HOME);
                }
            }
        });
    }

    @Override
    public void onDetached() {

    }
}
