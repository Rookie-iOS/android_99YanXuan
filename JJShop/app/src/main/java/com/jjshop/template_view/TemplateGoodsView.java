package com.jjshop.template_view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.bean.HotZhuanquBean;
import com.jjshop.bean.JJHomeBean;
import com.jjshop.bean.ProductListBean;
import com.jjshop.bean.SearchBean;
import com.jjshop.ui.activity.home.DetailActivity;
import com.jjshop.utils.glide_img.GlideUtil;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.ShareUtils;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.UIUtils;

/**
 * 商品item
 */

public class TemplateGoodsView extends BaseTemplateView{

    private ImageView ivImg;
    private TextView tvTitle, tvPrice, tvGetPrice, tvShare;
    private ProductListBean bean;

    public TemplateGoodsView(Context context) {
        super(context);
    }

    public TemplateGoodsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateGoodsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        findViewById(R.id.template_goods).setOnClickListener(this);
        ivImg = findViewById(R.id.good_img);
        tvTitle = findViewById(R.id.title);
        tvPrice = findViewById(R.id.price);
        tvGetPrice = findViewById(R.id.earn_price);
        tvShare = findViewById(R.id.share);
        tvShare.setOnClickListener(this);
        // 设置图片的宽高
        int w = UIUtils.getWidth() - UIUtils.dip2px(21);
        ivImg.setLayoutParams(new RelativeLayout.LayoutParams(w, (w * 35 / 75)));
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
        tvPrice.setText("¥" + bean.getPrice());
        tvGetPrice.setText("赚" + bean.getEarn_price());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share:
                share();
                break;
            case R.id.template_goods:
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
        DetailActivity.invoke(mContext, bean.getIdcode(), String.valueOf(bean.getProduct_id()));
    }
    // 分享
    private void share(){
        if(null == mContext || null == bean || StringUtil.isEmpty(String.valueOf(bean.getProduct_id()))){
            return;
        }
        ShareUtils.build().loadShare(mContext, String.valueOf(bean.getProduct_id()));
    }

    @Override
    public void onDetached() {

    }


}
