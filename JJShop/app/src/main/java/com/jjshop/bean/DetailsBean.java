package com.jjshop.bean;

import java.util.List;

public class DetailsBean {
    private List<String> piclist;
    private String webstr;
    private List<ProductListBean> likeproductBeans;
    private String goodsname;
    private String price;
    private String earnprice;
    private DetailBean.DataBeanX.DataBean.ValidSpecialActivityBean valid_special_activity;
    private List<ProductListBean> bestproduct;

    public List<ProductListBean> getBestproduct() {
        return bestproduct;
    }

    public void setBestproduct(List<ProductListBean> bestproduct) {
        this.bestproduct = bestproduct;
    }

    public DetailBean.DataBeanX.DataBean.ValidSpecialActivityBean getValid_special_activity() {
        return valid_special_activity;
    }

    public void setValid_special_activity(DetailBean.DataBeanX.DataBean.ValidSpecialActivityBean valid_special_activity) {
        this.valid_special_activity = valid_special_activity;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEarnprice() {
        return earnprice;
    }

    public void setEarnprice(String earnprice) {
        this.earnprice = earnprice;
    }

    public List<ProductListBean> getLikeproductBeans() {
        return likeproductBeans;
    }

    public void setLikeproductBeans(List<ProductListBean> likeproductBeans) {
        this.likeproductBeans = likeproductBeans;
    }

    public List<String> getPiclist() {
        return piclist;
    }

    public void setPiclist(List<String> piclist) {
        this.piclist = piclist;
    }

    public String getWebstr() {
        return webstr;
    }

    public void setWebstr(String webstr) {
        this.webstr = webstr;
    }
}
