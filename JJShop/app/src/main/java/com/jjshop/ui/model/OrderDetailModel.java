package com.jjshop.ui.model;


import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.OrderDetailBean;
import com.jjshop.bean.WxPayBean;
import com.jjshop.listener.OnCommonCallBackIm;
import com.jjshop.ui.presenter.OrderDetailPresenter;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class OrderDetailModel extends BaseModel {
    private OrderDetailPresenter presenter;
    public OrderDetailModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (OrderDetailPresenter) basePresenter;
    }

    public void loadDataDetail(String shopId, long no){
        HttpHelper.bulid().getRequest(HttpUrl.build().getPersonOrderDetail(shopId, no),
                OrderDetailBean.class, new OnRequestCallBack<OrderDetailBean>() {
                    @Override
                    public void onSuccess(OrderDetailBean data) {
                        OrderDetailBean.OrderDetailData bean = data.getData();
                        if(null == bean){
                            presenter.onErrorList("数据为空");
                            return;
                        }
                        presenter.onSuccessDetail(bean);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onErrorList(msg);
                    }
                });
    }

    /**
     * 微信支付
     * @param shopId
     * @param payment_no    订单id
     * @param payment_type  支付类型
     */
    public void loadPayOrderData(String shopId, String payment_no, String payment_type) {
        CommonUtils.build().loadWxPayOrderData(shopId, payment_no, payment_type, new OnCommonCallBackIm() {
            @Override
            public void onWxPayDataSuccess(WxPayBean.WxPayData data) {
                presenter.onPayOrderSuccess(data);
            }

            @Override
            public void onWxPayDataError(String msg) {
                presenter.onPayOrderFail(msg);
            }
        });
    }

    /**
     * 取消订单
     * @param shopId
     * @param orderNo
     */
    public void loadCancelOrder(String shopId, long orderNo, String status){
        CommonUtils.build().loadCancelOrder(shopId, orderNo, status, new OnCommonCallBackIm() {
            @Override
            public void onCanceOrderSuccess(BaseBean data) {
                presenter.onCancelOrderSuccess(data);
            }

            @Override
            public void onCanceOrderError(String msg) {
                presenter.onCancelOrderFail(msg);
            }
        });
    }

}
