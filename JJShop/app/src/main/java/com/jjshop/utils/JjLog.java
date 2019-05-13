package com.jjshop.utils;

import android.util.Log;

import com.jjshop.BuildConfig;

/**
 * 作者：张国庆
 * 时间：2018/7/17
 */

public class JjLog {
    private static final String TAG = "JJLog";
    public static void e(String msg){
        e(TAG, msg);
    }
    public static void e(String tag, String msg){
        if(BuildConfig.LOG_DEBUG){
            Log.e(tag, msg);
        }
    }
}
