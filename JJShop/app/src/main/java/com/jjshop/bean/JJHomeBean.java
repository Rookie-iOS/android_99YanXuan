package com.jjshop.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JJHomeBean extends BaseBean{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private IndexDataBean index_data;
        public ArrayList<RecommendData> recommend;

        public ArrayList<RecommendData> getRecommend() {
            return recommend;
        }

        public void setRecommend(ArrayList<RecommendData> recommend) {
            this.recommend = recommend;
        }

        public class RecommendData{
            private int type;
            private String name;
            private String img;

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getName() {
                return name == null ? "" : name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImg() {
                return img == null ? "" : img;
            }

            public void setImg(String img) {
                this.img = img;
            }
        }

        public IndexDataBean getIndex_data() {
            return index_data;
        }

        public void setIndex_data(IndexDataBean index_data) {
            this.index_data = index_data;
        }

        public static class IndexDataBean {
            private BannerBean banner;
            private List<CarouselBean> carousel;
            private List<ProductListBean> product_list;

            public BannerBean getBanner() {
                return banner;
            }

            public void setBanner(BannerBean banner) {
                this.banner = banner;
            }

            public List<CarouselBean> getCarousel() {
                return carousel;
            }

            public void setCarousel(List<CarouselBean> carousel) {
                this.carousel = carousel;
            }

            public List<ProductListBean> getProduct_list() {
                return product_list;
            }

            public void setProduct_list(List<ProductListBean> product_list) {
                this.product_list = product_list;
            }

            public static class BannerBean {


                private String img_url;
                private String img;

                public String getImg_url() {
                    return img_url;
                }

                public void setImg_url(String img_url) {
                    this.img_url = img_url;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }
            }

            public static class CarouselBean {
                private String img_url;
                private String img;

                public String getImg_url() {
                    return img_url;
                }

                public void setImg_url(String img_url) {
                    this.img_url = img_url;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }
            }
        }
    }
}
