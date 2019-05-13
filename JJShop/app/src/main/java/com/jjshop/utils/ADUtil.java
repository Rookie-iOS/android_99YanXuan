package com.jjshop.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.jjshop.bean.ADShowBean;
import com.jjshop.bean.ActivityBean;
import com.jjshop.bean.BaseBean;
import com.jjshop.dialog.ActivityDialog;
import com.jjshop.listener.OnCommonCallBackIm;
import com.jjshop.ui.activity.home.DetailActivity;
import com.jjshop.ui.activity.home.HomeActivity;
import com.jjshop.ui.activity.home.PayActivity;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/10/25
 */

public class ADUtil {
    private static volatile ADUtil AD_UTIL;

    public static ADUtil build(){
        if(null == AD_UTIL){
            synchronized (ADUtil.class){
                if(null == AD_UTIL){
                    AD_UTIL = new ADUtil();
                }
            }
        }
        return AD_UTIL;
    }

    public static final String APPKEY_AD_DETAILS = "0000010000000001";// 详情页
    public static final String APPKEY_AD_MYSHOP = "0000010000000002";// 我的店
    public static final String APPKEY_AD_MYSHOP_BOTTOM = "0000010000000007";// 我的店-底部
    public static final String APPKEY_AD_PAY = "0000010000000003";// 支付成功

    public static final String KEY_IMG = "img_key";
    public static final String KEY_URL = "url_key";
    public static final String KEY_WIDTH = "width_key";
    public static final String KEY_HEIGHT = "height_key";

    /**
     * 请求广告数据
     * @param context
     * @param appkey     广告位
     */
    public void loadADShow(Context context, String appkey){
        loadADShow(context, appkey, null);
    }
    public void loadADShow(final Context context, final String appkey, final OnCommonCallBackIm callBackIm){
        if(null == context){
            return;
        }
        String shopId = PreUtils.getString(context, PreUtils.SHOP_ID);
        if(StringUtil.isEmpty(shopId)){
            return;
        }
        HttpHelper.bulid().getRequest(HttpUrl.build().getADShow(shopId, appkey), ADShowBean.class,
                new OnRequestCallBack<ADShowBean>() {
                    @Override
                    public void onSuccess(ADShowBean bean) {
                        if(null == bean){
                            if(null != callBackIm){
                                callBackIm.onError("数据为空");
                            }
                            return;
                        }
                        ADShowBean.ADShowBeanData data = bean.getData();
                        if(null == data || data.getMedia_id() <= 0){
                            if(null != callBackIm){
                                callBackIm.onError("数据为空");
                            }
                            return;
                        }
                        if(null != callBackIm){
                            callBackIm.onSuccess(data);
                        }
                        // 调用展示上报接口
                        loadADReport(context, false, data);
                        // 显示广告图片
                        Bundle bundle = new Bundle();
                        bundle.putString(KEY_IMG, data.getAds_contents());
                        bundle.putString(KEY_URL, data.getAds_src_url());
                        bundle.putInt(KEY_WIDTH, data.getP_width());
                        bundle.putInt(KEY_HEIGHT, data.getP_height());
                        if(context instanceof DetailActivity){// 详情页
                            ((DetailActivity)context).showAdImg(bundle);
                        }else if(context instanceof HomeActivity){// 店铺
                            if(appkey.equals(APPKEY_AD_MYSHOP_BOTTOM)){// 底部的广告位
                                ((HomeActivity)context).showBottomAdImg(bundle);
                                return;
                            }
                            ((HomeActivity)context).showAdImg(bundle);
                        }else if(context instanceof PayActivity){// 支付页
                            ((PayActivity)context).showAdImg(bundle);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if(null != callBackIm){
                            callBackIm.onError(msg);
                        }
                    }
                });
    }


    /**
     * 广告上报
     * @param context
     * @param isClickReport    true: 点击上报    false: 展示上报
     * @param data             上报所需参数
     */
    public void loadADReport(final Context context, boolean isClickReport, ADShowBean.ADShowBeanData data){
        if(null == context || null == data){
            return;
        }
        String shopId = PreUtils.getString(context, PreUtils.SHOP_ID);
        String uid = PreUtils.getString(context, PreUtils.USER_ID_MINGWEN);
        if(StringUtil.isEmpty(shopId)){
            return;
        }
        HttpHelper.bulid().getRequest(
                HttpUrl.build().getADShowReport(isClickReport, isClickReport ? data.getClick_url() : data.getShow_url(),
                shopId, "", "", data.getAds_src_url(), data.getMedia_id(), data.getPostions_id(),
                data.getBanner_id(), uid, "", data.getChecksum(), data.getTime()),
                BaseBean.class,null);
    }

    /**
     * 首页活动
     * @param context
     * @param type      1: 首页    2：支付成功页
     */
    public void loadActivityData(Context context, int type){
        loadActivityData(context, type, null);
    }
    public void loadActivityData(final Context context, int type, final OnCommonCallBackIm callBackIm){
        if(null == context){
            return;
        }
        String shopId = PreUtils.getString(context, PreUtils.SHOP_ID);
        if(StringUtil.isEmpty(shopId)){
            return;
        }
        HttpHelper.bulid().getRequest(HttpUrl.build().getActivity(shopId, type), ActivityBean.class,
                new OnRequestCallBack<ActivityBean>() {
                    @Override
                    public void onSuccess(ActivityBean bean) {
                        if(null == bean){
                            if(null != callBackIm){
                                callBackIm.onError("数据为空");
                            }
                            return;
                        }
                        if(null != callBackIm){// 如果需要自己处理结果，则默认的处理不执行
                            callBackIm.onSuccess(bean);
                            return;
                        }
                        ArrayList<ActivityBean.ActivityBeanData> list = bean.getData();
                        if(null != list && list.size() > 0){
                            for(ActivityBean.ActivityBeanData data : list){
                                if(null != data){
                                    int type = data.getType();
                                    if(type == 1){// 活动弹框
                                        ActivityDialog.build().showView(((FragmentActivity)context).getSupportFragmentManager(),
                                                data.getThumb(), data.getUrl());
                                    }else if(type == 2){// 右下角显示浮窗
                                        if(context instanceof HomeActivity){// 首页
                                            ((HomeActivity)context).showFuchuang(data.getThumb(), data.getUrl());
                                        }else if(context instanceof PayActivity){
                                            ((PayActivity)context).showFuchuang(data.getThumb(), data.getUrl());
                                        }
                                    }
                                }
                            }

                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if(null != callBackIm){
                            callBackIm.onError(msg);
                        }
                    }
                });
    }
}
