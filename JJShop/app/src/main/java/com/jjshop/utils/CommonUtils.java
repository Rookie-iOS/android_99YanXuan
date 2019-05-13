package com.jjshop.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.view.WheelView;
import com.google.gson.Gson;
import com.jjshop.R;
import com.jjshop.app.JJShopApplication;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.CitysBean;
import com.jjshop.bean.CitysPickerBean;
import com.jjshop.bean.HomeClassifyBean;
import com.jjshop.bean.UploadImgBean;
import com.jjshop.bean.WxPayBean;
import com.jjshop.listener.OnCommonCallBackIm;
import com.jjshop.ui.activity.person.PersonEditActivity;
import com.jjshop.ui.activity.person.UpdateAddAddressActivity;
import com.jjshop.utils.dbutils.DBManager;
import com.jjshop.utils.dbutils.LitepalUtil;
import com.jjshop.utils.httputil.GsonUtil;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：张国庆
 * 时间：2018/8/8
 */

public class CommonUtils {
    private static volatile CommonUtils commonUtils;

    private CommonUtils() {
    }

    public static CommonUtils build() {
        if (null == commonUtils) {
            synchronized (CommonUtils.class) {
                if (null == commonUtils) {
                    commonUtils = new CommonUtils();
                }
            }
        }
        return commonUtils;
    }

    // 订单详情-待付款
    public static final int ORDER_DFK = 15;
    // 订单详情-买家已付款
    public static final int ORDER_YFK_ONE = 20;
    public static final int ORDER_YFK_TWO = 30;
    // 订单详情-卖家已发货
    public static final int ORDER_YFH_ONE = 40;
    public static final int ORDER_YFH_TWO = 41;
    // 订单详情-交易成功
    public static final int ORDER_SUCCESS_ONE = 50;
    public static final int ORDER_SUCCESS_TWO = 100;
    public static final int ORDER_SUCCESS_THREE = 200;
    public static final int ORDER_SUCCESS_FOUR = -100;
    // 订单详情-交易关闭
    public static final int ORDER_CANCEL_ONE = -10;
    public static final int ORDER_CANCEL_TWO = -20;

    /**
     * 活动类型
     */
    public static final int ACTIVITY_TYPE_HOME = 1;
    public static final int ACTIVITY_TYPE_PAY = 2;


    /**
     * 判断本地是否有城市数据
     *
     * @param callBackIm
     */
    public void isHasLocalCitysData(final DBManager dbManager, final OnCommonCallBackIm
            callBackIm) {
        Observable.just("")
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(String s) throws Exception {
                        String city = dbManager.query();
                        if (StringUtil.isEmpty(city)) {
                            return false;
                        }
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean b) {
                        if (null != callBackIm) {
                            callBackIm.isHasCity(b);
                        }
                        if (b) {
                            JjLog.e("有数据的  嘎嘎嘎");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 获取本地城市数据，并解析
     */
    public void loadLocalCitysData(final DBManager dbManager, final OnCommonCallBackIm callBackIm) {
        Observable.just("")
                .map(new Function<String, CitysPickerBean>() {
                    @Override
                    public CitysPickerBean apply(String s) throws Exception {
                        if (null == dbManager) {
                            return null;
                        }
                        // 读取数据库的城市数据
                        String citys = dbManager.query();
                        JjLog.e("读取数据库的城市列表 = " + citys);
                        // 解析之后的实体类
                        CitysBean bean = GsonUtil.getGsonUtil().strJsonToBean(citys, CitysBean
                                .class);
                        if (null == bean) {
                            return null;
                        }
                        // 一级数据集合
                        ArrayList<CitysBean.CityOneData> listOne = bean.getData();
                        if (null == listOne || listOne.size() <= 0) {
                            return null;
                        }
                        // 二级数据集合
                        ArrayList<ArrayList<CitysBean.CityTwoData>> listTwo = new ArrayList<>();
                        // 三级数据集合
                        ArrayList<ArrayList<ArrayList<CitysBean.CityThreeData>>> listThree = new
                                ArrayList<>();

                        for (int i = 0; i < listOne.size(); i++) {//遍历省份
                            ArrayList<CitysBean.CityTwoData> two = listOne.get(i).getChildren();
                            listTwo.add(two);
                            ArrayList<ArrayList<CitysBean.CityThreeData>> three = new ArrayList<>();
                            for (int j = 0; j < two.size(); j++) {
                                three.add(two.get(j).getChildren());
                            }
                            listThree.add(three);
                        }
                        CitysPickerBean pickerBean = new CitysPickerBean();
                        pickerBean.setListOne(listOne);
                        pickerBean.setListTwo(listTwo);
                        pickerBean.setListThree(listThree);
                        return pickerBean;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CitysPickerBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CitysPickerBean bean) {
                        if (null == callBackIm) {
                            return;
                        }
                        if (null != bean) {
                            callBackIm.onCitysSuccess(bean);
                            return;
                        }
                        callBackIm.onCitysError("组合数据失败");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (null != callBackIm) {
                            callBackIm.onCitysError(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 保存城市数据到本地文件
     *
     * @param cityJson
     */
    public void saveLocalCitysData(final DBManager dbManager, final String cityJson) {
        saveLocalCitysData(dbManager, cityJson, null);
    }

    public void saveLocalCitysData(final DBManager dbManager, final String cityJson, final
    OnCommonCallBackIm callBackIm) {
        Observable.just("")
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(String s) throws Exception {
                        if (null == dbManager || StringUtil.isEmpty(cityJson)) {
                            return false;
                        }
                        String citys = dbManager.query();
                        if (StringUtil.isEmpty(citys)) {
                            return dbManager.insert(cityJson);
                        }
                        return dbManager.update(cityJson);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean bean) {
                        if (bean) {
                            JjLog.e("保存成功");
                        }
                        if (null != callBackIm) {
                            callBackIm.isHasCity(bean);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 初始化城市选择器
     */
    public OptionsPickerView initCityPicker(final Context context, CitysPickerBean bean) {
        if (null == bean || null == context) {
            return null;
        }
        final ArrayList<CitysBean.CityOneData> listOne = bean.getListOne();
        final ArrayList<ArrayList<CitysBean.CityTwoData>> listTwo = bean.getListTwo();
        final ArrayList<ArrayList<ArrayList<CitysBean.CityThreeData>>> listThree = bean
                .getListThree();
        if (null == listOne || null == listTwo || null == listThree) {
            return null;
        }
        OptionsPickerView mCityPickerView = new OptionsPickerBuilder(context, new
                OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String city1 = listOne.get(options1).getPickerViewText();
                String city2 = listTwo.get(options1).get(options2).getPickerViewText();
                String city3 = listThree.get(options1).get(options2).get(options3)
                        .getPickerViewText();
                int cityId1 = listOne.get(options1).getValue();
                int cityId2 = listTwo.get(options1).get(options2).getValue();
                int cityId3 = listThree.get(options1).get(options2).get(options3).getValue();
                if (context instanceof PersonEditActivity) {
                    ((PersonEditActivity) context).selectCity(city1, city2, city3, cityId1,
                            cityId2, cityId3);
                } else if (context instanceof UpdateAddAddressActivity) {
                    ((UpdateAddAddressActivity) context).selectCity(city1, city2, city3, cityId1,
                            cityId2, cityId3);
                }
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.RED)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setDividerType(WheelView.DividerType.WRAP)
                .setContentTextSize(20)
                .build();

        mCityPickerView.setPicker(listOne, listTwo, listThree);//三级选择器

        return mCityPickerView;
    }


    /**
     * 初始化出生年月选择器
     */
    public TimePickerView initTimePicker(final Context context) {
        if (null == context) {
            return null;
        }
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        //正确设置方式 原因：注意事项有说明
        selectedDate.set(1970, 5, 15);
        startDate.set(1918, 0, 1);

        TimePickerView mTimePickerView = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if (context instanceof PersonEditActivity) {
                    ((PersonEditActivity) context).selectTime(date);
                }
            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setContentTextSize(20)
                .setTitleText("请选择出生年月")
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.RED)
                .setDividerType(WheelView.DividerType.WRAP)
                .build();

        return mTimePickerView;
    }

    /**
     * 设置购物车数量、订单数量的显示和隐藏
     *
     * @param textView
     * @param num
     */
    public void setCirleTextNum(TextView textView, int num) {
        if (null == textView) {
            return;
        }
        if (num <= 0) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            if (num > 99) {
                textView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,
                        textView.getContext().getResources().getDimensionPixelOffset(R.dimen
                                .space_8));
                textView.setText("99+");
            } else {
                textView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,
                        textView.getContext().getResources().getDimensionPixelOffset(R.dimen
                                .space_11));
                textView.setText(String.valueOf(num));
            }
        }
    }

    /**
     * 安装APK
     *
     * @param context
     * @param file
     */
    public void installApk(Context context, File file) {
        if (null == context || null == file) {
            return;
        }
        try {
            Uri contentUri;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //android N的权限问题
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                contentUri = FileProvider.getUriForFile(context, "com.jjshop.fileprovider", file)
                ;//注意修改
            } else {
                contentUri = Uri.fromFile(file);
            }
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (Exception e) {
            JjLog.e("e= " + e.getMessage());
        }
    }

    /**
     * 通过订单号获取微信支付所需要的数据（不是在提交订单页进行支付操作）
     *
     * @param shopId
     * @param payment_no
     * @param payment_type
     * @param callBackIm
     */
    public void loadWxPayOrderData(String shopId, String payment_no, String payment_type, final
    OnCommonCallBackIm callBackIm) {
        loadWxPayOrderData(shopId, payment_no, payment_type, false, callBackIm);
    }

    /**
     * 通过订单号获取微信支付所需要的数据
     *
     * @param shopId
     * @param payment_no
     * @param payment_type
     * @param isSubmitOrder true: 代表是在提交订单页进行支付操作    false: 在其他页进行支付操作
     * @param callBackIm
     */
    public void loadWxPayOrderData(String shopId, String payment_no, String payment_type, boolean
            isSubmitOrder, final OnCommonCallBackIm callBackIm) {
        payment_type = StringUtil.isEmpty(payment_type) ? "weixin" : payment_type;
        if (StringUtil.isEmpty(shopId) || StringUtil.isEmpty(payment_no) || StringUtil.isEmpty
                (payment_type) || null == callBackIm) {
            return;
        }
        HttpHelper.bulid().getRequest(HttpUrl.build().getPayInfo(shopId, payment_no,
                payment_type, isSubmitOrder),
                WxPayBean.class, new OnRequestCallBack<WxPayBean>() {
                    @Override
                    public void onSuccess(WxPayBean data) {
                        WxPayBean.WxPayData bean = data.getData();
                        if (null == bean) {
                            callBackIm.onWxPayDataError("没有数据");
                            return;
                        }
                        callBackIm.onWxPayDataSuccess(bean);
                    }

                    @Override
                    public void onError(String msg) {
                        JjLog.e("微信支付数据 = " + msg);
                        callBackIm.onWxPayDataError(msg);
                    }
                });
    }

    /**
     * 取消订单
     *
     * @param shopId
     * @param orderNo
     */
    public void loadCancelOrder(String shopId, long orderNo, String status, final
    OnCommonCallBackIm callBackIm) {
        HttpHelper.bulid().postRequest(HttpUrl.URL_PERSON_CANCEL_ORDER, BaseBean.class,
                HttpUrl.build().getCancelOrder(shopId, orderNo, status),
                new OnRequestCallBack<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean data) {
                        if (null == data) {
                            callBackIm.onCanceOrderError("数据为空");
                            return;
                        }
                        callBackIm.onCanceOrderSuccess(data);
                    }

                    @Override
                    public void onError(String msg) {
                        callBackIm.onCanceOrderError(msg);
                    }
                });
    }

    /**
     * 上传图片
     *
     * @param shopId
     * @param file
     * @param callBackIm
     */
    public void loadDataFile(String shopId, final File file, final OnCommonCallBackIm callBackIm) {
        if (null == file || StringUtil.isEmpty(shopId)) {
            callBackIm.onUploadImgError("系统不兼容");
            return;
        }
        final Map<String, String> map = new HashMap<>();
        map.put("shop", shopId);
        HttpHelper.bulid().postRequest(HttpUrl.URL_UPLOAD_HEAD, UploadImgBean.class, map, file,
                "headimg_upload",
                new OnRequestCallBack<UploadImgBean>() {
                    @Override
                    public void onSuccess(UploadImgBean data) {
                        if (null != callBackIm) {
                            callBackIm.onUploadImgSuccess(data);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (null != callBackIm) {
                            callBackIm.onUploadImgError(msg);
                        }
                    }
                });
    }


    /**
     * 下载图片并且保存到相册
     *
     * @param context
     * @param imgUrl
     * @param callBackIm
     */
    public void downLoadImgToAlbum(final Context context, final String imgUrl, final
    OnCommonCallBackIm callBackIm) {
        downLoadImgToAlbum(context, imgUrl, System.currentTimeMillis() + "jjshop.jpg", callBackIm);
    }

    public void downLoadImgToAlbum(final Context context, final String imgUrl, String fileName,
                                   final OnCommonCallBackIm callBackIm) {
        if (null == context || StringUtil.isEmpty(imgUrl)) {
            return;
        }
        HttpHelper.bulid().getRequestFile(imgUrl, Tools.systemAlumb(), fileName, new
                OnCommonCallBackIm() {

            @Override
            public void onProgress(float progress, long total) {
                if (null != callBackIm) {
                    callBackIm.onProgress(progress, total);
                }
            }

            @Override
            public void onSuccess(Object o) {
                if (o == null || !(o instanceof File)) {
                    if (null != callBackIm) {
                        callBackIm.onError("保存失败");
                        return;
                    }
                    JjToast.getInstance().toast("保存失败");
                    return;
                }
                File file = (File) o;
                //通知相册更新
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        BitmapFactory.decodeFile(file.getAbsolutePath()), file.getName(), null);
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                context.sendBroadcast(intent);
                // 自定义回调
                if (null != callBackIm) {
                    callBackIm.onSuccess(true);
                    return;
                }
                JjToast.getInstance().toast("保存成功");
            }

            @Override
            public void onError(String msg) {
                if (null != callBackIm) {
                    callBackIm.onError("保存失败");
                    return;
                }
                JjToast.getInstance().toast("保存失败");
            }
        });
    }

    /**
     * 获取IP
     *
     * @param context
     * @return
     */
    public String getNetIP(final Context context) {
        if (null == context) {
            return "";
        }
        String ip = null;
        try {
            ConnectivityManager conMan = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);

            // mobile 3G Data Network
            android.net.NetworkInfo.State mobile = conMan.getNetworkInfo(
                    ConnectivityManager.TYPE_MOBILE).getState();
            // wifi
            android.net.NetworkInfo.State wifi = conMan.getNetworkInfo(
                    ConnectivityManager.TYPE_WIFI).getState();

            // 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
            if (mobile == android.net.NetworkInfo.State.CONNECTED
                    || mobile == android.net.NetworkInfo.State.CONNECTING) {
                ip = getLocalIpAddress();
            }
            if (wifi == android.net.NetworkInfo.State.CONNECTED
                    || wifi == android.net.NetworkInfo.State.CONNECTING) {
                //获取wifi服务
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context
                        .WIFI_SERVICE);
                //判断wifi是否开启
                if (!wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(true);
                }
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();
                ip = (ipAddress & 0xFF) + "." +
                        ((ipAddress >> 8) & 0xFF) + "." +
                        ((ipAddress >> 16) & 0xFF) + "." +
                        (ipAddress >> 24 & 0xFF);
            }
        } catch (Exception e) {
            JjLog.e("获取IP地址异常 = " + e.getMessage());
            ip = "";
        }
        return ip == null ? "" : ip;

    }

    /**
     * @return 手机GPRS网络的IP
     */
    private String getLocalIpAddress() {
        try {
            //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                        .hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address)
                    {//获取IPv4的IP地址
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }


        return "";
    }

    public void setViewW(View view, int w) {
        setViewWH(view, w, 0);
    }

    public void setViewH(View view, int h) {
        setViewWH(view, 0, h);
    }

    /**
     * 设置view的宽高
     *
     * @param view
     * @param w
     * @param h
     */
    public void setViewWH(View view, int w, int h) {
        if (null == view) {
            return;
        }
        ViewGroup.LayoutParams eParams = view.getLayoutParams();
        if (eParams instanceof RelativeLayout.LayoutParams) {
            if (w > 0) {
                ((RelativeLayout.LayoutParams) eParams).width = w;
            }
            if (h > 0) {
                ((RelativeLayout.LayoutParams) eParams).height = h;
            }
        } else if (eParams instanceof LinearLayout.LayoutParams) {
            if (w > 0) {
                ((LinearLayout.LayoutParams) eParams).width = w;
            }
            if (h > 0) {
                ((LinearLayout.LayoutParams) eParams).height = h;
            }
        }
        view.setLayoutParams(eParams);
    }

    /**
     * 设置view的上下左右的边距
     *
     * @param view
     * @param left
     * @param top
     * @param rigth
     * @param bottom
     */
    private void setViewMargin(View view, int left, int top, int rigth, int bottom) {
        if (null == view || left < 0 || top < 0 || rigth < 0 || bottom < 0) {
            return;
        }
        ViewGroup.LayoutParams eParams = view.getLayoutParams();
        if (eParams instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) eParams).setMargins(left, top, rigth, bottom);
        } else if (eParams instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) eParams).setMargins(left, top, rigth, bottom);
        }
        view.setLayoutParams(eParams);
    }

    /**
     * 设置状态栏高度，颜色
     *
     * @param view
     * @param isTransparent
     */
    public void setStatusViewH(View view, boolean isTransparent) {
        if (null == view || null == view.getContext()) {
            return;
        }
        Context context = view.getContext();
        if (null == context) {
            return;
        }
        setViewWH(view, 0, UIUtils.getStatusHeight(context));
        if (isTransparent && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {// 小于6.0设置成黑色半透明
            view.setBackgroundColor(context.getResources().getColor(R.color.color_80333333));
        }
    }

    /**
     * 获取本地数据
     *
     * @param context
     * @param keyValue   表 key列对应的值
     * @param callBackIm
     */
    public void loadLocalData(final Context context, final String keyValue, final
    OnCommonCallBackIm callBackIm) {
        if (null == context || StringUtil.isEmpty(keyValue) || null == callBackIm) {
            return;
        }
        Observable.just("")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return LitepalUtil.build().find(LitepalUtil.TAB_COLUMN_KEY, keyValue);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        if (StringUtil.isEmpty(s)) {
                            JjLog.e("没有数据key = " + keyValue);
                            callBackIm.onError("没有本地数据");
                            return;
                        }
                        JjLog.e("保存的数据key = " + keyValue + "\n数据 = " + s);
                        if (!s.contains("{") && !s.contains("}")) {
                            callBackIm.onError("不是json数据");
                            JjLog.e("不是json数据");
                            return;
                        }
                        if (!keyValue.equals(LitepalUtil.HOME_TITLE_KEY)) {
                            callBackIm.onSuccess(s);
                            return;
                        }
                        try {
                            HomeClassifyBean bean = new Gson().fromJson(s, HomeClassifyBean.class);
                            if (null != bean) {
                                bean.setJson(s);
                                callBackIm.onSuccess(bean);
                                return;
                            }
                            callBackIm.onError("没有本地数据");
                        } catch (Exception e) {
                            callBackIm.onError("没有本地数据");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBackIm.onError("没有本地数据");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 保存json数据
     *
     * @param key  表 key列的值
     * @param json 表 value列的值
     */
    public void saveNetData(final String key, final String json) {
        if (StringUtil.isEmpty(key) || StringUtil.isEmpty(json)) {
            return;
        }
        if (!json.contains("{") && !json.contains("}")) {
            JjLog.e("不是json数据");
            return;
        }
        Observable.just("")
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(String s) throws Exception {
                        LitepalUtil.build().save(key, json);
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean s) {
                        if (s) {
                            JjLog.e("数据保存成功key = " + key);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 其他页日志统计
     *
     * @param context
     * @param name
     */
    public void otherLog(Context context, String name) {
        if (null == context || StringUtil.isEmpty(name)) {
            return;
        }
        String uIdcode = PreUtils.getString(context, PreUtils.USER_ID_CODE);
        String shop = PreUtils.getString(context, PreUtils.SHOP_ID);
        if (StringUtil.isEmpty(uIdcode) || StringUtil.isEmpty(shop)) {
            return;
        }
        JjLog.e("日志统计名称 = " + name);
        // 日志统计
        HttpHelper.bulid().getRequest(HttpUrl.build().getOtherLog(uIdcode, name, shop), BaseBean
                .class, null);
    }

    /**
     * cookie、userAgent信息
     *
     * @param context
     */
    public void initUserAgentAndCookie(Context context) {
        if (null == context) {
            return;
        }
        // 获取当前经纬度
//        AppCallBack.build().initLocation(context);
//        Location location = AppCallBack.build().showLocation();
//        if(null != location){
//            PreUtils.saveLatLont(context,
//                    String.valueOf(location.getLatitude()), String.valueOf(location
// .getLongitude()));
//        }else{
//            PreUtils.saveLatLont(context, "0.0", "0.0");
//        }
        String device = "device=#" + DeviceUtils.getDeviceCode(context) + "#Android#" + android
                .os.Build.VERSION.RELEASE + "#JJdance#" + JJShopApplication.sVersion + "#"
                + android.os.Build.MODEL + "#" + DeviceUtils.getNetworkType(context) + "#" +
                UIUtils.getWidth() + "#" + UIUtils.getHeight() + "#" +
                PreUtils.getString(context, PreUtils.LATITUDE, "0.0") + "#" + PreUtils.getString
                (context, PreUtils.LONTITUDE, "0.0") + "#YT-LOGIN-TOKEN#99yanxuanapp#;";
        PreUtils.setString(context, PreUtils.DEVICE, device);
        JJShopApplication.mCookie = HttpUrl.getCookies(context);
        JJShopApplication.mUser_agent = device;
        JjLog.e("device====" + device);
    }

}
