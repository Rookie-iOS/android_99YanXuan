package com.jjshop.bean;

import com.jjshop.utils.StringUtil;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/8/16
 */

public class SubmitOrderBean extends BaseBean{
    private SubmitOrderData data;

    public SubmitOrderData getData() {
        return data;
    }

    public void setData(SubmitOrderData data) {
        this.data = data;
    }

    public class SubmitOrderData implements Serializable{
        ArrayList<CouponDataBean> coupons;
        private int coupons_id;
        private CouponDataBean selected_coupons;
        private SubmitOrderInfo  data;
        private String address;
        private int coupon_count;
        private double promotion_coupon_price;

        public ArrayList<CouponDataBean> getCoupons() {
            return coupons;
        }

        public void setCoupons(ArrayList<CouponDataBean> coupons) {
            this.coupons = coupons;
        }

        public int getCoupons_id() {
            return coupons_id;
        }

        public void setCoupons_id(int coupons_id) {
            this.coupons_id = coupons_id;
        }

        public CouponDataBean getSelected_coupons() {
            return selected_coupons;
        }

        public void setSelected_coupons(CouponDataBean selected_coupons) {
            this.selected_coupons = selected_coupons;
        }

        public SubmitOrderInfo getData() {
            return data;
        }

        public void setData(SubmitOrderInfo data) {
            this.data = data;
        }

        public String getAddress() {
            return address == null ? "" : address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getCoupon_count() {
            return coupon_count;
        }

        public void setCoupon_count(int coupon_count) {
            this.coupon_count = coupon_count;
        }

        public double getPromotion_coupon_price() {
            return StringUtil.getPrice(promotion_coupon_price);
        }

        public void setPromotion_coupon_price(double promotion_coupon_price) {
            this.promotion_coupon_price = promotion_coupon_price;
        }
    }

    public class SubmitOrderInfo implements Serializable{
        private AddressData address;
        private Account_balance account_balance;
        private double goods_total_price;
        private double total_price;
        private double promotion_coupon_price;
        private double freight_price;
        private double promotion_goods_price;
        private double promotion_order_price;
        private double freight_promotion_price;
        private ArrayList<CartDataBean> products;

        public AddressData getAddress() {
            return address;
        }

        public void setAddress(AddressData address) {
            this.address = address;
        }

        public double getGoods_total_price() {
            return StringUtil.getPrice(goods_total_price);
        }

        public void setGoods_total_price(double goods_total_price) {
            this.goods_total_price = goods_total_price;
        }

        public double getTotal_price() {
            return StringUtil.getPrice(total_price);
        }

        public void setTotal_price(double total_price) {
            this.total_price = total_price;
        }

        public double getPromotion_coupon_price() {
            return StringUtil.getPrice(promotion_coupon_price);
        }

        public void setPromotion_coupon_price(double promotion_coupon_price) {
            this.promotion_coupon_price = promotion_coupon_price;
        }

        public double getFreight_price() {
            return StringUtil.getPrice(freight_price);
        }

        public void setFreight_price(double freight_price) {
            this.freight_price = freight_price;
        }

        public double getPromotion_goods_price() {
            return StringUtil.getPrice(promotion_goods_price);
        }

        public void setPromotion_goods_price(double promotion_goods_price) {
            this.promotion_goods_price = promotion_goods_price;
        }

        public double getPromotion_order_price() {
            return StringUtil.getPrice(promotion_order_price);
        }

        public void setPromotion_order_price(double promotion_order_price) {
            this.promotion_order_price = promotion_order_price;
        }

        public double getFreight_promotion_price() {
            return freight_promotion_price;
        }

        public void setFreight_promotion_price(double freight_promotion_price) {
            this.freight_promotion_price = freight_promotion_price;
        }

        public ArrayList<CartDataBean> getProducts() {
            return products;
        }

        public void setProducts(ArrayList<CartDataBean> products) {
            this.products = products;
        }

        public Account_balance getAccount_balance() {
            return account_balance;
        }

        public void setAccount_balance(Account_balance account_balance) {
            this.account_balance = account_balance;
        }
    }

    public class Account_balance implements Serializable {

        private double available; // 可用余额
        private double deduction; // 余额抵扣

        public double getAvailable() {
            return StringUtil.getPrice(available);
        }


        public double getDeduction() {
            return StringUtil.getPrice(deduction);
        }

        public void setAvailable(double available) {
            this.available = available;
        }

        public void setDeduction(double deduction) {
            this.deduction = deduction;
        }
    }

    public class AddressData implements Serializable{
        private int id;
        private String accept_name;
        private String mobile;
        private CityData province;
        private CityData city;
        private CityData area;
        private String address;
        private int is_default;
        private String address_idcode;

        public String getAddress_idcode() {
            return address_idcode == null ? "" : address_idcode;
        }

        public void setAddress_idcode(String address_idcode) {
            this.address_idcode = address_idcode;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public CityData getProvince() {
            return province;
        }

        public void setProvince(CityData province) {
            this.province = province;
        }

        public CityData getCity() {
            return city;
        }

        public void setCity(CityData city) {
            this.city = city;
        }

        public CityData getArea() {
            return area;
        }

        public void setArea(CityData area) {
            this.area = area;
        }

        public String getAddress() {
            return address == null ? "" : address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getIs_default() {
            return is_default;
        }

        public void setIs_default(int is_default) {
            this.is_default = is_default;
        }
    }

    public class CityData implements Serializable{
        private int id;
        private String name;

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
    }

}
