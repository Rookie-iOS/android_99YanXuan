package com.jjshop.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/8/8
 */

public class CitysBean extends BaseBean{
    ArrayList<CityOneData> data;

    public ArrayList<CityOneData> getData() {
        return data;
    }

    public void setData(ArrayList<CityOneData> data) {
        this.data = data;
    }

    public class CityOneData implements IPickerViewData {
        private int value;
        private String text;
        private ArrayList<CityTwoData> children;

        @Override
        public String getPickerViewText() {
            return this.getText();
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public ArrayList<CityTwoData> getChildren() {
            return children;
        }

        public void setChildren(ArrayList<CityTwoData> children) {
            this.children = children;
        }
    }

    public class CityTwoData implements IPickerViewData{
        private int value;
        private String text;
        private ArrayList<CityThreeData> children;

        @Override
        public String getPickerViewText() {
            return this.getText();
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public ArrayList<CityThreeData> getChildren() {
            return children;
        }

        public void setChildren(ArrayList<CityThreeData> children) {
            this.children = children;
        }
    }

    public class CityThreeData implements IPickerViewData{
        private int value;
        private String text;

        @Override
        public String getPickerViewText() {
            return this.getText();
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
