package com.jjshop.ui.activity.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jjshop.R;
import com.jjshop.adapter.RushBuyPagerAdapter;
import com.jjshop.base.BaseActivity;
import com.jjshop.bean.JJEvent;
import com.jjshop.bean.JJShareBean;
import com.jjshop.bean.RushBuyBean;
import com.jjshop.ui.LoginActivity;
import com.jjshop.ui.fragment.RushBuyFragment;
import com.jjshop.ui.presenter.RushBuyPresenter;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.ShareUtils;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.httputil.HttpUrl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者：张国庆
 * 时间：2018/7/12
 */

public class RushBuyActivity extends BaseActivity<RushBuyPresenter> implements TabLayout.OnTabSelectedListener,
        View.OnClickListener {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ImageView mIvBack, mIvShare;
    private RushBuyPagerAdapter mAdapter;
    private ArrayList<Fragment> mListFragment;
    private ArrayList<RushBuyBean.RushBuyData.NavData> mListTime;
    private RushBuyBean.RushBuyData.InfoData mInfoData;

    private View mViewLoading, mViewEmpty, mViewError;
    private TextView mTvNoData, mTvRefresh;

    private RushBuyBean.ShopshareData mShopshareData;

    private ImmersionBar mImmersionBar;

    @Override
    protected RushBuyPresenter getPresenter() {
        return new RushBuyPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_rush_buy_layout;
    }

    public static void invoke(Context context){
        Intent intent = new Intent(context, RushBuyActivity.class);
        context.startActivity(intent);
    }

    public static void invokePush(Context context, Map<String, String> map){
        if(StringUtil.isEmpty(HttpUrl.getCookies(context))){
            LoginActivity.invokePush(context, map);
            return;
        }
        if(null == HomeActivity.homeInstance){
            HomeActivity.invokePush(context, map);
            return;
        }
        Intent intent = new Intent(context, RushBuyActivity.class);
        if(!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            JjLog.e("不是activity");
        }
        context.startActivity(intent);
        map.clear();
    }

    @Override
    protected void setStatusBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fitsSystemWindows(true);
        mImmersionBar.statusBarColor(R.color.color_d4382b);
        mImmersionBar.init();
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        mIvBack = findViewById(R.id.m_iv_back);
        mIvShare = findViewById(R.id.m_iv_share);
        mIvBack.setOnClickListener(this);
        mIvShare.setOnClickListener(this);
        mTabLayout = findViewById(R.id.m_tablayout);
        mViewPager = findViewById(R.id.m_viewpager);

        mViewLoading = findViewById(R.id.m_view_loading);
        mViewEmpty = findViewById(R.id.m_view_empty);
        mViewError = findViewById(R.id.m_view_error);
        mTvNoData = findViewById(R.id.m_tv_nodate);
        mTvRefresh = findViewById(R.id.m_tv_refresh);
        mTvRefresh.setOnClickListener(this);

        mListFragment = new ArrayList<>();
    }

    @Override
    protected void initData() {
        loading();
        mPresenter.loadGoods();
    }

    public void onSuccess(RushBuyBean bean){
        mIvShare.setVisibility(View.VISIBLE);
        mShopshareData = bean.getShopsharedata();
        showData();
        mListTime = bean.getData().getNav();
        mInfoData = bean.getData().getInfo();
        for(int i = 0;i < mListTime.size(); i++){
            RushBuyFragment fragment = RushBuyFragment.invoke(mListTime.get(i).getId());
            mListFragment.add(fragment);
        }
        if(mListTime.size() <= 4){
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        }else{
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
        mAdapter = new RushBuyPagerAdapter(getSupportFragmentManager(), mListFragment);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);
        setTabLayout();
    }

    public void onFail(String str){
        JjLog.e("TAG", str);
        showError();
    }

    public String shopId(){
        return PreUtils.getString(this, PreUtils.SHOP_ID, "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.m_iv_back:
                finish();
                break;
            case R.id.m_iv_share:
                share();
                break;
            case R.id.m_tv_refresh:
                loading();
                mPresenter.loadGoods();
                break;
        }

    }

    private void setTabLayout(){
        selectColor = this.getResources().getColor(R.color.white);
        unSelectColor = this.getResources().getColor(R.color.color_f8bdbf);
        for(int i = 0; i < mListTime.size(); i++){
            View view = LayoutInflater.from(this).inflate(R.layout.rushbuy_tab_layout, null);
            TabLayout.Tab tab = mTabLayout.getTabAt(i);//获得每一个tab
            tab.setCustomView(view);//给每一个tab设置view
            TextView tvTime = view.findViewById(R.id.m_tv_time);
            TextView tvStatus = view.findViewById(R.id.m_tv_status);
            if (i == 0) {
                // 设置第一个tab的TextView是被选择的样式
                tvTime.setTextColor(selectColor);
                tvStatus.setTextColor(selectColor);
            }
            tvTime.setText(mListTime.get(i).getTime());
            tvStatus.setText(mListTime.get(i).getStatus_str());
        }
        mTabLayout.setOnTabSelectedListener(this);
    }
    int selectColor;
    int unSelectColor;


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        ((TextView)tab.getCustomView().findViewById(R.id.m_tv_time)).setTextColor(selectColor);
        ((TextView)tab.getCustomView().findViewById(R.id.m_tv_status)).setTextColor(selectColor);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        ((TextView)tab.getCustomView().findViewById(R.id.m_tv_time)).setTextColor(unSelectColor);
        ((TextView)tab.getCustomView().findViewById(R.id.m_tv_status)).setTextColor(unSelectColor);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPause() {
        super.onPause();
        mViewLoading.setVisibility(View.GONE);
    }

    private boolean mIsFrist = true;
    @Override
    public void onResume() {
        super.onResume();
        if(mIsFrist){
            mIsFrist = false;
            return;
        }
        mViewLoading.setVisibility(View.GONE);
    }

    public void shareShowHideLoading(int visible){
        if(!(View.GONE == visible || View.VISIBLE == visible || null == mViewLoading)){
            return;
        }
        mViewLoading.setVisibility(visible);
    }

    private void loading(){
        mViewLoading.setVisibility(View.VISIBLE);
        mViewEmpty.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
        mTabLayout.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
    }
    private void showData(){
        mTabLayout.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewEmpty.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }
    private void showError(){
        mViewError.setVisibility(View.VISIBLE);
        mViewEmpty.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);
        mTabLayout.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
    }

    // 分享
    private void share(){
        if(null == mShopshareData){
            return;
        }
        final String title = mShopshareData.getTitle();
        String intro = mShopshareData.getIntro();
        final String thumb = mShopshareData.getThumb();
        final String url = mShopshareData.getUrl();

        JJShareBean bean = new JJShareBean();
        JJShareBean.DataBean dataBean = new JJShareBean.DataBean();
        dataBean.setTitle(title);
        dataBean.setIntro(intro);
        dataBean.setThumb(thumb);
        dataBean.setUrl(url);
        List<String> list = new ArrayList<>();
        dataBean.setShare_imgs(list);
        bean.setData(dataBean);

        ShareUtils.build().shareDialog(this, bean, false, null);

    }

    @Subscribe
    public void onEvent(JJEvent event){
        if(null == event){
            return;
        }
        int id = event.getEventId();
        switch (id){
            case JJEvent.FINISH_ACTIVITY:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if(mImmersionBar != null){
            mImmersionBar.destroy();
        }
        if(mViewPager != null){
            mViewPager.removeAllViews();
        }
        if(mTabLayout != null){
            mTabLayout.removeAllTabs();
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
