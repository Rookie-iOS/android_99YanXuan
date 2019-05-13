package com.jjshop.ui.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjshop.R;
import com.jjshop.adapter.CommentListAdapter;
import com.jjshop.base.BaseActivity;
import com.jjshop.bean.CartDataBean;
import com.jjshop.bean.CouponDataBean;
import com.jjshop.bean.GetOrderIdBean;
import com.jjshop.bean.JJEvent;
import com.jjshop.bean.SubmitOrderBean;
import com.jjshop.bean.WxPayBean;
import com.jjshop.dialog.DiscountQuanDialog;
import com.jjshop.template_view.TemplateUtil;
import com.jjshop.ui.activity.person.MyAddressActivity;
import com.jjshop.ui.presenter.SubmitOrderPresenter;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.Tools;
import com.jjshop.utils.WXUtil;
import com.jjshop.widget.MyListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：张国庆
 * 时间：2018/8/15
 */

public class SubmitOrderActivity extends BaseActivity<SubmitOrderPresenter> {

    @BindView(R.id.m_tv_title)
    TextView mTvTitle;
    @BindView(R.id.m_tv_go_selelct_address)
    TextView mTvSelectAddress;
    @BindView(R.id.m_tv_address_name)
    TextView mTvAddressName;
    @BindView(R.id.m_tv_address_mobile)
    TextView mTvAddressMobile;
    @BindView(R.id.m_tv_address)
    TextView mTvAddress;
    //    @BindView(R.id.m_tv_peisong_price)
//    TextView mTvPeisongPrice;
    @BindView(R.id.m_tv_discount_quan)
    TextView mTvDiscountQuan;
    @BindView(R.id.m_listview)
    MyListView mListview;
    @BindView(R.id.m_et_liuyan)
    EditText mEtLiuyan;
    @BindView(R.id.m_tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.m_tv_yun_price)
    TextView mTvYunPrice;
    @BindView(R.id.m_tv_discount_price)
    TextView mTvDiscountPrice;
    @BindView(R.id.m_tv_price)
    TextView mTvPrice;
    @BindView(R.id.m_tv_yingfu_money)
    TextView mTvYingfuMoney;
    @BindView(R.id.m_layout_center)
    ScrollView mLayoutCenter;
    @BindView(R.id.m_layout_bottom)
    RelativeLayout mLayoutBottom;
    @BindView(R.id.m_iv_discount_jiantou)
    ImageView mIvDiscount;
    @BindView(R.id.m_layout_discount)
    RelativeLayout mLayoutDiscount;
    @BindView(R.id.m_view_loading)
    View mViewLoading;
    @BindView(R.id.m_view_error)
    View mViewError;
    @BindView(R.id.m_tv_address_bottom)
    TextView mTvAddressBottom;
    @BindView(R.id.m_tv_submit_order)
    TextView mTvSubmitOrder;
    @BindView(R.id.balance_pay)
    RelativeLayout balancePay;
    @BindView(R.id.balance_pay_check)
    ImageView balancePayCheck;
    @BindView(R.id.weixin_pay_action)
    TextView weixinPayAction;
    @BindView(R.id.balance_pay_action)
    TextView balancePayAction;
    @BindView(R.id.m_balance_discount_price)
    TextView mBalanceDiscountPrice;

    private CommentListAdapter mAdapter;
    private ArrayList<Object> mListGoods;
    private ArrayList<Object> mListQuan;
    private WxPayBean.WxPayData wxPayData;
    private boolean is_balance_pay_checked = false;

    // 传给服务器的字段
    private String quanIdCode = "";
    private String address = "";
    private String payment_no = "";
    private String payment_type = "";
    private String mobile = ""; // 收货地址手机号
    private String addressIdcode = "";
    private double account_available_count = 0; // 可用余额
    private double deduction_count = 0 ; // 抵扣余额
    private DiscountQuanDialog mQuanDialog;
    private boolean mIsOpenWX = false;// 是否调启微信

    public static void invoke(Context context) {
        Intent intent = new Intent(context, SubmitOrderActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int setLayoutId() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return R.layout.activity_submit_order_layout;
    }

    @Override
    protected SubmitOrderPresenter getPresenter() {
        return new SubmitOrderPresenter(this);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        mTvTitle.setText("确认订单");
        // 微信支付文字
        weixinPayAction.setText(Html.fromHtml("<font color='black'>微信支付</font><br/><font color = " +
                "'#9c9c9c'><small>微信安全支付</small></font>"));
        setBalanceMoney(0,0);
        mListGoods = new ArrayList<>();
        mListQuan = new ArrayList<>();
        mAdapter = new CommentListAdapter(this, mListGoods, TemplateUtil.TEMPLATE_1005);
        mListview.setAdapter(mAdapter);
        loadData("");
    }

    private void setBalanceMoney(double available_count, double deduction_count) {

        DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String availableString = decimalFormat.format(available_count);//format 返回的是字符串
        String deductionString = decimalFormat.format(deduction_count);
        balancePayAction.setText(Html.fromHtml("<font color='black'>余额抵扣：</font>" + "<font " +
                "color='#fa1a16'>¥ " + deductionString + "</font>" + "<br/><font color = " +
                "'#9c9c9c'><small>可用余额：¥ "+availableString+"</small></font>"));
        mBalanceDiscountPrice.setText("¥ "+deductionString);
    }
    // 加载订单信息
    public void loadData(String quanIdcode) {
        if (null != mPresenter) {
            if (null != mQuanDialog) {
                mQuanDialog.dismiss();
                mQuanDialog = null;
            }
            showLoading();
            this.quanIdCode = quanIdcode;
            mPresenter.loadData();
        }
    }

    // 加载提交订单
    public void loadSubmitOrderData() {
        if (null != mPresenter) {
            showLoading();
            mPresenter.loadSubmitOrderData();
        }
    }

    // 加载支付数据
    public void loadPayOrderData() {
        if (null != mPresenter) {
            showLoading();
            mPresenter.loadPayOrderData();
        }
    }

    // 获取订单信息成功
    public void onSuccess(SubmitOrderBean.SubmitOrderData bean) {
        showData();
        // 需要传服务器的地址
        address = bean.getAddress();
        // 优惠券
        mListQuan.clear();
        mListQuan.addAll(bean.getCoupons());

        SubmitOrderBean.SubmitOrderInfo info = bean.getData();
        // 地址相关
        SubmitOrderBean.AddressData addressData = info.getAddress();
        if (null != addressData) {
            String name = addressData.getAccept_name();
            mobile = addressData.getMobile();
            String path = addressData.getAddress();
            if (StringUtil.isEmpty(name) || StringUtil.isEmpty(mobile) || StringUtil.isEmpty
                    (path)) {
                mTvSelectAddress.setVisibility(View.VISIBLE);
                mTvAddressName.setVisibility(View.GONE);
                mTvAddressMobile.setVisibility(View.GONE);
                mTvAddress.setVisibility(View.GONE);
                mTvAddressBottom.setVisibility(View.GONE);
            } else {
                mTvSelectAddress.setVisibility(View.GONE);
                mTvAddressName.setVisibility(View.VISIBLE);
                mTvAddressMobile.setVisibility(View.VISIBLE);
                mTvAddress.setVisibility(View.VISIBLE);
                mTvAddressBottom.setVisibility(View.VISIBLE);

                mTvAddressName.setText("收货人：" + name);
                mTvAddressMobile.setText(mobile);
                // 地址
                String address = "";
                SubmitOrderBean.CityData cityOne = addressData.getProvince();
                if (null != cityOne) {// 省
                    address = cityOne.getName();
                }
                SubmitOrderBean.CityData cityTwo = addressData.getCity();
                if (null != cityTwo) {// 市
                    address = address + cityTwo.getName();
                }
                SubmitOrderBean.CityData cityThree = addressData.getArea();
                if (null != cityThree) {// 区
                    address = address + cityThree.getName();
                }
                address = address + path;
                mTvAddress.setText(address);
                mTvAddressBottom.setText("送至： " + address);
                addressIdcode = addressData.getAddress_idcode();
            }
        }
        // 快递费
//        mTvPeisongPrice.setText("快递 ¥" + info.getFreight_price());
        // 优惠券可用数量
        int quanNum = bean.getCoupon_count();
        if (quanNum > 0) {
            mLayoutDiscount.setClickable(true);
            mLayoutDiscount.setEnabled(true);
            mIvDiscount.setVisibility(View.VISIBLE);
            mTvDiscountQuan.setText(quanNum + "张可用");
        } else {
            mLayoutDiscount.setClickable(false);
            mLayoutDiscount.setEnabled(false);
            mIvDiscount.setVisibility(View.GONE);
            mTvDiscountQuan.setText("暂无优惠券");
        }

        //选中的优惠券
        CouponDataBean quanData = bean.getSelected_coupons();
        if (null != quanData && !StringUtil.isEmpty(quanIdCode)) {
            String quanName = quanData.getPatch_name();
            if (!StringUtil.isEmpty(quanName)) {
                mTvDiscountQuan.setText("已选1张," + quanName);
            }

        }

        // 商品列表
        mListGoods.clear();
        ArrayList<CartDataBean> list = info.getProducts();
        if (null == list) {
            list = new ArrayList<>();
        }
        mListGoods.addAll(list);
        mAdapter.notifyDataSetChanged();
        // 商品总额
        mTvTotalPrice.setText("¥" + info.getGoods_total_price());
        // 运费
        mTvYunPrice.setText("¥" + info.getFreight_price());
        // 优惠抵扣
        double promotionPriceOrder = info.getPromotion_coupon_price() + info
                .getPromotion_goods_price() + info.getPromotion_order_price();
        mTvDiscountPrice.setText("-¥" + StringUtil.getPrice(promotionPriceOrder));
        // 应付价格
        mTvPrice.setText("¥" + info.getTotal_price());
        mTvYingfuMoney.setText("应付金额：¥" + info.getTotal_price());

        SubmitOrderBean.Account_balance balance = info.getAccount_balance();
        if (null == balance){
            return;
        }
        deduction_count = balance.getDeduction();
        account_available_count = balance.getAvailable();
        // 余额抵扣
        if (is_balance_pay_checked) {
            setBalanceMoney(account_available_count,deduction_count);
        }else {
            setBalanceMoney(account_available_count,0);
        }
    }

    public void onFail(String msg) {
        showError();
    }

    // 提交订单成功
    public void onSubmitOrderSuccess(GetOrderIdBean.GetOrderIdData bean) {
        payment_no = bean.getPayment_no();
        payment_type = bean.getPay_type();
        // 加载微信支付数据
        loadPayOrderData();
        EventBus.getDefault().post(new JJEvent(JJEvent.REFRESH_SHOPPING_CART));
    }

    public void onSubmitOrderFail(String msg) {
        JjToast.getInstance().toast(msg);
        mViewLoading.setVisibility(View.GONE);
    }

    // 获取微信支付数据成功
    public void onPayOrderSuccess(WxPayBean.WxPayData bean) {
        wxPayData = bean;
        mViewLoading.setVisibility(View.GONE);
        mIsOpenWX = true;
        WXUtil.getWxUtil().pay(this, bean);
    }

    public void onPayOrderFail(String msg) {
        JjToast.getInstance().toast(msg);
        mViewLoading.setVisibility(View.GONE);
    }

    public String quanIdcode() {
        return quanIdCode;
    }

    public String address() {
        return address;
    }

    public String getBalance() {
        if (is_balance_pay_checked) {
            return "1";
        }
        return "";
    }

    public String addressIdcode() {
        return addressIdcode;
    }

    public String order_remarks() {
        if (null == mEtLiuyan) {
            return "";
        }
        String order_remarks = mEtLiuyan.getText().toString();
        if (StringUtil.isEmpty(order_remarks)) {
            return "";
        }
        return order_remarks;
    }

    public String payment_no() {
        if (StringUtil.isEmpty(payment_no)) {
            payment_no = "";
        }
        return payment_no;
    }

    public String payment_type() {
        if (StringUtil.isEmpty(payment_type)) {
            payment_type = "";
        }
        return payment_type;
    }

    public ArrayList<Object> listGoods() {
        return mListGoods;
    }

    @OnClick({R.id.m_title_back, R.id.m_layout_address, R.id.balance_pay, R.id.m_layout_discount,
            R.id.m_tv_submit_order, R.id.m_tv_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_title_back:
                finish();
                break;
            case R.id.m_layout_address:
                MyAddressActivity.invoke(this, MyAddressActivity.INVOKE_SUBMIT_ORDER);
                break;
            case R.id.balance_pay:
                // 余额是否足够，不够的话不能选择
                String price = mTvPrice.getText().toString().substring(1);
                if (account_available_count == 0) { // 余额抵扣不足
                    JjToast.getInstance().toast("您的余额已经花光了哦~");
                    return;
                }
                is_balance_pay_checked = !is_balance_pay_checked;
                if (is_balance_pay_checked) {
                    // 设置按钮选中
                    balancePayCheck.setImageResource(R.mipmap.icon_on);
                } else {
                    balancePayCheck.setImageResource(R.mipmap.icon_off);
                }
                // 更新数据
                loadData(quanIdCode);
                break;
            case R.id.m_layout_discount:
                if (mTvSelectAddress.getVisibility() == View.VISIBLE) {
                    JjToast.getInstance().toast("请先去添加地址");
                    return;
                }
                mQuanDialog = DiscountQuanDialog.build().showView(getSupportFragmentManager(),
                        mListQuan);
                break;
            case R.id.m_tv_refresh:
                loadData(quanIdCode);
                break;
            case R.id.m_tv_submit_order:
                if (mTvSelectAddress.getVisibility() == View.VISIBLE) {
                    JjToast.getInstance().toast("请先去添加地址");
                    return;
                }
                if (!Tools.isWeixinAvilible(this)) {
                    JjToast.getInstance().toast("您还没有安装微信");
                    return;
                }
                if (mPresenter.isNotDistribution()) {
                    JjToast.getInstance().toast("您的收获区域不配送");
                    return;
                }
                if (StringUtil.isEmpty(payment_no()) || StringUtil.isEmpty(payment_type())) {
                    loadSubmitOrderData();
                    return;
                }
                if (null == wxPayData) {
                    loadPayOrderData();
                    return;
                }
                mIsOpenWX = true;
                WXUtil.getWxUtil().pay(this, wxPayData);
                break;
        }
    }

    private void showLoading() {
        mViewLoading.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);
    }

    private void showData() {
        mLayoutCenter.setVisibility(View.VISIBLE);
        mLayoutBottom.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }

    private void showError() {
        mViewError.setVisibility(View.VISIBLE);
        mLayoutCenter.setVisibility(View.GONE);
        mLayoutBottom.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);
    }

    public void showHideLoading(int visible) {
        if (!(View.GONE == visible || View.VISIBLE == visible || null == mViewLoading)) {
            return;
        }
        mViewLoading.setVisibility(visible);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsOpenWX) {
            showHideLoading(View.GONE);
            if (null != wxPayData) {
                JjLog.e("哼-----跳转了吧111111111");
                PayActivity.invoke(SubmitOrderActivity.this, wxPayData.getPay_result_url(),mobile);
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
            case JJEvent.SUBMIT_ORDER_REFRESH_ADDRESS:
                address = event.getEventMessage();
                loadData(quanIdCode);
                break;
            case JJEvent.PAY_SUCCESS:
            case JJEvent.PAY_CANCEL:
            case JJEvent.PAY_FAIL:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showHideLoading(View.GONE);
                        if (null != wxPayData) {
                            JjLog.e("哼-----跳转了吧22222222");
                            PayActivity.invoke(SubmitOrderActivity.this, wxPayData
                                    .getPay_result_url(),mobile);
                        }
                    }
                });
                break;
            case JJEvent.FINISH_ACTIVITY:
                finish();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        showHideLoading(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
