package com.jjshop.ui.model;

import android.content.Context;

import com.jjshop.app.JJShopApplication;
import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.ApkInfoBean;
import com.jjshop.bean.CitysPickerBean;
import com.jjshop.bean.HomeClassifyBean;
import com.jjshop.bean.JsonCityBean;
import com.jjshop.listener.OnCommonCallBackIm;
import com.jjshop.ui.presenter.HomePresenter;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.dbutils.DBManager;
import com.jjshop.utils.dbutils.LitepalUtil;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.OnRequestCallBack;


/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class HomeModel extends BaseModel {
    private HomePresenter presenter;
    public HomeModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (HomePresenter) basePresenter;
    }

    public void loadApkInfo(){
        HttpHelper.bulid().getRequest(HttpUrl.build().getApkInfo(JJShopApplication.sVersion),
                ApkInfoBean.class, new OnRequestCallBack<ApkInfoBean>() {
                    @Override
                    public void onSuccess(ApkInfoBean data) {
                        presenter.onApkSuccess(data);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onApkFail(msg);
                    }
                });
    }

    // 请求首页标题数据
    public void loadTitleData() {
        HttpHelper.bulid().getRequest(HttpUrl.JJMAIL_INDEX, HomeClassifyBean.class,
                new OnRequestCallBack<HomeClassifyBean>() {
            @Override
            public void onSuccess(HomeClassifyBean data) {
                if(null == data){
                    presenter.onTitleFail("没有数据");
                    return;
                }
                presenter.onTitleSuccess(data);
                // 保存数据到数据库
                CommonUtils.build().saveNetData(LitepalUtil.HOME_TITLE_KEY, data.getJson());
            }

            @Override
            public void onError(String msg) {
                presenter.onTitleFail(msg);
            }
        });
    }

    // 获取本地首页标题数据
    public void loadLocalTitleData(final Context context){
        if(null == context){
            return;
        }
        CommonUtils.build().loadLocalData(context, LitepalUtil.HOME_TITLE_KEY, new OnCommonCallBackIm() {
            @Override
            public void onSuccess(Object o) {
                if(null != o && o instanceof HomeClassifyBean){
                    presenter.onTitleSuccess((HomeClassifyBean) o);
                }
            }
        });
    }

    // 请求城市列表数据
    public void loadCitysData(String isnew, String cookie, String userAgent) {
        HttpHelper.bulid().getRequest(HttpUrl.build().getCitys(isnew), JsonCityBean.class,
                new OnRequestCallBack<JsonCityBean>() {
            @Override
            public void onSuccess(JsonCityBean data) {
                presenter.onCitysSuccess(data);
            }

            @Override
            public void onError(String msg) {
                presenter.onFail(msg);
            }
        });
    }

    // 保存城市数据到本地
    public void saveCitysData(DBManager dbManager, final String cityJson){
        CommonUtils.build().saveLocalCitysData(dbManager, cityJson, new OnCommonCallBackIm() {
            @Override
            public void isHasCity(boolean b) {
                if(b){// 保存成功
                    presenter.onSaveSuccess();
                    return;
                }

            }
        });
    }

    // 获取本地城市数据
    public void getCitysData(DBManager dbManager){
        CommonUtils.build().loadLocalCitysData(dbManager, new OnCommonCallBackIm() {
            @Override
            public void onCitysSuccess(CitysPickerBean bean) {
                presenter.onGetLocalSuccess(bean);
            }

            @Override
            public void onCitysError(String msg) {
//                presenter.onFail(msg);
            }
        });
    }

    // 本地是否有城市数据
    public void loadIsHasLocalCitysData(DBManager dbManager){
        CommonUtils.build().isHasLocalCitysData(dbManager, new OnCommonCallBackIm() {
            @Override
            public void isHasCity(boolean b) {
                presenter.isHasLocalCitysData(b);
            }
        });
    }


}
