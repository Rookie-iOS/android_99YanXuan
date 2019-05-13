package com.jjshop.utils.httputil;

import android.content.Context;

import com.jjshop.BuildConfig;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 接口域名、地址、参数
 */
public class HttpUrl {

    private static volatile HttpUrl contanst;

    private HttpUrl(){}

    public static HttpUrl build(){
        if(null == contanst){
            synchronized (HttpUrl.class){
                if(null == contanst){
                    contanst = new HttpUrl();
                }
            }
        }
        return contanst;
    }

    public static final Boolean isOnline = BuildConfig.IS_RELEASE;

    // 正式域名
    private static final String RELEASE_URL = "https://yxds.999d.com/";
    // 测试域名
    private static final String DEBUG_URL = "http://yxds.i999d.cn:8809/";
//    春哥测试接口
//    private static final String DEBUG_URL = "http://yxds.i999d.cn:8805/";
    // 测试域名-可以测试支付的
//    private static final String DEBUG_URL = "http://testyxds.999d.com/";
    // 根据当前环境来确定使用正式域名、测试域名
    private static final String BASE_URL = isOnline ? RELEASE_URL : DEBUG_URL;

    // 微信 key
    public static String WECHAT_APP_ID = "wxeff2cdc8550933f3";
    public static String WECHAT_APP_SECRET = "c9c206fc86a95eb60186d327ac4011fc";
    // 小程序原始id
    public static String WECHAT_MIN_ID = "gh_be6249c82b1a";
    // 小能
    public static String XN_SITE_ID = "kf_10239";
    public static String XN_SDK_KEY = "ec30a4a1-51e6-47a0-aad0-d3eee562182a";
    public static String XN_SETTING_ID = "kf_10239_1527495124046";
    // 小米推送
    public static String MI_APP_ID = "2882303761517843291";
    public static String MI_APP_KEY = "5241784373291";
    // bugly
    public static String BUGLY_APP_ID = "a216a343e3";

    // 首页
    public static final String JJMAIL_INDEX = BASE_URL + "v1/index/index";
    // 分享
    public static final String URL_SHARE_INFO = BASE_URL + "v1/product/shareInfo";
    // 更新APK
    public static final String URL_UPDATE_APK = BASE_URL + "m/updateapp/index";
    // 搜索商品
    public static final String URL_SEARCH = BASE_URL + "v1/shopproduct/list";
    // 详情
    public static final String URL_DETAILS = BASE_URL + "v1/product/index";
    // 限时秒杀
    public static final String URL_RUSH_BUG = BASE_URL + "v1/promotion/m_shopFlashSaleList?shop=";
    // 新品推荐、人气爆款
    public static final String URL_NEW_TUI_BAO_KUAN = BASE_URL + "v1/specialrecommend/info?";
    // 获取验证码
    public static final String URL_LOGIN_CODE = BASE_URL + "v1/passport/smscode?mobile=";
    // 登录
    public static final String URL_LOGIN = BASE_URL + "v1/passport/loginsms";
    // 我的店
    public static final String URL_MY_SHOP = BASE_URL + "v1/myshop/index";
    // 我的店-分享
    public static final String URL_MY_SHOP_SHARE = BASE_URL + "m/myshop/ajaxshopcode?shop=";
    // 修改店铺信息
    public static final String URL_SHOP_INFO = BASE_URL + "v1/myshop/m_editmyshop?shop=";
    // 购物车
    public static final String URL_SHOPPING_CART = BASE_URL + "v1/cart/m_index?shop=";
    // 购物车-修改数量
    public static final String URL_UPDATE_CART_NUM = BASE_URL + "v1/cart/updateCart";
    // 购物车-删除
    public static final String URL_DEL_CART_GOODS = BASE_URL + "v1/cart/delFromCart";
    // 购物车-选中、非选中
    public static final String URL_SELECT_CART = BASE_URL + "v1/cart/m_selected";
    // 个人中心
    public static final String URL_PERSON_CENTER = BASE_URL + "v1/user/index";
    // 上传头像
    public static final String URL_UPLOAD_HEAD = BASE_URL + "m/upload/imgs";
    // 修改个人中心
    public static final String URL_UPDATE_PERSON_INFO = BASE_URL + "v1/user/m_editmyinfo";
    // 城市列表
    public static final String URL_CITYS_LIST = BASE_URL + "v1/user/getJsonAreas?";
    // 加入购物车
    public static final String URL_ADD_CART = BASE_URL + "v1/cart/m_addProduct";
    // 确认订单
    public static final String URL_SUBMIT_ORDER_INFO = BASE_URL + "v1/order/confirmOrder";
    // 地址列表
    public static final String URL_ADDRESS_LIST = BASE_URL + "v1/address/orderAddressList";
    // 添加地址
    public static final String URL_ADDRESS_ADD = BASE_URL + "v1/address/addAddress";
    // 修改地址
    public static final String URL_ADDRESS_UPDATE = BASE_URL + "v1/address/updateAddress";
    // 删除地址
    public static final String URL_ADDRESS_DEL = BASE_URL + "v1/address/deleteAddress";
    // 默认地址
    public static final String URL_ADDRESS_DEFAULT = BASE_URL + "v1/address/setDefaultAddress";
    // 提交订单
    public static final String URL_SUBMIT_ORDER = BASE_URL + "v1/order/createorder";
    // 立即支付
    public static final String URL_PAY = BASE_URL + "v1/wxpay/pay";
    // 个人中心-我的订单
    public static final String URL_PERSON_MY_ORDER = BASE_URL + "v1/order/getMyOrder";
    // 个人中心-订单详情
    public static final String URL_PERSON_ORDER_DETAIL = BASE_URL + "v1/order/info";
    // 个人中心-取消订单
    public static final String URL_PERSON_CANCEL_ORDER = BASE_URL + "v1/order/status";
    // 个人中心-售后退款
    public static final String URL_PERSON_AFTER_SALE_REFUND = BASE_URL + "v1/order/afterSales";
    // 个人中心-退款详情
    public static final String URL_PERSON_REFUND_DETAIL = BASE_URL + "v1/order/refundInfo";
    // 个人中心-退货详情
    public static final String URL_PERSON_RETUEN_DETAIL = BASE_URL + "v1/order/returnInfo";
    // 个人中心-提交物流
    public static final String URL_PERSON_SUBMIT_WULIU = BASE_URL + "v1/order/returnExpress";
    // 个人中心-退款页
    public static final String URL_PERSON_REFUND = BASE_URL + "v1/order/applyRefundPage";
    // 个人中心-退货页
    public static final String URL_PERSON_RETUEN = BASE_URL + "v1/order/applyReturnPage";
    // 个人中心-提交退款请求
    public static final String URL_PERSON_SUBMIT_REFUND = BASE_URL + "v1/order/applyRefund";
    // 个人中心-提交退货请求
    public static final String URL_PERSON_SUBMIT_RETUEN = BASE_URL + "v1/order/applyReturn";
    // 个人中心-物流信息
    public static final String URL_PERSON_WULIU_INFO = BASE_URL + "v1/order/newexpressInfo";
    // 活动
    public static final String URL_ACTIVITY = BASE_URL + "v1/Specialwindow/info";
    // 广告
    public static final String URL_AD_SHOW = BASE_URL + "v1/ads/index";
    // 商品详情页-日志统计
    public static final String URL_GOODS_DETAILS_LOG = BASE_URL + "m/log/product";
    // 口令
    public static final String URL_KOULING = BASE_URL + "v1/shorturl/short";
    // 其他页-日志统计
    public static final String URL_OTHER_LOG = BASE_URL + "m/log/global";
    // 我的优惠券
    public static final String URL_MY_COUPON = BASE_URL + "v1/User/mycoupon";




    // 加载H5时 注入cookie的域名
    public static final String WEB_COOKIE_KEY = BASE_URL;
    // 正式、测试的域名
    public static final String KOULING_URL = isOnline ? "yxds.999d.com" : "yxds.i999d.cn";
    //H5链接-消息
    public static final String WEB_MSG = BASE_URL + "m/myshopmsg/news?shop=";
    //H5链接-今日订单
    public static final String WEB_TODAY_ORDER = BASE_URL + "m/shoporder/getOrderList?shop=";
    //H5链接-今日销售
    public static final String WEB_TODAY_SALE = BASE_URL + "m/myshopsales/index?time=2&shop=";
    //H5链接-本月销售
    public static final String WEB_MONTH_SALE = BASE_URL + "m/myshopsales/index?time=1&shop=";
    //H5链接-全部订单
    public static final String WEB_ALL_ORDER = WEB_TODAY_ORDER;
    //H5链接-待付款
    public static final String WEB_DFK_ORDER = BASE_URL + "m/shoporder/getOrderList?status=2&shop=";
    //H5链接-待发货
    public static final String WEB_DFH_ORDER = BASE_URL + "m/shoporder/getOrderList?status=3&shop=";
    //H5链接-待收货
    public static final String WEB_DSH_ORDER = BASE_URL + "m/shoporder/getOrderList?status=4&shop=";
    //H5链接-售后、退款
    public static final String WEB_SH_TUIKUAN_ORDER = BASE_URL + "m/shoporder/afterSales?shop=";
    //H5链接-提现
    public static final String WEB_TIXIAN = BASE_URL + "m/withdraw/index?shop=";
    //H5链接-未结算收入
    public static final String WEB_WJS_MONEY = BASE_URL + "m/myshopasset/unsettled?shop=";
    //H5链接-已结算收入
    public static final String WEB_YJS_MONEY = BASE_URL + "m/myshopasset/settled?shop=";
    //H5链接-收支明细
    public static final String WEB_SHOUZHI_MX = BASE_URL + "m/myshopasset/detailed?shop=";
    //H5链接-提现记录
    public static final String WEB_TIXIAN_JILU = BASE_URL + "m/withdraw/record?shop=";
    //H5链接-开店奖励
    public static final String WEB_KD_JIANGLI = BASE_URL + "m/myshopasset/reward?shop=";
    //H5链接-邀请开店
    public static final String WEB_YQ_KD = BASE_URL + "m/myshop/myinvite?shop=";
    //H5链接-我的培训师
    public static final String WEB_MY_PXS = BASE_URL + "m/myshop/mytutor?shop=";
    //H5链接-个人中心-全部订单
    public static final String WEB_PERSON_ALL_ORDER = BASE_URL + "m/order/getmyorder?shop=";
    //H5链接-个人中心-待付款
    public static final String WEB_PERSON_DFK = BASE_URL + "m/order/getmyorder?status=2&shop=";
    //H5链接-个人中心-待发货
    public static final String WEB_PERSON_DFH = BASE_URL + "m/order/getmyorder?status=3&shop=";
    //H5链接-个人中心-待收货
    public static final String WEB_PERSON_DSH = BASE_URL + "m/order/getmyorder?status=4&shop=";
    //H5链接-个人中心-售后、退款
    public static final String WEB_PERSON_SHTK = BASE_URL + "m/order/afterSales?shop=";
    //H5链接-个人中心-我的优惠券
    public static final String WEB_PERSON_DISCOUNT_QUAN = BASE_URL + "m/User/mycoupon?shop=";
    //H5链接-个人中心-地址管理
    public static final String WEB_PERSON_ADDRESS = BASE_URL + "m/address/list?shop=";
    //H5链接-购物车-去结算
    public static final String WEB_CART_BUY = BASE_URL + "m/order/confirmOrder?shop=";
    //H5链接-客服
    public static final String WEB_KEFU = BASE_URL + "m/kefu/index?shop=";
    //H5链接-优惠
    public static final String WEB_DISCOUNT = BASE_URL + "m/promotion/info?shop=";


    public final int PAGE_SIZE = 10;

    // 登录成功返回的cookie
    public static String COOKIE = "cookie";
    public static String User_Agent = "User-Agent";
    public static String YXFC = "yxfc";

    public static String getUserAgent(Context context){
        if(null == context){
            return "";
        }
        return PreUtils.getString(context, PreUtils.DEVICE);
    }

    public static String getCookies(Context context){
        if(null == context){
            return "";
        }
        String cookieId = PreUtils.getString(context, PreUtils.USER_COOKIE_ID);
        String cookieMobile = PreUtils.getString(context, PreUtils.USER_COOKIE_MOBILE);
        String cookie;
        if(StringUtil.isEmpty(cookieId) || StringUtil.isEmpty(cookieMobile)){
            cookie = "";
        }else{
            cookie = cookieId + ";" + cookieMobile;
        }
        return cookie;
    }

    // 检查更新
    public String getApkInfo(String versionCode) {
        String url = URL_UPDATE_APK + "?vid=" + versionCode + "&type=android";
        return url;
    }

    // 新品推荐、人气爆款
    public String getNewtjRenqibk(String shopId, String type, int page) {
        String url = URL_NEW_TUI_BAO_KUAN + "shop_id=" + shopId + "&type=" + type + "&pg=" + page + "&num=" + PAGE_SIZE;
        return url;
    }

    // 限时抢购
    public String getRushbuy(String shopId) {
        return getRushbuy(shopId, "", -1);
    }
    public String getRushbuy(String shopId, String timeId, int page) {
        String url;
        if(page == -1){
            url = URL_RUSH_BUG + shopId;
        }else{
            url = HttpUrl.URL_RUSH_BUG + shopId + "&id=" + timeId + "&pg=" + page;
        }
        return url;
    }

    // 首页
    public String getHome(int id, int vid, int page) {
        String url = JJMAIL_INDEX + "?cid=" + id + "&vid=" + vid + "&page=" + page;
        return url;
    }

    // 分享
    public String getShare(String good_id, String shop_id) {
        String url = URL_SHARE_INFO + "?pid=" + good_id + "&shop="+ shop_id;
        return url;
    }

    // 详情
    public String getDetails(String idCode, String shop_id) {
        String url = URL_DETAILS + "?id=" + idCode + "&shop=" + shop_id;
        return url;
    }

    // 搜索
    public String getSearch(String search, int page) {
        String url = URL_SEARCH + "?word=" + search + (page == 1 ? "" : "&page=" + page);
        return url;
    }

    // 获取验证码
    public String getLoginCode(String mobile) {
        String url = URL_LOGIN_CODE + mobile + "&shop=537fdt&smstype=login";
        return url;
    }

    // 登录
    public Map<String, String> getLogin(String mobile, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("reg_way", "only_phone");
        map.put("mobile", mobile);
        map.put("captcha", code);
        map.put("shop", "537fdt");
        return map;
    }

    // 我的店
    public String getMyShop(String shopId) {
        String url = URL_MY_SHOP + "?shop=" + shopId;
        return url;
    }

    // 我的店-分享
    public String getMyShopShare(String shopId) {
        String url = URL_MY_SHOP_SHARE + shopId;
        return url;
    }

    // 店铺信息
    public String getUploadHead(String shopId) {
        String url = URL_SHOP_INFO + shopId;
        return url;
    }

    // 购物车
    public String getShoppingCart(String shopId) {
        String url = URL_SHOPPING_CART + shopId;
        return url;
    }

    // 购物车-修改数量
    public Map<String, String> getUpdateCartNum(String shopId, String cart_id, String sku, int num,
                                   String cookie, String userAgent) {
        Map<String, String> map = new HashMap<>();
        map.put("shop", shopId);
        map.put("cart_id", cart_id);
        map.put("sku", sku);
        map.put("num", String.valueOf(num));
        map.put("data", "");
        map.put("type", "product");
        map.put(COOKIE, cookie);
        map.put(User_Agent, userAgent);
        return map;
    }

    // 购物车-修改数量
    public Map<String, String> getDelCartGoods(String shopId, String id, String cookie, String userAgent) {
        Map<String, String> map = new HashMap<>();
        map.put("shop", shopId);
        map.put("id", id);
        map.put(COOKIE, cookie);
        map.put(User_Agent, userAgent);
        return map;
    }

    // 购物车-选中、非选中
    public Map<String, String> getSelectCart(String shopId, String cart_id, int select, String cookie, String userAgent) {
        Map<String, String> map = new HashMap<>();
        map.put("shop", shopId);
        map.put("cart_id", cart_id);
        map.put("do", select == 2 ? "unset" : "set");
        map.put(COOKIE, cookie);
        map.put(User_Agent, userAgent);
        return map;
    }

    // 个人中心
    public Map<String, String> getPersonCent(String shopId, String cookie, String userAgent) {
        Map<String, String> map = new HashMap<>();
        map.put("shop", shopId);
        map.put(COOKIE, cookie);
        map.put(User_Agent, userAgent);
        return map;
    }

    // 城市列表
    public String getCitys(String isnew) {
        return URL_CITYS_LIST + "is_new=" + isnew;
    }

    // 加入购物车
    public Map<String, String> getAddCart(String shop, String sku, int num, int type, String cookie, String userAgent) {
        Map<String, String> map = new HashMap<>();
        map.put("shop", shop);
        map.put("sku", sku);
        map.put("num", String.valueOf(num));
        map.put("type", String.valueOf(type));
        map.put(COOKIE, cookie);
        map.put(User_Agent, userAgent);
        return map;
    }

    // 确认订单
    public String getCommitOrderLast(String shop, String address, String quanIdcode, String balance) {
        String url = URL_SUBMIT_ORDER_INFO + "?shop=" + shop;
        if(!StringUtil.isEmpty(address) && !StringUtil.isEmpty(quanIdcode)){
            url = url + "&address=" + address + "&coupons=" + quanIdcode + "&use_balance=" + balance;
        }else if(!StringUtil.isEmpty(address)){
            url = url + "&address=" + address+ "&use_balance=" + balance;
        }else if(!StringUtil.isEmpty(quanIdcode)){
            url = url + "&coupons=" + quanIdcode+ "&use_balance=" + balance;
        }
        return url;
    }

    // 地址相关
    public Map<String, String> getAddressList(String shop, String cookie, String userAgent) {
        Map<String, String> map = new HashMap<>();
        map.put("shop", shop);
        map.put(COOKIE, cookie);
        map.put(User_Agent, userAgent);
        return map;
    }

    // 地址删除、设置默认地址
    public Map<String, String> getAddressDel(String shop, String address, String cookie, String userAgent) {
        Map<String, String> map = new HashMap<>();
        map.put("shop", shop);
        map.put("address", address);
        map.put(COOKIE, cookie);
        map.put(User_Agent, userAgent);
        return map;
    }

    // 地址添加、修改
    public Map<String, String> getAddressAddUpdate(String shop, int id, String accept_name, String accept_phone,
                                             String cityids, String address, int is_default,
                                             String cookie, String userAgent) {
        Map<String, String> map = new HashMap<>();
        map.put("shop", shop);
        if(id != -1){
            map.put("id", String.valueOf(id));
        }
        map.put("accept_name", accept_name);
        map.put("accept_phone", accept_phone);
        map.put("cityids", cityids);
        map.put("address", address);
        map.put("is_default", String.valueOf(is_default));
        map.put(COOKIE, cookie);
        map.put(User_Agent, userAgent);
        return map;
    }

    // 提交订单
    public Map<String, String> getSubmitOrderInfo(String shop, String address, String coupons,String balance,
                                             String order_remarks, String cookie, String userAgent) {
        Map<String, String> map = new HashMap<>();
        map.put("shop", shop);
        map.put("address", address);
        map.put("coupons", coupons);
        map.put("use_balance",balance);
        map.put("order_remarks", order_remarks);
        map.put(COOKIE, cookie);
        map.put(User_Agent, userAgent);
        return map;
    }

    // 立即支付
    public String getPayInfo(String shop, String payment_no, String payment_type, boolean isSubmitOrder) {
        return URL_PAY + "?shop=" + shop + "&" + (isSubmitOrder ? "payment_no=" : "order_no=") + payment_no +
                "&payment_type=" + payment_type + "&callback=";
    }

    // 个人中心-我的订单
    public String getPersonMyOrder(String shop, int status , int page) {
        return URL_PERSON_MY_ORDER + "?shop=" + shop + "&page=" + page +
                "&status=" + status;
    }

    // 个人中心-订单详情
    public String getPersonOrderDetail(String shop, long no) {
        return URL_PERSON_ORDER_DETAIL + "?shop=" + shop + "&no=" + no;
    }

    // 个人中心-取消订单
    public Map<String, String> getCancelOrder(String shop, long no, String status) {
        Map<String, String> map = new HashMap<>();
        map.put("shop", shop);
        map.put("order_no", String.valueOf(no));
        map.put("status", status);
        return map;
    }

    // 个人中心-售后退款
    public String getAterSaleRefund(String shop, int page) {
        return URL_PERSON_AFTER_SALE_REFUND + "?shop=" + shop + "&page=" + page;
    }

    // 个人中心-退款详情
    public String getRefundDetail(String shop, int service_id, boolean isTuikuan) {
        String url = isTuikuan ? URL_PERSON_REFUND_DETAIL : URL_PERSON_RETUEN_DETAIL;
        return url + "?shop=" + shop + "&service_id=" + service_id;
    }

    // 个人中心-提交物流信息
    public Map<String, String> getSubmitWuliu(String shop, int service_id, String company_code, String express_no) {
        Map<String, String> map = new HashMap<>();
        map.put("shop", shop);
        map.put("service_id", String.valueOf(service_id));
        map.put("company_code", company_code);
        map.put("express_no", express_no);
        return map;
    }

    // 个人中心-退款页
    public String getRefund(String shop, long order_no, int item_id, boolean isTuikuan) {
        String url = isTuikuan ? URL_PERSON_REFUND : URL_PERSON_RETUEN;
        return url + "?shop=" + shop + "&order_no=" + order_no + "&item_id=" + item_id;
    }

    // 个人中心-提交退款
    public Map<String, String> getSubmitTuikuan(String shop, long order_no, int item_id, String reason,
                                                String refund_aomunt, String t_price, String note) {
        Map<String, String> map = new HashMap<>();
        map.put("shop", shop);
        map.put("item_id", String.valueOf(item_id));
        map.put("reason", reason);
        map.put("refund_aomunt", refund_aomunt);
        map.put("note", note);
        map.put("order_no", String.valueOf(order_no));
        map.put("t_price", t_price);
        return map;
    }

    // 个人中心-提交退货
    public Map<String, String> getSubmitTuihuo(String shop, long order_no, int item_id, String reason,
                                                String back_money, String t_price, String note, String card_img_a,
                                               String card_img_b, String card_img_c, int is_receive_goods) {
        Map<String, String> map = new HashMap<>();
        map.put("shop", shop);
        map.put("item_id", String.valueOf(item_id));
        map.put("reason", reason);
        map.put("back_money", back_money);
        map.put("note", note);
        map.put("order_no", String.valueOf(order_no));
        map.put("t_price", t_price);
        map.put("card_img_a", card_img_a);
        map.put("card_img_b", card_img_b);
        map.put("card_img_c", card_img_c);
        map.put("is_receive_goods", String.valueOf(is_receive_goods));
        return map;
    }

    // 个人中心-物流信息
    public Map<String, String> getWuliuInfo(String shop, long order_no) {
        Map<String, String> map = new HashMap<>();
        map.put("shop", shop);
        map.put("no", String.valueOf(order_no));
        return map;
    }

    // 活动
    public String getActivity(String shop, int type) {
        return URL_ACTIVITY + "?shop=" + shop + "&place=" + type;
    }

    // 广告-显示
    public String getADShow(String shop, String appkey) {
        return URL_AD_SHOW + "?shop=" + shop + "&app_key=" + appkey;
    }

    // 广告-显示上报
    public String getADShowReport(boolean isClickReport, String showReportUrl, String shop, String url, String referer_url, String ads_src_url,
                                  int media_id,  int postions_id, int banner_id, String uid, String session_id,
                                  String checksum, String time) {
        showReportUrl = showReportUrl + "?shop=" + shop + "&url=" + url + "&referer_url=" + referer_url;
        if (isClickReport){// 点击上报传此字段
            showReportUrl = showReportUrl + "&ads_src_url=" + ads_src_url;
        }
        showReportUrl = showReportUrl + "&media_id=" + media_id + "&postions_id=" + postions_id + "&banner_id=" + banner_id
                + "&uid=" + uid + "&session_id=" + session_id + "&checksum=" + checksum + "&time=" + time;
        return showReportUrl;
    }

    // 商品详情页-日志统计
    public String getGoodsDetailsLog(String product, String user, String shop, String url) {
        return URL_GOODS_DETAILS_LOG + "?product=" + product + "&user=" + user + "&shop=" + shop + "&url=" + url +
                "&referrer=" + "&from=&ct=and";
    }

    // 口令
    public String getKouling(String shop, String uri) {
        return URL_KOULING + "?shop=" + shop + "&uri=" + uri;
    }

    // 其他页-日志统计
    public String getOtherLog(String user, String url, String shop) {
        return URL_OTHER_LOG + "?user=" + user + "&shop=" + shop + "&url=" + url +
                "&referrer=" + "&ct=and";
    }

    // 我的优惠券
    public String getMyCoupon(String shop) {
        return URL_MY_COUPON + "?shop=" + shop;
    }

    // 兑换优惠券
    public String getDuihuanCoupon(String shop, String couponCode) {
        return URL_MY_COUPON + "?shop=" + shop + "&is_ajax=1&coupon_code=" + couponCode;
    }

}
