package com.jjshop.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.jjshop.BuildConfig;
import com.jjshop.ui.WebViewActivity;
import com.jjshop.ui.activity.home.HomeActivity;
import com.jjshop.utils.httputil.HttpUrl;

import cn.xiaoneng.activity.ChatActivity;
import cn.xiaoneng.coreapi.ChatParamsBody;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.uiapi.OnMsgUrlClickListener;
import cn.xiaoneng.uiapi.OnUnreadmsgListener;
import cn.xiaoneng.uiapi.XNClickGoodsListener;
import cn.xiaoneng.utils.CoreData;
import cn.xiaoneng.xpush.XPush;

/**
 * 作者：张国庆
 * 时间：2018/8/27
 */

public class XNUtil {
    private static volatile XNUtil wxUtil;
    private XNUtil(){}

    public static XNUtil getXNUtil(){
        if(null == wxUtil){
            synchronized (WXUtil.class){
                if(null == wxUtil){
                    wxUtil = new XNUtil();
                }
            }
        }
        return wxUtil;
    }

    // sdk初始化状态
    private int mIsInitSdkSuccess = -1;
    // 登录状态
    private int mIsLoginSuccess = -1;


    public static final String GOODS_ID = "goods_id";
    public static final String GOODS_ID_CODE = "goods_id_code";
    public static final String GOODS_NAME = "goods_id_name";
    public static final String GOODS_PRICE = "goods_id_price";
    public static final String GOODS_IMG = "goods_id_img";
    public static final String GOODS_URL = "goods_url";
    // 进入详情页的类型
    public static final int XN_INVOKE = 101;

    /**
     * 保证初始化一次
     * @param context
     * @return
     */
    public int initSDK(Context context){
        mIsInitSdkSuccess = Ntalker.getBaseInstance().initSDK(context, HttpUrl.XN_SITE_ID, HttpUrl.XN_SDK_KEY);
        JjLog.e("初始化状态 = " + mIsInitSdkSuccess);
        return mIsInitSdkSuccess;
    }

    /**
     * 推送消息的点击、显示
     * @param context
     */
    public void setXpush(Context context){
        // 点击通知栏跳转到的activity类，默认可传入ChatActivity.class
        XPush.setNotificationClickToActivity(context, ChatActivity.class);
        // iconResId 通知栏显示的大图标，传0为app图标
        // smallIconResId 通知栏和状态栏显示的小图标，传0为app图标
        XPush.setNotificationShowIconId(context, 0,0);
        // 通知栏显示的标题，传null则显示app的名字
        XPush.setNotificationShowTitleHead(context, null);//getResources().getString(R.string.app_name)

    }

    /**
     * 打印log信息. 默认是关闭状态
     */
    public void debug(){
        Ntalker.getBaseInstance().enableDebug(BuildConfig.LOG_DEBUG);
    }

    public void login(Context context){
        login(context, 0);
    }

    /**
     * 登入
     * @param userlevel     1：访客咨询时无视排队逻辑   0：走正常排队逻辑
     */
    public void login(Context context, int userlevel){
        if(null == context || mIsLoginSuccess == 0){
            return;
        }
        String userId = PreUtils.getString(context, PreUtils.USER_ID_MINGWEN);
        String userName = PreUtils.getString(context, PreUtils.USER_NICKNAME);
        JjLog.e("传给小能的id = " + userId + "===name = " + userName);
        if(StringUtil.isEmpty(userId) || StringUtil.isEmpty(userName)){
            return;
        }
        mIsLoginSuccess = Ntalker.getBaseInstance().login(userId, userName, userlevel);
        JjLog.e("小能登录状态 = " + mIsLoginSuccess);
    }

    /**
     * 登出
     */
    public void loginOut(){
        mIsLoginSuccess = -1;
        Ntalker.getBaseInstance().logout();
    }

    /**
     * 打开聊天页面
     * @param context
     * @param bundle
     */
    public void openChat(Context context, Bundle bundle){
        if(null == context){
            return;
        }
        if(mIsInitSdkSuccess != 0){
            mIsInitSdkSuccess = initSDK(context);
        }
        if(mIsInitSdkSuccess == 0){
            Ntalker.getBaseInstance().startChat(context, HttpUrl.XN_SETTING_ID,
                    "99严选", chatParamsBody(context, bundle));
        }

    }

    // 打开聊天页面 传的参数
    private ChatParamsBody chatParamsBody(Context context, Bundle bundle){
        ChatParamsBody body = new ChatParamsBody();
        // 设置聊窗中url链接打开方式
        body.clickurltoshow_type = CoreData.CLICK_TO_APP_COMPONENT;
        // 设置商品条目打开方式
        body.itemparams.clicktoshow_type = CoreData.CLICK_TO_APP_COMPONENT;
        // 设置用户头像
        body.headurl = PreUtils.getString(context, PreUtils.SHOP_LOGO);
        if(null != bundle){
            String shopId = PreUtils.getString(context, PreUtils.SHOP_ID);
            body.erpParam = shopId;
            // 传入客服端商品id
            body.itemparams.clientgoodsinfo_type = CoreData.SHOW_GOODS_BY_ID;
            body.itemparams.goods_id = bundle.getString(GOODS_ID_CODE);//示例：ntalker_test，传入商品id
            body.itemparams.itemparam = shopId;
            // 商品信息
            body.itemparams.appgoodsinfo_type = CoreData.SHOW_GOODS_BY_WIDGET;
            body.itemparams.goods_name = bundle.getString(GOODS_NAME);
            body.itemparams.goods_price = bundle.getString(GOODS_PRICE);
            body.itemparams.goods_image = bundle.getString(GOODS_IMG);
//            body.itemparams.goods_url = bundle.getString(GOODS_URL);
            body.itemparams.goods_showurl = bundle.getString(GOODS_ID);
        }
        return body;
    }

    /**
     * 关闭聊天窗
     */
    public void finishChat(){
        Ntalker.getExtendInstance().chatHeadBar().setFinishButtonFunctions();
    }

    /**
     * 小能事件监听
     */
    public void setOnXnCallBack(final Context context){
        Ntalker.getExtendInstance().message().setOnUnreadmsgListener(new OnUnreadmsgListener() {
            @Override
            public void onUnReadMsg(String s, String s1, String s2, final int i) {
                if(null == context){
                    return;
                }
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(context instanceof HomeActivity){
                            ((HomeActivity)context).isShowXnmsg(i);
                        }
                    }
                });

            }
        });

        // 聊天窗  点击链接  跳转
        Ntalker.getExtendInstance().message().setOnMsgUrlClickListener(new OnMsgUrlClickListener() {
            @Override
            public void onClickUrlorEmailorNumber(int i, String s) {
                WebViewActivity.invoke(context, s);
            }
        });

        // 商品条目点击
        Ntalker.getExtendInstance().message().onClickShowGoods(new XNClickGoodsListener() {
            @Override
            public void onClickShowGoods(int i, int i1, String goods_id_code, String goods_name, String goods_price,
                                         String goods_image, String goods_url, String goods_showurl) {
                finishChat();
            }
        });
    }

    /**
     * 商品详情页轨迹
     */
    public void startGoodsDetail(String title, String url){
        if(StringUtil.isEmpty(title) || StringUtil.isEmpty(url)){
            return;
        }
        JjLog.e("轨迹的数据 title = " + title + "==url = " + url);
        /**
         * 商品详情页轨迹标准接口
         * @param title 商品详情页的标题, 必传字段
         * @param url 商品详情页的url, 必传字段，需要保证每个商品详情页的url唯一，可传入商品链接
         * @param sellerid 商户id, B2C企业传空, B2B企业需要传入商户的id
         * @param ref 上一页url, 如没有可传空
         * @return
         */
        int code = Ntalker.getBaseInstance().startAction_goodsDetail(title, url, "", "");
        JjLog.e("轨迹返回的code = " + code);
    }



}
