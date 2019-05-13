package com.jjshop.bean;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/7/12
 */

public class RushBuyBean extends BaseBean{
    public RushBuyData data;
    public ShopshareData shopsharedata;

    public RushBuyData getData() {
        return data;
    }

    public void setData(RushBuyData data) {
        this.data = data;
    }

    public ShopshareData getShopsharedata() {
        return shopsharedata;
    }

    public void setShopsharedata(ShopshareData shopsharedata) {
        this.shopsharedata = shopsharedata;
    }

    public class RushBuyData{
        public ArrayList<NavData> nav;
        public InfoData info;
        public ArrayList<ProductListBean> product_list;

        public ArrayList<NavData> getNav() {
            return nav;
        }

        public void setNav(ArrayList<NavData> nav) {
            this.nav = nav;
        }

        public InfoData getInfo() {
            return info;
        }

        public void setInfo(InfoData info) {
            this.info = info;
        }

        public ArrayList<ProductListBean> getProduct_list() {
            return product_list;
        }

        public void setProduct_list(ArrayList<ProductListBean> product_list) {
            this.product_list = product_list;
        }

        public class NavData{
            private String time;
            private String status_str;
            private String id;

            public String getTime() {
                return time == null ? "" : time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getStatus_str() {
                return status_str == null ? "" : status_str;
            }

            public void setStatus_str(String status_str) {
                this.status_str = status_str;
            }

            public String getId() {
                return id == null ? "" : id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
        public class InfoData{
            private long start_time;
            private long end_time;

            public long getStart_time() {
                return start_time;
            }

            public void setStart_time(long start_time) {
                this.start_time = start_time;
            }

            public long getEnd_time() {
                return end_time;
            }

            public void setEnd_time(long end_time) {
                this.end_time = end_time;
            }
        }
    }

    public class ShopshareData{
        private String title;
        private String thumb;
        private String intro;
        private String url;
        private String qrcodeimg;
        private String[] share_imgs;

        public String getTitle() {
            return title == null ? "" : title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getThumb() {
            return thumb == null ? "" : thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getIntro() {
            return intro == null ? "" : intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getUrl() {
            return url == null ? "" : url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getQrcodeimg() {
            return qrcodeimg == null ? "" : qrcodeimg;
        }

        public void setQrcodeimg(String qrcodeimg) {
            this.qrcodeimg = qrcodeimg;
        }

        public String[] getShare_imgs() {
            return share_imgs;
        }

        public void setShare_imgs(String[] share_imgs) {
            this.share_imgs = share_imgs;
        }
    }
}
