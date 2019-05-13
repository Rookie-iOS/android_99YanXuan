package com.jjshop.ui.activity.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.jjshop.R;
import com.jjshop.base.BaseActivity;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.DetailBean;
import com.jjshop.bean.JJEvent;
import com.jjshop.template_view.TemplateVideoView;
import com.jjshop.ui.LoginActivity;
import com.jjshop.ui.fragment.GoodsInfoFragment;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.ShareUtils;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.Tools;
import com.jjshop.utils.UIUtils;
import com.jjshop.utils.UrlInvokeUtil;
import com.jjshop.utils.httputil.HttpUrl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class DetailActivity extends BaseActivity {
    @BindView(R.id.m_tv_title)
    TextView mTvTitle;
    @BindView(R.id.m_layout_title)
    RelativeLayout mLayoutTitle;
    @BindView(R.id.image_share)
    ImageView image_share;
    @BindView(R.id.m_view_status)
    View mViewStatus;

    private String mIdCode = "", mProductId = "";
    private GoodsInfoFragment fragment;
    private ImmersionBar immersionBar;

    public static void invoke(Context context, String idCode, String productId) {
        JjLog.e("商品详情 idcode = " + idCode + "----id = " + productId);
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("id_code", idCode);
        intent.putExtra("product_id", productId);
        context.startActivity(intent);
    }

    /**
     * 点击push消息进来
     * @param context
     */
    public static void invokePush(Context context, Map<String, String> map) {
        if(StringUtil.isEmpty(HttpUrl.getCookies(context))){
            LoginActivity.invokePush(context, map);
            return;
        }
        if(null == HomeActivity.homeInstance){
            HomeActivity.invokePush(context, map);
            return;
        }
        if(null == map || map.size() <= 0 ||
                (!map.containsKey(UrlInvokeUtil.KEY_ID) && !map.containsKey(UrlInvokeUtil.KEY_IDCODE))){
            return;
        }
        Intent intent = new Intent(context, DetailActivity.class);
        if(!(context instanceof Activity)){
            JjLog.e("不是activity");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra("id_code", map.get(UrlInvokeUtil.KEY_IDCODE));
        intent.putExtra("product_id", map.get(UrlInvokeUtil.KEY_ID));
        context.startActivity(intent);
        map.clear();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_detail_layout;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        immersionBar = setStatusImgBar(this);
        EventBus.getDefault().register(this);
        if (null != getIntent()) {
            mIdCode = getIntent().getStringExtra("id_code");
            mProductId = getIntent().getStringExtra("product_id");
        }
        fragment = GoodsInfoFragment.invoke(mIdCode, mProductId);
        Tools.addFrament(R.id.m_frame_layout, getSupportFragmentManager(), fragment, false);
        // 设置状态栏、标题的高度
        CommonUtils.build().setStatusViewH(mViewStatus, false);
        int h = UIUtils.dip2px(50) + UIUtils.getStatusHeight(this);
        CommonUtils.build().setViewH(mLayoutTitle, h);
    }

    @OnClick({R.id.image_left, R.id.image_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_left:
                if (TemplateVideoView.backPress()) {
                    return;
                }
                finish();
                break;
            case R.id.image_share:
                share();
                break;
        }
    }

    // 显示分享图片按钮
    public void showShareImg(){
        if(null != image_share){
            image_share.setVisibility(View.VISIBLE);
        }
    }

    // 分享
    public void share(){
        if(null != fragment){
            if(fragment.getIsShouqing() == View.GONE){
                ShareUtils.build().loadShare(this, mProductId);
            }
        }
    }

    // 是否显示加载框
    public void shareShowHideLoading(int visible) {
        if (null != fragment) {
            fragment.shareShowHideLoading(visible);
        }
    }

    // 显示规格弹框
    public void selectGuige(String guige, int colorPosition, int sizePosition) {
        if (null != fragment) {
            fragment.selectGuige(guige, colorPosition, sizePosition);
        }
    }

    // 加入购物车
    public void addCart(String sku, int num) {
        if (null != fragment) {
            fragment.addCart(sku, num);
        }
    }

    // 立即购买
    public void lijiBuy(String sku, int num) {
        if (null != fragment) {
            fragment.lijiBuy(sku, num);
        }
    }

    // 设置标题背景颜色
    public void setTitleBg(int color, int tvColor) {
        if(null != mLayoutTitle && null != mTvTitle){
            mLayoutTitle.setBackgroundColor(color);
            mTvTitle.setTextColor(tvColor);
        }
    }

    // 设置标题内容
    public void setTitleContent(String content) {
        if(null != mTvTitle){
            mTvTitle.setText(content);
        }
    }

    // 获取某个规格总库存
    public int getStocks(ArrayList<DetailBean.DataBeanX.DataBean.SpecData.SpecSizeData> list){
        if(null != fragment){
            return fragment.getStocks(list);
        }
        return 0;
    }

    // 显示广告图片
    public void showAdImg(Bundle bundle){
        if(null != fragment){
            fragment.showAdImg(bundle);
        }
    }

    public int getPosition(){
        return 1;
    }

    @Override
    public void onBackPressed() {
        if (TemplateVideoView.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        TemplateVideoView.goOnPlayOnResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        shareShowHideLoading(View.GONE);
        TemplateVideoView.goOnPlayOnPause();
    }

    /**
     * 设置视频全屏的时候，隐藏底部的导航栏
     * @param b true显示   false隐藏
     */
    public void showHideNavigationBar(boolean b){
        if(null != immersionBar){
            immersionBar.hideBar(b ? BarHide.FLAG_SHOW_BAR : BarHide.FLAG_HIDE_NAVIGATION_BAR);
            immersionBar.init();
        }
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
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        EventBus.getDefault().unregister(this);
        if(null != immersionBar){
            immersionBar.destroy();
        }
        TemplateVideoView.releaseAllVideos();
    }
}
