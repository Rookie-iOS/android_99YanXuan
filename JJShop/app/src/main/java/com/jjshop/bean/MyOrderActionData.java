package com.jjshop.bean;

/**
 * 作者：张国庆
 * 时间：2018/9/19
 */

public class MyOrderActionData {

    private String name;
    private String uri;
    private int status;
    private int app_status;
    private MyOrderActionParameter parameter;

    public int getApp_status() {
        return app_status;
    }

    public void setApp_status(int app_status) {
        this.app_status = app_status;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri == null ? "" : uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MyOrderActionParameter getParameter() {
        return parameter;
    }

    public void setParameter(MyOrderActionParameter parameter) {
        this.parameter = parameter;
    }

    public class MyOrderActionParameter{
        private long order_no;
        private int item_id;
        private String shop;
        private String status;

        public String getStatus() {
            return status == null ? "" : status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public long getOrder_no() {
            return order_no;
        }

        public void setOrder_no(long order_no) {
            this.order_no = order_no;
        }

        public int getItem_id() {
            return item_id;
        }

        public void setItem_id(int item_id) {
            this.item_id = item_id;
        }

        public String getShop() {
            return shop == null ? "" : shop;
        }

        public void setShop(String shop) {
            this.shop = shop;
        }
    }

}
