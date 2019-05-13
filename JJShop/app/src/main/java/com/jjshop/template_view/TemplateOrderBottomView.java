package com.jjshop.template_view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.bean.MyOrderActionData;
import com.jjshop.bean.MyOrderBottomBean;
import com.jjshop.dialog.CartNumDelDialog;
import com.jjshop.ui.activity.person.MyOrderActivity;
import com.jjshop.ui.activity.person.OrderDetailActivity;
import com.jjshop.ui.activity.person.WuliuInfoActivity;
import com.jjshop.utils.StringUtil;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/7/25
 */

public class TemplateOrderBottomView extends BaseTemplateView{

    private TextView mTvOrderNum, mTvOrderPrice, mTvGoodsNum;
    private TextView mTvLeft, mTvCenter, mTvRight;
    private ArrayList<MyOrderActionData> listAction;
    private MyOrderBottomBean bean;

    private final int CLICK_LEFT = 101;
    private final int CLICK_CENTER = 102;
    private final int CLICK_RIGHT = 103;

    public TemplateOrderBottomView(Context context) {
        super(context);
    }

    public TemplateOrderBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateOrderBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        findViewById(R.id.template_order_bottom).setOnClickListener(this);
        mTvOrderNum = findViewById(R.id.m_tv_order_num);
        mTvOrderPrice = findViewById(R.id.m_tv_order_price);
        mTvGoodsNum = findViewById(R.id.m_tv_order_goods_num);
        mTvLeft = findViewById(R.id.m_tv_order_left);
        mTvCenter = findViewById(R.id.m_tv_order_center);
        mTvRight = findViewById(R.id.m_tv_order_right);
        mTvLeft.setOnClickListener(this);
        mTvCenter.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
    }

    @Override
    public void getDate(Object data, Bundle bundle) {
        if(null == mContext || null == data || !(data instanceof MyOrderBottomBean)){
            return;
        }
        bean = (MyOrderBottomBean) data;
        // 订单号
        mTvOrderNum.setText("订单号:" + bean.getOrder_no());
        // 商品数量
        mTvGoodsNum.setText("共" + bean.getSum_num() + "件商品  总计:");
        // 总价
        mTvOrderPrice.setText("¥" + bean.getPrice());
        // 按钮
        listAction = bean.getAction();
        if(null == listAction || listAction.size() <= 0){
            mTvLeft.setVisibility(GONE);
            mTvCenter.setVisibility(GONE);
            mTvRight.setVisibility(GONE);
        }else{
            int size = listAction.size();
            if(size == 1){
                mTvLeft.setVisibility(GONE);
                mTvCenter.setVisibility(GONE);
                mTvRight.setVisibility(VISIBLE);
                mTvRight.setText(listAction.get(0).getName());
            }else if(size == 2){
                mTvLeft.setVisibility(GONE);
                mTvRight.setVisibility(VISIBLE);
                mTvRight.setText(listAction.get(1).getName());
                mTvCenter.setVisibility(VISIBLE);
                mTvCenter.setText(listAction.get(0).getName());
            }else if(size == 3){
                mTvRight.setVisibility(VISIBLE);
                mTvRight.setText(listAction.get(2).getName());
                mTvCenter.setVisibility(VISIBLE);
                mTvCenter.setText(listAction.get(1).getName());
                mTvLeft.setVisibility(VISIBLE);
                mTvLeft.setText(listAction.get(0).getName());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.template_order_bottom:
                if(null == mContext || null == bean){
                    return;
                }
                OrderDetailActivity.invoke(mContext, bean.getOrder_no());
                break;
            case R.id.m_tv_order_left:
                clickBtn(CLICK_LEFT);
                break;
            case R.id.m_tv_order_center:
                clickBtn(CLICK_CENTER);
                break;
            case R.id.m_tv_order_right:
                clickBtn(CLICK_RIGHT);
                break;
        }
    }

    private void clickBtn(int clickPosition){
        if(null == listAction || listAction.size() <= 0){
            return;
        }
        int size = listAction.size();
        MyOrderActionData bean;
        if(size == 1){
            clickPosition = 0;
        }else if(size == 2){
            if(clickPosition == CLICK_RIGHT){
                clickPosition = 1;
            }else if(clickPosition == CLICK_CENTER){
                clickPosition = 0;
            }
        }else if(size == 3){
            if(clickPosition == CLICK_RIGHT){
                clickPosition = 2;
            }else if(clickPosition == CLICK_CENTER){
                clickPosition = 1;
            }else if(clickPosition == CLICK_LEFT){
                clickPosition = 0;
            }
        }
        bean = listAction.get(clickPosition);
        if(null != bean){
            invoke(bean.getStatus(), bean.getName(), bean.getParameter());
        }
    }

    public void invoke(int status, String statuaName, final MyOrderActionData.MyOrderActionParameter pdata){
        if(null == mContext || null == bean){
            return;
        }
        long orderNo = bean.getOrder_no();
        switch (status){
            case -10:// 取消订单
            case 50:// 查看物流、确定收货
                if(null == pdata){
                    return;
                }
                // 取消订单
                if(!StringUtil.isEmpty(statuaName) && statuaName.equals("确定收货") || status == -10){// 取消订单
                    CartNumDelDialog.build().showView(((FragmentActivity)mContext).getSupportFragmentManager(), 0,
                            CartNumDelDialog.CANCEL_ORDER_CODE).setOnCommonClickCalllBack(new CartNumDelDialog.OnCommonClickCalllBack() {
                        @Override
                        public void callBack(int typeCode, int num) {
                            if(typeCode == CartNumDelDialog.CANCEL_ORDER_CODE){
                                if(mContext instanceof MyOrderActivity){
                                    ((MyOrderActivity)mContext).loadCancelOrder(bean.getOrder_no(), pdata.getStatus());
                                }
                            }
                        }
                    });
                    return;
                }
                // 查看物流
                WuliuInfoActivity.invoke(mContext, orderNo);
                break;
            case 20:// 支付订单
                if(mContext instanceof MyOrderActivity){
                    ((MyOrderActivity)mContext).loadDataWxPay(bean.getOrder_no(), "");
                }
                break;
            case 0:// 订单详情
                OrderDetailActivity.invoke(mContext, orderNo);
                break;
        }
    }

    @Override
    public void onDetached() {

    }
}
