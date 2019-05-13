package com.jjshop.bean;

import com.contrarywind.interfaces.IPickerViewData;
import com.jjshop.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：张国庆
 * 时间：2018/9/26
 */

public class RefundDetailBean extends BaseBean{
    private RefundDetailData data;

    public RefundDetailData getData() {
        return data;
    }

    public void setData(RefundDetailData data) {
        this.data = data;
    }

    public class RefundDetailData{
        private RefundDetailInfo info;
        private MyOrderProductList item;
        private MyOrderProductList product;
        private List<ExpressBean> express;
        private RefundDetailSupplier supplier;

        public List<ExpressBean> getExpress() {
            return express;
        }

        public void setExpress(List<ExpressBean> express) {
            this.express = express;
        }

        public RefundDetailSupplier getSupplier() {
            return supplier;
        }

        public void setSupplier(RefundDetailSupplier supplier) {
            this.supplier = supplier;
        }

        public RefundDetailInfo getInfo() {
            return info;
        }

        public void setInfo(RefundDetailInfo info) {
            this.info = info;
        }

        public MyOrderProductList getItem() {
            return item;
        }

        public void setItem(MyOrderProductList item) {
            this.item = item;
        }

        public MyOrderProductList getProduct() {
            return product;
        }

        public void setProduct(MyOrderProductList product) {
            this.product = product;
        }
    }

    public class RefundDetailInfo{
        private int id;
        private long order_id;
        private int status;
        private int type;
        private double back_money;
        private String note;
        private String reason;
        private String created_at;
        private String updated_at;
        private int examined_status;
        private long order_no;
        private double real_balance_price;
        private double real_wx_refund_price;
        private double real_back_money;
        private String express_company;
        private String express_company_code;
        private String express_num;

        public String getExpress_company() {
            return express_company == null ? "" : express_company;
        }

        public void setExpress_company(String express_company) {
            this.express_company = express_company;
        }

        public String getExpress_company_code() {
            return express_company_code == null ? "" : express_company_code;
        }

        public void setExpress_company_code(String express_company_code) {
            this.express_company_code = express_company_code;
        }

        public String getExpress_num() {
            return express_num == null ? "" : express_num;
        }

        public void setExpress_num(String express_num) {
            this.express_num = express_num;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getOrder_id() {
            return order_id;
        }

        public void setOrder_id(long order_id) {
            this.order_id = order_id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public double getBack_money() {
            return StringUtil.getPrice(back_money);
        }

        public void setBack_money(double back_money) {
            this.back_money = back_money;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getReason() {
            return reason == null ? "" : reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getExamined_status() {
            return examined_status;
        }

        public void setExamined_status(int examined_status) {
            this.examined_status = examined_status;
        }

        public long getOrder_no() {
            return order_no;
        }

        public void setOrder_no(long order_no) {
            this.order_no = order_no;
        }

        public double getReal_back_money() {
            return StringUtil.getPrice(real_back_money);
        }

        public void setReal_back_money(double real_back_money) {
            this.real_back_money = real_back_money;
        }

        public double getReal_balance_price() {
            return StringUtil.getPrice(real_balance_price);
        }

        public void setReal_balance_price(double real_balance_price) {
            this.real_balance_price = real_balance_price;
        }

        public double getReal_wx_refund_price() {
            return StringUtil.getPrice(real_wx_refund_price);
        }

        public void setReal_wx_refund_price(double real_wx_refund_price) {
            this.real_wx_refund_price = real_wx_refund_price;
        }
    }

    public class RefundDetailSupplier{
        private String return_address;
        private String return_linkman;
        private String return_phone;

        public String getReturn_address() {
            return return_address == null ? "" : return_address;
        }

        public void setReturn_address(String return_address) {
            this.return_address = return_address;
        }

        public String getReturn_linkman() {
            return return_linkman == null ? "" : return_linkman;
        }

        public void setReturn_linkman(String return_linkman) {
            this.return_linkman = return_linkman;
        }

        public String getReturn_phone() {
            return return_phone == null ? "" : return_phone;
        }

        public void setReturn_phone(String return_phone) {
            this.return_phone = return_phone;
        }
    }

}
