package com.jjshop.bean;

import java.util.List;

public class HomeClassifyBean extends BaseBean{
    private ClassifyData data;

    public ClassifyData getData() {
        return data;
    }

    public void setData(ClassifyData data) {
        this.data = data;
    }

    public static class ClassifyData {
        private IndexDataBean index_data;
        private IndexDataNavBean index_data_nav;

        public IndexDataBean getIndex_data() {
            return index_data;
        }

        public void setIndex_data(IndexDataBean index_data) {
            this.index_data = index_data;
        }

        public IndexDataNavBean getIndex_data_nav() {
            return index_data_nav;
        }

        public void setIndex_data_nav(IndexDataNavBean index_data_nav) {
            this.index_data_nav = index_data_nav;
        }
    }

    public class IndexDataBean {
        private ShopInfoBean shop_info;
        private String uid_code;

        public String getUid_code() {
            return uid_code == null ? "" : uid_code;
        }

        public void setUid_code(String uid_code) {
            this.uid_code = uid_code;
        }

        public ShopInfoBean getShop_info() {
            return shop_info;
        }

        public void setShop_info(ShopInfoBean shop_info) {
            this.shop_info = shop_info;
        }

    }
    public static class ShopInfoBean {
        private String id;
        private String shopname;
        private String shopinfo;
        private String shoplogo;
        private String qrcode_img;

        public String getId() {
            return id == null ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShopname() {
            return shopname == null ? "" : shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getShopinfo() {
            return shopinfo == null ? "" : shopinfo;
        }

        public void setShopinfo(String shopinfo) {
            this.shopinfo = shopinfo;
        }

        public String getShoplogo() {
            return shoplogo == null ? "" : shoplogo;
        }

        public void setShoplogo(String shoplogo) {
            this.shoplogo = shoplogo;
        }

        public String getQrcode_img() {
            return qrcode_img == null ? "" : qrcode_img;
        }

        public void setQrcode_img(String qrcode_img) {
            this.qrcode_img = qrcode_img;
        }
    }

    public class IndexDataNavBean {
        private List<CategoryListBean> category_list;

        public List<CategoryListBean> getCategory_list() {
            return category_list;
        }

        public void setCategory_list(List<CategoryListBean> category_list) {
            this.category_list = category_list;
        }
    }

    public static class CategoryListBean {
        private int id;
        private String name;
        private String parent_id;
        private String pic;
        private int vid;
        private String url;

        public String getUrl() {
            return url == null ? "" : url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getVid() {
            return vid;
        }

        public void setVid(int vid) {
            this.vid = vid;
        }

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

        public String getParent_id() {
            return parent_id == null ? "" : parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public String getPic() {
            return pic == null ? "" : pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
