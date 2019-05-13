package com.jjshop.template_view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jjshop.R;
import com.jjshop.adapter.NewsPicAdapter;
import com.jjshop.bean.JJHomeBean;
import com.jjshop.ui.WebViewActivity;
import com.jjshop.ui.activity.home.HotZhuanquActivity;
import com.jjshop.ui.activity.home.RushBuyActivity;
import com.jjshop.utils.glide_img.GlideUtil;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：张国庆
 * 时间：2018/7/20
 */

public class TemplateGoodsAllHeadView extends BaseLinearTemplateView implements ViewPager.OnPageChangeListener{

    private ViewPager mViewPager;
    private LinearLayout mCirclePoint;
    private LinearLayout mLayoutShop;
    private ImageView mIvOpenSHop, mIvLeft, mIvCenter, mIvRight;
    private BannerHandler mHandler;
    private JJHomeBean.DataBean dataBean;
    private String mOpenShopUrl = "";
    private List<JJHomeBean.DataBean.RecommendData> threeImgList;
    private View mLineOne, mLineTwo;
    private ArrayList<ImageView> mListImgs;

    public TemplateGoodsAllHeadView(Context context) {
        super(context);
    }

    public TemplateGoodsAllHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateGoodsAllHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        mHandler = new BannerHandler();
        int w = UIUtils.getWidth();
        // Banner
        mListImgs = new ArrayList<>();
        int banW = w - UIUtils.dip2px(20);
        mViewPager = findViewById(R.id.temp_all_head_viewpager);
        mViewPager.setLayoutParams(new RelativeLayout.LayoutParams(banW, (int)(banW * (35 / 75.0))));
        mCirclePoint = findViewById(R.id.temp_all_head_point);// 小圆点
        mLineOne = findViewById(R.id.view_line_one);
        // 邀请开店
        mLayoutShop = findViewById(R.id.m_layout_shop);
        mIvOpenSHop = findViewById(R.id.temp_all_head_open_shop);
        int shopW = w - UIUtils.dip2px(20);
        mIvOpenSHop.setLayoutParams(new LinearLayout.LayoutParams(shopW, (int) (shopW * (18 / 75.0))));
        mLineTwo = findViewById(R.id.view_line_two);
        // 三图模板
        mIvLeft = findViewById(R.id.temp_all_head_left);
        mIvCenter = findViewById(R.id.temp_all_head_center);
        mIvRight = findViewById(R.id.temp_all_head_right);
        setImgWh(mIvLeft);
        setImgWh(mIvCenter);
        setImgWh(mIvRight);
        mViewPager.setOnPageChangeListener(this);

        mIvOpenSHop.setOnClickListener(this);
        mIvLeft.setOnClickListener(this);
        mIvCenter.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
    }

    @Override
    public void getDate(Object data, Bundle bundle) {
        if(null == mContext || null == data || !(data instanceof JJHomeBean.DataBean)){
            return;
        }
        dataBean = (JJHomeBean.DataBean) data;
        // banner数据
        List<JJHomeBean.DataBean.IndexDataBean.CarouselBean> bannerList = dataBean.getIndex_data().getCarousel();
        if(null != bannerList && bannerList.size() > 0){
            mViewPager.setAdapter(new NewsPicAdapter(mContext, bannerList));
            mViewPager.setOffscreenPageLimit(3);
            mCirclePoint.removeAllViews();
            mListImgs.clear();
            int wh = UIUtils.dip2px(8);
            int margin = UIUtils.dip2px(5);
            // 设置小点的宽高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(wh, wh);
            // 设置小点之间的间距
            params.setMargins(margin, 0, margin, 0);
            for(int i = 0; i < bannerList.size(); i++){
                ImageView view = new ImageView(mContext);
                if(i == 0){
                    view.setImageResource(R.drawable.shape_blackcorner);
                }else{
                    view.setImageResource(R.drawable.shape_redcorner);
                }
                // 将小点添加布局中
                mCirclePoint.addView(view, params);
                mListImgs.add(view);
            }
            if(null != mHandler){// 开启自动轮播
                mHandler.startLoop();
            }
        }else{
            if(null != mHandler){// 关闭自动轮播
                mHandler.stopLoop();
            }
            mViewPager.setVisibility(GONE);
            mCirclePoint.setVisibility(GONE);
            mLineOne.setVisibility(GONE);
        }
        // 邀请开店
        JJHomeBean.DataBean.IndexDataBean.BannerBean openShopBean = dataBean.getIndex_data().getBanner();
        if(null != openShopBean && !StringUtil.isEmpty(openShopBean.getImg())){
            mIvOpenSHop.setVisibility(VISIBLE);
            mLineTwo.setVisibility(VISIBLE);
            mLayoutShop.setVisibility(VISIBLE);
            GlideUtil.getInstence().loadImage(mContext, mIvOpenSHop, openShopBean.getImg());
            mOpenShopUrl = openShopBean.getImg_url();
        }else{
            mIvOpenSHop.setVisibility(GONE);
            mLineTwo.setVisibility(GONE);
            mLayoutShop.setVisibility(GONE);
        }
        // 三张图
        threeImgList = dataBean.getRecommend();
        if(null == threeImgList || threeImgList.isEmpty()){
            mIvLeft.setVisibility(View.GONE);
            mIvCenter.setVisibility(View.GONE);
            mIvRight.setVisibility(View.GONE);
            return;
        }
        mIvLeft.setVisibility(View.VISIBLE);
        mIvCenter.setVisibility(View.VISIBLE);
        mIvRight.setVisibility(View.VISIBLE);

        if(threeImgList.size() >= 1){
            GlideUtil.getInstence().loadImage(mContext, mIvLeft, threeImgList.get(0).getImg());
        }
        if(threeImgList.size() >= 2){
            GlideUtil.getInstence().loadImage(mContext, mIvCenter, threeImgList.get(1).getImg());
        }
        if(threeImgList.size() >= 3){
            GlideUtil.getInstence().loadImage(mContext, mIvRight, threeImgList.get(2).getImg());
        }
    }

    @Override
    public void onClick(View v) {
        if(null == mContext){
            return;
        }
        switch (v.getId()){
            case R.id.temp_all_head_open_shop:
                if(StringUtil.isEmpty(mOpenShopUrl)){
                    return;
                }
                WebViewActivity.invoke(mContext, mOpenShopUrl);
                break;
            case R.id.temp_all_head_left:
                onClickThree(0);
                break;
            case R.id.temp_all_head_center:
                onClickThree(1);
                break;
            case R.id.temp_all_head_right:
                onClickThree(2);
                break;
        }
    }

    /**
     * 设置三图模板图片的宽高
     * @param view
     */
    private void setImgWh(View view){
        int width = UIUtils.getWidth();
        int margin = UIUtils.dip2px(30);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.width = (width - margin) / 3;
        params.height = params.width * 194 / 340;
        view.setLayoutParams(params);
    }

    // 三张图的点击事件
    private void onClickThree(int position){
        if(null == mContext || null == threeImgList || threeImgList.isEmpty() || position >= threeImgList.size()){
            return;
        }
        JJHomeBean.DataBean.RecommendData bean = threeImgList.get(position);
        if(null == bean){
            return;
        }
        if(bean.getType() == 1){// 秒杀
            RushBuyActivity.invoke(mContext);
            return;
        }
        String type = "";
        if(bean.getType() == 2){// 新品
            type = HotZhuanquActivity.NEW_GOODS;
        }
        if(bean.getType() == 3){// 人气
            type = HotZhuanquActivity.HOT_GOODS;
        }
        HotZhuanquActivity.invoke(mContext, type, bean.getName());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(null == mCirclePoint || null == mListImgs || mListImgs.isEmpty()){
            return;
        }
        int size = mListImgs.size();
        for (int i = 0; i < size; i++){
            if(position % size == i){
                mListImgs.get(i).setImageResource(R.drawable.shape_blackcorner);
            }else{
                mListImgs.get(i).setImageResource(R.drawable.shape_redcorner);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(null == mHandler){
            return;
        }
        // 滑动时，不自动循环
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                mHandler.stopLoop();
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                mHandler.startLoop();
                break;
        }
    }

    class BannerHandler extends Handler {
        // 轮播间隔时间
        public static final long TIME = 3000;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mViewPager == null) {
                return;
            }
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
            sendEmptyMessageDelayed(msg.what, TIME);
        }

        /**
         * 开始轮播
         */
        public void startLoop() {
            removeCallbacksAndMessages(null);
            sendEmptyMessageDelayed(1, TIME);
        }

        /**
         * 停止轮播
         */
        public void stopLoop() {
            removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if(visibility == VISIBLE){
            if(null != mHandler){
                mHandler.startLoop();
            }
        }else{
            if(null != mHandler){
                mHandler.stopLoop();
            }
        }
    }

    @Override
    public void onDetached() {
    }
}
