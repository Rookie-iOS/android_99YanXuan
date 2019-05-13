package com.jjshop.bean;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/8/1
 */

public class PersonCenterBean extends BaseBean{

    public PersonCenterData data;

    public PersonCenterData getData() {
        return data;
    }

    public void setData(PersonCenterData data) {
        this.data = data;
    }

    public class PersonCenterData{
        private PersonInfoBean data;
        public ArrayList<NavigationBean> navigation;// 订单类型、个数
        public PersonInfoBean getData() {
            return data;
        }

        public ArrayList<NavigationBean> getNavigation() {
            return navigation;
        }

        public void setNavigation(ArrayList<NavigationBean> navigation) {
            this.navigation = navigation;
        }

        public void setData(PersonInfoBean data) {
            this.data = data;
        }

        public class PersonInfoBean{
            private int uid;// 用户id
            private String img;// 头像
            private String name;// 昵称
            private String sex_name;// 性别
            private String mobile;// 手机号
            private String birthday;// 出生日期
            private String areas_name;// 城市
            private String bank_real_name;// 真实姓名

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getImg() {
                return img == null ? "" : img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getAreas_name() {
                return areas_name == null ? "" : areas_name;
            }

            public void setAreas_name(String areas_name) {
                this.areas_name = areas_name;
            }

            public String getBank_real_name() {
                return bank_real_name == null ? "" : bank_real_name;
            }

            public void setBank_real_name(String bank_real_name) {
                this.bank_real_name = bank_real_name;
            }

            public String getBirthday() {
                return birthday == null ? "" : birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getName() {
                return name == null ? "" : name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSex_name() {
                return sex_name == null ? "" : sex_name;
            }

            public void setSex_name(String sex_name) {
                this.sex_name = sex_name;
            }

            public String getMobile() {
                return mobile == null ? "" : mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }
        }
    }
}
