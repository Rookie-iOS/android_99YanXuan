package com.jjshop.ui.activity.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gyf.barlibrary.ImmersionBar;
import com.jjshop.R;
import com.jjshop.app.JJShopApplication;
import com.jjshop.base.BaseActivity;
import com.jjshop.bean.ApkInfoBean;
import com.jjshop.bean.CitysPickerBean;
import com.jjshop.bean.HomeClassifyBean;
import com.jjshop.bean.JsonCityBean;
import com.jjshop.bean.TabEntity;
import com.jjshop.download.DownLoadDialog;
import com.jjshop.listener.OnCommonCallBackIm;
import com.jjshop.ui.fragment.HomeFragment;
import com.jjshop.ui.fragment.MyCenterFragment;
import com.jjshop.ui.fragment.MyShopFragment;
import com.jjshop.ui.fragment.ShopCarFragment;
import com.jjshop.ui.presenter.HomePresenter;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.PermissionUtil;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.UIUtils;
import com.jjshop.utils.UrlInvokeUtil;
import com.jjshop.utils.XNUtil;
import com.jjshop.utils.dbutils.DBManager;
import com.jjshop.utils.dbutils.LitepalUtil;
import com.jjshop.widget.NoScrollViewPager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class HomeActivity extends BaseActivity<HomePresenter> {

    private NoScrollViewPager noScrollViewPager;
    private CommonTabLayout commonTabLayout;
    private TextView mTvCartNum, mTvXnmsg;

    private String[] mTitles = {"首页", "我的店", "购物车", "个人中心"};

    private int[] mIconUnselectIds = {
            R.mipmap.img_home_bottom_tab_one_false, R.mipmap.img_home_bottom_tab_two_false,
            R.mipmap.img_home_bottom_tab_three_false, R.mipmap.img_home_bottom_tab_four_false};

    private int[] mIconSelectIds = {
            R.mipmap.img_home_bottom_tab_one_true, R.mipmap.img_home_bottom_tab_two_true,
            R.mipmap.img_home_bottom_tab_three_true, R.mipmap.img_home_bottom_tab_four_true
    };

    private ArrayList<CustomTabEntity> mTabEntities = null;
    private HomeFragment homeFragment = null;
    private MyShopFragment myShopFragment = null;
    private ShopCarFragment shopCarFragment = null;
    private MyCenterFragment myCenterFragment = null;

    public static final int SELECT_HOME = 0;
    public static final int SELECT_SHOP = 1;
    public static final int SELECT_CART = 2;
    public static final int SELECT_PERSON = 3;
    private static final String SELECT_KEY = "position";

    // 城市数据加载错误次数
    private int mCityLoadErrorNum;
    // 获取本地城市数据实体bean
    private CitysPickerBean mCitysPickerBean = null;
    // 本地城市的数据库
    private DBManager dbManager;
    // HomeActivity 自身的实例
    public static HomeActivity homeInstance;
    // viewpager适配器
    private MyAdapter myAdapter;
    // 升级提示实体bean
    private ApkInfoBean.DataBean apkBean;

    public static void setHomeInstance(HomeActivity tabHome) {
        if (null == homeInstance) {
            homeInstance = tabHome;
        }
    }

    @Override
    protected HomePresenter getPresenter() {
        return new HomePresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_home;
    }

    public static void invoke(Context context, int position){
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(SELECT_KEY, position);
        if(!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            JjLog.e("不是activity");
        }
        context.startActivity(intent);
        if(null != context && context instanceof Activity && !(((Activity)context).isFinishing())
                && !(context instanceof HomeActivity)){
            ((Activity)context).finish();
        }
    }

    public static void invoke(Context context){
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    // push消息点击
    public static void invokePush(Context context, Map<String, String> map){
        Intent intent = new Intent(context, HomeActivity.class);
        if(!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            JjLog.e("不是activity");
        }
        intent.putExtra("map", (Serializable) map);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setHomeInstance(this);
        // 崩溃时重启应用
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);
        noScrollViewPager = findViewById(R.id.no_scroll_viewpager);
        commonTabLayout = findViewById(R.id.commonTabLayout);
        mTvCartNum = findViewById(R.id.m_tv_cart_num);
        mTvXnmsg = findViewById(R.id.m_tv_xnmsg);
        PermissionUtil.build().checkPermission(this, PermissionUtil.READ_EXTERNAL_STORAGE);
        PermissionUtil.build().checkPermission(this, PermissionUtil.WRITE_EXTERNAL_STORAGE);
        // 检测是否有新版本
        mPresenter.loadAPKInfo();
        // 设置购物车数量的位置
        setCartNumMargin();
        // 初始化数据库
        dbManager = new DBManager(this);
        // 加载城市列表数据
        loadCityData();
        // 设置小能客服事件监听
        XNUtil.getXNUtil().setOnXnCallBack(this);
        // 设置沉浸式
        setStatusImgBar();
        // push跳转
        pushInvoke(getIntent());
        // 检测通知权限
        PermissionUtil.build().settingTongzhi(this);
        // 删除之前下载apk
        try {
            DownLoadDialog.getInstence().delOldApk(this);
        }catch (Exception e){

        }
    }

    @Override
    protected void initData() {
        mTabEntities = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        noScrollViewPager.setOffscreenPageLimit(4);
        myAdapter = new MyAdapter(getSupportFragmentManager());
        noScrollViewPager.setAdapter(myAdapter);
        // 默认选中第一个
        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setCurrentTab(0);

        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if(null == noScrollViewPager || null == commonTabLayout){
                    return;
                }
                String shop = PreUtils.getString(HomeActivity.this, PreUtils.SHOP_ID);
                if(StringUtil.isEmpty(shop)){
                    commonTabLayout.setCurrentTab(0);
                    return;
                }
                noScrollViewPager.setCurrentItem(position, false);
                sellectLoading(position);
                replaceEdit();
                if(position == 0 || position == 2){
                    setStatusTextColor(false);
                }else{
                    setStatusTextColor(true);
                }
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }

    /**
     * 设置购物车数量红点、个人中心消息红点位置
     */
    private void setCartNumMargin(){
        if(null == mTvCartNum || null == mTvXnmsg){
            return;
        }
        int oneWidth = UIUtils.getWidth() / 4;
        int left = oneWidth * 2 + oneWidth / 2;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTvCartNum.getLayoutParams();
        params.setMargins(left, 0, 0, 0);
        mTvCartNum.setLayoutParams(params);

        int right = oneWidth / 10 * 3;
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) mTvXnmsg.getLayoutParams();
        params2.setMargins(0, 0, right, 0);
        mTvXnmsg.setLayoutParams(params2);
    }

    /**
     * 是否显示未读消息小红点
     * @param num
     */
    public void isShowXnmsg(int num){
        if(null != myCenterFragment){
            myCenterFragment.setXnmsgNum(num);
        }
        if(null != mTvXnmsg){
            if(num > 0){
                mTvXnmsg.setVisibility(View.VISIBLE);
            }else{
                mTvXnmsg.setVisibility(View.GONE);
            }

        }
    }

    /**
     * 设置购物车的数量
     * @param num
     */
    public void setCartNum(int num){
        CommonUtils.build().setCirleTextNum(mTvCartNum, num);
    }

    /**
     * 显示首页的小红点
     * @param num
     */
    public void setHomeMsgNum(int num){
        if(null != homeFragment){
            homeFragment.showMsgNum(num);
        }
    }

    /**
     * 购物车的处于编辑状态的时候，恢复到正常状态
     */
    public void replaceEdit(){
        if(null != shopCarFragment){
            shopCarFragment.replaceEdit();
        }
    }

    // 加载首页标题数据
    public void loadTitleData(boolean isLoadNetData){
        if(null != mPresenter){
            mPresenter.loadTitleData(isLoadNetData);
        }
    }

    // 加载城市数据
    public void loadCityData(){
        if(null != mPresenter){
            mCityLoadErrorNum++;
            mPresenter.loadCitysData();
        }
    }

    // 加载升级apk信息成功
    public void onApkSuccess(ApkInfoBean bean){
        ApkInfoBean.DataBean infoBean = bean.getData();
        apkBean = infoBean;
        if(null != myCenterFragment){
            myCenterFragment.setUpdateDian();
        }
        if(null == infoBean){
            return;
        }
        if(infoBean.getForce() == false && PreUtils.getBoolean(this, PreUtils.IS_SHOW_DOWNLOAD, false)){
            return;
        }
        DownLoadDialog.getInstence().show(this,
                infoBean.getForce() ? DownLoadDialog.UPDATE_TRUE : DownLoadDialog.UPDATE_FALSE,
                infoBean.getPath(), infoBean.getNotice(), infoBean.getVid());
    }

    // 加载升级apk信息失败
    public void onApkFail(String msg){
        apkBean = null;
    }

    // 首页标题数据加载成功
    public void onTitleSuccess(HomeClassifyBean bean){
        mErrorNum = 0;
        if(null != homeFragment){
            homeFragment.onSuccess(bean);
        }
    }

    // 首页标题数据加载失败
    private int mErrorNum;
    public void onTitleFail(final String msg){
        mErrorNum++;
        if(mErrorNum <= 2){
            loadTitleData(true);
            return;
        }
        mErrorNum = 0;
        CommonUtils.build().loadLocalData(this, LitepalUtil.HOME_TITLE_KEY, new OnCommonCallBackIm() {
            @Override
            public void onError(String msg) {
                if(null != homeFragment){
                    homeFragment.onFail(msg);
                }
            }
        });

    }

    // 城市列表加载成功
    public void onCitysSuccess(JsonCityBean bean){
        if(null != dbManager){
            PreUtils.setString(this, PreUtils.IS_UPDATE_CITY_DATA, bean.getIsnew());
            mPresenter.saveCitysData(dbManager, bean.getJson());
        }
    }

    // 城市加载失败
    public void onFail(String str){
        if(null != dbManager){
            mPresenter.loadIsHasLocalCitysData(dbManager);
        }
    }

    // 本地是否有城市数据
    public void isHasLocalCitysData(boolean b){
        if(b && null != dbManager && null != mPresenter){
            mPresenter.loadLocalCitysData(dbManager);
            return;
        }
        if(mCityLoadErrorNum >= 3){
            return;
        }
        loadCityData();
    }

    // 城市列表保存本地成功
    public void onSaveSuccess(){
        if(null != dbManager && null != mPresenter){
            mPresenter.loadLocalCitysData(dbManager);
        }
    }

    // 获取本地城市列表成功
    public void onGetLocalSuccess(CitysPickerBean bean){
        mCitysPickerBean = bean;
    }

    // 最终解析完成的城市数据
    public CitysPickerBean citysPickerBean(){
        return mCitysPickerBean;
    }


    // 显示隐藏加载框
    public void shareShowHideLoading(int visible){
        if(!(View.GONE == visible || View.VISIBLE == visible)){
            return;
        }
        if(null == noScrollViewPager){
            return;
        }
        if(noScrollViewPager.getCurrentItem() == SELECT_HOME){
            if(null != homeFragment){
                homeFragment.shareShowHideLoading(visible);
            }
        }else if(noScrollViewPager.getCurrentItem() == SELECT_SHOP){
            if(null != myShopFragment){
                myShopFragment.shareShowHideLoading(visible);
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        shareShowHideLoading(View.GONE);
        scrollShowFuchuang(View.VISIBLE);
    }

    public class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        public final static int TAB_COUNT = 4;

        @Override
        public Fragment getItem(int id) {
            switch (id) {
                case 0:
                    homeFragment = new HomeFragment();
                    return homeFragment;
                case 1:
                    myShopFragment = new MyShopFragment();
                    return myShopFragment;
                case 2:
                    shopCarFragment = new ShopCarFragment();
                    return shopCarFragment;
                case 3:
                    myCenterFragment = new MyCenterFragment();
                    return myCenterFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }

    /**
     * 获取购物车fragment
     * @return
     */
    public ShopCarFragment getCurrFragment(){
        if(null != noScrollViewPager && noScrollViewPager.getCurrentItem() == 2){
            return shopCarFragment;
        }
        return null;
    }

    /**
     * 升级apk实体bean
     * @return
     */
    public ApkInfoBean.DataBean apkBean(){
        return apkBean;
    }

    /**
     * 设置选中某一个TAB
     * @param position
     */
    public void selectPosition(int position){
        if(null != noScrollViewPager && null != commonTabLayout && null != myAdapter) {
            if(position < 0 || position >= myAdapter.getCount()){
                return;
            }
            noScrollViewPager.setCurrentItem(position, false);
            commonTabLayout.setCurrentTab(position);
            sellectLoading(position);
        }
    }

    ImmersionBar immersionBar;
    /** 设置图片状态栏 */
    private void setStatusImgBar() {
        immersionBar = ImmersionBar.with(this);
        immersionBar.fitsSystemWindows(false);
        immersionBar.transparentBar();
        immersionBar.fullScreen(false);
        setStatusTextColor(false);
    }

    /**
     * 设置状态栏字体颜色
     * @param isWhite
     */
    private void setStatusTextColor(boolean isWhite){
        if(null != immersionBar){
            immersionBar.flymeOSStatusBarFontColor(isWhite ? R.color.white : R.color.color_333333);
            immersionBar.statusBarDarkFont(!isWhite);// 状态栏字体是深色，不写默认为亮色
            immersionBar.init();
        }
    }

    // 点击push进来
    private void pushInvoke(Intent intent){
        if(null != intent && intent.hasExtra("map")){
            Map<String, String> map = (Map<String, String>) intent.getSerializableExtra("map");
            UrlInvokeUtil.build().pushInvoke(this, map);
        }
    }

    // 加载选中页面的数据
    private void sellectLoading(int position){
        switch (position){
            case 0:
                if(null != homeFragment){
                    homeFragment.log();
                }
                break;
            case 1:
                if(null != myShopFragment){
                    myShopFragment.loadData();
                }
                break;
            case 2:
                if(null != shopCarFragment){
                    shopCarFragment.loadData();
                }
                break;
            case 3:
                if(null != myCenterFragment){
                    myCenterFragment.loadData();
                }
                break;
        }
    }

    /**
     * 显示浮窗
     * @param img
     * @param url
     */
    public void showFuchuang(String img, String url){
        if(null != homeFragment){
            homeFragment.showFuchuang(img, url);
        }
    }

    /**
     * 滑动时隐藏、显示浮窗
     */
    public void scrollShowFuchuang(int visi){
        if(null != homeFragment){
            homeFragment.scrollShowFuchuang(visi);
        }
    }

    // 显示广告图片
    public void showAdImg(Bundle bundle){
        if(null != myShopFragment){
            myShopFragment.showAdImg(bundle);
        }
    }

    // 显示广告图片-底部
    public void showBottomAdImg(Bundle bundle){
        if(null != myShopFragment){
            myShopFragment.showBottomAdImg(bundle);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != immersionBar){
            immersionBar.destroy();
        }
        if(null != dbManager){
            dbManager.closeDB();
            dbManager = null;
        }
        if(null != noScrollViewPager){
            noScrollViewPager.removeAllViews();
        }
        if(null != mTabEntities){
            mTabEntities.clear();
        }

        homeInstance = null;

        XNUtil.getXNUtil().loginOut();

        JJShopApplication.yxfc = "";
        JJShopApplication.mCookie = "";
        JJShopApplication.mUser_agent = "";
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(null == intent){
            return;
        }
        pushInvoke(intent);
        if(intent.hasExtra(SELECT_KEY)){
            int position = intent.getIntExtra(SELECT_KEY, 0);
            selectPosition(position);
        }
    }

    long startTime = 0;
    @Override
    public void onBackPressed() {
        long endTime = System.currentTimeMillis();
        if (endTime - startTime < 2000) {
            super.onBackPressed();
        } else {
            startTime = endTime;
            JjToast.getInstance().toast("再按一次退出应用");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PermissionUtil.PERMISSION_PACKAGEINSTALLS){
            DownLoadDialog.getInstence().installApk();
        }
    }
}
