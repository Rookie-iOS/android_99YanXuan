package com.jjshop.widget;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.View;

import com.jjshop.ui.activity.home.DetailActivity;

import cn.jzvd.JzvdStd;

/**
 * 作者：张国庆
 * 时间：2018/11/15
 */

public class VideoJzvdView extends JzvdStd {

    private Context context;

    public VideoJzvdView(Context context) {
        super(context);
        this.context = context;
    }

    public VideoJzvdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case cn.jzvd.R.id.fullscreen:
                if (currentState == CURRENT_STATE_AUTO_COMPLETE) return;
                if(null == context || !(context instanceof Activity)){
                    return;
                }
                if(currentScreen == SCREEN_WINDOW_FULLSCREEN){
                    if(context instanceof DetailActivity){
                        ((DetailActivity)context).showHideNavigationBar(true);
                    }
                    // 设置成竖屏
                    ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    return;
                }
                if(context instanceof DetailActivity){
                    ((DetailActivity)context).showHideNavigationBar(false);
                }
                // 设置成横屏
                ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }
}
