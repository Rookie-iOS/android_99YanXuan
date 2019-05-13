package com.jjshop.ui.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.adapter.CommentListAdapter;
import com.jjshop.base.BaseActivity;
import com.jjshop.bean.ADShowBean;
import com.jjshop.bean.JJEvent;
import com.jjshop.bean.PayFinishBean;
import com.jjshop.bean.ProductListBean;
import com.jjshop.dialog.HongbaoDialog;
import com.jjshop.listener.OnCommonCallBackIm;
import com.jjshop.template_view.TemplateUtil;
import com.jjshop.ui.activity.person.MyOrderActivity;
import com.jjshop.ui.presenter.PayPresenter;
import com.jjshop.utils.ADUtil;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.UIUtils;
import com.jjshop.utils.UrlInvokeUtil;
import com.jjshop.utils.XNUtil;
import com.jjshop.utils.glide_img.GlideUtil;
import com.jjshop.widget.MyGridView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：张国庆
 * 时间：2018/8/18
 */

public class PayActivity extends BaseActivity<PayPresenter> {
    @BindView(R.id.m_tv_title)
    TextView mTvTitle;
    @BindView(R.id.m_tv_pay_status)
    TextView mTvPayStatus;
    @BindView(R.id.m_tv_pay_money)
    TextView mTvPayMoney;
    @BindView(R.id.m_tv_left)
    TextView mTvLeft;
    @BindView(R.id.m_tv_right)
    TextView mTvRight;
    @BindView(R.id.m_gridview)
    MyGridView mGridview;
    @BindView(R.id.m_layout_sv)
    ScrollView mScrollView;
    @BindView(R.id.m_view_loading)
    View mViewLoading;
    @BindView(R.id.m_view_error)
    View mViewError;
    @BindView(R.id.m_tv_status)
    TextView mTvStatus;
    @BindView(R.id.m_iv_status)
    ImageView mIvStatus;
    @BindView(R.id.m_iv_activity)
    ImageView mIvActivity;
    @BindView(R.id.m_iv_details_ad)
    ImageView mIvDetailsAd;

    private CommentListAdapter mAdapter;
    private ArrayList<Object> mListData;
    private int mPayStatus = -1;
    private String resultUrl = "", mActivityUrl = "", mAdUrl = "", hongbaoMobile="";
    private ADShowBean.ADShowBeanData adShowBeanData;// 广告数据
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_pay_layout;
    }

    @Override
    protected PayPresenter getPresenter() {
        return new PayPresenter(this);
    }

    public static void invoke(Context context, String resultUrl ,String kaquanMobile) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra("resultUrl", resultUrl);
        intent.putExtra("mobile",kaquanMobile);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        if (null != getIntent()) {
            resultUrl = getIntent().getStringExtra("resultUrl");
            hongbaoMobile = getIntent().getStringExtra("mobile");
        }
        mTvTitle.setText("收银台");
        mListData = new ArrayList<>();
        mAdapter = new CommentListAdapter(this, mListData, TemplateUtil.TEMPLATE_1006);
        mGridview.setAdapter(mAdapter);
        showLoading();
        // 延迟3秒查支付状态
        handler.removeCallbacks(runnable);
        runnable = new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        };
        handler.postDelayed(runnable, 2000);
    }

    private void loadData() {
        if (null != mPresenter) {
            showLoading();
            mPresenter.loadData();
        }
    }

    @OnClick({R.id.m_title_back, R.id.m_tv_left, R.id.m_tv_right, R.id.m_tv_refresh,
            R.id.m_iv_activity, R.id.m_iv_details_ad})
    public void onClick(View view) {
        cancelTimer();
        switch (view.getId()) {
            case R.id.m_title_back:// 返回到订单页
                goMyOrder();
                break;
            case R.id.m_tv_left:
                if(mPayStatus == 1){
                    goHome();
                    return;
                }
                goMyOrder();
                break;
            case R.id.m_tv_right:
                if(mPayStatus == 1){
                    goMyOrder();
                    return;
                }
                goHome();
                break;
            case R.id.m_iv_activity:
                Map<String, String> map = UrlInvokeUtil.build().pushData(mActivityUrl);
                UrlInvokeUtil.build().pushInvoke(this, map);
                break;
            case R.id.m_tv_refresh:
                loadData();
                break;
            case R.id.m_iv_details_ad:// 广告
                ADUtil.build().loadADReport(this, true, adShowBeanData);
                UrlInvokeUtil.build().pushInvoke(this, UrlInvokeUtil.build().pushData(mAdUrl));
                break;
        }
    }

    public void onSuccess(PayFinishBean.PayFinishData bean) {
        showData();
        mListData.clear();
        mPayStatus = bean.getType();
        if (bean.getType() == 1) {
            mTvStatus.setBackgroundResource(R.drawable.circle_f52d26_select);
            mIvStatus.setImageResource(R.mipmap.img_pay_finish_fail);
            mTvPayStatus.setTextColor(getResources().getColor(R.color.color_f52d26));
            mTvPayStatus.setText("订单支付失败，请稍后重试");
            mTvPayMoney.setTextSize(15f);
            mTvPayMoney.setTextColor(getResources().getColor(R.color.color_8a8c8f));
            startTimer();
            mTvLeft.setText("返回首页");
            mTvRight.setText("立即前往");
        }else{
            PayFinishBean.PaymentorderData paymentorderData = bean.getPaymentorder();
            if(null != paymentorderData){
                mTvPayMoney.setText("¥" + paymentorderData.getPay_price());
            }
            // 加载活动数据
            ADUtil.build().loadActivityData(this, CommonUtils.ACTIVITY_TYPE_PAY);
            // 红包弹窗
            hongBaoPopView(bean.getRed_envelope());
            // 加载广告数据
            ADUtil.build().loadADShow(this, ADUtil.APPKEY_AD_PAY, new OnCommonCallBackIm() {
                @Override
                public void onSuccess(Object o) {
                    if(null != o && o instanceof ADShowBean.ADShowBeanData){
                        adShowBeanData = (ADShowBean.ADShowBeanData) o;
                    }
                }
            });
        }
        ArrayList<ProductListBean> list = bean.getRec_list();
        if(null == list){
            mAdapter.notifyDataSetChanged();
            return;
        }
        mListData.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    public void onFail(String msg) {
        showError();
    }

    public String resultUrl(){
        if(StringUtil.isEmpty(resultUrl)){
            resultUrl = "";
        }
        return resultUrl;
    }
    // 红包弹窗
    private void hongBaoPopView(PayFinishBean.RedBaoData redBaoData){

        // 当给自己买券的话就不显示弹窗
        if (StringUtil.isEmpty(hongbaoMobile)) return;
        if (null == redBaoData) return;
        if (hongbaoMobile.equals(redBaoData.getUser_mobile())) return;
        //【100元新年红包】
        String productTitle = redBaoData.getProduct_name().substring(redBaoData.getProduct_name().indexOf("【"),redBaoData.getProduct_name().indexOf("】"));
        if (productTitle.indexOf("红包") != -1) {
            // 弹出红包弹层
            HongbaoDialog.build().showView(getSupportFragmentManager(),redBaoData.getTotal_price());
        }
    }

    private CountDownTimer mTimer;
    private void startTimer(){
        mTimer = new CountDownTimer(5 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                if(null == mTvPayMoney){
                    return;
                }
                mTvPayMoney.setText((l / 1000) + "s自动返回我的订单页");
            }

            @Override
            public void onFinish() {
                if(null == mTvPayMoney){
                    return;
                }
                mTvPayMoney.setText("0s自动返回我的订单页");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        goMyOrder();
                    }
                });
            }
        }.start();
    }

    public void cancelTimer(){
        if(null != mTimer){
            mTimer.cancel();
            mTimer = null;
        }
    }

    private void goHome(){
        HomeActivity.invoke(this, HomeActivity.SELECT_HOME);
        EventBus.getDefault().post(new JJEvent(JJEvent.FINISH_ACTIVITY));
    }

    public void goMyOrder(){
        MyOrderActivity.invoke(this, 0);
        EventBus.getDefault().post(new JJEvent(JJEvent.FINISH_ACTIVITY));
    }

    /**
     * 显示浮窗
     * @param img
     * @param url
     */
    public void showFuchuang(String img, String url){
        if(null == mIvActivity){
            return;
        }
        mActivityUrl = url;
        GlideUtil.getInstence().loadImageNoFix(this, mIvActivity, img);
        mIvActivity.setVisibility(View.VISIBLE);
    }

    // 显示广告图片
    public void showAdImg(Bundle bundle){
        if(null == mIvDetailsAd || null == bundle || bundle.size() <= 0){
            return;
        }
        mAdUrl = bundle.getString(ADUtil.KEY_URL);
        mIvDetailsAd.setVisibility(View.VISIBLE);
        int w = UIUtils.getWidth();
        int h = w * bundle.getInt(ADUtil.KEY_HEIGHT) / bundle.getInt(ADUtil.KEY_WIDTH);
        CommonUtils.build().setViewWH(mIvDetailsAd, w, h);
        GlideUtil.getInstence().loadImage(this, mIvDetailsAd, bundle.getString(ADUtil.KEY_IMG));
    }

    private void showLoading() {
        if(null == mViewLoading || null == mViewError){
            return;
        }
        mViewLoading.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);
    }

    private void showData() {
        if(null == mViewLoading || null == mViewError || null == mScrollView){
            return;
        }
        mScrollView.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }

    private void showError() {
        if(null == mViewLoading || null == mViewError || null == mScrollView){
            return;
        }
        mViewError.setVisibility(View.VISIBLE);
        mScrollView.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {

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
        cancelTimer();
        XNUtil.getXNUtil().finishChat();
        EventBus.getDefault().unregister(this);
        if(null != handler){
            handler.removeCallbacks(runnable);
        }
    }
}
