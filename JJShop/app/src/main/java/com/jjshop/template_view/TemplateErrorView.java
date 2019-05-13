package com.jjshop.template_view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.Tools;

/**
 * 作者：张国庆
 * 时间：2018/7/17
 */

public class TemplateErrorView extends BaseLinearTemplateView{
    public TemplateErrorView(Context context) {
        super(context);
    }

    public TemplateErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    TextView tvErrorCode, tvErrorTishi, tvRefresh;
    @Override
    public void initView() {
        tvErrorCode = findViewById(R.id.m_tv_error_code);
        tvErrorTishi = findViewById(R.id.m_tv_error_tishi);
        tvRefresh = findViewById(R.id.m_tv_refresh);
        if(Tools.isNetConnected(mContext)){
            tvErrorCode.setText("网络请求失败-" + PreUtils.getString(mContext, PreUtils.SHOP_ID));
        }else{
            tvErrorCode.setText("请检查你是否连接网络");
        }

    }

    public void showNoShopView(){
        if(null != tvErrorCode){
            tvErrorCode.setText("您的店铺已经被关闭-" + PreUtils.getString(mContext, PreUtils.SHOP_ID));
        }
        if(null != tvErrorTishi){
            tvErrorTishi.setVisibility(INVISIBLE);
        }
        if(null != tvRefresh){
            tvRefresh.setText("退出登录");
        }
    }

    @Override
    public void getDate(Object data, Bundle bundle) {

    }

    @Override
    public void onDetached() {
        removeAllViews();
    }
}
