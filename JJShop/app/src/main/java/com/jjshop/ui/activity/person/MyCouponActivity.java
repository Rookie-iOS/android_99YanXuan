package com.jjshop.ui.activity.person;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.adapter.CommentRecycleAdapter;
import com.jjshop.base.BaseActivity;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.CouponDataBean;
import com.jjshop.bean.MyCouponBean;
import com.jjshop.template_view.TemplateUtil;
import com.jjshop.ui.presenter.MyCouponPresenter;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.Tools;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：张国庆
 * 时间：2018/12/4
 */

public class MyCouponActivity extends BaseActivity<MyCouponPresenter> implements SwipeMenuRecyclerView.LoadMoreListener {
    @BindView(R.id.m_tv_title)
    TextView mTvTitle;
    @BindView(R.id.m_recycleview)
    SwipeMenuRecyclerView mRecyclerView;
    @BindView(R.id.m_view_loading)
    View mViewLoading;
    @BindView(R.id.m_view_empty)
    View mViewEmpty;
    @BindView(R.id.m_view_error)
    View mViewError;
    @BindView(R.id.m_tv_nodate)
    TextView mTvNodate;

    private ArrayList<Object> mListData;
    private CommentRecycleAdapter mAdapter;
    private String mCouonCode = "";// 输入的优惠码

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_coupon_layout;
    }

    @Override
    protected MyCouponPresenter getPresenter() {
        return new MyCouponPresenter(this);
    }

    public static void invoke(Context context){
        Intent intent = new Intent(context, MyCouponActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        mTvTitle.setText("我的优惠券");
        mTvNodate.setText("暂时还没有优惠券");
        View couponTopView = LayoutInflater.from(this).inflate(R.layout.template_coupon_top_view_layout, null);
        mRecyclerView.addHeaderView(couponTopView);
        mRecyclerView.setLoadMoreListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.useDefaultLoadMore();

        mListData = new ArrayList<>();
        mAdapter = new CommentRecycleAdapter(this, mListData, TemplateUtil.TEMPLATE_1004);
        mRecyclerView.setAdapter(mAdapter);
        loadData();
    }

    private void loadData(){
        if(null != mPresenter){
            showLoading();
            mPresenter.loadData();
        }
    }

    public void loadDuihuanData(String etCouponCode){
        if(null != mPresenter){
            mCouonCode = etCouponCode;
            showLoading();
            mPresenter.loadDuihuanData();
        }
    }

    @OnClick({R.id.m_title_back, R.id.m_tv_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_title_back:
                Tools.hideSoftInput(view);
                finish();
                break;
            case R.id.m_tv_refresh:
                loadData();
                break;
        }
    }

    // 列表数据加载成功
    public void onSuccessList(MyCouponBean bean){
        mListData.clear();
        if(null == bean){
            showEmpty();
            return;
        }
        ArrayList<CouponDataBean> list = bean.getData();
        if(null == list || list.isEmpty()){
            showEmpty();
            return;
        }
        showData();
        mListData.addAll(list);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.loadMoreFinish(false, false);
    }

    // 列表数据加载失败
    public void onError(String msg){
        showError();
    }

    // 兑换数据加载成功
    public void onDuihuanSuccess(BaseBean bean){
        loadData();
    }

    // 兑换数据加载失败
    public void onDuihuanError(String msg){
        showData();
        JjToast.getInstance().toast(msg);
    }

    // 获取输入框输入的优惠码
    public String discountCode(){
        if(StringUtil.isEmpty(mCouonCode)){
            return "";
        }
        return mCouonCode;
    }

    // 滑动到顶部
    public void scrollTop(){
        if(null != mRecyclerView){
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    private void showLoading(){
        if(null == mViewError || null == mViewLoading || null == mViewEmpty){
            return;
        }
        mViewLoading.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);
        mViewEmpty.setVisibility(View.GONE);
    }

    private void showData(){
        if(null == mViewError || null == mRecyclerView ||  null == mViewLoading || null == mViewEmpty){
            return;
        }
        mRecyclerView.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewEmpty.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }

    private void showEmpty(){
        if(null == mViewError || null == mRecyclerView || null == mViewLoading || null == mViewEmpty){
            return;
        }
        mViewEmpty.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);
    }

    private void showError(){
        if(null == mViewError || null == mRecyclerView || null == mViewLoading || null == mViewEmpty){
            return;
        }
        mViewError.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);
        mViewEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        Tools.hideSoftInput(mTvTitle);
        super.onBackPressed();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mRecyclerView){
            mRecyclerView.removeAllViews();
        }
        if(null != mListData){
            mListData.clear();
        }
    }
}
