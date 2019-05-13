package com.jjshop.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.jjshop.app.JJShopApplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Colin on 2015/11/25.
 */
public class StringUtil {

    /**
     * 判断图片地址是否是正确的
     */
    public static boolean isImage(String imgUrl) {
        if(StringUtil.isEmpty(imgUrl)){
            return false;
        }
        if(!imgUrl.contains("http://") && !imgUrl.contains("https://")){
            return false;
        }
        if(!imgUrl.contains(".jpg") && !imgUrl.contains(".jpeg") && !imgUrl.contains(".png")){
            return false;
        }
        return true;
    }

    /**
     * 判断SD卡是否可用
     */
    public static boolean isSDcardOK() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 强制隐藏键盘
     *
     * @param v
     */
    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    /***
     * 显示虚拟键盘
     *
     * @param v
     */
    public static void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }


    /***
     * 解决TextView错乱问题
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }


    /***
     * 图片转Base64
     *
     * @param imageData
     * @return
     */
    public static String translate(byte[] imageData) {
        String body = Base64.encodeToString(imageData, Base64.DEFAULT);
        return body;
    }

    /***
     * Base64转图片
     *
     * @param body
     * @return
     */
    public static Bitmap getImage(String body) {
        byte[] imageData = Base64.decode(body, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        return bitmap;
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getDate() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//可以方便地修改日期格式
        String dateText = dateFormat.format(date);
        return dateText;
    }

    /**
     * 获得格式化之后的系统时间
     *
     * @return
     */
    public static String dateToString(Date date) {
        if(null == date){
            date = new Date();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /***
     * 字符串非空验证
     *
     * @param result
     * @return
     */
    public static boolean isNull(String result) {
        if (result == null || "".equals(result) || result.length() <= 0 || result.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 返回当前程序版本
     */
    public static String getAppVersionName(Context context) {
        return JJShopApplication.sVersion;
    }

    public static String transEndoing(Context context, String urlStr) {
        try {
            urlStr = java.net.URLEncoder.encode(PreUtils.getString(context, "_uauth", ""), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlStr;
    }

    /***
     * MD5加密
     *
     * @param plainText
     * @return
     */
    public static String textToMD5L32(String plainText) {
        String result = "";
        //首先判断是否为空
        if (isNull(plainText)) {
            return result;
        }
        try {
            //首先进行实例化和初始化
            MessageDigest md = MessageDigest.getInstance("MD5");
            //得到一个操作系统默认的字节编码格式的字节数组
            byte[] btInput = plainText.getBytes();
            //对得到的字节数组进行处理
            md.update(btInput);
            //进行哈希计算并返回结果
            byte[] btResult = md.digest();
            //进行哈希计算后得到的数据的长度
            StringBuffer sb = new StringBuffer();
            for (byte b : btResult) {
                int bt = b & 0xff;
                if (bt < 16) {
                    sb.append(0);
                }
                sb.append(Integer.toHexString(bt));
            }
            result = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }


    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if(null == str || str.equals("") || str.equals("null")){
            return true;
        }
        return false;
    }

    public static double getPrice(double price){
        BigDecimal bg = new BigDecimal(price);
        // 仅保留有效位
        price = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        // 使用0.00不足位补0，例如1.2 会变成 1.20
//        DecimalFormat df = new DecimalFormat("0.00");
//        price = Double.parseDouble(df.format(price));
        return price;
    }

    /**
     * 通过字符分割字符串
     * @param content
     * @param regex
     * @return
     */
    public static String[] strToArray(String content, String regex){
        if(StringUtil.isEmpty(content)){
            return null;
        }
        return content.split(regex);
    }

}
