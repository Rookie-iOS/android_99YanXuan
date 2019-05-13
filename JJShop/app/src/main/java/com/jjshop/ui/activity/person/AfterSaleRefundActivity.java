package com.jjshop.ui.activity.person;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.adapter.CommentRecycleAdapter;
import com.jjshop.base.BaseActivity;
import com.jjshop.bean.AfterSaleRefundBean;
import com.jjshop.template_view.TemplateUtil;
import com.jjshop.ui.presenter.AfterSaleRefundPresenter;
import com.jjshop.utils.Tools;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：张国庆
 * 时间：2018/9/20
 */

public class AfterSaleRefundActivity extends BaseActivity<AfterSaleRefundPresenter> implements SwipeRefreshLayout.OnRefreshListener,
        SwipeMenuRecyclerView.LoadMoreListener{
    @BindView(R.id.m_tv_title)
    TextView mTvTitle;
    @BindView(R.id.m_view_loading)
    View mViewLoading;
    @BindView(R.id.m_view_empty)
    View mViewEmpty;
    @BindView(R.id.m_view_error)
    View mViewError;
    @BindView(R.id.m_tv_nodate)
    TextView mTvNoData;
    @BindView(R.id.m_recycleview)
    SwipeMenuRecyclerView mRecycleview;
    @BindView(R.id.m_refresh)
    SwipeRefreshLayout mRefresh;

    private int page = 1;
    private CommentRecycleAdapter mAdapter;
    private ArrayList<Object> mListData;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_after_sale_refund_layout;
    }

    @Override
    protected AfterSaleRefundPresenter getPresenter() {
        return new AfterSaleRefundPresenter(this);
    }

    public static void invoke(Context context){
        Intent intent = new Intent(context, AfterSaleRefundActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        mTvTitle.setText("售后/退款");
        Tools.setRefreshTopColor(mRefresh);
        mRecycleview.setLayoutManager(new LinearLayoutManager(this));
        mRecycleview.useDefaultLoadMore();
        mRefresh.setOnRefreshListener(this);
        mRecycleview.setLoadMoreListener(this);

        mListData = new ArrayList<>();
        mAdapter = new CommentRecycleAdapter(this, mListData, TemplateUtil.TEMPLATE_1008);
        mRecycleview.setAdapter(mAdapter);

        loadDataList(false);
    }

    // 加载订单列表
    public void loadDataList(boolean isRefreshMore) {
        if (null != mPresenter) {
            if (!isRefreshMore) {
                page = 1;
                loading();
            }
            mPresenter.loadData();
        }
    }

    @OnClick({R.id.m_title_back, R.id.m_tv_refresh})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.m_title_back:
                finish();
                break;
            case R.id.m_tv_refresh:
                loadDataList(false);
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        loadDataList(true);
    }

    @Override
    public void onLoadMore() {
        page = page + 1;
        loadDataList(true);
    }

    // 列表数据获取成功
    public void onSuccessList(AfterSaleRefundBean bean){
        showData();
        ArrayList<AfterSaleRefundBean.AfterSaleRefundData> list = bean.getData();
        if (page == 1) {
            mListData.clear();
            mRefresh.setRefreshing(false);
        }
        mRecycleview.setAutoLoadMore(true);
        if (null == list || list.size() <= 0) {
            if (page == 1) {
                showNoData();
                return;
            }
            mRecycleview.loadMoreFinish(false, false);
            return;
        }
        mListData.addAll(list);
        mAdapter.notifyDataSetChanged();
        mRecycleview.loadMoreFinish(false, true);
    }

    // 列表数据加载失败
    public void onErrorList(String msg){
        if (page <= 1) {
            showError();
        }
        if (page > 1) {
            page = page - 1;
            mRecycleview.setAutoLoadMore(false);
        }
    }

    public int page(){
        return page;
    }

    private void loading() {
        mViewLoading.setVisibility(View.VISIBLE);
        mViewEmpty.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }

    private void showData() {
        mRefresh.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewEmpty.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }

    private void showNoData() {
        mViewEmpty.setVisibility(View.VISIBLE);
        mRefresh.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
        mTvNoData.setText("还没有订单哦");
    }

    private void showError() {
        mViewError.setVisibility(View.VISIBLE);
        mViewEmpty.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);
        mRefresh.setVisibility(View.GONE);
    }
}
