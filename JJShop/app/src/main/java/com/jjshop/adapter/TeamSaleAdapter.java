package com.jjshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjshop.R;

import java.util.List;

public class TeamSaleAdapter extends BaseAdapter {

    private List<String> mData;
    private LayoutInflater mInflater;
    public TeamSaleAdapter(Context context,List<String> datas){
        mInflater = LayoutInflater.from(context);
        mData = datas;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if (view == null){
            view = mInflater.inflate(R.layout.team_sale_item_layout,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.headimg = view.findViewById(R.id.head_img);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        // 绑定数据

        return view;
    }

    private class ViewHolder {

        TextView name;
        TextView shopInfo;
        ImageView headimg;
        TextView loginTime;

    }
}
