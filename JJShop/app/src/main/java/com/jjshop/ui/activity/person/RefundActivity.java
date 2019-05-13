package com.jjshop.ui.activity.person;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.contrarywind.view.WheelView;
import com.jjshop.R;
import com.jjshop.base.BaseActivity;
import com.jjshop.bean.JJEvent;
import com.jjshop.bean.MyOrderProductList;
import com.jjshop.bean.RefundBean;
import com.jjshop.bean.SubmitRefundBean;
import com.jjshop.bean.UploadImgBean;
import com.jjshop.template_view.BaseTemplateIm;
import com.jjshop.ui.presenter.RefundPresenter;
import com.jjshop.utils.FileUtilLM;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.Tools;
import com.jjshop.utils.UIUtils;
import com.jjshop.utils.UploadImgUtil;
import com.jjshop.utils.glide_img.GlideUtil;
import com.qingmei2.rximagepicker.ui.SystemImagePicker;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：张国庆
 * 时间：2018/9/20
 */

public class RefundActivity extends BaseActivity<RefundPresenter> {
    @BindView(R.id.m_tv_title)
    TextView mTvTitle;
    @BindView(R.id.m_layout_goods)
    LinearLayout mLayoutGoods;
    @BindView(R.id.m_tv_status)
    TextView mTvStatus;
    @BindView(R.id.m_layout_status)
    RelativeLayout mLayoutStatus;
    @BindView(R.id.m_view_line)
    View mViewLine;
    @BindView(R.id.m_tv_yuanyin)
    TextView mTvYuanyin;
    @BindView(R.id.m_tv_money)
    TextView mTvMoney;
    @BindView(R.id.m_tv_goods_price)
    TextView mTvGoodsPrice;
    @BindView(R.id.m_tv_coupon_price)
    TextView mTvCouponPrice;
    @BindView(R.id.m_tv_tui_price)
    TextView mTvTuiPrice;
    @BindView(R.id.m_tv_all_price)
    TextView mTvAllPrice;
    @BindView(R.id.m_et_content)
    EditText mEtContent;
    @BindView(R.id.m_iv_one)
    ImageView mIvOne;
    @BindView(R.id.m_iv_two)
    ImageView mIvTwo;
    @BindView(R.id.m_iv_three)
    ImageView mIvThree;
    @BindView(R.id.m_layout_img)
    RelativeLayout mLayoutImg;
    @BindView(R.id.m_view_loading)
    View mViewLoading;
    @BindView(R.id.m_view_empty)
    View mViewEmpty;
    @BindView(R.id.m_view_error)
    View mViewError;
    @BindView(R.id.m_layout_sv)
    ScrollView mLayoutScroll;
    @BindView(R.id.m_et_money)
    EditText mEtMoney;
    @BindView(R.id.m_tv_shuoming)
    TextView mTvShuoming;
    @BindView(R.id.m_tv_yuanyin_left)
    TextView mTvYuanyinLeft;
    @BindView(R.id.m_tv_yue_price)
    TextView mTvYuePrice;

    private OptionsPickerView mPickerView;// 货物状态、退款原因选择框
    private List<String> mPickerData;// 货物状态、退款原因选择框的数据
    private SystemImagePicker imagePicker;// 打开相册
    private int mClickPosition;// 当前的点击的图片的position
    private final int IMGONE_POSITION = 1;// 图片1
    private final int IMGTWO_POSITION = 2;// 图片2
    private final int IMGTHREE_POSITION = 3;// 图片3

    public static final int STATUS_1001 = 1001;// 退款
    public static final int STATUS_1002 = 1002;// 退货

    // 需要传给服务器的图片地址
    private String mImgOneUrl = "", mImgTwoUrl = "", mImgThreeUrl = "";
    private String refund_aomunt = "0.0";
    private int status;
    private View mGoodsView;
    private long order_no;
    private int item_id;
    private File file;


    @Override
    protected int setLayoutId() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return R.layout.activity_refund_layout;
    }

    @Override
    protected RefundPresenter getPresenter() {
        return new RefundPresenter(this);
    }

    public static void invoke(Context context, int status, long order_no, int item_id) {
        Intent intent = new Intent(context, RefundActivity.class);
        intent.putExtra("status", status);
        intent.putExtra("order_no", order_no);
        intent.putExtra("item_id", item_id);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        if (null != getIntent()) {
            status = getIntent().getIntExtra("status", 0);
            order_no = getIntent().getLongExtra("order_no", 0);
            item_id = getIntent().getIntExtra("item_id", 0);
        }
        mTvTitle.setText(status == STATUS_1001 ? "申请退款" : "申请退货");
        mPickerData = new ArrayList<>();
        if (status == STATUS_1002) {// 申请退货
            imagePicker = UploadImgUtil.build().getSystemImagePicker();
            mLayoutImg.setVisibility(View.VISIBLE);
            mLayoutStatus.setVisibility(View.VISIBLE);
            mViewLine.setVisibility(View.VISIBLE);
            mEtContent.setHint("请填写退货说明");
            mTvShuoming.setText("退货说明");
            mTvYuanyinLeft.setText("退货原因");
            // 设置图片大小
            int wh = (UIUtils.getWidth() - UIUtils.dip2px(40)) / 3;
            setImgWh(mIvOne, wh);
            setImgWh(mIvTwo, wh);
            setImgWh(mIvThree, wh);
        }
        mGoodsView = LayoutInflater.from(this).inflate(R.layout.template_order_item_view_layout,
                null);
        mLayoutGoods.addView(mGoodsView);
        // 请求接口
        loadData();
    }

    // 加载详情数据
    public void loadData() {
        if (null != mPresenter) {
            loading();
            if (status == STATUS_1001) {
                mPresenter.loadTuikuanData();
                return;
            }
            mPresenter.loadTuihuoData();
        }
    }

    // 加载上传图片获取链接
    public void loadUploadImg() {
        if (null != mPresenter) {
            loading();
            mPresenter.loadUploadImg();
        }
    }

    // 加载提交申请
    public void loadSubmitData() {
        if (null != mPresenter) {
            loading();
            if (status == STATUS_1001) {
                mPresenter.loadSubmitRefund();
                return;
            }
            mPresenter.loadSubmitReturn();
        }
    }

    @OnClick({R.id.m_title_back, R.id.m_layout_status, R.id.m_layout_yuanyin, R.id.m_iv_one,
            R.id.m_iv_two, R.id.m_iv_three, R.id.m_tv_submit})
    public void onClick(View view) {
        Tools.hideSoftInput(view);
        switch (view.getId()) {
            case R.id.m_title_back:
                finish();
                break;
            case R.id.m_layout_status:// 货物状态
                showSelectData(true);
                break;
            case R.id.m_layout_yuanyin:// 退货/款原因
                showSelectData(false);
                break;
            case R.id.m_iv_one:// 上传第1张图片
                mClickPosition = IMGONE_POSITION;
                UploadImgUtil.build().openDefaultAlbum(imagePicker, this);
                break;
            case R.id.m_iv_two:// 上传第2张图片
                mClickPosition = IMGTWO_POSITION;
                UploadImgUtil.build().openDefaultAlbum(imagePicker, this);
                break;
            case R.id.m_iv_three:// 上传第3张图片
                mClickPosition = IMGTHREE_POSITION;
                UploadImgUtil.build().openDefaultAlbum(imagePicker, this);
                break;
            case R.id.m_tv_submit:// 提交申请
                loadSubmitData();
                break;
        }
    }

    /**
     * 页面数据请求成功
     */
    public void onSuccess(RefundBean.RefundBeanData data) {
        showData();
        RefundBean.RefundData bean = data.getData();
        if (null == bean) {
            return;
        }
        // 商品信息
        MyOrderProductList product = bean.getProduct();
        if (null != mGoodsView && mGoodsView instanceof BaseTemplateIm) {
            ((BaseTemplateIm) mGoodsView).getDate(product, null);
        }
        mTvStatus.setText("已收到货");
        mTvYuanyin.setText("质量问题");
        // 退款金额
        refund_aomunt = String.valueOf(data.getRefund_aomunt());
        mEtMoney.setText(refund_aomunt);
        if (!StringUtil.isEmpty(refund_aomunt) && refund_aomunt.length() > 0) {
            mEtMoney.setSelection(refund_aomunt.length());
        }
        mTvMoney.setText("¥" + refund_aomunt);
        // 商品价格
        if (null != product) {
            mTvGoodsPrice.setText("商品价格：¥" + product.getPrice());
        } else {
            mTvGoodsPrice.setText("0.0");
        }
        // 订单优惠价格
        double price = bean.getPromotion_coupon_price() + bean.getPromotion_goods_price() + bean
                .getPromotion_order_price();
        mTvCouponPrice.setText("订单优惠：¥" + StringUtil.getPrice(price));
        // 余额抵扣
        mTvYuePrice.setText("余额抵扣：¥" + bean.getBalance_price());
        // 已申请退款
        mTvTuiPrice.setText("订单已退款：¥" + bean.getRefunded_fee());
        // 订单总金额
        mTvAllPrice.setText("订单总金额：¥" + bean.getPrice());
    }

    /**
     * 页面数据请求失败
     */
    public void onError(String msg) {
        showError();
    }

    /**
     * 提交成功
     */
    public void onSubmitSuccess(SubmitRefundBean.SubmitRefundData bean) {
        showData();
        EventBus.getDefault().post(new JJEvent(JJEvent.REFRESH_ORDER_LIST));
        EventBus.getDefault().post(new JJEvent(JJEvent.REFRESH_ORDER_DETAILS));
        EventBus.getDefault().post(new JJEvent(JJEvent.REFRESH_PERSON_INFO));
        RefundDetailActivity.invoke(this,
                status == STATUS_1001 ? RefundDetailActivity.STATUS_1003 : RefundDetailActivity
                        .STATUS_1004, bean.getId());
        finish();
    }

    /**
     * 提交失败
     */
    public void onSubmitError(String msg) {
        showData();
        JjToast.getInstance().toast(msg);
    }

    /**
     * 获取图片链接成功
     */
    public void onFileSuccess(UploadImgBean bean) {
        showData();
        ArrayList<UploadImgBean.UploadImgData> list = bean.getData();
        if (null != list && list.size() >= 1) {
            UploadImgBean.UploadImgData imgData = list.get(0);
            if (null != imgData) {
                String url = imgData.getUrl();
                switch (mClickPosition) {
                    case IMGONE_POSITION:
                        mImgOneUrl = url;
                        GlideUtil.getInstence().loadImage(this, mIvOne, url);
                        break;
                    case IMGTWO_POSITION:
                        mImgTwoUrl = url;
                        GlideUtil.getInstence().loadImage(this, mIvTwo, url);
                        break;
                    case IMGTHREE_POSITION:
                        mImgThreeUrl = url;
                        GlideUtil.getInstence().loadImage(this, mIvThree, url);
                        break;
                }
            }
        }
    }

    /**
     * 获取图片链接失败
     */
    public void onFileError(String msg) {
        showData();
        JjToast.getInstance().toast(msg);
    }

    public long order_no() {
        return order_no;
    }

    public int item_id() {
        return item_id;
    }

    public String reason() {
        if (null != mTvYuanyin) {
            return mTvYuanyin.getText().toString();
        }
        return "";
    }

    public String refund_aomunt() {
        return refund_aomunt;
    }

    public String t_price() {
        if (null != mEtMoney) {
            return mEtMoney.getText().toString();
        }
        return "";
    }

    public String note() {
        if (null != mEtContent) {
            return mEtContent.getText().toString();
        }
        return "";
    }

    public String card_img_a() {
        return mImgOneUrl;
    }

    public String card_img_b() {
        return mImgTwoUrl;
    }

    public String card_img_c() {
        return mImgThreeUrl;
    }

    public File file() {
        return file;
    }

    public int is_receive_goods() {
        if (null != mTvStatus) {
            if (mTvStatus.getText().toString().equals("未收到货")) {
                return 0;
            }
        }
        return 1;
    }

    // 获取从相册选取的图片
    public void getSelectImg(Uri uri) {
        file = null;
        try {
            file = FileUtilLM.getUriToFile(uri, this);
            loadUploadImg();
        } catch (Exception e) {
            JjLog.e("转化异常 = " + e.getMessage());
        }
    }

    // 显示货物状态、退款原因弹框 true: 货物状态   false: 退款原因
    private void showSelectData(final boolean isStatus) {
        mPickerData.clear();
        if (isStatus) {
            mPickerData.add("已收到货");
            mPickerData.add("未收到货");
        } else {
            mPickerData.add("质量问题");
            mPickerData.add("买错号");
        }
        mPickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (isStatus) {
                    if (null != mTvStatus) {
                        mTvStatus.setText(mPickerData.get(options1));
                    }
                    return;
                }
                if (null != mTvYuanyin) {
                    mTvYuanyin.setText(mPickerData.get(options1));
                }

            }
        }).setLayoutRes(R.layout.pickerview_tui_layout, new CustomListener() {
            @Override
            public void customLayout(View v) {
                //自定义布局中的控件初始化及事件处理
                final TextView tvTitle = v.findViewById(R.id.m_tv_select_title);
                final TextView tvSubmit = v.findViewById(R.id.m_tv_select_sure);
                tvTitle.setText(isStatus ? "请选择货物状态" : "请选择退货原因");
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPickerView.returnData();
                        mPickerView.dismiss();
                    }
                });
            }
        })
                .setDividerColor(Color.RED)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setDividerType(WheelView.DividerType.WRAP)
                .setContentTextSize(20)
                .isDialog(true)
                .build();
        mPickerView.setPicker(mPickerData);//添加数据
        mPickerView.show();
    }

    private void setImgWh(ImageView img, int wh) {
        if (null == img) {
            return;
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) img.getLayoutParams();
        params.width = wh;
        params.height = wh;
        img.setLayoutParams(params);
    }

    private void loading() {
        if (null == mViewLoading || null == mViewError) {
            return;
        }
        mViewLoading.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);
    }

    private void showData() {
        if (null == mViewLoading || null == mViewError || null == mLayoutScroll) {
            return;
        }
        mLayoutScroll.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }

    private void showError() {
        if (null == mViewLoading || null == mViewError || null == mLayoutScroll) {
            return;
        }
        mViewError.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mLayoutScroll.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imagePicker = null;
        if (null != mPickerView && mPickerView.isShowing()) {
            mPickerView.dismiss();
        }
        if (null != mPickerData) {
            mPickerData.clear();
        }
    }
}
