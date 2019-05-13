package com.jjshop.bean;

import com.jjshop.utils.StringUtil;

/**
 * 作者：张国庆
 * 时间：2018/8/8
 */

public class JsonCityBean extends BaseBean{

    private String isnew;

    public String getIsnew() {
        return StringUtil.isEmpty(isnew) ? "0" : isnew;
    }

    public void setIsnew(String isnew) {
        this.isnew = isnew;
    }

}
