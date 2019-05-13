package com.jjshop.listener;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 作者：张国庆
 * 时间：2018/7/19
 */

public interface OnJjShareCallBackIn {
    void onSuccess(SHARE_MEDIA share_media);
    void onError(SHARE_MEDIA share_media);
    void onStart(SHARE_MEDIA share_media);
    void onCancel(SHARE_MEDIA share_media);
}
