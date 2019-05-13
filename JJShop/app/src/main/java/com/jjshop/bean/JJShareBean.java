package com.jjshop.bean;

import com.jjshop.utils.StringUtil;

import java.util.List;

public class JJShareBean extends BaseBean{

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * title : 新款挂钩舞蹈桶包（大号）【HTTY】
         * share_imgs : ["https://pimgds.999d
         * .com/uploads/product/8b6e4ffd0b0b5fabfd8f1272e5da5813.jpg?imageView2/1/w/494/h/800/q
         * /75|imageslim","https://pimgds.999d
         * .com/uploads/product/ff966d8e507bf50d4d4bda0b13a58db4.jpg?imageView2/1/w/494/h/800/q
         * /75|imageslim","https://pimgds.999d
         * .com/uploads/product/1adc5eb89d4c88211135ce4833bba4d6.jpg?imageView2/1/w/494/h/800/q
         * /75|imageslim","https://pimgds.999d
         * .com/uploads/qrcode/custom_code/58_537fdt_e05977c6f6921dcf485b4b9d0c721856.png
         * ?imageView2/1/w/494/h/800/q/75|imageslim&1529629708"]
         * intro : 推荐你看看这个~
         * url : https://yxds.999d.com/m/product/index?id=537glj&shop=537fdt
         * qrcodeimg : https://pimgds.999d
         * .com/uploads/qrcode/custom_code/58_537fdt_e05977c6f6921dcf485b4b9d0c721856.png
         * ?imageView2/1/w/494/h/800/q/75|imageslim&1529629708
         * thumb : https://pimgds.999d.com/uploads/product/8b6e4ffd0b0b5fabfd8f1272e5da5813.jpg
         * ?imageView2/1/w/494/h/800/q/75|imageslim&1529629708
         */

        private String title;
        private String intro;
        private String url;
        private String qrcodeimg;
        private String thumb;
        private List<String> share_imgs;
        private String xcx_share_url;

        public String getXcx_share_url() {
            return xcx_share_url == null ? "" : xcx_share_url;
        }

        public void setXcx_share_url(String xcx_share_url) {
            this.xcx_share_url = xcx_share_url;
        }

        public String getTitle() {
            return StringUtil.isEmpty(title) ? "99严选" : title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIntro() {
            return StringUtil.isEmpty(intro) ? "99严选" : intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getUrl() {
            return url == null ? "https://yxds.999d.com" : url;
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

        public String getThumb() {
            return thumb == null ? "" : thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public List<String> getShare_imgs() {
            return share_imgs;
        }

        public void setShare_imgs(List<String> share_imgs) {
            this.share_imgs = share_imgs;
        }
    }
}
