package com.jjshop.bean;

import com.jjshop.utils.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DetailBean extends BaseBean{


    /**
     * code : 0
     * msg :
     * data : {"data":{"name":"【时尚优雅·棉麻透气】时尚宽松棉麻印花中长款上衣七分裤套装【GZYY】","adword":"时尚棉麻透气舒适","imgs":["https://pimgds.999d.com/uploads/product/5cee4f8c31e9255f8ffe35490937e4c3.jpg?imageView2/1/w/500/h/500/q/75|imageslim"],"videoimg":"","videouri":null,"product_status":"1","info":"<p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700157170155.jpg\" style=\"\" title=\"1530700157170155.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700158602842.jpg\" style=\"\" title=\"1530700158602842.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700160776188.jpg\" style=\"\" title=\"1530700160776188.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700161931539.jpg\" style=\"\" title=\"1530700161931539.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700163255178.jpg\" style=\"\" title=\"1530700163255178.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700165976553.jpg\" style=\"\" title=\"1530700165976553.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700166679162.jpg\" style=\"\" title=\"1530700166679162.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700168279599.jpg\" style=\"\" title=\"1530700168279599.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700170199530.jpg\" style=\"\" title=\"1530700170199530.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700171552845.jpg\" style=\"\" title=\"1530700171552845.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700173240311.jpg\" style=\"\" title=\"1530700173240311.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700175868707.jpg\" style=\"\" title=\"1530700175868707.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700178190727.jpg\" style=\"\" title=\"1530700178190727.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700181931375.jpg\" style=\"\" title=\"1530700181931375.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700184106477.jpg\" style=\"\" title=\"1530700184106477.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700187953898.jpg\" style=\"\" title=\"1530700187953898.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700189564798.jpg\" style=\"\" title=\"1530700189564798.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700192741557.jpg\" style=\"\" title=\"1530700192741557.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700195328137.jpg\" style=\"\" title=\"1530700195328137.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700197297195.jpg\" style=\"\" title=\"1530700197297195.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700202237071.jpg\" style=\"\" title=\"1530700202237071.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700205973746.jpg\" style=\"\" title=\"1530700205973746.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700208378985.jpg\" style=\"\" title=\"1530700208378985.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700211543790.jpg\" style=\"\" title=\"1530700211543790.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700215451573.jpg\" style=\"\" title=\"1530700215451573.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700217665395.jpg\" style=\"\" title=\"1530700217665395.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700219586672.jpg\" style=\"\" title=\"1530700219586672.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700221130900.jpg\" style=\"\" title=\"1530700221130900.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700226264806.jpg\" style=\"\" title=\"1530700226264806.jpg\"/><\/p><p><br/><\/p>","price":"110.00","org_price":"110.00","price_tag":"213.00","earn_price":11,"valid_special_activity":{"id":"88","special_name":"99严选每日新品推荐","type":"1","rule":"","special_status":"1","start_time":"2018-07-05 12:00:00","end_time":"2018-07-09 12:00:00","header_img":"/message/161cd359460a429b78f00ddd4d558a5d.jpg","footer_img":"","click_num":null,"is_del":"2","deleted_at":"2018-07-05 10:12:22","updated_at":"2018-07-05 11:06:22","created_at":"2018-07-05 10:12:25","note":"99严选每日多款新品推出，种类丰富，价格实惠，抓紧抢购！"},"link":"https://yxds.999d.com/m/product/index?id=538kxg&shop=538geb"},"likeproduct":[{"id":"537g79","name":"【舒适面料·吸汗透气】舒适冰丝亚麻大码刺绣短袖七分裤睡衣套装【HHFS】","price":"32.90","org_price":"32.90","price_tag":"32.90","img":"https://pimgds.999d.com/uploads/product/f06bb6e3d713898d1229932b721db933.jpg?imageView2/1/w/250/h/250/q/75|imageslim"},{"id":"537uaf","name":"【多三针】中袖两件套演出舞台比赛服【DSZ】","price":"59.00","org_price":"59.00","price_tag":"59.00","img":"https://pimgds.999d.com/uploads/product/29e6095d1f5ceeb54982a6bed92b4d95.jpg?imageView2/1/w/250/h/250/q/75|imageslim"}],"bestproduct":[{"id":"537gbo","name":"【手感柔滑·悬垂度高】圆领修身短袖莫代尔面料拼接短袖单件短袖（下单后5天内出库）【DSFS】","price":"29.90","org_price":"29.90","price_tag":"29.90","img":"https://pimgds.999d.com/uploads/product/08f75a5e5b1c8d3c1064f900b09254f3.jpg?imageView2/1/w/250/h/250/q/75|imageslim"},{"id":"537uv5","name":"花枝招展款牛筋底民族风广场舞平底绣花鞋【QDY】","price":"42.00","org_price":"42.00","price_tag":"42.00","img":"https://pimgds.999d.com/uploads/product/230046012dd1d3fa2341cab0db9b0d51.jpg?imageView2/1/w/250/h/250/q/75|imageslim"},{"id":"537uw1","name":"【洛神】仿汉服古典金丝绒套装【LS】","price":"55.00","org_price":"55.00","price_tag":"55.00","img":"https://pimgds.999d.com/uploads/product/a0922592307da9305c2608c719d97fce.jpg?imageView2/1/w/250/h/250/q/75|imageslim"},{"id":"537uxr","name":"【洛神】仿汉服古典金丝绒套装【LS】","price":"55.00","org_price":"55.00","price_tag":"55.00","img":"https://pimgds.999d.com/uploads/product/0169b38b11463bfbf91cf11580e78a7c.jpg?imageView2/1/w/250/h/250/q/75|imageslim"}]}
     */

    private DataBeanX data;

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX implements Serializable{
        /**
         * data : {"name":"【时尚优雅·棉麻透气】时尚宽松棉麻印花中长款上衣七分裤套装【GZYY】","adword":"时尚棉麻透气舒适","imgs":["https://pimgds.999d.com/uploads/product/5cee4f8c31e9255f8ffe35490937e4c3.jpg?imageView2/1/w/500/h/500/q/75|imageslim"],"videoimg":"","videouri":null,"product_status":"1","info":"<p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700157170155.jpg\" style=\"\" title=\"1530700157170155.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700158602842.jpg\" style=\"\" title=\"1530700158602842.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700160776188.jpg\" style=\"\" title=\"1530700160776188.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700161931539.jpg\" style=\"\" title=\"1530700161931539.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700163255178.jpg\" style=\"\" title=\"1530700163255178.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700165976553.jpg\" style=\"\" title=\"1530700165976553.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700166679162.jpg\" style=\"\" title=\"1530700166679162.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700168279599.jpg\" style=\"\" title=\"1530700168279599.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700170199530.jpg\" style=\"\" title=\"1530700170199530.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700171552845.jpg\" style=\"\" title=\"1530700171552845.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700173240311.jpg\" style=\"\" title=\"1530700173240311.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700175868707.jpg\" style=\"\" title=\"1530700175868707.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700178190727.jpg\" style=\"\" title=\"1530700178190727.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700181931375.jpg\" style=\"\" title=\"1530700181931375.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700184106477.jpg\" style=\"\" title=\"1530700184106477.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700187953898.jpg\" style=\"\" title=\"1530700187953898.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700189564798.jpg\" style=\"\" title=\"1530700189564798.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700192741557.jpg\" style=\"\" title=\"1530700192741557.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700195328137.jpg\" style=\"\" title=\"1530700195328137.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700197297195.jpg\" style=\"\" title=\"1530700197297195.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700202237071.jpg\" style=\"\" title=\"1530700202237071.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700205973746.jpg\" style=\"\" title=\"1530700205973746.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700208378985.jpg\" style=\"\" title=\"1530700208378985.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700211543790.jpg\" style=\"\" title=\"1530700211543790.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700215451573.jpg\" style=\"\" title=\"1530700215451573.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700217665395.jpg\" style=\"\" title=\"1530700217665395.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700219586672.jpg\" style=\"\" title=\"1530700219586672.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700221130900.jpg\" style=\"\" title=\"1530700221130900.jpg\"/><\/p><p><img src=\"https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700226264806.jpg\" style=\"\" title=\"1530700226264806.jpg\"/><\/p><p><br/><\/p>","price":"110.00","org_price":"110.00","price_tag":"213.00","earn_price":11,"valid_special_activity":{"id":"88","special_name":"99严选每日新品推荐","type":"1","rule":"","special_status":"1","start_time":"2018-07-05 12:00:00","end_time":"2018-07-09 12:00:00","header_img":"/message/161cd359460a429b78f00ddd4d558a5d.jpg","footer_img":"","click_num":null,"is_del":"2","deleted_at":"2018-07-05 10:12:22","updated_at":"2018-07-05 11:06:22","created_at":"2018-07-05 10:12:25","note":"99严选每日多款新品推出，种类丰富，价格实惠，抓紧抢购！"},"link":"https://yxds.999d.com/m/product/index?id=538kxg&shop=538geb"}
         * likeproduct : [{"id":"537g79","name":"【舒适面料·吸汗透气】舒适冰丝亚麻大码刺绣短袖七分裤睡衣套装【HHFS】","price":"32.90","org_price":"32.90","price_tag":"32.90","img":"https://pimgds.999d.com/uploads/product/f06bb6e3d713898d1229932b721db933.jpg?imageView2/1/w/250/h/250/q/75|imageslim"},{"id":"537uaf","name":"【多三针】中袖两件套演出舞台比赛服【DSZ】","price":"59.00","org_price":"59.00","price_tag":"59.00","img":"https://pimgds.999d.com/uploads/product/29e6095d1f5ceeb54982a6bed92b4d95.jpg?imageView2/1/w/250/h/250/q/75|imageslim"}]
         * bestproduct : [{"id":"537gbo","name":"【手感柔滑·悬垂度高】圆领修身短袖莫代尔面料拼接短袖单件短袖（下单后5天内出库）【DSFS】","price":"29.90","org_price":"29.90","price_tag":"29.90","img":"https://pimgds.999d.com/uploads/product/08f75a5e5b1c8d3c1064f900b09254f3.jpg?imageView2/1/w/250/h/250/q/75|imageslim"},{"id":"537uv5","name":"花枝招展款牛筋底民族风广场舞平底绣花鞋【QDY】","price":"42.00","org_price":"42.00","price_tag":"42.00","img":"https://pimgds.999d.com/uploads/product/230046012dd1d3fa2341cab0db9b0d51.jpg?imageView2/1/w/250/h/250/q/75|imageslim"},{"id":"537uw1","name":"【洛神】仿汉服古典金丝绒套装【LS】","price":"55.00","org_price":"55.00","price_tag":"55.00","img":"https://pimgds.999d.com/uploads/product/a0922592307da9305c2608c719d97fce.jpg?imageView2/1/w/250/h/250/q/75|imageslim"},{"id":"537uxr","name":"【洛神】仿汉服古典金丝绒套装【LS】","price":"55.00","org_price":"55.00","price_tag":"55.00","img":"https://pimgds.999d.com/uploads/product/0169b38b11463bfbf91cf11580e78a7c.jpg?imageView2/1/w/250/h/250/q/75|imageslim"}]
         */

        private DataBean data;
        private List<ProductListBean> likeproduct;
        private List<ProductListBean> bestproduct;
        private int cartProfuctNum;
        private int user_id;
        private String user_idcode;
        private String user_name;

        public String getUser_idcode() {
            return user_idcode == null ? "" : user_idcode;
        }

        public void setUser_idcode(String user_idcode) {
            this.user_idcode = user_idcode;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name == null ? "" : user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public int getCartProfuctNum() {
            return cartProfuctNum;
        }

        public void setCartProfuctNum(int cartProfuctNum) {
            this.cartProfuctNum = cartProfuctNum;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public List<ProductListBean> getLikeproduct() {
            return likeproduct;
        }

        public void setLikeproduct(List<ProductListBean> likeproduct) {
            this.likeproduct = likeproduct;
        }

        public List<ProductListBean> getBestproduct() {
            return bestproduct;
        }

        public void setBestproduct(List<ProductListBean> bestproduct) {
            this.bestproduct = bestproduct;
        }

        public static class DataBean implements Serializable{
            /**
             * name : 【时尚优雅·棉麻透气】时尚宽松棉麻印花中长款上衣七分裤套装【GZYY】
             * adword : 时尚棉麻透气舒适
             * imgs : ["https://pimgds.999d.com/uploads/product/5cee4f8c31e9255f8ffe35490937e4c3.jpg?imageView2/1/w/500/h/500/q/75|imageslim"]
             * videoimg :
             * videouri : null
             * product_status : 1
             * info : <p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700157170155.jpg" style="" title="1530700157170155.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700158602842.jpg" style="" title="1530700158602842.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700160776188.jpg" style="" title="1530700160776188.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700161931539.jpg" style="" title="1530700161931539.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700163255178.jpg" style="" title="1530700163255178.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700165976553.jpg" style="" title="1530700165976553.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700166679162.jpg" style="" title="1530700166679162.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700168279599.jpg" style="" title="1530700168279599.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700170199530.jpg" style="" title="1530700170199530.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700171552845.jpg" style="" title="1530700171552845.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700173240311.jpg" style="" title="1530700173240311.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700175868707.jpg" style="" title="1530700175868707.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700178190727.jpg" style="" title="1530700178190727.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700181931375.jpg" style="" title="1530700181931375.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700184106477.jpg" style="" title="1530700184106477.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700187953898.jpg" style="" title="1530700187953898.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700189564798.jpg" style="" title="1530700189564798.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700192741557.jpg" style="" title="1530700192741557.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700195328137.jpg" style="" title="1530700195328137.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700197297195.jpg" style="" title="1530700197297195.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700202237071.jpg" style="" title="1530700202237071.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700205973746.jpg" style="" title="1530700205973746.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700208378985.jpg" style="" title="1530700208378985.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700211543790.jpg" style="" title="1530700211543790.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700215451573.jpg" style="" title="1530700215451573.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700217665395.jpg" style="" title="1530700217665395.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700219586672.jpg" style="" title="1530700219586672.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700221130900.jpg" style="" title="1530700221130900.jpg"/></p><p><img src="https://editimgds.999d.com//uploads/ueditor/php/upload/image/20180704/1530700226264806.jpg" style="" title="1530700226264806.jpg"/></p><p><br/></p>
             * price : 110.00
             * org_price : 110.00
             * price_tag : 213.00
             * earn_price : 11
             * valid_special_activity : {"id":"88","special_name":"99严选每日新品推荐","type":"1","rule":"","special_status":"1","start_time":"2018-07-05 12:00:00","end_time":"2018-07-09 12:00:00","header_img":"/message/161cd359460a429b78f00ddd4d558a5d.jpg","footer_img":"","click_num":null,"is_del":"2","deleted_at":"2018-07-05 10:12:22","updated_at":"2018-07-05 11:06:22","created_at":"2018-07-05 10:12:25","note":"99严选每日多款新品推出，种类丰富，价格实惠，抓紧抢购！"}
             * link : https://yxds.999d.com/m/product/index?id=538kxg&shop=538geb
             */

            private String name;
            private String adword;
            private String videoimg;
            private String videouri;
            private int product_status;
            private int stocks;
            private String info;
            private double price;
            private double org_price;
            private double price_tag;
            private double earn_price;
            private ValidSpecialActivityBean valid_special_activity;
            private String link;
            private ArrayList<String> imgs;
            private ArrayList<SpecData> spec;
            private LimitBuyData limit_buy;
            private PreviewData preview;

            public PreviewData getPreview() {
                return preview;
            }

            public void setPreview(PreviewData preview) {
                this.preview = preview;
            }

            public LimitBuyData getLimit_buy() {
                return limit_buy;
            }

            public void setLimit_buy(LimitBuyData limit_buy) {
                this.limit_buy = limit_buy;
            }

            public LimitBuyData getLimitbuy() {
                return limit_buy;
            }

            public void setLimitbuy(LimitBuyData limit_buy) {
                this.limit_buy = limit_buy;
            }

            public int getStocks() {
                return stocks;
            }

            public void setStocks(int stocks) {
                this.stocks = stocks;
            }

            public ArrayList<SpecData> getSpec() {
                return spec;
            }

            public void setSpec(ArrayList<SpecData> spec) {
                this.spec = spec;
            }

            public String getName() {
                return name == null ? "" : name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAdword() {
                return adword == null ? "" : adword;
            }

            public void setAdword(String adword) {
                this.adword = adword;
            }

            public String getVideoimg() {
                return videoimg;
            }

            public void setVideoimg(String videoimg) {
                this.videoimg = videoimg;
            }

            public String getVideouri() {
                return videouri;
            }

            public void setVideouri(String videouri) {
                this.videouri = videouri;
            }

            public int getProduct_status() {
                return product_status;
            }

            public void setProduct_status(int product_status) {
                this.product_status = product_status;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }

            public double getPrice() {
                return StringUtil.getPrice(price);
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public double getOrg_price() {
                return org_price;
            }

            public void setOrg_price(double org_price) {
                this.org_price = org_price;
            }

            public double getPrice_tag() {
                return price_tag;
            }

            public void setPrice_tag(double price_tag) {
                this.price_tag = price_tag;
            }

            public double getEarn_price() {
                return StringUtil.getPrice(earn_price);
            }

            public void setEarn_price(double earn_price) {
                this.earn_price = earn_price;
            }

           public ValidSpecialActivityBean getValid_special_activity() {
               return valid_special_activity;
          }

           public void setValid_special_activity(ValidSpecialActivityBean valid_special_activity) {
               this.valid_special_activity = valid_special_activity;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public ArrayList<String> getImgs() {
                return imgs;
            }

            public void setImgs(ArrayList<String> imgs) {
                this.imgs = imgs;
            }

            public static class ValidSpecialActivityBean {
                /**
                 * id : 88
                 * special_name : 99严选每日新品推荐
                 * type : 1
                 * rule :
                 * special_status : 1
                 * start_time : 2018-07-05 12:00:00
                 * end_time : 2018-07-09 12:00:00
                 * header_img : /message/161cd359460a429b78f00ddd4d558a5d.jpg
                 * footer_img :
                 * click_num : null
                 * is_del : 2
                 * deleted_at : 2018-07-05 10:12:22
                 * updated_at : 2018-07-05 11:06:22
                 * created_at : 2018-07-05 10:12:25
                 * note : 99严选每日多款新品推出，种类丰富，价格实惠，抓紧抢购！
                 */

                private String id;
                private String special_name;
                private String type;
                private String rule;
                private String special_status;
                private String start_time;
                private long end_time;
                private String header_img;
                private String footer_img;
                private Object click_num;
                private String is_del;
                private String deleted_at;
                private String updated_at;
                private String created_at;
                private String note;
                private String id_code;

                public String getId_code() {
                    return id_code == null ? "" : id_code;
                }

                public void setId_code(String id_code) {
                    this.id_code = id_code;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getSpecial_name() {
                    return special_name == null ? "" : special_name;
                }

                public void setSpecial_name(String special_name) {
                    this.special_name = special_name;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getRule() {
                    return rule;
                }

                public void setRule(String rule) {
                    this.rule = rule;
                }

                public String getSpecial_status() {
                    return special_status;
                }

                public void setSpecial_status(String special_status) {
                    this.special_status = special_status;
                }

                public String getStart_time() {
                    return start_time;
                }

                public void setStart_time(String start_time) {
                    this.start_time = start_time;
                }

                public long getEnd_time() {
                    return end_time;
                }

                public void setEnd_time(long end_time) {
                    this.end_time = end_time;
                }

                public String getHeader_img() {
                    return header_img;
                }

                public void setHeader_img(String header_img) {
                    this.header_img = header_img;
                }

                public String getFooter_img() {
                    return footer_img;
                }

                public void setFooter_img(String footer_img) {
                    this.footer_img = footer_img;
                }

                public Object getClick_num() {
                    return click_num;
                }

                public void setClick_num(Object click_num) {
                    this.click_num = click_num;
                }

                public String getIs_del() {
                    return is_del;
                }

                public void setIs_del(String is_del) {
                    this.is_del = is_del;
                }

                public String getDeleted_at() {
                    return deleted_at;
                }

                public void setDeleted_at(String deleted_at) {
                    this.deleted_at = deleted_at;
                }

                public String getUpdated_at() {
                    return updated_at;
                }

                public void setUpdated_at(String updated_at) {
                    this.updated_at = updated_at;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public void setCreated_at(String created_at) {
                    this.created_at = created_at;
                }

                public String getNote() {
                    return note;
                }

                public void setNote(String note) {
                    this.note = note;
                }
            }

            public class SpecData implements Serializable{
                private int item_id;
                private String name;
                private String attr_name;
                private int status;
                private String img;
                private ArrayList<SpecSizeData> list;

                public int getItem_id() {
                    return item_id;
                }

                public void setItem_id(int item_id) {
                    this.item_id = item_id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getAttr_name() {
                    return StringUtil.isEmpty(attr_name) ? "颜色" : attr_name;
                }

                public void setAttr_name(String attr_name) {
                    this.attr_name = attr_name;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public ArrayList<SpecSizeData> getList() {
                    return list;
                }

                public void setList(ArrayList<SpecSizeData> list) {
                    this.list = list;
                }

                public class SpecSizeData implements Serializable{
                    private int sku_id;
                    private String name;
                    private String attr_name;
                    private double price;
                    private double price_tag;
                    private int stocks;
                    private int status;
                    private int purchases_limit_num;
                    private String purchases_limit_type;
                    private int commission_type;
                    private double commission_value;
                    private double org_price;
                    private double earn_price;
                    private String sku_idcode;

                    public String getSku_idcode() {
                        return sku_idcode == null ? "" : sku_idcode;
                    }

                    public void setSku_idcode(String sku_idcode) {
                        this.sku_idcode = sku_idcode;
                    }

                    public String getAttr_name() {
                        return StringUtil.isEmpty(attr_name) ? "尺寸" : attr_name;
                    }

                    public void setAttr_name(String attr_name) {
                        this.attr_name = attr_name;
                    }

                    public int getSku_id() {
                        return sku_id;
                    }

                    public void setSku_id(int sku_id) {
                        this.sku_id = sku_id;
                    }

                    public String getName() {
                        return StringUtil.isEmpty(name) ? "" : name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public double getPrice() {
                        return price;
                    }

                    public void setPrice(double price) {
                        this.price = price;
                    }

                    public double getPrice_tag() {
                        return price_tag;
                    }

                    public void setPrice_tag(double price_tag) {
                        this.price_tag = price_tag;
                    }

                    public int getStocks() {
                        return stocks;
                    }

                    public void setStocks(int stocks) {
                        this.stocks = stocks;
                    }

                    public int getStatus() {
                        return status;
                    }

                    public void setStatus(int status) {
                        this.status = status;
                    }

                    public int getPurchases_limit_num() {
                        return purchases_limit_num;
                    }

                    public void setPurchases_limit_num(int purchases_limit_num) {
                        this.purchases_limit_num = purchases_limit_num;
                    }

                    public String getPurchases_limit_type() {
                        return purchases_limit_type;
                    }

                    public void setPurchases_limit_type(String purchases_limit_type) {
                        this.purchases_limit_type = purchases_limit_type;
                    }

                    public int getCommission_type() {
                        return commission_type;
                    }

                    public void setCommission_type(int commission_type) {
                        this.commission_type = commission_type;
                    }

                    public double getCommission_value() {
                        return commission_value;
                    }

                    public void setCommission_value(double commission_value) {
                        this.commission_value = commission_value;
                    }

                    public double getOrg_price() {
                        return org_price;
                    }

                    public void setOrg_price(double org_price) {
                        this.org_price = org_price;
                    }

                    public double getEarn_price() {
                        return earn_price;
                    }

                    public void setEarn_price(double earn_price) {
                        this.earn_price = earn_price;
                    }
                }
            }
        }
    }

    public class PreviewData implements Serializable{
        private int id;
        private long stime;
        private String special_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getStime() {
            return stime;
        }

        public void setStime(long stime) {
            this.stime = stime;
        }

        public String getSpecial_name() {
            return special_name == null ? "" : special_name;
        }

        public void setSpecial_name(String special_name) {
            this.special_name = special_name;
        }
    }

    public class LimitBuyData implements Serializable{
        private int limity_type;
        private int limit_num;
        private String msg;

        public int getLimity_type() {
            return limity_type;
        }

        public void setLimity_type(int limity_type) {
            this.limity_type = limity_type;
        }

        public int getLimit_num() {
            return limit_num;
        }

        public void setLimit_num(int limit_num) {
            this.limit_num = limit_num;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
