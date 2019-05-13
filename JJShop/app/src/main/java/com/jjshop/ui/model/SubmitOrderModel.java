package com.jjshop.ui.model;

import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.CartDataBean;
import com.jjshop.bean.GetOrderIdBean;
import com.jjshop.bean.SubmitOrderBean;
import com.jjshop.bean.WxPayBean;
import com.jjshop.listener.OnCommonCallBackIm;
import com.jjshop.ui.presenter.SubmitOrderPresenter;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/8/15
 */

public class SubmitOrderModel extends BaseModel{

    private SubmitOrderPresenter presenter;

    public SubmitOrderModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (SubmitOrderPresenter) basePresenter;
    }

    public void loadData(String shopId, String address, String quanIdcode, String use_balance, String cookie, String userAgent){
        HttpHelper.bulid().getRequest(HttpUrl.build().getCommitOrderLast(shopId, address, quanIdcode,use_balance),
                SubmitOrderBean.class, new OnRequestCallBack<SubmitOrderBean>() {
                    @Override
                    public void onSuccess(SubmitOrderBean data) {
                        SubmitOrderBean.SubmitOrderData bean = data.getData();
                        if(null == bean){
                            presenter.onFail("没有数据");
                            return;
                        }
                        presenter.onSuccess(bean);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onFail(msg);
                    }
                });
    }

    /**
     * 提交订单
     * @param shopId
     * @param address       地址id
     * @param quanIdcode    券id
     * @param order_remarks 留言
     * @param cookie
     * @param userAgent
     */
    public void loadSubmitOrderData(String shopId, String address, String quanIdcode,
                                    String order_remarks, String balance, String cookie, String userAgent){
        HttpHelper.bulid().postRequest(HttpUrl.URL_SUBMIT_ORDER, GetOrderIdBean.class,
                HttpUrl.build().getSubmitOrderInfo(shopId, address, quanIdcode, balance, order_remarks,
                        cookie, userAgent), new OnRequestCallBack<GetOrderIdBean>() {
                    @Override
                    public void onSuccess(GetOrderIdBean data) {
                        GetOrderIdBean.GetOrderIdData bean = data.getData();
                        if(null == bean){
                            presenter.onSubmitOrderFail("没有数据");
                            return;
                        }
                        presenter.onSubmitOrderSuccess(bean);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onSubmitOrderFail(msg);
                    }
                });
    }

    /**
     * 微信支付
     * @param shopId
     * @param payment_no    订单id
     * @param payment_type  支付类型
     * @param cookie
     * @param userAgent
     */
    public void loadPayOrderData(String shopId, String payment_no, String payment_type, String cookie, String userAgent){
        CommonUtils.build().loadWxPayOrderData(shopId, payment_no, payment_type, true, new OnCommonCallBackIm() {
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
     * 是否有不可配送的商品
     * @param list
     * @return
     */
    public boolean isNotDistribution(ArrayList<Object> list){
        if(null == list || list.size() <= 0){
            return true;
        }
        for(Object o : list){
            if(null != o && o instanceof CartDataBean){
                CartDataBean bean = (CartDataBean) o;
                if(bean.isNot_distribution()){
                    return true;
                }
            }
        }
        return false;
    }
}
