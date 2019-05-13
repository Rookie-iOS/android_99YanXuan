package com.jjshop.utils.httputil;

import com.google.gson.Gson;
import com.jjshop.bean.BaseBean;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者：张国庆
 * 时间：2018/7/27
 * 解析工具类
 */

public class GsonUtil {

    private static volatile GsonUtil gsonUtil;
    private GsonUtil(){}
    public static GsonUtil getGsonUtil(){
        if(null == gsonUtil){
            synchronized (GsonUtil.class){
                if(null == gsonUtil){
                    gsonUtil = new GsonUtil();
                }
            }
        }
        return gsonUtil;
    }

    private final String KEY_CODE = "code";
    private final String KEY_MSG = "msg";

    public static final int CODE_SUCCESS = 0;
    public static final int CODE_STOCK = 200002;

    /**
     * json字符串解析成实体类
     * @param json          json字符串
     * @param classBean     实体bean
     * @param <T>           实体bean
     * @return
     */
    public <T extends BaseBean> T  strJsonToBean(String json, Class<T> classBean){
        JSONObject object;
        T bean;
        try {
            if(StringUtil.isEmpty(json)){
                json = defaultErrorJson();// 默认拼接一个json传
            }
            JjLog.e("返回的数据：" + json);
            if(!json.contains("{") && !json.contains("}")){
                JjLog.e("不是json数据");
                return null;
            }
            object = new JSONObject(json);
            if(null == object){
                return null;
            }
            // 当code为0时
            if(object.has(KEY_CODE) && (object.getInt(KEY_CODE) == CODE_SUCCESS || object.getInt(KEY_CODE) == CODE_STOCK)){
                bean = new Gson().fromJson(json, classBean);
                return bean;
            }
            // 错误时只解析code，msg的值
            JSONObject objectError = new JSONObject();
            if(object.has(KEY_CODE)){
                objectError.put(KEY_CODE, object.getInt(KEY_CODE));
            }
            if(object.has(KEY_MSG)){
                objectError.put(KEY_MSG, object.getString(KEY_MSG));
            }
            JjLog.e("code不为0时：" + objectError.toString());
            bean = new Gson().fromJson(objectError.toString(), classBean);
            return bean;
        } catch (Exception e) {
            JjLog.e("解析异常：" + e.getMessage());
        }
        return null;
    }

    // 返回数据为空或者不是json串时，默认拼接json串
    private String defaultErrorJson(){
        JSONObject object = new JSONObject();
        try {
            object.put(KEY_CODE, -101);
            object.put(KEY_MSG, "没有获取到数据");
        } catch (JSONException e) {
            JjLog.e("默认错误json拼接异常：" + e.getMessage());
        }
        return object.toString();
    }

}
