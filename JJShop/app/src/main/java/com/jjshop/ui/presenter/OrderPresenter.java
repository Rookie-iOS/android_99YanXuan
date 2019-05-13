package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.MyOrderBean;
import com.jjshop.bean.WxPayBean;
import com.jjshop.ui.fragment.OrderFragment;
import com.jjshop.ui.model.OrderModel;
import com.jjshop.utils.PreUtils;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/7/20
 */

public class OrderPresenter extends BasePresenter<OrderFragment> {
    private OrderModel model;
    public OrderPresenter(IView iView) {
        super(iView);
        model = (OrderModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new OrderModel(this);
    }

    // 加载订单列表
    public void loadData(){
        OrderFragment view = getIView();
        if(null != view){
            model.loadDataList(PreUtils.getString(view.getActivity(), PreUtils.SHOP_ID), view.id(), view.page());
        }
    }

    // 列表数据获取成功
    public void onSuccessList(MyOrderBean.MyOrderData bean){
        OrderFragment view = getIView();
        if(null != view){
            view.onSuccessList(bean);
        }
    }

    // 列表数据加载失败
    public void onErrorList(String msg){
        OrderFragment view = getIView();
        if(null != view){
            view.onErrorList(msg);
        }
    }

    // 请求微信相关数据
    public void loadPayOrderData(){
        OrderFragment view = getIView();
        if(null != view){
            model.loadPayOrderData(PreUtils.getString(view.getActivity(), PreUtils.SHOP_ID), String.valueOf(view.orderNo()), view.payType());
        }
    }

    // 获取微信相关数据成功
    public void onPayOrderSuccess(WxPayBean.WxPayData bean){
        OrderFragment view = getIView();
        if(null != view){
            view.onPayOrderSuccess(bean);
        }
    }

    // 获取微信相关数据失败
    public void onPayOrderFail(String msg){
        OrderFragment view = getIView();
        if(null != view){
            view.onPayOrderFail(msg);
        }
    }

    // 请求取消订单
    public void loadCancelOrder(){
        OrderFragment view = getIView();
        if(null != view){
            model.loadCancelOrder(PreUtils.getString(view.getActivity(), PreUtils.SHOP_ID),
                    view.orderNo(), view.status());
        }
    }

    // 取消订单成功
    public void onCancelOrderSuccess(BaseBean bean){
        OrderFragment view = getIView();
        if(null != view){
            view.onCancelOrderSuccess(bean);
        }
    }

    // 取消订单失败
    public void onCancelOrderFail(String msg){
        OrderFragment view = getIView();
        if(null != view){
            view.onCancelOrderFail(msg);
        }
    }

    // 拆分订单列表数据
    public void splitData(ArrayList<MyOrderBean.MyOrderDataList> splitList){
        OrderFragment view = getIView();
        if(null != view){
            model.splitData(splitList, view.mListData());
        }

    }

}
