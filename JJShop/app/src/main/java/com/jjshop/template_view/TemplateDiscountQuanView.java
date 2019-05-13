package com.jjshop.template_view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.bean.CouponDataBean;
import com.jjshop.ui.activity.home.SubmitOrderActivity;

/**
 * 作者：张国庆
 * 时间：2018/7/25
 */

public class TemplateDiscountQuanView extends BaseTemplateView{

    private TextView mTvPrice, mTvManjian, mTvName, mTvTime, mTvGuize, mTvDetailContent;
    private CouponDataBean bean;
    private ImageView mIvQuanStatus, mIvLineHeng, mIvLineShu;
    private RelativeLayout layout_quan;

    public TemplateDiscountQuanView(Context context) {
        super(context);
    }

    public TemplateDiscountQuanView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateDiscountQuanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        findViewById(R.id.template_discount_quan).setOnClickListener(this);
        layout_quan = findViewById(R.id.layout_quan);
        mTvPrice = findViewById(R.id.m_tv_quan_price);
        mTvManjian = findViewById(R.id.m_tv_manjian);
        mTvName = findViewById(R.id.m_tv_quan_name);
        mTvTime = findViewById(R.id.m_tv_quan_time);
        mTvGuize = findViewById(R.id.m_tv_detail_guize);
        mTvGuize.setOnClickListener(this);
        mTvDetailContent = findViewById(R.id.m_tv_detail_content);
        mIvQuanStatus = findViewById(R.id.m_iv_quan_status);
        mIvLineHeng = findViewById(R.id.view_line_heng);
        mIvLineShu = findViewById(R.id.view_line_shu);
    }

    @Override
    public void getDate(Object data, Bundle bundle) {
        if(null == mContext || null == data || !(data instanceof CouponDataBean)){
            return;
        }
        bean = (CouponDataBean) data;

        mTvPrice.setText("¥" + bean.getPrice());
        mTvManjian.setText("满" + bean.getUse_price() + "可用");
        mTvName.setText(bean.getPatch_name());
        mTvTime.setText(bean.getStart_time() + " - " + bean.getEnd_time());
        mTvDetailContent.setText(bean.getNote());
        mTvDetailContent.setVisibility(bean.isIs_show_guize() ? VISIBLE : GONE);
        // 根据券的状态修改UI样式
        switch (bean.getStatus()){
            case 3:// 已使用的券
                mIvQuanStatus.setImageResource(R.mipmap.img_coupon_shiyong_icon);
                setTextColor(getResources().getColor(R.color.color_d0a8a8));
                setImgIcon(VISIBLE, R.color.color_d0a8a8, R.mipmap.img_discount_quan_details_icon, R.drawable.yuanjiao_fff1f1_select,
                        R.mipmap.img_discount_quan_line_heng, R.mipmap.img_discount_quan_line_shu);
                break;
            case 5:// 已过期的券
                mIvQuanStatus.setImageResource(R.mipmap.img_coupon_guoqi_icon);
                setTextColor(getResources().getColor(R.color.color_bababa));
                setImgIcon(VISIBLE, R.color.color_bababa, R.mipmap.img_jiantou_bottom_coupon, R.drawable.yuanjiao_f2f2f2_select,
                        R.mipmap.img_discount_quan_line_heng_guoqi, R.mipmap.img_discount_quan_line_shu_guoqi);
                break;
            default:// 可以使用的券
                setTextColor(getResources().getColor(R.color.color_f23030));
                setImgIcon(GONE, R.color.color_d0a8a8, R.mipmap.img_discount_quan_details_icon, R.drawable.yuanjiao_fff1f1_select,
                        R.mipmap.img_discount_quan_line_heng, R.mipmap.img_discount_quan_line_shu);
                break;
        }
    }

    // 根据券的状态设置文字颜色
    private void setTextColor(int color){
        if(null == mTvPrice || null == mTvManjian || null == mTvName
                || null == mTvTime || null == mTvDetailContent){
            return;
        }
        mTvPrice.setTextColor(color);
        mTvManjian.setTextColor(color);
        mTvName.setTextColor(color);
        mTvTime.setTextColor(color);
        mTvDetailContent.setTextColor(color);
    }

    /**
     * 根据券的状态更换图标
     * @param visible       是否显示状态图标
     * @param guizeColor    规则文字颜色
     * @param guizeIcon     规则右边的icon
     * @param bg            整个item的背景
     * @param lineHeng      横线icon
     * @param lineShu       竖线icon
     */
    private void setImgIcon(int visible, int guizeColor, int guizeIcon, int bg, int lineHeng, int lineShu){
        if(null == mTvGuize || null == layout_quan || null == mIvLineHeng
                || null == mIvLineShu || null == mIvQuanStatus){
            return;
        }
        mIvQuanStatus.setVisibility(visible);
        mTvGuize.setTextColor(getResources().getColor(guizeColor));
        mTvGuize.setCompoundDrawablesWithIntrinsicBounds(0, 0,guizeIcon, 0);
        layout_quan.setBackgroundResource(bg);
        mIvLineHeng.setImageResource(lineHeng);
        mIvLineShu.setImageResource(lineShu);
    }

    @Override
    public void onClick(View v) {
        if(null == bean || null == mContext){
            return;
        }
        switch (v.getId()){
            case R.id.template_discount_quan:// item点击
                if(mContext instanceof SubmitOrderActivity){
                    ((SubmitOrderActivity)mContext).loadData(bean.getId_code());
                }
                break;
            case R.id.m_tv_detail_guize:// 详细规则
                if(null == mTvDetailContent){
                    return;
                }
                if(bean.isIs_show_guize()) {
                    bean.setIs_show_guize(false);
                    mTvDetailContent.setVisibility(GONE);
                    return;
                }
                bean.setIs_show_guize(true);
                mTvDetailContent.setVisibility(VISIBLE);
                break;
        }
    }

    @Override
    public void onDetached() {

    }
}
