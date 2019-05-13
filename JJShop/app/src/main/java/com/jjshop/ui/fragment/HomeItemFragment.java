package com.jjshop.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.adapter.CommentRecycleAdapter;
import com.jjshop.base.BaseFragment;
import com.jjshop.bean.JJHomeBean;
import com.jjshop.bean.ProductListBean;
import com.jjshop.listener.OnCommonCallBackIm;
import com.jjshop.listener.ProductLoadMoreCallBack;
import com.jjshop.template_view.TemplateGoodsAllHeadView;
import com.jjshop.template_view.TemplateUtil;
import com.jjshop.ui.activity.home.HomeActivity;
import com.jjshop.ui.presenter.HomeItemPresenter;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.Tools;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 18/6/23.
 */

public class HomeItemFragment extends BaseFragment<HomeItemPresenter> implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, SwipeMenuRecyclerView.LoadMoreListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeMenuRecyclerView recyclerView;
    private RelativeLayout root_view;

    private int cid, vid;
    private String url;// 配置的活动链接
    private Activity activity;
    private CommentRecycleAdapter productListAdapter;
    private ArrayList<Object> mListGoods;
    private int page = 1;
    private View mViewLoading, mViewEmpty, mViewError;
    private TextView mTvNoData, mTvRefresh;

    private View mHeadView;
    private boolean mIsNoData = false;
    private boolean mIsLoadingData = false;


    @Override
    protected HomeItemPresenter getPresenter() {
        return new HomeItemPresenter(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_home_item;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            cid = getArguments().getInt("cid", 0);
            vid = getArguments().getInt("vid", 0);
            url = getArguments().getString("url", "");
        }
    }

    public static HomeItemFragment invoke(int cid, int vid, String url){
        HomeItemFragment homeItemFragment = new HomeItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("cid", cid);
        bundle.putInt("vid", vid);
        bundle.putString("url", url);
        homeItemFragment.setArguments(bundle);
        return homeItemFragment;
    }

    @Override
    public void initView(View view) {

        swipeRefreshLayout = view.findViewById(R.id.homefragment_refreshlayout);
        Tools.setRefreshTopColor(swipeRefreshLayout);
        root_view = view.findViewById(R.id.root_view);
        recyclerView = view.findViewById(R.id.homefragment_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        if(isShowHead()){
            mHeadView = headervirew();
            recyclerView.removeHeaderView(mHeadView);
            recyclerView.addHeaderView(mHeadView);
        }
        recyclerView.useDefaultLoadMore();
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLoadMoreListener(this);

        mViewLoading = view.findViewById(R.id.m_view_loading);
        mViewEmpty = view.findViewById(R.id.m_view_empty);
        mViewError = view.findViewById(R.id.m_view_error);
        mTvNoData = view.findViewById(R.id.m_tv_nodate);
        mTvRefresh = view.findViewById(R.id.m_tv_refresh);
        mTvRefresh.setOnClickListener(this);

        mListGoods = new ArrayList<>();
        productListAdapter = new CommentRecycleAdapter(activity, mListGoods,
                new ProductLoadMoreCallBack() {
                    @Override
                    public void loadMore(int position) {
                        if(position == mListGoods.size() - 2 && !mIsNoData && !mIsLoadingData){
                            onLoadMore();
                        }
                    }
                });
        recyclerView.setAdapter(productListAdapter);
        page = 1;
        if(StringUtil.isEmpty(url)){
            loading();
            mPresenter.loadData(false);
        }else{
            mViewLoading.setVisibility(View.GONE);
            mViewEmpty.setVisibility(View.GONE);
            mViewError.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.GONE);
        }

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == 0){// 停止滑动
                    if(null != activity && activity instanceof HomeActivity){
                        ((HomeActivity)activity).scrollShowFuchuang(View.VISIBLE);
                    }
//                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                    //判断是当前layoutManager是否为LinearLayoutManager
//                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
//                    if (null != layoutManager && layoutManager instanceof LinearLayoutManager) {
//                        LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
//                        //获取最后一个可见view的位置
//                        int lastItemPosition = linearManager.findLastVisibleItemPosition();
//                        //获取第一个可见view的位置
//                        int firstItemPosition = linearManager.findFirstVisibleItemPosition();
//                        JjLog.e("firstItemPosition = " + firstItemPosition + ",name = " + mListGoods.get(firstItemPosition).getName());
//                    }
                }else if(newState == 1 || newState == 2){// 滑动中
                    if(null != activity && activity instanceof HomeActivity){
                        ((HomeActivity)activity).scrollShowFuchuang(View.GONE);
                    }
                }

            }
        });

    }

    private View headervirew(){
        View view = activity.getLayoutInflater().inflate(R.layout.template_goods_all_head_view_layout, recyclerView,false);
        return view;
    }

    public void onSuccess(JJHomeBean bean){
        mIsLoadingData = false;
        showData();
        if(isShowHead() && page == 1){// 给头部布局赋值
            JJHomeBean.DataBean dataBean = bean.getData();
            if(null != mHeadView && mHeadView instanceof TemplateGoodsAllHeadView){
                ((TemplateGoodsAllHeadView)mHeadView).getDate(dataBean, null);
            }
        }

        if(page == 1){// 清空商品列表数据
            mListGoods.clear();
            swipeRefreshLayout.setRefreshing(false);
        }
        recyclerView.setAutoLoadMore(true);
        // 商品列表
        List<ProductListBean> listBeans = bean.getData().getIndex_data().getProduct_list();
        if(null == listBeans || listBeans.isEmpty()){
            mIsNoData = true;
            if(page != 1){
                recyclerView.loadMoreFinish(false, false);
            }
            if(page == 1){
                showEmpty();
            }
            return;
        }
        mListGoods.addAll(listBeans);
        productListAdapter.notifyDataSetChanged();
        recyclerView.loadMoreFinish(false, true);
    }

    public void onFail(String msg){
        mIsLoadingData = false;
        if(page == 1){
            swipeRefreshLayout.setRefreshing(false);
            CommonUtils.build().loadLocalData(activity, key(), new OnCommonCallBackIm() {
                @Override
                public void onError(String msg) {
                    showError();
                }
            });
        }
        if(page >= 1){
            recyclerView.setAutoLoadMore(false);
            page = page > 1 ? page - 1 : page;
        }
    }

    public int cid(){
        return cid;
    }

    public int vid(){
        return vid;
    }

    public int page(){
        return page;
    }

    public String key(){// 保存数据的key
        return "home_list_" + String.valueOf(cid) + String.valueOf(vid);
    }

    public String url(){// 保存数据的key
        return url;
    }

    private void loading(){
        if(null == mViewError || null == mViewEmpty || null == mViewLoading || null == swipeRefreshLayout){
            return;
        }
        mViewLoading.setVisibility(View.VISIBLE);
        mViewEmpty.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);
    }
    private void showData(){
        if(null == mViewError || null == mViewEmpty || null == mViewLoading || null == swipeRefreshLayout){
            return;
        }
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewEmpty.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }
    private void showEmpty(){
        if(null == mViewError || null == mViewEmpty || null == mViewLoading || null == swipeRefreshLayout){
            return;
        }
        mViewEmpty.setVisibility(View.VISIBLE);
        mTvNoData.setText("暂时还没有商品哦");
        swipeRefreshLayout.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }
    private void showError(){
        if(null == mViewError || null == mViewEmpty || null == mViewLoading || null == swipeRefreshLayout){
            return;
        }
        mViewError.setVisibility(View.VISIBLE);
        mViewEmpty.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);
    }

    // 是否显示头部
    private boolean isShowHead(){
        if(cid <= 0 && vid <= 0){
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.m_tv_refresh:
                if(isShowHead()){
                    page = 1;
                }
                loading();
                mPresenter.loadData(true);
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        mPresenter.loadData(true);
    }

    @Override
    public void onLoadMore() {
        if(mIsLoadingData){
            return;
        }
        mIsLoadingData = true;
        page = page + 1;
        mPresenter.loadData(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != root_view){
            root_view.removeAllViews();
        }
        if(null != swipeRefreshLayout){
            swipeRefreshLayout.removeAllViews();
        }
        if(null != recyclerView){
            recyclerView.removeAllViews();
        }
    }
}
