package com.jjshop.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.base.BaseFragment;
import com.jjshop.bean.ADShowBean;
import com.jjshop.bean.JJEvent;
import com.jjshop.bean.JJShareBean;
import com.jjshop.bean.MyShopBean;
import com.jjshop.bean.NavigationBean;
import com.jjshop.listener.OnCommonCallBackIm;
import com.jjshop.ui.WebViewActivity;
import com.jjshop.ui.activity.home.HomeActivity;
import com.jjshop.ui.activity.shop.ShopEditActivity;
import com.jjshop.ui.presenter.MyShopPresenter;
import com.jjshop.utils.ADUtil;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.ShareUtils;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.UIUtils;
import com.jjshop.utils.UrlInvokeUtil;
import com.jjshop.utils.glide_img.GlideUtil;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.widget.MyScrollView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的店
 */

public class MyShopFragment extends BaseFragment<MyShopPresenter> implements MyScrollView.OnMyScrollChanged{

    @BindView(R.id.m_layout_title)
    RelativeLayout mLayoutTitle;
    @BindView(R.id.m_scroll)
    MyScrollView mScrollView;
    @BindView(R.id.m_iv_bg)
    ImageView mIvBg;
    @BindView(R.id.m_iv_head)
    ImageView mIvHead;
    @BindView(R.id.m_tv_msg_num)
    TextView mTvMsgNum;
    @BindView(R.id.m_tv_shopname)
    TextView mTvShopname;
    @BindView(R.id.m_tv_vip)
    TextView mTvVip;
    @BindView(R.id.m_tv_mobile_city)
    TextView mTvMobileCity;
    @BindView(R.id.m_tv_today_order_num)
    TextView mTvTodayOrderNum;
    @BindView(R.id.m_tv_today_sale_money)
    TextView mTvTodaySaleMoney;
    @BindView(R.id.m_tv_month_sale_money)
    TextView mTvMonthSaleMoney;
    @BindView(R.id.m_tv_dfk_num)
    TextView mTvDfkNum;
    @BindView(R.id.m_tv_dfh_num)
    TextView mTvDfhNum;
    @BindView(R.id.m_tv_dsh_num)
    TextView mTvDshNum;
    @BindView(R.id.m_tv_shtk_num)
    TextView mTvShtkNum;
    @BindView(R.id.m_tv_today_look_num)
    TextView mTvTodayLookNum;
    @BindView(R.id.m_tv_zuo_look_num)
    TextView mTvZuoLookNum;
    @BindView(R.id.m_tv_tixian_money)
    TextView mTvTixianMoney;
    @BindView(R.id.m_tv_yjs_money)
    TextView mTvYjsMoney;
    @BindView(R.id.m_tv_wjs_money)
    TextView mTvWjsMoney;
    @BindView(R.id.m_tv_kdjl)
    TextView mTvKdjl;
    @BindView(R.id.m_view_loading)
    View mViewLoading;
    @BindView(R.id.m_view_error)
    View mViewError;
    @BindView(R.id.m_tv_refresh)
    TextView mTvRefresh;
    @BindView(R.id.m_iv_details_ad)
    ImageView mIvDetailsAd;
    @BindView(R.id.m_iv_shop_bottom_ad)
    ImageView mIvShopBottomAd;

    private int mTitleHeight;
    private JJShareBean shareBean;
    private boolean mIsLoading = false;
    private String mAdUrl = "", mAdBottomUrl = "";
    private ADShowBean.ADShowBeanData adShowBeanData;// 广告数据
    private ADShowBean.ADShowBeanData adShowBeanDataBottom;// 底部广告数据


    @Override
    protected MyShopPresenter getPresenter() {
        return new MyShopPresenter(this);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_my_mail;
    }

    @Override
    public void initView(View view) {
        EventBus.getDefault().register(this);
        mTitleHeight = UIUtils.getWidth() / 2;
        mLayoutTitle.getBackground().setAlpha(0);
        setTitleHeight();
        setBgImgHeight();
        mScrollView.setOnMyScrollChanged(this);
        mLayoutTitle.setVisibility(View.GONE);
        mScrollView.setVisibility(View.GONE);
//        loadData();
    }

    public void onSuccess(MyShopBean.MyShopDataBean bean) {
        mIsLoading = false;
        mErrorNum = 0;
        showDataView();
        // 显示消息点
        int msgNum = bean.getMessage_count();
        if(null != activity && activity instanceof HomeActivity){
            ((HomeActivity)activity).setHomeMsgNum(msgNum);
        }
        mTvMsgNum.setVisibility(msgNum <= 0 ? View.GONE : View.VISIBLE);
        // 店铺信息
        MyShopBean.MyShopDataBean.ShopInfoBean shopInfoBean = bean.getShopinfo();
        if(null != shopInfoBean){
            setShopInfo(shopInfoBean.getShoplogo(), shopInfoBean.getShopname(), shopInfoBean.getShopinfo());
            // 会员等级
            String vip = shopInfoBean.getShop_grade_name();
            if(StringUtil.isEmpty(vip)){
                mTvVip.setVisibility(View.GONE);
            }else{
                mTvVip.setVisibility(View.VISIBLE);
                mTvVip.setText(vip);
            }
            // 刷新首页标题的名称
            EventBus.getDefault().post(new JJEvent(JJEvent.UPDATE_LOCAL_SHOP_INFO));

        }
        // 今日订单、销售；本月销售
        MyShopBean.MyShopDataBean.InfoOrderBean infoOrderBean = bean.getInfoOrderData();
        if(null != infoOrderBean){
            // 今日订单
            mTvTodayOrderNum.setText(String.valueOf(infoOrderBean.getToday_order_num()));
            // 今日销售
            mTvTodaySaleMoney.setText(String.valueOf(infoOrderBean.getToday_total_sales()));
            // 本月销售
            mTvMonthSaleMoney.setText(String.valueOf(infoOrderBean.getMonth_total_sales()));
        }
        //订单类型、数量
        ArrayList<NavigationBean> navigationList = bean.getNavigation();
        if(null != navigationList){
            int size = navigationList.size();
            // 待付款
            if(size >= 1){
                setOrderNum(navigationList.get(0), mTvDfkNum);
            }
            // 待发货
            if(size >= 2){
                setOrderNum(navigationList.get(1), mTvDfhNum);
            }
            // 待收货
            if(size >= 3){
                setOrderNum(navigationList.get(2), mTvDshNum);
            }
            // 售后、退款
            if(size >= 4){
                setOrderNum(navigationList.get(3), mTvShtkNum);
            }
        }
        // 今日浏览人数
        mTvTodayLookNum.setText(String.valueOf(bean.getCurrent_date_uv()));
        // 昨日浏览人数
        mTvZuoLookNum.setText(String.valueOf(bean.getLast_date_uv()));
        // 可提现金额
        MyShopBean.MyShopDataBean.InfoShopBopBean bopBean = bean.getInfoShopBop();
        if(null != bopBean){
            mTvTixianMoney.setText("¥" + bopBean.getBop_total());
        }
        // 收入
        MyShopBean.MyShopDataBean.InfosettledBean infosettledBean = bean.getInfosettled();
        if(null != infoOrderBean){
            // 未结算收入
            mTvWjsMoney.setText("¥" + infosettledBean.getUnsettled());
            // 已结算收入
            mTvYjsMoney.setText("¥" + infosettledBean.getSettled());
        }
        // 开店奖励
        MyShopBean.MyShopDataBean.ShopInviteInfoBean shopInviteInfoBean = bean.getShopInviteInfo();
        if(null != shopInviteInfoBean){
            mTvKdjl.setText("¥" + shopInviteInfoBean.getReward_value());
        }
        // 分享数据
        shareBean = new JJShareBean();
        JJShareBean.DataBean shareData = bean.getSharshopinfo();
        if(null != shareData){
            shareBean.setData(shareData);
        }

    }

    private int mErrorNum;
    public void onFail(String msg) {
        mIsLoading = false;
        JjLog.e("msg = " + msg);
        mErrorNum++;
        if(mErrorNum <= 2){
            loadData();
            return;
        }
        mErrorNum = 0;
        showErrorView();
    }

    //设置订单数量
    private void setOrderNum(NavigationBean navigationBean, TextView tvNum){
        if(null == tvNum){
            return;
        }
        if(null != navigationBean){
            int count = navigationBean.getCount();
            CommonUtils.build().setCirleTextNum(tvNum, count);
            return;
        }
        tvNum.setVisibility(View.GONE);
    }

    private void setShopInfo(String imgUrl, String name, String info){
        PreUtils.saveShop(activity, PreUtils.DEFAULT_SHOP, name,
                info, imgUrl);
        // 店铺头像
        GlideUtil.getInstence().loadCirleImage(activity, mIvHead, imgUrl, R.mipmap.img_shop_default_head);
        // 名称
        if(StringUtil.isEmpty(name)){
            mTvShopname.setText("");
        }else{
            if(name.length() >= 8){
                name = name.substring(0, 8).toString() + "...";
            }
            mTvShopname.setText(name);
        }
        // 信息
        if(StringUtil.isEmpty(info)){
            mTvMobileCity.setText("");
        }else{
            if(info.length() >= 12){
                info = info.substring(0, 12).toString() + "...";
            }
            mTvMobileCity.setText(info);
        }
    }

    @OnClick({R.id.m_layout_bjdp, R.id.m_iv_msg, R.id.m_iv_share, R.id.m_layout_today_order, R.id.m_layout_today_sale,
            R.id.m_layout_month_sale, R.id.m_layout_ddgl, R.id.m_layout_dfk, R.id.m_layout_dfh, R.id.m_layout_dsh,
            R.id.m_layout_shtk, R.id.qiaobao_hit, R.id.m_tv_tixian, R.id.m_layout_wjssr, R.id.m_layout_yjssr, /*R.id.m_layout_szmx,*/
            R.id.m_layout_txjl, R.id.m_layout_kdjl, R.id.m_layout_yqkd, R.id.m_layout_wdpxs, R.id.m_tv_refresh,
            R.id.m_layout_look, R.id.m_iv_details_ad, R.id.m_iv_shop_bottom_ad})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_layout_bjdp:// 编辑店铺
                ShopEditActivity.invoke(activity);
                break;
            case R.id.m_iv_msg:// 消息
                invokeWeb(HttpUrl.WEB_MSG);
                break;
            case R.id.m_iv_share:// 分享
                if(null == shareBean){
                    return;
                }
                ShareUtils.build().shareDialog(activity, shareBean, true, null);
                break;
            case R.id.m_layout_today_order:// 今日订单
                invokeWeb(HttpUrl.WEB_TODAY_ORDER);
                break;
            case R.id.m_layout_today_sale:// 今日销售
                invokeWeb(HttpUrl.WEB_TODAY_SALE);
                break;
            case R.id.m_layout_month_sale:// 本月销售
                invokeWeb(HttpUrl.WEB_MONTH_SALE);
                break;
            case R.id.m_layout_ddgl:// 订单管理
                invokeWeb(HttpUrl.WEB_ALL_ORDER);
                break;
            case R.id.m_layout_dfk:// 待付款
                invokeWeb(HttpUrl.WEB_DFK_ORDER);
                break;
            case R.id.m_layout_dfh:// 待发货
                invokeWeb(HttpUrl.WEB_DFH_ORDER);
                break;
            case R.id.m_layout_dsh:// 待收货
                invokeWeb(HttpUrl.WEB_DSH_ORDER);
                break;
            case R.id.m_layout_shtk:// 售后，退款
                invokeWeb(HttpUrl.WEB_SH_TUIKUAN_ORDER);
                break;
            case R.id.qiaobao_hit:// 钱包提示
                invokeWeb(HttpUrl.WEB_SHOUZHI_MX);
                break;
            case R.id.m_tv_tixian:
                invokeWeb(HttpUrl.WEB_TIXIAN);
                break;
            case R.id.m_layout_wjssr:// 未结算收入
                invokeWeb(HttpUrl.WEB_WJS_MONEY);
                break;
            case R.id.m_layout_yjssr:// 已结算收入
                invokeWeb(HttpUrl.WEB_YJS_MONEY);
                break;
//            case R.id.m_layout_szmx:// 收支明细
//                invokeWeb(HttpUrl.WEB_SHOUZHI_MX);
//                break;
            case R.id.m_layout_txjl:// 提现记录
                invokeWeb(HttpUrl.WEB_TIXIAN_JILU);
                break;
            case R.id.m_layout_kdjl:// 开店奖励
                invokeWeb(HttpUrl.WEB_KD_JIANGLI);
                break;
            case R.id.m_layout_yqkd:// 邀请开店
                invokeWeb(HttpUrl.WEB_YQ_KD);
                break;
            case R.id.m_layout_wdpxs:// 我的培训师
                invokeWeb(HttpUrl.WEB_MY_PXS);
                break;
            case R.id.m_tv_refresh:// 刷新
                loadData();
                break;
            case R.id.m_layout_look:// 退出
                break;
            case R.id.m_iv_details_ad:// 广告
                ADUtil.build().loadADReport(activity, true, adShowBeanData);
                UrlInvokeUtil.build().pushInvoke(activity, UrlInvokeUtil.build().pushData(mAdUrl));
                break;
            case R.id.m_iv_shop_bottom_ad:// 广告-底部
                ADUtil.build().loadADReport(activity, true, adShowBeanDataBottom);
                UrlInvokeUtil.build().pushInvoke(activity, UrlInvokeUtil.build().pushData(mAdBottomUrl));
                break;
        }
    }

    private void invokeWeb(String url){

        WebViewActivity.invoke(activity, url + PreUtils.getString(activity, PreUtils.SHOP_ID, ""));
    }

    /***
     * 刷新我的店铺数据
     */
    public void loadData(){
        if(null != mPresenter){
            if(mIsLoading){
                return;
            }
            CommonUtils.build().otherLog(activity, getClass().getName());
            mIsLoading = true;
            showLoadingView();
            mPresenter.loadData();
            // 加载广告数据
            ADUtil.build().loadADShow(activity, ADUtil.APPKEY_AD_MYSHOP, new OnCommonCallBackIm() {
                @Override
                public void onSuccess(Object o) {
                    if(null != o && o instanceof ADShowBean.ADShowBeanData){
                        adShowBeanData = (ADShowBean.ADShowBeanData) o;
                    }
                }

                @Override
                public void onError(String msg) {
                    adShowBeanData = null;
                    if(null != mIvDetailsAd){
                        mIvDetailsAd.setVisibility(View.GONE);
                    }
                }
            });

            // 加载广告数据底部
            ADUtil.build().loadADShow(activity, ADUtil.APPKEY_AD_MYSHOP_BOTTOM, new OnCommonCallBackIm() {
                @Override
                public void onSuccess(Object o) {
                    if(null != o && o instanceof ADShowBean.ADShowBeanData){
                        adShowBeanDataBottom = (ADShowBean.ADShowBeanData) o;
                    }
                }

                @Override
                public void onError(String msg) {
                    adShowBeanDataBottom = null;
                    if(null != mIvShopBottomAd){
                        mIvShopBottomAd.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
    @Override
    public void onScrollChanged(int x, int y, int oldx, int oldy) {
        if(null == mLayoutTitle){
            return;
        }
        if (y <= 0) {
            mLayoutTitle.getBackground().setAlpha(0);
        } else if (y > 0 && y <= mTitleHeight) {
            float alpha = (255 * y / mTitleHeight);
            mLayoutTitle.getBackground().setAlpha((int) alpha);
        } else {
            mLayoutTitle.getBackground().setAlpha(255);
        }
    }

    // 设置标题的高度
    private void setTitleHeight(){
        if(null == mLayoutTitle){
            return;
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLayoutTitle.getLayoutParams();
        params.width = UIUtils.getWidth();
        params.height = UIUtils.dip2px(50) + UIUtils.getStatusHeight(activity);
        mLayoutTitle.setLayoutParams(params);
        mLayoutTitle.setPadding(0, UIUtils.getStatusHeight(activity), 0, 0);
    }

    // 设置顶部背景图片的高度
    private void setBgImgHeight(){
        if(null == mIvBg){
            return;
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIvBg.getLayoutParams();
        params.width = UIUtils.getWidth();
        params.height = UIUtils.dip2px(135) + UIUtils.getStatusHeight(activity);
        mIvBg.setLayoutParams(params);
        mIvBg.setPadding(0, UIUtils.getStatusHeight(activity), 0, 0);
    }

    private void showLoadingView(){
        if(null == mViewLoading || null == mViewError){
            return;
        }
        mViewLoading.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);
    }
    private void showErrorView(){
        if(null == mViewLoading || null == mViewError || null == mLayoutTitle || null == mScrollView){
            return;
        }
        mViewError.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mLayoutTitle.setVisibility(View.GONE);
        mScrollView.setVisibility(View.GONE);
    }
    private void showDataView(){
        if(null == mViewLoading || null == mViewError || null == mLayoutTitle || null == mScrollView){
            return;
        }
        mLayoutTitle.setVisibility(View.VISIBLE);
        mScrollView.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);
    }
    // 显示隐藏加载框
    public void shareShowHideLoading(int visible){
        if(!(View.GONE == visible || View.VISIBLE == visible)){
            return;
        }
        if(null != mViewLoading){
            mViewLoading.setVisibility(visible);
        }
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
        GlideUtil.getInstence().loadImage(activity, mIvDetailsAd, bundle.getString(ADUtil.KEY_IMG));
    }

    // 显示底部广告图片
    public void showBottomAdImg(Bundle bundle){
        if(null == mIvShopBottomAd || null == bundle || bundle.size() <= 0){
            return;
        }
        mAdBottomUrl = bundle.getString(ADUtil.KEY_URL);
        mIvShopBottomAd.setVisibility(View.VISIBLE);
        int w = UIUtils.getWidth();
        int h = w * bundle.getInt(ADUtil.KEY_HEIGHT) / bundle.getInt(ADUtil.KEY_WIDTH);
        CommonUtils.build().setViewWH(mIvShopBottomAd, w, h);
        GlideUtil.getInstence().loadImage(activity, mIvShopBottomAd, bundle.getString(ADUtil.KEY_IMG));
    }

    @Subscribe
    public void onEvent(JJEvent event){
        if(null == event){
            return;
        }
        int id = event.getEventId();
        switch (id){
            case JJEvent.UPDATE_LOCAL_SHOP_INFO:
                setShopInfo(PreUtils.getString(activity, PreUtils.SHOP_LOGO, ""),
                        PreUtils.getString(activity, PreUtils.SHOP_NAME, ""),
                        PreUtils.getString(activity, PreUtils.SHOP_INFO, ""));
                break;
            case JJEvent.REFRESH_MY_SHOP_INFO:
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
