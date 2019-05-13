package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.RefundBean;
import com.jjshop.bean.SubmitRefundBean;
import com.jjshop.bean.UploadImgBean;
import com.jjshop.listener.OnCommonCallBackIm;
import com.jjshop.ui.activity.person.RefundActivity;
import com.jjshop.ui.model.RefundModel;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.PreUtils;

/**
 * 作者：张国庆
 * 时间：2018/7/20
 */

public class RefundPresenter extends BasePresenter<RefundActivity> {
    private RefundModel model;
    public RefundPresenter(IView iView) {
        super(iView);
        model = (RefundModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new RefundModel(this);
    }

    // 加载退款详情
    public void loadTuikuanData(){
        RefundActivity view = getIView();
        if(null != view){
            model.loadTuiData(PreUtils.getString(view, PreUtils.SHOP_ID), view.order_no(), view.item_id(), true);
        }
    }

    // 加载退货详情
    public void loadTuihuoData(){
        RefundActivity view = getIView();
        if(null != view){
            model.loadTuiData(PreUtils.getString(view, PreUtils.SHOP_ID), view.order_no(), view.item_id(), false);
        }
    }

    // 数据获取成功
    public void onSuccess(RefundBean.RefundBeanData bean){
        RefundActivity view = getIView();
        if(null != view){
            view.onSuccess(bean);
        }
    }

    // 数据加载失败
    public void onError(String msg){
        RefundActivity view = getIView();
        if(null != view){
            view.onError(msg);
        }
    }

    // 加载退款请求
    public void loadSubmitRefund(){
        RefundActivity view = getIView();
        if(null != view){
            model.loadSubmitRefundData(PreUtils.getString(view, PreUtils.SHOP_ID), view.order_no(), view.item_id(),
                    view.reason(), view.refund_aomunt(), view.t_price(), view.note());
        }
    }

    // 加载退款请求
    public void loadSubmitReturn(){
        RefundActivity view = getIView();
        if(null != view){
            model.loadSubmitReturnData(PreUtils.getString(view, PreUtils.SHOP_ID), view.order_no(), view.item_id(),
                    view.reason(), view.refund_aomunt(), view.t_price(), view.note(), view.card_img_a(), view.card_img_b(),
                    view.card_img_c(), view.is_receive_goods());
        }
    }

    // 数据获取成功
    public void onSubmitSuccess(SubmitRefundBean.SubmitRefundData bean){
        RefundActivity view = getIView();
        if(null != view){
            view.onSubmitSuccess(bean);
        }
    }

    // 数据加载失败
    public void onSubmitError(String msg){
        RefundActivity view = getIView();
        if(null != view){
            view.onSubmitError(msg);
        }
    }

    // 加载上传图片获取链接
    public void loadUploadImg(){
        final RefundActivity view = getIView();
        if(null != view){
            CommonUtils.build().loadDataFile(PreUtils.getString(view, PreUtils.SHOP_ID), view.file(),
                    new OnCommonCallBackIm() {
                @Override
                public void onUploadImgSuccess(UploadImgBean data) {
                    view.onFileSuccess(data);
                }

                @Override
                public void onUploadImgError(String msg) {
                    view.onFileError(msg);
                }
            });
        }

    }

}
