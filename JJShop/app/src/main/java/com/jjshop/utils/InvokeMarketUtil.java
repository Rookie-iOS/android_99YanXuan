package com.jjshop.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;

import com.jjshop.app.JJShopApplication;

import java.lang.reflect.Method;
import java.util.List;

import cn.xiaoneng.xpush.manager.BuildProperties;

/**
 * 作者：张国庆
 * 时间：2018/11/8
 */

public class InvokeMarketUtil {
    private static volatile InvokeMarketUtil marketUtil;

    private InvokeMarketUtil(){}

    public static InvokeMarketUtil build(){
        if(null == marketUtil){
            synchronized (InvokeMarketUtil.class){
                if(null == marketUtil){
                    marketUtil = new InvokeMarketUtil();
                }
            }
        }
        return marketUtil;
    }

    // 小米 MIUI标识
    private final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    // 华为 EMUI标识
    private final String KEY_EMUI_VERSION_CODE = "ro.build.version.emui";
    private final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";
    // 魅族 Flyme标识
    private final String KEY_FLYME_ICON_FALG = "persist.sys.use.flyme.icon";
    private final String KEY_FLYME_SETUP_FALG = "ro.meizu.setupwizard.flyme";
    private final String KEY_FLYME_PUBLISH_FALG = "ro.flyme.published";
    // OPPO标识
    private final String KEY_OPPO_PROP = "ro.build.version.opporom";
    private final String KEY_OPPO_VERSION = "ro.oppo.theme.version"; // "703"
    private final String KEY_OPPO_THEME_VERSION = "ro.oppo.version"; // ""
    private final String KEY_OPPO_ROM_VERSION = "ro.rom.different.version"; // "ColorOS2.1"
    // VIVO标识
    private final String KEY_VIVO_PROP = "ro.vivo.os.version";
    private final String KEY_VIVO_BOARD_VERSION = "ro.vivo.board.version"; // "MD"
    private final String KEY_VIVO_OS_NAME = "ro.vivo.os.name"; // "Funtouch"
    private final String KEY_VIVO_DISPLAY_ID = "ro.vivo.os.build.display.id"; // "FuntouchOS_3.0"
    private final String KEY_VIVO_ROM_VERSION = "ro.vivo.rom.version"; // "rom_3.1"
    // 联想标识
    private final String KEY_LENOVO_DEVICE = "ro.lenovo.device"; // "phone"
    private final String KEY_LENOVO_PLATFORM = "ro.lenovo.platform"; // "qualcomm"
    private final String KEY_LENOVO_ADB = "ro.lenovo.adb"; // "apkctl,speedup"

    // 各大应用市场包名
    private final String PACKAGE_NAME_XM = "com.xiaomi.market";
    private final String PACKAGE_NAME_HW = "com.huawei.appmarket";
    private final String PACKAGE_NAME_YYB = "com.tencent.android.qqdownloader";
    private final String PACKAGE_NAME_360 = "com.qihoo.appstore";
    private final String PACKAGE_NAME_MZ = "com.meizu.mstore";
    private final String PACKAGE_NAME_VIVO = "com.bbk.appstore";
    private final String PACKAGE_NAME_OPPO = "com.oppo.market";
    private final String PACKAGE_NAME_LX = "com.lenovo.leos.appstore";

    public boolean invokeMarket(Context context) {
        if(null == context){
            return false;
        }
        if(!PermissionUtil.build().checkPermission(context, PermissionUtil.READ_EXTERNAL_STORAGE)){
            return false;
        }
        if(!PermissionUtil.build().checkPermission(context, PermissionUtil.WRITE_EXTERNAL_STORAGE)){
            return false;
        }
        ROM_TYPE rom_type = getRomType();
        if(rom_type == ROM_TYPE.OTHER){
            return false;
        }
        String marketPkg = "";
        if(rom_type == ROM_TYPE.MIUI){
            marketPkg = PACKAGE_NAME_XM;
        }else if(rom_type == ROM_TYPE.EMUI){
            marketPkg = PACKAGE_NAME_HW;
        }else if(rom_type == ROM_TYPE.FLYME){
            marketPkg = PACKAGE_NAME_MZ;
        }else if(rom_type == ROM_TYPE.LX){
            marketPkg = PACKAGE_NAME_LX;
        }else if(rom_type == ROM_TYPE.OPPO){
            marketPkg = PACKAGE_NAME_OPPO;
        }else if(rom_type == ROM_TYPE.VIVO){
            marketPkg = PACKAGE_NAME_VIVO;
        }else if(getAppPkgName(context, PACKAGE_NAME_YYB)){
            marketPkg = PACKAGE_NAME_YYB;
        }else if(getAppPkgName(context, PACKAGE_NAME_360)){
            marketPkg = PACKAGE_NAME_360;
        }
        if(StringUtil.isEmpty(marketPkg)){
            return false;
        }
        try {
            Uri uri = Uri.parse("market://details?id=" + JJShopApplication.sPackageName);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage(marketPkg);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            JjLog.e("异常 = " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public ROM_TYPE getRomType() {
        ROM_TYPE rom_type = ROM_TYPE.OTHER;
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){// android O版本及以上
                // 华为
                if (!StringUtil.isEmpty(getSystemProperty(KEY_EMUI_VERSION_CODE)) ||
                        !StringUtil.isEmpty(getSystemProperty(KEY_EMUI_API_LEVEL)) ||
                        !StringUtil.isEmpty(getSystemProperty(KEY_EMUI_CONFIG_HW_SYS_VERSION))) {
                    return ROM_TYPE.EMUI;
                }
                // 小米
                if (!StringUtil.isEmpty(getSystemProperty(KEY_MIUI_VERSION_CODE)) ||
                        !StringUtil.isEmpty(getSystemProperty(KEY_MIUI_VERSION_NAME)) ||
                        !StringUtil.isEmpty(getSystemProperty(KEY_MIUI_INTERNAL_STORAGE))) {
                    return ROM_TYPE.MIUI;
                }
                // 魅族
                if (!StringUtil.isEmpty(getSystemProperty(KEY_FLYME_ICON_FALG)) ||
                        !StringUtil.isEmpty(getSystemProperty(KEY_FLYME_SETUP_FALG)) ||
                        !StringUtil.isEmpty(getSystemProperty(KEY_FLYME_PUBLISH_FALG))) {
                    return ROM_TYPE.FLYME;
                }
                // OPPO
                if (!StringUtil.isEmpty(getSystemProperty(KEY_OPPO_PROP)) ||
                        !StringUtil.isEmpty(getSystemProperty(KEY_OPPO_VERSION)) ||
                        !StringUtil.isEmpty(getSystemProperty(KEY_OPPO_THEME_VERSION)) ||
                        !StringUtil.isEmpty(getSystemProperty(KEY_OPPO_ROM_VERSION))) {
                    return ROM_TYPE.OPPO;
                }
                // VIVO
                if (!StringUtil.isEmpty(getSystemProperty(KEY_VIVO_PROP)) ||
                        !StringUtil.isEmpty(getSystemProperty(KEY_VIVO_BOARD_VERSION)) ||
                        !StringUtil.isEmpty(getSystemProperty(KEY_VIVO_OS_NAME)) ||
                        !StringUtil.isEmpty(getSystemProperty(KEY_VIVO_DISPLAY_ID)) ||
                        !StringUtil.isEmpty(getSystemProperty(KEY_VIVO_ROM_VERSION))) {
                    return ROM_TYPE.VIVO;
                }
                // 联想
                if (!StringUtil.isEmpty(getSystemProperty(KEY_LENOVO_DEVICE)) ||
                        !StringUtil.isEmpty(getSystemProperty(KEY_LENOVO_PLATFORM)) ||
                        !StringUtil.isEmpty(getSystemProperty(KEY_LENOVO_ADB))) {
                    return ROM_TYPE.LX;
                }
                return rom_type;
            }
            // 8.0以下版本
            BuildProperties buildProperties = BuildProperties.newInstance();
            if(null == buildProperties){
                return rom_type;
            }
            // 华为
            if (buildProperties.containsKey(KEY_EMUI_VERSION_CODE) ||
                    buildProperties.containsKey(KEY_EMUI_API_LEVEL) ||
                    buildProperties.containsKey(KEY_EMUI_CONFIG_HW_SYS_VERSION)) {
                return ROM_TYPE.EMUI;
            }
            // 小米
            if (buildProperties.containsKey(KEY_MIUI_VERSION_CODE) ||
                    buildProperties.containsKey(KEY_MIUI_VERSION_NAME) ||
                    buildProperties.containsKey(KEY_MIUI_INTERNAL_STORAGE)) {
                return ROM_TYPE.MIUI;
            }
            // 魅族
            if (buildProperties.containsKey(KEY_FLYME_ICON_FALG) ||
                    buildProperties.containsKey(KEY_FLYME_SETUP_FALG) ||
                    buildProperties.containsKey(KEY_FLYME_PUBLISH_FALG)) {
                return ROM_TYPE.FLYME;
            }
            // OPPO
            if (buildProperties.containsKey(KEY_OPPO_PROP) ||
                    buildProperties.containsKey(KEY_OPPO_VERSION) ||
                    buildProperties.containsKey(KEY_OPPO_THEME_VERSION) ||
                    buildProperties.containsKey(KEY_OPPO_ROM_VERSION)) {
                return ROM_TYPE.OPPO;
            }
            // VIVO
            if (buildProperties.containsKey(KEY_VIVO_PROP) ||
                    buildProperties.containsKey(KEY_VIVO_BOARD_VERSION) ||
                    buildProperties.containsKey(KEY_VIVO_OS_NAME) ||
                    buildProperties.containsKey(KEY_VIVO_DISPLAY_ID) ||
                    buildProperties.containsKey(KEY_VIVO_ROM_VERSION)) {
                return ROM_TYPE.VIVO;
            }
            // 联想
            if (buildProperties.containsKey(KEY_LENOVO_DEVICE) ||
                    buildProperties.containsKey(KEY_LENOVO_PLATFORM) ||
                    buildProperties.containsKey(KEY_LENOVO_ADB)) {
                return ROM_TYPE.LX;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rom_type;
    }

    /**
     * 通过映射来获取rom信息
     * @param key
     * @return
     */
    private String getSystemProperty(String key) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            return (String) get.invoke(clz, key, "");
        } catch (Exception e) {
        }
        return "";
    }

    public enum ROM_TYPE {
        MIUI,// 小米
        FLYME,// 魅族
        EMUI,// 华为
        OPPO,// oppo
        VIVO,// vivo
        LX,// 联想
        OTHER// 其他的
    }

    /**
     * 判断是否包含这个包名
     * @param context
     * @param pkgName
     * @return
     */
    public boolean getAppPkgName(Context context, String pkgName) {
        if(null == context || StringUtil.isEmpty(pkgName)){
            return false;
        }
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        if(null == packages || packages.size() <= 0){
            return false;
        }
        String name = "", pName = "";
        for(int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if(null == packageInfo){
                break;
            }
            name = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
            pName = packageInfo.packageName;
            JjLog.e("程序名称 = " + name + "，程序包名 = " + pName);
            if(pkgName.equals(pName)){
                return true;
            }
        }
        return false;
    }

}
