package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.JJHomeBean;
import com.jjshop.ui.fragment.HomeItemFragment;
import com.jjshop.ui.model.HomeItemModel;
import com.jjshop.utils.httputil.HttpUrl;

/**
 * 作者：张国庆
 * 时间：2018/7/20
 */

public class HomeItemPresenter extends BasePresenter<HomeItemFragment> {
    private HomeItemModel model;
    public HomeItemPresenter(IView iView) {
        super(iView);
        model = (HomeItemModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new HomeItemModel(this);
    }

    public void loadData(boolean isLoadNetData){
        HomeItemFragment view = getIView();
        if(null != view){
            if(!isLoadNetData){
                model.loadLocalData(view.getActivity(), view.key());
            }
            model.loadData(view.cid(), view.vid(), view.page(), view.key(), view.url());
        }
    }

    public void onSuccess(JJHomeBean bean){
        HomeItemFragment view = getIView();
        if(null != view){
            view.onSuccess(bean);
        }
    }
    public void onFail(String str){
        HomeItemFragment view = getIView();
        if(null != view){
            view.onFail(str);
        }
    }
}
