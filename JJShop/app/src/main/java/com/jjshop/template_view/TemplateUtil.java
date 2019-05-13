package com.jjshop.template_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.jjshop.R;
import com.jjshop.bean.CartDataBean;
import com.jjshop.bean.MyOrderBottomBean;
import com.jjshop.bean.MyOrderProductList;
import com.jjshop.bean.MyOrderTopBean;
import com.jjshop.bean.ProductListBean;
import com.jjshop.bean.WuliuInfoBeanExpress;
import com.jjshop.bean.WuliuTopBean;

/**
 * 作者：张国庆
 * 时间：2018/8/4
 */

public class TemplateUtil {

    private static volatile TemplateUtil templateUtil;

    private TemplateUtil(){}

    public static TemplateUtil build(){
        if(null == templateUtil){
            synchronized (TemplateUtil.class){
                if(null == templateUtil){
                    templateUtil = new TemplateUtil();
                }
            }
        }
        return templateUtil;
    }

    // 购物车列表数据
    public static final int TEMPLATE_1000 = 1000;
    // 购物车没有数据
    public static final int TEMPLATE_1001 = 1001;
    // 购物车猜你喜欢数据
    public static final int TEMPLATE_1002 = 1002;
    // 地址列表
    public static final int TEMPLATE_1003 = 1003;
    // 优惠券
    public static final int TEMPLATE_1004 = 1004;
    // 确认订单-商品列表
    public static final int TEMPLATE_1005 = 1005;
    // 可能喜欢商品列表
    public static final int TEMPLATE_1006 = 1006;
    // 订单列表商品item
    public static final int TEMPLATE_1007 = 1007;
    // 售后、退款订单列表的item
    public static final int TEMPLATE_1008 = 1008;
    // 订单列表顶部item
    public static final int TEMPLATE_1010 = 1010;
    // 订单列表底部item
    public static final int TEMPLATE_1011 = 1011;
    // 物流信息顶部 快递信息
    public static final int TEMPLATE_1012 = 1012;
    // 物流信息
    public static final int TEMPLATE_1013 = 1013;
    // 商品列表item
    public static final int TEMPLATE_1014 = 1014;
    // 显示秒杀商品列表item
    public static final int TEMPLATE_1015 = 1015;


    /**
     * 获取模板id
     * @param o
     * @return
     */
    public int getTemplateId(Object o){
        if(null == o){
            return 0;
        }
        if(o instanceof ProductListBean){// 购物车-猜你喜欢数据、商品列表
            ProductListBean bean = (ProductListBean) o;
            return bean.getTemplateType();
        }else if(o instanceof CartDataBean){// 购物车数据-没有数据的时候
            CartDataBean bean = (CartDataBean) o;
            return bean.getTemplateType();
        }else if(o instanceof MyOrderTopBean){// 订单列表顶部
            MyOrderTopBean bean = (MyOrderTopBean) o;
            return bean.getTemplate();
        }else if(o instanceof MyOrderProductList){// 订单列表商品
            MyOrderProductList bean = (MyOrderProductList) o;
            return bean.getTemplate();
        }else if(o instanceof MyOrderBottomBean){// 订单列表底部
            MyOrderBottomBean bean = (MyOrderBottomBean) o;
            return bean.getTemplate();
        }else if(o instanceof WuliuTopBean){// 物流信息顶部
            WuliuTopBean bean = (WuliuTopBean) o;
            return bean.getTemplate();
        }else if(o instanceof WuliuInfoBeanExpress){// 物流信息
            WuliuInfoBeanExpress bean = (WuliuInfoBeanExpress) o;
            return bean.getTemplate();
        }
        return 0;
    }

    /**
     * 通过模板id获取相对应的view(有新模板直接添加在方法里)
     * @param itemViewType  模板id
     * @param context       上下文
     * @return              对应的view
     */
    public View getContentView(int itemViewType, Context context, LayoutInflater inflater){
        if(null == context){
            return null;
        }
        if(null == inflater){
            return new View(context);
        }
        View contentView;
        switch (itemViewType){
            case TEMPLATE_1000:// 购物车列表item模板
                contentView = getTemplateView(R.layout.template_cart_view_layout, inflater);
                break;
            case TEMPLATE_1001:// 购物车没有数据模板
                contentView = getTemplateView(R.layout.template_cart_no_data_view_layout,inflater);
                break;
            case TEMPLATE_1002:// 购物车底部猜你喜欢列表item模板
                contentView = getTemplateView(R.layout.template_cart_like_layout, inflater);
                break;
            case TEMPLATE_1003:// 地址列表item模板
                contentView = getTemplateView(R.layout.template_address_view_layout, inflater);
                break;
            case TEMPLATE_1004:// 优惠券列表item模板
                contentView = getTemplateView(R.layout.template_discount_quan_layout, inflater);
                break;
            case TEMPLATE_1005:// 提交订单商品列表item
                contentView = getTemplateView(R.layout.template_submit_order_goods_view_layout, inflater);
                break;
            case TEMPLATE_1006:// 详情页猜你喜欢、大家都在买列表item
                contentView = getTemplateView(R.layout.template_goods_like_view_layout, inflater);
                break;
            case TEMPLATE_1007:// 订单列表item商品模板
                contentView = getTemplateView(R.layout.template_order_item_view_layout, inflater);
                break;
            case TEMPLATE_1008:// 售后、退款订单列表item模板
                contentView = getTemplateView(R.layout.template_after_sale_list_view_layout, inflater);
                break;
            case TEMPLATE_1010:// 订单列表item顶部模板
                contentView = getTemplateView(R.layout.template_order_top_view_layout, inflater);
                break;
            case TEMPLATE_1011:// 订单列表item底部模板
                contentView = getTemplateView(R.layout.template_order_bottom_view_layout, inflater);
                break;
            case TEMPLATE_1012:// 物流信息列表顶部快递信息item
                contentView = getTemplateView(R.layout.template_wuliu_top_view_layout, inflater);
                break;
            case TEMPLATE_1013:// 物流信息列表顶部快递信息item
                contentView = getTemplateView(R.layout.template_wuliu_item_view_layout, inflater);
                break;
            case TEMPLATE_1014:// 商品列表item
                contentView = getTemplateView(R.layout.template_goods_view_layout, inflater);
                break;
            case TEMPLATE_1015:// 显示秒杀商品列表item
                contentView = getTemplateView(R.layout.template_rushbuy_view_layout, inflater);
                break;
            default:
                contentView = new View(context);
                break;
        }
        return contentView;
    }

    // 加载view
    private View getTemplateView(int viewId, LayoutInflater inflater){
        if(null == inflater){
            return null;
        }
        return inflater.inflate(viewId, null);
    }
}
