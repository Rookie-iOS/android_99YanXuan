package com.jjshop.utils;

import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * 作者：张国庆
 * 时间：2018/9/13
 */

public class BuglyUtil {
    private static volatile BuglyUtil buglyUtil;

    private BuglyUtil(){}

    public static BuglyUtil build(){
        if(null == buglyUtil){
            synchronized (BuglyUtil.class){
                if(null == buglyUtil){
                    buglyUtil = new BuglyUtil();
                }
            }
        }
        return buglyUtil;
    }


    /**
     * 设置用户的唯一标识
     * @param userId
     */
    public void setUserId(String userId){
        CrashReport.setUserId(userId);
    }

    /**
     * 主动上报异常
     * @param thr
     */
    public void postCatchedException(Throwable thr){
        CrashReport.postCatchedException(thr);
    }
}
