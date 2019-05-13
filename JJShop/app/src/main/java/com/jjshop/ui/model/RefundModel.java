package com.jjshop.ui.model;


import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.RefundBean;
import com.jjshop.bean.SubmitRefundBean;
import com.jjshop.ui.presenter.RefundPresenter;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class RefundModel extends BaseModel {
    private RefundPresenter presenter;
    public RefundModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (RefundPresenter) basePresenter;
    }


    /**
     * 加载退款页、退货页
     * @param isTuikuan  true:退款页   false:退货页
     */
    public void loadTuiData(String shopId, long order_no, int item_id, boolean isTuikuan){
        HttpHelper.bulid().getRequest(HttpUrl.build().getRefund(shopId, order_no, item_id, isTuikuan),
                RefundBean.class, new OnRequestCallBack<RefundBean>() {
                    @Override
                    public void onSuccess(RefundBean data) {
                        RefundBean.RefundBeanData bean = data.getData();
                        if(null == bean){
                            presenter.onError("没有数据");
                            return;
                        }
                        presenter.onSuccess(bean);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onError(msg);
                    }
                });
    }

    /**
     * 加载提交退款请求
     */
    public void loadSubmitRefundData(String shop, long order_no, int item_id, String reason, String refund_aomunt,
                                     String t_price, String note){
        if(StringUtil.isEmpty(t_price)){
            presenter.onSubmitError("请输入退款金额");
            return;
        }
//        refund_aomunt = "26.53";
////        t_price = "26.54";
        if(!StringUtil.isEmpty(refund_aomunt) && Double.parseDouble(t_price) > Double.parseDouble(refund_aomunt)){
            presenter.onSubmitError("输入的退款金额不能大于实际退款金额"+refund_aomunt + "=====" + t_price);
            return;
        }
        HttpHelper.bulid().postRequest(HttpUrl.URL_PERSON_SUBMIT_REFUND, SubmitRefundBean.class,
                HttpUrl.build().getSubmitTuikuan(shop, order_no, item_id, reason, refund_aomunt, t_price, note),
                new OnRequestCallBack<SubmitRefundBean>() {
                    @Override
                    public void onSuccess(SubmitRefundBean data) {
                        presenter.onSubmitSuccess(data.getData());
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onSubmitError(msg);
                    }
                });
    }

    /**
     * 加载提交退货请求
     */
    public void loadSubmitReturnData(String shop, long order_no, int item_id, String reason, String back_money,
                                     String t_price, String note, String card_img_a, String card_img_b,
                                     String card_img_c, int is_receive_goods){
        if(StringUtil.isEmpty(t_price)){
            presenter.onSubmitError("请输入退款金额");
            return;
        }
        if(!StringUtil.isEmpty(back_money) && Double.parseDouble(t_price) > Double.parseDouble(back_money)){
            presenter.onSubmitError("输入的退款金额不能大于实际退款金额");
            return;
        }
        if(StringUtil.isEmpty(card_img_a) && StringUtil.isEmpty(card_img_b) && StringUtil.isEmpty(card_img_c)){
            presenter.onSubmitError("至少选择一张图片");
            return;
        }
        HttpHelper.bulid().postRequest(HttpUrl.URL_PERSON_SUBMIT_RETUEN, SubmitRefundBean.class,
                HttpUrl.build().getSubmitTuihuo(shop, order_no, item_id, reason, back_money, t_price,
                        note, card_img_a, card_img_b, card_img_c, is_receive_goods),
                new OnRequestCallBack<SubmitRefundBean>() {
                    @Override
                    public void onSuccess(SubmitRefundBean data) {
                        presenter.onSubmitSuccess(data.getData());
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onSubmitError(msg);
                    }
                });
    }

}
