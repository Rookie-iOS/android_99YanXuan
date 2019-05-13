package com.jjshop.ui.activity.person;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.jjshop.R;
import com.jjshop.base.BaseActivity;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.CitysPickerBean;
import com.jjshop.bean.JJEvent;
import com.jjshop.bean.UploadImgBean;
import com.jjshop.ui.activity.home.HomeActivity;
import com.jjshop.ui.activity.shop.UpdateActivity;
import com.jjshop.ui.presenter.PersonEditPresenter;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.UploadImgUtil;
import com.jjshop.utils.dbutils.DBManager;
import com.jjshop.utils.glide_img.GlideUtil;
import com.qingmei2.rximagepicker.ui.SystemImagePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：张国庆
 * 时间：2018/8/1
 */

public class PersonEditActivity extends BaseActivity<PersonEditPresenter> {
    @BindView(R.id.m_tv_title)
    TextView mTvTitle;
    @BindView(R.id.m_view_loading)
    View mViewLoading;
    @BindView(R.id.m_iv_user_head)
    ImageView mIvUserHead;
    @BindView(R.id.m_tv_user_nickname)
    TextView mTvUserNickname;
    @BindView(R.id.m_tv_user_sex)
    TextView mTvUserSex;
    @BindView(R.id.m_tv_user_birthday)
    TextView mTvUserBirthday;
    @BindView(R.id.m_tv_user_mobile)
    TextView mTvUserMobile;
    @BindView(R.id.m_tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.m_tv_user_city)
    TextView mTvUserCity;

    private String mUpdateContent = "";// 需要传给服务器的值
    private String mHeadImgUrl = "";// 上传图片之后，返回的完整链接
    private String mCityName = "";// 选择城市之后的名称
    private SystemImagePicker imagePicker;
    private TimePickerView mTimePickerView;// 出生年月选择器
    private OptionsPickerView mCityPickerView;// 城市选择器
    public static final int SEX_BAOMI = 1;// 保密
    public static final int SEX_MAN = 2;// 男
    public static final int SEX_WOMAN = 3;// 女

    // 城市数据库
    private DBManager dbManager;

    private CitysPickerBean mCitysPickerBean;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_person_edit_layout;
    }

    @Override
    protected PersonEditPresenter getPresenter() {
        return new PersonEditPresenter(this);
    }

    public static void invoke(Context context) {
        Intent intent = new Intent(context, PersonEditActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        mTvTitle.setText("我的账户");
        imagePicker = UploadImgUtil.build().getSystemImagePicker();
        dbManager = new DBManager(this);
        // 加载本地城市数据
        if(null != HomeActivity.homeInstance){
            mCitysPickerBean = HomeActivity.homeInstance.citysPickerBean();
        }
        if(null != mCitysPickerBean){
            initCityPicker();
            return;
        }
        mViewLoading.setVisibility(View.VISIBLE);
        mPresenter.loadLocalCitysData();
    }

    @Override
    protected void initData() {
        loadHeadImg(PreUtils.getString(this, PreUtils.USER_IMG));
        mTvUserNickname.setText(PreUtils.getString(this, PreUtils.USER_NICKNAME));
        mTvUserSex.setText(PreUtils.getString(this, PreUtils.USER_SEX));
        mTvUserBirthday.setText(PreUtils.getString(this, PreUtils.USER_BIRTHDAY));
        mTvUserMobile.setText(PreUtils.getString(this, PreUtils.USER_MOBILE));
        mTvUserName.setText(PreUtils.getString(this, PreUtils.USER_NAME));
        mTvUserCity.setText(PreUtils.getString(this, PreUtils.USER_CITY));
    }

    @OnClick({R.id.m_title_back, R.id.m_layout_user_head, R.id.m_layout_user_nickname, R.id.m_layout_user_sex, R.id.m_layout_user_birthday,
            R.id.m_layout_user_name, R.id.m_layout_user_city})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_title_back:
                finish();
                break;
            case R.id.m_layout_user_head:// 头像
                UploadImgUtil.build().openDefaultAlbum(imagePicker, this);
                break;
            case R.id.m_layout_user_nickname:// 昵称
                UpdateActivity.invoke(this, UpdateActivity.UPDATE_PERSON_NICKNAME);
                break;
            case R.id.m_layout_user_sex:// 性别
                UpdateActivity.invoke(this, UpdateActivity.UPDATE_PERSON_SEX);
                break;
            case R.id.m_layout_user_birthday:// 出生年月
                if(null == mTimePickerView){
                    mTimePickerView = CommonUtils.build().initTimePicker(this);
                }
                if(null != mTimePickerView && !mTimePickerView.isShowing()){
                    mTimePickerView.show();
                }

                break;
            case R.id.m_layout_user_name:// 真实姓名
                UpdateActivity.invoke(this, UpdateActivity.UPDATE_PERSON_NAME);
                break;
            case R.id.m_layout_user_city:// 城市选择
                if(null != mCityPickerView && !mCityPickerView.isShowing()){
                    mCityPickerView.show();
                }
                break;
        }
    }

    // 获取头像链接
    public void onFileSuccess(UploadImgBean bean) {
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

    // 修改头像成功
    public void onHeadSuccess(BaseBean bean) {
        mViewLoading.setVisibility(View.GONE);
        mUpdateContent = mHeadImgUrl;
        saveUserInfo(PreUtils.USER_IMG);
        loadHeadImg(mHeadImgUrl);
        EventBus.getDefault().post(new JJEvent(JJEvent.UPDATE_LOCAL_PERSON_INFO));
    }

    // 修改昵称成功
    public void onNickNameSuccess(BaseBean bean) {
        mViewLoading.setVisibility(View.GONE);
        saveUserInfo(PreUtils.USER_NICKNAME);
        mTvUserNickname.setText(updateContent());
        EventBus.getDefault().post(new JJEvent(JJEvent.UPDATE_LOCAL_PERSON_INFO));
    }

    // 修改性别成功
    public void onSexSuccess(BaseBean bean) {
        mViewLoading.setVisibility(View.GONE);
        if(!StringUtil.isEmpty(updateContent())){
            if(updateContent().indexOf(String.valueOf(SEX_BAOMI)) != -1){
                mUpdateContent = "保密";
            }else if(updateContent().indexOf(String.valueOf(SEX_MAN)) != -1){
                mUpdateContent = "男";
            }else if(updateContent().indexOf(String.valueOf(SEX_WOMAN)) != -1){
                mUpdateContent = "女";
            }
            saveUserInfo(PreUtils.USER_SEX);
            mTvUserSex.setText(updateContent());
        }
    }

    // 修改出生年月成功
    public void onBirthdaySuccess(BaseBean bean) {
        mViewLoading.setVisibility(View.GONE);
        saveUserInfo(PreUtils.USER_BIRTHDAY);
        mTvUserBirthday.setText(updateContent());
    }

    // 修改姓名成功
    public void onNameSuccess(BaseBean bean) {
        mViewLoading.setVisibility(View.GONE);
        saveUserInfo(PreUtils.USER_NAME);
        mTvUserName.setText(updateContent());
    }

    // 修改城市成功
    public void onCitySuccess(BaseBean bean) {
        mViewLoading.setVisibility(View.GONE);
        mUpdateContent = mCityName;
        saveUserInfo(PreUtils.USER_CITY);
        mTvUserCity.setText(updateContent());
        EventBus.getDefault().post(new JJEvent(JJEvent.UPDATE_LOCAL_PERSON_INFO));
    }

    // 获取本地城市数据成功
    public void onLocalCitySuccess(CitysPickerBean bean) {
        mViewLoading.setVisibility(View.GONE);
        mCitysPickerBean = bean;
        initCityPicker();
    }

    public void onFail(String msg) {
        JjToast.getInstance().toast(msg);
        UploadImgUtil.build().deletePicTempFile();
        mViewLoading.setVisibility(View.GONE);
    }

    // 初始化城市选择器
    private void initCityPicker(){
        if(null == mCitysPickerBean){
            return;
        }
        mCityPickerView = CommonUtils.build().initCityPicker(this, mCitysPickerBean);
    }

    public String updateContent(){
        if(StringUtil.isEmpty(mUpdateContent)){
           mUpdateContent = "";
        }
        return mUpdateContent;
    }

    public DBManager dbManager(){
        return dbManager;
    }



    private void saveUserInfo(String type){
        PreUtils.setString(this, type, updateContent());
    }

    // 选择成功完成
    public void selectCity(String city1, String city2, String city3, int cityId1, int citydId2, int cityId3){
        mCityName = city1 + city2 + city3;
        mUpdateContent = cityId1 + "," + citydId2 + "," + cityId3;
        mViewLoading.setVisibility(View.VISIBLE);
        mPresenter.loadCitysData();
    }

    // 选择时间完成
    public void selectTime(Date date){
        mUpdateContent = StringUtil.dateToString(date);
        mViewLoading.setVisibility(View.VISIBLE);
        mPresenter.loadDataBirthday();
    }

    private void loadHeadImg(String imgUrl){
        GlideUtil.getInstence().loadCirleImage(this, mIvUserHead, imgUrl,
                R.mipmap.img_person_default_head);
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
                if (null == file || !file.exists()) {
                    return;
                }
                mViewLoading.setVisibility(View.VISIBLE);
                mPresenter.loadDataFile(file);
                break;
        }
    }


    @Subscribe
    public void onEvent(JJEvent event) {
        if (null == event) {
            return;
        }
        int id = event.getEventId();
        mUpdateContent = event.getEventMessage();
        switch (id) {
            case JJEvent.UPDATE_PERSON_NICKNAME:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(mUpdateContent)) {
                            mViewLoading.setVisibility(View.VISIBLE);
                            mPresenter.loadDataNickName();
                        }
                    }
                });
                break;
            case JJEvent.UPDATE_PERSON_SEX:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(mUpdateContent)) {
                            mViewLoading.setVisibility(View.VISIBLE);
                            mPresenter.loadDataSex();
                        }
                    }
                });
                break;
            case JJEvent.UPDATE_PERSON_NAME:
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
            case JJEvent.FINISH_ACTIVITY:
                finish();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        imagePicker = null;
        if(null != mTimePickerView){
            mTimePickerView.dismiss();
        }
        if(null != dbManager){
            dbManager.closeDB();
        }
        EventBus.getDefault().unregister(this);
    }


}
