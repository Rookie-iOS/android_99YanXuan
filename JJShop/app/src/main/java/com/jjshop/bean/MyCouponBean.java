package com.jjshop.bean;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/12/4
 */

public class MyCouponBean extends BaseBean{
    private ArrayList<CouponDataBean> data;

    public ArrayList<CouponDataBean> getData() {
        return data;
    }

    public void setData(ArrayList<CouponDataBean> data) {
        this.data = data;
    }
}
