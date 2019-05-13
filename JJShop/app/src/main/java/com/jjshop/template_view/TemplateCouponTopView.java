package com.jjshop.template_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.ui.activity.person.MyCouponActivity;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.Tools;

/**
 * 作者：张国庆
 * 时间：2018/7/25
 */

public class TemplateCouponTopView extends BaseTemplateView{

    private TextView mTvDuihuan;
    private EditText mEtCouponCode;

    public TemplateCouponTopView(Context context) {
        super(context);
    }

    public TemplateCouponTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateCouponTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        mEtCouponCode = findViewById(R.id.m_et_coupon_code);
        mTvDuihuan = findViewById(R.id.m_tv_duihuan);
        mTvDuihuan.setOnClickListener(this);
        // 监听输入框的焦点
        mEtCouponCode.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){// 获取焦点时滑动到顶部
                    if(null != mContext && mContext instanceof MyCouponActivity){
                        ((MyCouponActivity)mContext).scrollTop();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.m_tv_duihuan:// 兑换
                if(null == mEtCouponCode){
                    return;
                }
                String content = mEtCouponCode.getText().toString();
                if(StringUtil.isEmpty(content)){
                    JjToast.getInstance().toast("请输入优惠码");
                    return;
                }
                mEtCouponCode.setText("");
                Tools.hideSoftInput(v);
                if(null != mContext && mContext instanceof MyCouponActivity){
                    ((MyCouponActivity)mContext).loadDuihuanData(content);
                }
                break;
        }
    }

    @Override
    public void onDetached() {

    }
}
