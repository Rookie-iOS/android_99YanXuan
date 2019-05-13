package com.jjshop.ui.presenter;

import com.jjshop.base.BasePresenter;
import com.jjshop.base.IModel;
import com.jjshop.base.IView;
import com.jjshop.bean.ProductListBean;
import com.jjshop.bean.ShoppingCartBean;
import com.jjshop.bean.UpdateCartNumBean;
import com.jjshop.ui.fragment.ShopCarFragment;
import com.jjshop.ui.model.ShoppingCartModel;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.httputil.HttpUrl;

import java.util.ArrayList;

/**
 * 作者：张国庆
 * 时间：2018/7/20
 */

public class ShoppingCartPresenter extends BasePresenter<ShopCarFragment> {
    private ShoppingCartModel model;
    public ShoppingCartPresenter(IView iView) {
        super(iView);
        model = (ShoppingCartModel) loadBaseModel();
    }

    @Override
    public IModel loadBaseModel() {
        return new ShoppingCartModel(this);
    }

    // 加载列表
    public void loadData(){
        ShopCarFragment view = getIView();
        if(null != view && null != view.getActivity()){
            model.loadData(PreUtils.getString(view.getActivity(), PreUtils.SHOP_ID, ""),
                    HttpUrl.getCookies(view.getActivity()), HttpUrl.getUserAgent(view.getActivity()));
        }
    }

    // 修改数量
    public void loadUpdateNumData(){
        ShopCarFragment view = getIView();
        if(null != view && null != view.getActivity()){
            model.loadUpdateNumData(PreUtils.getString(view.getActivity(), PreUtils.SHOP_ID, ""),
                    String.valueOf(view.cart_id()), view.sku(), view.updateNum(),HttpUrl.getCookies(view.getActivity()),
                    HttpUrl.getUserAgent(view.getActivity()));
        }
    }

    // 删除商品
    public void loadDelData(){
        ShopCarFragment view = getIView();
        if(null != view && null != view.getActivity()){
            model.loadDelData(PreUtils.getString(view.getActivity(), PreUtils.SHOP_ID, ""),
                    view.cart_ids(), HttpUrl.getCookies(view.getActivity()), HttpUrl.getUserAgent(view.getActivity()));
        }
    }

    // 选中、非选中商品
    public void loadSelectData(){
        ShopCarFragment view = getIView();
        if(null != view && null != view.getActivity()){
            model.loadSelectData(PreUtils.getString(view.getActivity(), PreUtils.SHOP_ID, ""),
                    view.cart_ids(), view.select(), HttpUrl.getCookies(view.getActivity()), HttpUrl.getUserAgent(view.getActivity()));
        }
    }

    // 列表数据
    public void onSuccess(ShoppingCartBean.ShoppingCartData bean){
        ShopCarFragment view = getIView();
        if(null != view){
            view.onSuccess(bean);
        }
    }

    // 修改数量成功
    public void onUpdateNumSuccess(UpdateCartNumBean.UpdateCartNumData bean){
        ShopCarFragment view = getIView();
        if(null != view){
            view.onUpdateNumSuccess(bean);
        }
    }

    public void onFail(String str){
        ShopCarFragment view = getIView();
        if(null != view){
            view.onFail(str);
        }
    }

    public void onUpdateNumFail(String str){
        ShopCarFragment view = getIView();
        if(null != view){
            view.onUpdateNumFail(str);
        }
    }

    public void onDelSelectSuccess(){
        ShopCarFragment view = getIView();
        if(null != view){
            view.onDelSelectSuccess();
        }
    }

    public void setIsSelect(boolean isSelect){
        ShopCarFragment view = getIView();
        if(null != view){
            model.setIsSelect(isSelect, view.getListData(), view.isDelStatus());
        }
    }

    public ArrayList<ProductListBean> getGoodsList(ArrayList<ProductListBean> list){
        ShopCarFragment view = getIView();
        if(null != view){
            return model.getGoodsList(list);
        }
        return null;
    }

    public boolean isSelectAll(){
        ShopCarFragment view = getIView();
        if(null != view){
            return model.isSelectAll(view.getListData(), view.isDelStatus());
        }
        return false;
    }

    public void delLocalGoods(){
        ShopCarFragment view = getIView();
        if(null != view){
            model.delLocalGoods(view.getListData());
        }
    }

    public String getSelectId(){
        ShopCarFragment view = getIView();
        if(null != view){
            return model.getSelectId(view.getListData());
        }
        return "";
    }

    public String getGoodsAllId(){
        ShopCarFragment view = getIView();
        if(null != view){
            return model.getGoodsAllId(view.getListData());
        }
        return "";
    }

    public boolean isHaveCartData(){
        ShopCarFragment view = getIView();
        if(null != view){
            return model.isHaveCartData(view.getListData());
        }
        return false;
    }
}
