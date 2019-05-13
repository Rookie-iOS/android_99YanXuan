package com.jjshop.ui.model;

import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.UploadImgBean;
import com.jjshop.ui.presenter.ShopEditPresenter;
import com.jjshop.utils.StringUtil;
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

public class ShopEditModel extends BaseModel {
    private ShopEditPresenter presenter;
    private final String KEY_NAME = "name";
    private final String KEY_INFO = "info";
    private final String KEY_HEAD = "head";
    public ShopEditModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (ShopEditPresenter) basePresenter;
    }

    public void loadDataName(String shopId, String cookie, String userAgent, String name){
        loadData(shopId, cookie, userAgent, KEY_NAME, name);
    }
    public void loadDataInfo(String shopId, String cookie, String userAgent, String info){
        loadData(shopId, cookie, userAgent, KEY_INFO, info);
    }
    public void loadDataHead(String shopId, String cookie, String userAgent, String head){
        loadData(shopId, cookie, userAgent, KEY_HEAD, head);
    }
    private void loadData(String shopId, String cookie, String userAgent, final String key, final String value){
        final Map<String, String> map = new HashMap<>();
        map.put(HttpUrl.COOKIE, cookie);
        map.put(HttpUrl.User_Agent, userAgent);
        if(StringUtil.isEmpty(key) && StringUtil.isEmpty(value)){
            return;
        }
        map.put(key, value);
        HttpHelper.bulid().postRequest(HttpUrl.build().getUploadHead(shopId), BaseBean.class, map,
                new OnRequestCallBack<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean data) {
                        if(key.equals(KEY_NAME)){
                            presenter.onNameSuccess(data);
                            return;
                        }
                        if(key.equals(KEY_INFO)){
                            presenter.onInfoSuccess(data);
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

    // 上传头像
    public void loadDataFile(String shopId, String cookie, String userAgent, final File file){
        final Map<String, String> map = new HashMap<>();
        map.put("shop", shopId);
        map.put(HttpUrl.COOKIE, cookie);
        map.put(HttpUrl.User_Agent, userAgent);
        HttpHelper.bulid().postRequest(HttpUrl.URL_UPLOAD_HEAD, UploadImgBean.class, map, file, "headimg_upload",
                new OnRequestCallBack<UploadImgBean>() {
                    @Override
                    public void onSuccess(UploadImgBean data) {
                        presenter.onFileSuccess(data);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onFail(msg);
                    }
                });
    }

}
