package com.jjshop.template_view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jjshop.R;
import com.jjshop.adapter.BannerAdapter;
import com.jjshop.ui.activity.home.ShowBigImgActivity;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.UIUtils;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/7/25
 */

public class TemplateBannerView extends BaseTemplateView implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private LinearLayout layoutPoint;
    private ArrayList<ImageView> mListImgs;
    private String videoUrl;

    public TemplateBannerView(Context context) {
        super(context);
    }

    public TemplateBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        mListImgs = new ArrayList<>();
        viewPager = findViewById(R.id.m_viewpager);
        layoutPoint = findViewById(R.id.m_point);
        viewPager.setOnPageChangeListener(this);
        int screenW = UIUtils.getWidth();
        // 设置Viewpager的宽高，默认是屏幕的宽
        int w = screenW, h = screenW;
        if(null != mContext){
            // 可以通过不同页面来判断，设置宽高
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
        params.width = w;
        params.height = h;
        viewPager.setLayoutParams(params);
    }

    @Override
    public void getDate(Object data, Object data2, Bundle bundle) {
        if(null == mContext || null == data || null == data2){
            return;
        }
        int size = 0;
        ArrayList<String> bannerList = (ArrayList<String>) data;
        videoUrl = (String) data2;
        if(null == bannerList || bannerList.isEmpty()){
            layoutPoint.setVisibility(GONE);
            viewPager.setVisibility(GONE);
        }else{
            if(StringUtil.isEmpty(videoUrl)){
                layoutPoint.setVisibility(VISIBLE);
            }else{
                layoutPoint.setVisibility(GONE);
            }
            viewPager.setVisibility(VISIBLE);
            size = bannerList.size();
            BannerAdapter adapter = new BannerAdapter(mContext, bannerList, videoUrl);
            viewPager.setAdapter(adapter);
        }
        layoutPoint.removeAllViews();
        mListImgs.clear();
        if(mContext instanceof ShowBigImgActivity){
            layoutPoint.setVisibility(GONE);
            return;
        }
        if(size <= 0){
            return;
        }
        int wh = UIUtils.dip2px(8);
        int margin = UIUtils.dip2px(5);
        // 设置小点的宽高
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(wh, wh);
        // 设置小点之间的间距
        params.setMargins(margin, 0, margin, 0);
        for(int i = 0; i < size; i++){
            ImageView view = new ImageView(mContext);
            if(i == 0){
                view.setImageResource(R.drawable.shape_blackcorner);
            }else{
                view.setImageResource(R.drawable.shape_redcorner);
            }
            // 将小点添加布局中
            layoutPoint.addView(view, params);
            mListImgs.add(view);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(null != mContext && mContext instanceof ShowBigImgActivity){
            ((ShowBigImgActivity)mContext).setCurrNum(position);
            return;
        }
        if(null == layoutPoint || null == mListImgs || mListImgs.isEmpty()){
            return;
        }
        if(position == 0 && !StringUtil.isEmpty(videoUrl)){
            layoutPoint.setVisibility(View.GONE);
        }else{
            layoutPoint.setVisibility(View.VISIBLE);
            for (int i = 0; i < mListImgs.size(); i++){
                if(position == i){
                    mListImgs.get(i).setImageResource(R.drawable.shape_blackcorner);
                }else{
                    mListImgs.get(i).setImageResource(R.drawable.shape_redcorner);
                }
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setCurrentItem(int position){
        if(null != viewPager){
            viewPager.setCurrentItem(position);
        }
    }


    @Override
    public void onDetached() {
        if(null != mListImgs){
            mListImgs.clear();
        }
        if(null != layoutPoint){
            layoutPoint.removeAllViews();
        }
        if(null != viewPager){
            viewPager.removeAllViews();
        }
    }
}
