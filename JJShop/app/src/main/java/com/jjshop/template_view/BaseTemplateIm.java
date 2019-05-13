package com.jjshop.template_view;

import android.os.Bundle;

/**
 * 作者：张国庆
 * 时间：2018/8/3
 */

public interface BaseTemplateIm {
    /** 刷新数据 */
    public void getDate(Object data, Bundle bundle);

    /** 刷新数据 */
    public void getDate(Object data, Object data2, Bundle bundle);

    /** 刷新数据 */
    public void getDate(Object data, int position);
}
