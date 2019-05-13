package com.jjshop.utils.glide_img;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jjshop.R;
import com.jjshop.utils.JjLog;

/**
 * 图片加载工具类
 */

public class GlideUtil {

    private static volatile GlideUtil glideUtil = null;

    private GlideUtil(){}

    public static GlideUtil getInstence(){
        if(null == glideUtil){
            synchronized (GlideUtil.class){
                if(null == glideUtil){
                    glideUtil = new GlideUtil();
                }
            }
        }
        return glideUtil;
    }

    // 默认图片
    public final int DEFAULT_IMG = R.drawable.img_default_zhijiao;

    private final int LODING_IMG_NO_FIX = -4;// 正常加载， 不进行fixxy  centerCrop
    private final int LODING_IMG = -1;// 正常加载
    private final int LODING_ROUND_IMG = -2;// 加载圆角
    private final int LODING_CIRLE_IMG = -3;// 加载圆形
    private final int ROUND_SIZE = 6;// 默认的圆角大小



    public void loadImage(Context context, final ImageView imageView, String url) {
        loadImage(context, imageView, url, DEFAULT_IMG, LODING_IMG);
    }

    public void loadImageNoFix(Context context, final ImageView imageView, String url) {
        loadImage(context, imageView, url, DEFAULT_IMG, LODING_IMG_NO_FIX);
    }

    /**
     * 加载正常图片
     * @param context           上下文
     * @param imageView         显示图片的imageView
     * @param url               图片地址
     * @param defaultImageRid   默认图
     */
    public void loadImage(Context context, final ImageView imageView, String url, int defaultImageRid, int type) {
        glide(context, imageView, url, defaultImageRid, type, -1);
    }




    // 四个角全部圆角、圆角大小是6
    public void loadRoundImage (Context context, ImageView imageView, String url){
        loadRoundImage(context, imageView, url, DEFAULT_IMG, ROUND_SIZE,
                RoundedCornersTransformation.CornerType.ALL);
    }

    // 四个角全部圆角、圆角大小可以任意设置
    public void loadRoundImage (Context context, ImageView imageView, String url, int radius){
        loadRoundImage(context, imageView, url, DEFAULT_IMG, radius,
                RoundedCornersTransformation.CornerType.ALL);
    }

    // 设置任意一个角是圆角、圆角大小默认是6
    public void loadRoundImage (Context context, ImageView imageView, String url,
                                RoundedCornersTransformation.CornerType cornerType){
        loadRoundImage(context, imageView, url, DEFAULT_IMG, ROUND_SIZE, cornerType);
    }

    // 设置任意一个角是圆角
    public void loadRoundImage (Context context, ImageView imageView, String url, int radius,
                                RoundedCornersTransformation.CornerType cornerType){
        loadRoundImage(context, imageView, url, DEFAULT_IMG, radius, cornerType);
    }

    /**
     * 加载圆角图片
     * @param context           上下文
     * @param imageView         显示图片的imageView
     * @param url               图片地址
     * @param defaultImageRid   默认图
     * @param radius            圆角大小(dp)
     * @param cornerType        圆角位置（ALL  TOP  BOTTOM 等）
     */
    public void loadRoundImage(Context context, final ImageView imageView, String url,
                               int defaultImageRid, int radius, RoundedCornersTransformation.CornerType cornerType) {
        glide(context, imageView, url, defaultImageRid, LODING_ROUND_IMG, radius, cornerType);
    }




    public void loadCirleImage(Context context, final ImageView imageView, String url) {
        loadCirleImage(context, imageView, url, DEFAULT_IMG);
    }
    /**
     * 加载圆形图片
     * @param context           上下文
     * @param imageView         显示图片的imageView
     * @param url               图片地址
     * @param defaultImageRid   默认图
     */
    public void loadCirleImage(Context context, final ImageView imageView, String url, int defaultImageRid) {
        glide(context, imageView, url, defaultImageRid, LODING_CIRLE_IMG, -1);
    }



    // 最终执行加载图片的方法
    private void glide(Context context, final ImageView imageView, String url, int defaultImageRid,
                       int type, int radius){
        glide(context, imageView, url, defaultImageRid, type, radius,
                RoundedCornersTransformation.CornerType.ALL);
    }

    /**
     * 最终执行加载图片的方法
     * @param context           上下文
     * @param imageView         显示图片的ImageView
     * @param url               图片地址
     * @param defaultImageRid   默认图片
     * @param type              直角、圆角、圆形
     * @param radius            圆角大小
     * @param cornerType        圆角的位置（ALL TOP BOTTOM等）
     */
    private void glide(Context context, final ImageView imageView, String url, int defaultImageRid,
                       int type, int radius, RoundedCornersTransformation.CornerType cornerType){
        if (null == context || null == imageView){
            return;
        }

        if (context instanceof Activity && ((Activity)context).isFinishing()) {
            return;
        }
//        JjLog.e("图片地址 = " + url);
        if(type == LODING_ROUND_IMG || type == LODING_IMG){
            if(type == LODING_ROUND_IMG){
                // 圆角大小
                radius = (int) (Resources.getSystem().getDisplayMetrics().density * radius);
            }
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        Glide.with(context)
                .load(url)
                .apply(getOptions(defaultImageRid, type, radius, cornerType))
                .thumbnail(0.5f)
                .into(imageView);
    }

    /**
     * 配置加载图片的样式
     * @param defaultImageRid   默认图片
     * @param type              直角、圆角、圆形
     * @param radius            圆角大小
     * @param cornerType        圆角的位置（ALL TOP BOTTOM等）
     * @return
     */
    private RequestOptions getOptions(int defaultImageRid, int type, int radius, RoundedCornersTransformation.CornerType cornerType){
        RequestOptions options = new RequestOptions();
        options.placeholder(defaultImageRid);// 占位图
        options.error(defaultImageRid);// 错误图
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);// 只缓存转化后的图片
        if(type == LODING_ROUND_IMG){// 圆角
            options.transforms(new CenterCrop(),
                    new RoundedCornersTransformation(radius, 0, cornerType));
        }
        if(type == LODING_CIRLE_IMG){// 圆形
            options.transforms(new CenterCrop(), new CircleCrop());
        }
        if(type == LODING_IMG){// 直角
            options.centerCrop();
        }
        return options;
    }

    /**
     * 清除图片缓存
     */
    public void clearImgCache(Context context){
        if(null == context){
            return;
        }
        // 清除内存缓存.
        Glide.get(context).clearMemory();
        clearDiskCache(context);
    }

    // 清除磁盘缓存.
    private void clearDiskCache(final Context context){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                // This method must be called on a background thread.
                Glide.get(context).clearDiskCache();
                return null;
            }
        };
    }

}
