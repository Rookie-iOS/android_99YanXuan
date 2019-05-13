package com.jjshop.ui.model;

import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.HotZhuanquBean;
import com.jjshop.ui.presenter.HotZhuanquPresenter;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

public class HotZhuanquModel extends BaseModel{
    private HotZhuanquPresenter presenter;
    public HotZhuanquModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (HotZhuanquPresenter) basePresenter;
    }

    public void loadData(String shopId, String type, int page) {
        HttpHelper.bulid().getRequest(HttpUrl.build().getNewtjRenqibk(shopId, type, page),
                HotZhuanquBean.class, new OnRequestCallBack<HotZhuanquBean>() {
                    @Override
                    public void onSuccess(HotZhuanquBean data) {
                        presenter.onSuccess(data);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onFail(msg);
                    }
                });
    }
}
