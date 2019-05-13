package com.jjshop.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.jjshop.bean.JJEvent;
import com.jjshop.ui.activity.WelcomeActivity;
import com.jjshop.ui.activity.home.HomeActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * 作者：张国庆
 * 时间：2018/9/15
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    private static CrashHandler INSTANCE = new CrashHandler();
    private Context mContext;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        BuglyUtil.build().postCatchedException(ex);
        // 重新启动APP
        Intent intent = new Intent(mContext, WelcomeActivity.class);
        if(null != mContext && !(mContext instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext.startActivity(intent);
        EventBus.getDefault().post(new JJEvent(JJEvent.FINISH_ACTIVITY));
        if(mContext instanceof HomeActivity){
            ((HomeActivity)mContext).finish();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
