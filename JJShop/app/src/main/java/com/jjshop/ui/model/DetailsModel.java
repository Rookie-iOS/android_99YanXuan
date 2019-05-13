package com.jjshop.ui.model;


import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.AddCartBean;
import com.jjshop.bean.DetailBean;
import com.jjshop.ui.presenter.DetailsPresenter;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class DetailsModel extends BaseModel {
    private DetailsPresenter presenter;
    public DetailsModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (DetailsPresenter) basePresenter;
    }

    public void loadData(String idCode, String shopId, String cookie, String userAgent){
        HttpHelper.bulid().getRequest(HttpUrl.build().getDetails(idCode, shopId),
                DetailBean.class, new OnRequestCallBack<DetailBean>() {
                    @Override
                    public void onSuccess(DetailBean data) {
                        presenter.onSuccess(data);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onFail(msg);
                    }
                });
    }

    public void loadAddCartOrBuyData(String shopId, String sku, final int num, final int type, String cookie, String userAgent){
        HttpHelper.bulid().postRequest(HttpUrl.URL_ADD_CART, AddCartBean.class,
                HttpUrl.build().getAddCart(shopId, sku, num, type, cookie, userAgent),
                new OnRequestCallBack<AddCartBean>() {
                    @Override
                    public void onSuccess(AddCartBean data) {
                        AddCartBean.AddCartData bean = data.getData();
                        if(null == bean){
                            presenter.onAddCartFail("没有数据");
                            return;
                        }
                        presenter.onAddCartSuccess(bean, type);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onAddCartFail(msg);
                    }
                });
    }

}
