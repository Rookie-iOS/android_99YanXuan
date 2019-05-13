package com.jjshop.bean;

/**
 * 作者：张国庆
 * 时间：2018/10/25
 */

public class ADShowBean extends BaseBean{

    private ADShowBeanData data;

    public ADShowBeanData getData() {
        return data;
    }

    public void setData(ADShowBeanData data) {
        this.data = data;
    }

    public class ADShowBeanData{
        private int media_id;
        private String media_type;
        private int postions_id;
        private int postions_type;
        private int p_width;
        private int p_height;
        private String describe;
        private String show_url;
        private String click_url;
        private int banner_id;
        private int banner_type;
        private int b_width;
        private int b_height;
        private String ads_contents;
        private String checksum;
        private String time;
        private String ads_src_url;

        public String getAds_src_url() {
            return ads_src_url == null ? "" : ads_src_url;
        }

        public void setAds_src_url(String ads_src_url) {
            this.ads_src_url = ads_src_url;
        }

        public int getMedia_id() {
            return media_id;
        }

        public void setMedia_id(int media_id) {
            this.media_id = media_id;
        }

        public String getMedia_type() {
            return media_type;
        }

        public void setMedia_type(String media_type) {
            this.media_type = media_type;
        }

        public int getPostions_id() {
            return postions_id;
        }

        public void setPostions_id(int postions_id) {
            this.postions_id = postions_id;
        }

        public int getPostions_type() {
            return postions_type;
        }

        public void setPostions_type(int postions_type) {
            this.postions_type = postions_type;
        }

        public int getP_width() {
            return p_width;
        }

        public void setP_width(int p_width) {
            this.p_width = p_width;
        }

        public int getP_height() {
            return p_height;
        }

        public void setP_height(int p_height) {
            this.p_height = p_height;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getShow_url() {
            return show_url;
        }

        public void setShow_url(String show_url) {
            this.show_url = show_url;
        }

        public String getClick_url() {
            return click_url == null ? "" : click_url;
        }

        public void setClick_url(String click_url) {
            this.click_url = click_url;
        }

        public int getBanner_id() {
            return banner_id;
        }

        public void setBanner_id(int banner_id) {
            this.banner_id = banner_id;
        }

        public int getBanner_type() {
            return banner_type;
        }

        public void setBanner_type(int banner_type) {
            this.banner_type = banner_type;
        }

        public int getB_width() {
            return b_width;
        }

        public void setB_width(int b_width) {
            this.b_width = b_width;
        }

        public int getB_height() {
            return b_height;
        }

        public void setB_height(int b_height) {
            this.b_height = b_height;
        }

        public String getAds_contents() {
            return ads_contents == null ? "" : ads_contents;
        }

        public void setAds_contents(String ads_contents) {
            this.ads_contents = ads_contents;
        }

        public String getChecksum() {
            return checksum;
        }

        public void setChecksum(String checksum) {
            this.checksum = checksum;
        }

        public String getTime() {
            return time == null ? "" : time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
