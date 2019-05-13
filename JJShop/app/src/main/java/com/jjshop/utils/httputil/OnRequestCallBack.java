package com.jjshop.utils.httputil;

/**
 * 作者：张国庆
 * 时间：2018/7/27
 */

public interface OnRequestCallBack<T> {
    void onSuccess(T data);
    void onError(String msg);
}
