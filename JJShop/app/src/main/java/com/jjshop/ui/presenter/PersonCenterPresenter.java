package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.PersonCenterBean;
import com.jjshop.ui.fragment.MyCenterFragment;
import com.jjshop.ui.model.PersonCenterModel;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.httputil.HttpUrl;

/**
 * 作者：张国庆
 * 时间：2018/7/20
 */

public class PersonCenterPresenter extends BasePresenter<MyCenterFragment> {
    private PersonCenterModel model;
    public PersonCenterPresenter(IView iView) {
        super(iView);
        model = (PersonCenterModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new PersonCenterModel(this);
    }

    public void loadData(){
        MyCenterFragment view = getIView();
        if(null != view && null != view.getActivity()){
            model.loadData(PreUtils.getString(view.getActivity(), PreUtils.SHOP_ID, ""),
                    HttpUrl.getCookies(view.getActivity()), HttpUrl.getUserAgent(view.getActivity()));
        }
    }

    public void onSuccess(PersonCenterBean.PersonCenterData bean){
        MyCenterFragment view = getIView();
        if(null != view){
            view.onSuccess(bean);
        }
    }
    public void onFail(String str){
        MyCenterFragment view = getIView();
        if(null != view){
            view.onFail(str);
        }
    }
}
