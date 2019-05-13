package com.jjshop.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jjshop.bean.WuliuInfoBeanExpress;
import com.jjshop.bean.WuliuTopBean;
import com.jjshop.template_view.BaseTemplateIm;
import com.jjshop.template_view.TemplateUtil;

import java.util.ArrayList;

public class CommentListAdapter extends BaseAdapter  {
    private ArrayList<Object> mDatas;
    private Context context;
    private int type = -1;
    private int mNextType = 1;
    private SparseIntArray intArray;
    private LayoutInflater inflater;

    public CommentListAdapter(Context context, ArrayList<Object> datas, int type) {
        this.context = context;
        this.mDatas = datas;
        this.type = type;
        inflater = LayoutInflater.from(context);
        intArray = new SparseIntArray();
    }
    public CommentListAdapter(Context context, ArrayList<Object> datas) {
        this.context = context;
        this.mDatas = datas;
        inflater = LayoutInflater.from(context);
        intArray = new SparseIntArray();
    }

    @Override
    public int getCount() {
        return null != mDatas ? mDatas.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if(null == mDatas || mDatas.isEmpty()){
            return 0;
        }
        if(type >= 1000){

            return getViewType(type);
        }
        Object o = mDatas.get(position);
        return getViewType(TemplateUtil.build().getTemplateId(o));
    }

    @Override
    public int getViewTypeCount() {
        return 13;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup) {
        Object o = mDatas.get(position);
//        if(this.type < 1000){
//            type = TemplateUtil.build().getTemplateId(o);
//        }
        if(this.type >= 1000){
            convertview = getTemplateView(type, convertview);
        }else{
            convertview = getTemplateView(TemplateUtil.build().getTemplateId(o), convertview);
        }

        convertview = getTemplateView(type, convertview);
        if(null == convertview){
            convertview = new View(context);
        }
        // 刷新数据
        if(convertview instanceof BaseTemplateIm){
            ((BaseTemplateIm) convertview).getDate(o, null);
        }
        return convertview;
    }


    private View getTemplateView(int itemViewType, View convertview){
        if(null != convertview && convertview.getId() == -1) {
            convertview = null;
        }
        if(null == convertview){
            convertview = TemplateUtil.build().getContentView(itemViewType, context, inflater);
        }
        return convertview;
    }

    private int getViewType(int key){
        int resultType = intArray.get(key);
        if (resultType <= 0){
            resultType = mNextType++;
            intArray.put(key,resultType);
        }
        return resultType;
    }
}
