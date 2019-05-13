package com.jjshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jjshop.R;
import com.jjshop.bean.JJHomeBean;
import com.jjshop.ui.WebViewActivity;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.glide_img.GlideUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/12.
 */
public class NewsPicAdapter extends PagerAdapter {
    private Context context;
    private List<JJHomeBean.DataBean.IndexDataBean.CarouselBean> focusBeanList;
    private LayoutInflater listContainer;//视图容器
    private ListItemView listItemView;
    private LinkedList<View> mCacheViews = null;

    public NewsPicAdapter(Context context, List<JJHomeBean.DataBean.IndexDataBean.CarouselBean> focusBeanList) {
        this.context = context;
        this.focusBeanList = focusBeanList;
        listContainer = LayoutInflater.from(this.context);
        mCacheViews = new LinkedList<>();
    }

    @Override
    public int getCount() {
        // 设置成最大，使用户看不到边界
        return Integer.MAX_VALUE;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        position %= focusBeanList.size();
        JJHomeBean.DataBean.IndexDataBean.CarouselBean nominate = focusBeanList.get(position);
        View view = null;
        if (mCacheViews.size() <= 0) {
            view = listContainer.inflate(R.layout.banner_item, null);
            listItemView = new ListItemView();
            listItemView.iv = (ImageView) view.findViewById(R.id.image);
            view.setTag(listItemView);
        } else {
            view = mCacheViews.removeFirst();
            listItemView = (ListItemView) view.getTag();
        }

        GlideUtil.getInstence().loadImage(context, listItemView.iv, nominate.getImg());
        listItemView.iv.setTag(position);
        listItemView.iv.setOnClickListener(new click());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
        mCacheViews.add(view);

    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {

    }

    private class click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            JJHomeBean.DataBean.IndexDataBean.CarouselBean focusBean = focusBeanList.get(position);
            if (null != context && null != focusBean) {
                String url = focusBean.getImg_url();
                if (!StringUtil.isEmpty(url)) {
                     WebViewActivity.invoke(context, url);
                }

            }
        }

    }

    static class ListItemView {
        private ImageView iv;
    }
}