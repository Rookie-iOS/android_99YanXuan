package com.jjshop.ui.fragment;

import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.adapter.CommentRecycleAdapter;
import com.jjshop.base.BaseFragment;
import com.jjshop.bean.CartDataBean;
import com.jjshop.bean.JJEvent;
import com.jjshop.bean.ProductListBean;
import com.jjshop.bean.ShoppingCartBean;
import com.jjshop.bean.UpdateCartNumBean;
import com.jjshop.dialog.CartNumDelDialog;
import com.jjshop.template_view.TemplateUtil;
import com.jjshop.ui.activity.home.HomeActivity;
import com.jjshop.ui.activity.home.SubmitOrderActivity;
import com.jjshop.ui.presenter.ShoppingCartPresenter;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.InvokeMarketUtil;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.UIUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 购物车
 */

public class ShopCarFragment extends BaseFragment<ShoppingCartPresenter> implements SwipeMenuRecyclerView.LoadMoreListener/*,
        SwipeRefreshLayout.OnRefreshListener*/{

    @BindView(R.id.m_title_back)
    ImageView mTitleBack;
    @BindView(R.id.m_tv_title)
    TextView mTvTitle;
    @BindView(R.id.m_title_right_tv)
    TextView mTvEdit;
    @BindView(R.id.m_recycleview)
    SwipeMenuRecyclerView mRecycleview;
//    @BindView(R.id.m_refresh)
//    SwipeRefreshLayout mRefresh;
    @BindView(R.id.m_checkBox)
    CheckBox mCheckBox;
    @BindView(R.id.m_tv_discount_price)
    TextView mTvDiscountPrice;
    @BindView(R.id.m_tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.m_tv_buy)
    TextView mTvBuy;
    @BindView(R.id.m_tv_nodate)
    TextView mTvNodate;
    @BindView(R.id.m_tv_refresh)
    TextView mTvRefresh;
    @BindView(R.id.m_layout_check)
    LinearLayout mLayoutCheck;
    @BindView(R.id.m_view_loading)
    View mViewLoading;
    @BindView(R.id.m_view_error)
    View mViewError;
    @BindView(R.id.layout_bottom)
    RelativeLayout mLayoutBottom;
    @BindView(R.id.m_view_status)
    View mViewStatus;

    private CommentRecycleAdapter mAdapter;
    private ArrayList<Object> mListObject = new ArrayList<>();
    private int mUpdateNum = 1;
    private long cart_id;
    private String sku = "";
    private int select = 1;
    private String cart_ids = "";
    private boolean mIsLoading = false;


    @Override
    protected ShoppingCartPresenter getPresenter() {
        return new ShoppingCartPresenter(this);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_shopcar;
    }

    @Override
    public void initView(View view) {
        EventBus.getDefault().register(this);
        mRecycleview.setLoadMoreListener(this);
//        mRefresh.setOnRefreshListener(this);
//        Tools.setRefreshTopColor(mRefresh);
        mTitleBack.setVisibility(View.GONE);
        mTvTitle.setText("购物车");
        mTvEdit.setText("编辑");
        mTvEdit.setVisibility(View.VISIBLE);
        mListObject = new ArrayList<>();

        mRecycleview.setLayoutManager(new GridLayoutManager(activity, 1));
        mRecycleview.useDefaultLoadMore();
        mAdapter = new CommentRecycleAdapter(activity, mListObject);
        mRecycleview.setAdapter(mAdapter);
        mRecycleview.loadMoreFinish(false, false);
        mTvEdit.setVisibility(View.GONE);
        mLayoutBottom.setVisibility(View.GONE);
//        mRefresh.setVisibility(View.GONE);
        mRecycleview.setVisibility(View.GONE);
        // 沉浸式，增加一个状态栏的高度
        setTitleLayout();
//        loadData();
    }

    private void setTitleLayout(){
        if(null == mViewStatus){
            return;
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mViewStatus.getLayoutParams();
        params.height = UIUtils.getStatusHeight(activity);
        mViewStatus.setLayoutParams(params);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M &&
                InvokeMarketUtil.build().getRomType() != InvokeMarketUtil.ROM_TYPE.MIUI){// 小于6.0设置成黑色半透明
            mViewStatus.setBackgroundColor(getResources().getColor(R.color.color_80333333));
        }
    }

    public void loadData(){
        if(null != mPresenter){
            if(mIsLoading){
                return;
            }
            CommonUtils.build().otherLog(activity, getClass().getName());
            mIsLoading = true;
            showLoading();
            mPresenter.loadData();
        }
    }

    public void loadUpdateNumData(int num, CartDataBean bean){
        if(null != mPresenter){
            showLoading();
            mUpdateNum = num;
            cart_id = bean.getCart_id();
            sku = bean.getColor().getSize().getSku();
            mPresenter.loadUpdateNumData();
        }
    }

    public void loadDelData(String cart_id){
        if(null != mPresenter){
            cart_ids = cart_id;
            showLoading();
            mPresenter.loadDelData();
        }
    }

    public void loadSelectData(int select, String cart_id){
        if(null != mPresenter){
            showLoading();
            this.select = select;
            this.cart_ids = cart_id;
            mPresenter.loadSelectData();
        }
    }

    // 获取购物车列表数据成功
    public void onSuccess(ShoppingCartBean.ShoppingCartData bean){
        mIsLoading = false;
        showData();
        mListObject.clear();
        ShoppingCartBean.ShoppingCartData.CartData cartData = bean.getData();
        mTvEdit.setVisibility(View.VISIBLE);
        mLayoutCheck.setVisibility(View.VISIBLE);
        if(null != cartData){
            ArrayList<CartDataBean> listCart = cartData.getProducts();
            if(null == listCart){
                listCart = new ArrayList<>();
            }
            if(listCart.size() <= 0){
                mTvEdit.setVisibility(View.GONE);
                mLayoutBottom.setVisibility(View.GONE);
                setEdit("编辑", "去结算", R.color.color_d6004f, View.GONE);
                CartDataBean listBean = new CartDataBean();
                listBean.setTemplateType(TemplateUtil.TEMPLATE_1001);
                listCart.add(listBean);
            }
            for(CartDataBean cart : listCart){
                if(null != cart){
                    if(cart.getTemplateType() != TemplateUtil.TEMPLATE_1001){
                        cart.setTemplateType(TemplateUtil.TEMPLATE_1000);
                    }
                    mListObject.add(cart);
                }
            }
            setTextAllPrice(cartData.getTotal_price());
            setTextDiscountPrice(cartData.getDiscount());
        }else {
            mTvEdit.setVisibility(View.GONE);
            mLayoutBottom.setVisibility(View.GONE);
            setEdit("编辑", "去结算", R.color.color_d6004f, View.GONE);
            CartDataBean listBean = new CartDataBean();
            listBean.setTemplateType(TemplateUtil.TEMPLATE_1001);
            mListObject.add(listBean);
        }

        ArrayList<ProductListBean> listGoods = bean.getRec_list();
        if(null != listGoods && listGoods.size() >= 1){
            if(null != mPresenter.getGoodsList(listGoods)){
                mListObject.addAll(mPresenter.getGoodsList(listGoods));
            }
        }
        notifyData();
        setCheck();

        // 购物车数量
        if(null != activity && activity instanceof HomeActivity){
            ((HomeActivity)activity).setCartNum(bean.getCartProfuctNum());
        }
    }

    private int mErrorNum;
    public void onFail(String str){
        mIsLoading = false;
        mErrorNum++;
        if(mErrorNum <= 2){
            loadData();
            return;
        }
        mErrorNum = 0;
        showError();
    }

    // 修改数量成功
    public void onUpdateNumSuccess(UpdateCartNumBean.UpdateCartNumData bean){
        if(mUpdateNum >= bean.getStock()){
            JjToast.getInstance().toast("库存最多为" + bean.getStock());
        }
        loadData();
    }

    public void onDelSelectSuccess(){
        loadData();
    }

    public void onUpdateNumFail(String str){
        showData();
        JjToast.getInstance().toast(str);
    }

    @OnClick({R.id.m_tv_buy, R.id.m_title_right_tv, R.id.m_layout_check, R.id.m_tv_refresh})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.m_tv_buy:// 结算、删除
                if(null == mPresenter || null == mTvBuy){
                    return;
                }
                if(StringUtil.isEmpty(mPresenter.getSelectId())){
                    JjToast.getInstance().toast("至少选择一件商品");
                    return;
                }
                if(mTvBuy.getText().toString().equals("去结算")){
                    SubmitOrderActivity.invoke(activity);
                    return;
                }
                CartNumDelDialog.build().showView(((FragmentActivity) activity).getSupportFragmentManager(), -1, CartNumDelDialog.DEL_GOODS)
                        .setOnCommonClickCalllBack(new CartNumDelDialog.OnCommonClickCalllBack() {
                            @Override
                            public void callBack(int typeCode, int num) {
                                if(typeCode == CartNumDelDialog.DEL_GOODS) {
                                    loadDelData(mPresenter.getSelectId());
                                }
                            }
                        });

                break;
            case R.id.m_title_right_tv:// 编辑、完成
                if(null == mTvEdit){
                    return;
                }
                if(mTvEdit.getText().toString().equals("编辑")){// 可以执行删除操作
                    setEdit("完成", "删除", R.color.color_2897d4, View.GONE);
                }else{// 正常操作
                    replaceEdit();
                }
                break;
            case R.id.m_layout_check:// 全选
                loadSelectData(mCheckBox.isChecked() ? 2 : 1, mPresenter.getGoodsAllId());
                break;
            case R.id.m_tv_refresh:// 刷新
                loadData();
                break;
        }
    }

    // 恢复到正常状态
    public void replaceEdit(){
        if(null != mTvEdit && !mTvEdit.getText().toString().equals("编辑")){
            setEdit("编辑", "去结算", R.color.color_d6004f, View.VISIBLE);
        }
    }

    // 设置编辑按钮样式状态
    private void setEdit(String editType, String buyType, int color, int visib){
        if(null == mTvEdit || null == mTvBuy || null == mTvTotalPrice){
            return;
        }
        mTvEdit.setText(editType);
        mTvBuy.setText(buyType);
        mTvBuy.setBackgroundColor(getResources().getColor(color));
        mTvTotalPrice.setVisibility(visib);
        setTextDiscountPrice(visib == View.VISIBLE ? mDisCountPrice : 0);
    }

    // 列表数据
    public ArrayList<Object> getListData(){
        return mListObject;
    }

    public int updateNum(){
        return mUpdateNum;
    }

    public long cart_id(){
        return cart_id;
    }

    public String sku(){
        if(StringUtil.isEmpty(sku)){
            return "";
        }
        return sku;
    }

    public String cart_ids(){
        return cart_ids;
    }

    public int select(){
        return select;
    }

//    @Override
//    public void onRefresh() {
//        mRefresh.setRefreshing(false);
//    }

    @Override
    public void onLoadMore() {
        mRecycleview.loadMoreFinish(false, false);
    }

    // 全选
    public void setCheck(){
        if(null == mCheckBox){
            return;
        }
        mCheckBox.setChecked(mPresenter.isSelectAll());
    }

    // 总计
    public void setTextAllPrice(double price){
        if(null == mTvTotalPrice){
            return;
        }
        mTvTotalPrice.setText("总价：¥"+ price);
        mTvTotalPrice.setVisibility(isDelStatus() ? View.GONE : View.VISIBLE);
    }


    // 优惠价格
    private double mDisCountPrice;
    public void setTextDiscountPrice(double price){
        mDisCountPrice = price;
        if(null == mTvDiscountPrice){
            return;
        }
        if(price <= 0){
            mTvDiscountPrice.setVisibility(View.GONE);
        }else{
            mTvDiscountPrice.setText("优惠：¥"+ price);
            mTvDiscountPrice.setVisibility(isDelStatus() ? View.GONE : View.VISIBLE);
        }
    }

    // 刷新适配器
    public void notifyData(){
        if(null != mAdapter){
            mAdapter.notifyDataSetChanged();
        }
    }

    // 是否处于删除状态
    public boolean isDelStatus(){
        if(null != mTvEdit && mTvEdit.getText().toString().equals("完成")){// 可删除
            return true;
        }
        return false;
    }

    private void showLoading(){
        if(null == mViewLoading || null == mViewError){
            return;
        }
        mViewLoading.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);
    }

    private void showData(){
        if(null == mRecycleview || null == mViewLoading || null == mViewError
                || null == mTvEdit || null == mLayoutBottom){
            return;
        }
        mRecycleview.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
        mTvEdit.setVisibility(View.VISIBLE);
        mLayoutBottom.setVisibility(View.VISIBLE);
    }

    private void showError(){
        if(null == mRecycleview || null == mViewLoading || null == mViewError
                || null == mTvEdit || null == mLayoutBottom){
            return;
        }
        mViewError.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mRecycleview.setVisibility(View.GONE);
        mTvEdit.setVisibility(View.GONE);
        mLayoutBottom.setVisibility(View.GONE);
    }

    @Subscribe
    public void onEvent(JJEvent event){
        if(null == event){
            return;
        }
        int id = event.getEventId();
        switch (id){
            case JJEvent.REFRESH_SHOPPING_CART:
                loadData();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
