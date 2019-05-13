package com.jjshop.ui.model;


import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.MyOrderBean;
import com.jjshop.bean.MyOrderBottomBean;
import com.jjshop.bean.MyOrderProductList;
import com.jjshop.bean.MyOrderTopBean;
import com.jjshop.bean.WxPayBean;
import com.jjshop.listener.OnCommonCallBackIm;
import com.jjshop.template_view.TemplateUtil;
import com.jjshop.ui.presenter.OrderPresenter;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class OrderModel extends BaseModel {
    private OrderPresenter presenter;
    public OrderModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (OrderPresenter) basePresenter;
    }

    /**
     * 加载订单列表
     * @param shopId
     * @param status
     * @param page
     */
    public void loadDataList(String shopId, int status, final int page){
        HttpHelper.bulid().getRequest(HttpUrl.build().getPersonMyOrder(shopId, status, page),
                MyOrderBean.class, new OnRequestCallBack<MyOrderBean>() {
                    @Override
                    public void onSuccess(MyOrderBean data) {
                        MyOrderBean.MyOrderData bean = data.getData();
                        if(null == bean){
                            presenter.onErrorList("数据为空");
                            return;
                        }
                        presenter.onSuccessList(bean);
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
     * 取消订单、确认收货
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

    /**
     * 拆分订单列表的数据
     * @param splitList
     * @param list
     */
    public void splitData(ArrayList<MyOrderBean.MyOrderDataList> splitList, ArrayList<Object> list){
        if(null == splitList || splitList.isEmpty() || null == list){
            return;
        }
        for(MyOrderBean.MyOrderDataList data : splitList){
            if(null != data){
                // 顶部数据
                MyOrderTopBean topBean = new MyOrderTopBean();
                topBean.setTemplate(TemplateUtil.TEMPLATE_1010);
                topBean.setOrder_no(data.getOrder_no());
                if(null != data.getShop()){
                    topBean.setShopName(data.getShop().getName());
                }
                topBean.setStatus_str(data.getStatus_str());
                list.add(topBean);
                // 中间的商品数据
                ArrayList<MyOrderProductList> productList = data.getProduct();
                if(null != productList && productList.size() > 0){
                    for(MyOrderProductList contentBean : productList){
                        if(null != contentBean){
                            contentBean.setTemplate(TemplateUtil.TEMPLATE_1007);
                            contentBean.setOrder_no(data.getOrder_no());
                        }
                    }
                    list.addAll(productList);
                }
                // 底部数据
                MyOrderBottomBean bottomBean = new MyOrderBottomBean();
                bottomBean.setTemplate(TemplateUtil.TEMPLATE_1011);
                bottomBean.setOrder_no(data.getOrder_no());
                bottomBean.setPrice(data.getPrice());
                bottomBean.setSum_num(data.getSum_num());
                bottomBean.setAction(data.getAction());
                list.add(bottomBean);
            }
        }
    }

}
