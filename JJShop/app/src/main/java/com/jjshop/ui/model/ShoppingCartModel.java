package com.jjshop.ui.model;

import com.jjshop.base.BaseModel;
import com.jjshop.base.BasePresenter;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.CartDataBean;
import com.jjshop.bean.ProductListBean;
import com.jjshop.bean.ShoppingCartBean;
import com.jjshop.bean.UpdateCartNumBean;
import com.jjshop.template_view.TemplateUtil;
import com.jjshop.ui.presenter.ShoppingCartPresenter;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.httputil.OnRequestCallBack;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class ShoppingCartModel extends BaseModel {
    private ShoppingCartPresenter presenter;
    public ShoppingCartModel(BasePresenter basePresenter) {
        super(basePresenter);
        presenter = (ShoppingCartPresenter) basePresenter;
    }

    /**
     * 购物车列表数据
     * @param shopId
     * @param cookie
     * @param userAgent
     */
    public void loadData(String shopId, String cookie, String userAgent){
        HttpHelper.bulid().getRequest(HttpUrl.build().getShoppingCart(shopId), ShoppingCartBean.class,
                new OnRequestCallBack<ShoppingCartBean>() {
                    @Override
                    public void onSuccess(ShoppingCartBean bean) {
                        ShoppingCartBean.ShoppingCartData data = bean.getData();
                        if(null == data){
                            presenter.onFail("数据为空");
                            return;
                        }
                        presenter.onSuccess(data);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onFail(msg);
                    }
                });
    }

    /**
     * 删除购物车商品
     * @param shopId
     * @param cookie
     * @param userAgent
     */
    public void loadDelData(String shopId, String cart_id, String cookie, String userAgent){
        HttpHelper.bulid().postRequest(HttpUrl.URL_DEL_CART_GOODS, BaseBean.class,
                HttpUrl.build().getDelCartGoods(shopId, cart_id, cookie, userAgent),
                new OnRequestCallBack<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean bean) {
                        presenter.onDelSelectSuccess();
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onUpdateNumFail(msg);
                    }
                });
    }

    /**
     * 修改购物车商品的数量
     * @param shopId
     * @param cookie
     * @param userAgent
     */
    public void loadUpdateNumData(String shopId, String cart_id, String sku, final int num,
                                  String cookie, String userAgent){
        HttpHelper.bulid().postRequest(HttpUrl.URL_UPDATE_CART_NUM, UpdateCartNumBean.class,
                HttpUrl.build().getUpdateCartNum(shopId, cart_id, sku, num, cookie, userAgent),
                new OnRequestCallBack<UpdateCartNumBean>() {
                    @Override
                    public void onSuccess(UpdateCartNumBean bean) {
                        UpdateCartNumBean.UpdateCartNumData data = bean.getData();
                        if(null == data){
                            presenter.onUpdateNumFail("没有数据");
                            return;
                        }
                        presenter.onUpdateNumSuccess(data);
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onUpdateNumFail(msg);
                    }
                });
    }

    /**
     * 选中、非选中购物车的商品
     * @param shopId
     * @param cookie
     * @param userAgent
     */
    public void loadSelectData(String shopId, String cart_id, int select, String cookie, String userAgent){
        HttpHelper.bulid().postRequest(HttpUrl.URL_SELECT_CART, BaseBean.class,
                HttpUrl.build().getSelectCart(shopId, cart_id, select, cookie, userAgent),
                new OnRequestCallBack<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean bean) {
                        presenter.onDelSelectSuccess();
                    }

                    @Override
                    public void onError(String msg) {
                        presenter.onUpdateNumFail(msg);
                    }
                });
    }

    /**
     * 设置全选、非全选
     * @param isSelect
     */
    public void setIsSelect(boolean isSelect, ArrayList<Object> list, boolean isDelStatus){
        if(null != list && list.size() > 0){
            for(int i = 0; i < list.size(); i++){
                Object o = list.get(i);
                if(null != o && o instanceof CartDataBean){
                    CartDataBean bean = (CartDataBean) o;
                    if(null != bean && bean.getTemplateType() != TemplateUtil.TEMPLATE_1001){
                        bean.setSelected(isSelect ? 2 : 1);
                    }
                }
            }
        }
    }

    /**
     * 底部可能喜欢的数据，组合成2个一组
     * @param list
     * @return
     */
    public ArrayList<ProductListBean> getGoodsList(ArrayList<ProductListBean> list){
        // 最终返回的商品集合
        ArrayList<ProductListBean> likeListAll = new ArrayList<>();
        // 取两个存入的集合
        ArrayList<ProductListBean> likeList = new ArrayList<>();
        int size = list.size();
        for(int i = 1; i <= size; i++){
            likeList.add(list.get(i - 1));
            if(i % 2 == 0 || i == size){
                ProductListBean bean = new ProductListBean();
                bean.setTemplateType(TemplateUtil.TEMPLATE_1002);
                bean.setCartIsCheck(false);
                bean.setCustomBlock(likeList);
                if((size >= 2 && i == 2) || size < 2){
                    bean.setFirstShow(true);
                }
                likeListAll.add(bean);
                if(i % 2 == 0){
                    likeList = new ArrayList<>();
                }
            }
        }
        return likeListAll;
    }

    /**
     * 获取是否所有的都选中
     * @param list
     * @param isDelStatus
     * @return
     */
    public boolean isSelectAll(ArrayList<Object> list, boolean isDelStatus){
        if(null != list && list.size() > 0){
            for(int i = 0; i < list.size(); i++){
                Object o = list.get(i);
                if(null != o && o instanceof CartDataBean){
                    CartDataBean bean = (CartDataBean) o;
                    if(null != bean && bean.getTemplateType() != TemplateUtil.TEMPLATE_1001){
                        if(bean.getSelected() != 2){
                            return false;
                        }

                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 选中商品的id
     * @return
     */
    public String getSelectId(ArrayList<Object> list){
        String string = "";
        if(null != list && list.size() > 0){
            Iterator iterator = list.iterator();
            while (iterator.hasNext()){
                Object o = iterator.next();
                if(null != o && o instanceof CartDataBean) {
                    CartDataBean bean = (CartDataBean) o;
                    if(null != bean && bean.getTemplateType() != TemplateUtil.TEMPLATE_1001){
                        if(bean.getSelected() == 2){
//                            if(StringUtil.isEmpty(string)){
                            string = string + String.valueOf(bean.getCart_id()) + ",";
//                            }else{
//                                string = string + "," + bean.getCart_id();
//                            }
                        }
                    }
                }
            }
        }
        return string;
    }

    /**
     * 所有商品id
     * @return
     */
    public String getGoodsAllId(ArrayList<Object> list){
        String string = "";
        if(null != list && list.size() > 0){
            Iterator iterator = list.iterator();
            while (iterator.hasNext()){
                Object o = iterator.next();
                if(null != o && o instanceof CartDataBean) {
                    CartDataBean bean = (CartDataBean) o;
                    if(null != bean && bean.getTemplateType() != TemplateUtil.TEMPLATE_1001){
                        string = string + String.valueOf(bean.getCart_id()) + ",";
                    }
                }
            }
        }
        return string;
    }

    /**
     * 删除本地集合中的商品
     * @param list
     */
    public void delLocalGoods(ArrayList<Object> list){
        String ids = getSelectId(list);
        if(StringUtil.isEmpty(ids)){
            return;
        }
        if(null != list && list.size() > 0){
            Iterator iterator = list.iterator();
            while(iterator.hasNext()){
                Object o = iterator.next();
                if(null != o && o instanceof CartDataBean){
                    CartDataBean bean = (CartDataBean) o;
                    if(null != bean && bean.getTemplateType() != TemplateUtil.TEMPLATE_1001){
                        if(ids.indexOf(String.valueOf(bean.getCart_id())) != -1){
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }

    /**
     * 是否还有购物车数据（不包括底部的你可能喜欢）
     * @return
     */
    public boolean isHaveCartData(ArrayList<Object> list){
        if(null != list && list.size() > 0){
            for(int i = 0; i < list.size(); i++){
                Object o = list.get(i);
                if(null != o && o instanceof CartDataBean){
                    CartDataBean bean = (CartDataBean) o;
                    if(null != bean && bean.getTemplateType() != TemplateUtil.TEMPLATE_1001){
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

}
