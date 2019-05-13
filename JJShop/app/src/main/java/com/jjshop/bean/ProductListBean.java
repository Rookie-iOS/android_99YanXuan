package com.jjshop.bean;

import com.jjshop.template_view.TemplateUtil;
import com.jjshop.utils.StringUtil;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/7/23
 */

public class ProductListBean {

    private int id;
    private long brand_id;
    private int category_id;
    private int supplier_id;
    private String name;
    private int stock_num;
    private String idcode;
    private double price;// 当前的价格
    private double earn_price;// 分享赚的价格
    private double org_price;// 秒杀原价
    private int product_id;
    private int has_stock;
    private String img;
    private int type;// 自定义字段，非服务端下发  1：未开始   2：秒杀中  3：已结束
    private boolean cartIsCheck;// 自定义字段，非服务端下发 true：选中  false：未选中（购物车列表）
    private int templateType;// 自定义字段，非服务端下发
    private ArrayList<ProductListBean> customBlock;// 自定义字段，非服务端下发
    private boolean firstShow;// 自定义字段，非服务端下发,只有第一个显示，其他的不显示

    public boolean isFirstShow() {
        return firstShow;
    }

    public void setFirstShow(boolean firstShow) {
        this.firstShow = firstShow;
    }

    public ArrayList<ProductListBean> getCustomBlock() {
        return customBlock;
    }

    public void setCustomBlock(ArrayList<ProductListBean> customBlock) {
        this.customBlock = customBlock;
    }

    public int getTemplateType() {
        return templateType <= 0 ? TemplateUtil.TEMPLATE_1014 : templateType;
    }

    public void setTemplateType(int templateType) {
        this.templateType = templateType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getOrg_price() {
        return StringUtil.getPrice(org_price);
    }

    public void setOrg_price(double org_price) {
        this.org_price = org_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(long brand_id) {
        this.brand_id = brand_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public int getStock_num() {
        return stock_num;
    }

    public void setStock_num(int stock_num) {
        this.stock_num = stock_num;
    }

    public String getIdcode() {
        return idcode == null ? "" : idcode;
    }

    public void setIdcode(String idcode) {
        this.idcode = idcode;
    }

    public double getPrice() {
        return StringUtil.getPrice(price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getEarn_price() {
        return StringUtil.getPrice(earn_price);
    }

    public void setEarn_price(double earn_price) {
        this.earn_price = earn_price;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getHas_stock() {
        return has_stock;
    }

    public void setHas_stock(int has_stock) {
        this.has_stock = has_stock;
    }

    public String getImg() {
        return img == null ? "" : img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isCartIsCheck() {
        return cartIsCheck;
    }

    public void setCartIsCheck(boolean cartIsCheck) {
        this.cartIsCheck = cartIsCheck;
    }
}
