package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.RushBuyBean;
import com.jjshop.ui.fragment.RushBuyFragment;
import com.jjshop.ui.model.RushBuyFragmentModel;

/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class RushBuyFragmentPresenter extends BasePresenter<RushBuyFragment> {
    RushBuyFragmentModel model;

    public RushBuyFragmentPresenter(IView iView) {
        super(iView);
        model = (RushBuyFragmentModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new RushBuyFragmentModel(this);
    }

    public void loadGoods(){
        RushBuyFragment view = getIView();
        if(null != view){
            model.loadData(view.shopId(), view.timeId(), view.page());
        }
    }

    public void onSuccess(RushBuyBean bean){
        RushBuyFragment view = getIView();
        if(null != view){
            view.onSuccess(bean);
        }
    }
    public void onFail(String str){
        RushBuyFragment view = getIView();
        if(null != view){
            view.onFail(str);
        }
    }
}
