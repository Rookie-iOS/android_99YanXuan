package com.jjshop.ui.model;

import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.CitysPickerBean;
import com.jjshop.bean.UploadImgBean;
import com.jjshop.listener.OnCommonCallBackIm;
import com.jjshop.ui.presenter.PersonEditPresenter;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.dbutils.DBManager;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class PersonEditModel extends BaseModel {
    private PersonEditPresenter presenter;

    private final String KEY_NICKNAME = "name";
    private final String KEY_SEX = "sex";
    private final String KEY_BIRTHDAY = "birthday";
    private final String KEY_NAME = "real_name";
    private final String KEY_CITY = "cityids";
    private final String KEY_HEAD = "head";

    public PersonEditModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (PersonEditPresenter) basePresenter;
    }

    // 上传头像
    public void loadDataFile(String shopId, String cookie, String userAgent, final File file){
        CommonUtils.build().loadDataFile(shopId, file, new OnCommonCallBackIm() {
            @Override
            public void onUploadImgSuccess(UploadImgBean data) {
                presenter.onFileSuccess(data);
            }

            @Override
            public void onUploadImgError(String msg) {
                presenter.onFail(msg);
            }
        });
    }


    public void loadDataHead(String shopId, String cookie, String userAgent, String value){
        loadData(shopId, cookie, userAgent, KEY_HEAD, value);
    }
    public void loadDataNickName(String shopId, String cookie, String userAgent, String value){
        loadData(shopId, cookie, userAgent, KEY_NICKNAME, value);
    }
    public void loadDataSex(String shopId, String cookie, String userAgent, String value){
        loadData(shopId, cookie, userAgent, KEY_SEX, value);
    }
    public void loadDataBirthday(String shopId, String cookie, String userAgent, String value){
        loadData(shopId, cookie, userAgent, KEY_BIRTHDAY, value);
    }
    public void loadDataName(String shopId, String cookie, String userAgent, String value){
        loadData(shopId, cookie, userAgent, KEY_NAME, value);
    }
    public void loadDataCity(String shopId, String cookie, String userAgent, String value){
        loadData(shopId, cookie, userAgent, KEY_CITY, value);
    }
    private void loadData(String shopId, String cookie, String userAgent, final String key, final String value){
        final Map<String, String> map = new HashMap<>();
        map.put("shop", shopId);
        map.put("type", key);
        map.put(HttpUrl.COOKIE, cookie);
        map.put(HttpUrl.User_Agent, userAgent);
        if(!StringUtil.isEmpty(key) && !StringUtil.isEmpty(value)){
            map.put(key, value);
        }
        HttpHelper.bulid().postRequest(HttpUrl.URL_UPDATE_PERSON_INFO, BaseBean.class, map,
                new OnRequestCallBack<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean data) {
                        if(key.equals(KEY_NICKNAME)){
                            presenter.onNickNameSuccess(data);
                            return;
                        }
                        if(key.equals(KEY_SEX)){
                            presenter.onSexSuccess(data);
                            return;
                        }
                        if(key.equals(KEY_BIRTHDAY)){
                            presenter.onBirthdaySuccess(data);
                            return;
                        }
                        if(key.equals(KEY_NAME)){
                            presenter.onNameSuccess(data);
                            return;
                        }
                        if(key.equals(KEY_CITY)){
                            presenter.onCitySuccess(data);
                            return;
                        }
                        if(key.equals(KEY_HEAD)){
                            presenter.onHeadSuccess(data);
                            return;
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onFail(msg);
                    }
                });
    }

    public void loadLocalCitysData(DBManager dbManager){
        CommonUtils.build().loadLocalCitysData(dbManager, new OnCommonCallBackIm() {
            @Override
            public void onCitysSuccess(CitysPickerBean bean) {
                presenter.onLocalCitySuccess(bean);
            }

            @Override
            public void onCitysError(String msg) {
                presenter.onFail(msg);
            }
        });
    }

}
