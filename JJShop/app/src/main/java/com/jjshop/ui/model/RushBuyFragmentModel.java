package com.jjshop.ui.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.RushBuyBean;
import com.jjshop.ui.presenter.RushBuyFragmentPresenter;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.httputil.OnRequestCallBack;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import okhttp3.Call;

/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class RushBuyFragmentModel extends BaseModel {
    private RushBuyFragmentPresenter presenter;
    public RushBuyFragmentModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (RushBuyFragmentPresenter) basePresenter;
    }

    public void loadData(String shopId, String timeId, int page){
        HttpHelper.bulid().getRequest(HttpUrl.build().getRushbuy(shopId, timeId, page),
                RushBuyBean.class, new OnRequestCallBack<RushBuyBean>() {
                    @Override
                    public void onSuccess(RushBuyBean data) {
                        presenter.onSuccess(data);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onFail(msg);
                    }
                });
    }

}
