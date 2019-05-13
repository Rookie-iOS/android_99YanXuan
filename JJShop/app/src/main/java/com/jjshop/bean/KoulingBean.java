package com.jjshop.bean;

/**
 * 作者：张国庆
 * 时间：2018/11/8
 */

public class KoulingBean extends BaseBean{
    private KouLingBeanData data;

    public KouLingBeanData getData() {
        return data;
    }

    public void setData(KouLingBeanData data) {
        this.data = data;
    }

    public class KouLingBeanData{
        private String img;
        private String title;
        private String url;

        public String getImg() {
            return img == null ? "" : img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTitle() {
            return title == null ? "" : title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url == null ? "" : url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
