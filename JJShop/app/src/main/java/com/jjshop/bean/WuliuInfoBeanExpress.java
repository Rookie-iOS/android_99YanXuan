package com.jjshop.bean;

/**
 * 作者：张国庆
 * 时间：2018/12/17
 */

public class WuliuInfoBeanExpress {
    private String time;
    private String context;
    private int position;// 自定义字段  下标
    private int template;// 自定义字段  模板类型

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getTemplate() {
        return template;
    }

    public void setTemplate(int template) {
        this.template = template;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContext() {
        return context == null ? "" : context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
