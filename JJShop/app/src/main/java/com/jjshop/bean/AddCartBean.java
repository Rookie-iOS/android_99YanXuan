package com.jjshop.bean;

/**
 * 作者：张国庆
 * 时间：2018/8/15
 */

public class AddCartBean extends BaseBean {
    private AddCartData data;

    public AddCartData getData() {
        return data;
    }

    public void setData(AddCartData data) {
        this.data = data;
    }

    public class AddCartData{
        private int cartProfuctNum;

        public int getCartProfuctNum() {
            return cartProfuctNum;
        }

        public void setCartProfuctNum(int cartProfuctNum) {
            this.cartProfuctNum = cartProfuctNum;
        }
    }
}
