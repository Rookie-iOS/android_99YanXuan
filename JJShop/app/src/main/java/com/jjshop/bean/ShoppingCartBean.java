package com.jjshop.bean;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/8/3
 */

public class ShoppingCartBean extends BaseBean {
    public ShoppingCartData data;

    public ShoppingCartData getData() {
        return data;
    }

    public void setData(ShoppingCartData data) {
        this.data = data;
    }

    public class ShoppingCartData{
        private ArrayList<ProductListBean> rec_list;
        public CartData data;
        private int cartProfuctNum;

        public int getCartProfuctNum() {
            return cartProfuctNum;
        }

        public void setCartProfuctNum(int cartProfuctNum) {
            this.cartProfuctNum = cartProfuctNum;
        }

        public class CartData{
            private ArrayList<CartDataBean> products;
            private double total_price;// 总计
            private double discount;// 优惠

            public double getTotal_price() {
                return total_price;
            }

            public void setTotal_price(double total_price) {
                this.total_price = total_price;
            }

            public double getDiscount() {
                return discount;
            }

            public void setDiscount(double discount) {
                this.discount = discount;
            }

            public ArrayList<CartDataBean> getProducts() {
                return products;
            }

            public void setProducts(ArrayList<CartDataBean> products) {
                this.products = products;
            }

        }

        public CartData getData() {
            return data;
        }

        public void setData(CartData data) {
            this.data = data;
        }

        public ArrayList<ProductListBean> getRec_list() {
            return rec_list;
        }

        public void setRec_list(ArrayList<ProductListBean> rec_list) {
            this.rec_list = rec_list;
        }
    }
}
