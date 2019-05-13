package com.jjshop.listener;

import com.jjshop.bean.BaseBean;
import com.jjshop.bean.CitysPickerBean;
import com.jjshop.bean.UploadImgBean;
import com.jjshop.bean.WxPayBean;

/**
 * 通用抽象类回调
 */
public abstract class OnCommonCallBackIm implements OnCommonCallBack{
        @Override
        public void isHasCity(boolean b) {

        }

        @Override
        public void onCitysSuccess(CitysPickerBean bean) {

        }

        @Override
        public void onCitysError(String msg) {

        }

        @Override
        public void onWxPayDataSuccess(WxPayBean.WxPayData data) {

        }

        @Override
        public void onWxPayDataError(String msg) {

        }

        @Override
        public void onCanceOrderSuccess(BaseBean data) {

        }

        @Override
        public void onCanceOrderError(String msg) {

        }

        @Override
        public void onUploadImgSuccess(UploadImgBean data) {

        }

        @Override
        public void onUploadImgError(String msg) {

        }

        @Override
        public void onSuccess(Object o) {

        }

        @Override
        public void onError(String msg) {

        }

        @Override
        public void onProgress(float progress, long total) {

        }
}

