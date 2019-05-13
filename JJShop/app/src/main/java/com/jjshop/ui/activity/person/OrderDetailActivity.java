package com.jjshop.ui.activity.person;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.adapter.CommentListAdapter;
import com.jjshop.base.BaseActivity;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.JJEvent;
import com.jjshop.bean.MyOrderActionData;
import com.jjshop.bean.MyOrderProductList;
import com.jjshop.bean.OrderDetailBean;
import com.jjshop.bean.OrderDetailInfoBean;
import com.jjshop.bean.ProductListBean;
import com.jjshop.bean.ShopBean;
import com.jjshop.bean.WxPayBean;
import com.jjshop.dialog.CartNumDelDialog;
import com.jjshop.template_view.TemplateUtil;
import com.jjshop.ui.presenter.OrderDetailPresenter;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.Tools;
import com.jjshop.utils.WXUtil;
import com.jjshop.utils.XNUtil;
import com.jjshop.widget.MyGridView;
import com.jjshop.widget.MyListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：张国庆
 * 时间：2018/9/19
 */

public class OrderDetailActivity extends BaseActivity<OrderDetailPresenter> {

    @BindView(R.id.m_title_back)
    ImageView mTitleBack;
    @BindView(R.id.m_tv_title)
    TextView mTvTitle;
    @BindView(R.id.m_iv_status)
    ImageView mIvStatus;
    @BindView(R.id.m_tv_status)
    TextView mTvStatus;
    @BindView(R.id.m_tv_yuanyin)
    TextView mTvYuanyin;
    @BindView(R.id.m_tv_yfk)
    TextView mTvYfk;
    @BindView(R.id.m_tv_sheng)
    TextView mTvSheng;
    @BindView(R.id.m_tv_time)
    TextView mTvTime;
    @BindView(R.id.m_tv_colse)
    TextView mTvColse;
    @BindView(R.id.m_tv_address_name)
    TextView mTvAddressName;
    @BindView(R.id.m_tv_address_mobile)
    TextView mTvAddressMobile;
    @BindView(R.id.m_tv_address)
    TextView mTvAddress;
    @BindView(R.id.m_tv_shop_name)
    TextView mTvShopName;
    @BindView(R.id.m_listview)
    MyListView mListview;
    @BindView(R.id.m_tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.m_tv_yun_price)
    TextView mTvYunPrice;
    @BindView(R.id.m_tv_discount_price)
    TextView mTvDiscountPrice;
    @BindView(R.id.m_tv_quan_price)
    TextView mTvQuanPrice;
    @BindView(R.id.m_tv_price)
    TextView mTvPrice;
    @BindView(R.id.m_tv_order_num)
    TextView mTvOrderNum;
    @BindView(R.id.m_tv_new_order_time)
    TextView mTvNewOrderTime;
    @BindView(R.id.m_gridview)
    MyGridView mGridview;
    @BindView(R.id.m_layout_bottom)
    RelativeLayout mLayoutBottom;
    @BindView(R.id.m_layout_scroll)
    ScrollView mLayoutScroll;
    @BindView(R.id.m_view_loading)
    View mViewLoading;
    @BindView(R.id.m_view_error)
    View mViewError;
    @BindView(R.id.m_layout_wuliu)
    RelativeLayout mLayoutWuliu;
    @BindView(R.id.tv_yue_price)
    TextView tvYuePrice;
    @BindView(R.id.m_tv_yue_price)
    TextView mTvYuePrice;

    private long mOrderNo;
    private CommentListAdapter mAdapterOrderGoods;
    private ArrayList<Object> mOrderGoodsList;
    private CommentListAdapter mAdapterGoods;
    private ArrayList<Object> mGoodsList;
    private WxPayBean.WxPayData wxPayData;
    private String payType = "", status = "";

    @Override
    protected int setLayoutId() {
        return R.layout.activity_order_detail_layout;
    }

    @Override
    protected OrderDetailPresenter getPresenter() {
        return new OrderDetailPresenter(this);
    }

    public static void invoke(Context context, long orderNo) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("order_no", orderNo);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        mTvTitle.setText("订单详情");
        EventBus.getDefault().register(this);
        if (null != getIntent()) {
            mOrderNo = getIntent().getLongExtra("order_no", 0);
        }
        mOrderGoodsList = new ArrayList<>();
        mGoodsList = new ArrayList<>();
        mAdapterOrderGoods = new CommentListAdapter(this, mOrderGoodsList, TemplateUtil
                .TEMPLATE_1007);
        mListview.setAdapter(mAdapterOrderGoods);
        mAdapterGoods = new CommentListAdapter(this, mGoodsList, TemplateUtil.TEMPLATE_1006);
        mGridview.setAdapter(mAdapterGoods);
        loadDataDetail();
    }

    // 加载订单详情数据
    public void loadDataDetail() {
        if (null != mPresenter) {
            loading();
            mPresenter.loadDataDetail();
        }
    }

    // 加载微信支付数据
    public void loadDataWxPay() {
        if (null != mPresenter) {
            mViewLoading.setVisibility(View.VISIBLE);
            mPresenter.loadPayOrderData();
        }
    }

    // 取消订单请求
    public void loadCancelOrder() {
        if (null != mPresenter) {
            mViewLoading.setVisibility(View.VISIBLE);
            mPresenter.loadCancelOrder();
        }
    }

    // 取消订单成功、支付成功，刷新订单信息
    private void refreshData() {
        loadDataDetail();
        EventBus.getDefault().post(new JJEvent(JJEvent.REFRESH_ORDER_LIST));
    }

    @OnClick({R.id.m_title_back, R.id.m_layout_kefu, R.id.m_tv_crop, R.id.m_tv_pay, R.id
            .m_tv_cancel_tui,
            R.id.m_layout_wuliu, R.id.m_tv_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_title_back:
                finish();
                break;
            case R.id.m_layout_kefu:// 客服
                XNUtil.getXNUtil().openChat(this, null);
                break;
            case R.id.m_tv_crop:// 复制订单号
                Tools.clipContent(this, String.valueOf(mOrderNo));
                JjToast.getInstance().toast("复制成功");
                break;
            case R.id.m_tv_pay:// 支付
                if (null != wxPayData) {
                    WXUtil.getWxUtil().pay(this, wxPayData);
                    return;
                }
                loadDataWxPay();
                break;
            case R.id.m_tv_cancel_tui:// 取消支付
                CartNumDelDialog.build().showView(getSupportFragmentManager(), 0,
                        CartNumDelDialog.CANCEL_ORDER_CODE).setOnCommonClickCalllBack(new CartNumDelDialog.OnCommonClickCalllBack() {
                    @Override
                    public void callBack(int typeCode, int num) {
                        if (typeCode == CartNumDelDialog.CANCEL_ORDER_CODE) {
                            loadCancelOrder();
                        }
                    }
                });
                break;
            case R.id.m_layout_wuliu:// 物流
                WuliuInfoActivity.invoke(this, orderNo());
                break;
            case R.id.m_tv_refresh:// 刷新
                loadDataDetail();
                break;
        }
    }


    // 列表数据获取成功
    public void onSuccessDetail(OrderDetailBean.OrderDetailData bean) {
        mErrorNum = 0;
        showData();
        OrderDetailInfoBean detailInfo = bean.getInfo();
        if (null == detailInfo) {
            return;
        }
        // 订单状态
        showOrderStatus(detailInfo.getStatus(), detailInfo.getStatus_str_time());
        // 收货人
        mTvAddressName.setText("收货人：" + detailInfo.getAccept_name());
        // 手机号
        mTvAddressMobile.setText(detailInfo.getMobile());
        // 地址
        mTvAddress.setText(detailInfo.getProvince() + detailInfo.getCity() + detailInfo.getArea()
                + detailInfo.getAddress());
        // 待付款按钮action
        ArrayList<MyOrderActionData> actionData = detailInfo.getAction();
        if (null != actionData && actionData.size() > 0) {
            for (MyOrderActionData data : actionData) {
                if (null != data && data.getStatus() == -10) {
                    if (data.getParameter() != null) {
                        status = data.getParameter().getStatus();
                        break;
                    }
                }
            }
        }
        // 店铺名称
        ShopBean shopBean = detailInfo.getShop();
        if (null != shopBean) {
            mTvShopName.setText(shopBean.getName());
        }
        // 订单的商品
        mOrderGoodsList.clear();
        ArrayList<MyOrderProductList> orderGoodsList = detailInfo.getProduct();
        if (null != orderGoodsList && orderGoodsList.size() > 0) {
            mOrderGoodsList.addAll(orderGoodsList);
        }
        mAdapterOrderGoods.notifyDataSetChanged();
        // 商品总额
        mTvTotalPrice.setText("¥" + detailInfo.getGoods_total_price());
        // 运费
        mTvYunPrice.setText(detailInfo.getFreight_price());
        // 优惠抵扣
        double promotionPriceOrder = detailInfo.getPromotion_goods_price() + detailInfo
                .getPromotion_order_price();
        mTvDiscountPrice.setText("-¥" + StringUtil.getPrice(promotionPriceOrder));
        // 优惠券抵扣
        mTvQuanPrice.setText("-¥" + detailInfo.getPromotion_coupon_price());
        // 余额抵扣
        mTvYuePrice.setText("-¥"+detailInfo.getAccount_balance_price());
        // 实付款
        mTvPrice.setText("¥" + detailInfo.getPrice());
        // 订单编号
        mTvOrderNum.setText("订单编号：" + detailInfo.getOrder_no());
        // 创建时间
        mTvNewOrderTime.setText("创建时间：" + detailInfo.getCreated_at());
        // 可能喜欢的商品
        ArrayList<ProductListBean> goodsList = bean.getRec_list();
        mGoodsList.clear();
        if (null != goodsList && goodsList.size() > 0) {
            mGoodsList.addAll(goodsList);
        }
        mAdapterGoods.notifyDataSetChanged();
    }

    // 列表数据加载失败
    private int mErrorNum;

    public void onErrorDetail(String msg) {
        mErrorNum++;
        if (mErrorNum <= 2) {
            loadDataDetail();
            return;
        }
        showError();
    }

    // 获取微信支付数据成功
    public void onPayOrderSuccess(WxPayBean.WxPayData bean) {
        wxPayData = bean;
        mViewLoading.setVisibility(View.GONE);
        WXUtil.getWxUtil().pay(this, bean);
    }

    // 获取微信支付数据失败
    public void onPayOrderFail(String msg) {
        JjToast.getInstance().toast(msg);
        mViewLoading.setVisibility(View.GONE);
    }

    // 取消订单成功
    public void onCancelOrderSuccess(BaseBean bean) {
        mViewLoading.setVisibility(View.GONE);
        refreshData();
        EventBus.getDefault().post(new JJEvent(JJEvent.REFRESH_PERSON_INFO));
    }

    // 取消订单失败
    public void onCancelOrderFail(String msg) {
        JjToast.getInstance().toast(msg);
        mViewLoading.setVisibility(View.GONE);
    }

    public long orderNo() {
        return mOrderNo;
    }

    public String payType() {
        return payType;
    }

    public String status() {
        return status;
    }

    private void showOrderStatus(int ststus, String statusStr) {
        int visiBottom = View.GONE;// 底部的布局是否显示
        int statusIcon = -1;// 显示的状态图标
        int visiStatusRed = View.GONE;// 未付款
        int visiStatusYfk = View.GONE;// 已付款
        int visiWuliu = View.GONE;// 显示物流布局
        int visiDfk = View.GONE;// 待付款是否显示倒计时
        mTvYuanyin.setVisibility(View.GONE);
        mTvStatus.setTextColor(getResources().getColor(R.color.color_f23030));
        String statusContent = "";
        switch (ststus) {
            case CommonUtils.ORDER_DFK:// 待付款
                statusIcon = R.mipmap.img_order_dfk_icon;
                visiBottom = View.VISIBLE;
                visiStatusRed = View.VISIBLE;
                visiDfk = View.VISIBLE;
                mTvStatus.setText("待付款");
                mTvSheng.setText("剩 ");
                mTvTime.setText(statusStr);
                mTvColse.setText(" 自动关闭");
                break;
            case CommonUtils.ORDER_YFK_ONE:// 买家已付款
            case CommonUtils.ORDER_YFK_TWO:
                statusIcon = R.mipmap.img_order_yfk_icon;
                statusContent = "买家已付款";
                visiStatusYfk = View.VISIBLE;
                break;
            case CommonUtils.ORDER_YFH_ONE:// 卖家已发货
            case CommonUtils.ORDER_YFH_TWO:
                mTvStatus.setTextColor(getResources().getColor(R.color.color_2897d4));
                statusIcon = R.mipmap.img_order_yunshu_icon;
                visiWuliu = View.VISIBLE;
                visiStatusRed = View.VISIBLE;
                visiDfk = View.VISIBLE;
                mTvStatus.setText("卖家已发货");
                mTvSheng.setText("剩 ");
                mTvTime.setText(statusStr);
                mTvColse.setText(" 自动确认");
                break;
            case CommonUtils.ORDER_SUCCESS_ONE:// 交易成功
            case CommonUtils.ORDER_SUCCESS_TWO:
            case CommonUtils.ORDER_SUCCESS_THREE:
            case CommonUtils.ORDER_SUCCESS_FOUR:
                statusIcon = R.mipmap.img_order_yfk_icon;
                statusContent = "交易成功";
                visiStatusYfk = View.VISIBLE;
                visiWuliu = View.VISIBLE;
                break;
            case CommonUtils.ORDER_CANCEL_ONE:// 交易关闭
            case CommonUtils.ORDER_CANCEL_TWO:
                statusIcon = R.mipmap.img_order_cancel_icon;
                visiStatusRed = View.VISIBLE;
                mTvStatus.setText("交易关闭");
                mTvYuanyin.setVisibility(View.VISIBLE);
                mTvYuanyin.setText("取消订单原因：我不想买了");
                break;
        }
        mLayoutBottom.setVisibility(visiBottom);
        if (statusIcon != -1) {
            mIvStatus.setImageResource(statusIcon);
        }
        mTvStatus.setVisibility(visiStatusRed);
        mTvYfk.setText(statusContent);
        mTvYfk.setVisibility(visiStatusYfk);
        mLayoutWuliu.setVisibility(visiWuliu);
        // 待付款显示倒计时
        mTvSheng.setVisibility(visiDfk);
        mTvTime.setVisibility(visiDfk);
        mTvColse.setVisibility(visiDfk);

    }

    private void loading() {
        mViewLoading.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);
    }

    private void showData() {
        mLayoutScroll.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }

    private void showError() {
        mViewError.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mLayoutScroll.setVisibility(View.GONE);
    }


    @Subscribe
    public void onEvent(JJEvent event) {
        if (null == event) {
            return;
        }
        final int id = event.getEventId();
        switch (id) {
            case JJEvent.FINISH_ACTIVITY:
                finish();
                break;
            case JJEvent.PAY_SUCCESS:
            case JJEvent.REFRESH_ORDER_DETAILS:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (id == JJEvent.PAY_SUCCESS) {
                            EventBus.getDefault().post(new JJEvent(JJEvent.REFRESH_PERSON_INFO));
                        }
                        refreshData();
                    }
                });
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
