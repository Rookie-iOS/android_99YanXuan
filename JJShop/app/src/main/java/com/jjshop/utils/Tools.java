package com.jjshop.utils;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.jjshop.utils.httputil.HttpUrl;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * 工具类
 */
public class Tools {

    //判断是否安装了微信
    public static boolean isWeixinAvilible(Context context) {
        if(null == context || !(context instanceof Activity)){
            return false;
        }
        boolean b = UMShareAPI.get(context).isInstall((Activity) context, SHARE_MEDIA.WEIXIN);
        return b;
    }

    /**
     * 计算播放时间
     * @param time	倒计时的时间戳
     * @return
     */
    public static String getSmh(long time) {
        return getSmh(time, false);
    }

    /**
     * 倒计时
     * @param time
     * @param isSecond 是否只显示秒
     * @return
     */
    public static String getSmh(long time, boolean isSecond) {
        if(time <= 0){
            if(isSecond){
                return "00";
            }
            return "00:00:00";
        }
        long hour = 0;
        long minute = 0;
        long second = 0;
        String strHour = null;
        String strMinute;
        String strSecond;
        if(time >= 3600){
            hour = time / 3600;
            minute = time % 3600 / 60;
            second = time % 3600 % 60;
        }else if (time >= 60) {
            minute = time / 60;
            second = time % 60;
        }else if (time < 60) {
            minute = 0;
            second = time;
        }
        //判断当前小时数是否在10以内，在的话需要在分钟前加一个0
        if(hour > 0){
            strHour = hour < 10 ? ("0" + hour + ":") : String.valueOf(hour) + ":";
        }
        //判断当前分钟数是否在10以内，在的话需要在分钟前加一个0
        strMinute = minute < 10 ? ("0" + minute) : String.valueOf(minute);
        //判断当前秒数是否在10以内，在的话需要在秒数前加一个0
        strSecond = second < 10 ? ("0" + second) : String.valueOf(second);
        // 小时为空时赋值空字符串
        if(null == strHour || strHour.isEmpty()){
            strHour = "00:";
        }
        if(isSecond){
            return strSecond;
        }
        return strHour + strMinute + ":" + strSecond;
    }
    /**
     * 倒计时(商品详情)
     * @param time  时间戳（秒）
     * @return
     */
    public static String goodsDetailsDaojishi(long time) {
        if(time <= 0){
            return "00秒";
        }
        long timeMin = 60;
        long timeHour = 3600;
        long timeDay = 3600 * 24;
        long day = 0;
        long hour = 0;
        long minute = 0;
        long second = 0;
        String strDay = "";
        String strHour = "";
        String strMinute = "";
        String strSecond = "00秒";
        if(time >= timeDay){
            day = time / timeDay;
            hour = time % timeDay / timeHour;
            minute = time % timeDay % timeHour / timeMin;
            second = time % timeDay % timeHour % timeMin;
        }else if(time >= 3600){
            hour = time / 3600;
            minute = time % 3600 / 60;
            second = time % 3600 % 60;
        }else if (time >= 60) {
            minute = time / 60;
            second = time % 60;
        }else if (time < 60) {
            minute = 0;
            second = time;
        }
        if(day > 0){
            strDay = day + "天";
        }
        //判断当前小时数是否在10以内，在的话需要在分钟前加一个0
        if(hour > 0){
            strHour = (hour < 10 ? ("0" + hour) : hour)  + "小时";
        }else if(day > 0){// 天大于0，小时为0 ，显示00小时
            strHour = "00小时";
        }
        //判断当前分钟数是否在10以内，在的话需要在分钟前加一个0
        if(minute > 0){
            strMinute = (minute < 10 ? ("0" + minute) : minute) + "分钟";
        }else if(hour > 0){// 小时大于0，分钟为0 ，显示00分钟
            strMinute = "00分钟";
        }
        //判断当前秒数是否在10以内，在的话需要在秒数前加一个0
        if(second > 0){
            strSecond = (second < 10 ? ("0" + second) : second) + "秒";
        }

        return strDay + strHour + strMinute + strSecond;
    }

    /**
     * 设置下拉刷新的突变颜色
     * @param refreshLayout
     */
    public static void setRefreshTopColor(SwipeRefreshLayout refreshLayout){
        refreshLayout.setColorSchemeColors(Color.parseColor("#FF4081"),Color.parseColor("#303F9F"));
    }

    /**
     * 复制内容
     * @param context
     * @param strContent
     */
    public static void clipContent(Context context, String strContent){
        if(null == context){
            return;
        }
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(strContent);
    }

    /**
     * 获取剪切板的内容
     * @param context
     */
    public static String getClipContent(Context context){
        if(null == context){
            return "";
        }
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        if(null == cm){
            return "";
        }
        ClipData data = cm.getPrimaryClip();
        if(null == data || data.getItemCount() <= 0){
            return "";
        }
        ClipData.Item item = data.getItemAt(0);
        if(null == item || null == item.getText()){
            return "";
        }
        String content = item.getText().toString();
        if(StringUtil.isEmpty(content)){
            return "";
        }
        return content;
    }

    /**
     * 显示fragment
     * @param id
     * @param fm
     * @param fragment
     * @param back
     */
    public static void addFrament(int id, FragmentManager fm, Fragment fragment, boolean back) {
        if(null == fm || null == fragment){
            return;
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (back)
            ft.addToBackStack(null);
        // ft.setCustomAnimations(R.anim.fragment_slide_left_enter,
        // R.anim.fragment_slide_left_exit);
        ft.add(id, fragment).commitAllowingStateLoss();
    }

    /**
     * back时pop栈操作
     *
     * @param fm
     */
    public static boolean back(FragmentManager fm) {
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否有网络
     *
     * @param context
     * @return
     */
    public static boolean isNetConnected(Context context) {
        if (context == null)
            return false;
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;

    }

    /**
     * 隐藏键盘
     */
    public static void hideSoftInput(View view) {
        if(null == view || null == view.getContext()){
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 强制显示键盘（配合定时器 Timer使用）
     */
    public static void showSoftInput(View view) {
        if(null == view || null == view.getContext()){
            return;
        }
        InputMethodManager imm = ( InputMethodManager ) view.getContext().getSystemService( Context.INPUT_METHOD_SERVICE );
        if(null == imm){
            return;
        }
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 判断是否是手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern.compile("^((1[1-9]))\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 通过秒来换算是几天
     * @param l     秒
     * @return
     */
    public static int getDay(long l){
        return (int) (l / (3600 * 24));
    }

    /**
     * 解析剪切板的内容,获取口令
     * @param context
     * @return
     */
    public static String resolveClipContent(Context context){
        String content = getClipContent(context);
        if(StringUtil.isEmpty(content)){
            return "";
        }
        JjLog.e("解析剪切板的内容 = " + content);
        if(!content.contains(HttpUrl.KOULING_URL)){
            return "";
        }
        if(!(content.contains("<") && content.contains(">"))){
            return "";
        }
        // 通过正则获取尖括号里面的值
        Pattern p1 = Pattern.compile("(<(.*?)>)");
        if(null == p1){
            return "";
        }
        Matcher m = p1.matcher(content);
        if(null == m){
            return "";
        }
        if(m.find()){
            int size = m.groupCount();
            JjLog.e("长度 = " + size);
            if(size <= 0){
                return "";
            }
            String con = m.group(size);
            // 清空剪切板
            Tools.clipContent(context, "");
            JjLog.e("尖括号里面的值 = " + con);
            return con;
        }
        return "";
    }

    /**
     * 获取系统相册路径
     * @return
     */
    public static String systemAlumb(){
        String path = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                +File.separator + "Camera" + File.separator;
        return path;
    }

    /**
     * 通过某一个字符分割字符串
     * @param content
     * @param split
     * @return
     */
    public static List<String> getSplit(String content, String split){
        if(StringUtil.isEmpty(content) || StringUtil.isEmpty(split)){
            return null;
        }
        List<String> list = new ArrayList<>();
        if(!content.contains(split)){
            list.add(content);
            return list;
        }
        String[] str = content.split(split);
        if(null == str || str.length <= 0){
            list.add(content);
            return list;
        }
        list.addAll(Arrays.asList(str));
        return list;
    }

}