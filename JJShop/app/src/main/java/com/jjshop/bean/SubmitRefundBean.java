package com.jjshop.bean;

/**
 * 作者：张国庆
 * 时间：2018/9/27
 */

public class SubmitRefundBean extends BaseBean{
    private SubmitRefundData data;

    public SubmitRefundData getData() {
        return data;
    }

    public void setData(SubmitRefundData data) {
        this.data = data;
    }

    public class SubmitRefundData{
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
