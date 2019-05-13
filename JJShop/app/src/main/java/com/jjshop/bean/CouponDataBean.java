package com.jjshop.bean;

import com.jjshop.utils.StringUtil;

import java.io.Serializable;

/**
 * 作者：张国庆
 * 时间：2018/12/4
 */

public class CouponDataBean implements Serializable {

    private int id;
    private double price;//卡卷优惠金额
    private String patch_name;//卡卷名字
    private double use_price;//满减金额
    private int time_type;//1显示时间段、其他不限时间
    private String start_time;
    private String end_time;
    private String id_code;//卡卷idcode
    private String note;
    private int status;
    private boolean is_show_guize;// 自定义字段 是否显示详细规则   true:显示   false:不显示

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNote() {
        return note == null ? "" : note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isIs_show_guize() {
        return is_show_guize;
    }

    public void setIs_show_guize(boolean is_show_guize) {
        this.is_show_guize = is_show_guize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return StringUtil.getPrice(price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPatch_name() {
        return patch_name == null ? "" : patch_name;
    }

    public void setPatch_name(String patch_name) {
        this.patch_name = patch_name;
    }

    public double getUse_price() {
        return StringUtil.getPrice(use_price);
    }

    public void setUse_price(double use_price) {
        this.use_price = use_price;
    }

    public int getTime_type() {
        return time_type;
    }

    public void setTime_type(int time_type) {
        this.time_type = time_type;
    }

    public String getStart_time() {
        return start_time == null ? "" : start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time == null ? "" : end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getId_code() {
        return id_code == null ? "" : id_code;
    }

    public void setId_code(String id_code) {
        this.id_code = id_code;
    }

}
