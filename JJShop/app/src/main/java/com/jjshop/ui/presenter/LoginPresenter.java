package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.LoginBean;
import com.jjshop.ui.LoginActivity;
import com.jjshop.ui.model.LoginModel;

/**
 * 作者：张国庆
 * 时间：2018/7/21
 */

public class LoginPresenter extends BasePresenter<LoginActivity>{
    private LoginModel model;
    public LoginPresenter(IView iView) {
        super(iView);
        model = (LoginModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new LoginModel(this);
    }

    public void loadCode(){
        LoginActivity view = getIView();
        if(null != view){
            model.loadCode(view.mobile());
        }
    }

    public void loadLogin(){
        LoginActivity view = getIView();
        if(null != view){
            model.loadLogin(view.mobile(), view.code());
        }
    }

    public void onCodeSuccess(){
        LoginActivity view = getIView();
        if(null != view){
            view.onCodeSuccess();
        }
    }

    public void onLoginSuccess(final LoginBean bean){
        final LoginActivity view = getIView();
        if(null != view){
            view.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.onLoginSuccess(bean);
                }
            });

        }
    }

    public void onFail(final String msg){
        final LoginActivity view = getIView();
        if(null != view){
            view.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.onFail(msg);
                }
            });
        }
    }
}
