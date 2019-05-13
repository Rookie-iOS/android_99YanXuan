package com.jjshop.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/8/17
 */

public class AddressListBean  extends BaseBean{
    private AddressListData data;

    public AddressListData getData() {
        return data;
    }

    public void setData(AddressListData data) {
        this.data = data;
    }

    public class AddressListData implements Serializable{
        private ArrayList<AddressList> data;

        public ArrayList<AddressList> getData() {
            return data;
        }

        public void setData(ArrayList<AddressList> data) {
            this.data = data;
        }
    }
    public class AddressList implements Serializable{
        private int id;
        private String accept_name;
        private String mobile;
        private String address;
        private int is_default;
        private int province;
        private int city;
        private int area;
        private String province_name;
        private String city_name;
        private String area_name;
        private String id_code;

        public void setId_code(String id_code) {
            this.id_code = id_code;
        }

        public String getId_code() {
            return id_code;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAccept_name() {
            return accept_name == null ? "" : accept_name;
        }

        public void setAccept_name(String accept_name) {
            this.accept_name = accept_name;
        }

        public String getMobile() {
            return mobile == null ? "" : mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAddress() {
            return address == null ? "" : address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getIs_default() {
            return is_default;
        }

        public void setIs_default(int is_default) {
            this.is_default = is_default;
        }

        public int getProvince() {
            return province;
        }

        public void setProvince(int province) {
            this.province = province;
        }

        public int getCity() {
            return city;
        }

        public void setCity(int city) {
            this.city = city;
        }

        public int getArea() {
            return area;
        }

        public void setArea(int area) {
            this.area = area;
        }

        public String getProvince_name() {
            return province_name == null ? "" : province_name;
        }

        public void setProvince_name(String province_name) {
            this.province_name = province_name;
        }

        public String getCity_name() {
            return city_name == null ? "" : city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getArea_name() {
            return area_name == null ? "" : area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }
    }
}
