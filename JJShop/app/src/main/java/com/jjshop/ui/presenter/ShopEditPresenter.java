package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.UploadImgBean;
import com.jjshop.ui.activity.shop.ShopEditActivity;
import com.jjshop.ui.model.ShopEditModel;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.httputil.HttpUrl;

import java.io.File;

/**
 * 作者：张国庆
 * 时间：2018/7/20
 */

public class ShopEditPresenter extends BasePresenter<ShopEditActivity> {
    private ShopEditModel model;
    public ShopEditPresenter(IView iView) {
        super(iView);
        model = (ShopEditModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new ShopEditModel(this);
    }

    public void loadDataFile(File file){
        ShopEditActivity view = getIView();
        if(null != view){
            model.loadDataFile(PreUtils.getString(view, PreUtils.SHOP_ID, ""),
                    HttpUrl.getCookies(view), HttpUrl.getUserAgent(view), file);
        }
    }
    public void loadDataHead(){
        ShopEditActivity view = getIView();
        if(null != view){
            model.loadDataHead(PreUtils.getString(view, PreUtils.SHOP_ID, ""),
                    HttpUrl.getCookies(view), HttpUrl.getUserAgent(view), view.updateContent());
        }
    }
    public void loadDataName(){
        ShopEditActivity view = getIView();
        if(null != view){
            model.loadDataName(PreUtils.getString(view, PreUtils.SHOP_ID, ""),
                    HttpUrl.getCookies(view), HttpUrl.getUserAgent(view), view.updateContent());
        }
    }
    public void loadDataInfo(){
        ShopEditActivity view = getIView();
        if(null != view){
            model.loadDataInfo(PreUtils.getString(view, PreUtils.SHOP_ID, ""),
                    HttpUrl.getCookies(view), HttpUrl.getUserAgent(view), view.updateContent());
        }
    }

    public void onFileSuccess(UploadImgBean bean){
        ShopEditActivity view = getIView();
        if(null != view){
            view.onFileSuccess(bean);
        }
    }
    public void onHeadSuccess(BaseBean bean){
        ShopEditActivity view = getIView();
        if(null != view){
            view.onHeadSuccess(bean);
        }
    }
    public void onNameSuccess(BaseBean bean){
        ShopEditActivity view = getIView();
        if(null != view){
            view.onNameSuccess(bean);
        }
    }
    public void onInfoSuccess(BaseBean bean){
        ShopEditActivity view = getIView();
        if(null != view){
            view.onInfoSuccess(bean);
        }
    }
    public void onFail(String str){
        ShopEditActivity view = getIView();
        if(null != view){
            view.onFail(str);
        }
    }
}
