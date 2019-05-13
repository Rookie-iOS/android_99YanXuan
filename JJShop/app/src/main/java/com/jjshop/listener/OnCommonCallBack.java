package com.jjshop.listener;

import com.jjshop.bean.BaseBean;
import com.jjshop.bean.CitysPickerBean;
import com.jjshop.bean.UploadImgBean;
import com.jjshop.bean.WxPayBean;

/**
 * 通用接口回调
 */
public interface OnCommonCallBack {
        void isHasCity(boolean b);
        void onCitysSuccess(CitysPickerBean bean);
        void onCitysError(String msg);
        void onWxPayDataSuccess(WxPayBean.WxPayData data);
        void onWxPayDataError(String msg);
        void onCanceOrderSuccess(BaseBean data);
        void onCanceOrderError(String msg);
        void onUploadImgSuccess(UploadImgBean data);
        void onUploadImgError(String msg);
        void onSuccess(Object o);
        void onError(String msg);
        void onProgress(float progress, long total);
}

