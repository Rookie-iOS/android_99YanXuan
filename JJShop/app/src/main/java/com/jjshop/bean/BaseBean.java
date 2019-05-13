package com.jjshop.bean;

import java.io.Serializable;

/**
 * 作者：张国庆
 * 时间：2018/7/12
 */

public class BaseBean implements Serializable{
    private int code;
    private String msg;
    private String json;// 自定义字段
    private String userMobile;// 自定义字段
    private String userId;// 自定义字段

    public String getJson() {
        return json == null ? "" : json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getUserMobile() {
        return userMobile == null ? "" : userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
