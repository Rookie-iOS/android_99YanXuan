package com.jjshop.ui.model;

import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.AddressListBean;
import com.jjshop.bean.BaseBean;
import com.jjshop.ui.presenter.MyAddressPresenter;
import com.jjshop.ui.presenter.SubmitOrderPresenter;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

/**
 * 作者：张国庆
 * 时间：2018/8/15
 */

public class MyAddressModel extends BaseModel{

    private MyAddressPresenter presenter;

    public MyAddressModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (MyAddressPresenter) basePresenter;
    }

    public void loadData(String shopId, String cookie, String userAgent){
        HttpHelper.bulid().postRequest(HttpUrl.URL_ADDRESS_LIST, AddressListBean.class,
                HttpUrl.build().getAddressList(shopId, cookie, userAgent), new OnRequestCallBack<AddressListBean>() {
                    @Override
                    public void onSuccess(AddressListBean data) {
                        AddressListBean.AddressListData bean = data.getData();
                        if(null == bean){
                            presenter.onFail("没有数据");
                            return;
                        }
                        presenter.onListSuccess(bean);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onFail(msg);
                    }
                });
    }

    public void loadDelData(String shopId, String addressId, String cookie, String userAgent){
        loadDelDefaultData(HttpUrl.URL_ADDRESS_DEL, shopId, addressId, cookie, userAgent);
    }

    public void loadDefaultData(String shopId, String addressId, String cookie, String userAgent){
        loadDelDefaultData(HttpUrl.URL_ADDRESS_DEFAULT, shopId, addressId, cookie, userAgent);
    }

    private void loadDelDefaultData(String url, String shopId, String addressId, String cookie, String userAgent){
        HttpHelper.bulid().postRequest(url, BaseBean.class,
                HttpUrl.build().getAddressDel(shopId, addressId, cookie, userAgent), new OnRequestCallBack<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean data) {
                        presenter.onDelSuccess(data);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onDelFail(msg);
                    }
                });
    }

}
