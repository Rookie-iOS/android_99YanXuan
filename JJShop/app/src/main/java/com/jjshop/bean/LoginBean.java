package com.jjshop.bean;

/**
 * 作者：张国庆
 * 时间：2018/7/30
 */

public class LoginBean extends BaseBean{
    public DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean{
        private long id;
        private String auth;
        private String _uid;
        private String _uid_code;
        private String _u_name;
        private String _shop_id;

        public String get_uid() {
            return _uid == null ? "" : _uid;
        }

        public void set_uid(String _uid) {
            this._uid = _uid;
        }

        public String get_uid_code() {
            return _uid_code == null ? "" : _uid_code;
        }

        public void set_uid_code(String _uid_code) {
            this._uid_code = _uid_code;
        }

        public String get_u_name() {
            return _u_name == null ? "" : _u_name;
        }

        public void set_u_name(String _u_name) {
            this._u_name = _u_name;
        }

        public String get_shop_id() {
            return _shop_id ==  null ? "" : _shop_id;
        }

        public void set_shop_id(String _shop_id) {
            this._shop_id = _shop_id;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getAuth() {
            return auth ==  null ? "" : auth;
        }

        public void setAuth(String auth) {
            this.auth = auth;
        }
    }
}
