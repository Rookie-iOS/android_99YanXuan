package com.jjshop.template_view;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.bean.ProductListBean;
import com.jjshop.ui.activity.home.DetailActivity;
import com.jjshop.ui.activity.home.PayActivity;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.UIUtils;
import com.jjshop.utils.glide_img.GlideUtil;

/**
 * 商品可能喜欢item
 */

public class TemplateGoodsLikeView extends BaseTemplateView{

    private ImageView ivImg;
    private TextView tvTitle, tvPrice, tvOldPrice;
    private ProductListBean bean;

    public TemplateGoodsLikeView(Context context) {
        super(context);
    }

    public TemplateGoodsLikeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateGoodsLikeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        findViewById(R.id.template_goods_like).setOnClickListener(this);
        ivImg = findViewById(R.id.cell_gridimage);
        tvTitle = findViewById(R.id.cell_gridgoodname);
        tvPrice = findViewById(R.id.cell_gridgoodoriginprice);
        tvOldPrice = findViewById(R.id.cell_gridgoodlastprice);
        // 设置图片的宽高
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivImg.getLayoutParams();
        params.width = (UIUtils.getWidth() - UIUtils.dip2px(30)) / 2;
        params.height = params.width;
        ivImg.setLayoutParams(params);
    }


    @Override
    public void getDate(Object data, Bundle bundle) {
        if(null == mContext || null == data || !(data instanceof ProductListBean)){
            return;
        }
        bean = (ProductListBean) data;
        if(null == bean){
            return;
        }
        GlideUtil.getInstence().loadImage(mContext, ivImg, bean.getImg());
        tvTitle.setText(bean.getName());
        // 价格
        double price = bean.getPrice();// 当前价格
        double oldPrice = bean.getOrg_price();// 原价
        if(price >= oldPrice){// 当前价格大于等于原价，不显示原价
            tvOldPrice.setVisibility(GONE);
        }else{
            tvOldPrice.setVisibility(VISIBLE);
            tvOldPrice.setText("¥" + oldPrice);
            tvOldPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
        }
        tvPrice.setText("¥" + price);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.template_goods_like:
                invoke();
                break;
        }
    }

    // 跳转到详情
    private void invoke(){
        if(null == mContext || null == bean || StringUtil.isEmpty(bean.getIdcode())
                || StringUtil.isEmpty(String.valueOf(bean.getProduct_id()))){
            return;
        }
        if(mContext instanceof PayActivity){
            ((PayActivity)mContext).cancelTimer();
        }
        DetailActivity.invoke(mContext, bean.getIdcode(), String.valueOf(bean.getProduct_id()));
    }

    @Override
    public void onDetached() {

    }


}
