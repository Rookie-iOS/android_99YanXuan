package com.jjshop.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.jjshop.bean.LoginBean;
import com.jjshop.bean.PersonCenterBean;

/**
 * Created by Colin on 15/12/15.
 */
public class PreUtils {

    private static final String PREF_NAME = "config";// 名称

    public static final String USER_ID_MINGWEN = "user_id_mingwen";// 用户id
    public static final String USER_IMG = "user_img";// 头像
    public static final String USER_NICKNAME = "user_nickname";// 昵称
    public static final String USER_NAME = "user_name";// 真实姓名
    public static final String USER_MOBILE = "user_mobile_mingwen";// 手机号
    public static final String USER_CITY = "user_city";// 城市
    public static final String USER_SEX = "user_sex";// 性别
    public static final String USER_BIRTHDAY = "user_birthday";// 生日
    public static final String USER_AUTH = "user_auth";
    public static final String USER_COOKIE_ID = "user_id";// 用户cookie ID
    public static final String USER_COOKIE_MOBILE = "user_mobile";// 用户cookie手机号
    public static final String USER_ID_CODE = "user_id_code";// 用户cookie手机号

    public static final String SHOP_ID = "shop_id";
    public static final String SHOP_NAME = "shop_name";
    public static final String SHOP_INFO = "shop_info";
    public static final String SHOP_LOGO = "shop_logo";
    public static final String SHOP_IMG = "shop_img";

    public static final String UAUTH = "_uauth";
    public static final String DEVICE = "device";
    public static final String LATITUDE = "latitude";
    public static final String LONTITUDE = "lontitude";

    // 是否显示升级弹框
    public static final String IS_SHOW_DOWNLOAD = "is_show_download";
    // 是否需要更新城市数据
    public static final String IS_UPDATE_CITY_DATA = "is_update_city_data";
    // 搜索历史数据
    public static final String SEARCH_HISTORY = "search_history";


    public static boolean getBoolean(Context ctx, String key, boolean defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static void setBoolean(Context ctx, String key, boolean value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static String getString(Context ctx, String key) {
        return getString(ctx, key, "");
    }
    public static String getString(Context ctx, String key, String defaultValue) {
        if(null == ctx || StringUtil.isEmpty(key)){
            return "";
        }
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static void setString(Context ctx, String key, String value) {
        if (ctx == null){
            return;
        }
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static float getFloat(Context ctx, String key, float defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getFloat(key, defaultValue);
    }

    public static void setFloat(Context ctx, String key, float value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putFloat(key, value).commit();
    }

    public static final String DEFAULT_SHOP = "-101";
    /**
     * 保存店铺信息
     */
    public static void saveShop(Context ctx, String shopid, String shopname, String shopinfo, String shoplogo) {
        if(null == ctx){
            return;
        }
        SharedPreferences preferences = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if(StringUtil.isEmpty(shopid) || !shopid.equals(DEFAULT_SHOP)){
            editor.putString(SHOP_ID, shopid);
        }
        if(StringUtil.isEmpty(shopname) || !shopname.equals(DEFAULT_SHOP)){
            editor.putString(SHOP_NAME, shopname);
        }
        if(StringUtil.isEmpty(shopinfo) || !shopinfo.equals(DEFAULT_SHOP)){
            editor.putString(SHOP_INFO, shopinfo);
        }
        if(StringUtil.isEmpty(shoplogo) || !shoplogo.equals(DEFAULT_SHOP)){
            editor.putString(SHOP_LOGO, shoplogo);
        }
        editor.commit();
    }

    /**
     * 保存用户信息
     */
    public static void saveUser(Context ctx, PersonCenterBean.PersonCenterData.PersonInfoBean infoBean) {
        if(null == ctx || null == infoBean){
            return;
        }
        SharedPreferences preferences = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_ID_MINGWEN, String.valueOf(infoBean.getUid()));
        editor.putString(USER_IMG, infoBean.getImg());
        editor.putString(USER_NICKNAME, infoBean.getName());
        editor.putString(USER_NAME, infoBean.getBank_real_name());
        editor.putString(USER_MOBILE, infoBean.getMobile());
        editor.putString(USER_CITY, infoBean.getAreas_name());
        editor.putString(USER_SEX, infoBean.getSex_name());
        editor.putString(USER_BIRTHDAY, infoBean.getBirthday());
        editor.commit();
    }
    /**
     * 保存登录信息
     */
    public static void saveLoginInfo(Context ctx, String id, String mobile, LoginBean.DataBean bean) {
        if(null == ctx || null == bean) {
            return;
        }


        SharedPreferences preferences = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // 清空就得信息，保存新的信息
        editor.clear(); // 新增的一行代码
        editor.putString(USER_COOKIE_ID, id);
        editor.putString(USER_COOKIE_MOBILE, mobile);
        editor.putString(USER_ID_MINGWEN, bean.get_uid());
        editor.putString(USER_ID_CODE, bean.get_uid_code());
        editor.putString(USER_NICKNAME, bean.get_u_name());
        editor.putString(SHOP_ID, bean.get_shop_id());
        editor.commit();
    }
    /**
     * 保存经纬度信息
     */
    public static void saveLatLont(Context ctx, String lat, String lont) {
        if(null == ctx) {
            return;
        }
        SharedPreferences preferences = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LATITUDE, lat);
        editor.putString(LONTITUDE, lont);
        editor.commit();
    }

    /**
     * 清除所有数据
     */
    public static void clear(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

}
