package com.jjshop.ui.activity.person;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.adapter.CommentRecycleAdapter;
import com.jjshop.base.BaseActivity;
import com.jjshop.bean.AddressListBean;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.JJEvent;
import com.jjshop.template_view.TemplateUtil;
import com.jjshop.ui.presenter.MyAddressPresenter;
import com.jjshop.utils.JjToast;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：张国庆
 * 时间：2018/8/16
 */

public class MyAddressActivity extends BaseActivity<MyAddressPresenter> implements SwipeMenuRecyclerView.LoadMoreListener{
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
    @BindView(R.id.m_tv_add_address)
    TextView mTvAddress;
    @BindView(R.id.m_tv_nodate)
    TextView mTvNoData;

    private int mInvokeType = -1;
    public static final int INVOKE_SUBMIT_ORDER = 1;
    public static final int INVOKE_PERSON = 2;

    private ArrayList<Object> mListData;
    private CommentRecycleAdapter mAdapter;
    private String mAddressId = "";

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_address_layout;
    }

    @Override
    protected MyAddressPresenter getPresenter() {
        return new MyAddressPresenter(this);
    }

    public static void invoke(Context context, int type){
        Intent intent = new Intent(context, MyAddressActivity.class);
        intent.putExtra("invokeType", type);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        if(null != getIntent()){
            mInvokeType = getIntent().getIntExtra("invokeType", -1);
        }
        mTvTitle.setText("地址管理");
        mTvNoData.setText("还没有地址，点击添加新地址去添加吧");
        mRecyclerView.setLoadMoreListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.useDefaultLoadMore();
    }

    @Override
    protected void initData() {
        mListData = new ArrayList<>();
        mAdapter = new CommentRecycleAdapter(this, mListData, TemplateUtil.TEMPLATE_1003);
        mRecyclerView.setAdapter(mAdapter);
        loadData();
    }

    // 调用列表接口
    public void loadData(){
        if(null != mPresenter){
            showLoading();
            mPresenter.loadData();
        }
    }

    // 调用删除接口
    public void loadDelData(String addressId){
        if(null != mPresenter){
            mAddressId = addressId;
            mViewLoading.setVisibility(View.VISIBLE);
            mPresenter.loadDelData();
        }
    }

    // 调用设置默认接口
    public void loadDefaultData(String addressId){
        if(null != mPresenter){
            mAddressId = addressId;
            mViewLoading.setVisibility(View.VISIBLE);
            mPresenter.loadDefaultData();
        }
    }

    @OnClick({R.id.m_title_back, R.id.m_tv_add_address, R.id.m_tv_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_title_back:
                finish();
                break;
            case R.id.m_tv_add_address:
                UpdateAddAddressActivity.invoke(this, mInvokeType, addressSize());
                break;
            case R.id.m_tv_refresh:
                loadData();
                break;
        }
    }

    // 列表数据获取成功
    public void onListSuccess(AddressListBean.AddressListData bean){
        showData();
        mListData.clear();
        ArrayList<AddressListBean.AddressList> list = bean.getData();
        if(null != list && list.size() > 0){
            mListData.addAll(list);
        }else{
            showEmpty();
        }
        mAdapter.notifyDataSetChanged();
    }

    // 删除成功、设置默认成功
    public void onDelSuccess(BaseBean bean){
        mViewLoading.setVisibility(View.VISIBLE);
        mPresenter.loadData();
        if(mInvokeType == INVOKE_SUBMIT_ORDER){
            // 刷新确认订单的地址信息
            EventBus.getDefault().post(new JJEvent(JJEvent.SUBMIT_ORDER_REFRESH_ADDRESS, ""));
        }
    }

    // 删除失败、设置默认失败
    public void onDelFail(String msg){
        mViewLoading.setVisibility(View.GONE);
        JjToast.getInstance().toast(msg);
    }

    public void onFail(String msg){
        showError();
    }

    public String addressId(){
        return mAddressId;
    }

    // 刷新确认订单的地址信息
    public void selectAddress(String addressId){
        if(mInvokeType == -1){
            return;
        }
        if(mInvokeType == INVOKE_SUBMIT_ORDER){
            EventBus.getDefault().post(new JJEvent(JJEvent.SUBMIT_ORDER_REFRESH_ADDRESS, addressId));
            finish();
            return;
        }
    }

    // 修改地址
    public void editAddress(AddressListBean.AddressList bean){
        UpdateAddAddressActivity.invoke(this, bean, mInvokeType, addressSize());
    }

    private int addressSize(){
        if(null == mListData){
            return 0;
        }
        return mListData.size();
    }

    @Override
    public void onLoadMore() {

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
        if(null == mViewError || null == mRecyclerView || null == mTvAddress
                || null == mViewLoading || null == mViewEmpty){
            return;
        }
        mRecyclerView.setVisibility(View.VISIBLE);
        mTvAddress.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewEmpty.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }

    private void showEmpty(){
        if(null == mViewError || null == mRecyclerView || null == mTvAddress
                || null == mViewLoading || null == mViewEmpty){
            return;
        }
        mViewEmpty.setVisibility(View.VISIBLE);
        mTvAddress.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);
    }

    private void showError(){
        if(null == mViewError || null == mRecyclerView || null == mTvAddress
                || null == mViewLoading || null == mViewEmpty){
            return;
        }
        mViewError.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mTvAddress.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);
        mViewEmpty.setVisibility(View.GONE);
    }

    @Subscribe
    public void onEvent(JJEvent event){
        if(null == event){
            return;
        }
        int id = event.getEventId();
        switch (id){
            case JJEvent.REFRESH_ADDRESS:
                loadData();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
