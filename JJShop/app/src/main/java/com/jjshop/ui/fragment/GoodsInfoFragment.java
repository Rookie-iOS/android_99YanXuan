package com.jjshop.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.adapter.CommentListAdapter;
import com.jjshop.base.BaseFragment;
import com.jjshop.bean.ADShowBean;
import com.jjshop.bean.AddCartBean;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.DetailBean;
import com.jjshop.bean.JJEvent;
import com.jjshop.bean.ProductListBean;
import com.jjshop.dialog.GoodsGuigeDialog;
import com.jjshop.listener.OnCommonCallBackIm;
import com.jjshop.template_view.TemplateBannerView;
import com.jjshop.template_view.TemplateUtil;
import com.jjshop.ui.WebViewActivity;
import com.jjshop.ui.activity.home.DetailActivity;
import com.jjshop.ui.activity.home.HomeActivity;
import com.jjshop.ui.activity.home.SubmitOrderActivity;
import com.jjshop.ui.presenter.DetailsPresenter;
import com.jjshop.utils.ADUtil;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.JjToast;
import com.jjshop.widget.MyGridView;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.Tools;
import com.jjshop.utils.UIHelper;
import com.jjshop.utils.UIUtils;
import com.jjshop.utils.UrlInvokeUtil;
import com.jjshop.utils.XNUtil;
import com.jjshop.utils.glide_img.GlideUtil;
import com.jjshop.utils.httputil.HttpHelper;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.widget.MyScrollView;
import com.jjshop.widget.NoScrollWebview;
import com.jjshop.widget.SlideDetailsLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：张国庆
 * 时间：2018/7/26
 */

public class GoodsInfoFragment extends BaseFragment<DetailsPresenter> implements
        SlideDetailsLayout.OnSlideDetailsListener,
        MyScrollView.OnMyScrollChanged {
    @BindView(R.id.cell_gridgoodname)
    TextView mTvName;
    @BindView(R.id.price)
    TextView mTvPrice;
    @BindView(R.id.earn_price)
    TextView mTvZhuanPrice;
    @BindView(R.id.cell_grid_limitprice)
    TextView mTvRushPrice;
    @BindView(R.id.m_gridview_one)
    MyGridView mGridviewOne;
    @BindView(R.id.m_sv_layout)
    MyScrollView mSvLayout;
    @BindView(R.id.m_webview)
    NoScrollWebview mWebview;
    @BindView(R.id.m_gridview_two)
    MyGridView mGridviewTwo;
    @BindView(R.id.m_slide_layout)
    SlideDetailsLayout mSlideLayout;
    @BindView(R.id.m_tv_cart_num)
    TextView mTvCartNum;
    @BindView(R.id.m_layout_bottom)
    LinearLayout mLayoutBottom;
    @BindView(R.id.m_iv_up_down)
    ImageView mIvUpDown;
    @BindView(R.id.m_tv_uppdown)
    TextView mTvUppdown;
    @BindView(R.id.m_up_down_layout)
    LinearLayout mUpDownLayout;
    @BindView(R.id.m_tv_discount)
    TextView mTvDiscount;
    @BindView(R.id.m_tv_jptj)
    TextView mTvJptj;
    @BindView(R.id.m_tv_guige)
    TextView mTvGuige;
    @BindView(R.id.m_view_loading)
    View mViewLoading;
    @BindView(R.id.m_view_error)
    View mViewError;
    @BindView(R.id.m_layout_jptj)
    RelativeLayout mLayoutJptj;
    @BindView(R.id.m_layout_discount)
    RelativeLayout mLayoutDiscount;
    @BindView(R.id.m_layout_rush)
    RelativeLayout mLayoutRush;
    @BindView(R.id.m_tv_rush)
    TextView mTvRush;
    @BindView(R.id.m_layout_add_cart_success)
    RelativeLayout mLayoutAddCartSuccess;
    @BindView(R.id.m_tv_goods_status)
    TextView mTvGoodsStatus;
    @BindView(R.id.m_layout_guige)
    RelativeLayout mLayoutGuige;
    @BindView(R.id.m_tv_sell)
    TextView mTvSell;
    @BindView(R.id.m_tv_buy)
    TextView mTvBuy;
    @BindView(R.id.m_iv_details_ad)
    ImageView mIvDetailsAd;
    @BindView(R.id.m_tv_kefu)
    TextView mTvKefu;
    @BindView(R.id.m_cart_layout)
    RelativeLayout mLayoutCart;
    @BindView(R.id.m_ll_bottom_djs)
    LinearLayout mLayoutBottomDjs;
    @BindView(R.id.m_tv_bottom_djs)
    TextView mTvBottomDjs;
    @BindView(R.id.m_tv_bottom_djs_tishi)
    TextView mTvBottomDjsTishi;
    @BindView(R.id.relative_layout)
    RelativeLayout bannerLayout;

    private String mIdCode = "", mProductId = "", mDisCountId = "", mAdUrl = "";

    private CommentListAdapter mAdapterLike, mAdapterBuy;
    private ArrayList<Object> mListLike, mListBuy;
    private int mTitleHeight = 0;
    // 规格集合
    private ArrayList<DetailBean.DataBeanX.DataBean.SpecData> mListGuige;
    // 默认选中的规格下标
    private int mColorPosition = 0, mSizePosition = 0;
    // 商品信息
    private DetailBean.DataBeanX.DataBean goodsBean;
    private Handler handler;
    private Runnable runnable;
    private DetailBean.LimitBuyData limitBuyData;
    private ADShowBean.ADShowBeanData adShowBeanData;// 广告数据
    private GoodsGuigeDialog guigeDialog;// 规格弹窗
    private View mBannerView;

    @Override
    protected DetailsPresenter getPresenter() {
        return new DetailsPresenter(this);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_goods_info_layout;
    }

    public static GoodsInfoFragment invoke(String idCode, String productId) {
        GoodsInfoFragment fragment = new GoodsInfoFragment();
        Bundle b = new Bundle();
        b.putString("idcode", idCode);
        b.putString("product_id", productId);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void initView(View view) {
        mTitleHeight = UIUtils.getWidth() / 2;
        Bundle b = getArguments();
        if (null != b) {
            mIdCode = b.getString("idcode", "");
            mProductId = b.getString("product_id", "");
        }
        mSvLayout.setOnMyScrollChanged(this);
        mSlideLayout.setOnSlideDetailsListener(this);
        mSlideLayout.setVisibility(View.GONE);
        mLayoutBottom.setVisibility(View.GONE);
        mListLike = new ArrayList<>();
        mListBuy = new ArrayList<>();
        loadData();
        // 加载广告数据
        ADUtil.build().loadADShow(activity, ADUtil.APPKEY_AD_DETAILS, new OnCommonCallBackIm() {
            @Override
            public void onSuccess(Object o) {
                if (null != o && o instanceof ADShowBean.ADShowBeanData) {
                    adShowBeanData = (ADShowBean.ADShowBeanData) o;
                }
            }
        });
    }

    @OnClick({R.id.m_cart_layout, R.id.m_tv_buy, R.id.m_tv_sell, R.id.m_up_down_layout, R.id
            .m_tv_refresh,
            R.id.m_tv_kefu, R.id.m_layout_discount, R.id.m_layout_guige, R.id
            .m_layout_add_cart_success,
            R.id.m_tv_goods_status, R.id.m_iv_details_ad})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_up_down_layout:
                break;
            case R.id.m_cart_layout:// 购物车
                HomeActivity.invoke(activity, HomeActivity.SELECT_CART);
                XNUtil.getXNUtil().finishChat();
                EventBus.getDefault().post(new JJEvent(JJEvent.FINISH_ACTIVITY));
                break;
            case R.id.m_tv_buy:// 买
            case R.id.m_layout_guige:// 规格
                if (null == mListGuige || mListGuige.isEmpty()) {
                    return;
                }
                guigeDialog = GoodsGuigeDialog.build().showView(getFragmentManager(), mListGuige,
                        mColorPosition, mSizePosition, limitBuyData, mBottomTime);
                break;
            case R.id.m_tv_sell:// 卖
                share();
                break;
            case R.id.m_layout_discount:// 优惠
                if (StringUtil.isEmpty(mDisCountId)) {
                    return;
                }
                WebViewActivity.invoke(activity, HttpUrl.WEB_DISCOUNT + PreUtils.getString
                        (activity, PreUtils.SHOP_ID)
                        + "&id=" + mDisCountId);
                break;
            case R.id.m_tv_kefu:// 客服
                if (null == goodsBean) {
                    XNUtil.getXNUtil().openChat(activity, null);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(XNUtil.GOODS_ID, mProductId);
                bundle.putString(XNUtil.GOODS_ID_CODE, mIdCode);
                bundle.putString(XNUtil.GOODS_NAME, goodsBean.getName());
                bundle.putString(XNUtil.GOODS_PRICE, "¥" + goodsBean.getPrice());
                ArrayList<String> imgs = goodsBean.getImgs();
                if (null != imgs && imgs.size() > 0) {
                    bundle.putString(XNUtil.GOODS_IMG, imgs.get(0));
                }
                XNUtil.getXNUtil().openChat(activity, bundle);
                break;
            case R.id.m_layout_add_cart_success:// 加入购物车成功显示的布局
                SubmitOrderActivity.invoke(activity);
                break;
            case R.id.m_tv_goods_status:// 下架、售罄

                break;
            case R.id.m_tv_refresh:// 刷新
                loadData();
                break;
            case R.id.m_iv_details_ad:// 广告
                ADUtil.build().loadADReport(activity, true, adShowBeanData);
                UrlInvokeUtil.build().pushInvoke(activity, UrlInvokeUtil.build().pushData(mAdUrl));
                break;
        }
    }

    private void loadData() {
        if (null != mPresenter) {
            showLoadingView();
            mPresenter.loadData();
        }
    }

    // 添加购物车
    public void addCart(String sku, int num) {
        if (null == mPresenter) {
            return;
        }
        mViewLoading.setVisibility(View.VISIBLE);
        mPresenter.loadAddCartData(sku, num);
    }

    // 立即购买
    public void lijiBuy(String sku, int num) {
        if (null == mPresenter) {
            return;
        }
        mViewLoading.setVisibility(View.VISIBLE);
        mPresenter.loadLijiBuyData(sku, num);
    }

    // 详情数据获取成功
    public void onSuccess(DetailBean bean) {
        showDataView();
        mErrorNum = 0;
        DetailBean.DataBeanX dataBean = bean.getData();
        if (null == dataBean) {
            shoErrorView();
            return;
        }
        if (null != activity && activity instanceof DetailActivity) {
            ((DetailActivity) activity).showShareImg();
        }
        // 存储用户id、名称
        PreUtils.setString(activity, PreUtils.USER_ID_MINGWEN, String.valueOf(dataBean.getUser_id
                ()));
        PreUtils.setString(activity, PreUtils.USER_NICKNAME, dataBean.getUser_name());
        // 登录小能客服
        XNUtil.getXNUtil().login(activity);
        // 猜你喜欢
        List<ProductListBean> listLike = dataBean.getLikeproduct();
        if (null != listLike && listLike.size() > 0) {
            mListLike.clear();
            mListLike.addAll(listLike);
            if (null == mAdapterLike) {
                mAdapterLike = new CommentListAdapter(activity, mListLike, TemplateUtil
                        .TEMPLATE_1006);
                mGridviewOne.setAdapter(mAdapterLike);
            } else {
                mAdapterLike.notifyDataSetChanged();
            }
        }
        // 大家都在买
        List<ProductListBean> listDou = dataBean.getBestproduct();
        if (null != listLike && listDou.size() > 0) {
            mListBuy.clear();
            mListBuy.addAll(listDou);
            if (null == mAdapterBuy) {
                mAdapterBuy = new CommentListAdapter(activity, mListBuy, TemplateUtil
                        .TEMPLATE_1006);
                mGridviewTwo.setAdapter(mAdapterBuy);
            } else {
                mAdapterBuy.notifyDataSetChanged();
            }
        }
        // 购物车数量
        showCartNum(dataBean.getCartProfuctNum());
        goodsBean = dataBean.getData();
        if (null == goodsBean) {
            return;
        }
        String goodsUrl = goodsBean.getLink();
        // 日志统计
        HttpHelper.bulid().getRequest(HttpUrl.build().getGoodsDetailsLog(mIdCode, dataBean
                        .getUser_idcode(),
                PreUtils.getString(activity, PreUtils.SHOP_ID), goodsUrl), BaseBean.class, null);
        // 商品轨迹
        XNUtil.getXNUtil().startGoodsDetail(goodsBean.getName(), goodsUrl);
        //商品详情
        ArrayList<String> bannerList = goodsBean.getImgs();
        if (null != bannerList && bannerList.size() > 0) {// Banner
            bannerLayout.removeAllViews();
            mBannerView = LayoutInflater.from(activity).inflate(R.layout
                    .template_banner_view_layout, null);
            bannerLayout.addView(mBannerView);
            String videoImg = goodsBean.getVideoimg();// 视频封面图
            String videoUrl = goodsBean.getVideouri();// 视频地址
            if (!StringUtil.isEmpty(videoUrl) && StringUtil.isEmpty(videoImg)) {//
                // 有视频没有视频封面图时，使用banner第一张图片
                videoImg = bannerList.get(0);
                bannerList.add(0, videoImg);
            }
            if (mBannerView instanceof TemplateBannerView) {
                ((TemplateBannerView) mBannerView).getDate(bannerList, videoUrl, null);
            }
        } else {
            bannerLayout.removeAllViews();
            CommonUtils.build().setViewH(bannerLayout, UIUtils.getWidth());
        }
        mTvName.setText(goodsBean.getName());
        mTvPrice.setText("¥" + goodsBean.getPrice());
        mTvZhuanPrice.setText("赚" + goodsBean.getEarn_price());
        // 未开始活动倒计时
        DetailBean.PreviewData previewData = goodsBean.getPreview();
        if (null != previewData) {
            startBottomTimer(previewData.getStime());
        }
        //优惠秒杀
        DetailBean.DataBeanX.DataBean.ValidSpecialActivityBean activityBean = goodsBean
                .getValid_special_activity();
        mTvRushPrice.setVisibility(View.GONE);
        mLayoutDiscount.setVisibility(View.GONE);
        mLayoutRush.setVisibility(View.GONE);
        if (null != activityBean) {
            if (activityBean.getEnd_time() > 0) {
                mLayoutRush.setVisibility(View.VISIBLE);
                mTvRushPrice.setVisibility(View.VISIBLE);
                // 倒计时
                startTimer(activityBean.getEnd_time());
            }
            if (!StringUtil.isEmpty(activityBean.getSpecial_name())) {
                mLayoutDiscount.setVisibility(View.VISIBLE);
                // 优惠秒杀
                mTvDiscount.setText(activityBean.getSpecial_name());
            }
            mDisCountId = activityBean.getId_code();
        }
        // 精品推荐
        if (StringUtil.isEmpty(goodsBean.getAdword())) {
            mLayoutJptj.setVisibility(View.GONE);
        } else {
            mLayoutJptj.setVisibility(View.VISIBLE);
            mTvJptj.setText(goodsBean.getAdword());
        }
        // 图文详情
        loadWebview(goodsBean.getInfo());
        // 规格
        if (goodsBean.getProduct_status() != 1 || goodsBean.getStocks() < 1) {
            mLayoutGuige.setVisibility(View.GONE);
            mTvBuy.setEnabled(false);
            mTvBuy.setClickable(false);
            mTvBuy.setBackgroundColor(getResources().getColor(R.color.color_ffc48d));
            mTvSell.setEnabled(false);
            mTvSell.setClickable(false);
            mTvSell.setBackgroundColor(getResources().getColor(R.color.color_eb8bad));
            mTvGoodsStatus.setVisibility(View.VISIBLE);
            if (goodsBean.getProduct_status() != 1) {
                mTvGoodsStatus.setText("商品已经下架啦");
            } else {
                mTvGoodsStatus.setText("商品已经售罄");
            }
            return;
        }
        mListGuige = goodsBean.getSpec();
        limitBuyData = goodsBean.getLimitbuy();
        selectGuige("", 0, 0);

    }

    // 获取详情数据失败
    private int mErrorNum;

    public void onFail(String msg) {
        mErrorNum++;
        if (mErrorNum <= 2) {
            loadData();
            return;
        }
        mErrorNum = 0;
        shoErrorView();
    }

    // 加入购物车、立即购买成功
    public void onAddCartSuccess(AddCartBean.AddCartData bean, int type) {
        mViewLoading.setVisibility(View.GONE);
        showCartNum(bean.getCartProfuctNum());
        if (type == 2) {// 立即购买、跳转到确认订单页
            SubmitOrderActivity.invoke(activity);
        } else {
            mLayoutAddCartSuccess.setVisibility(View.VISIBLE);
            showAddCartSuccessLayout();
        }
        EventBus.getDefault().post(new JJEvent(JJEvent.REFRESH_SHOPPING_CART));

    }

    // 加入购物车、立即购买失败
    public void onAddCartFail(String msg) {
        mViewLoading.setVisibility(View.GONE);
        JjToast.getInstance().toast(msg);
    }

    // 显示加入成功之后的提示布局
    private void showAddCartSuccessLayout() {
        if (null == runnable) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    mLayoutAddCartSuccess.setVisibility(View.GONE);
                }
            };
        }
        hindAddCartSuccessLayout();
        handler = new Handler();
        handler.postDelayed(runnable, 3000);
    }

    // 隐藏加入成功之后的提示布局
    private void hindAddCartSuccessLayout() {
        if (null != handler && null != runnable) {
            handler.removeCallbacks(runnable);
            handler = null;
        }
    }

    // 显示广告图片
    public void showAdImg(Bundle bundle) {
        if (null == mIvDetailsAd || null == bundle || bundle.size() <= 0) {
            return;
        }
        mAdUrl = bundle.getString(ADUtil.KEY_URL);
        mIvDetailsAd.setVisibility(View.VISIBLE);
        int w = UIUtils.getWidth();
        int h = w * bundle.getInt(ADUtil.KEY_HEIGHT) / bundle.getInt(ADUtil.KEY_WIDTH);
        CommonUtils.build().setViewWH(mIvDetailsAd, w, h);
        GlideUtil.getInstence().loadImage(activity, mIvDetailsAd, bundle.getString(ADUtil.KEY_IMG));
    }

    public String idCode() {
        if (StringUtil.isEmpty(mIdCode)) {
            return "";
        }
        return mIdCode;
    }

    @Override
    public void onStatucChanged(SlideDetailsLayout.Status status) {
        if (status == SlideDetailsLayout.Status.CLOSE) {// 第一页
            setTitleContent("商品详情");
            if (null != mTvUppdown) {
                mTvUppdown.setText("上拉查看图文详情");
            }
            return;
        }
        // 第二页
        setTitleContent("图文详情");
        if (null != mTvUppdown) {
            mTvUppdown.setText("下拉收起图文详情");
        }
    }

    // 加载图文详情
    private void loadWebview(String info) {
        if (StringUtil.isEmpty(info)) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(UIHelper.WEB_STYLE).append(UIHelper.WEB_LOAD_IMAGES);
        String body = info;
        String bodys = String.format("<div  class='bodyPlist'>%s</div>", body, "");
        sb.append(bodys);
        sb.append("<br/>");
        // 封尾
        sb.append("</div><script type=\"text/javascript\" src=\"file:///android_asset/jquery.min" +
                ".js\"></script></body>");
        String ua = mWebview.getSettings().getUserAgentString();
        String urlStr = StringUtil.transEndoing(activity, PreUtils.getString(activity, PreUtils
                .UAUTH, ""));
        mWebview.getSettings().setUserAgentString(ua + " YT-Android/" + StringUtil
                .getAppVersionName(activity) + " YT-LOGIN-TOKEN:" + urlStr + "99yanxuanapp");
        mWebview.loadDataWithBaseURL(null, sb.toString(),
                "text/html", "utf-8", null);
    }

    private CountDownTimer mTimer;

    private void startTimer(long time) {
        long currTime = System.currentTimeMillis() / 1000;
        time = time - currTime;
        if (time <= 0) {
            mTvRush.setText("已结束");
            return;
        }
        mTimer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long l) {
                if (null == mTvRush) {
                    return;
                }
                mTvRush.setText("距结束仅剩   " + Tools.goodsDetailsDaojishi(l / 1000));
            }

            @Override
            public void onFinish() {
                if (null == mTvRush) {
                    return;
                }
                mTvRush.setText("已结束");
            }
        }.start();
    }

    /**
     * 距开始的倒计时
     *
     * @param t  秒
     */
    private long mBottomTime = 0;
    private Timer mTimerBottom;

    private void startBottomTimer(final long t) {
        if (t <= 0) {
            return;
        }
        if (null == mTimerBottom) {
            mTimerBottom = new Timer();
        }
        mBottomTime = t;
        setBottomTimerView(View.VISIBLE, View.GONE);
        JjLog.e("时间 = " + mBottomTime);
        mTimerBottom.schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (null == mTvBottomDjs || null == mTvBottomDjsTishi) {
                            return;
                        }
                        mBottomTime--;
                        mTvBottomDjsTishi.setText((Tools.getDay(mBottomTime) >= 1) ? "距离开始还有 " :
                                "距离开始仅剩 ");
                        mTvBottomDjs.setText(Tools.goodsDetailsDaojishi(mBottomTime));
                        if (mBottomTime <= 0) {
                            setBottomTimerView(View.GONE, View.VISIBLE);
                            mTimerBottom.cancel();
                            mTimerBottom.purge();
                            mTimerBottom = null;
                        }
                        if (null != guigeDialog && null != guigeDialog.getDialog() && guigeDialog
                                .getDialog().isShowing()) {
                            guigeDialog.setLayoutDjs(mBottomTime);
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    private void setBottomTimerView(int timerView, int other) {
        mLayoutBottomDjs.setVisibility(timerView);
        mTvKefu.setVisibility(other);
        mLayoutCart.setVisibility(other);
        mTvBuy.setVisibility(other);
        mTvSell.setVisibility(other);
    }

    // 是否显示加载框
    public void shareShowHideLoading(int visible) {
        if (!(View.GONE == visible || View.VISIBLE == visible) || null == mViewLoading) {
            return;
        }
        mViewLoading.setVisibility(visible);
    }

    private void showLoadingView() {
        if (null == mViewError || null == mViewLoading) {
            return;
        }
        mViewLoading.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);

    }

    private void showDataView() {
        if (null == mViewError || null == mViewLoading || null == mSlideLayout || null ==
                mLayoutBottom) {
            return;
        }
        mSlideLayout.setVisibility(View.VISIBLE);
        mLayoutBottom.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }

    private void shoErrorView() {
        if (null == mViewError || null == mViewLoading || null == mSlideLayout || null ==
                mLayoutBottom) {
            return;
        }
        mViewError.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mSlideLayout.setVisibility(View.GONE);
        mLayoutBottom.setVisibility(View.GONE);
    }

    private int alpha = 0;

    @Override
    public void onScrollChanged(int x, int y, int oldx, int oldy) {
        if (y <= 0) {
            alpha = 0;
        } else if (y > 0 && y <= mTitleHeight) {
            alpha = (255 * y / mTitleHeight);
        } else {
            alpha = 255;
        }
        setTitleBg(alpha);
    }

    // 设置标题背景颜色
    private void setTitleBg(int alpha) {
        if (null != activity && activity instanceof DetailActivity) {
            int bg = Color.argb(alpha, 255, 255, 255);
            int color = alpha <= 0 ? bg : Color.argb(alpha, 51, 51, 51);
            ((DetailActivity) activity).setTitleBg(bg, color);
        }
    }

    // 设置标题内容
    private void setTitleContent(String content) {
        if (null != activity && activity instanceof DetailActivity && !StringUtil.isEmpty
                (content)) {
            ((DetailActivity) activity).setTitleContent(content);
        }
    }

    // 分享
    private void share() {
        if (null != activity && activity instanceof DetailActivity) {
            ((DetailActivity) activity).share();
        }
    }

    // 是否售罄、下架
    public int getIsShouqing() {
        if (null == mTvGoodsStatus) {
            return View.GONE;
        }
        return mTvGoodsStatus.getVisibility();
    }

    // 选中的规格
    public void selectGuige(String guige, int colorPosition, int sizePosition) {
        guigeDialog = null;
        mColorPosition = colorPosition;
        mSizePosition = sizePosition;
        if (StringUtil.isEmpty(guige)) {
            if (null != mListGuige && mListGuige.size() > 0) {
                DetailBean.DataBeanX.DataBean.SpecData colorBean;
                for (int i = 0; i < mListGuige.size(); i++) {
                    colorBean = mListGuige.get(i);
                    guige = "选择：" + colorBean.getAttr_name();
                    if (getStocks(colorBean.getList()) > 0) {
                        guige = guige + "、" + colorBean.getList().get(0).getAttr_name();
                        break;
                    }
                }
            }
        }
        if (null != mTvGuige) {
            mTvGuige.setText(guige);
        }
    }

    // 显示购物车数量
    private void showCartNum(int cartNum) {
        CommonUtils.build().setCirleTextNum(mTvCartNum, cartNum);
    }

    // 获取某个规格的库存总数
    public int getStocks(ArrayList<DetailBean.DataBeanX.DataBean.SpecData.SpecSizeData> list) {
        int stock = 0;
        if (null == list || list.size() <= 0) {
            return stock;
        }
        for (int i = 0; i < list.size(); i++) {
            DetailBean.DataBeanX.DataBean.SpecData.SpecSizeData sizeData = list.get(i);
            stock = stock + sizeData.getStocks();
        }
        return stock;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mTimer) {
            mTimer.cancel();
            mTimer = null;
        }
        if (null != mTimerBottom) {
            mTimerBottom.cancel();
            mTimerBottom.purge();
            mTimerBottom = null;
        }
        if (null != mSlideLayout) {
            mSlideLayout.setOnSlideDetailsListener(null);
            mSlideLayout = null;
        }
        if (null != bannerLayout) {
            bannerLayout.removeAllViews();
        }
        if (null != mSvLayout) {
            mSvLayout.setOnMyScrollChanged(null);
            mSvLayout = null;
        }
        if (null != mAdapterLike) {
            mAdapterLike = null;
        }
        if (null != mAdapterBuy) {
            mAdapterBuy = null;
        }
        if (null != mListGuige) {
            mListGuige.clear();
            mListGuige = null;
        }
        if (null != mListBuy) {
            mListBuy.clear();
            mListBuy = null;
        }
        if (null != mListLike) {
            mListLike.clear();
            mListLike = null;
        }
        if (null != goodsBean) {
            goodsBean = null;
        }
        if (null != limitBuyData) {
            limitBuyData = null;
        }
        if (null != mSlideLayout) {
            mSlideLayout.removeAllViews();
        }
        hindAddCartSuccessLayout();
        try {
            if (null != mWebview) {
                if (null != mSlideLayout) {
                    mSlideLayout.removeView(mWebview);
                }
                mWebview.removeAllViews();
                mWebview.destroy();
                mWebview = null;
            }
        } catch (Exception e) {

        }
    }
}
