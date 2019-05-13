package com.jjshop.bean;

import com.jjshop.utils.StringUtil;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/12/14
 */

public class MyOrderBottomBean {
    private long order_no;// 订单号
    private int sum_num;// 商品总数
    private double price;// 商品总价
    private ArrayList<MyOrderActionData> action;
    private int template;// 自定义字段  模板类型

    public int getTemplate() {
        return template;
    }

    public void setTemplate(int template) {
        this.template = template;
    }

    public int getSum_num() {
        return sum_num;
    }

    public void setSum_num(int sum_num) {
        this.sum_num = sum_num;
    }

    public double getPrice() {
        return StringUtil.getPrice(price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ArrayList<MyOrderActionData> getAction() {
        return action;
    }

    public void setAction(ArrayList<MyOrderActionData> action) {
        this.action = action;
    }

    public long getOrder_no() {
        return order_no;
    }

    public void setOrder_no(long order_no) {
        this.order_no = order_no;
    }
}
