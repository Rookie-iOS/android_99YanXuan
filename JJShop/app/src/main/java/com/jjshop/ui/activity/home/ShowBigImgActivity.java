package com.jjshop.ui.activity.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.base.BaseActivity;
import com.jjshop.base.BasePresenter;
import com.jjshop.dialog.SaveImgDialog;
import com.jjshop.template_view.TemplateBannerView;
import com.jjshop.utils.UIUtils;
import com.jjshop.utils.glide_img.GlideUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：张国庆
 * 时间：2018/8/30
 */

public class ShowBigImgActivity extends BaseActivity {
    @BindView(R.id.m_viewpager)
    LinearLayout mViewpager;
    @BindView(R.id.m_iv_big_img)
    ImageView mIvBigImg;
    @BindView(R.id.m_tv_curr_num)
    TextView mTvCurrNum;
    @BindView(R.id.m_tv_all_num)
    TextView mTvAllNum;
    @BindView(R.id.m_iv_close)
    ImageView mIvClose;

    private ArrayList<String> mListImgs;
    private int mPosition;

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_show_big_img_layout;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    public static void invoke(Context context, String img){
        ArrayList<String> list = new ArrayList<>();
        list.add(img);
        invoke(context, list, 0);
    }

    public static void invoke(Context context, ArrayList<String> list, int position){
        Intent intent = new Intent(context, ShowBigImgActivity.class);
        intent.putStringArrayListExtra("list", list);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {
        if(null != getIntent()){
            mListImgs = getIntent().getStringArrayListExtra("list");
            mPosition = getIntent().getIntExtra("position", 0);
        }

        if(null == mListImgs || mListImgs.size() <= 0){
            return;
        }
        mTvCurrNum.setText(String.valueOf(mPosition + 1));
        mTvAllNum.setText("/" + mListImgs.size());
        // 一张图片的时候直接显示
        if(mListImgs.size() == 1){
            mIvBigImg.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIvBigImg.getLayoutParams();
            params.width = UIUtils.getWidth();
            params.height = params.width;
            mIvBigImg.setLayoutParams(params);
            GlideUtil.getInstence().loadImage(this, mIvBigImg, mListImgs.get(0));
            mIvBigImg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    SaveImgDialog.build().showView(getSupportFragmentManager(), mListImgs.get(0));
                    return true;
                }
            });
            return;
        }
        // 多张图片
        mViewpager.setVisibility(View.VISIBLE);
        mViewpager.removeAllViews();
        View view = LayoutInflater.from(this).inflate(R.layout.template_banner_view_layout, null);
        mViewpager.addView(view);
        if(null != view && view instanceof TemplateBannerView){
            ((TemplateBannerView)view).getDate(mListImgs, "", null);
            ((TemplateBannerView)view).setCurrentItem(mPosition);
        }
    }

    @OnClick({R.id.m_iv_big_img, R.id.m_iv_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_iv_big_img:
            case R.id.m_iv_close:
                finish();
                break;
        }
    }

    public void setCurrNum(int position){
        if(null == mTvCurrNum){
            return;
        }
        mTvCurrNum.setText(String.valueOf(position + 1));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mViewpager){
            mViewpager.removeAllViews();
            mViewpager = null;
        }
        if(null != mListImgs){
            mListImgs.clear();
            mListImgs = null;
        }
    }
}
