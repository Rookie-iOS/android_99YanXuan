package com.jjshop.utils.glide_img;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

/**
 * 作者：张国庆
 * 时间：2018/7/23
 */

public class GlideModelConfig  implements GlideModule {

    private final static int MAX_MEMORY = 5 * 1024 * 1024;

    private final static double BITMAP_CACHESIZE = 1.2;

    /**
     * 单位换算
     */
    private static final int MB = 1024 * 1024;
    /**
     * 文件缓存最大值(单位 M)
     */
    private static final int IMG_CACHE_MAX_SIZE = 100;

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).build();
        builder.setMemoryCache(new LruResourceCache(MAX_MEMORY));
        builder.setBitmapPool(new LruBitmapPool((int) (calculator.getBitmapPoolSize() * BITMAP_CACHESIZE)));
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, IMG_CACHE_MAX_SIZE * MB));//内部磁盘缓存
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, IMG_CACHE_MAX_SIZE * MB));//磁盘缓存到外部存储
//        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
//        builder.setDiskCache(new DiskCache.Factory() {
//            @Override
//            public DiskCache build() {
//                File file = FileUtil.instance().getImgDir();
//                return DiskLruCacheWrapper.get(file, IMG_CACHE_MAX_SIZE * MB);
//            }
//        });
    }
}
