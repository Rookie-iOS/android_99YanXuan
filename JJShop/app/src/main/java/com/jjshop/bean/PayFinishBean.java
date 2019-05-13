package com.jjshop.bean;

import com.jjshop.utils.StringUtil;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/8/22
 */

public class PayFinishBean extends BaseBean{
    private PayFinishData data;

    public PayFinishData getData() {
        return data;
    }

    public void setData(PayFinishData data) {
        this.data = data;
    }

    public class PayFinishData{
        private int type;
        private RedBaoData red_envelope;
        private PaymentorderData paymentorder;
        private ArrayList<ProductListBean> rec_list;

        public PaymentorderData getPaymentorder() {
            return paymentorder;
        }

        public void setPaymentorder(PaymentorderData paymentorder) {
            this.paymentorder = paymentorder;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public ArrayList<ProductListBean> getRec_list() {
            return rec_list;
        }

        public void setRec_list(ArrayList<ProductListBean> rec_list) {
            this.rec_list = rec_list;
        }

        public RedBaoData getRed_envelope() {
            return red_envelope;
        }

        public void setRed_envelope(RedBaoData red_envelope) {
            this.red_envelope = red_envelope;
        }
    }

    public class RedBaoData {

        private String product_name;
        private String user_mobile;
        private String total_price;

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getUser_mobile() {
            return user_mobile;
        }

        public void setUser_mobile(String user_mobile) {
            this.user_mobile = user_mobile;
        }

        public String getTotal_price() {
            return total_price;
        }

        public void setTotal_price(String total_price) {
            this.total_price = total_price;
        }
    }

    public class PaymentorderData{

        private double pay_price;

        public double getPay_price() {
            return StringUtil.getPrice(pay_price);
        }

        public void setPay_price(double pay_price) {
            this.pay_price = pay_price;
        }
    }
}
