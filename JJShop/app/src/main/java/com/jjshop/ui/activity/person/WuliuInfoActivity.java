package com.jjshop.ui.activity.person;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.adapter.CommentListAdapter;
import com.jjshop.base.BaseActivity;
import com.jjshop.bean.OrderDetailInfoBean;
import com.jjshop.bean.WuliuInfoBean;
import com.jjshop.template_view.TemplateUtil;
import com.jjshop.ui.presenter.WuliuInfoPresenter;
import com.jjshop.utils.StringUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：张国庆
 * 时间：2018/9/27
 */

public class WuliuInfoActivity extends BaseActivity<WuliuInfoPresenter> {
    @BindView(R.id.m_tv_title)
    TextView mTvTitle;
    @BindView(R.id.m_listview)
    ListView mListview;
    @BindView(R.id.m_view_loading)
    View mViewLoading;
    @BindView(R.id.m_view_error)
    View mViewError;

    private View mHeadView;
    private TextView mTvAddress;
    private CommentListAdapter mAdapter;
    private ArrayList<Object> mListData;
    private long orderno;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_wuliu_info_layout;
    }

    @Override
    protected WuliuInfoPresenter getPresenter() {
        return new WuliuInfoPresenter(this);
    }

    public static void invoke(Context context, long orderno){
        Intent intent = new Intent(context, WuliuInfoActivity.class);
        intent.putExtra("orderno", orderno);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        mTvTitle.setText("订单跟踪");
        mHeadView = LayoutInflater.from(this).inflate(R.layout.include_wuliu_top_layout, null);
        mTvAddress = mHeadView.findViewById(R.id.m_tv_address);
    }

    @Override
    protected void initData() {
        if(null != getIntent()){
            orderno = getIntent().getLongExtra("orderno", 0);
        }
        mListData = new ArrayList<>();
        mAdapter = new CommentListAdapter(this, mListData);
        mListview.setAdapter(mAdapter);
        mListview.addHeaderView(mHeadView);
        loadData();
    }

    public void loadData(){
        if(null != mPresenter){
            loading();
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
                loadData();
                break;
        }
    }

    // 获取物流信息成功
    public void onSuccess(WuliuInfoBean.WuliuInfoBeanData bean){
        showData();
        OrderDetailInfoBean detailInfoBean = bean.getOrderinfo();
        if(null != detailInfoBean){
            // 收货地址
            mTvAddress.setText(detailInfoBean.getProvince() + detailInfoBean.getCity() +
                    detailInfoBean.getArea() + detailInfoBean.getAddress());
        }
        ArrayList<WuliuInfoBean.WuliuInfoBeanExpressinfo> expressinfo = bean.getExpressinfo();
        if(expressinfo != null){
            mPresenter.splitData(expressinfo);
            mAdapter.notifyDataSetChanged();
        }
    }

    // 获取物流信息失败
    public void onFail(String msg){
        showError();
    }

    public long orderno(){
        return orderno;
    }

    public ArrayList<Object> mListData(){
        return mListData;
    }

    private void loading() {
        mViewLoading.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);
    }

    private void showData() {
        mListview.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }

    private void showError() {
        mViewError.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mListview.setVisibility(View.GONE);
    }
}
