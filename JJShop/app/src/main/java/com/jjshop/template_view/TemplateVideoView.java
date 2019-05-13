package com.jjshop.template_view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;

import com.jjshop.R;
import com.jjshop.ui.activity.home.DetailActivity;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.UIUtils;
import com.jjshop.utils.glide_img.GlideUtil;
import com.jjshop.widget.VideoJzvdView;

import cn.jzvd.Jzvd;

/**
 * 作者：张国庆
 * 时间：2018/7/17
 */

public class TemplateVideoView extends BaseTemplateView{

    private VideoJzvdView jzTextureView;

    public TemplateVideoView(Context context) {
        super(context);
    }

    public TemplateVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        jzTextureView = findViewById(R.id.m_texture_view);
        if(null != mContext && mContext instanceof DetailActivity){
            int position = ((DetailActivity)mContext).getPosition();
            int w = UIUtils.getWidth();
            if(position == 1){
                CommonUtils.build().setViewWH(jzTextureView, w, w);
            }
        }
    }

    @Override
    public void getDate(Object data, Bundle bundle) {
        if(null == mContext || null == data || !(data instanceof String)){
            return;
        }
        String url = (String) data;
        String img = "";
        if(StringUtil.isEmpty(url)){
            return;
        }
        if(null != bundle){
            img = bundle.getString("img");
        }
        if(null != jzTextureView.thumbImageView){
            jzTextureView.thumbImageView.setBackgroundResource(R.color.white);
            GlideUtil.getInstence().loadImage(mContext, jzTextureView.thumbImageView, img);
        }
        jzTextureView.setUp(url, "", Jzvd.SCREEN_WINDOW_NORMAL);
    }

    @Override
    public void onDetached() {
        releaseAllVideos();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if(visibility == GONE){
            releaseAllVideos();
        }
    }

    /**
     * 释放视频资源
     */
    public static void releaseAllVideos(){
        Jzvd.SAVE_PROGRESS = false;
        Jzvd.releaseAllVideos();
    }

    /**
     * 暂停视频
     */
    public static void goOnPlayOnPause(){
        Jzvd.goOnPlayOnPause();
    }

    /**
     * 播放视频
     */
    public static void goOnPlayOnResume(){
        Jzvd.goOnPlayOnResume();
    }

    /**
     * 返回键操作
     */
    public static boolean backPress(){
        return Jzvd.backPress();
    }
}
