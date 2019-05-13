package com.jjshop.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.jjshop.app.JJShopApplication;
import com.jjshop.dialog.CartNumDelDialog;
import com.jjshop.ui.activity.WelcomeActivity;

/**
 * 作者：张国庆
 * 时间：2018/8/2
 */

public class PermissionUtil {
    private static volatile PermissionUtil permissionUtil;
    private PermissionUtil(){}
    public static PermissionUtil build(){
        if(null == permissionUtil){
            synchronized (PermissionUtil.class){
                if(null == permissionUtil){
                    permissionUtil = new PermissionUtil();
                }
            }
        }
        return permissionUtil;
    }

    public static final int PERMISSION_REQUEST_CODE = 101;
    public static final int PERMISSION_PACKAGEINSTALLS = 102;
    public static final int PERMISSION_ALL = 103;
    public static final String P_TONGZHI = "检测到您没有开启通知权限，开启权限您可以提前知道最新的活动、更多的优惠，赶紧开启吧";

    // 储存卡写入
    public static final String WRITE_EXTERNAL_STORAGE = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
    // 储存卡读取
    public static final String READ_EXTERNAL_STORAGE = android.Manifest.permission.READ_EXTERNAL_STORAGE;
    // 相机
    public static final String CAMERA = android.Manifest.permission.CAMERA;
    // 位置信息
    public static final String[] LOCATION = {android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION};

    public boolean checkPermission(Context context, String permission){
        return checkPermission(context, permission, true);
    }

    /**
     *检查是否有权限，没有权限去请求权限
     * @param context
     * @param permission    权限
     * @param isRequest     没有权限是否去请求权限
     * @return
     */
    public boolean checkPermission(Context context, String permission, boolean isRequest){
        if(null == context || !(context instanceof Activity) || StringUtil.isEmpty(permission)){
            return false;
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){// 小于23默认都有权限
            return true;
        }
        int checkInt = ContextCompat.checkSelfPermission(context, permission);
        if(checkInt != PackageManager.PERMISSION_GRANTED){// 说明没有权限，需要去申请
            if(isRequest){
                requestPermission(context, new String[]{permission});
            }
            return false;
        }
        return true;
    }

    /**
     * 权限请求
     * @param context
     * @param permission    权限组
     * @return
     */
    public void requestPermission(Context context, String[] permission){
        if(null == context || !(context instanceof Activity) || null == permission || permission.length <= 0){
            return;
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){// 小于23默认都有权限
            return;
        }
        ActivityCompat.requestPermissions((Activity) context, permission, PERMISSION_REQUEST_CODE);
    }

    /**
     * 判断是否可以弹出系统权限框
     * @param context
     * @param permission    权限
     * @return              true: 没有选中不再提示（可以弹出权限框）  false: 选中不再提示（不能弹出权限框）
     */
    public boolean showPermission(Context context, String permission){
        if(null == context || !(context instanceof Activity) || StringUtil.isEmpty(permission)){
            return true;
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){// 小于23默认都有权限
            return true;
        }
        return ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission);
    }

    // 检测是否有未知来源权限
    public boolean canRequestPackageInstalls(Context context){
        if(null == context){
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return context.getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }

    // 打开系统未知来源页面
    public void settingPackageInstall(Context context){
        if(null == context){
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        ((Activity)context).startActivityForResult(intent, PERMISSION_PACKAGEINSTALLS);
    }

    // 打开通知设置页面
    public void settingTongzhi(final Context context){
        if(null == context || !(context instanceof FragmentActivity)){
            return;
        }
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        boolean isOpened = manager.areNotificationsEnabled();
        if(isOpened){
            return;
        }
        CartNumDelDialog.build().showView(((FragmentActivity)context).getSupportFragmentManager(), -1,
                CartNumDelDialog.TONGZHI_CODE, PermissionUtil.P_TONGZHI)
                .setOnCommonClickCalllBack(new CartNumDelDialog.OnCommonClickCalllBack() {
                    @Override
                    public void callBack(int typeCode, int num) {
                        if(typeCode != CartNumDelDialog.TONGZHI_CODE){
                            return;
                        }
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", JJShopApplication.sPackageName, null);
                        intent.setData(uri);
                        context.startActivity(intent);
                    }
                });
    }

    public void openAppSetting(Context context, String msg){
        if(null == context){
            return;
        }
        CartNumDelDialog.build().showView(((FragmentActivity)context).getSupportFragmentManager(), -1,
                CartNumDelDialog.TONGZHI_CODE, msg)
                .setOnCommonClickCalllBack(new CartNumDelDialog.OnCommonClickCalllBack() {
                    @Override
                    public void callBack(int typeCode, int num) {
                        if(typeCode == CartNumDelDialog.CANCEL_CODE){
                            if(context instanceof WelcomeActivity){
                                ((WelcomeActivity)context).initCookieUserAgent();
                            }
                            return;
                        }
                        if(typeCode != CartNumDelDialog.TONGZHI_CODE){
                            return;
                        }
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", JJShopApplication.sPackageName, null);
                        intent.setData(uri);
                        ((FragmentActivity) context).startActivityForResult(intent, PERMISSION_ALL);
                    }
                });
    }
}
