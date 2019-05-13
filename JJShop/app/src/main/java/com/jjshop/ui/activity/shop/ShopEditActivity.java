package com.jjshop.ui.activity.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.base.BaseActivity;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.JJEvent;
import com.jjshop.bean.UploadImgBean;
import com.jjshop.ui.presenter.ShopEditPresenter;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.UploadImgUtil;
import com.jjshop.utils.glide_img.GlideUtil;
import com.qingmei2.rximagepicker.ui.SystemImagePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：张国庆
 * 时间：2018/8/1
 */

public class ShopEditActivity extends BaseActivity<ShopEditPresenter> {
    @BindView(R.id.m_tv_title)
    TextView mTvTitle;
    @BindView(R.id.m_tv_shop_name)
    TextView mTvShopName;
    @BindView(R.id.m_tv_shop_info)
    TextView mTvShopInfo;
    @BindView(R.id.m_iv_shop_head)
    ImageView mIvShopHead;
    @BindView(R.id.m_view_loading)
    View mViewLoading;

    private String mUpdateContent = "";// 需要上传给服务器的值
    private String mHeadImgUrl = "";// 上传图片服务器返回的完成的图片链接
    private SystemImagePicker imagePicker;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_shop_edit_layout;
    }

    @Override
    protected ShopEditPresenter getPresenter() {
        return new ShopEditPresenter(this);
    }

    public static void invoke(Context context){
        Intent intent = new Intent(context, ShopEditActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        mTvTitle.setText("店铺信息");
        imagePicker = UploadImgUtil.build().getSystemImagePicker();
    }

    @Override
    protected void initData() {
        mTvShopName.setText(PreUtils.getString(this, PreUtils.SHOP_NAME, ""));
        mTvShopInfo.setText(PreUtils.getString(this, PreUtils.SHOP_INFO, ""));
        GlideUtil.getInstence().loadCirleImage(this, mIvShopHead,
                PreUtils.getString(this, PreUtils.SHOP_LOGO, ""), R.mipmap.img_shop_default_head);
    }

    @OnClick({R.id.m_title_back, R.id.m_layout_shop_name, R.id.m_layout_shop_info, R.id.m_layout_shop_head})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_title_back:
                finish();
                break;
            case R.id.m_layout_shop_name:
                UpdateActivity.invoke(this, UpdateActivity.UPDATE_SHOPNAME);
                break;
            case R.id.m_layout_shop_info:
                UpdateActivity.invoke(this, UpdateActivity.UPDATE_SHOPINFO);
                break;
            case R.id.m_layout_shop_head:
                UploadImgUtil.build().openDefaultAlbum(imagePicker, this);
                break;
        }
    }

    public void onFileSuccess(UploadImgBean bean){
        mViewLoading.setVisibility(View.GONE);
        UploadImgUtil.build().deletePicTempFile();
        ArrayList<UploadImgBean.UploadImgData> list = bean.getData();
        if(null != list && list.size() >= 1){
            UploadImgBean.UploadImgData imgData = list.get(0);
            if(null != imgData){
                mUpdateContent = imgData.getPath();
                mHeadImgUrl = imgData.getUrl();
                mViewLoading.setVisibility(View.VISIBLE);
                mPresenter.loadDataHead();
            }
        }
    }

    public void onHeadSuccess(BaseBean bean){
        PreUtils.setString(this, PreUtils.SHOP_LOGO, mHeadImgUrl);
        mViewLoading.setVisibility(View.GONE);
        GlideUtil.getInstence().loadCirleImage(this, mIvShopHead, mHeadImgUrl, R.mipmap.img_shop_default_head);
        EventBus.getDefault().post(new JJEvent(JJEvent.UPDATE_LOCAL_SHOP_INFO));
    }

    public void onNameSuccess(BaseBean bean){
        PreUtils.setString(this, PreUtils.SHOP_NAME, mUpdateContent);
        mTvShopName.setText(mUpdateContent);
        EventBus.getDefault().post(new JJEvent(JJEvent.UPDATE_LOCAL_SHOP_INFO));
        mViewLoading.setVisibility(View.GONE);
    }

    public void onInfoSuccess(BaseBean bean){
        PreUtils.setString(this, PreUtils.SHOP_INFO, mUpdateContent);
        mTvShopInfo.setText(mUpdateContent);
        EventBus.getDefault().post(new JJEvent(JJEvent.UPDATE_LOCAL_SHOP_INFO));
        mViewLoading.setVisibility(View.GONE);
    }

    public void onFail(String msg){
        UploadImgUtil.build().deletePicTempFile();
        mViewLoading.setVisibility(View.GONE);
    }

    public String updateContent(){
        if(StringUtil.isEmpty(mUpdateContent)){
            mUpdateContent = "";
        }
        return mUpdateContent;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case UploadImgUtil.CROP_REQUEST_CODE://裁剪后
                File file = UploadImgUtil.build().getPicTempFile();
                if(null == file || !file.exists()){
                    return;
                }
                mViewLoading.setVisibility(View.VISIBLE);
                mPresenter.loadDataFile(file);
                break;
        }
    }
    @Subscribe
    public void onEvent(JJEvent event){
        if(null == event){
            return;
        }
        int id = event.getEventId();
        mUpdateContent = event.getEventMessage();
        switch (id){
            case JJEvent.UPDATE_SHOP_NAME:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(mUpdateContent)) {
                            mViewLoading.setVisibility(View.VISIBLE);
                            mPresenter.loadDataName();
                        }
                    }
                });
                break;
            case JJEvent.UPDATE_SHOP_INFO:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(mUpdateContent)) {
                            mViewLoading.setVisibility(View.VISIBLE);
                            mPresenter.loadDataInfo();
                        }
                    }
                });
                break;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        imagePicker = null;
        EventBus.getDefault().unregister(this);
    }
}
