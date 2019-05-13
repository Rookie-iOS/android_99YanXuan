package com.jjshop.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.jjshop.ui.activity.person.RefundActivity;
import com.qingmei2.rximagepicker.core.RxImagePicker;
import com.qingmei2.rximagepicker.entity.Result;
import com.qingmei2.rximagepicker.ui.SystemImagePicker;

import java.io.File;
import io.reactivex.functions.Consumer;

/**
 * 作者：张国庆
 * 时间：2018/8/1
 */

public class UploadImgUtil {
    private static volatile UploadImgUtil imgUtil;
    private UploadImgUtil(){}

    public static UploadImgUtil build(){
        if(null == imgUtil){
            synchronized (UploadImgUtil.class){
                if(null == imgUtil){
                    imgUtil = new UploadImgUtil();
                }
            }
        }
        return imgUtil;
    }

    /** 初始化默认相册，相机选择器 (onCreate方法中调用)*/
    public SystemImagePicker getSystemImagePicker(){
        return RxImagePicker.INSTANCE.create();
    }

    /** 打开系统相机 （需要先调用getDefaultImagePicker（）方法，初始化选择器）
     * imagePicker      默认选择器
     * context          上下文
     * */
    public void openCamera(SystemImagePicker imagePicker, final Context context){
        if(null == imagePicker || null == context){
            return;
        }
        if(!PermissionUtil.build().checkPermission(context, PermissionUtil.CAMERA)){
            return;
        }
        if(!PermissionUtil.build().checkPermission(context, PermissionUtil.WRITE_EXTERNAL_STORAGE)){
            return;
        }
        if(!PermissionUtil.build().checkPermission(context, PermissionUtil.READ_EXTERNAL_STORAGE)){
            return;
        }
        imagePicker.openCamera(context).subscribe(new Consumer<Result>() {
            @Override
            public void accept(Result result) throws Exception {
                Uri uri = result.getUri();
                // 对图片进行处理，比如加载到ImageView中
                UploadImgUtil.build().cropImg((Activity) context, uri,
                        512, 512, UploadImgUtil.CROP_REQUEST_CODE);
            }
        });
    }

    /** 打开系统相册 （需要先调用getDefaultImagePicker（）方法，初始化选择器）
     * imagePicker      默认选择器
     * context          上下文
     * */
    public void openDefaultAlbum(SystemImagePicker imagePicker, final Context context){
        if(null == imagePicker || null == context){
            return;
        }
        if(!PermissionUtil.build().checkPermission(context, PermissionUtil.WRITE_EXTERNAL_STORAGE)){
            return;
        }
        if(!PermissionUtil.build().checkPermission(context, PermissionUtil.READ_EXTERNAL_STORAGE)){
            return;
        }
        imagePicker.openGallery(context).subscribe(new Consumer<Result>() {
            @Override
            public void accept(Result result){
                if(null == result){
                    return;
                }
                try {
                    Uri uri = result.getUri();
                    if(context instanceof RefundActivity){
                        ((RefundActivity)context).getSelectImg(uri);
                        return;
                    }
                    UploadImgUtil.build().cropImg((Activity) context, uri,
                            512, 512, UploadImgUtil.CROP_REQUEST_CODE);
                }catch (Exception e){
                    JjLog.e("异常 = " + e.getMessage());
                }

            }
        });
    }

//    /** 多图片选择（需要先调用getZhihuImagePicker（）方法 初始化选择器）
//     *  imagePicker     选择器
//     *  context         上下文
//     * */
//    public void openZhihuAlbum(WechatImagePicker imagePicker, final FragmentActivity context){
//        imagePicker.openGallery().subscribe(new Consumer<Result>() {
//            @Override
//            public void accept(Result result) throws Exception {
//                // 选择多张会多次回调
//                JjLog.e("回调了");
//                Uri uri = result.getUri();
//            }
//        });
//    }
//
//
//    /**
//     * 初始化多图相册选择器
//     * @param activity      上下文
//     * @param maxSize       最大选择的图片张数
//     * @return
//     */
//    public WechatImagePicker getZhihuImagePicker(FragmentActivity activity, int maxSize){
//        maxSize = maxSize >= 9 ? 9 : maxSize;
//        WechatImagePicker imagePicker = new RxImagePicker.Builder()
//                .with(activity)
//                .addCustomGallery(		//注入夜间模式主题的UI
//                        WechatImagePicker.KEY_WECHAT_PICKER_ACTIVITY,
//                        WechatImagePickerActivity.class,
//                        new WechatConfigrationBuilder(MimeType.Companion.ofImage(), false)
//                                .maxSelectable(maxSize)	//最大可选图片数
//                                .spanCount(3)		//每行展示四张图片
//                                .countable(false)	//关闭计数模式
//                                .theme(R.style.Wechat)	//微信主题
//                                .build()
//                )
//                .build()
//                .create(WechatImagePicker.class);
//        return imagePicker;
//    }
//
//    private interface WechatImagePicker {
//        String KEY_WECHAT_PICKER_ACTIVITY = "key_wechat_picker";
//
//        @Gallery(viewKey = KEY_WECHAT_PICKER_ACTIVITY)
//        Observable<Result> openGallery();
//    }




    public static final int CROP_REQUEST_CODE = 101;

    /**
     * 调用系统裁剪
     * @param activity
     * @param uri              图片的Uri
     * @param outputX          裁剪之后的宽
     * @param outputY          裁剪之后的高
     * @param requestCode      回调Code
     */
    public void cropImg(Activity activity, Uri uri, int outputX, int outputY, int requestCode) {
        if (uri == null || null == activity) {
            return;
        }
        JjLog.e("进到这了");
        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
//                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        // 文件路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getPicTempFile()));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//设置输出格式
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);//设置宽高比例
        if (outputX != 0 && outputY != 0) {
            intent.putExtra("outputX", outputX);
            intent.putExtra("outputY", outputY);//设置裁剪后的长宽的像素值
        }
        intent.putExtra("return-data", false);//是否将数据保留在bitmap中返回
        intent.putExtra("circleCrop", false);//是否是圆形裁剪区域
        intent.putExtra("scale", true);//黑边
        intent.putExtra("scaleUpIfNeeded", true);//黑边
        activity.startActivityForResult(intent, requestCode);
    }

    // 裁剪之后图片的图片名称
    private final String PIC_TEMP_FILE = "pic_tmp.jpg";
    // 裁剪之后图片的文件夹名称
    private final String PIC_ADDRESS = "jjshop";

    /**
     * 获取上传所保存的临时图片文件
     *
     * @return
     */
    public File getPicTempFile() {
        File dir = new File(Environment.getExternalStorageDirectory(), PIC_ADDRESS);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File picTempFile = new File(dir, PIC_TEMP_FILE);
        return picTempFile;
    }

    /**
     * 删除上传所保存的临时图片文件
     */
    public void deletePicTempFile() {
        File dir = new File(Environment.getExternalStorageDirectory(), PIC_ADDRESS);
        File picTempFile = new File(dir, PIC_TEMP_FILE);
        if (picTempFile.exists()) {
            picTempFile.delete();
        }
    }

}
