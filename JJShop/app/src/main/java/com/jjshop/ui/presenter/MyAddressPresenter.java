package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.AddressListBean;
import com.jjshop.bean.BaseBean;
import com.jjshop.ui.activity.person.MyAddressActivity;
import com.jjshop.ui.model.MyAddressModel;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.httputil.HttpUrl;

/**
 * 作者：张国庆
 * 时间：2018/8/15
 */

public class MyAddressPresenter extends BasePresenter<MyAddressActivity>{
    private MyAddressModel model;

    public MyAddressPresenter(IView iView) {
        super(iView);
        model = (MyAddressModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new MyAddressModel(this);
    }

    public void loadData(){
        MyAddressActivity view = getIView();
        if(null != view){
            model.loadData(PreUtils.getString(view, PreUtils.SHOP_ID), HttpUrl.getCookies(view),
                    HttpUrl.getUserAgent(view));
        }
    }

    public void loadDelData(){
        MyAddressActivity view = getIView();
        if(null != view){
            model.loadDelData(PreUtils.getString(view, PreUtils.SHOP_ID), view.addressId(),
                    HttpUrl.getCookies(view), HttpUrl.getUserAgent(view));
        }
    }

    public void loadDefaultData(){
        MyAddressActivity view = getIView();
        if(null != view){
            model.loadDefaultData(PreUtils.getString(view, PreUtils.SHOP_ID), view.addressId(),
                    HttpUrl.getCookies(view), HttpUrl.getUserAgent(view));
        }
    }

    public void onListSuccess(AddressListBean.AddressListData bean){
        MyAddressActivity view = getIView();
        if(null != view){
            view.onListSuccess(bean);
        }
    }

    public void onDelSuccess(BaseBean bean){
        MyAddressActivity view = getIView();
        if(null != view){
            view.onDelSuccess(bean);
        }
    }

    public void onDelFail(String msg){
        MyAddressActivity view = getIView();
        if(null != view){
            view.onDelFail(msg);
        }
    }

    public void onFail(String msg){
        MyAddressActivity view = getIView();
        if(null != view){
            view.onFail(msg);
        }
    }
}
