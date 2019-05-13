package com.jjshop.utils.dbutils;

import com.jjshop.bean.DbUtilBean;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.StringUtil;

import org.litepal.LitePal;
import org.litepal.crud.callback.SaveCallback;

import java.util.List;

/**
 * 作者：张国庆
 * 时间：2018/12/13
 */

public class LitepalUtil {

    private static volatile LitepalUtil latePalUtil;

    private LitepalUtil(){}

    public static LitepalUtil build(){
        if(null == latePalUtil){
            synchronized (LitepalUtil.class){
                if(null == latePalUtil){
                    latePalUtil = new LitepalUtil();
                }
            }
        }
        return latePalUtil;
    }

    // 首页标题的key
    public static final String HOME_TITLE_KEY = "home_title_key";

    // 表的字段名
    public static final String TAB_COLUMN_KEY = "key";

    /**
     * 保存数据到数据库
     * @param key       key字段的值
     * @param value     value字段的值
     */
    public void save(String key, String value){
        save(TAB_COLUMN_KEY, key, value);
    }
    public void save(String columnKey, String key, String value){
        if(StringUtil.isEmpty(key) || StringUtil.isEmpty(value)){
            return;
        }
        DbUtilBean bean = LitePal.find(DbUtilBean.class, 1);
        if(null == bean){
            bean = new DbUtilBean();
            bean.setKey(key);
            bean.setValue(value);
        }else{
            List<DbUtilBean> list = LitePal.where(columnKey + " = ?", key).find(DbUtilBean.class);
            if(null != list && list.size() > 0){
                bean = list.get(0);
                bean.setValue(value);
            }else{
                bean = new DbUtilBean();
                bean.setKey(key);
                bean.setValue(value);
            }
        }
        bean.saveAsync().listen(new SaveCallback() {
            @Override
            public void onFinish(boolean success) {
                JjLog.e("保存成功 = " + success);
            }
        });

    }

    /**
     * 条件查询
     * @param key   条件字段
     * @param value 条件字段的值
     * @return      返回的值（json）
     */
    public String find(String key, String value){
        DbUtilBean bean = LitePal.find(DbUtilBean.class, 1);
        if(null == bean){
            return "";
        }
        List<DbUtilBean> list = LitePal.where(key + " = ?", value).find(DbUtilBean.class);
        if(null != list && list.size() > 0){
            bean = list.get(0);
            if(null == bean){
                return "";
            }
            return bean.getValue();
        }
        return "";
    }

    /**
     * 删除数据
     * @param aClass
     * @param key   条件字段
     * @param value 条件字段的值
     */
    public void deleteAll(Class<?> aClass, String key, String value){
        if(null == aClass || StringUtil.isEmpty(key) || StringUtil.isEmpty(value)){
            return;
        }
        LitePal.deleteAll(aClass, key + " = ?" , value);
    }

    /**
     * 删除数据库
     * @param dbName
     */
    public void deleteDatabase(String dbName){
        if(StringUtil.isEmpty(dbName)){
            return;
        }
        LitePal.deleteDatabase(dbName);
    }
}
