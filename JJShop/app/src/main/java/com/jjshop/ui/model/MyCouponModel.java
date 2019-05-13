package com.jjshop.ui.model;

import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.MyCouponBean;
import com.jjshop.ui.presenter.MyCouponPresenter;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

/**
 * 作者：张国庆
 * 时间：2018/8/15
 */

public class MyCouponModel extends BaseModel{

    private MyCouponPresenter presenter;

    public MyCouponModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (MyCouponPresenter) basePresenter;
    }

    public void loadData(String shop){
        HttpHelper.bulid().getRequest(HttpUrl.build().getMyCoupon(shop), MyCouponBean.class,
                new OnRequestCallBack<MyCouponBean>() {
                    @Override
                    public void onSuccess(MyCouponBean data) {
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

    public void loadDuihuanData(String shop, String code){
        if(StringUtil.isEmpty(code)){
            presenter.onDuihuanFail("优惠码不能为空");
            return;
        }
        HttpHelper.bulid().getRequest(HttpUrl.build().getDuihuanCoupon(shop, code), BaseBean.class,
                new OnRequestCallBack<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean data) {
                        if(null == data){
                            presenter.onDuihuanFail("券号不存在");
                            return;
                        }
                        presenter.onDuihuanSuccess(data);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onDuihuanFail(msg);
                    }
                });
    }

}
