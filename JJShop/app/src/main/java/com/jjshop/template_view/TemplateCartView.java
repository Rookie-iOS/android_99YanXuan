package com.jjshop.template_view;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.bean.CartDataBean;
import com.jjshop.dialog.CartNumDelDialog;
import com.jjshop.ui.activity.home.DetailActivity;
import com.jjshop.ui.activity.home.HomeActivity;
import com.jjshop.ui.fragment.ShopCarFragment;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.UIUtils;
import com.jjshop.utils.glide_img.GlideUtil;

/**
 * 作者：张国庆
 * 时间：2018/7/25
 */

public class TemplateCartView extends BaseTemplateView{

    private ImageView mIvCheck, mIvImg, mIvDel, mIvAdd;
    private TextView mTvTitle, mTvGuige, mTvPrice, mTvOldPrice, mTvNum, mTvAllPrice, mTvRush;
    private LinearLayout mLayoutLeft;
    private CartDataBean bean;

    public TemplateCartView(Context context) {
        super(context);
    }

    public TemplateCartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateCartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        findViewById(R.id.template_cart).setOnClickListener(this);
        mIvCheck = findViewById(R.id.m_iv_check);
        mLayoutLeft = findViewById(R.id.m_layout_left);
        mLayoutLeft.setOnClickListener(this);
        mIvImg = findViewById(R.id.m_iv_img);
        mTvTitle = findViewById(R.id.m_tv_title);
        mTvGuige = findViewById(R.id.m_tv_guige);
        mTvPrice = findViewById(R.id.m_tv_price);
        mTvOldPrice = findViewById(R.id.m_tv_old_price);
        mIvDel = findViewById(R.id.m_tv_del);
        mIvDel.setOnClickListener(this);
        mTvNum = findViewById(R.id.m_tv_num);
        mTvNum.setOnClickListener(this);
        mIvAdd = findViewById(R.id.m_tv_add);
        mIvAdd.setOnClickListener(this);
        mTvAllPrice = findViewById(R.id.m_tv_all_price);
        mTvRush = findViewById(R.id.m_tv_rush);
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
        mTvNum.setText(String.valueOf(num));
        // 小计
        mTvAllPrice.setText("¥" + bean.getTotal_price());
        // 是否选中
        mIvCheck.setImageResource(bean.getSelected() == 2 ?
                R.mipmap.img_cart_check_true : R.mipmap.img_cart_check_false);
        // 是否显示限时秒杀标识
        CartDataBean.PromotionsData promotionsData = bean.getPromotions();
        if(null != promotionsData && !StringUtil.isEmpty(promotionsData.getEtime())){
            mTvRush.setVisibility(VISIBLE);
            mTvRush.setText("限时  " + promotionsData.getEtime() + "  结束");
        }else{
            mTvRush.setVisibility(GONE);
        }

    }

    @Override
    public void onClick(View v) {
        if(null == bean){
            return;
        }
        switch (v.getId()){
            case R.id.m_layout_left:// 选中、非选中
                if(null != cartFragment() && null != bean){
                    cartFragment().loadSelectData(bean.getSelected(), bean.getCart_id() + ",");
                }
                break;
            case R.id.m_tv_num:// 数量输入框
                CartNumDelDialog.build().showView(((FragmentActivity) mContext).getSupportFragmentManager(), bean.getNum())
                        .setOnCommonClickCalllBack(new CartNumDelDialog.OnCommonClickCalllBack() {
                            @Override
                            public void callBack(int typeCode, int num) {
                                if(typeCode == CartNumDelDialog.SURE_CODE){
                                    addOrDel(num);
                                }
                            }
                        });
                break;
            case R.id.m_tv_del:// 减少数量
                if(bean.getNum() == 1){// 数量为1时，弹出删除窗口
                    CartNumDelDialog.build().showView(((FragmentActivity) mContext).getSupportFragmentManager(), -1, CartNumDelDialog.DEL_GOODS)
                            .setOnCommonClickCalllBack(new CartNumDelDialog.OnCommonClickCalllBack() {
                                @Override
                                public void callBack(int typeCode, int num) {
                                    if(typeCode == CartNumDelDialog.DEL_GOODS) {
                                        if (null != cartFragment()) {
                                            cartFragment().loadDelData(bean.getCart_id() + ",");
                                        }
                                    }
                                }
                            });
                    return;
                }
                addOrDel(bean.getNum() - 1);
                break;
            case R.id.m_tv_add:// 增加数量
                addOrDel(bean.getNum() + 1);
                break;
            case R.id.template_cart:// 进入详情页
                if(null != cartFragment() && cartFragment().isDelStatus()){// 删除状态
                    return;
                }
                DetailActivity.invoke(mContext, bean.getIdcode(), String.valueOf(bean.getId()));
                break;
        }
    }

    // 获取当前引用的Fragment
    private ShopCarFragment cartFragment(){
        if(null == mContext || !(mContext instanceof HomeActivity)){
            return null;
        }
        Fragment fragment = ((HomeActivity)mContext).getCurrFragment();
        if(null == fragment || !(fragment instanceof ShopCarFragment)){
            return null;
        }
        return ((ShopCarFragment)fragment);
    }

    // 增加数量、减少数量
    private void addOrDel(int num){
        if(null == cartFragment() || null == bean){
            return;
        }
        cartFragment().loadUpdateNumData(num, bean);

    }

    @Override
    public void onDetached() {

    }
}
