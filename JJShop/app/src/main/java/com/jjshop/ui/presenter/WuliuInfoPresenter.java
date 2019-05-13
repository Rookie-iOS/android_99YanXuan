package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.WuliuInfoBean;
import com.jjshop.ui.activity.person.WuliuInfoActivity;
import com.jjshop.ui.model.WuliuInfoModel;
import com.jjshop.utils.PreUtils;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/8/15
 */

public class WuliuInfoPresenter extends BasePresenter<WuliuInfoActivity>{
    private WuliuInfoModel model;

    public WuliuInfoPresenter(IView iView) {
        super(iView);
        model = (WuliuInfoModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new WuliuInfoModel(this);
    }

    // 请求订单信息
    public void loadData(){
        WuliuInfoActivity view = getIView();
        if(null != view){
            model.loadData(PreUtils.getString(view, PreUtils.SHOP_ID), view.orderno());
        }
    }

    // 获取物流信息成功
    public void onSuccess(WuliuInfoBean.WuliuInfoBeanData bean){
        WuliuInfoActivity view = getIView();
        if(null != view){
            view.onSuccess(bean);
        }
    }

    // 获取物流信息失败
    public void onFail(String msg){
        WuliuInfoActivity view = getIView();
        if(null != view){
            view.onFail(msg);
        }
    }

    // 拆分物流信息数据
    public void splitData(ArrayList<WuliuInfoBean.WuliuInfoBeanExpressinfo> splitList){
        WuliuInfoActivity view = getIView();
        if(null != view){
            model.splitData(splitList, view.mListData());
        }
    }

}
