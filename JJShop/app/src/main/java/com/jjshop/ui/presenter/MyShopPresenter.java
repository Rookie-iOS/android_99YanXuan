package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.MyShopBean;
import com.jjshop.ui.fragment.MyShopFragment;
import com.jjshop.ui.model.MyShopModel;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.httputil.HttpUrl;

/**
 * 作者：张国庆
 * 时间：2018/7/20
 */

public class MyShopPresenter extends BasePresenter<MyShopFragment> {
    private MyShopModel model;
    public MyShopPresenter(IView iView) {
        super(iView);
        model = (MyShopModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new MyShopModel(this);
    }

    public void loadData(){
        MyShopFragment view = getIView();
        if(null != view && null != view.getActivity()){
            model.loadData(PreUtils.getString(view.getActivity(), PreUtils.SHOP_ID, ""),
                    HttpUrl.getCookies(view.getActivity()), HttpUrl.getUserAgent(view.getActivity()));
        }
    }

    public void onSuccess(MyShopBean.MyShopDataBean bean){
        MyShopFragment view = getIView();
        if(null != view){
            view.onSuccess(bean);
        }
    }
    public void onFail(String str){
        MyShopFragment view = getIView();
        if(null != view){
            view.onFail(str);
        }
    }
}
