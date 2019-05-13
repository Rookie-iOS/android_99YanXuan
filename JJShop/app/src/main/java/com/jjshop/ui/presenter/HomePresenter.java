package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.ApkInfoBean;
import com.jjshop.bean.CitysPickerBean;
import com.jjshop.bean.HomeClassifyBean;
import com.jjshop.bean.JsonCityBean;
import com.jjshop.ui.activity.home.HomeActivity;
import com.jjshop.ui.model.HomeModel;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.dbutils.DBManager;
import com.jjshop.utils.httputil.HttpUrl;

/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class HomePresenter extends BasePresenter<HomeActivity> {
    HomeModel model;

    public HomePresenter(IView iView) {
        super(iView);
        model = (HomeModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new HomeModel(this);
    }

    public void loadAPKInfo(){
        HomeActivity view = getIView();
        if(null != view){
            model.loadApkInfo();
        }
    }

    public void loadTitleData(boolean isLoadNetData){
        HomeActivity view = getIView();
        if(null != view){
            if(!isLoadNetData){
                model.loadLocalTitleData(view);
            }
            model.loadTitleData();
        }
    }

    public void loadCitysData(){
        HomeActivity view = getIView();
        if(null != view){
            model.loadCitysData(PreUtils.getString(view, PreUtils.IS_UPDATE_CITY_DATA, "0"),
                    HttpUrl.getCookies(view), HttpUrl.getUserAgent(view));
        }
    }

    public void loadIsHasLocalCitysData(DBManager dbManager){
        HomeActivity view = getIView();
        if(null != view){
            model.loadIsHasLocalCitysData(dbManager);
        }
    }

    public void loadLocalCitysData(DBManager dbManager){
        HomeActivity view = getIView();
        if(null != view){
            model.getCitysData(dbManager);
        }
    }

    public void saveCitysData(DBManager dbManager, String cityJson){
        HomeActivity view = getIView();
        if(null != view){
            model.saveCitysData(dbManager, cityJson);
        }
    }

    public void onApkSuccess(ApkInfoBean bean){
        HomeActivity view = getIView();
        if(null != view){
            view.onApkSuccess(bean);
        }
    }

    public void onApkFail(String msg){
        HomeActivity view = getIView();
        if(null != view){
            view.onApkFail(msg);
        }
    }

    public void onTitleSuccess(HomeClassifyBean bean){
        HomeActivity view = getIView();
        if(null != view){
            view.onTitleSuccess(bean);
        }
    }
    public void onTitleFail(String str){
        HomeActivity view = getIView();
        if(null != view){
            view.onTitleFail(str);
        }
    }

    public void onCitysSuccess(JsonCityBean bean){
        HomeActivity view = getIView();
        if(null != view){
            view.onCitysSuccess(bean);
        }
    }

    public void onFail(String str){
        HomeActivity view = getIView();
        if(null != view){
            view.onFail(str);
        }
    }

    public void isHasLocalCitysData(boolean b){
        HomeActivity view = getIView();
        if(null != view){
            view.isHasLocalCitysData(b);
        }
    }

    public void onSaveSuccess(){
        HomeActivity view = getIView();
        if(null != view){
            view.onSaveSuccess();
        }
    }

    public void onGetLocalSuccess(CitysPickerBean bean){
        HomeActivity view = getIView();
        if(null != view){
            view.onGetLocalSuccess(bean);
        }
    }
}
