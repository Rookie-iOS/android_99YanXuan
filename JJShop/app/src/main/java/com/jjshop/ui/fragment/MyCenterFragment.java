package com.jjshop.ui.fragment;

import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.app.JJShopApplication;
import com.jjshop.base.BaseFragment;
import com.jjshop.bean.ApkInfoBean;
import com.jjshop.bean.JJEvent;
import com.jjshop.bean.NavigationBean;
import com.jjshop.bean.PersonCenterBean;
import com.jjshop.dialog.CartNumDelDialog;
import com.jjshop.download.DownLoadDialog;
import com.jjshop.ui.LoginActivity;
import com.jjshop.ui.activity.home.HomeActivity;
import com.jjshop.ui.activity.person.AfterSaleRefundActivity;
import com.jjshop.ui.activity.person.MyAddressActivity;
import com.jjshop.ui.activity.person.MyCouponActivity;
import com.jjshop.ui.activity.person.MyOrderActivity;
import com.jjshop.ui.activity.person.PersonEditActivity;
import com.jjshop.ui.activity.person.TeamSalesActivity;
import com.jjshop.ui.presenter.PersonCenterPresenter;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.UIUtils;
import com.jjshop.utils.XNUtil;
import com.jjshop.utils.glide_img.GlideUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 个人中心
 */

public class MyCenterFragment extends BaseFragment<PersonCenterPresenter> {
    @BindView(R.id.m_layout_sv)
    ScrollView mLayoutSv;
    @BindView(R.id.m_iv_bg)
    ImageView mIvBg;
    @BindView(R.id.m_iv_head)
    ImageView mIvHead;
    @BindView(R.id.m_tv_shopname)
    TextView mTvShopname;
    @BindView(R.id.m_tv_vip)
    TextView mTvVip;
    @BindView(R.id.m_tv_mobile_city)
    TextView mTvMobileCity;
    @BindView(R.id.m_tv_edit_shop)
    TextView mTvEditShop;
    @BindView(R.id.m_layout_ddgl)
    RelativeLayout mLayoutDdgl;
    @BindView(R.id.m_tv_order_guanli)
    TextView mTvOrderGuanli;
    @BindView(R.id.m_tv_dfk_num)
    TextView mTvDfkNum;
    @BindView(R.id.m_tv_dfh_num)
    TextView mTvDfhNum;
    @BindView(R.id.m_tv_dsh_num)
    TextView mTvDshNum;
    @BindView(R.id.m_tv_shtk_num)
    TextView mTvShtkNum;
    @BindView(R.id.m_tv_xnmsg_num)
    TextView mTvXnmsgNum;
    @BindView(R.id.m_view_loading)
    View mViewLoading;
    @BindView(R.id.m_view_error)
    View mViewError;
    @BindView(R.id.m_tv_version)
    TextView mTvVersion;
    @BindView(R.id.m_tv_update)
    TextView mTvUpdate;

    private boolean mIsLoading = false;

    @Override
    protected PersonCenterPresenter getPresenter() {
        return new PersonCenterPresenter(this);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_mycenter;
    }

    @Override
    public void initView(View view) {
        EventBus.getDefault().register(this);
        mTvVersion.setText("V" + JJShopApplication.sVersion);
        setBgImgHeight();
        // 隐藏vip标识
        mTvVip.setVisibility(View.GONE);
        // 隐藏编辑店铺字样
        mTvEditShop.setVisibility(View.GONE);
        mTvOrderGuanli.setText("我的订单");
        // 设置订单管理的margin
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLayoutDdgl.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        mLayoutDdgl.setLayoutParams(params);
        // 设置头像margin
        RelativeLayout.LayoutParams headParams = (RelativeLayout.LayoutParams) mIvHead.getLayoutParams();
        headParams.setMargins(UIUtils.dip2px(25), 0, UIUtils.dip2px(12), UIUtils.dip2px(40));
        mIvHead.setLayoutParams(headParams);
        mLayoutSv.setVisibility(View.GONE);
//        loadData();
    }

    public void loadData(){
        if(null != mPresenter){
            if(mIsLoading){
                return;
            }
            CommonUtils.build().otherLog(activity, getClass().getName());
            mIsLoading = true;
            showLoadingView();
            mPresenter.loadData();
        }
    }

    @OnClick({R.id.m_layout_bjdp, R.id.m_layout_ddgl, R.id.m_layout_dfk, R.id.m_layout_dfh, R.id.m_layout_dsh,
            R.id.m_layout_shtk, R.id.m_layout_quan, R.id.m_layout_address, R.id.m_layout_lxkf, R.id.m_tv_refresh,
            R.id.m_layout_version, R.id.m_tv_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_layout_bjdp:
                PersonEditActivity.invoke(activity);
                break;
            case R.id.m_layout_ddgl:
                MyOrderActivity.invoke(activity, 0);
                break;
            case R.id.m_layout_dfk:
                MyOrderActivity.invoke(activity, 1);
                break;
            case R.id.m_layout_dfh:
                MyOrderActivity.invoke(activity, 2);
                break;
            case R.id.m_layout_dsh:
                MyOrderActivity.invoke(activity, 3);
                break;
            case R.id.m_layout_shtk:
                AfterSaleRefundActivity.invoke(activity);
                break;
            case R.id.m_layout_quan:
                MyCouponActivity.invoke(activity);
                break;
            case R.id.m_layout_address:
                MyAddressActivity.invoke(activity, MyAddressActivity.INVOKE_PERSON);
                break;
            case R.id.m_layout_lxkf:
                XNUtil.getXNUtil().openChat(activity, null);
                break;
            case R.id.m_layout_version:
                // TODO: 2019/2/20 测试
//                TeamSalesActivity.invoke(activity);
                if(null != activity && activity instanceof HomeActivity){
                    ApkInfoBean.DataBean infoBean = ((HomeActivity)activity).apkBean();
                    if(null == infoBean){
                        JjToast.getInstance().toast("当前已是最新版本");
                        return;
                    }
                    DownLoadDialog.getInstence().show(activity,
                            infoBean.getForce() ? DownLoadDialog.UPDATE_TRUE : DownLoadDialog.UPDATE_FALSE,
                            infoBean.getPath(), infoBean.getNotice(), infoBean.getVid());
                    return;
                }
                JjToast.getInstance().toast("当前已是最新版本");
                break;
            case R.id.m_tv_exit:
                CartNumDelDialog.build().showView(getFragmentManager(), 0, CartNumDelDialog.EXIT_CODE)
                        .setOnCommonClickCalllBack(new CartNumDelDialog.OnCommonClickCalllBack() {
                    @Override
                    public void callBack(int typeCode, int num) {
                        if(typeCode == CartNumDelDialog.EXIT_CODE){
                            // 清除webview的cookie
                            CookieManager cookieManager = CookieManager.getInstance();
                            cookieManager.removeSessionCookie();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                cookieManager.removeAllCookies(null);
                            } else {
                                cookieManager.removeAllCookie();
                            }
                            // 退出小能登录
                            XNUtil.getXNUtil().loginOut();
                            LoginActivity.invoke(activity);
                            System.exit(0);
                        }
                    }
                });
                break;
            case R.id.m_tv_refresh:
                loadData();
                break;
        }
    }

    public void onSuccess(PersonCenterBean.PersonCenterData bean){
        mErrorNum = 0;
        mIsLoading = false;
        showDataView();
        PersonCenterBean.PersonCenterData.PersonInfoBean infoBean = bean.getData();
        if(null != infoBean){
            setUserInfo(infoBean.getImg(), infoBean.getName(), infoBean.getMobile(), infoBean.getAreas_name());
            // 保存用户信息
            PreUtils.saveUser(activity, infoBean);
            // 登录小能客服
            XNUtil.getXNUtil().login(activity);
        }
        //订单类型、数量
        ArrayList<NavigationBean> navigationList = bean.getNavigation();
        if(null != navigationList) {
            int size = navigationList.size();
            // 待付款
            if (size >= 1) {
                setOrderNum(navigationList.get(0), mTvDfkNum);
            }
            // 待发货
            if (size >= 2) {
                setOrderNum(navigationList.get(1), mTvDfhNum);
            }
            // 待收货
            if (size >= 3) {
                setOrderNum(navigationList.get(2), mTvDshNum);
            }
            // 售后、退款
            if (size >= 4) {
                setOrderNum(navigationList.get(3), mTvShtkNum);
            }
        }

    }

    private int mErrorNum;
    public void onFail(String msg){
        mErrorNum++;
        mIsLoading = false;
        if(mErrorNum <= 2){
            loadData();
            return;
        }
        mErrorNum = 0;
        showErrorView();
    }

    private void setUserInfo(String img, String name, String mobile, String city){
        if(null == mIvHead || null == mTvShopname || null == mTvMobileCity){
            return;
        }
        // 头像
        GlideUtil.getInstence().loadCirleImage(activity, mIvHead, img, R.mipmap.img_person_default_head);
        // 用户昵称
        mTvShopname.setText(name);
        // 手机号、城市
        mTvMobileCity.setText(mobile + " " + city);
    }

    // 设置顶部背景图片的高度
    private void setBgImgHeight() {
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
        if(null == mViewError || null == mViewLoading){
            return;
        }
        mViewLoading.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);
    }
    private void showErrorView(){
        if(null == mLayoutSv || null == mViewError || null == mViewLoading){
            return;
        }
        mViewError.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mLayoutSv.setVisibility(View.GONE);
    }
    private void showDataView(){
        if(null == mLayoutSv || null == mViewError || null == mViewLoading){
            return;
        }
        mLayoutSv.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);
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

    /**
     * 显示小能客服未读消息个数
     * @param num
     */
    public void setXnmsgNum(int num){
        if(null != mTvXnmsgNum){
            if(num > 0){
                mTvXnmsgNum.setVisibility(View.VISIBLE);
                mTvXnmsgNum.setText(num + "条未读消息，点击查看");
            }else{
                mTvXnmsgNum.setVisibility(View.GONE);
            }

        }
    }
    /**
     * 显示更新小红点
     */
    public void setUpdateDian(){
        if(null != mTvUpdate){
            mTvUpdate.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void onEvent(JJEvent event){
        if(null == event){
            return;
        }
        int id = event.getEventId();
        switch (id){
            case JJEvent.UPDATE_LOCAL_PERSON_INFO:
                setUserInfo(PreUtils.getString(activity, PreUtils.USER_IMG),
                        PreUtils.getString(activity, PreUtils.USER_NICKNAME),
                        PreUtils.getString(activity, PreUtils.USER_MOBILE),
                        PreUtils.getString(activity, PreUtils.USER_CITY));
                break;
            case JJEvent.REFRESH_PERSON_INFO:
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
