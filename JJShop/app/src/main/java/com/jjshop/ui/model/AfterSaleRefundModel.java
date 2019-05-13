package com.jjshop.ui.model;


import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.AfterSaleRefundBean;
import com.jjshop.ui.presenter.AfterSaleRefundPresenter;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class AfterSaleRefundModel extends BaseModel {
    private AfterSaleRefundPresenter presenter;
    public AfterSaleRefundModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (AfterSaleRefundPresenter) basePresenter;
    }

    /**
     * 加载订单列表
     * @param shopId
     * @param page
     */
    public void loadDataList(String shopId, final int page){
        HttpHelper.bulid().getRequest(HttpUrl.build().getAterSaleRefund(shopId, page),
                AfterSaleRefundBean.class, new OnRequestCallBack<AfterSaleRefundBean>() {
                    @Override
                    public void onSuccess(AfterSaleRefundBean data) {
                        presenter.onSuccessList(data);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onErrorList(msg);
                    }
                });
    }

}
