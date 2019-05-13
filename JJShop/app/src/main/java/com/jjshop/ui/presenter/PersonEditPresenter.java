package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.CitysBean;
import com.jjshop.bean.CitysPickerBean;
import com.jjshop.bean.UploadImgBean;
import com.jjshop.ui.activity.person.PersonEditActivity;
import com.jjshop.ui.model.PersonEditModel;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.httputil.HttpUrl;

import java.io.File;

/**
 * 作者：张国庆
 * 时间：2018/7/20
 */

public class PersonEditPresenter extends BasePresenter<PersonEditActivity> {
    private PersonEditModel model;
    public PersonEditPresenter(IView iView) {
        super(iView);
        model = (PersonEditModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new PersonEditModel(this);
    }

    public void loadDataFile(File file){
        PersonEditActivity view = getIView();
        if(null != view){
            model.loadDataFile(PreUtils.getString(view, PreUtils.SHOP_ID),
                    HttpUrl.getCookies(view), HttpUrl.getUserAgent(view), file);
        }
    }

    public void loadDataHead(){
        PersonEditActivity view = getIView();
        if(null != view){
            model.loadDataHead(PreUtils.getString(view, PreUtils.SHOP_ID),
                    HttpUrl.getCookies(view), HttpUrl.getUserAgent(view), view.updateContent());
        }
    }

    public void loadDataNickName(){
        PersonEditActivity view = getIView();
        if(null != view){
            model.loadDataNickName(PreUtils.getString(view, PreUtils.SHOP_ID),
                    HttpUrl.getCookies(view), HttpUrl.getUserAgent(view), view.updateContent());
        }
    }

    public void loadDataSex(){
        PersonEditActivity view = getIView();
        if(null != view){
            model.loadDataSex(PreUtils.getString(view, PreUtils.SHOP_ID),
                    HttpUrl.getCookies(view), HttpUrl.getUserAgent(view), view.updateContent());
        }
    }

    public void loadDataBirthday(){
        PersonEditActivity view = getIView();
        if(null != view){
            model.loadDataBirthday(PreUtils.getString(view, PreUtils.SHOP_ID),
                    HttpUrl.getCookies(view), HttpUrl.getUserAgent(view), view.updateContent());
        }
    }

    public void loadDataName(){
        PersonEditActivity view = getIView();
        if(null != view){
            model.loadDataName(PreUtils.getString(view, PreUtils.SHOP_ID),
                    HttpUrl.getCookies(view), HttpUrl.getUserAgent(view), view.updateContent());
        }
    }

    public void loadCitysData(){
        PersonEditActivity view = getIView();
        if(null != view){
            model.loadDataCity(PreUtils.getString(view, PreUtils.SHOP_ID),
                    HttpUrl.getCookies(view), HttpUrl.getUserAgent(view), view.updateContent());
        }
    }

    public void loadLocalCitysData(){
        PersonEditActivity view = getIView();
        if(null != view){
            model.loadLocalCitysData(view.dbManager());
        }
    }

    public void onLocalCitySuccess(CitysPickerBean bean){
        PersonEditActivity view = getIView();
        if(null != view){
            view.onLocalCitySuccess(bean);
        }
    }

    public void onFileSuccess(UploadImgBean bean){
        PersonEditActivity view = getIView();
        if(null != view){
            view.onFileSuccess(bean);
        }
    }

    public void onNickNameSuccess(BaseBean bean){
        PersonEditActivity view = getIView();
        if(null != view){
            view.onNickNameSuccess(bean);
        }
    }

    public void onSexSuccess(BaseBean bean){
        PersonEditActivity view = getIView();
        if(null != view){
            view.onSexSuccess(bean);
        }
    }

    public void onBirthdaySuccess(BaseBean bean){
        PersonEditActivity view = getIView();
        if(null != view){
            view.onBirthdaySuccess(bean);
        }
    }

    public void onNameSuccess(BaseBean bean){
        PersonEditActivity view = getIView();
        if(null != view){
            view.onNameSuccess(bean);
        }
    }

    public void onCitySuccess(BaseBean bean){
        PersonEditActivity view = getIView();
        if(null != view){
            view.onCitySuccess(bean);
        }
    }

    public void onHeadSuccess(BaseBean bean){
        PersonEditActivity view = getIView();
        if(null != view){
            view.onHeadSuccess(bean);
        }
    }

    public void onFail(String str){
        PersonEditActivity view = getIView();
        if(null != view){
            view.onFail(str);
        }
    }
}
