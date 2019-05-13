package com.jjshop.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by ym on 2015/12/9.
 */
public class TabPageIndicatorAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments; // 每个Fragment对应一个Page


    public TabPageIndicatorAdapter(FragmentManager fragmentManager, List<Fragment> fragments) {
        super(fragmentManager);
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return null == fragments || fragments. isEmpty() ? 0 : fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

}
