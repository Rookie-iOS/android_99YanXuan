package com.jjshop.bean;

/**
 * 作者：张国庆
 * 时间：2018/12/14
 */

public class WuliuTopBean {
    private String kdName;
    private String kdNo;
    private int template;// 自定义字段  模板类型

    public int getTemplate() {
        return template;
    }

    public void setTemplate(int template) {
        this.template = template;
    }

    public String getKdName() {
        return kdName;
    }

    public void setKdName(String kdName) {
        this.kdName = kdName;
    }

    public String getKdNo() {
        return kdNo;
    }

    public void setKdNo(String kdNo) {
        this.kdNo = kdNo;
    }
}
