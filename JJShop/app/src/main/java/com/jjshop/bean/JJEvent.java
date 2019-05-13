package com.jjshop.bean;

public class JJEvent<T> {
    /** 支付成功 **/
    public static final int PAY_SUCCESS = 10001;
    /** 支付取消 **/
    public static final int PAY_CANCEL = 10002;
    /** 支付失败 **/
    public static final int PAY_FAIL = 10003;
    /** 修改店铺名称 **/
    public static final int UPDATE_SHOP_NAME = 10004;
    /** 修改店铺简介 **/
    public static final int UPDATE_SHOP_INFO = 10005;
    /** 刷新店铺信息（修改本地数据） **/
    public static final int UPDATE_LOCAL_SHOP_INFO = 10006;
    /** 刷新我的店 （调用接口刷新数据） **/
    public static final int REFRESH_MY_SHOP_INFO = 10007;
    /** 刷新购物车 **/
    public static final int REFRESH_SHOPPING_CART = 10008;
    /** 修改昵称 **/
    public static final int UPDATE_PERSON_NICKNAME = 10009;
    /** 修改性别 **/
    public static final int UPDATE_PERSON_SEX = 10010;
    /** 修改姓名 **/
    public static final int UPDATE_PERSON_NAME = 10011;
    /** 修改用户头像、名称、手机号、所属地区 **/
    public static final int UPDATE_LOCAL_PERSON_INFO = 10012;
    /** 刷新地址列表 **/
    public static final int REFRESH_ADDRESS = 10013;
    /** 确认订单刷新地址 **/
    public static final int SUBMIT_ORDER_REFRESH_ADDRESS = 10014;
    /** finish当前页面之前的activity **/
    public static final int FINISH_ACTIVITY = 10015;
    /** 刷新订单列表数据 **/
    public static final int REFRESH_ORDER_LIST = 10016;
    /** 刷新订单详情数据 **/
    public static final int REFRESH_ORDER_DETAILS = 10017;
    /** 刷新个人中心页面信息 **/
    public static final int REFRESH_PERSON_INFO = 10018;


    // 通知id，需要保持应用级别的唯一性
    // 定义于VivaEventID类
    private final int mEventId;

    // 通知说明
    private String mEventMessage;

    // 通知实体
    private T mData;

    public JJEvent(int eventId) {
        this.mEventId = eventId;
    }

    public JJEvent(int eventId, String eventMessage) {
        this.mEventId = eventId;
        this.mEventMessage = eventMessage;
    }

    public JJEvent(int eventId, T data) {
        this.mEventId = eventId;
        this.mData = data;
    }

    public JJEvent(int eventId, String eventMessage, T data) {
        this.mEventId = eventId;
        this.mEventMessage = eventMessage;
        this.mData = data;
    }

    public int getEventId() {
        return mEventId;
    }

    public String getEventMessage() {
        return mEventMessage;
    }

    public T getData() {
        return mData;
    }
}
