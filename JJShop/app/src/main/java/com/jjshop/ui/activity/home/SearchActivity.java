package com.jjshop.ui.activity.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.adapter.CommentRecycleAdapter;
import com.jjshop.base.BaseActivity;
import com.jjshop.bean.JJEvent;
import com.jjshop.bean.ProductListBean;
import com.jjshop.bean.SearchBean;
import com.jjshop.ui.presenter.SearchPresenter;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.Tools;
import com.jjshop.utils.UIUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity<SearchPresenter> implements
        SwipeMenuRecyclerView.LoadMoreListener {
    @BindView(R.id.m_tv_search_history)
    TextView mTvSearchHistory;
    @BindView(R.id.m_search_flow)
    TagFlowLayout mSearchFlow;
    @BindView(R.id.m_tv_clear_history)
    TextView mTvClearHistory;
    private ImageView mIvBack;
    private EditText mEditContent;
    private LinearLayout mSearchLayout;
    private TextView mTvSearch;
    private SwipeMenuRecyclerView swipeMenuRecyclerView;

    private View mViewLoading, mViewEmpty, mViewError;
    private TextView mTvNoData, mTvRefresh;

    private ArrayList<Object> mListGoods;
    private CommentRecycleAdapter otherAdapter;
    private int page = 1;
    private boolean mIsloading = false;
    private Timer timer;
    private String mSearchContent = "";
    private MyTagAdapter myTagAdapter;
    private List<String> myTagList;

    @Override
    protected int setLayoutId() {
        return R.layout.layout_search;
    }

    @Override
    protected SearchPresenter getPresenter() {
        return new SearchPresenter(this);
    }

    public static void invoke(Context context) {
        Intent in = new Intent(context, SearchActivity.class);
        context.startActivity(in);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        swipeMenuRecyclerView = findViewById(R.id.search_recycleview);
        swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeMenuRecyclerView.useDefaultLoadMore();
        swipeMenuRecyclerView.setLoadMoreListener(this);

        mIvBack = findViewById(R.id.m_title_back);
        mEditContent = findViewById(R.id.m_edit_content);
        mTvSearch = findViewById(R.id.m_title_right_tv);
        mTvSearch.setVisibility(View.VISIBLE);
        mSearchLayout = findViewById(R.id.m_title_search_layout);
        mSearchLayout.setVisibility(View.VISIBLE);

        mViewLoading = findViewById(R.id.m_view_loading);
        mViewEmpty = findViewById(R.id.m_view_empty);
        mViewError = findViewById(R.id.m_view_error);
        mTvNoData = findViewById(R.id.m_tv_nodate);
        mTvRefresh = findViewById(R.id.m_tv_refresh);

        mListGoods = new ArrayList<>();
        otherAdapter = new CommentRecycleAdapter(this, mListGoods);
        swipeMenuRecyclerView.setAdapter(otherAdapter);
        swipeMenuRecyclerView.setVisibility(View.GONE);

        // 监听键盘的搜索按钮
        mEditContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
        cancelTimer();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                cancelTimer();
                Tools.showSoftInput(mEditContent);
            }
        }, 100);

        myTagList = new ArrayList<>();
        // 加载搜索关键字历史数据
        mPresenter.loadHistoryData();
        mSearchFlow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if(null != mEditContent && null != myTagList && position < myTagList.size()){
                    String string = myTagList.get(position);
                    if(!StringUtil.isEmpty(string)){
                        mEditContent.setText(string);
                        mEditContent.setSelection(string.length());
                    }
                    search();
                }
                return true;
            }
        });

    }

    @OnClick({R.id.m_tv_clear_history, R.id.m_title_back, R.id.m_title_right_tv, R.id.m_tv_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_title_back:
                Tools.hideSoftInput(mEditContent);
                finish();
                break;
            case R.id.m_title_right_tv:
                search();
                break;
            case R.id.m_tv_clear_history:
                PreUtils.setString(this, PreUtils.SEARCH_HISTORY, "");
                myTagList.clear();
                myTagAdapter.notifyDataChanged();
                showHideHistory(View.GONE);
                break;
            case R.id.m_tv_refresh:
                page = 1;
                loadData();
                break;
        }
    }

    // 执行搜索操作
    private void search() {
        if (StringUtil.isEmpty(editContent())) {
            JjToast.getInstance().toast("请输入搜索关键字");
            return;
        }
        showHideHistory(View.GONE);
        mSearchContent = editContent();
        saveSearchContent();
        page = 1;
        loadData();
    }

    // 保存搜索关键字
    private void saveSearchContent() {
        if(null != mPresenter){
            mPresenter.saveSearchContent();
            notifyTagAdapter();
        }
    }

    // 刷新关键字列表数据
    private void notifyTagAdapter(){
        if(null == myTagList || null == mSearchFlow){
            return;
        }
        if (null == myTagAdapter) {
            myTagAdapter = new MyTagAdapter(myTagList);
            mSearchFlow.setAdapter(myTagAdapter);
        } else {
            myTagAdapter.notifyDataChanged();
        }
    }

    @Override
    public void onLoadMore() {
        page = page + 1;
        loadData();
    }

    // 加载搜索数据
    private void loadData() {
        Tools.hideSoftInput(mEditContent);
        if (page == 1) {
            loading();
        }
        if (mIsloading) {// 加载中，不往下执行
            return;
        }
        mIsloading = true;
        mPresenter.loadData();
    }

    // 获取到搜索关键字的历史数据
    public void onHistorySuccess(List<String> list){
        if (null == list || list.isEmpty()) {
            return;
        }
        showHideHistory(View.VISIBLE);
        myTagList.addAll(list);
        notifyTagAdapter();
    }

    public void onSuccess(SearchBean bean) {
        mIsloading = false;
        showData();
        if (page == 1) {
            mListGoods.clear();
        }
        swipeMenuRecyclerView.setAutoLoadMore(true);
        List<ProductListBean> goodsList = bean.getData().getProducts().getProduct_list();
        if (null == goodsList || goodsList.isEmpty()) {
            if (page == 1) {
                showEmpty();
            } else {
                swipeMenuRecyclerView.loadMoreFinish(false, false);
            }
            return;
        }
        mListGoods.addAll(goodsList);
        otherAdapter.notifyDataSetChanged();
        swipeMenuRecyclerView.loadMoreFinish(false, true);
    }

    public void onFail(String msg) {
        mIsloading = false;
        if (page == 1) {
            showError();
        }
        if (page > 1) {
            page = page - 1;
            swipeMenuRecyclerView.setAutoLoadMore(false);
        }
    }

    public String content() {
        if (StringUtil.isEmpty(mSearchContent)) {
            return "";
        }
        return mSearchContent;
    }

    public String editContent() {
        if (null == mEditContent) {
            return "";
        }
        String search = mEditContent.getText().toString();
        if (StringUtil.isEmpty(search)) {
            return "";
        }
        return search;
    }

    public int page() {
        return page;
    }

    public List<String> myTagList() {
        return myTagList;
    }


    @Override
    public void onBackPressed() {
        Tools.hideSoftInput(mEditContent);
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewLoading.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewLoading.setVisibility(View.GONE);
    }

    // 显示隐藏搜索历史布局
    private void showHideHistory(int vis){
        if (!(View.GONE == vis || View.VISIBLE == vis) || null == mTvSearchHistory ||
                null == mSearchFlow || null == mTvClearHistory) {
            return;
        }
        mTvSearchHistory.setVisibility(vis);
        mSearchFlow.setVisibility(vis);
        mTvClearHistory.setVisibility(vis);
    }

    public void shareShowHideLoading(int visible) {
        if (!(View.GONE == visible || View.VISIBLE == visible) || null == mViewLoading) {
            return;
        }
        mViewLoading.setVisibility(visible);
    }

    private void loading() {
        if (null == mViewError || null == mViewEmpty || null == mViewLoading) {
            return;
        }
        mViewLoading.setVisibility(View.VISIBLE);
        mViewEmpty.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }

    private void showData() {
        if (null == mViewError || null == mViewEmpty || null == mViewLoading
                || null == swipeMenuRecyclerView) {
            return;
        }
        swipeMenuRecyclerView.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewEmpty.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }

    private void showEmpty() {
        if (null == mViewError || null == mViewEmpty || null == mViewLoading
                || null == swipeMenuRecyclerView) {
            return;
        }
        mViewEmpty.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
        swipeMenuRecyclerView.setVisibility(View.GONE);
        mTvNoData.setText("没有找到相关商品，换个关键字试试");
    }

    private void showError() {
        if (null == mViewError || null == mViewEmpty || null == mViewLoading
                || null == swipeMenuRecyclerView) {
            return;
        }
        mViewError.setVisibility(View.VISIBLE);
        mViewEmpty.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);
        swipeMenuRecyclerView.setVisibility(View.GONE);
    }

    // 取消定时器
    private void cancelTimer() {
        if (null != timer) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private class MyTagAdapter extends TagAdapter<String> {
        final int paddingTb = UIUtils.dip2px(5);
        final int paddingLr = UIUtils.dip2px(15);

        public MyTagAdapter(List<String> datas) {
            super(datas);
        }

        @Override
        public View getView(FlowLayout parent, int position, String s) {
            TextView textView = new TextView(SearchActivity.this);
            textView.setPadding(paddingLr, paddingTb, paddingLr, paddingTb);
            textView.setBackgroundResource(R.drawable.yuanjiao_f4f4f4_select);
            textView.setTextColor(getResources().getColor(R.color.color_1b1b1b));
            textView.setTextSize(15f);
            textView.setText(s);
            return textView;
        }

        @Override
        public void onSelected(int position, View view) {
            if (null != view && view instanceof TextView) {
                ((TextView) view).setBackgroundResource(R.drawable.yuanjiao_f4f4f4_select);
                ((TextView) view).setTextColor(getResources().getColor(R.color.color_1b1b1b));
            }
        }

        @Override
        public void unSelected(int position, View view) {
            if (null != view && view instanceof TextView) {
                ((TextView) view).setBackgroundResource(R.drawable.yuanjiao_f4f4f4_select);
                ((TextView) view).setTextColor(getResources().getColor(R.color.color_1b1b1b));
            }
        }
    }

    @Subscribe
    public void onEvent(JJEvent event) {
        if (null == event) {
            return;
        }
        int id = event.getEventId();
        switch (id) {
            case JJEvent.FINISH_ACTIVITY:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
        EventBus.getDefault().unregister(this);
    }
}
