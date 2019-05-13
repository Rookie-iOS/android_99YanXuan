package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.SearchBean;
import com.jjshop.ui.activity.home.SearchActivity;
import com.jjshop.ui.model.SearchModel;

import java.util.List;

/**
 * 作者：张国庆
 * 时间：2018/7/21
 */

public class SearchPresenter extends BasePresenter<SearchActivity>{
    private SearchModel model;
    public SearchPresenter(IView iView) {
        super(iView);
        model = (SearchModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new SearchModel(this);
    }

    public void loadData(){
        SearchActivity view = getIView();
        if(null != view){
            model.loadData(view.content(), view.page());
        }
    }

    public void loadHistoryData(){
        SearchActivity view = getIView();
        if(null != view){
            model.loadHistoryData(view);
        }
    }

    public void saveSearchContent(){
        SearchActivity view = getIView();
        if(null != view){
            model.saveSearchContent(view, view.content(), view.myTagList());
        }
    }

    public void onSuccess(SearchBean bean){
        SearchActivity view = getIView();
        if(null != view){
            view.onSuccess(bean);
        }
    }

    public void onFail(String msg){
        SearchActivity view = getIView();
        if(null != view){
            view.onFail(msg);
        }
    }

    public void onHistorySuccess(List<String> list){
        SearchActivity view = getIView();
        if(null != view){
            view.onHistorySuccess(list);
        }
    }

}
