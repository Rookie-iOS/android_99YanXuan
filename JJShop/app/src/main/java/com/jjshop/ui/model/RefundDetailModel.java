package com.jjshop.ui.model;


import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.RefundDetailBean;
import com.jjshop.ui.presenter.RefundDetailPresenter;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class RefundDetailModel extends BaseModel {
    private RefundDetailPresenter presenter;
    public RefundDetailModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (RefundDetailPresenter) basePresenter;
    }

    /**
     * 加载详情
     * @param isTuikuan  true:加载退款详情   false:加载退货详情
     */
    public void loadTuiData(String shopId, final int service_id, boolean isTuikuan){
        HttpHelper.bulid().getRequest(HttpUrl.build().getRefundDetail(shopId, service_id, isTuikuan),
                RefundDetailBean.class, new OnRequestCallBack<RefundDetailBean>() {
                    @Override
                    public void onSuccess(RefundDetailBean data) {
                        RefundDetailBean.RefundDetailData bean = data.getData();
                        if(null == bean){
                            presenter.onErrorDetails("没有数据");
                            return;
                        }
                        presenter.onSuccessDetails(bean);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onErrorDetails(msg);
                    }
                });
    }

    /**
     * 加载提交物流信息
     */
    public void loadSubmitData(String shop, int service_id, String company_code, String express_no){
        HttpHelper.bulid().postRequest(HttpUrl.URL_PERSON_SUBMIT_WULIU, BaseBean.class,
                HttpUrl.build().getSubmitWuliu(shop, service_id, company_code, express_no),
                new OnRequestCallBack<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean data) {
                        presenter.onSubmitSuccess(data);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onErrorSubmit(msg);
                    }
                });
    }

}
