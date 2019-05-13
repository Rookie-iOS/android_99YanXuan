package com.jjshop.bean;

import com.jjshop.utils.StringUtil;

/**
 * 作者：张国庆
 * 时间：2018/9/19
 */

public class MyOrderProductList {
    private int id;
    private String name;
    private String product_name;
    private int product_id;
    private String color_name;
    private String size;
    private double price;
    private double org_price;// 秒杀原价
    private int num;
    private String[] img;
    private MyOrderActionData action;
    private int service_id;
    private String idcode;
    private long order_no;// 自定义字段  订单号
    private int template;// 自定义字段  模板类型

    public long getOrder_no() {
        return order_no;
    }

    public void setOrder_no(long order_no) {
        this.order_no = order_no;
    }

    public int getTemplate() {
        return template;
    }

    public void setTemplate(int template) {
        this.template = template;
    }

    public double getOrg_price() {
        return StringUtil.getPrice(org_price);
    }

    public void setOrg_price(double org_price) {
        this.org_price = org_price;
    }

    public String getIdcode() {
        return idcode;
    }

    public void setIdcode(String idcode) {
        this.idcode = idcode;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public MyOrderActionData getAction() {
        return action;
    }

    public void setAction(MyOrderActionData action) {
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return StringUtil.isEmpty(name) ? product_name : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getColor_name() {
        return color_name == null ? "" : color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }

    public String getSize() {
        return size == null ? "" : size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getPrice() {
        return StringUtil.getPrice(price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String[] getImg() {
        return img;
    }

    public void setImg(String[] img) {
        this.img = img;
    }

}
