package com.jjshop.template_view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.bean.WuliuInfoBeanExpress;
import com.jjshop.utils.StringUtil;

/**
 * 作者：张国庆
 * 时间：2018/7/25
 */

public class TemplateWuliuItemView extends BaseTemplateView{

    private TextView m_tv_date, m_tv_time, m_tv_content;
    private ImageView m_iv_icon;

    public TemplateWuliuItemView(Context context) {
        super(context);
    }

    public TemplateWuliuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateWuliuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        m_tv_date = findViewById(R.id.m_tv_date);
        m_tv_time = findViewById(R.id.m_tv_time);
        m_tv_content = findViewById(R.id.m_tv_content);
        m_iv_icon = findViewById(R.id.m_iv_icon);
    }

    @Override
    public void getDate(Object data, Bundle bundle) {
        if(null == mContext || null == data || !(data instanceof WuliuInfoBeanExpress)){
            return;
        }
        WuliuInfoBeanExpress beanExpress = (WuliuInfoBeanExpress) data;
        // 日期
        String[] dateTime = StringUtil.strToArray(beanExpress.getTime(), " ");
        if(null != dateTime && dateTime.length > 0){
            if(dateTime.length >= 1){
                m_tv_date.setText(dateTime[0]);
            }
            if(dateTime.length >= 2){
                m_tv_time.setText(dateTime[1]);
            }
        }
        // 内容
        m_tv_content.setText(beanExpress.getContext());
        // 设置第一个信息为蓝色字体
        if(beanExpress.getPosition() == 0){
            setTextColor(R.mipmap.img_wuliu_lan_icon, R.color.color_1d96d7);
        }else{
            setTextColor(R.mipmap.img_wuliu_hui_icon, R.color.color_666666);
        }
    }


    private void setTextColor(int icon, int color){
        if(null == m_iv_icon || null == m_tv_date || null == m_tv_time || null == m_tv_content){
            return;
        }
        m_iv_icon.setImageResource(icon);
        m_tv_date.setTextColor(getResources().getColor(color));
        m_tv_time.setTextColor(getResources().getColor(color));
        m_tv_content.setTextColor(getResources().getColor(color));
    }

    @Override
    public void onDetached() {

    }
}
