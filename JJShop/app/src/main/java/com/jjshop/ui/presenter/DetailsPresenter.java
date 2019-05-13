package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.AddCartBean;
import com.jjshop.bean.DetailBean;
import com.jjshop.ui.fragment.GoodsInfoFragment;
import com.jjshop.ui.model.DetailsModel;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.PreUtils;

/**
 * 作者：张国庆
 * 时间：2018/7/20
 */

public class DetailsPresenter extends BasePresenter<GoodsInfoFragment> {
    private DetailsModel model;
    public DetailsPresenter(IView iView) {
        super(iView);
        model = (DetailsModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new DetailsModel(this);
    }

    public void loadData(){
        GoodsInfoFragment view = getIView();
        if(null != view){
            model.loadData(view.idCode(), PreUtils.getString(view.getActivity(), PreUtils.SHOP_ID, ""),
                    HttpUrl.getCookies(view.getActivity()), HttpUrl.getUserAgent(view.getActivity()));
        }
    }

    // 加入购物车
    public void loadAddCartData(String sku, int num){
        loadAddCartOrLijiBuyData(sku, num, 11);
    }

    // 立即购买
    public void loadLijiBuyData(String sku, int num){
        loadAddCartOrLijiBuyData(sku, num, 2);
    }

    private void loadAddCartOrLijiBuyData(String sku, int num, int type){
        GoodsInfoFragment view = getIView();
        if(null != view){
            model.loadAddCartOrBuyData(PreUtils.getString(view.getActivity(), PreUtils.SHOP_ID, ""),
                    sku, num, type, HttpUrl.getCookies(view.getActivity()), HttpUrl.getUserAgent(view.getActivity()));
        }
    }

    public void onSuccess(DetailBean bean){
        GoodsInfoFragment view = getIView();
        if(null != view){
            view.onSuccess(bean);
        }
    }

    public void onAddCartSuccess(AddCartBean.AddCartData bean, int type){
        GoodsInfoFragment view = getIView();
        if(null != view){
            view.onAddCartSuccess(bean, type);
        }
    }

    public void onAddCartFail(String str){
        GoodsInfoFragment view = getIView();
        if(null != view){
            view.onAddCartFail(str);
        }
    }

    public void onFail(String str){
        GoodsInfoFragment view = getIView();
        if(null != view){
            view.onFail(str);
        }
    }
}
