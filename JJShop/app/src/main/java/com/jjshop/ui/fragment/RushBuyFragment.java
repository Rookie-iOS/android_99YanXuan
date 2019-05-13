package com.jjshop.ui.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.adapter.CommentRecycleAdapter;
import com.jjshop.base.BaseFragment;
import com.jjshop.bean.ProductListBean;
import com.jjshop.bean.RushBuyBean;
import com.jjshop.template_view.TemplateUtil;
import com.jjshop.ui.presenter.RushBuyFragmentPresenter;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.Tools;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：张国庆
 * 时间：2018/7/12
 */

public class RushBuyFragment extends BaseFragment<RushBuyFragmentPresenter> implements SwipeMenuRecyclerView.LoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private SwipeRefreshLayout mRefreshLayout;
    private SwipeMenuRecyclerView mRecyclerView;
    private RelativeLayout mLayoutTop;
    private CommentRecycleAdapter mAdapter;
    private ArrayList<Object> mListData;
    private static final String TIME_ID = "time_id";
    private CountDownTimer timer;
    private TextView mTvHour, mTvMinute, mTvSecond, mTvType;
    private String mTimeId = "";
    private int page = 1;
    private int PAGE_SIZE = 10;

    private View mViewLoading, mViewEmpty, mViewError;
    private TextView mTvNoData, mTvRefresh;

    @Override
    protected RushBuyFragmentPresenter getPresenter() {
        return new RushBuyFragmentPresenter(this);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_rush_buy_layout;
    }

    public static RushBuyFragment invoke(String id) {
        RushBuyFragment fragment = new RushBuyFragment();
        Bundle b = new Bundle();
        b.putString(TIME_ID, id);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void initView(View view) {
        Bundle b = getArguments();
        if (null != b) {
            mTimeId = b.getString(TIME_ID);
        }
        mRecyclerView = view.findViewById(R.id.m_recycleview);
        mRefreshLayout = view.findViewById(R.id.m_refresh);
        Tools.setRefreshTopColor(mRefreshLayout);
        mLayoutTop = view.findViewById(R.id.layout_top);
        mTvHour = view.findViewById(R.id.m_tv_hour);
        mTvMinute = view.findViewById(R.id.m_tv_min);
        mTvSecond = view.findViewById(R.id.m_tv_second);
        mTvType = view.findViewById(R.id.m_tv_type);

        mViewLoading = view.findViewById(R.id.m_view_loading);
        mViewEmpty = view.findViewById(R.id.m_view_empty);
        mViewError = view.findViewById(R.id.m_view_error);
        mTvNoData = view.findViewById(R.id.m_tv_nodate);
        mTvRefresh = view.findViewById(R.id.m_tv_refresh);
        mTvRefresh.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerView.useDefaultLoadMore();
        mRecyclerView.setLoadMoreListener(this);
        mRefreshLayout.setOnRefreshListener(this);

        mListData = new ArrayList<>();
        mAdapter = new CommentRecycleAdapter(activity, mListData, TemplateUtil.TEMPLATE_1015);
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setVisibility(View.GONE);
        loadData();
    }

    private void loadData(){
        if(null == mPresenter){
            return;
        }
        loading();
        mPresenter.loadGoods();
    }

    public void onSuccess(RushBuyBean bean){
        showData();
        long currTime = System.currentTimeMillis() / 1000;
        long startTime = bean.getData().getInfo().getStart_time();
        long endTime = bean.getData().getInfo().getEnd_time();
        long timer = 0;
        int type = 0;
        if(currTime < startTime){// 未开始
            mTvType.setText("距开始：");
            timer = startTime - currTime;
            type = 1;
        }else if(currTime > startTime && currTime < endTime){// 秒杀中
            mTvType.setText("距结束：");
            timer = endTime - currTime;
            type = 2;
        }else{// 已结束
            mTvType.setText("已结束：");
            type = 3;
            timer = 0;
        }
        startTimer(timer);
        if(page == 1){
            mRefreshLayout.setRefreshing(false);
            mListData.clear();
        }
        ArrayList<ProductListBean> list = bean.getData().getProduct_list();
        if(null == list || list.isEmpty()){
            if(page == 1){
                showEmpty();
            }else{
                mRecyclerView.loadMoreFinish(false, false);
            }
            return;
        }
        for(ProductListBean data : list){
            data.setType(type);
        }
        mListData.addAll(list);
        mAdapter.notifyDataSetChanged();
//        if(list.size() >= PAGE_SIZE){
//            mRecyclerView.loadMoreFinish(false, true);
//            return;
//        }
        mRecyclerView.loadMoreFinish(false, false);
    }

    public void onFail(String str){
        showError();
    }

    public int page(){
        return page;
    }

    public String shopId(){
        return PreUtils.getString(activity, PreUtils.SHOP_ID, "");
    }

    public String timeId(){
        return mTimeId;
    }

    @Override
    public void onLoadMore() {
        page = page() + 1;
        mPresenter.loadGoods();
    }

    @Override
    public void onRefresh() {
        page = 1;
        mPresenter.loadGoods();
    }

    public void startTimer(long time){
        if(time <= 0){
            mTvHour.setText("00");
            mTvMinute.setText("00");
            mTvSecond.setText("00");
            return;
        }
        time = time * 1000;
        JjLog.e("TAG", "时间1 = " + time);
        stopTimer();
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long l) {
                if(null == mTvHour || null == mTvMinute || null == mTvSecond){
                    return;
                }
                String[] split = Tools.getSmh(l / 1000).split(":");
                if(null == split || split.length <= 0){
                    mTvHour.setText("00");
                    mTvMinute.setText("00");
                    mTvSecond.setText("00");
                    return;
                }
                for (int i = 0; i < split.length; i++) {
                    if(i == 0){
                        mTvHour.setText(split[0]);
                    }
                    if(i == 1){
                        mTvMinute.setText(split[1]);
                    }
                    if(i == 2){
                        mTvSecond.setText(split[2]);
                    }
                }
            }

            @Override
            public void onFinish() {
                if(null == mTvHour || null == mTvMinute || null == mTvSecond){
                    return;
                }
                mTvHour.setText("00");
                mTvMinute.setText("00");
                mTvSecond.setText("00");
            }
        }.start();
    }

    private void stopTimer(){
        if(null != timer){
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    @Override
    public void onClick(View view) {
        loading();
        mPresenter.loadGoods();
    }

    private void loading(){
        if(null == mViewError || null == mViewEmpty || null == mViewLoading){
            return;
        }
        mViewLoading.setVisibility(View.VISIBLE);
        mViewEmpty.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }
    private void showData(){
        if(null == mViewError || null == mViewEmpty || null == mViewLoading || null == mRefreshLayout
                || null == mLayoutTop){
            return;
        }
        mRefreshLayout.setVisibility(View.VISIBLE);
        mLayoutTop.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewEmpty.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }
    private void showEmpty(){
        if(null == mViewError || null == mViewEmpty || null == mViewLoading || null == mRefreshLayout
                || null == mLayoutTop){
            return;
        }
        mViewEmpty.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
        mRefreshLayout.setVisibility(View.GONE);
        mLayoutTop.setVisibility(View.GONE);
        mTvNoData.setText("暂时还没有商品哦");
    }
    private void showError(){
        if(null == mViewError || null == mViewEmpty || null == mViewLoading || null == mRefreshLayout
                || null == mLayoutTop){
            return;
        }
        mViewError.setVisibility(View.VISIBLE);
        mViewEmpty.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);
        mRefreshLayout.setVisibility(View.GONE);
        mLayoutTop.setVisibility(View.GONE);
    }
}
