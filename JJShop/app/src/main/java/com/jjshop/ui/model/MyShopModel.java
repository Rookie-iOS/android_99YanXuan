package com.jjshop.ui.model;

import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.MyShopBean;
import com.jjshop.ui.presenter.MyShopPresenter;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class MyShopModel extends BaseModel {
    private MyShopPresenter presenter;
    public MyShopModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (MyShopPresenter) basePresenter;
    }

    public void loadData(String shopId, String cookie, String userAgent){
        HttpHelper.bulid().getRequest(HttpUrl.build().getMyShop(shopId), MyShopBean.class,
                new OnRequestCallBack<MyShopBean>() {
                    @Override
                    public void onSuccess(MyShopBean data) {
                        MyShopBean.MyShopDataBean bean = data.getData();
                        if(null == bean){
                            presenter.onFail("没有数据");
                            return;
                        }
                        presenter.onSuccess(bean);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onFail(msg);
                    }
                });
    }

}
