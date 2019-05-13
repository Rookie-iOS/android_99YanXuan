package com.jjshop.bean;

import com.jjshop.utils.StringUtil;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/9/19
 */

public class OrderDetailBean extends BaseBean{

    private OrderDetailData data;

    public OrderDetailData getData() {
        return data;
    }

    public void setData(OrderDetailData data) {
        this.data = data;
    }

    public class OrderDetailData{
        private ArrayList<ProductListBean> rec_list;
        private OrderDetailInfoBean info;

        public ArrayList<ProductListBean> getRec_list() {
            return rec_list;
        }

        public void setRec_list(ArrayList<ProductListBean> rec_list) {
            this.rec_list = rec_list;
        }

        public OrderDetailInfoBean getInfo() {
            return info;
        }

        public void setInfo(OrderDetailInfoBean info) {
            this.info = info;
        }
    }
}
