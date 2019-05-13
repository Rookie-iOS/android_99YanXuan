package com.jjshop.app;

import android.content.Context;

import com.jjshop.utils.JjLog;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.UrlInvokeUtil;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.Map;


/**
 * 作者：张国庆
 * 时间：2018/9/10
 */

public class MIPushReceiver extends PushMessageReceiver {
    private String mMessage;

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        JjLog.e("传过来的数据onReceivePassThroughMessage = " + mMessage);
    }
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        JjLog.e("点击传过来的数据 = " + mMessage);
        if(StringUtil.isEmpty(mMessage)){
            return;
        }
        JjLog.e("点击通知1 = " + mMessage);
        Map<String, String> map = UrlInvokeUtil.build().pushData(mMessage);
        if(null == map || map.size() <= 0){
            JjLog.e("通知数据不符合要求 = " + mMessage);
            return;
        }
        // 执行跳转动作
        UrlInvokeUtil.build().pushInvoke(context, map);
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        JjLog.e("传过来的数据onNotificationMessageArrived = " + mMessage);
    }
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
    }
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
    }

}
