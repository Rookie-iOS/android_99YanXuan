package com.jjshop.ui.activity.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.adapter.CommentRecycleAdapter;
import com.jjshop.bean.HotZhuanquBean;
import com.jjshop.base.BaseActivity;
import com.jjshop.bean.JJEvent;
import com.jjshop.bean.ProductListBean;
import com.jjshop.template_view.TemplateUtil;
import com.jjshop.ui.LoginActivity;
import com.jjshop.ui.presenter.HotZhuanquPresenter;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.UrlInvokeUtil;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.Tools;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HotZhuanquActivity extends BaseActivity<HotZhuanquPresenter> implements SwipeMenuRecyclerView.LoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private SwipeRefreshLayout mRefreshLayout;
    private SwipeMenuRecyclerView mRecyclerView;
    private TextView mTvTitle;
    private ImageView mIvBack;
    private CommentRecycleAdapter mAdapter;
    private ArrayList<Object> mListData;
    private static final String TYPE = "type";
    private static final String TITLE = "title";
    private String type = "";
    private String title = "";
    private int page = 1;
    public static final String NEW_GOODS = "New_product_recomment";
    public static final String HOT_GOODS = "Popular_hot_style";

    private View mViewLoading, mViewEmpty, mViewError;
    private TextView mTvNoData, mTvRefresh;

    @Override
    protected HotZhuanquPresenter getPresenter() {
        return new HotZhuanquPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_hot_zhuanqu_layout;
    }

    public static void invoke(Context context, String type, String title){
        Intent intent = new Intent(context, HotZhuanquActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(TITLE, title);
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
        if(null == map || map.size() <= 0 ||
                (!map.containsKey(UrlInvokeUtil.KEY_TITLE) && !map.containsKey(UrlInvokeUtil.KEY_TYPE))){
            return;
        }
        Intent intent = new Intent(context, HotZhuanquActivity.class);
        if(!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            JjLog.e("不是activity");
        }
        intent.putExtra(TYPE, map.get(UrlInvokeUtil.KEY_TYPE));
        intent.putExtra(TITLE, map.get(UrlInvokeUtil.KEY_TITLE));
        context.startActivity(intent);
        map.clear();
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        if(null != getIntent()){
            type = getIntent().getStringExtra(TYPE);
            title = getIntent().getStringExtra(TITLE);
        }
        mIvBack = findViewById(R.id.m_title_back);
        mIvBack.setOnClickListener(this);
        mTvTitle = findViewById(R.id.m_tv_title);
        mRecyclerView = findViewById(R.id.m_recycleview);
        mRefreshLayout = findViewById(R.id.m_refresh);
        Tools.setRefreshTopColor(mRefreshLayout);

        mViewLoading = findViewById(R.id.m_view_loading);
        mViewEmpty = findViewById(R.id.m_view_empty);
        mViewError = findViewById(R.id.m_view_error);
        mTvNoData = findViewById(R.id.m_tv_nodate);
        mTvRefresh = findViewById(R.id.m_tv_refresh);
        mTvRefresh.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.useDefaultLoadMore();
        mRecyclerView.setLoadMoreListener(this);
        mRefreshLayout.setOnRefreshListener(this);

        mListData = new ArrayList<>();
        mAdapter = new CommentRecycleAdapter(this, mListData);
        mRecyclerView.setAdapter(mAdapter);

        mTvTitle.setText(title == null ? "" : title);
        loadData();
    }

    private void loadData(){
        if(null != mPresenter){
            loading();
            mPresenter.loadData();
        }
    }

    public void onSuccess(HotZhuanquBean bean){
        showData();
        ArrayList<ProductListBean> list = bean.getData().getProduct_list();
        mRecyclerView.setAutoLoadMore(true);
        if(null == list || list.isEmpty()){
            if(page == 1){
                mRefreshLayout.setVisibility(View.GONE);
                showEmpty();
            }else{
                mRefreshLayout.setVisibility(View.VISIBLE);
                mRecyclerView.loadMoreFinish(false, false);
            }
            return;
        }
        if(page == 1){
            mRefreshLayout.setRefreshing(false);
            mListData.clear();
        }
        mListData.addAll(list);
        mAdapter.notifyDataSetChanged();
        if(list.size() >= HttpUrl.build().PAGE_SIZE){
            mRecyclerView.loadMoreFinish(false, true);
            return;
        }
        mRecyclerView.loadMoreFinish(false, false);
    }

    public void onFail(String msg){
        if(page == 1){
            mRefreshLayout.setRefreshing(false);
            showError();
        }
        if(page > 1){
            page = page - 1;
            mRecyclerView.setAutoLoadMore(false);
        }

    }

    public String shopId(){
        return PreUtils.getString(this, PreUtils.SHOP_ID, "");
    }

    public String type(){
        return type;
    }

    public int page(){
        return page;
    }

    private void loading(){
        if(null == mViewError || null == mViewEmpty || null == mViewLoading || null == mRefreshLayout){
            return;
        }
        mViewLoading.setVisibility(View.VISIBLE);
        mViewEmpty.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }
    private void showData(){
        if(null == mViewError || null == mViewEmpty || null == mViewLoading || null == mRefreshLayout){
            return;
        }
        mRefreshLayout.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewEmpty.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }
    private void showEmpty(){
        if(null == mViewError || null == mViewEmpty || null == mViewLoading || null == mRefreshLayout){
            return;
        }
        mViewEmpty.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
        mRefreshLayout.setVisibility(View.GONE);
        mTvNoData.setText("暂时还没有商品哦");
    }
    private void showError(){
        if(null == mViewError || null == mViewEmpty || null == mViewLoading || null == mRefreshLayout){
            return;
        }
        mViewError.setVisibility(View.VISIBLE);
        mViewEmpty.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);
        mRefreshLayout.setVisibility(View.GONE);
    }

    public void shareShowHideLoading(int visible){
        if(!(View.GONE == visible || View.VISIBLE == visible) || null == mViewLoading){
            return;
        }
        mViewLoading.setVisibility(visible);
    }

    @Override
    public void onRefresh() {
        page = 1;
        mPresenter.loadData();
    }

    @Override
    public void onLoadMore() {
        page = page + 1;
        mPresenter.loadData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.m_title_back:
                finish();
                break;
            case R.id.m_tv_refresh:
                loadData();
                break;
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        shareShowHideLoading(View.GONE);
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
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
