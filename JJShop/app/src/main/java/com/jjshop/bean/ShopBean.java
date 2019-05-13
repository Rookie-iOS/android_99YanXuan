package com.jjshop.bean;

/**
 * 作者：张国庆
 * 时间：2018/9/19
 */

public class ShopBean {
    private String name;
    private String logo;

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
