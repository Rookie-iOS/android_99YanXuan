package com.jjshop.ui.fragment;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.adapter.TabPageIndicatorAdapter;
import com.jjshop.app.JJShopApplication;
import com.jjshop.base.BasePresenter;
import com.jjshop.base.BaseFragment;
import com.jjshop.bean.HomeClassifyBean;
import com.jjshop.bean.JJEvent;
import com.jjshop.ui.WebViewActivity;
import com.jjshop.ui.activity.home.HomeActivity;
import com.jjshop.ui.activity.home.SearchActivity;
import com.jjshop.utils.ADUtil;
import com.jjshop.utils.BuglyUtil;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.InvokeMarketUtil;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.UrlInvokeUtil;
import com.jjshop.utils.glide_img.GlideUtil;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.UIUtils;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 首页
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener{

    private TabLayout tablayout;
    private ViewPager viewPager;
    private ImageView mIvActivity;
    private List<Fragment> fragmentlist = new ArrayList<>();

    private View mViewLoading, mViewEmpty, mViewError;
    private TextView mTvNoData, mTvRefresh;

    private ImageView mIvLeft;
    private TextView mTvTitle;
    private ImageView mIvRight;
    private TextView mTvMsgNum;
    private View mViewStatus;

    private String mActivityUrl = "";
    private boolean mIsShowFuchuang;
    private ObjectAnimator translationX;// 动画
    private int mOldPosition = 0;// 选中的下标

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(View view) {
        EventBus.getDefault().register(this);
        mViewStatus = view.findViewById(R.id.m_view_status);
        mTvMsgNum = view.findViewById(R.id.m_tv_msg_num);
        mIvLeft = view.findViewById(R.id.m_title_back);
        mIvLeft.setImageResource(R.drawable.home_email);
        mIvLeft.setPadding(UIUtils.dip2px(10), 0, 0, 0);
        mIvLeft.setOnClickListener(this);
        mTvTitle = view.findViewById(R.id.m_tv_title);
        mTvTitle.setOnClickListener(this);
        mIvRight = view.findViewById(R.id.m_title_right_iv);
        mIvRight.setImageResource(R.mipmap.img_home_right_search);
        mIvRight.setVisibility(View.VISIBLE);
        mIvRight.setOnClickListener(this);

        tablayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpager2);
        mIvActivity = view.findViewById(R.id.m_iv_activity);
        mIvActivity.setOnClickListener(this);

        mViewLoading = view.findViewById(R.id.m_view_loading);
        mViewEmpty = view.findViewById(R.id.m_view_empty);
        mViewError = view.findViewById(R.id.m_view_error);
        mTvNoData = view.findViewById(R.id.m_tv_nodate);
        mTvRefresh = view.findViewById(R.id.m_tv_refresh);
        mTvRefresh.setOnClickListener(this);
        mIsShowFuchuang = false;
        // 沉浸式，增加一个状态栏的高度
        setTitleLayout();
        // 加载列表数据
        loadData();
        // 设置id给bugly
        BuglyUtil.build().setUserId(PreUtils.getString(activity, PreUtils.SHOP_ID));
        // 加载活动数据
        ADUtil.build().loadActivityData(activity, CommonUtils.ACTIVITY_TYPE_HOME);
        // 页面统计
        log();
    }

    private void setTitleLayout(){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mViewStatus.getLayoutParams();
        params.height = UIUtils.getStatusHeight(activity);
        mViewStatus.setLayoutParams(params);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M &&
                InvokeMarketUtil.build().getRomType() != InvokeMarketUtil.ROM_TYPE.MIUI){// 小于6.0设置成黑色半透明
            mViewStatus.setBackgroundColor(getResources().getColor(R.color.color_80333333));
        }
    }

    // 加载标题数据、店铺信息
    private void loadData(){
        if(null != activity && activity instanceof HomeActivity){
            loading();
            ((HomeActivity)activity).loadTitleData(false);
        }
    }
    // 成功获取数据
    public void onSuccess(HomeClassifyBean bean){

        JjLog.e("========= iiiii:"+MiPushClient.getRegId(activity));

        showData();
        HomeClassifyBean.ShopInfoBean shopInfoBean = bean.getData().getIndex_data().getShop_info();
        if(null != shopInfoBean){
            // 保存店铺信息
            PreUtils.saveShop(activity, shopInfoBean.getId(), shopInfoBean.getShopname(),
                    shopInfoBean.getShopinfo(), shopInfoBean.getShoplogo());
            // 显示店铺名称
            setTitle(shopInfoBean.getShopname());
        }
        // userIdCode
        PreUtils.setString(activity, PreUtils.USER_ID_CODE, bean.getData().getIndex_data().getUid_code());
        // 初始化Tab
        initViewpager(bean.getData().getIndex_data_nav().getCategory_list());
    }
    // 获取数据失败
    public void onFail(String msg){
        showError();
    }

    // 初始化Tab
    private void initViewpager(List<HomeClassifyBean.CategoryListBean> listTitle) {
        fragmentlist.clear();
        viewPager.removeAllViews();
        tablayout.removeAllTabs();
        tablayout.setOnTabSelectedListener(null);
        viewPager.setOnPageChangeListener(null);
        if(null == listTitle){
            listTitle = new ArrayList<>();
        }
        // 默认第一个显示全部
        HomeClassifyBean.CategoryListBean titleBean = new HomeClassifyBean.CategoryListBean();
        titleBean.setId(0);
        titleBean.setVid(0);
        titleBean.setName("全部");
        listTitle.add(0, titleBean);
        // fragment集合
        for (int i = 0; i < listTitle.size(); i++) {
            final HomeItemFragment otherFragment = HomeItemFragment.invoke(
                    listTitle.get(i).getId(), listTitle.get(i).getVid(),
                    listTitle.get(i).getUrl());
            fragmentlist.add(otherFragment);
        }
        // 适配器
        TabPageIndicatorAdapter adapter = new TabPageIndicatorAdapter(getChildFragmentManager(), fragmentlist);
        viewPager.setAdapter(adapter);
        // 默认缓存两个
        viewPager.setOffscreenPageLimit(2);
        tablayout.setupWithViewPager(viewPager);

        setTabLayout(listTitle);
        final List<HomeClassifyBean.CategoryListBean> finalList = listTitle;
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(null != finalList && finalList.size() > 0 && position < finalList.size()){
                    HomeClassifyBean.CategoryListBean bean = finalList.get(position);
                    if(null != bean){
                        String url = bean.getUrl();
                        if(!StringUtil.isEmpty(url)){// 活动链接不为空时，选择上一个页面且直接跳转到WebviewActivity加载链接
                            UrlInvokeUtil.build().pushInvoke(activity, UrlInvokeUtil.build().pushData(url));
                            invokeWeb();
                        }else{
                            mOldPosition = position;
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == 1){
                    scrollShowFuchuang(View.GONE);
                }else if(state == 0){
                    scrollShowFuchuang(View.VISIBLE);
                }
            }
        });
    }
    // 自定义Tablayout布局
    private void setTabLayout(List<HomeClassifyBean.CategoryListBean> listTitle){
        final int selectColor = activity.getResources().getColor(R.color.color_333333);
        final int unSelectColor = activity.getResources().getColor(R.color.color_999999);
        for(int i = 0; i < listTitle.size(); i++){
            View view = LayoutInflater.from(activity).inflate(R.layout.home_tab_layout, null);
            TabLayout.Tab tab = tablayout.getTabAt(i);//获得每一个tab
            tab.setCustomView(view);//给每一个tab设置view
            TextView TvTitle = view.findViewById(R.id.m_tv_title);
            View line = view.findViewById(R.id.m_view_line);
            TvTitle.setText(listTitle.get(i).getName());
            if (i == 0) {
                // 设置第一个tab的TextView是被选择的样式
                TvTitle.setTextColor(selectColor);
                line.setVisibility(View.VISIBLE);
            }
        }
        final List<HomeClassifyBean.CategoryListBean> finalList = listTitle;
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(null != finalList && finalList.size() > 0 && tab.getPosition() < finalList.size()){
                    HomeClassifyBean.CategoryListBean bean = finalList.get(tab.getPosition());
                    if(null != bean){
                        String url = bean.getUrl();
                        if(!StringUtil.isEmpty(url)){// 活动链接不为空时，不改变颜色
                            ((TextView)tab.getCustomView().findViewById(R.id.m_tv_title)).setTextColor(unSelectColor);
                            tab.getCustomView().findViewById(R.id.m_view_line).setVisibility(View.GONE);
                        }else{
                            ((TextView)tab.getCustomView().findViewById(R.id.m_tv_title)).setTextColor(selectColor);
                            tab.getCustomView().findViewById(R.id.m_view_line).setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView)tab.getCustomView().findViewById(R.id.m_tv_title)).setTextColor(unSelectColor);
                tab.getCustomView().findViewById(R.id.m_view_line).setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }

    private Timer tExit;
    private int clickCount = 0;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.m_title_back:
                if(StringUtil.isEmpty(PreUtils.getString(activity, PreUtils.SHOP_ID))){
                    JjToast.getInstance().toast("加载中，请稍后");
                    return;
                }
                WebViewActivity.invoke(activity, HttpUrl.WEB_MSG + PreUtils.getString(activity, PreUtils.SHOP_ID,""));
                break;
            case R.id.m_title_right_iv:
                SearchActivity.invoke(activity);
                break;
            case R.id.m_iv_activity:
                Map<String, String> map = UrlInvokeUtil.build().pushData(mActivityUrl);
                UrlInvokeUtil.build().pushInvoke(activity, map);
                break;
            case R.id.m_tv_refresh:
                loadData();
                break;
            case R.id.m_tv_title:
                if (clickCount <= 10) {
                    clickCount++;
                    if (tExit == null) {
                        tExit = new Timer();
                        tExit.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                clickCount = 0;
                                tExit.cancel();
                                tExit.purge();
                                tExit = null;
                            }
                        }, 5000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
                    }
                } else {
                    JjToast.getInstance().toast( "当前版本：" + JJShopApplication.sVersion +
                                    "\n当前渠道：" + JJShopApplication.sChannel);
                    clickCount = 0;
                    tExit.cancel();
                    tExit.purge();
                    tExit = null;
                }
                break;
        }
    }

    /**
     * 显示浮窗
     * @param img
     * @param url
     */
    public void showFuchuang(String img, String url){
        if(null == mIvActivity){
            return;
        }
        mIsShowFuchuang = true;
        mActivityUrl = url;
        GlideUtil.getInstence().loadImageNoFix(activity, mIvActivity, img);
        mIvActivity.setVisibility(View.VISIBLE);
    }

    private int mVisi;
    /**
     * 滑动时是否隐藏、显示浮窗
     */
    public void scrollShowFuchuang(int visi){
        if(null == mIvActivity){
            return;
        }
        if(!mIsShowFuchuang){
            return;
        }
        if(mVisi == visi){
            return;
        }
        float start = 0;
        float end = UIUtils.dip2px(65);
        if(null != translationX){
            translationX.cancel();
            translationX = null;
        }
        translationX = ObjectAnimator
                        .ofFloat(mIvActivity, "translationX",
                        visi == View.GONE ? start : end, visi == View.GONE ? end: start);
        translationX.setDuration(300);//时间
        translationX.start();//开始执行
        mVisi = visi;

    }

    // 日志统计
    public void log(){
        CommonUtils.build().otherLog(activity, getClass().getName());
    }

    private void loading(){
        if(null == mViewError || null == mViewEmpty || null == mViewLoading || null == viewPager || null == tablayout){
            return;
        }
        mViewLoading.setVisibility(View.VISIBLE);
        mViewEmpty.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
        tablayout.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
    }
    private void showData(){
        if(null == mViewError || null == mViewEmpty || null == mViewLoading || null == viewPager || null == tablayout){
            return;
        }
        tablayout.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewEmpty.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }
    private void showError(){
        if(null == mViewError || null == mViewEmpty || null == mViewLoading || null == viewPager || null == tablayout){
            return;
        }
        mViewError.setVisibility(View.VISIBLE);
        mViewEmpty.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        tablayout.setVisibility(View.GONE);
    }

    // 显示隐藏加载框
    public void shareShowHideLoading(int visible){
        if(!(View.GONE == visible || View.VISIBLE == visible) || null == mViewLoading){
            return;
        }
        if(View.GONE == visible){
            mViewLoading.setVisibility(View.GONE);
        }else{
            mViewLoading.setVisibility(View.VISIBLE);
        }
    }


    Timer timer;
    /**
     * 滑动切换跳转到新页面加载链接
     */
    private void invokeWeb(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(null != viewPager){
                            viewPager.setCurrentItem(mOldPosition, false);
                        }
                        timer.cancel();
                        timer.purge();
                        timer = null;
                    }
                });

            }
        }, 1);
    }

    @Subscribe
    public void onEvent(JJEvent event){
        if(null == event){
            return;
        }
        int id = event.getEventId();
        switch (id){
            case JJEvent.UPDATE_LOCAL_SHOP_INFO:
                setTitle(PreUtils.getString(activity, PreUtils.SHOP_NAME));
                break;
        }
    }

    private void setTitle(String shopName){
        if(null == mTvTitle){
            return;
        }
        if(StringUtil.isEmpty(shopName)){
            mTvTitle.setVisibility(View.GONE);
        }else {
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(shopName);
        }
    }

    public void showMsgNum(int num){
        if(null == mTvMsgNum){
            return;
        }
        mTvMsgNum.setVisibility(num <= 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(null != viewPager){
            viewPager.removeAllViews();
        }
        if(null != tablayout){
            tablayout.removeAllTabs();
        }
        if(null != fragmentlist){
            fragmentlist.clear();
        }
        EventBus.getDefault().unregister(this);
        if(null != tExit){
            tExit.cancel();
            tExit.purge();
            tExit = null;
        }
        if(null != translationX){
            translationX.cancel();
        }
        if(null != mIvActivity){
            mIvActivity.clearAnimation();
        }
    }
}
