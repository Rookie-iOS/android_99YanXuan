package com.jjshop.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.jjshop.app.JJShopApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * Created by JJGCW on 2017/8/3.
 */

public class FileUtilLM {

    private static final String ROOT_DIR = "jjdance";





    /**
     * 判断sd卡是否可以用
     *
     * @return
     */
    private static boolean isSDAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) ? true : false;
    }

    /**
     * 获取到手机内存的目录
     */
    private static String getDataDir(String string) {
        // data/data/包名/cache
        String path = JJShopApplication.getContext().getCacheDir()
                .getAbsolutePath()
                + File.separator + string;
        File file = new File(path);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return file.getAbsolutePath();
            } else {
                return "";
            }
        }
        return file.getAbsolutePath();
    }

    /**
     * 获取到sd卡的目录
     *
     * @param key_dir
     * @return
     */
    private static String getSDDir(String key_dir) {
        StringBuilder sb = new StringBuilder();
        String absolutePath = getExternalStorageDirectory()
                .getAbsolutePath();// /mnt/sdcard
        sb.append(absolutePath);
        sb.append(File.separator).append(ROOT_DIR).append(File.separator)
                .append(key_dir);

        String filePath = sb.toString();
        File file = new File(filePath);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return file.getAbsolutePath();
            } else {
                return "";
            }
        }

        return file.getAbsolutePath();
    }

    /**
     * 删除文件或目录
     */
    public static void deleteAll(File file) {
        if (file.isFile() || file.list().length == 0) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteAll(files[i]);
                files[i].delete();
            }

            if (file.exists()) { // 如果文件本身就是目录 ，就要删除目�?
                file.delete();
            }
        }
    }

    /**
     * 读取指定路径缓存
     * /F
     */
    public static String readCache(String filePath) {
        String cache = null;
        File cacheFile = new File(filePath);
        if (cacheFile.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(filePath));
                StringBuilder sb = new StringBuilder();
                String temp;
                while ((temp = reader.readLine()) != null) {
                    sb.append(temp);
                }
                cache = sb.toString();
                reader.close();
            } catch (Exception e) {
//                XgoLog.e(e);
            }


            if (TextUtils.isEmpty(cache)) {
//                XgoLog.d("***NOT CACHE***");
            } else {
//                XgoLog.d("***READ CACHE***\r\n" + cache);
            }
        }
        return cache;
    }


    /**
     * 将缓存写入指定文件
     */
    public static Boolean writeCache(String filePath, String cache) {
//        XgoLog.d("writeCache::" + cache);
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(cache.toCharArray());
            writer.flush();
            writer.close();
            return true;
        } catch (Exception e) {
        }
        return false;
    }


    public static String getCacheSize() {
        long cacheSize = getFolderSize(new File(getDataDir("")));
        long externalCacheSize = 0;
        if (isSDAvailable()) {
            externalCacheSize = getFolderSize(new File(getSDDir("")));
        }

        return getFormatSize(cacheSize + externalCacheSize);
    }


    /**
     * 获取文件大小
     *
     * @param file
     * @return
     */
    public static long getFolderSize(File file) {
        long size = 0;
        if (file != null && file.exists()) {
            try {
                File[] fileList = file.listFiles();
                for (int i = 0; i < fileList.length; i++) {
                    // 如果下面还有文件
                    if (fileList[i].isDirectory()) {
                        size = size + getFolderSize(fileList[i]);
                    } else {
                        size = size + fileList[i].length();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    /**
     * 格式化文件大小
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        if (0 == size) {
            return "0K";
        }

        double kiloByte = size / 1024;
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .stripTrailingZeros().toPlainString()
                    + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .stripTrailingZeros().toPlainString()
                    + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .stripTrailingZeros().toPlainString()
                    + "G";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP)
                .stripTrailingZeros().toPlainString()
                + "T";
    }

    public static String getSDFile() {
        String absolutePath = getExternalStorageDirectory()
                .getAbsolutePath();// /mnt/sdcard
        return absolutePath;
    }

    private FileUtilLM(){}

    private static volatile FileUtilLM fileUtilLM;

    public static FileUtilLM build(){
        if(null == fileUtilLM){
            synchronized (FileUtilLM.class){
                if(null == fileUtilLM){
                    fileUtilLM = new FileUtilLM();
                }
            }
        }
        return fileUtilLM;
    }

    // 城市文件的路径
    private final String CITY_PATH = "jjshopcitys";
    // 城市文件的名称
    private final String CITY_FILENAME = "jjshopcitys.txt";

    public boolean saveCitys(String data){
        return createFile(CITY_PATH, CITY_FILENAME, data);
    }

    public String readCitys(){
        return readData(CITY_PATH, CITY_FILENAME);
    }

    /**
     * 获取上传所保存的临时图片文件
     *
     * @return
     */
    public boolean createFile(String path, String fileName, String data) {
        if(!isSDAvailable() || StringUtil.isEmpty(data)){
            return false;
        }
        JjLog.e("需要写入的数据1 = " + data);
        deletePicTempFile(path, fileName);
        File dir = new File(Environment.getExternalStorageDirectory(), path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(data);
            osw.flush();
            osw.close();
            fos.close();
            return true;
        }catch (Exception e){
            JjLog.e("写入数据失败 = " + e.getMessage());
        }
        return false;
    }

    /**
     * 删除文件夹
     */
    public void deletePicTempFile(String path, String fileName) {
        if(!isSDAvailable()){
            return ;
        }
        File dir = new File(Environment.getExternalStorageDirectory(), path);
        File picTempFile = new File(dir, fileName);
        if (picTempFile.exists()) {
            picTempFile.delete();
        }
    }

    public String readData(String path, String fileName){
        if(!isSDAvailable()){
            return "";
        }
        File dir = new File(Environment.getExternalStorageDirectory(), path);
        if(null == dir){
            return "";
        }
        File file = new File(dir, fileName);
        if(null == file){
            return "";
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            if(null == fis){
                return "";
            }
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            if(null == isr){
                return "";
            }
            BufferedReader reader = new BufferedReader(isr);
            String line;
            String fileContent = "";
            while ((line = reader.readLine()) != null) {
                fileContent += line;
            }
            isr.close();
            fis.close();
            reader.close();
            return fileContent;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    /**
     * Uri转File
     * @param uri
     * @param context
     * @return
     */
    public static File getUriToFile(Uri uri, Context context) {
        if(null == uri || null == context){
            return null;
        }
        String path = null;
        try{
            if ("file".equals(uri.getScheme())) {
                path = uri.getEncodedPath();
                if (path != null) {
                    path = Uri.decode(path);
                    ContentResolver cr = context.getContentResolver();
                    StringBuffer buff = new StringBuffer();
                    buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                    Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA }, buff.toString(), null, null);
                    int dataIdx = 0;
                    for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                        dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        path = cur.getString(dataIdx);
                    }
                    cur.close();
                    return new File(path);
                }
            } else if ("content".equals(uri.getScheme())) {
                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    path = cursor.getString(columnIndex);
                }
                cursor.close();
                if (path != null) {
                    return new File(path);
                }
            }
        }catch (Exception e){
            JjLog.e("uri转file = " + e.getMessage());
        }

        return null;
    }
}
