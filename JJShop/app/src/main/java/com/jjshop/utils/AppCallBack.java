package com.jjshop.utils;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.jjshop.dialog.KoulingDialog;
import com.jjshop.ui.LoginActivity;
import com.jjshop.ui.activity.WelcomeActivity;

import java.util.List;

/**
 * 作者：张国庆
 * 时间：2018/11/7
 */

public class AppCallBack {
    private static volatile AppCallBack appCallBack;

    private AppCallBack(){}

    public static AppCallBack build(){
        if(null == appCallBack){
            synchronized (AppCallBack.class){
                if(null == appCallBack){
                    appCallBack = new AppCallBack();
                }
            }
        }
        return appCallBack;
    }

    /**
     * 注册状态监听，仅在Application中使用
     * @param application
     */
    public void register(Application application){
        if(null == application || null == callbacks){
            return;
        }
        application.registerActivityLifecycleCallbacks(callbacks);
    }

    public void unRegister(Application application){
        if(null == application || null == callbacks){
            return;
        }
        application.unregisterActivityLifecycleCallbacks(callbacks);
    }

    private IntentFilter mHomeBtnIntentFilter = null;
    private HomeBtnReceiver mHomeBtnReceiver = null;

    public void startHomeListener(Context context) {
        if(null == context){
            return;
        }
        if(null != mHomeBtnReceiver){
            context.unregisterReceiver(mHomeBtnReceiver);
            mHomeBtnReceiver = null;
            mHomeBtnIntentFilter = null;
        }
        mHomeBtnIntentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        mHomeBtnReceiver = new HomeBtnReceiver();
        context.registerReceiver(mHomeBtnReceiver, mHomeBtnIntentFilter);
    }

    public void stopHomeListener(Context context) {
        if(null == context || null == mHomeBtnReceiver){
            return;
        }
        context.unregisterReceiver(mHomeBtnReceiver);
        mHomeBtnIntentFilter = null;
        mHomeBtnReceiver = null;
    }

    /**
     * 前后台切换的监听
     */
    private Application.ActivityLifecycleCallbacks callbacks = new Application.ActivityLifecycleCallbacks() {
        private KoulingDialog koulingDialog;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            JjLog.e("切换到前台");
            if(!Tools.isNetConnected(activity)){
                return;
            }
            //数值从0变到1说明是从后台切到前台
            if(null == activity || activity instanceof WelcomeActivity ||
                    activity instanceof LoginActivity){
                return;
            }
            if(StringUtil.isEmpty(PreUtils.getString(activity, PreUtils.SHOP_ID))){
                return;
            }
            // 获取剪切板里面99口令
            String clipContent = Tools.resolveClipContent(activity);
            if(StringUtil.isEmpty(clipContent)){
                return;
            }
            if(!(activity instanceof FragmentActivity)){
                return;
            }
            if(null != koulingDialog && null != koulingDialog.getDialog() && koulingDialog.getDialog().isShowing()){
                koulingDialog.dismiss();
                koulingDialog = null;
            }
            koulingDialog = KoulingDialog.build().showView(
                    ((FragmentActivity) activity).getSupportFragmentManager(), clipContent);
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            JjLog.e("切换到后台");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            JjLog.e("取消了");
        }
    };

    /**
     * home键的监听
     */
    private class HomeBtnReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra("reason");
                if (reason != null) {
                    if (reason.equals("homekey")) {
                        //按Home按键
                    } else if (reason.equals("recentapps")) {
                        //最近任务键也就是菜单键
                    } else if (reason.equals("assist")) {
                        //常按home键盘
                    }
                }
            }
        }
    }

    private LocationManager locationManager;
    private String locationProvider;
    private Location location;

    public void initLocation(Context context) {
        if(null == context){
            return;
        }
        //1.获取位置管理器
        locationManager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getProviders( true );
        if (providers.contains( LocationManager.NETWORK_PROVIDER )) {
            //如果是网络定位
            JjLog.e("如果是网络定位" );
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains( LocationManager.GPS_PROVIDER )) {
            //如果是GPS定位
            JjLog.e( "如果是GPS定位" );
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            JjLog.e("没有可用的位置提供器" );
            return;
        }
        if(!PermissionUtil.build().checkPermission(context, PermissionUtil.LOCATION[0], false)){
            return;
        }
        if(!PermissionUtil.build().checkPermission(context, PermissionUtil.LOCATION[1], false)){
            return;
        }
        //3.获取上次的位置，一般第一次运行，此值为null
        Location location = locationManager.getLastKnownLocation( locationProvider );
        if (location != null) {
            setLocation( location );
        }
        // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
        locationManager.requestLocationUpdates( locationProvider, 0, 0, locationListener );
    }

    private void setLocation(Location location) {
        this.location = location;
    }

    //获取经纬度
    public Location showLocation() {
        return location;
    }

    // 移除定位监听
    public void removeLocationUpdatesListener() {
        if (locationManager != null) {
            locationManager.removeUpdates( locationListener );
        }
    }

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    LocationListener locationListener = new LocationListener() {

        /**
         * 当某个位置提供者的状态发生改变时
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        /**
         * 某个设备打开时
         */
        @Override
        public void onProviderEnabled(String provider) {

        }

        /**
         * 某个设备关闭时
         */
        @Override
        public void onProviderDisabled(String provider) {

        }

        /**
         * 手机位置发生变动
         */
        @Override
        public void onLocationChanged(Location location) {
            location.getAccuracy();//精确度
            setLocation( location );
        }
    };

}
