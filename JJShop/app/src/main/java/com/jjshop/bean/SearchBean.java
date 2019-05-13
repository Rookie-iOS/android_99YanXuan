package com.jjshop.bean;

import java.util.List;

public class SearchBean extends BaseBean{

    /**
     * code : 0
     * msg :
     * data : {"page":1,"words":"鞋","products":{"shop_info":{"code":"538geb","shopname":"99严选-春哥代言","shopinfo":"严谨态度，最优之选！","qrcode_img":"/qrcode/shop/3605.png"},"product_list":[{"id":"5387tl","brand_id":"1000013","category_id":"75","supplier_id":"28","name":"【起点云】2018新款枝头喜鹊坡跟民族风绣花鞋","code":"020102792","price":"44.50","earn_price":"5.00","img":"https://pimgds.999d.com/uploads/product/2645c09ed9bfc8163a73d756c11f47e8.jpg?imageView2/1/w/900/h/420/q/75|imageslim"},{"id":"537ge0","brand_id":"1000003","category_id":"73","supplier_id":"4","name":"【镂空透气·气囊减震】轻料柔性气囊底飞织布透气鬼步舞鞋【YGY】","code":"02010117","price":"79.00","earn_price":"19.60","img":"https://pimgds.999d.com/uploads/product/ad74a1f2b2229381ec64b3221cf3f655.jpg?imageView2/1/w/900/h/420/q/75|imageslim"},{"id":"538ag6","brand_id":"1000013","category_id":"81","supplier_id":"28","name":"丝光缎面蔷薇版民族风牛筋底绣花布鞋【QDY】","code":"0202011104","price":"49.00","earn_price":"5.50","img":"https://pimgds.999d.com/uploads/product/0cd338a952f5086f2c35431f8ca48937.jpg?imageView2/1/w/900/h/420/q/75|imageslim"},{"id":"538kx0","brand_id":"1000043","category_id":"210","supplier_id":"92","name":"【防臭抑菌·柔软按摩】软底舒适防滑透气水晶厚底拖鞋【GZNZ】","code":"020231781","price":30,"earn_price":"3.00","img":"https://pimgds.999d.com/uploads/product/fbef2f96115edb0ead878fdeac1131df.jpg?imageView2/1/w/900/h/420/q/75|imageslim","promotion_price":0,"promotions":{"promotion_id":"94","promotion_name":"99严选\u2014疯狂折扣","promotion_type":"0","promotion_rules":[["99","10"]]}},{"id":"538kj3","brand_id":"1000093","category_id":"19","supplier_id":"97","name":"【红舞鞋】薄棉舞字男女短袖文化衫【WFSM】","code":"0101021747","price":"32.90","earn_price":"3.50","img":"https://pimgds.999d.com/uploads/product/de65bc0abcd928f073376f4585cf5e88.jpg?imageView2/1/w/900/h/420/q/75|imageslim"},{"id":"5386zb","brand_id":"1000065","category_id":"81","supplier_id":"51","name":"圆头休闲漆皮厚底运动懒人鞋【TZJC】","code":"020201689","price":"35.00","earn_price":"3.70","img":"https://pimgds.999d.com/uploads/product/ba120a9201ae210873b360728538284a.jpg?imageView2/1/w/900/h/420/q/75|imageslim"}]}}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * page : 1
         * words : 鞋
         * products : {"shop_info":{"code":"538geb","shopname":"99严选-春哥代言","shopinfo":"严谨态度，最优之选！","qrcode_img":"/qrcode/shop/3605.png"},"product_list":[{"id":"5387tl","brand_id":"1000013","category_id":"75","supplier_id":"28","name":"【起点云】2018新款枝头喜鹊坡跟民族风绣花鞋","code":"020102792","price":"44.50","earn_price":"5.00","img":"https://pimgds.999d.com/uploads/product/2645c09ed9bfc8163a73d756c11f47e8.jpg?imageView2/1/w/900/h/420/q/75|imageslim"},{"id":"537ge0","brand_id":"1000003","category_id":"73","supplier_id":"4","name":"【镂空透气·气囊减震】轻料柔性气囊底飞织布透气鬼步舞鞋【YGY】","code":"02010117","price":"79.00","earn_price":"19.60","img":"https://pimgds.999d.com/uploads/product/ad74a1f2b2229381ec64b3221cf3f655.jpg?imageView2/1/w/900/h/420/q/75|imageslim"},{"id":"538ag6","brand_id":"1000013","category_id":"81","supplier_id":"28","name":"丝光缎面蔷薇版民族风牛筋底绣花布鞋【QDY】","code":"0202011104","price":"49.00","earn_price":"5.50","img":"https://pimgds.999d.com/uploads/product/0cd338a952f5086f2c35431f8ca48937.jpg?imageView2/1/w/900/h/420/q/75|imageslim"},{"id":"538kx0","brand_id":"1000043","category_id":"210","supplier_id":"92","name":"【防臭抑菌·柔软按摩】软底舒适防滑透气水晶厚底拖鞋【GZNZ】","code":"020231781","price":30,"earn_price":"3.00","img":"https://pimgds.999d.com/uploads/product/fbef2f96115edb0ead878fdeac1131df.jpg?imageView2/1/w/900/h/420/q/75|imageslim","promotion_price":0,"promotions":{"promotion_id":"94","promotion_name":"99严选\u2014疯狂折扣","promotion_type":"0","promotion_rules":[["99","10"]]}},{"id":"538kj3","brand_id":"1000093","category_id":"19","supplier_id":"97","name":"【红舞鞋】薄棉舞字男女短袖文化衫【WFSM】","code":"0101021747","price":"32.90","earn_price":"3.50","img":"https://pimgds.999d.com/uploads/product/de65bc0abcd928f073376f4585cf5e88.jpg?imageView2/1/w/900/h/420/q/75|imageslim"},{"id":"5386zb","brand_id":"1000065","category_id":"81","supplier_id":"51","name":"圆头休闲漆皮厚底运动懒人鞋【TZJC】","code":"020201689","price":"35.00","earn_price":"3.70","img":"https://pimgds.999d.com/uploads/product/ba120a9201ae210873b360728538284a.jpg?imageView2/1/w/900/h/420/q/75|imageslim"}]}
         */

        private int page;
        private String words;
        private ProductsBean products;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }

        public ProductsBean getProducts() {
            return products;
        }

        public void setProducts(ProductsBean products) {
            this.products = products;
        }

        public static class ProductsBean {
            /**
             * shop_info : {"code":"538geb","shopname":"99严选-春哥代言","shopinfo":"严谨态度，最优之选！","qrcode_img":"/qrcode/shop/3605.png"}
             * product_list : [{"id":"5387tl","brand_id":"1000013","category_id":"75","supplier_id":"28","name":"【起点云】2018新款枝头喜鹊坡跟民族风绣花鞋","code":"020102792","price":"44.50","earn_price":"5.00","img":"https://pimgds.999d.com/uploads/product/2645c09ed9bfc8163a73d756c11f47e8.jpg?imageView2/1/w/900/h/420/q/75|imageslim"},{"id":"537ge0","brand_id":"1000003","category_id":"73","supplier_id":"4","name":"【镂空透气·气囊减震】轻料柔性气囊底飞织布透气鬼步舞鞋【YGY】","code":"02010117","price":"79.00","earn_price":"19.60","img":"https://pimgds.999d.com/uploads/product/ad74a1f2b2229381ec64b3221cf3f655.jpg?imageView2/1/w/900/h/420/q/75|imageslim"},{"id":"538ag6","brand_id":"1000013","category_id":"81","supplier_id":"28","name":"丝光缎面蔷薇版民族风牛筋底绣花布鞋【QDY】","code":"0202011104","price":"49.00","earn_price":"5.50","img":"https://pimgds.999d.com/uploads/product/0cd338a952f5086f2c35431f8ca48937.jpg?imageView2/1/w/900/h/420/q/75|imageslim"},{"id":"538kx0","brand_id":"1000043","category_id":"210","supplier_id":"92","name":"【防臭抑菌·柔软按摩】软底舒适防滑透气水晶厚底拖鞋【GZNZ】","code":"020231781","price":30,"earn_price":"3.00","img":"https://pimgds.999d.com/uploads/product/fbef2f96115edb0ead878fdeac1131df.jpg?imageView2/1/w/900/h/420/q/75|imageslim","promotion_price":0,"promotions":{"promotion_id":"94","promotion_name":"99严选\u2014疯狂折扣","promotion_type":"0","promotion_rules":[["99","10"]]}},{"id":"538kj3","brand_id":"1000093","category_id":"19","supplier_id":"97","name":"【红舞鞋】薄棉舞字男女短袖文化衫【WFSM】","code":"0101021747","price":"32.90","earn_price":"3.50","img":"https://pimgds.999d.com/uploads/product/de65bc0abcd928f073376f4585cf5e88.jpg?imageView2/1/w/900/h/420/q/75|imageslim"},{"id":"5386zb","brand_id":"1000065","category_id":"81","supplier_id":"51","name":"圆头休闲漆皮厚底运动懒人鞋【TZJC】","code":"020201689","price":"35.00","earn_price":"3.70","img":"https://pimgds.999d.com/uploads/product/ba120a9201ae210873b360728538284a.jpg?imageView2/1/w/900/h/420/q/75|imageslim"}]
             */

            private ShopInfoBean shop_info;
            private List<ProductListBean> product_list;

            public ShopInfoBean getShop_info() {
                return shop_info;
            }

            public void setShop_info(ShopInfoBean shop_info) {
                this.shop_info = shop_info;
            }

            public List<ProductListBean> getProduct_list() {
                return product_list;
            }

            public void setProduct_list(List<ProductListBean> product_list) {
                this.product_list = product_list;
            }

            public static class ShopInfoBean {
                /**
                 * code : 538geb
                 * shopname : 99严选-春哥代言
                 * shopinfo : 严谨态度，最优之选！
                 * qrcode_img : /qrcode/shop/3605.png
                 */

                private String code;
                private String shopname;
                private String shopinfo;
                private String qrcode_img;

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getShopname() {
                    return shopname;
                }

                public void setShopname(String shopname) {
                    this.shopname = shopname;
                }

                public String getShopinfo() {
                    return shopinfo;
                }

                public void setShopinfo(String shopinfo) {
                    this.shopinfo = shopinfo;
                }

                public String getQrcode_img() {
                    return qrcode_img;
                }

                public void setQrcode_img(String qrcode_img) {
                    this.qrcode_img = qrcode_img;
                }
            }
        }
    }
}
