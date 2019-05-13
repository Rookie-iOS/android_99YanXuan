package com.jjshop.ui.model;

import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.PersonCenterBean;
import com.jjshop.ui.presenter.PersonCenterPresenter;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class PersonCenterModel extends BaseModel {
    private PersonCenterPresenter presenter;
    public PersonCenterModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (PersonCenterPresenter) basePresenter;
    }

    public void loadData(String shopId, String cookie, String userAgent){
        HttpHelper.bulid().postRequest(HttpUrl.URL_PERSON_CENTER, PersonCenterBean.class,
                HttpUrl.build().getPersonCent(shopId, cookie, userAgent),
                new OnRequestCallBack<PersonCenterBean>() {
                    @Override
                    public void onSuccess(PersonCenterBean bean) {
                        PersonCenterBean.PersonCenterData data = bean.getData();
                        if(null == data){
                            presenter.onFail("没有数据");
                            return;
                        }
                        presenter.onSuccess(data);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onFail(msg);
                    }
                });
    }

}
