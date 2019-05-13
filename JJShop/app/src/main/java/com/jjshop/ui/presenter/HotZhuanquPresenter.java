package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.HotZhuanquBean;
import com.jjshop.ui.activity.home.HotZhuanquActivity;
import com.jjshop.ui.model.HotZhuanquModel;

/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class HotZhuanquPresenter extends BasePresenter<HotZhuanquActivity> {
    HotZhuanquModel model;

    public HotZhuanquPresenter(IView iView) {
        super(iView);
        model = (HotZhuanquModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new HotZhuanquModel(this);
    }

    public void loadData(){
        HotZhuanquActivity view = getIView();
        if(null != view){
            model.loadData(view.shopId(), view.type(), view.page());
        }
    }

    public void onSuccess(HotZhuanquBean bean){
        HotZhuanquActivity view = getIView();
        if(null != view){
            view.onSuccess(bean);
        }
    }
    public void onFail(String str){
        HotZhuanquActivity view = getIView();
        if(null != view){
            view.onFail(str);
        }
    }

}
