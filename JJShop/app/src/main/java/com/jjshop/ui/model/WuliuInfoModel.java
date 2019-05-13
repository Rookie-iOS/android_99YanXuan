package com.jjshop.ui.model;

import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.WuliuInfoBean;
import com.jjshop.bean.WuliuInfoBeanExpress;
import com.jjshop.bean.WuliuTopBean;
import com.jjshop.template_view.TemplateUtil;
import com.jjshop.ui.presenter.WuliuInfoPresenter;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/8/15
 */

public class WuliuInfoModel extends BaseModel{

    private WuliuInfoPresenter presenter;

    public WuliuInfoModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (WuliuInfoPresenter) basePresenter;
    }

    public void loadData(String shop, long orderno){
        HttpHelper.bulid().postRequest(HttpUrl.URL_PERSON_WULIU_INFO, WuliuInfoBean.class,
                HttpUrl.build().getWuliuInfo(shop, orderno), new OnRequestCallBack<WuliuInfoBean>() {
                    @Override
                    public void onSuccess(WuliuInfoBean data) {
                        WuliuInfoBean.WuliuInfoBeanData bean = data.getData();
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

    /**
     * 拆分物流信息数据
     * @param splitList
     * @param list
     */
    public void splitData(ArrayList<WuliuInfoBean.WuliuInfoBeanExpressinfo> splitList, ArrayList<Object> list){
        if(null == splitList || splitList.isEmpty() || null == list){
            return;
        }
        for (WuliuInfoBean.WuliuInfoBeanExpressinfo bean : splitList){
            if(null != bean){
                // 顶部的快递信息
                WuliuTopBean topBean = new WuliuTopBean();
                topBean.setKdName(bean.getExpress_name());
                topBean.setKdNo(bean.getExpress_no());
                topBean.setTemplate(TemplateUtil.TEMPLATE_1012);
                list.add(topBean);
                // 物流信息
                ArrayList<WuliuInfoBeanExpress> infoList = bean.getExpress();
                if(null != infoList && infoList.size() > 0){
                    for(int i = 0; i < infoList.size(); i++){
                        WuliuInfoBeanExpress infoBean = infoList.get(i);
                        if(null != infoBean){
                            infoBean.setTemplate(TemplateUtil.TEMPLATE_1013);
                            infoBean.setPosition(i);
                        }
                    }
                    list.addAll(infoList);
                }
            }
        }
    }

}
