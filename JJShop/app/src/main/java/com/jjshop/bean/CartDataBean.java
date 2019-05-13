package com.jjshop.bean;

import com.jjshop.utils.StringUtil;

/**
 * 作者：张国庆
 * 时间：2018/8/3
 */

public class CartDataBean {

    private String name;
    private String custom_code;
    private long cart_id;
    private int id;
    private int shipping;
    private int num;
    private String[] imgs;
    private double weight;
    private double purchase_price;
    private double promotion_price;
    private double total_price;
    private int supplier_id;
    private int commission_type;
    private double commission_value;
    private double commission_amount;
    private String idcode;
    private int selected;
    private ColorData color;
    private PromotionsData promotions;
    private boolean not_distribution;// 是否可配送
    private int templateType;// 自定义字段，非服务端下发

    public boolean isNot_distribution() {
        return not_distribution;
    }

    public void setNot_distribution(boolean not_distribution) {
        this.not_distribution = not_distribution;
    }

    public String getIdcode() {
        return idcode == null ? "" : idcode;
    }

    public void setIdcode(String idcode) {
        this.idcode = idcode;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getTemplateType() {
        return templateType;
    }

    public void setTemplateType(int templateType) {
        this.templateType = templateType;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustom_code() {
        return custom_code == null ? "" : custom_code;
    }

    public void setCustom_code(String custom_code) {
        this.custom_code = custom_code;
    }

    public long getCart_id() {
        return cart_id;
    }

    public void setCart_id(long cart_id) {
        this.cart_id = cart_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShipping() {
        return shipping;
    }

    public void setShipping(int shipping) {
        this.shipping = shipping;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String[] getImgs() {
        return imgs;
    }

    public void setImgs(String[] imgs) {
        this.imgs = imgs;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getPurchase_price() {
        return StringUtil.getPrice(purchase_price);
    }

    public void setPurchase_price(double purchase_price) {
        this.purchase_price = purchase_price;
    }

    public double getPromotion_price() {
        return StringUtil.getPrice(promotion_price);
    }

    public void setPromotion_price(double promotion_price) {
        this.promotion_price = promotion_price;
    }

    public double getTotal_price() {
        return StringUtil.getPrice(total_price);
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
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

    public double getCommission_amount() {
        return commission_amount;
    }

    public void setCommission_amount(double commission_amount) {
        this.commission_amount = commission_amount;
    }

    public ColorData getColor() {
        return color;
    }

    public void setColor(ColorData color) {
        this.color = color;
    }

    public class ColorData{
        private int id;
        private int color_id;
        private String name;
        private int status;
        private SizeData size;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getColor_id() {
            return color_id;
        }

        public void setColor_id(int color_id) {
            this.color_id = color_id;
        }

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public SizeData getSize() {
            return size;
        }

        public void setSize(SizeData size) {
            this.size = size;
        }

        public class SizeData{
            private int id;
            private String package_efficiency;
            private String name;
            private double org_price;
            private double promotion_price;
            private double price;
            private int status;
            private int purchases_limit_num;
            private String purchases_limit_type;
            private int stock;
            private String sku;

            public String getSku() {
                return sku == null ? "" : sku;
            }

            public void setSku(String sku) {
                this.sku = sku;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPackage_efficiency() {
                return package_efficiency == null ? "" : package_efficiency;
            }

            public void setPackage_efficiency(String package_efficiency) {
                this.package_efficiency = package_efficiency;
            }

            public String getName() {
                return name == null ? "" : name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public double getOrg_price() {
                return StringUtil.getPrice(org_price);
            }

            public void setOrg_price(double org_price) {
                this.org_price = org_price;
            }

            public double getPromotion_price() {
                return StringUtil.getPrice(promotion_price);
            }

            public void setPromotion_price(double promotion_price) {
                this.promotion_price = promotion_price;
            }

            public double getPrice() {
                return StringUtil.getPrice(price);
            }

            public void setPrice(double price) {
                this.price = price;
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
                return purchases_limit_type == null ? "" : purchases_limit_type;
            }

            public void setPurchases_limit_type(String purchases_limit_type) {
                this.purchases_limit_type = purchases_limit_type;
            }

            public int getStock() {
                return stock;
            }

            public void setStock(int stock) {
                this.stock = stock;
            }
        }
    }

    public PromotionsData getPromotions() {
        return promotions;
    }

    public void setPromotions(PromotionsData promotions) {
        this.promotions = promotions;
    }

    public class PromotionsData{
        private int promotion_id;
        private String promotion_name;
        private int promotion_type;
        private double promotion_price;
        private int item_id;
        private String etime;

        public int getPromotion_id() {
            return promotion_id;
        }

        public void setPromotion_id(int promotion_id) {
            this.promotion_id = promotion_id;
        }

        public String getPromotion_name() {
            return promotion_name == null ? "" : promotion_name;
        }

        public void setPromotion_name(String promotion_name) {
            this.promotion_name = promotion_name;
        }

        public int getPromotion_type() {
            return promotion_type;
        }

        public void setPromotion_type(int promotion_type) {
            this.promotion_type = promotion_type;
        }

        public double getPromotion_price() {
            return promotion_price;
        }

        public void setPromotion_price(double promotion_price) {
            this.promotion_price = promotion_price;
        }

        public int getItem_id() {
            return item_id;
        }

        public void setItem_id(int item_id) {
            this.item_id = item_id;
        }

        public String getEtime() {
            return etime == null ? "" : etime;
        }

        public void setEtime(String etime) {
            this.etime = etime;
        }
    }

}
