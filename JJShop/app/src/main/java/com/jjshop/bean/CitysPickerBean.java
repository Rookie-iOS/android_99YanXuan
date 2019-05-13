package com.jjshop.bean;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/8/8
 */

public class CitysPickerBean {
    private int _id;
    private ArrayList<CitysBean.CityOneData> listOne;
    private ArrayList<ArrayList<CitysBean.CityTwoData>> listTwo;
    private ArrayList<ArrayList<ArrayList<CitysBean.CityThreeData>>> listThree;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public ArrayList<CitysBean.CityOneData> getListOne() {
        return listOne;
    }

    public void setListOne(ArrayList<CitysBean.CityOneData> listOne) {
        this.listOne = listOne;
    }

    public ArrayList<ArrayList<CitysBean.CityTwoData>> getListTwo() {
        return listTwo;
    }

    public void setListTwo(ArrayList<ArrayList<CitysBean.CityTwoData>> listTwo) {
        this.listTwo = listTwo;
    }

    public ArrayList<ArrayList<ArrayList<CitysBean.CityThreeData>>> getListThree() {
        return listThree;
    }

    public void setListThree(ArrayList<ArrayList<ArrayList<CitysBean.CityThreeData>>> listThree) {
        this.listThree = listThree;
    }
}
