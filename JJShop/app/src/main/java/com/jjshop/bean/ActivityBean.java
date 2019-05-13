package com.jjshop.bean;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/10/18
 */

public class ActivityBean extends BaseBean{
    private ArrayList<ActivityBeanData> data;

    public ArrayList<ActivityBeanData> getData() {
        return data;
    }

    public void setData(ArrayList<ActivityBeanData> data) {
        this.data = data;
    }

    public class ActivityBeanData{
        private int id;
        private String name;
        private String thumb;
        private int type;
        private String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getThumb() {
            return thumb == null ? "" : thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url == null ? "" : url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
