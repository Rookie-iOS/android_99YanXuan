package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.OrderDetailBean;
import com.jjshop.bean.WxPayBean;
import com.jjshop.ui.activity.person.OrderDetailActivity;
import com.jjshop.ui.model.OrderDetailModel;
import com.jjshop.utils.PreUtils;

/**
 * 作者：张国庆
 * 时间：2018/7/20
 */

public class OrderDetailPresenter extends BasePresenter<OrderDetailActivity> {
    private OrderDetailModel model;
    public OrderDetailPresenter(IView iView) {
        super(iView);
        model = (OrderDetailModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new OrderDetailModel(this);
    }

    // 加载订单列表
    public void loadDataDetail(){
        OrderDetailActivity view = getIView();
        if(null != view){
            model.loadDataDetail(PreUtils.getString(view, PreUtils.SHOP_ID), view.orderNo());
        }
    }

    // 列表数据获取成功
    public void onSuccessDetail(OrderDetailBean.OrderDetailData bean){
        OrderDetailActivity view = getIView();
        if(null != view){
            view.onSuccessDetail(bean);
        }
    }

    // 列表数据加载失败
    public void onErrorList(String msg){
        OrderDetailActivity view = getIView();
        if(null != view){
            view.onErrorDetail(msg);
        }
    }

    // 请求微信相关数据
    public void loadPayOrderData(){
        OrderDetailActivity view = getIView();
        if(null != view){
            model.loadPayOrderData(PreUtils.getString(view, PreUtils.SHOP_ID), String.valueOf(view.orderNo()), view.payType());
        }
    }

    // 获取微信相关数据成功
    public void onPayOrderSuccess(WxPayBean.WxPayData bean){
        OrderDetailActivity view = getIView();
        if(null != view){
            view.onPayOrderSuccess(bean);
        }
    }

    // 获取微信相关数据失败
    public void onPayOrderFail(String msg){
        OrderDetailActivity view = getIView();
        if(null != view){
            view.onPayOrderFail(msg);
        }
    }

    // 请求取消订单
    public void loadCancelOrder(){
        OrderDetailActivity view = getIView();
        if(null != view){
            model.loadCancelOrder(PreUtils.getString(view, PreUtils.SHOP_ID),
                    view.orderNo(), view.status());
        }
    }

    // 取消订单成功
    public void onCancelOrderSuccess(BaseBean bean){
        OrderDetailActivity view = getIView();
        if(null != view){
            view.onCancelOrderSuccess(bean);
        }
    }

    // 取消订单失败
    public void onCancelOrderFail(String msg){
        OrderDetailActivity view = getIView();
        if(null != view){
            view.onCancelOrderFail(msg);
        }
    }

}
