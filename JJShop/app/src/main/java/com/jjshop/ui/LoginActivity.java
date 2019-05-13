package com.jjshop.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.app.JJShopApplication;
import com.jjshop.base.BaseActivity;
import com.jjshop.bean.LoginBean;
import com.jjshop.ui.activity.home.HomeActivity;
import com.jjshop.ui.presenter.LoginPresenter;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.Tools;

import java.io.Serializable;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginPresenter> {

    @BindView(R.id.m_title_back)
    ImageView mTitleBack;
    @BindView(R.id.m_tv_title)
    TextView mTvTitle;
    @BindView(R.id.m_title_right_tv)
    TextView mTitleRightTv;
    @BindView(R.id.m_et_mobile)
    EditText mEtMobile;
    @BindView(R.id.m_tv_code)
    TextView mTvCode;
    @BindView(R.id.m_et_code)
    EditText mEtCode;
    @BindView(R.id.m_tv_login)
    TextView mTvLogin;
    @BindView(R.id.m_view_loading)
    View mViewLogin;

    private CountDownTimer timer;
    private Map<String, String> pushMap;

    public static void invoke(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void invokePush(Context context, Map<String, String> map) {
        Intent intent = new Intent(context, LoginActivity.class);
        if(!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            JjLog.e("不是activity");
        }
        intent.putExtra("map", (Serializable) map);
        context.startActivity(intent);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        PreUtils.clear(this);
        mTitleBack.setVisibility(View.GONE);
        mTvTitle.setText("登录");
        CommonUtils.build().initUserAgentAndCookie(this);
        pushData(getIntent());
    }

    private void pushData(Intent intent){
        JjLog.e("push消息进来");
        if(intent != null && intent.hasExtra("map")){
            pushMap = (Map<String, String>) intent.getSerializableExtra("map");
        }
    }

    @Override
    protected LoginPresenter getPresenter() {
        return new LoginPresenter(this);
    }

    // 获取验证码成功
    public void onCodeSuccess() {
        mViewLogin.setVisibility(View.GONE);
        startTimer();
        JjToast.getInstance().toast("验证码发送成功");
    }

    // 登录成功
    public void onLoginSuccess(LoginBean bean) {
        mViewLogin.setVisibility(View.GONE);
        LoginBean.DataBean dataBean = bean.getData();
        if(null == dataBean){
            return;
        }
        // 保存登录信息
        PreUtils.saveLoginInfo(this, bean.getUserId(), bean.getUserMobile(), dataBean);
        JJShopApplication.mCookie = bean.getUserId() + ";" + bean.getUserMobile();
        if(null == pushMap || pushMap.size() <= 0){
            HomeActivity.invoke(this);
        }else{
            HomeActivity.invokePush(this, pushMap);
        }
        finish();
    }

    // 请求失败
    public void onFail(String msg) {
        mTvLogin.setEnabled(true);
        mTvLogin.setClickable(true);
        mViewLogin.setVisibility(View.GONE);
        JjToast.getInstance().toast(msg);
    }

    public String mobile() {
        if(null == mEtMobile || StringUtil.isEmpty(mEtMobile.getText().toString())){
            return "";
        }
        return mEtMobile.getText().toString();
    }

    public String code() {
        if(null == mEtCode || StringUtil.isEmpty(mEtCode.getText().toString())){
            return "";
        }
        return mEtCode.getText().toString();
    }

    @OnClick({R.id.m_tv_code, R.id.m_tv_login})
    public void onClick(View view) {
        if(!Tools.isNetConnected(this)){
            JjToast.getInstance().toast("暂无网络，请稍后再试");
            return;
        }
        switch (view.getId()) {
            case R.id.m_tv_code:
                Tools.hideSoftInput(view);
                mViewLogin.setVisibility(View.VISIBLE);
                mPresenter.loadCode();
                break;
            case R.id.m_tv_login:
                mTvLogin.setEnabled(false);
                mTvLogin.setClickable(false);
                Tools.hideSoftInput(view);
                mViewLogin.setVisibility(View.VISIBLE);
                mPresenter.loadLogin();
                break;
        }
    }

    // 开始倒计时
    private void startTimer(){
        stopTimer();
        if(null == mTvCode){
            return;
        }
        mTvCode.setEnabled(false);
        mTvCode.setClickable(false);
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                mTvCode.setText(Tools.getSmh(l / 1000, true) + "S");
            }

            @Override
            public void onFinish() {
                if(null == mTvCode){
                    return;
                }
                mTvCode.setText("获取验证码");
                mTvCode.setEnabled(true);
                mTvCode.setClickable(true);
            }
        }.start();
    }

    // 停止倒计时
    private void stopTimer(){
        if(null != timer){
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        JjLog.e("进来了");
        pushData(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
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

}
