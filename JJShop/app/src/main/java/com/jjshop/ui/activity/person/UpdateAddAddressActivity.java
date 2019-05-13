package com.jjshop.ui.activity.person;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.view.OptionsPickerView;
import com.jjshop.R;
import com.jjshop.base.BaseActivity;
import com.jjshop.bean.AddressListBean;
import com.jjshop.bean.CitysPickerBean;
import com.jjshop.bean.JJEvent;
import com.jjshop.ui.activity.home.HomeActivity;
import com.jjshop.ui.presenter.UpdateAddressPresenter;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.Tools;
import com.jjshop.utils.dbutils.DBManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：张国庆
 * 时间：2018/8/17
 */

public class UpdateAddAddressActivity extends BaseActivity<UpdateAddressPresenter> {

    @BindView(R.id.m_tv_title)
    TextView mTvTitle;
    @BindView(R.id.m_edit_name)
    EditText mEditName;
    @BindView(R.id.m_edit_mobile)
    EditText mEditMobile;
    @BindView(R.id.m_tv_city)
    TextView mTvCity;
    @BindView(R.id.m_edit_address)
    EditText mEditAddress;
    @BindView(R.id.m_view_loading)
    View mViewLoading;
    @BindView(R.id.m_iv_default)
    ImageView mIvDefault;

    private AddressListBean.AddressList mAddressBean;
    private int mInvokeType;

    // 城市数据库
    private DBManager dbManager;
    private CitysPickerBean mCitysPickerBean;
    private OptionsPickerView mCityPickerView;// 城市选择器
    private String mCityIds = "";// 最终选择的城市id
    private int mIsDeault = 2;// 设置为默认
    private int mUpdateId = 0;// 修改地址的id
    private int mAddressSize;// 地址个数


    @Override
    protected int setLayoutId() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return R.layout.activity_update_add_address_layout;
    }

    @Override
    protected UpdateAddressPresenter getPresenter() {
        return new UpdateAddressPresenter(this);
    }

    public static void invoke(Context context, int type, int addressSize) {
        invoke(context, null, type, addressSize);
    }

    public static void invoke(Context context, AddressListBean.AddressList bean, int type, int addressSize) {
        Intent intent = new Intent(context, UpdateAddAddressActivity.class);
        intent.putExtra("invokeType", type);
        intent.putExtra("updateData", bean);
        intent.putExtra("addressSize", addressSize);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        if (null != getIntent()) {
            mAddressBean = (AddressListBean.AddressList) getIntent().getSerializableExtra("updateData");
            mInvokeType = getIntent().getIntExtra("invokeType", -1);
            mAddressSize = getIntent().getIntExtra("addressSize", 0);
        }
        if (null != mAddressBean) {
            mTvTitle.setText("修改地址");
            String acceptName = mAddressBean.getAccept_name();
            if(!StringUtil.isEmpty(acceptName)){
                acceptName = acceptName.replace(" ", "").toString();
                mEditName.setText(acceptName);
                mEditName.setSelection(acceptName.length() >= 8 ? 8 : acceptName.length());
            }
            mEditMobile.setText(mAddressBean.getMobile());
            mTvCity.setText(mAddressBean.getProvince_name() + mAddressBean.getCity_name() + mAddressBean.getArea_name());
            mCityIds = mAddressBean.getProvince() + "," + mAddressBean.getCity() + "," + mAddressBean.getArea();
            mEditAddress.setText(mAddressBean.getAddress());
            mIsDeault = mAddressBean.getIs_default();
            if(mIsDeault == 2){
                mIvDefault.setImageResource(R.mipmap.img_cart_check_true);
            }else{
                mIvDefault.setImageResource(R.mipmap.img_cart_check_false);
            }
            mUpdateId = mAddressBean.getId();
        }else{
            mTvTitle.setText("新增地址");
        }
    }

    @Override
    protected void initData() {
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
        mPresenter.loadLocalCityData();
    }

    @OnClick({R.id.m_title_back, R.id.m_tv_city, R.id.m_layout_default, R.id.m_tv_save_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_title_back:
                Tools.hideSoftInput(view);
                finish();
                break;
            case R.id.m_tv_city:
                Tools.hideSoftInput(view);
                if(null != mCityPickerView && !mCityPickerView.isShowing()){
                    mCityPickerView.show();
                }
                break;
            case R.id.m_layout_default:
                mIsDeault = mIsDeault == 2 ? 1 : 2;
                if(mIsDeault == 2){
                    mIvDefault.setImageResource(R.mipmap.img_cart_check_true);
                    return;
                }
                mIvDefault.setImageResource(R.mipmap.img_cart_check_false);
                break;
            case R.id.m_tv_save_address:
                Tools.hideSoftInput(view);
                mViewLoading.setVisibility(View.VISIBLE);
                if(null == mAddressBean){
                    mPresenter.loadAddData();
                    return;
                }
                mPresenter.loadUpdateData();
                break;
        }
    }

    public void onSuccess(int type){
        mViewLoading.setVisibility(View.GONE);
        EventBus.getDefault().post(new JJEvent(JJEvent.REFRESH_ADDRESS));
        if((type != -1 && mInvokeType == MyAddressActivity.INVOKE_SUBMIT_ORDER) || mAddressSize == 0){// 修改地址时
            // 刷新确认订单的地址信息
            EventBus.getDefault().post(new JJEvent(JJEvent.SUBMIT_ORDER_REFRESH_ADDRESS, addressId()));
        }
        finish();
    }

    public void onLocalCitySuccess(CitysPickerBean bean){
        mViewLoading.setVisibility(View.GONE);
        mCitysPickerBean = bean;
        initCityPicker();
    }

    public void onFail(String msg){
        mViewLoading.setVisibility(View.GONE);
        JjToast.getInstance().toast(msg);
    }

    // 初始化城市选择器
    private void initCityPicker(){
        mCityPickerView = CommonUtils.build().initCityPicker(this, mCitysPickerBean);
    }

    public DBManager dbManager(){
        return dbManager;
    }

    public String name(){
        if(null == mEditName){
            return "";
        }
        String name = mEditName.getText().toString();
        if(StringUtil.isEmpty(name)){
            return "";
        }
        return name;
    }

    public String mobile(){
        if(null == mEditMobile){
            return "";
        }
        String mobile = mEditMobile.getText().toString();
        if(StringUtil.isEmpty(mobile)){
            return "";
        }
        return mobile;
    }

    public String cityIds(){
        return mCityIds;
    }

    public String address(){
        if(null == mEditAddress){
            return "";
        }
        String address = mEditAddress.getText().toString();
        if(StringUtil.isEmpty(address)){
            return "";
        }
        return address;
    }

    public int isDefault(){
        return mIsDeault;
    }

    public int addressId(){
        return mUpdateId;
    }

    // 选择成功完成
    public void selectCity(String city1, String city2, String city3, int cityId1, int citydId2, int cityId3){
        mTvCity.setText(city1 + city2 + city3);
        mCityIds = cityId1 + "," + citydId2 + "," + cityId3;
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
        EventBus.getDefault().unregister(this);
        if(null != dbManager){
            dbManager.closeDB();
        }
    }
}
