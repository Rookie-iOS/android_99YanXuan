package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.MyCouponBean;
import com.jjshop.ui.activity.person.MyCouponActivity;
import com.jjshop.ui.model.MyCouponModel;
import com.jjshop.utils.PreUtils;

/**
 * 作者：张国庆
 * 时间：2018/8/15
 */

public class MyCouponPresenter extends BasePresenter<MyCouponActivity>{
    private MyCouponModel model;

    public MyCouponPresenter(IView iView) {
        super(iView);
        model = (MyCouponModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new MyCouponModel(this);
    }

    // 加载列表网络数据
    public void loadData(){
        MyCouponActivity view = getIView();
        if(null != view){
            model.loadData(PreUtils.getString(view, PreUtils.SHOP_ID));
        }
    }
    // 加载兑换网络数据
    public void loadDuihuanData(){
        MyCouponActivity view = getIView();
        if(null != view){
            model.loadDuihuanData(PreUtils.getString(view, PreUtils.SHOP_ID), view.discountCode());
        }
    }

    // 加载列表数据成功
    public void onSuccess(MyCouponBean bean){
        MyCouponActivity view = getIView();
        if(null != view){
            view.onSuccessList(bean);
        }
    }

    // 加载列表数据失败
    public void onFail(String msg){
        MyCouponActivity view = getIView();
        if(null != view){
            view.onError(msg);
        }
    }

    // 加载兑换数据成功
    public void onDuihuanSuccess(BaseBean bean){
        MyCouponActivity view = getIView();
        if(null != view){
            view.onDuihuanSuccess(bean);
        }
    }

    // 加载兑换数据失败
    public void onDuihuanFail(String msg){
        MyCouponActivity view = getIView();
        if(null != view){
            view.onDuihuanError(msg);
        }
    }

}
