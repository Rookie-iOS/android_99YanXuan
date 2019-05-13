package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.CitysPickerBean;
import com.jjshop.ui.activity.person.UpdateAddAddressActivity;
import com.jjshop.ui.model.UpdateAddressModel;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.httputil.HttpUrl;

/**
 * 作者：张国庆
 * 时间：2018/8/15
 */

public class UpdateAddressPresenter extends BasePresenter<UpdateAddAddressActivity>{
    private UpdateAddressModel model;

    public UpdateAddressPresenter(IView iView) {
        super(iView);
        model = (UpdateAddressModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new UpdateAddressModel(this);
    }

    public void loadAddData(){
        UpdateAddAddressActivity view = getIView();
        if(null != view){
            model.loadAddData(PreUtils.getString(view, PreUtils.SHOP_ID), view.name(), view.mobile(),
                    view.cityIds(), view.address(), view.isDefault(), HttpUrl.getCookies(view),
                    HttpUrl.getUserAgent(view));
        }
    }

    public void loadUpdateData(){
        UpdateAddAddressActivity view = getIView();
        if(null != view){
            model.loadUpdateData(PreUtils.getString(view, PreUtils.SHOP_ID), view.addressId(), view.name(),
                    view.mobile(), view.cityIds(), view.address(), view.isDefault(), HttpUrl.getCookies(view),
                    HttpUrl.getUserAgent(view));
        }
    }

    public void loadLocalCityData(){
        UpdateAddAddressActivity view = getIView();
        if(null != view){
            model.loadLocalCitysData(view.dbManager());
        }

    }

    public void onSuccess(int type){
        UpdateAddAddressActivity view = getIView();
        if(null != view){
            view.onSuccess(type);
        }
    }

    public void onLocalCitySuccess(CitysPickerBean bean){
        UpdateAddAddressActivity view = getIView();
        if(null != view){
            view.onLocalCitySuccess(bean);
        }
    }

    public void onFail(String msg){
        UpdateAddAddressActivity view = getIView();
        if(null != view){
            view.onFail(msg);
        }
    }
}
