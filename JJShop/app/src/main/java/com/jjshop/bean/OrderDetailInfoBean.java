package com.jjshop.bean;

import com.jjshop.utils.StringUtil;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/9/29
 */

public class OrderDetailInfoBean {

    private int id;
    private double goods_total_price;
    private long order_no;
    private int status;
    private String status_str;
    private String created_at;
    private String province;
    private String city;
    private String area;
    private String address;
    private double promotion_coupon_price;
    private double promotion_order_price;
    private double promotion_goods_price;
    private String accept_name;
    private String mobile;
    private double price;
    private double account_balance_price;
    private String freight_price;
    private ArrayList<MyOrderProductList> product;
    private ShopBean shop;
    private String status_str_time;

    private ArrayList<MyOrderActionData> action;

    public ArrayList<MyOrderActionData> getAction() {
        return action;
    }

    public void setAction(ArrayList<MyOrderActionData> action) {
        this.action = action;
    }


    public String getStatus_str_time() {
        return status_str_time == null ? "" : status_str_time;
    }

    public void setStatus_str_time(String status_str_time) {
        this.status_str_time = status_str_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getGoods_total_price() {
        return StringUtil.getPrice(goods_total_price);
    }

    public void setGoods_total_price(double goods_total_price) {
        this.goods_total_price = goods_total_price;
    }

    public long getOrder_no() {
        return order_no;
    }

    public void setOrder_no(long order_no) {
        this.order_no = order_no;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatus_str() {
        return status_str;
    }

    public void setStatus_str(String status_str) {
        this.status_str = status_str;
    }

    public String getCreated_at() {
        return created_at == null ? "" : created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getProvince() {
        return province == null ? "" : province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city == null ? "" : city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area == null ? "" : area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPromotion_coupon_price() {
        return StringUtil.getPrice(promotion_coupon_price);
    }

    public void setPromotion_coupon_price(double promotion_coupon_price) {
        this.promotion_coupon_price = promotion_coupon_price;
    }

    public double getPromotion_order_price() {
        return StringUtil.getPrice(promotion_order_price);
    }

    public void setPromotion_order_price(double promotion_order_price) {
        this.promotion_order_price = promotion_order_price;
    }

    public double getPromotion_goods_price() {
        return StringUtil.getPrice(promotion_goods_price);
    }

    public void setPromotion_goods_price(double promotion_goods_price) {
        this.promotion_goods_price = promotion_goods_price;
    }

    public String getAccept_name() {
        return accept_name == null ? "" : accept_name;
    }

    public void setAccept_name(String accept_name) {
        this.accept_name = accept_name;
    }

    public String getMobile() {
        return mobile == null ? "" : mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public double getPrice() {
        return StringUtil.getPrice(price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAccount_balance_price() {
        return StringUtil.getPrice(account_balance_price);
    }

    public void setAccount_balance_price(double account_balance_price) {
        this.account_balance_price = account_balance_price;
    }

    public String getFreight_price() {
        return freight_price == null ? "0.0" : freight_price;
    }

    public void setFreight_price(String freight_price) {
        this.freight_price = freight_price;
    }

    public ArrayList<MyOrderProductList> getProduct() {
        return product;
    }

    public void setProduct(ArrayList<MyOrderProductList> product) {
        this.product = product;
    }

    public ShopBean getShop() {
        return shop;
    }

    public void setShop(ShopBean shop) {
        this.shop = shop;
    }
}
