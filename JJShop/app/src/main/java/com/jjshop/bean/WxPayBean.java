package com.jjshop.bean;

/**
 * 作者：张国庆
 * 时间：2018/8/20
 */

public class WxPayBean extends BaseBean{

    private WxPayData data;

    public WxPayData getData() {
        return data;
    }

    public void setData(WxPayData data) {
        this.data = data;
    }

    public class WxPayData{
        private String appid;
        private String partnerid;
        private String prepayid;
        private String noncestr;
        private String timestamp;
        private String packages;
        private String sign;
        private String pay_result_url;

        public String getPay_result_url() {
            return pay_result_url;
        }

        public void setPay_result_url(String pay_result_url) {
            this.pay_result_url = pay_result_url;
        }

        public String getPackages() {
            return packages;
        }

        public void setPackages(String packages) {
            this.packages = packages;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
