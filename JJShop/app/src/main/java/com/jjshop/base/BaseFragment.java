package com.jjshop.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：张国庆
 * 时间：2018/7/12
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements IView{
    protected P mPresenter;
    private Unbinder unbinder;
    protected Activity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        mPresenter = getPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(setLayout(), null);
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    protected abstract P getPresenter();

    public abstract int setLayout();

    public abstract void initView(View view);

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != unbinder){
            unbinder.unbind();
        }
        if(null != mPresenter){
            mPresenter.clearData();
        }
    }
}
