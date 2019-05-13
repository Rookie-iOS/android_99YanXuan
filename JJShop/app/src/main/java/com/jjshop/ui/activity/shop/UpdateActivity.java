package com.jjshop.ui.activity.shop;

import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.base.BaseActivity;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.JJEvent;
import com.jjshop.ui.activity.person.PersonEditActivity;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.Tools;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：张国庆
 * 时间：2018/8/1
 */

public class UpdateActivity extends BaseActivity {
    @BindView(R.id.m_tv_title)
    TextView mTvTitle;
    @BindView(R.id.m_title_right_tv)
    TextView mTvRight;
    @BindView(R.id.m_et_content)
    EditText mEditText;
    @BindView(R.id.m_tv_tishi)
    TextView mTvTishi;
    @BindView(R.id.m_layout_one)
    RelativeLayout mLayoutOne;
    @BindView(R.id.m_layout_two)
    RelativeLayout mLayoutTwo;
    @BindView(R.id.m_layout_three)
    RelativeLayout mLayoutThree;
    @BindView(R.id.view_line_one)
    View mViewLineOne;
    @BindView(R.id.view_line_two)
    View mViewLineTwo;
    @BindView(R.id.m_iv_one)
    ImageView mIvOne;
    @BindView(R.id.m_iv_two)
    ImageView mIvTwo;
    @BindView(R.id.m_iv_three)
    ImageView mIvThree;

    private int mUpdateType;
    public static final int UPDATE_SHOPNAME = 100;// 修改店铺名称
    public static final int UPDATE_SHOPINFO = 101;// 修改店铺信息
    public static final int UPDATE_PERSON_NICKNAME = 102;// 修改用户昵称
    public static final int UPDATE_PERSON_SEX = 103;// 修改用户性别
    public static final int UPDATE_PERSON_NAME = 104;// 修改用户名称

    private int mSexType = -1;

    private String mTitleContent = "", mEditHint = "", mContent = "";
    private int mEditLines, mEventId, mMaxLength;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_update_layout;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    public static void invoke(Context context, int type){
        Intent intent = new Intent(context, UpdateActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        if(null == getIntent()){
            JjToast.getInstance().toast("没有传值");
            return;
        }
        mUpdateType = getIntent().getIntExtra("type", 0);
        if(mUpdateType != UPDATE_SHOPNAME && mUpdateType != UPDATE_SHOPINFO && mUpdateType != UPDATE_PERSON_NICKNAME
                && mUpdateType != UPDATE_PERSON_SEX && mUpdateType != UPDATE_PERSON_NAME){
            JjToast.getInstance().toast("传入的类型不合法");
            return;
        }
        getIntentType();
        // 标题
        mTvTitle.setText(mTitleContent);
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText("确定");
        if(mEditText.getVisibility() == View.GONE){
            return;
        }
        // 行数
        mEditText.setLines(mEditLines);
        // 提示
        mEditText.setHint(mEditHint);
        // 内容
        mEditText.setText(mContent);
        if(!StringUtil.isEmpty(mContent)){
            // 输入框光标居于末尾
            mEditText.setSelection(mContent.length());
            mTvTishi.setText(mEditHint);
        }
        //最大输入长度
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxLength)});

    }

    @OnClick({R.id.m_title_back, R.id.m_title_right_tv, R.id.m_layout_one, R.id.m_layout_two, R.id.m_layout_three})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_title_back:
                if(null != mEditText && mEditText.getVisibility() == View.VISIBLE){
                    Tools.hideSoftInput(view);
                }
                finish();
                break;
            case R.id.m_layout_one:// 男
                mSexType = PersonEditActivity.SEX_MAN;
                mIvOne.setVisibility(View.VISIBLE);
                mIvTwo.setVisibility(View.GONE);
                mIvThree.setVisibility(View.GONE);
                break;
            case R.id.m_layout_two:// 女
                mSexType = PersonEditActivity.SEX_WOMAN;
                mIvOne.setVisibility(View.GONE);
                mIvTwo.setVisibility(View.VISIBLE);
                mIvThree.setVisibility(View.GONE);
                break;
            case R.id.m_layout_three:// 保密
                mSexType = PersonEditActivity.SEX_BAOMI;
                mIvOne.setVisibility(View.GONE);
                mIvTwo.setVisibility(View.GONE);
                mIvThree.setVisibility(View.VISIBLE);
                break;
            case R.id.m_title_right_tv:
                if(mUpdateType == UPDATE_PERSON_SEX && mSexType != -1){
                    mEditText.setText(String.valueOf(mSexType));
                }
                if(StringUtil.isEmpty(content())){
                    JjToast.getInstance().toast("修改内容不能为空");
                    return;
                }
                Tools.hideSoftInput(view);
                EventBus.getDefault().post(new JJEvent(mEventId, content()));
                finish();
                break;
        }
    }

    public String content(){
        if(null == mEditText ||StringUtil.isEmpty(mEditText.getText().toString())){
            return "";
        }
        return mEditText.getText().toString();
    }

    /**
     * 根据传的类型不同
     */
    private void getIntentType(){
        switch (mUpdateType){
            case UPDATE_SHOPNAME:
                setEditText("修改店铺名称", PreUtils.getString(this, PreUtils.SHOP_NAME),
                        "请输入店铺名称,12个字符以内", 1, JJEvent.UPDATE_SHOP_NAME, 12);
                break;
            case UPDATE_SHOPINFO:
                setEditText("修改店铺简介", PreUtils.getString(this, PreUtils.SHOP_INFO),
                        "请输入店铺名称,20个字符以内", 3, JJEvent.UPDATE_SHOP_INFO, 20);
                break;
            case UPDATE_PERSON_NICKNAME:
                setEditText("修改昵称", PreUtils.getString(this, PreUtils.USER_NICKNAME),
                        "请输入昵称,10个字符以内", 1, JJEvent.UPDATE_PERSON_NICKNAME, 10);
                break;
            case UPDATE_PERSON_SEX:
                mEditText.setVisibility(View.GONE);
                mLayoutOne.setVisibility(View.VISIBLE);
                mLayoutTwo.setVisibility(View.VISIBLE);
                mLayoutThree.setVisibility(View.VISIBLE);
                mViewLineOne.setVisibility(View.VISIBLE);
                mViewLineTwo.setVisibility(View.VISIBLE);
                String sex = PreUtils.getString(this, PreUtils.USER_SEX);
                if(sex.indexOf("保密") != -1){
                    mIvThree.setVisibility(View.VISIBLE);
                    mSexType = PersonEditActivity.SEX_BAOMI;
                }else if(sex.indexOf("男") != -1){
                    mIvOne.setVisibility(View.VISIBLE);
                    mSexType = PersonEditActivity.SEX_MAN;
                }else if(sex.indexOf("女") != -1){
                    mIvTwo.setVisibility(View.VISIBLE);
                    mSexType = PersonEditActivity.SEX_WOMAN;
                }
                setEditText("修改性别", sex,
                        "请输入昵称,10个字符以内", 1, JJEvent.UPDATE_PERSON_SEX, 10);
                break;
            case UPDATE_PERSON_NAME:
                setEditText("修改真实姓名", PreUtils.getString(this, PreUtils.USER_NAME),
                        "请输入真实姓名,10个字符以内", 1, JJEvent.UPDATE_PERSON_NAME, 10);
                break;
        }
    }

    private void setEditText(String title, String etContent, String hintContent, int lines, int eventId, int etLength){
        mTitleContent = title;
        mContent = etContent;
        mEditHint = hintContent;
        mEditLines = lines;
        mEventId = eventId;
        mMaxLength = etLength;
    }
}
