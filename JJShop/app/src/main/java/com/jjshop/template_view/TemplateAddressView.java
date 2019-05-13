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
import com.jjshop.bean.AddressListBean;
import com.jjshop.bean.CartDataBean;
import com.jjshop.dialog.CartNumDelDialog;
import com.jjshop.ui.activity.home.DetailActivity;
import com.jjshop.ui.activity.home.HomeActivity;
import com.jjshop.ui.activity.person.MyAddressActivity;
import com.jjshop.ui.activity.person.UpdateAddAddressActivity;
import com.jjshop.ui.fragment.ShopCarFragment;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.glide_img.GlideUtil;

/**
 * 作者：张国庆
 * 时间：2018/7/25
 */

public class TemplateAddressView extends BaseTemplateView{

    private ImageView mIvDefault;
    private TextView mTvName, mTvMobile, mTvAddress, mTvDefault, mTvEdit, mTvDel;
    private AddressListBean.AddressList bean;

    public TemplateAddressView(Context context) {
        super(context);
    }

    public TemplateAddressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateAddressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        findViewById(R.id.template_address).setOnClickListener(this);
        mTvName = findViewById(R.id.temp_tv_address_name);
        mTvMobile = findViewById(R.id.temp_tv_address_mobile);
        mTvAddress = findViewById(R.id.temp_tv_address);
        mIvDefault = findViewById(R.id.temp_iv_default);
        mTvDefault = findViewById(R.id.temp_tv_default);
        mTvEdit = findViewById(R.id.temp_tv_edit);
        mTvDel = findViewById(R.id.temp_tv_del);

        mIvDefault.setOnClickListener(this);
        mTvDefault.setOnClickListener(this);
        mTvEdit.setOnClickListener(this);
        mTvDel.setOnClickListener(this);
    }

    @Override
    public void getDate(Object data, Bundle bundle) {
        if(null == mContext || null == data || !(data instanceof AddressListBean.AddressList)){
            return;
        }
        bean = (AddressListBean.AddressList) data;
        mTvName.setText(bean.getAccept_name());
        mTvMobile.setText(bean.getMobile());
        mTvAddress.setText(bean.getProvince_name() + bean.getCity_name() + bean.getArea_name() + bean.getAddress());
        if(bean.getIs_default() == 2){
            mIvDefault.setImageResource(R.mipmap.img_cart_check_true);
        }else{
            mIvDefault.setImageResource(R.mipmap.img_cart_check_false);
        }
    }

    @Override
    public void onClick(View v) {
        if(null == bean || null == mContext){
            return;
        }
        switch (v.getId()){
            case R.id.template_address:// item点击
                if(mContext instanceof MyAddressActivity){
                    ((MyAddressActivity)mContext).selectAddress(bean.getId_code());
                }
                break;
            case R.id.temp_iv_default:// 设置默认
            case R.id.temp_tv_default:// 设置默认
                if(mContext instanceof MyAddressActivity){
                    ((MyAddressActivity)mContext).loadDefaultData(bean.getId_code());
                }
                break;
            case R.id.temp_tv_edit:// 编辑
                if(mContext instanceof MyAddressActivity){
                    ((MyAddressActivity)mContext).editAddress(bean);
                }
                break;
            case R.id.temp_tv_del:// 删除
                if(mContext instanceof MyAddressActivity){
                    ((MyAddressActivity)mContext).loadDelData(bean.getId_code());
                }
                break;
        }
    }

    @Override
    public void onDetached() {

    }
}
