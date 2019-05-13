package com.jjshop.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjshop.base.BaseRecyclerViewHolder;
import com.jjshop.bean.CartDataBean;
import com.jjshop.bean.MyOrderBottomBean;
import com.jjshop.bean.MyOrderProductList;
import com.jjshop.bean.MyOrderTopBean;
import com.jjshop.bean.ProductListBean;
import com.jjshop.bean.WuliuInfoBeanExpress;
import com.jjshop.bean.WuliuTopBean;
import com.jjshop.listener.ProductLoadMoreCallBack;
import com.jjshop.template_view.BaseTemplateIm;
import com.jjshop.template_view.TemplateUtil;
import com.jjshop.utils.JjLog;

import java.util.ArrayList;

public class CommentRecycleAdapter extends RecyclerView.Adapter<CommentRecycleAdapter.MyHolder>{

    private Context mContext;
    private ArrayList<Object> datas;
    private int type;
    private LayoutInflater inflater;
    public ProductLoadMoreCallBack productLoadMoreCallBack;

    public CommentRecycleAdapter(Context context, ArrayList<Object> datas) {
        this.mContext = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }
    public CommentRecycleAdapter(Context context, ArrayList<Object> datas, int type) {
        this.mContext = context;
        this.datas = datas;
        this.type = type;
        inflater = LayoutInflater.from(context);
    }
    public CommentRecycleAdapter(Context context, ArrayList<Object> datas, ProductLoadMoreCallBack productLoadMoreCallBack) {
        this.mContext = context;
        this.datas = datas;
        this.productLoadMoreCallBack = productLoadMoreCallBack;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if(null == datas || datas.isEmpty()){
            return position;
        }
        if(type >= 1000){
            return type;
        }
        Object o = datas.get(position);
        return TemplateUtil.build().getTemplateId(o);
    }

    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int itemViewType) {
        View contentView = TemplateUtil.build().getContentView(itemViewType, mContext, inflater);
        if(null == contentView){
            contentView = new View(mContext);
        }
        MyHolder holder = new MyHolder(contentView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final Object o = datas.get(position);
        if(null == o){
            return;
        }
        if(null == holder){
            return;
        }
        if(null == holder.itemView){
            return;
        }
        if(holder.itemView instanceof BaseTemplateIm){
            ((BaseTemplateIm)holder.itemView).getDate(o, null);
        }
        if(null != productLoadMoreCallBack){
            productLoadMoreCallBack.loadMore(position);
        }
    }

    @Override
    public int getItemCount() {
        return null == datas ? 0 : datas.size();
    }

    public class MyHolder extends BaseRecyclerViewHolder{
        public MyHolder(View itemView) {
            super(itemView);
        }
    }

}
