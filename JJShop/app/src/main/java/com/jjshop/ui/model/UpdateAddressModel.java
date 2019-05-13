package com.jjshop.ui.model;

import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.CitysPickerBean;
import com.jjshop.listener.OnCommonCallBackIm;
import com.jjshop.ui.presenter.UpdateAddressPresenter;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.Tools;
import com.jjshop.utils.dbutils.DBManager;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

/**
 * 作者：张国庆
 * 时间：2018/8/15
 */

public class UpdateAddressModel extends BaseModel{

    private UpdateAddressPresenter presenter;

    public UpdateAddressModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (UpdateAddressPresenter) basePresenter;
    }


    public void loadAddData(String shopId, String accept_name, String accept_phone,
                            String cityids, String address, int is_default, String cookie, String userAgent){
        loadData(HttpUrl.URL_ADDRESS_ADD, shopId, -1, accept_name, accept_phone,
                cityids, address, is_default,cookie, userAgent);
    }

    public void loadUpdateData(String shopId, int id, String accept_name, String accept_phone,
                            String cityids, String address, int is_default, String cookie, String userAgent){
        loadData(HttpUrl.URL_ADDRESS_UPDATE, shopId, id, accept_name, accept_phone, cityids,
                address, is_default,cookie, userAgent);
    }


    private void loadData(String url, String shopId, final int id, String accept_name, String accept_phone,
                          String cityids, String address, int is_default, String cookie, String userAgent){
        if(StringUtil.isEmpty(accept_name)){
            presenter.onFail("名字不能为空");
            return;
        }
        if(StringUtil.isEmpty(accept_phone) || accept_phone.length() < 11 || !Tools.isMobileNum(accept_phone)){
            presenter.onFail("手机号不正确");
            return;
        }
        if(StringUtil.isEmpty(cityids)){
            presenter.onFail("请选择城市");
            return;
        }
        if(StringUtil.isEmpty(address)){
            presenter.onFail("请输入详细地址");
            return;
        }
        if(address.length() < 5){
            presenter.onFail("详细地址需要大于5个字");
            return;
        }
        HttpHelper.bulid().postRequest(url, BaseBean.class,
                HttpUrl.build().getAddressAddUpdate(shopId, id, accept_name, accept_phone,
                        cityids, address, is_default,cookie, userAgent),
                new OnRequestCallBack<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean data) {
                        presenter.onSuccess(id);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onFail(msg);
                    }
                });
    }

    public void loadLocalCitysData(DBManager dbManager){
        CommonUtils.build().loadLocalCitysData(dbManager, new OnCommonCallBackIm() {
            @Override
            public void onCitysSuccess(CitysPickerBean bean) {
                presenter.onLocalCitySuccess(bean);
            }

            @Override
            public void onCitysError(String msg) {
                presenter.onFail(msg);
            }
        });
    }
}
