package com.jjshop.ui.model;

import android.content.Context;
import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.SearchBean;
import com.jjshop.ui.presenter.SearchPresenter;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.Tools;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

import java.util.List;

/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class SearchModel extends BaseModel {
    private SearchPresenter presenter;
    private final String HISTORY_SPLIT = "§";

    public SearchModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (SearchPresenter) basePresenter;
    }

    public void loadData(String content, int page){
        HttpHelper.bulid().getRequest(HttpUrl.build().getSearch(content, page),
                SearchBean.class, new OnRequestCallBack<SearchBean>() {
                    @Override
                    public void onSuccess(SearchBean data) {
                        presenter.onSuccess(data);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onFail(msg);
                    }
                });
    }

    /**
     * 获取搜索关键字历史数据
     * @param context
     */
    public void loadHistoryData(Context context){
        if(null == context){
            return;
        }
        String history = PreUtils.getString(context, PreUtils.SEARCH_HISTORY);
        List<String> list = Tools.getSplit(history, HISTORY_SPLIT);
        presenter.onHistorySuccess(list);
    }

    /**
     * 保存搜索关键字、刷新本地数据
     * @param context
     * @param mSearchContent
     * @param myTagList
     */
    public void saveSearchContent(Context context, String mSearchContent, List<String> myTagList){
        if (null == context || StringUtil.isEmpty(mSearchContent) || null == myTagList) {
            return;
        }
        if(myTagList.size() >= 1 && myTagList.get(0).equals(mSearchContent)){
            return;
        }
        String history = PreUtils.getString(context, PreUtils.SEARCH_HISTORY);
        boolean isHas = false;
        if (StringUtil.isEmpty(history)) {
            PreUtils.setString(context, PreUtils.SEARCH_HISTORY, mSearchContent);
            myTagList.add(mSearchContent);
        } else {
            if(!myTagList.get(0).equals(mSearchContent)){//
                for(String string : myTagList){// 先删除重复的
                    if(string.equals(mSearchContent)){
                        myTagList.remove(string);
                        isHas = true;
                        break;
                    }
                }
                int size = myTagList.size();
                if(size >= 20){// 超过20个时，删除最老的一个
                    myTagList.remove(size - 1);
                    isHas = true;
                }
                String newHistory = "";
                myTagList.add(0, mSearchContent);
                if(isHas){// 之前有着个关键字重新存储所有的
                    for(String string : myTagList){
                        if(!StringUtil.isEmpty(string)){
                            if(StringUtil.isEmpty(newHistory)){
                                newHistory = string;
                            }else{
                                newHistory = newHistory + HISTORY_SPLIT + string;
                            }
                        }
                    }
                }else{
                    newHistory = mSearchContent + HISTORY_SPLIT + history;
                }
                PreUtils.setString(context, PreUtils.SEARCH_HISTORY, newHistory);
            }
        }
    }

}
