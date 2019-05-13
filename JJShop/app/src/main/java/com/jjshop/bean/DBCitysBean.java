package com.jjshop.bean;

import com.jjshop.utils.StringUtil;

/**
 * 作者：张国庆
 * 时间：2018/8/9
 */

public class DBCitysBean {
    private int _id;
    private String citysjson;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getCitysjson() {
        return citysjson;
    }

    public void setCitysjson(String citysjson) {
        this.citysjson = citysjson;
    }
}
