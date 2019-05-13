package com.jjshop.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 作者：张国庆
 * 时间：2018/7/26
 */

public class MyScrollView extends ScrollView {
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if(null != onMyScrollChanged){
            onMyScrollChanged.onScrollChanged(x, y, oldx, oldy);
        }
    }

    private OnMyScrollChanged onMyScrollChanged;

    public interface OnMyScrollChanged{
        void onScrollChanged(int x, int y, int oldx, int oldy);
    }

    public void setOnMyScrollChanged(OnMyScrollChanged onMyScrollChanged){
        this.onMyScrollChanged = onMyScrollChanged;
    }
}
