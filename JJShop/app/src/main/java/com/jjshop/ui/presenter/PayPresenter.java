package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.PayFinishBean;
import com.jjshop.ui.activity.home.PayActivity;
import com.jjshop.ui.model.PayModel;
import com.jjshop.utils.httputil.HttpUrl;

/**
 * 作者：张国庆
 * 时间：2018/8/15
 */

public class PayPresenter extends BasePresenter<PayActivity>{
    private PayModel model;

    public PayPresenter(IView iView) {
        super(iView);
        model = (PayModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new PayModel(this);
    }

    // 请求支付状态
    public void loadData(){
        PayActivity view = getIView();
        if(null != view){
            model.loadData(view.resultUrl(), HttpUrl.getCookies(view), HttpUrl.getUserAgent(view));
        }
    }

    // 获取支付状态成功
    public void onSuccess(PayFinishBean.PayFinishData bean){
        PayActivity view = getIView();
        if(null != view){
            view.onSuccess(bean);
        }
    }

    // 获取支付状态失败
    public void onFail(String msg){
        PayActivity view = getIView();
        if(null != view){
            view.onFail(msg);
        }
    }

}
