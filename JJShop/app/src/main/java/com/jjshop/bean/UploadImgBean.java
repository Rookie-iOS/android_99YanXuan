package com.jjshop.bean;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/8/7
 */

public class UploadImgBean extends BaseBean{
    ArrayList<UploadImgData> data;

    public ArrayList<UploadImgData> getData() {
        return data;
    }

    public void setData(ArrayList<UploadImgData> data) {
        this.data = data;
    }

    public class UploadImgData{
        private String url;
        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
