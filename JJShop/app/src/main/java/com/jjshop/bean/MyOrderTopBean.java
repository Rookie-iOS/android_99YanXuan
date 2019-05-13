package com.jjshop.bean;

/**
 * 作者：张国庆
 * 时间：2018/12/14
 */

public class MyOrderTopBean {
    private String shopName;
    private String status_str;
    private long order_no;
    private int template;// 自定义字段  模板类型

    public int getTemplate() {
        return template;
    }

    public void setTemplate(int template) {
        this.template = template;
    }

    public long getOrder_no() {
        return order_no;
    }

    public void setOrder_no(long order_no) {
        this.order_no = order_no;
    }

    public String getShopName() {
        return shopName == null ? "" : shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStatus_str() {
        return status_str == null ? "" : status_str;
    }

    public void setStatus_str(String status_str) {
        this.status_str = status_str;
    }
}
