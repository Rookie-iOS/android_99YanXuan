package com.jjshop.ui.model;

import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.CartDataBean;
import com.jjshop.bean.GetOrderIdBean;
import com.jjshop.bean.PayFinishBean;
import com.jjshop.bean.SubmitOrderBean;
import com.jjshop.bean.WxPayBean;
import com.jjshop.ui.presenter.PayPresenter;
import com.jjshop.ui.presenter.SubmitOrderPresenter;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/8/15
 */

public class PayModel extends BaseModel{

    private PayPresenter presenter;

    public PayModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (PayPresenter) basePresenter;
    }

    public void loadData(String resultUrl, String cookie, String userAgent){
        if(StringUtil.isEmpty(resultUrl)){
            presenter.onFail("地址是空的");
            return;
        }
        HttpHelper.bulid().getRequest(resultUrl, PayFinishBean.class,
                new OnRequestCallBack<PayFinishBean>() {
                    @Override
                    public void onSuccess(PayFinishBean data) {
                        PayFinishBean.PayFinishData bean = data.getData();
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
