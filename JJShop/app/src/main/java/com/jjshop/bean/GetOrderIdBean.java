package com.jjshop.bean;

/**
 * 作者：张国庆
 * 时间：2018/8/18
 */

public class GetOrderIdBean extends BaseBean{
    private GetOrderIdData data;

    public GetOrderIdData getData() {
        return data;
    }

    public void setData(GetOrderIdData data) {
        this.data = data;
    }

    public class GetOrderIdData{
        private String payment_no;
        private String pay_type;
        private double pay_price;

        public String getPayment_no() {
            return payment_no == null ? "" : payment_no;
        }

        public void setPayment_no(String payment_no) {
            this.payment_no = payment_no;
        }

        public String getPay_type() {
            return pay_type == null ? "" : pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public double getPay_price() {
            return pay_price;
        }

        public void setPay_price(double pay_price) {
            this.pay_price = pay_price;
        }
    }
}
