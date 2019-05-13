package com.jjshop.ui.activity.person;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.adapter.TabPageIndicatorAdapter;
import com.jjshop.base.BaseActivity;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.JJEvent;
import com.jjshop.ui.fragment.OrderFragment;
import com.jjshop.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：张国庆
 * 时间：2018/9/17
 */

public class MyOrderActivity extends BaseActivity {
    @BindView(R.id.m_tv_title)
    TextView mTvTitle;
    @BindView(R.id.m_tablayout)
    TabLayout mTablayout;
    @BindView(R.id.m_viewpager)
    ViewPager mViewpager;

    private ArrayList<Fragment> fragmentlist;
    private ArrayList<String> listTitle;
    private int mPosition = 0;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected int setLayoutId() {
        return R.layout.actiivty_my_order_layout;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    public static void invoke(Context context, int position){
        Intent intent = new Intent(context, MyOrderActivity.class);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        if(getIntent() != null){
            mPosition = getIntent().getIntExtra("position", 0);
        }
        mTvTitle.setText("我的订单");
        listTitle = new ArrayList<>();
        fragmentlist = new ArrayList<>();
        listTitle.add("全部");listTitle.add("待付款");listTitle.add("待发货");listTitle.add("待收货");listTitle.add("已完成");
        int[] status = {1, 2, 3, 4, 7};
        for (int i = 0; i < listTitle.size(); i++) {
            OrderFragment otherFragment = OrderFragment.invoke(status[i]);
            fragmentlist.add(otherFragment);
        }
        // 适配器
        TabPageIndicatorAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager(), fragmentlist);
        mViewpager.setAdapter(adapter);
        // 默认缓存两个
        mViewpager.setOffscreenPageLimit(5);
        mTablayout.setupWithViewPager(mViewpager);
        setTabLayout();

        mViewpager.setCurrentItem(mPosition);

    }

    // 自定义Tablayout布局
    private void setTabLayout(){
        final int selectColor = getResources().getColor(R.color.color_d6004f);
        final int unSelectColor = getResources().getColor(R.color.color_333333);
        for(int i = 0; i < listTitle.size(); i++){
            View view = LayoutInflater.from(this).inflate(R.layout.home_tab_layout, null);
            TabLayout.Tab tab = mTablayout.getTabAt(i);//获得每一个tab
            tab.setCustomView(view);//给每一个tab设置view
            TextView TvTitle = view.findViewById(R.id.m_tv_title);
            View line = view.findViewById(R.id.m_view_line);
            TvTitle.setText(listTitle.get(i));
            TvTitle.setTextColor(unSelectColor);
            if (i == mPosition) {
                // 设置第一个tab的TextView是被选择的样式
                TvTitle.setTextColor(selectColor);
                line.setVisibility(View.VISIBLE);
            }
        }
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((TextView)tab.getCustomView().findViewById(R.id.m_tv_title)).setTextColor(selectColor);
                tab.getCustomView().findViewById(R.id.m_view_line).setVisibility(View.VISIBLE);
                mViewpager.setCurrentItem(tab.getPosition());
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

        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                handler.removeCallbacks(runnable);
                runnable = null;
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        loadDataList(position);
                    }
                };
                handler.postDelayed(runnable, 300);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.m_title_back)
    public void onClick() {
        finish();
    }

    /**
     * 加载选中fragment的数据
     * @param position
     */
    private void loadDataList(int position){
        if(null != fragmentlist && fragmentlist.size() > position && position >= 0){
            Fragment fragment = fragmentlist.get(position);
            if(null != fragment && fragment instanceof OrderFragment){
                ((OrderFragment)fragment).loadDataList(false);
            }
        }
    }

    /**
     * 加载微信支付数据
     * @param orderNo
     * @param payType
     */
    public void loadDataWxPay(long orderNo, String payType) {
        if(null != mViewpager && null != fragmentlist && fragmentlist.size() > 0){
            Fragment fragment = fragmentlist.get(mViewpager.getCurrentItem());
            if(null != fragment){
                if(fragment instanceof OrderFragment){
                    ((OrderFragment)fragment).loadDataWxPay(orderNo, payType);
                }
            }
        }
    }

    /**
     * 加载取消订单
     * @param orderNo
     */
    public void loadCancelOrder(long orderNo, String status) {
        if(null != mViewpager && null != fragmentlist && fragmentlist.size() > 0){
            Fragment fragment = fragmentlist.get(mViewpager.getCurrentItem());
            if(null != fragment){
                if(fragment instanceof OrderFragment){
                    ((OrderFragment)fragment).loadCancelOrder(orderNo, status);
                }
            }
        }
    }

    /**
     * 设置标签的数量
     * @param dfkNum
     * @param dfhNum
     * @param dshNum
     */
    public void setTabNum(int dfkNum, int dfhNum, int dshNum){
        if(null != mTablayout && null != listTitle){
            if(listTitle.size() >= 5){
                TabLayout.Tab tabDfk = mTablayout.getTabAt(1);
                if(null != tabDfk){
                    TextView tvDfk = tabDfk.getCustomView().findViewById(R.id.m_tv_num);
                    CommonUtils.build().setCirleTextNum(tvDfk, dfkNum);
                }
                TabLayout.Tab tabDfh = mTablayout.getTabAt(2);
                if(null != tabDfh){
                    TextView tvDfh = tabDfh.getCustomView().findViewById(R.id.m_tv_num);
                    CommonUtils.build().setCirleTextNum(tvDfh, dfhNum);
                }
                TabLayout.Tab tabDsh = mTablayout.getTabAt(3);
                if(null != tabDsh){
                    TextView tvDsh = tabDsh.getCustomView().findViewById(R.id.m_tv_num);
                    CommonUtils.build().setCirleTextNum(tvDsh, dshNum);
                }
            }
        }
    }

    @Subscribe
    public void onEvent(JJEvent event){
        if(null == event){
            return;
        }
        final int id = event.getEventId();
        switch (id){
            case JJEvent.FINISH_ACTIVITY:
                finish();
                break;
            case JJEvent.PAY_CANCEL:
            case JJEvent.PAY_FAIL:
                break;
            case JJEvent.PAY_SUCCESS:
            case JJEvent.REFRESH_ORDER_LIST:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(id == JJEvent.PAY_SUCCESS){
                            EventBus.getDefault().post(new JJEvent(JJEvent.REFRESH_PERSON_INFO));
                        }
                        if(null != mViewpager){
                            loadDataList(mViewpager.getCurrentItem());
                        }
                    }
                });
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(null != handler){
            handler.removeCallbacks(runnable);
            runnable = null;
        }
    }
}
