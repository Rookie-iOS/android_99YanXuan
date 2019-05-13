package com.jjshop.bean;

import java.util.ArrayList;

public class HotZhuanquBean extends BaseBean {
    public HotZhuanquData data;

    public HotZhuanquData getData() {
        return data;
    }

    public void setData(HotZhuanquData data) {
        this.data = data;
    }

    public class HotZhuanquData {
        private ArrayList<ProductListBean> product_list;

        public ArrayList<ProductListBean> getProduct_list() {
            return product_list;
        }

        public void setProduct_list(ArrayList<ProductListBean> product_list) {
            this.product_list = product_list;
        }
    }

}
