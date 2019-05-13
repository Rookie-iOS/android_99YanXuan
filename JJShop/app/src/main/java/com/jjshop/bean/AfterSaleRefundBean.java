package com.jjshop.bean;

import com.jjshop.utils.StringUtil;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/9/20
 */

public class AfterSaleRefundBean extends BaseBean{
    private ArrayList<AfterSaleRefundData> data;

    public ArrayList<AfterSaleRefundData> getData() {
        return data;
    }

    public void setData(ArrayList<AfterSaleRefundData> data) {
        this.data = data;
    }

    public class AfterSaleRefundData{
        private int id;
        private int order_id;
        private long order_no;
        private int status;
        private int type;
        private String status_str;
        private MyOrderProductList item;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getStatus_str() {
            return status_str;
        }

        public void setStatus_str(String status_str) {
            this.status_str = status_str;
        }

        public MyOrderProductList getItem() {
            return item;
        }

        public void setItem(MyOrderProductList item) {
            this.item = item;
        }
    }
}
