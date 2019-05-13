package com.jjshop.template_view;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.bean.ProductListBean;
import com.jjshop.bean.RushBuyBean;
import com.jjshop.ui.activity.home.DetailActivity;
import com.jjshop.utils.glide_img.GlideUtil;
import com.jjshop.utils.PreUtils;

/**
 * 限时抢购item
 */

public class TemplateRushBuyView extends BaseTemplateView{

    private ImageView ivImg;
    private TextView tvTitle, tvOldPrice, tvNewPrice, tvBuy;
    private ProductListBean bean;

    public TemplateRushBuyView(Context context) {
        super(context);
    }

    public TemplateRushBuyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateRushBuyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        findViewById(R.id.template_rushbuy).setOnClickListener(this);
        ivImg = findViewById(R.id.rush_iv_img);
        tvTitle = findViewById(R.id.rush_tv_title);
        tvOldPrice = findViewById(R.id.rush_tv_old_price);
        tvNewPrice = findViewById(R.id.rush_tv_new_price);
        tvBuy = findViewById(R.id.rush_tv_buy);
    }


    @Override
    public void getDate(Object data, Bundle bundle) {
        if(null == mContext || null == data || !(data instanceof ProductListBean)){
            return;
        }
        bean = (ProductListBean) data;
        GlideUtil.getInstence().loadImage(mContext, ivImg, bean.getImg());
        tvTitle.setText(bean.getName());
        tvNewPrice.setText(String.valueOf(bean.getPrice()));
        tvOldPrice.setText("原价¥" + String.valueOf(bean.getOrg_price()));
        tvOldPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
        if(bean.getType() == 1){
            tvBuy.setText("即将开始");
            tvBuy.setBackgroundResource(R.drawable.rushbuy_yuanjiao_false_select);
        }else if(bean.getType() == 2) {
            tvBuy.setText("立即抢购");
            tvBuy.setBackgroundResource(R.drawable.rushbuy_yuanjiao_true_select);
        }else{
            tvBuy.setText("已结束");
            tvBuy.setBackgroundResource(R.drawable.rushbuy_yuanjiao_false_select);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.template_rushbuy:
                if(null == mContext || null == bean){
                    return;
                }
//                if(bean.getType() == 2){
                    DetailActivity.invoke(mContext, bean.getIdcode(), String.valueOf(bean.getId()));
//                }
                break;
        }
    }

    @Override
    public void onDetached() {

    }

}
