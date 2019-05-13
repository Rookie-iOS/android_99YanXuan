package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.RushBuyBean;
import com.jjshop.ui.activity.home.RushBuyActivity;
import com.jjshop.ui.model.RushBuyModel;

/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class RushBuyPresenter extends BasePresenter<RushBuyActivity> {
    RushBuyModel model;

    public RushBuyPresenter(IView iView) {
        super(iView);
        model = (RushBuyModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new RushBuyModel(this);
    }

    public void loadGoods(){
        RushBuyActivity view = getIView();
        if(null != view){
            model.loadData(view.shopId());
        }
    }

    public void onSuccess(RushBuyBean bean){
        RushBuyActivity view = getIView();
        if(null != view){
            view.onSuccess(bean);
        }
    }
    public void onFail(String str){
        RushBuyActivity view = getIView();
        if(null != view){
            view.onFail(str);
        }
    }
}
