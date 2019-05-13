package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.RefundDetailBean;
import com.jjshop.ui.activity.person.RefundDetailActivity;
import com.jjshop.ui.model.RefundDetailModel;
import com.jjshop.utils.PreUtils;

/**
 * 作者：张国庆
 * 时间：2018/7/20
 */

public class RefundDetailPresenter extends BasePresenter<RefundDetailActivity> {
    private RefundDetailModel model;
    public RefundDetailPresenter(IView iView) {
        super(iView);
        model = (RefundDetailModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new RefundDetailModel(this);
    }

    // 加载退款详情
    public void loadTuikuanData(){
        RefundDetailActivity view = getIView();
        if(null != view){
            model.loadTuiData(PreUtils.getString(view, PreUtils.SHOP_ID), view.service_id(), true);
        }
    }

    // 加载退货详情
    public void loadTuihuoData(){
        RefundDetailActivity view = getIView();
        if(null != view){
            model.loadTuiData(PreUtils.getString(view, PreUtils.SHOP_ID), view.service_id(), false);
        }
    }

    // 详情数据获取成功
    public void onSuccessDetails(RefundDetailBean.RefundDetailData bean){
        RefundDetailActivity view = getIView();
        if(null != view){
            view.onSuccessDetails(bean);
        }
    }

    // 详情数据获取失败
    public void onErrorDetails(String msg){
        RefundDetailActivity view = getIView();
        if(null != view){
            view.onErrorDetails(msg);
        }
    }

    // 加载提交物流信息
    public void loadSubmitData(){
        RefundDetailActivity view = getIView();
        if(null != view){
            model.loadSubmitData(PreUtils.getString(view, PreUtils.SHOP_ID), view.service_id(),
                    view.company_code(), view.express_no());
        }
    }

    // 提交物流成功
    public void onSubmitSuccess(BaseBean bean){
        RefundDetailActivity view = getIView();
        if(null != view){
            view.onSubmitSuccess(bean);
        }
    }

    // 提交物流失败
    public void onErrorSubmit(String msg){
        RefundDetailActivity view = getIView();
        if(null != view){
            view.onErrorSubmit(msg);
        }
    }

}
