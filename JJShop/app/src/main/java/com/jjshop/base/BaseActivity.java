package com.jjshop.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gyf.barlibrary.ImmersionBar;
import com.jjshop.R;
import com.jjshop.ui.activity.WelcomeActivity;
import com.jjshop.ui.activity.home.DetailActivity;
import com.jjshop.ui.activity.home.HomeActivity;
import com.jjshop.ui.activity.home.HotZhuanquActivity;
import com.jjshop.ui.activity.home.RushBuyActivity;
import com.jjshop.ui.activity.home.SearchActivity;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.InvokeMarketUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity 基类
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity  implements IView {
    protected P mPresenter;
    protected ImmersionBar mBaseImmersionBar = null;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatus();
        setContentView(setLayoutId());
        unbinder = ButterKnife.bind(this);
        mPresenter = getPresenter();
        initView();
        initData();
        // 当程序处于后台被杀的时候，再次进入时重新启动APP
        if(null != savedInstanceState && !(this instanceof WelcomeActivity)){
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        // 页面统计
        if(!(this instanceof WelcomeActivity || this instanceof HomeActivity)){
            CommonUtils.build().otherLog(this, getClass().getName());
        }
    }

    protected void setStatusBar(){}

    protected abstract int setLayoutId();

    protected void initView(){}

    protected abstract P getPresenter();

    protected void initData(){}

    private void initStatus(){
        if(!(this instanceof RushBuyActivity || this instanceof HomeActivity ||
                this instanceof WelcomeActivity || this instanceof DetailActivity)){
            mBaseImmersionBar = ImmersionBar.with(this);
            mBaseImmersionBar.fitsSystemWindows(true);
            mBaseImmersionBar.statusBarColor(R.color.white);
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M &&
                    InvokeMarketUtil.build().getRomType() != InvokeMarketUtil.ROM_TYPE.MIUI){
                mBaseImmersionBar.statusBarAlpha(0.2f);
            }else{
                mBaseImmersionBar.statusBarDarkFont(true);   //状态栏字体是深色，不写默认为亮色
            }
            mBaseImmersionBar.flymeOSStatusBarFontColor(R.color.color_333333).init();
        }
        setStatusBar();
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /** 设置图片状态栏 */
    public ImmersionBar setStatusImgBar(Activity activity) {
        if(null == activity){
            return null;
        }
        ImmersionBar immersionBar = ImmersionBar.with(activity);
        immersionBar.fitsSystemWindows(false);
        immersionBar.statusBarDarkFont(true);   //状态栏字体是深色，不写默认为亮色
        immersionBar.transparentBar();
        immersionBar.fullScreen(false);
        immersionBar.init();
        return immersionBar;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mPresenter) {
            mPresenter.clearData();
        }
        if(null != mBaseImmersionBar){
            mBaseImmersionBar.destroy();
        }
        if(null != unbinder){
            unbinder.unbind();
        }
        // 在使用分享或者授权的Activity中的onDestroy调用此方法，防止内存泄漏
        if(this instanceof HomeActivity || this instanceof SearchActivity || this instanceof HotZhuanquActivity
                || this instanceof RushBuyActivity || this instanceof DetailActivity){
            UMShareAPI.get(this).release();
        }
    }
}
