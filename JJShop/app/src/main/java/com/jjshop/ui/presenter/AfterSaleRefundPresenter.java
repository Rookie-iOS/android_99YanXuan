package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.AfterSaleRefundBean;
import com.jjshop.ui.activity.person.AfterSaleRefundActivity;
import com.jjshop.ui.model.AfterSaleRefundModel;
import com.jjshop.utils.PreUtils;

/**
 * 作者：张国庆
 * 时间：2018/7/20
 */

public class AfterSaleRefundPresenter extends BasePresenter<AfterSaleRefundActivity> {
    private AfterSaleRefundModel model;
    public AfterSaleRefundPresenter(IView iView) {
        super(iView);
        model = (AfterSaleRefundModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new AfterSaleRefundModel(this);
    }

    // 加载订单列表
    public void loadData(){
        AfterSaleRefundActivity view = getIView();
        if(null != view){
            model.loadDataList(PreUtils.getString(view, PreUtils.SHOP_ID), view.page());
        }
    }

    // 列表数据获取成功
    public void onSuccessList(AfterSaleRefundBean bean){
        AfterSaleRefundActivity view = getIView();
        if(null != view){
            view.onSuccessList(bean);
        }
    }

    // 列表数据加载失败
    public void onErrorList(String msg){
        AfterSaleRefundActivity view = getIView();
        if(null != view){
            view.onErrorList(msg);
        }
    }

}
