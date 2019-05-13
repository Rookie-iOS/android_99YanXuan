package com.jjshop.ui.model;

import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.LoginBean;
import com.jjshop.ui.presenter.LoginPresenter;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.Tools;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class LoginModel extends BaseModel {
    private LoginPresenter presenter;
    public LoginModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (LoginPresenter) basePresenter;
    }

    /**
     * 获取验证码
     * @param mobile    手机号
     */
    public void loadCode(String mobile){
        if(StringUtil.isEmpty(mobile) || mobile.length() < 11 || !Tools.isMobileNum(mobile)){
            presenter.onFail("手机号不正确");
            return;
        }
        HttpHelper.bulid().getRequest(HttpUrl.build().getLoginCode(mobile), BaseBean.class,
                new OnRequestCallBack<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean data) {
                        presenter.onCodeSuccess();
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onFail(msg);
                    }
                });
    }

    /**
     * 登录
     * @param mobile    手机号
     * @param code      验证码
     */
    public void loadLogin(String mobile, String code){
        if(StringUtil.isEmpty(mobile) || mobile.length() < 11 || !Tools.isMobileNum(mobile)){
            presenter.onFail("手机号不正确");
            return;
        }
        if(StringUtil.isEmpty(code) || code.length() < 5){
            presenter.onFail("验证不正确");
            return;
        }
        HttpHelper.bulid().postCookieRequest(HttpUrl.URL_LOGIN, HttpUrl.build().getLogin(mobile, code), LoginBean.class,
                new OnRequestCallBack<LoginBean>() {
                    @Override
                    public void onSuccess(LoginBean data) {
                        presenter.onLoginSuccess(data);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onFail(msg);
                    }
                });
    }

}
