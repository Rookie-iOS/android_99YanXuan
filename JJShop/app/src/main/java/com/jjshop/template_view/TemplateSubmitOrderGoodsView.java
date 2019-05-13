package com.jjshop.template_view;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.bean.CartDataBean;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.glide_img.GlideUtil;

/**
 * 作者：张国庆
 * 时间：2018/7/25
 */

public class TemplateSubmitOrderGoodsView extends BaseTemplateView{

    private ImageView mIvImg;
    private TextView mTvTitle, mTvGuige, mTvPrice, mTvOldPrice, mTvNum, mTvAllPrice, mTvRush, mTvBupeisong;
    private CartDataBean bean;

    public TemplateSubmitOrderGoodsView(Context context) {
        super(context);
    }

    public TemplateSubmitOrderGoodsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateSubmitOrderGoodsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        findViewById(R.id.template_submit_order_goods).setOnClickListener(this);
        mIvImg = findViewById(R.id.m_iv_img);
        mTvTitle = findViewById(R.id.m_tv_title);
        mTvGuige = findViewById(R.id.m_tv_guige);
        mTvPrice = findViewById(R.id.m_tv_price);
        mTvOldPrice = findViewById(R.id.m_tv_old_price);
        mTvNum = findViewById(R.id.m_tv_num);
        mTvAllPrice = findViewById(R.id.m_tv_all_price);
        mTvRush = findViewById(R.id.m_tv_rush);
        mTvBupeisong = findViewById(R.id.m_tv_buzai_peisong);
    }

    @Override
    public void getDate(Object data, Bundle bundle) {
        if(null == mContext || null == data || !(data instanceof CartDataBean)){
            return;
        }
        bean = (CartDataBean) data;
        if(null != bean.getImgs() && bean.getImgs().length >= 1){
            GlideUtil.getInstence().loadImageNoFix(mContext, mIvImg, bean.getImgs()[0]);
        }
        // 商品名称
        mTvTitle.setText(bean.getName());
        // 规格
        String color = "", size = "";
        double price = 0;// 当前价格
        double oldPrice = 0;// 原价
        CartDataBean.ColorData colorData = bean.getColor();
        if(null != colorData){
            color = colorData.getName();
            CartDataBean.ColorData.SizeData sizeData = colorData.getSize();
            if(null != sizeData){
                size = sizeData.getName();
                price = sizeData.getPrice();
                oldPrice = sizeData.getOrg_price();
            }
        }
        mTvGuige.setText(color + "  " + size);
        // 单价
        mTvPrice.setText("¥" + price);
        // 原价
        if(price == oldPrice){
            mTvOldPrice.setVisibility(GONE);
        }else{
            mTvOldPrice.setVisibility(VISIBLE);
            mTvOldPrice.setText("¥" + oldPrice);
            mTvOldPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
        }
        // 数量
        int num = bean.getNum();
        num = num <= 0 ? 1 : num;
        mTvNum.setText("x" + num);
        // 小计
        mTvAllPrice.setText("¥" + bean.getTotal_price());
        // 限时
        CartDataBean.PromotionsData promotionsData = bean.getPromotions();
        if(null != promotionsData && !StringUtil.isEmpty(promotionsData.getEtime())){
            mTvRush.setVisibility(VISIBLE);
            mTvRush.setText("限时  " + promotionsData.getEtime() + "  结束");
        }else{
            mTvRush.setVisibility(GONE);
        }
        // 不再配送区域
        mTvBupeisong.setVisibility(bean.isNot_distribution() ? VISIBLE : GONE);
    }

    @Override
    public void onDetached() {

    }
}
