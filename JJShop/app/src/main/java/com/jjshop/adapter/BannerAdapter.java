package com.jjshop.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jjshop.R;
import com.jjshop.dialog.SaveImgDialog;
import com.jjshop.template_view.TemplateVideoView;
import com.jjshop.ui.activity.home.ShowBigImgActivity;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.glide_img.GlideUtil;

import java.util.ArrayList;
import java.util.LinkedList;


public class BannerAdapter extends PagerAdapter {

    private ArrayList<String> viewlist;
    private Context mContext;
    private String mVideoUrl;
    private LinkedList<View> mCacheViews = null;

    public BannerAdapter(Context context, ArrayList<String> viewlist) {
        this.viewlist = viewlist;
        this.mContext = context;
        mCacheViews = new LinkedList<>();
    }

    public BannerAdapter(Context context, ArrayList<String> viewlist, String videoUrl) {
        this.viewlist = viewlist;
        this.mContext = context;
        this.mVideoUrl = videoUrl;
        mCacheViews = new LinkedList<>();
    }

    @Override
    public int getCount() {
        return null == viewlist ? 0 : viewlist.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
        if(position != 0 && StringUtil.isEmpty(mVideoUrl)){
            mCacheViews.add(view);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        if(!StringUtil.isEmpty(mVideoUrl) && position == 0){// 显示视频view
            View view = LayoutInflater.from(mContext).inflate(R.layout.template_video_view_layout, null);
            if(view instanceof TemplateVideoView){
                Bundle bundle = new Bundle();
                bundle.putString("img", viewlist.get(position));
                ((TemplateVideoView)view).getDate(mVideoUrl, bundle);
                bundle.clear();
            }
            container.addView(view);
            return view;
        }
        View view = null;
        ViewHolder holder = null;
        if(mCacheViews.size() <= 0){
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.banner_item, null);
            holder.imageView = view.findViewById(R.id.image);
            view.setTag(holder);
        }else{
            view = mCacheViews.removeFirst();
            holder = (ViewHolder) view.getTag();
        }
        GlideUtil.getInstence().loadImage(mContext, holder.imageView, viewlist.get(position));
        container.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != mContext && mContext instanceof ShowBigImgActivity){
                    ((ShowBigImgActivity) mContext).finish();
                    return;
                }
                ShowBigImgActivity.invoke(mContext, viewlist, position);
            }
        });
        if(null != mContext && mContext instanceof ShowBigImgActivity){
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    SaveImgDialog.build().showView(
                            ((ShowBigImgActivity)mContext).getSupportFragmentManager(), viewlist.get(position));
                    return true;
                }
            });
        }
        return view;
    }

    public final class ViewHolder {
        public ImageView imageView = null;
    }
}
