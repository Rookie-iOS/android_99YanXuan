package com.jjshop.bean;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/9/18
 */

public class MyOrderBean extends BaseBean{
    private MyOrderData data;

    public MyOrderData getData() {
        return data;
    }

    public void setData(MyOrderData data) {
        this.data = data;
    }

    public class MyOrderData{
        private ArrayList<MyOrderDataList> data;
        private MyOrderMenuNums menu_nums;

        public MyOrderMenuNums getMenu_nums() {
            return menu_nums;
        }

        public void setMenu_nums(MyOrderMenuNums menu_nums) {
            this.menu_nums = menu_nums;
        }

        public ArrayList<MyOrderDataList> getData() {
            return data;
        }

        public void setData(ArrayList<MyOrderDataList> data) {
            this.data = data;
        }
    }

    public class MyOrderDataList{
        private int id;
        private long order_no;
        private int status;
        private String status_str;
        private double price;
        private double freight_price;
        private ArrayList<MyOrderProductList> product;
        private ShopBean shop;
        private ArrayList<MyOrderActionData> action;
        private int sum_num;
        private String shop_idcode;

        public String getShop_idcode() {
            return shop_idcode == null ? "" : shop_idcode;
        }

        public void setShop_idcode(String shop_idcode) {
            this.shop_idcode = shop_idcode;
        }

        public int getSum_num() {
            return sum_num;
        }

        public void setSum_num(int sum_num) {
            this.sum_num = sum_num;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
            return status_str == null ? "" : status_str;
        }

        public void setStatus_str(String status_str) {
            this.status_str = status_str;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getFreight_price() {
            return freight_price;
        }

        public void setFreight_price(double freight_price) {
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

        public ArrayList<MyOrderActionData> getAction() {
            return action;
        }

        public void setAction(ArrayList<MyOrderActionData> action) {
            this.action = action;
        }
    }

    public class MyOrderMenuNums{
        private int pending_payment;// 待付款
        private int pending_delivery;// 待发货
        private int pending_goods;// 待收货

        public int getPending_payment() {
            return pending_payment;
        }

        public void setPending_payment(int pending_payment) {
            this.pending_payment = pending_payment;
        }

        public int getPending_delivery() {
            return pending_delivery;
        }

        public void setPending_delivery(int pending_delivery) {
            this.pending_delivery = pending_delivery;
        }

        public int getPending_goods() {
            return pending_goods;
        }

        public void setPending_goods(int pending_goods) {
            this.pending_goods = pending_goods;
        }
    }

}
