package com.jjshop.ui.model;

import android.content.Context;

import com.google.gson.Gson;
import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.JJHomeBean;
import com.jjshop.listener.OnCommonCallBackIm;
import com.jjshop.ui.presenter.HomeItemPresenter;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class HomeItemModel extends BaseModel {
    private HomeItemPresenter presenter;
    public HomeItemModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (HomeItemPresenter) basePresenter;
    }

    public void loadData(int cid, int vid, int page, String key, String url){
        HttpHelper.bulid().getRequest(HttpUrl.build().getHome(cid, vid, page),
                JJHomeBean.class, new OnRequestCallBack<JJHomeBean>() {
                    @Override
                    public void onSuccess(JJHomeBean data) {
                        if(null == data){
                            presenter.onFail("没有数据");
                            return;
                        }
                        presenter.onSuccess(data);
                        // 保存数据到数据库
                        if(StringUtil.isEmpty(url) && page == 1){
                            CommonUtils.build().saveNetData(key, data.getJson());
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onFail(msg);
                    }
                });
    }

    public void loadLocalData(Context context, String key){
        CommonUtils.build().loadLocalData(context, key, new OnCommonCallBackIm() {
            @Override
            public void onSuccess(Object o) {
                if(null != o && o instanceof String){
                    String json = (String) o;
                    if(StringUtil.isEmpty(json)){
                        return;
                    }
                    JJHomeBean bean = new Gson().fromJson(json, JJHomeBean.class);
                    if(null != bean){
                        presenter.onSuccess(bean);
                    }
                }
            }
        });
    }

}
