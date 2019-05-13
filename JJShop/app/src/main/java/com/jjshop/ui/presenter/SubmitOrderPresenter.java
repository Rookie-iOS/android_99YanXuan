package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.GetOrderIdBean;
import com.jjshop.bean.SubmitOrderBean;
import com.jjshop.bean.WxPayBean;
import com.jjshop.ui.activity.home.SubmitOrderActivity;
import com.jjshop.ui.model.SubmitOrderModel;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.httputil.HttpUrl;

/**
 * 作者：张国庆
 * 时间：2018/8/15
 */

public class SubmitOrderPresenter extends BasePresenter<SubmitOrderActivity>{
    private SubmitOrderModel model;

    public SubmitOrderPresenter(IView iView) {
        super(iView);
        model = (SubmitOrderModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new SubmitOrderModel(this);
    }

    // 请求订单信息
    public void loadData(){
        SubmitOrderActivity view = getIView();
        if(null != view){
            model.loadData(PreUtils.getString(view, PreUtils.SHOP_ID), view.address(), view.quanIdcode(),view.getBalance(),
                    HttpUrl.getCookies(view), HttpUrl.getUserAgent(view));
        }
    }

    // 获取订单信息成功
    public void onSuccess(SubmitOrderBean.SubmitOrderData bean){
        SubmitOrderActivity view = getIView();
        if(null != view){
            view.onSuccess(bean);
        }
    }

    // 获取订单信息失败
    public void onFail(String msg){
        SubmitOrderActivity view = getIView();
        if(null != view){
            view.onFail(msg);
        }
    }

    // 请求提交订单
    public void loadSubmitOrderData(){
        SubmitOrderActivity view = getIView();
        if(null != view){
            model.loadSubmitOrderData(PreUtils.getString(view, PreUtils.SHOP_ID), view.addressIdcode(), view.quanIdcode(),
                    view.order_remarks(), view.getBalance(), HttpUrl.getCookies(view), HttpUrl.getUserAgent(view));
        }
    }

    // 提交订单成功
    public void onSubmitOrderSuccess(GetOrderIdBean.GetOrderIdData bean){
        SubmitOrderActivity view = getIView();
        if(null != view){
            view.onSubmitOrderSuccess(bean);
        }
    }

    // 提交订单失败
    public void onSubmitOrderFail(String msg){
        SubmitOrderActivity view = getIView();
        if(null != view){
            view.onSubmitOrderFail(msg);
        }
    }

    // 请求微信相关数据
    public void loadPayOrderData(){
        SubmitOrderActivity view = getIView();
        if(null != view){
            model.loadPayOrderData(PreUtils.getString(view, PreUtils.SHOP_ID), view.payment_no(), view.payment_type(),
                    HttpUrl.getCookies(view), HttpUrl.getUserAgent(view));
        }
    }

    // 获取微信相关数据成功
    public void onPayOrderSuccess(WxPayBean.WxPayData bean){
        SubmitOrderActivity view = getIView();
        if(null != view){
            view.onPayOrderSuccess(bean);
        }
    }

    // 获取微信相关数据失败
    public void onPayOrderFail(String msg){
        SubmitOrderActivity view = getIView();
        if(null != view){
            view.onPayOrderFail(msg);
        }
    }

    public boolean isNotDistribution(){
        SubmitOrderActivity view = getIView();
        if(null != view){
            return model.isNotDistribution(view.listGoods());
        }
        return false;
    }
}
