package com.jjshop.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.adapter.CommentRecycleAdapter;
import com.jjshop.base.BaseFragment;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.JJEvent;
import com.jjshop.bean.MyOrderBean;
import com.jjshop.bean.WxPayBean;
import com.jjshop.template_view.TemplateUtil;
import com.jjshop.ui.activity.person.MyOrderActivity;
import com.jjshop.ui.presenter.OrderPresenter;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.Tools;
import com.jjshop.utils.WXUtil;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：张国庆
 * 时间：2018/9/17
 */

public class OrderFragment extends BaseFragment<OrderPresenter> implements SwipeRefreshLayout.OnRefreshListener,
        SwipeMenuRecyclerView.LoadMoreListener {
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

    private int id, page = 1;
    private CommentRecycleAdapter mAdapter;
    private ArrayList<Object> mListData;
    private long orderNo;
    private String payType = "", status = "";

    @Override
    protected OrderPresenter getPresenter() {
        return new OrderPresenter(this);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_order_layout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id", 0);
        }
    }

    public static OrderFragment invoke(int id) {
        OrderFragment orderFragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        orderFragment.setArguments(bundle);
        return orderFragment;
    }


    @Override
    public void initView(View view) {
        Tools.setRefreshTopColor(mRefresh);
        mRecycleview.setLayoutManager(new LinearLayoutManager(activity));
        mRecycleview.useDefaultLoadMore();
        mRefresh.setOnRefreshListener(this);
        mRecycleview.setLoadMoreListener(this);

        mListData = new ArrayList<>();
        mAdapter = new CommentRecycleAdapter(activity, mListData);
        mRecycleview.setAdapter(mAdapter);
        if(id == 1){
            loadDataList(false);
        }

    }

    @OnClick(R.id.m_tv_refresh)
    public void onClick() {
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

    // 加载微信支付数据
    public void loadDataWxPay(long orderNo, String payType) {
        if (null != mPresenter) {
            this.orderNo = orderNo;
            this.payType = payType;
            mViewLoading.setVisibility(View.VISIBLE);
            mPresenter.loadPayOrderData();
        }
    }

    // 取消订单请求
    public void loadCancelOrder(long orderNo, String status) {
        if (null != mPresenter) {
            this.orderNo = orderNo;
            this.status = status;
            mViewLoading.setVisibility(View.VISIBLE);
            mPresenter.loadCancelOrder();
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

    public void onSuccessList(MyOrderBean.MyOrderData bean) {
        showData();
        // 显示标签数量
        MyOrderBean.MyOrderMenuNums menuNums = bean.getMenu_nums();
        if(null != menuNums && null != activity && activity instanceof MyOrderActivity){
            ((MyOrderActivity)activity).setTabNum(menuNums.getPending_payment(),
                    menuNums.getPending_delivery(), menuNums.getPending_goods());
        }
        ArrayList<MyOrderBean.MyOrderDataList> list = bean.getData();
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
        mPresenter.splitData(list);// 拆分数据，添加集合中
        mAdapter.notifyDataSetChanged();
        mRecycleview.loadMoreFinish(false, true);
    }

    public void onErrorList(String msg) {
        if (page <= 1) {
            showError();
        }
        if (page > 1) {
            mRecycleview.setAutoLoadMore(false);
        }
    }

    // 获取微信支付数据成功
    public void onPayOrderSuccess(WxPayBean.WxPayData bean){
        mViewLoading.setVisibility(View.GONE);
        WXUtil.getWxUtil().pay(activity, bean);
    }

    // 获取微信支付数据失败
    public void onPayOrderFail(String msg){
        JjToast.getInstance().toast(msg);
        mViewLoading.setVisibility(View.GONE);
    }

    // 取消订单成功
    public void onCancelOrderSuccess(BaseBean bean){
        mViewLoading.setVisibility(View.GONE);
        loadDataList(false);
        EventBus.getDefault().post(new JJEvent(JJEvent.REFRESH_PERSON_INFO));
    }

    // 取消订单失败
    public void onCancelOrderFail(String msg){
        JjToast.getInstance().toast(msg);
        mViewLoading.setVisibility(View.GONE);
    }

    public int page() {
        return page;
    }

    public int id() {
        return id;
    }

    public long orderNo(){
        return orderNo;
    }

    public String payType(){
        return payType;
    }

    public String status(){
        return status;
    }

    public ArrayList<Object> mListData(){
        return mListData;
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
