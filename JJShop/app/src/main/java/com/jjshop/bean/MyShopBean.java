package com.jjshop.bean;

import com.jjshop.utils.StringUtil;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/7/31
 */

public class MyShopBean extends BaseBean{
    public MyShopDataBean data;

    public MyShopDataBean getData() {
        return data;
    }

    public void setData(MyShopDataBean data) {
        this.data = data;
    }

    public class MyShopDataBean{
        private int message_count;// 消息个数
        public ShopInfoBean shopinfo;// 店铺信息
        public InfoOrderBean infoOrderData;// 今日订单、销售，本月销售
        public ArrayList<NavigationBean> navigation;// 订单类型、个数
        private int current_date_uv;// 今日浏览人数
        private int last_date_uv;// 昨日浏览人数
        public InfoShopBopBean infoShopBop;// 可提现金额
        public InfosettledBean infosettled;// 收入
        public ShopInviteInfoBean shopInviteInfo;// 开店奖励
        public JJShareBean.DataBean sharshopinfo;

        public JJShareBean.DataBean getSharshopinfo() {
            return sharshopinfo;
        }

        public void setSharshopinfo(JJShareBean.DataBean sharshopinfo) {
            this.sharshopinfo = sharshopinfo;
        }

        public ShopInviteInfoBean getShopInviteInfo() {
            return shopInviteInfo;
        }

        public void setShopInviteInfo(ShopInviteInfoBean shopInviteInfo) {
            this.shopInviteInfo = shopInviteInfo;
        }

        public int getMessage_count() {
            return message_count;
        }

        public void setMessage_count(int message_count) {
            this.message_count = message_count;
        }

        public ShopInfoBean getShopinfo() {
            return shopinfo;
        }

        public void setShopinfo(ShopInfoBean shopinfo) {
            this.shopinfo = shopinfo;
        }

        public InfoOrderBean getInfoOrderData() {
            return infoOrderData;
        }

        public void setInfoOrderData(InfoOrderBean infoOrderData) {
            this.infoOrderData = infoOrderData;
        }

        public ArrayList<NavigationBean> getNavigation() {
            return navigation;
        }

        public void setNavigation(ArrayList<NavigationBean> navigation) {
            this.navigation = navigation;
        }

        public int getCurrent_date_uv() {
            return current_date_uv;
        }

        public void setCurrent_date_uv(int current_date_uv) {
            this.current_date_uv = current_date_uv;
        }

        public int getLast_date_uv() {
            return last_date_uv;
        }

        public void setLast_date_uv(int last_date_uv) {
            this.last_date_uv = last_date_uv;
        }

        public InfoShopBopBean getInfoShopBop() {
            return infoShopBop;
        }

        public void setInfoShopBop(InfoShopBopBean infoShopBop) {
            this.infoShopBop = infoShopBop;
        }

        public InfosettledBean getInfosettled() {
            return infosettled;
        }

        public void setInfosettled(InfosettledBean infosettled) {
            this.infosettled = infosettled;
        }

        public class ShopInfoBean{
            private String shopname;
            private String shopinfo;
            private String shoplogo;
            private String shop_grade;
            private String shop_grade_name;

            public String getShopname() {
                return shopname == null ? "" : shopname;
            }

            public void setShopname(String shopname) {
                this.shopname = shopname;
            }

            public String getShopinfo() {
                return shopinfo == null ? "" : shopinfo;
            }

            public void setShopinfo(String shopinfo) {
                this.shopinfo = shopinfo;
            }

            public String getShoplogo() {
                return shoplogo == null ? "" : shoplogo;
            }

            public void setShoplogo(String shoplogo) {
                this.shoplogo = shoplogo;
            }

            public String getShop_grade() {
                return shop_grade == null ? "" : shop_grade;
            }

            public void setShop_grade(String shop_grade) {
                this.shop_grade = shop_grade;
            }

            public String getShop_grade_name() {
                return shop_grade_name == null ? "" : shop_grade_name;
            }

            public void setShop_grade_name(String shop_grade_name) {
                this.shop_grade_name = shop_grade_name;
            }
        }

        public class InfoOrderBean{
            private int today_order_num;
            private double today_total_sales;
            private double month_total_sales;

            public int getToday_order_num() {
                return today_order_num;
            }

            public void setToday_order_num(int today_order_num) {
                this.today_order_num = today_order_num;
            }

            public double getToday_total_sales() {
                return StringUtil.getPrice(today_total_sales);
            }

            public void setToday_total_sales(double today_total_sales) {
                this.today_total_sales = today_total_sales;
            }

            public double getMonth_total_sales() {
                return StringUtil.getPrice(month_total_sales);
            }

            public void setMonth_total_sales(double month_total_sales) {
                this.month_total_sales = month_total_sales;
            }
        }

        public class InfoShopBopBean{
            private double bop_total;//可提现金额

            public double getBop_total() {
                return StringUtil.getPrice(bop_total);
            }

            public void setBop_total(double bop_total) {
                this.bop_total = bop_total;
            }
        }

        public class InfosettledBean{
            private double unsettled;// 未结算收入
            private double settled;// 已结算收入

            public double getUnsettled() {
                return StringUtil.getPrice(unsettled);
            }

            public void setUnsettled(double unsettled) {
                this.unsettled = unsettled;
            }

            public double getSettled() {
                return StringUtil.getPrice(settled);
            }

            public void setSettled(double settled) {
                this.settled = settled;
            }
        }
        public class ShopInviteInfoBean{
            private double reward_value;

            public double getReward_value() {
                return StringUtil.getPrice(reward_value);
            }

            public void setReward_value(double reward_value) {
                this.reward_value = reward_value;
            }
        }

    }
}
