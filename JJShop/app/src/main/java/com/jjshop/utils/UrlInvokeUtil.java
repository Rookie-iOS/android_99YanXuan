package com.jjshop.utils;

import android.content.Context;

import com.jjshop.app.JJShopApplication;
import com.jjshop.ui.WebViewActivity;
import com.jjshop.ui.activity.home.DetailActivity;
import com.jjshop.ui.activity.home.HomeActivity;
import com.jjshop.ui.activity.home.HotZhuanquActivity;
import com.jjshop.ui.activity.home.RushBuyActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：张国庆
 * 时间：2018/10/22
 */

public class UrlInvokeUtil {
    private static volatile UrlInvokeUtil urlInvokeUtil;

    private UrlInvokeUtil(){}

    public static UrlInvokeUtil build(){
        if(null == urlInvokeUtil){
            synchronized (UrlInvokeUtil.class){
                if(null == urlInvokeUtil){
                    urlInvokeUtil = new UrlInvokeUtil();
                }
            }
        }
        return urlInvokeUtil;
    }

    // 首页     99yx://999d.com/product/home
    // 商品详情 99yx://999d.com/product/detail?product_id=2432&idcode=538rbv
    // 限时抢购 99yx://999d.com/product/flash_sale
    // 新品推荐 99yx://999d.com/product/new_arrivals?title=新品推荐
    // 人气爆款 99yx://999d.com/product/hot_bursting?title=人气爆款

    // 信息头部
    private static final String YX = "99yx://";
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    // 内容类型
    private static final String CONTENT_ONE = "999d.com/";
    private static final String CONTENT_TWO = "product/";
    // 跳转页面的KEY
    public static final String PUSH_INVOKE_KEY = "push_invoke_key";
    // 跳转详情的类型
    public static final String INVOKE_DETAIL = CONTENT_ONE + CONTENT_TWO + "detail";
    public static final String KEY_ID = "product_id";// 传的参数key
    public static final String KEY_IDCODE = "idcode";// 传的参数key
    // 跳转限时抢购
    public static final String INVOKE_FLASH_SALE = CONTENT_ONE + CONTENT_TWO + "flash_sale";
    // 跳转H5
    public static final String INVOKE_WEB = "custom_h5";
    public static final String KEY_WEB = "url";// 传的参数key
    // 跳转新品推荐、人气爆款
    public static final String INVOKE_NEW_ARRIVALS = CONTENT_ONE + CONTENT_TWO + "new_arrivals";
    public static final String INVOKE_HOT_BURSTING = CONTENT_ONE + CONTENT_TWO + "hot_bursting";
    public static final String KEY_TYPE = "type";// 传的参数key
    public static final String KEY_TITLE = "title";// 传的参数key
    // 首页
    public static final String INVOKE_HOME = "invoke_home";
    // yxfc
    public static final String YXFC = "_fc";

    /**
     * 收到push消息或者一个url进行解析，获取到跳转对应的页面以及参数
     * @param url
     *
     */
    public Map<String, String> pushData(String url){
        if(StringUtil.isEmpty(url)){
            return null;
        }
        JjLog.e("push数据去空格之前 = " + url);
        url = url.replaceAll(" ", "");
        if(StringUtil.isEmpty(url)){
            return null;
        }
        JjLog.e("push数据去空格之后 = " + url);
        // 字符串分割
        final String YU = "&";
        final String DENG = "=";
        Map<String, String> map = new HashMap<>();
        // 通过url来获取即将跳转的页面
        getInvokeType(url, map);
        // 跳转加载h5页面
        if(url.contains(HTTP) || url.contains(HTTPS)){
            map.put(KEY_WEB, url);
            return map;
        }
        // 是否是99严选的自定义的Url
        if(url.contains(YX)){
            if(!url.contains("?")){// 无参数
                return map;
            }
            // 获取跳转页面所需要的参数
            String[] content = url.split("\\?");
            if(null == content || content.length <= 1){
                return null;
            }
            String perams = content[1];
            // 一个参数
            if(!perams.contains(YU)){
                String[] deng = perams.split(DENG);
                if(null == deng || deng.length <= 1){
                    return null;
                }
                map.put(deng[0], deng[1]);
                return map;
            }
            // 多个参数
            String[] yu = perams.split(YU);
            if(null == yu || yu.length <= 0){
                return null;
            }
            for(int i = 0; i < yu.length; i++){
                String[] deng = yu[i].split(DENG);
                if(null != deng && deng.length >= 2){
                    map.put(deng[0], deng[1]);
                }
            }
            return map;
        }
        return map;
    }

    /**
     * 通过Map的key，value执行跳转动作
     * @param context
     * @param map
     */
    public void pushInvoke(Context context, Map<String, String> map){
        if(null == context || null == map || map.size() <= 0 || !map.containsKey(PUSH_INVOKE_KEY)){
            JjToast.getInstance().toast("配置的URL不正确");
            return;
        }
        if(map.containsKey(YXFC)){
            JJShopApplication.yxfc = map.get(YXFC);
        }
        String invokeType = map.get(PUSH_INVOKE_KEY);
        if(StringUtil.isEmpty(invokeType)){
            return;
        }
        if(invokeType.contains(INVOKE_HOME)){// 跳转首页
            HomeActivity.invoke(context, HomeActivity.SELECT_HOME);
            return;
        }
        if(invokeType.contains(INVOKE_WEB)){// 跳转h5
            WebViewActivity.invokePush(context, map);
            return;
        }
        if(invokeType.contains(INVOKE_DETAIL)){// 跳转到商品详情
            DetailActivity.invokePush(context, map);
            return;
        }
        if(invokeType.contains(INVOKE_FLASH_SALE)){// 跳转到限时抢购
            RushBuyActivity.invokePush(context, map);
            return;
        }
        if(invokeType.contains(INVOKE_HOT_BURSTING) || invokeType.contains(INVOKE_NEW_ARRIVALS)){// 跳转到人气、热卖
            HotZhuanquActivity.invokePush(context, map);
            return;
        }
    }

    /**
     * 通过url来获取要跳转的页面
     * @param url
     * @param map
     * @return
     */
    private Map<String, String> getInvokeType(String url, Map<String, String> map){
        if(url.contains(HTTP) || url.contains(HTTPS)) {// 加载webview
            map.put(PUSH_INVOKE_KEY, INVOKE_WEB);
        }else if(url.contains(INVOKE_FLASH_SALE)){// 限时抢购
            map.put(PUSH_INVOKE_KEY, INVOKE_FLASH_SALE);
        }else if(url.contains(INVOKE_NEW_ARRIVALS)){// 新品推荐
            map.put(PUSH_INVOKE_KEY, INVOKE_NEW_ARRIVALS);
            map.put(KEY_TYPE, HotZhuanquActivity.NEW_GOODS);
        }else if(url.contains(INVOKE_HOT_BURSTING)){// 人气爆款
            map.put(PUSH_INVOKE_KEY, INVOKE_HOT_BURSTING);
            map.put(KEY_TYPE, HotZhuanquActivity.HOT_GOODS);
        }else if(url.contains(INVOKE_DETAIL)){// 商品详情
            map.put(PUSH_INVOKE_KEY, INVOKE_DETAIL);
        }
        return map;
    }
}
