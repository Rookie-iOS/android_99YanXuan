package com.jjshop.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.io.Serializable;

/**
 * 作者：张国庆
 * 时间：2018/9/26
 */

public class ExpressBean  implements IPickerViewData, Serializable {
    private int id;
    private String name;
    private String xiaoyatong_code;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXiaoyatong_code() {
        return xiaoyatong_code == null ? "" : xiaoyatong_code;
    }

    public void setXiaoyatong_code(String xiaoyatong_code) {
        this.xiaoyatong_code = xiaoyatong_code;
    }

    @Override
    public String getPickerViewText() {
        return getName();
    }
}