package com.jjshop.bean;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/9/29
 */

public class WuliuInfoBean extends BaseBean{
    private WuliuInfoBeanData data;

    public WuliuInfoBeanData getData() {
        return data;
    }

    public void setData(WuliuInfoBeanData data) {
        this.data = data;
    }

    public class WuliuInfoBeanData{
        private OrderDetailInfoBean orderinfo;
        private ArrayList<WuliuInfoBeanExpressinfo> expressinfo;

        public OrderDetailInfoBean getOrderinfo() {
            return orderinfo;
        }

        public void setOrderinfo(OrderDetailInfoBean orderinfo) {
            this.orderinfo = orderinfo;
        }

        public ArrayList<WuliuInfoBeanExpressinfo> getExpressinfo() {
            return expressinfo;
        }

        public void setExpressinfo(ArrayList<WuliuInfoBeanExpressinfo> expressinfo) {
            this.expressinfo = expressinfo;
        }
    }

    public class WuliuInfoBeanExpressinfo{
        private String express_name;
        private String express_no;
        private ArrayList<WuliuInfoBeanExpress> express;

        public ArrayList<WuliuInfoBeanExpress> getExpress() {
            return express;
        }

        public void setExpress(ArrayList<WuliuInfoBeanExpress> express) {
            this.express = express;
        }

        public String getExpress_name() {
            return express_name == null ? "" : express_name;
        }

        public void setExpress_name(String express_name) {
            this.express_name = express_name;
        }

        public String getExpress_no() {
            return express_no == null ? "" : express_no;
        }

        public void setExpress_no(String express_code) {
            this.express_no = express_code;
        }
    }
}
