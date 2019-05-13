package com.jjshop.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.jjshop.app.JJShopApplication;

/**
 * 作者：张国庆
 * 时间：2018/7/12
 */

public class JjToast {
    private static volatile JjToast jjToast = null;
    private Toast toast = null;

    private JjToast(){}
    public static JjToast getInstance(){
        if(null == jjToast){
            synchronized (JjToast.class){
                if(null == jjToast){
                    jjToast = new JjToast();
                }
            }
        }
        return jjToast;
    }

    /** 显示Toast */
    public void toast(String msg) {
        toast(JJShopApplication.getContext(), msg, Toast.LENGTH_SHORT);
    }

    public void toast(String msg, int time) {
        toast(JJShopApplication.getContext(), msg, time);
    }

    public void toast(Context context, String msg, int time) {
        if(null == context){
            return;
        }
        // 不重复创建Toast
        if(toast == null){
            toast = Toast.makeText(context, msg, time);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }else{
            toast.setText(msg);
        }
        toast.show();
    }
}
