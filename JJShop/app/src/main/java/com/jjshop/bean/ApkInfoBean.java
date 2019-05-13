package com.jjshop.bean;

/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class ApkInfoBean extends BaseBean{
    public DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean{
        private String version;
        private String vid;     // 版本号
        private String path;    // apk下载地址
        private boolean force; // true强制更新   false不强制
        private String creat;   // 创建时间
        private String notice; // 更新内容

        public String getVersion() {
            return version == null ? "" : version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getVid() {
            return vid == null ? "0.0.0" : vid;
        }

        public void setVid(String vid) {
            this.vid = vid;
        }

        public String getPath() {
            return path == null ? "" : path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public boolean getForce() {
            return force;
        }

        public void setForce(boolean force) {
            this.force = force;
        }

        public String getCreat() {
            return creat == null ? "" : creat;
        }

        public void setCreat(String creat) {
            this.creat = creat;
        }

        public String getNotice() {
            return notice == null ? "" : notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }
    }
}
