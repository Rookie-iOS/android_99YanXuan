package com.jjshop.bean;

import com.jjshop.utils.StringUtil;


/**
 * 作者：张国庆
 * 时间：2018/9/26
 */

public class RefundBean extends BaseBean{
    private RefundBeanData data;
    public RefundBeanData getData() {
        return data;
    }

    public void setData(RefundBeanData data) {
        this.data = data;
    }

    public class RefundBeanData{
        private RefundData data;
        private double refund_aomunt;

        public RefundData getData() {
            return data;
        }

        public void setData(RefundData data) {
            this.data = data;
        }

        public double getRefund_aomunt() {
            return StringUtil.getPrice(refund_aomunt);
        }

        public void setRefund_aomunt(double refund_aomunt) {
            this.refund_aomunt = refund_aomunt;
        }
    }

    public class RefundData{
        private long order_no;
        private double promotion_coupon_price;
        private double promotion_order_price;
        private double promotion_goods_price;
        private double price;

        public double getBalance_price() {
            return balance_price;
        }

        public void setBalance_price(double balance_price) {
            this.balance_price = balance_price;
        }

        private double balance_price;
        private double refunded_fee;
        private MyOrderProductList product;

        public long getOrder_no() {
            return order_no;
        }

        public void setOrder_no(long order_no) {
            this.order_no = order_no;
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

        public double getPrice() {
            return StringUtil.getPrice(price);
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getRefunded_fee() {
            return StringUtil.getPrice(refunded_fee);
        }

        public void setRefunded_fee(double refunded_fee) {
            this.refunded_fee = refunded_fee;
        }

        public MyOrderProductList getProduct() {
            return product;
        }

        public void setProduct(MyOrderProductList product) {
            this.product = product;
        }
    }




}
