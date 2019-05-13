package com.jjshop.ui.activity.person;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.base.BaseActivity;
import com.jjshop.bean.BaseBean;
import com.jjshop.bean.ExpressBean;
import com.jjshop.bean.RefundDetailBean;
import com.jjshop.dialog.SelectWuliuDialog;
import com.jjshop.template_view.BaseTemplateIm;
import com.jjshop.ui.presenter.RefundDetailPresenter;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.XNUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：张国庆
 * 时间：2018/9/20
 */

public class RefundDetailActivity extends BaseActivity<RefundDetailPresenter> {
    @BindView(R.id.m_tv_title)
    TextView mTvTitle;
    @BindView(R.id.m_iv_status)
    ImageView mIvStatus;
    @BindView(R.id.m_tv_status)
    TextView mTvStatus;
    @BindView(R.id.m_layout_goods)
    LinearLayout mLayoutGoods;
    @BindView(R.id.m_tv_tui_yuanyin)
    TextView mTvTuiYuanyin;
    @BindView(R.id.m_tv_shen_tui_money)
    TextView mTvShenTuiMoney;
    @BindView(R.id.m_tv_shi_tui_money)
    TextView mTvShiTuiMoney;
    @BindView(R.id.m_tv_tui_time)
    TextView mTvTuiTime;
    @BindView(R.id.m_view_loading)
    View mViewLoading;
    @BindView(R.id.m_view_empty)
    View mViewEmpty;
    @BindView(R.id.m_view_error)
    View mViewError;
    @BindView(R.id.m_layout_sv)
    ScrollView mLayoutScroll;
    @BindView(R.id.m_tv_address_name)
    TextView mTvAddressName;
    @BindView(R.id.m_tv_address_mobile)
    TextView mTvAddressMobile;
    @BindView(R.id.m_tv_address)
    TextView mTvAddress;
    @BindView(R.id.m_layout_address)
    RelativeLayout mLayoutAddress;
    @BindView(R.id.m_tv_wuliu_info)
    TextView mTvWuliuInfo;
    @BindView(R.id.m_layout_wuliu)
    RelativeLayout mLayoutWuliu;
    @BindView(R.id.m_tv_no_yuanyin)
    TextView mTvNoYuanyin;
    @BindView(R.id.m_tv_tui_info_title)
    TextView mTvTuiInfoTitle;

    public static final int STATUS_1003 = 1003;// 退款
    public static final int STATUS_1004 = 1004;// 退货
    @BindView(R.id.m_tv_yue_tui_money)
    TextView mTvYueTuiMoney;
    @BindView(R.id.m_tv_weixin_tui_money)
    TextView mTvWeixinTuiMoney;

    private View mGoodsView;
    private int status;
    private int service_id;
    private List<ExpressBean> mExpresssList;
    private String company_code = "", express_no = "";

    @Override
    protected int setLayoutId() {
        return R.layout.activity_refund_detail_layout;
    }

    @Override
    protected RefundDetailPresenter getPresenter() {
        return new RefundDetailPresenter(this);
    }

    public static void invoke(Context context, int status, int service_id) {
        Intent intent = new Intent(context, RefundDetailActivity.class);
        intent.putExtra("status", status);
        intent.putExtra("service_id", service_id);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        if (getIntent() != null) {
            status = getIntent().getIntExtra("status", 0);
            service_id = getIntent().getIntExtra("service_id", 0);
        }
        mTvTitle.setText(status == STATUS_1003 ? "退款详情" : "退货详情");
        mTvTuiInfoTitle.setText(status == STATUS_1003 ? "退款信息" : "退货信息");
        mGoodsView = LayoutInflater.from(this).inflate(R.layout.template_order_item_view_layout,
                null);
        mLayoutGoods.addView(mGoodsView);

        loadData();
    }

    // 加载详情数据
    public void loadData() {
        if (null != mPresenter) {
            loading();
            if (status == STATUS_1003) {
                mPresenter.loadTuikuanData();
                return;
            }
            mPresenter.loadTuihuoData();

        }
    }

    // 加载提交物流
    public void loadSubmitData(String company_code, String express_no) {
        if (null != mPresenter) {
            this.company_code = company_code;
            this.express_no = express_no;
            mPresenter.loadSubmitData();

        }
    }

    @OnClick({R.id.m_title_back, R.id.m_tv_kefu, R.id.m_layout_kefu, R.id.m_layout_wuliu, R.id
            .m_tv_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_title_back:
                finish();
                break;
            case R.id.m_layout_wuliu:
                SelectWuliuDialog.build().showView(getSupportFragmentManager(), mExpresssList);
                break;
            case R.id.m_tv_kefu:
            case R.id.m_layout_kefu:
                XNUtil.getXNUtil().openChat(this, null);
                break;
            case R.id.m_tv_refresh:
                loadData();
                break;
        }
    }

    // 详情数据获取成功
    public void onSuccessDetails(RefundDetailBean.RefundDetailData bean) {
        showData();
        // 物流公司
        mExpresssList = bean.getExpress();
        RefundDetailBean.RefundDetailInfo info = bean.getInfo();
        if (null != info) {
            setStatus(info.getExamined_status(), info.getStatus(), info.getNote());

            // 退款地址信息
            RefundDetailBean.RefundDetailSupplier addressBean = bean.getSupplier();
            if (info.getExamined_status() == 1 && null != addressBean && !StringUtil.isEmpty
                    (addressBean.getReturn_phone())) {
                mLayoutAddress.setVisibility(View.VISIBLE);
                mTvAddressName.setText("收货人:" + addressBean.getReturn_linkman());
                mTvAddressMobile.setText("手机号:"+addressBean.getReturn_phone());
                mTvAddress.setText(addressBean.getReturn_address());
            } else {
                mLayoutAddress.setVisibility(View.GONE);
            }

            mTvTuiYuanyin.setText("退款原因：" + info.getReason());
            mTvShenTuiMoney.setText("申请退款金额：¥" + info.getBack_money());
            mTvShiTuiMoney.setText("总退款金额：¥" + info.getReal_back_money());
            mTvYueTuiMoney.setText("退回至账户余额：¥" + info.getReal_balance_price());
            mTvWeixinTuiMoney.setText("退回至微信支付：¥"+info.getReal_wx_refund_price());
            mTvTuiTime.setText("申请时间：" + info.getUpdated_at());

            if (status == STATUS_1003) {
                mLayoutWuliu.setVisibility(View.GONE);
            } else {
                // 物流信息
                if (info.getStatus() == 1 && StringUtil.isEmpty(info.getExpress_num())) {// 显示物流弹框
                    mLayoutWuliu.setEnabled(true);
                    mLayoutWuliu.setClickable(true);
                    mLayoutWuliu.setVisibility(View.VISIBLE);
                    mTvWuliuInfo.setText("");
                    SelectWuliuDialog.build().showView(getSupportFragmentManager(), mExpresssList);
                } else if (info.getExamined_status() == 1 && null != addressBean && !StringUtil
                        .isEmpty(addressBean.getReturn_phone())) {
                    mLayoutWuliu.setEnabled(false);
                    mLayoutWuliu.setClickable(false);
                    mLayoutWuliu.setVisibility(View.VISIBLE);
                    mTvWuliuInfo.setText(info.getExpress_company() + "  " + info.getExpress_num());
                }
            }
        }
        // 商品信息
        if (null != mGoodsView && mGoodsView instanceof BaseTemplateIm) {
            ((BaseTemplateIm) mGoodsView).getDate(bean.getProduct(), null);
        }
    }

    // 详情数据获取失败
    public void onErrorDetails(String msg) {
        showError();
    }

    // 提交物流成功
    public void onSubmitSuccess(BaseBean bean) {
        loadData();
    }

    // 提交物流失败
    public void onErrorSubmit(String msg) {
        JjToast.getInstance().toast(msg);
    }

    public int service_id() {
        return service_id;
    }

    public String company_code() {
        return company_code;
    }

    public String express_no() {
        return express_no;
    }

    // 设置顶部的状态
    private void setStatus(int examined_status, int status, String note) {
        String statusContent = "";
        int statucIcon = R.mipmap.img_order_dfk_icon;
        int statusTvColor = getResources().getColor(R.color.color_f23030);
        mTvNoYuanyin.setVisibility(View.GONE);
        switch (examined_status) {
            case 0:
                statusContent = "请等待商家处理";
                break;
            case 2:
                statusContent = "商家已拒绝，您需要及时处理";
                mTvNoYuanyin.setVisibility(View.VISIBLE);
                mTvNoYuanyin.setText(note == null ? "" : note);
                break;
            default:
                switch (status) {
                    case 1:
                        statusContent = "已审核通过待归还商品";
                        break;
                    case 2:
                        statusContent = this.status == STATUS_1003 ? "已审核通过等待退款" : "商品已归还等待退款";
                        break;
                    case 3:
                    case 4:
                        statusTvColor = getResources().getColor(R.color.color_2897d4);
                        statucIcon = R.mipmap.img_order_yfk_icon;
                        statusContent = "退款成功";
                        mLayoutWuliu.setVisibility(View.GONE);
                        mLayoutAddress.setVisibility(View.GONE);
                        break;
                    default:
                        statusContent = this.status == STATUS_1003 ? "已审核通过等待退款" : "已审核通过待归还商品";
                        break;
                }
                break;
        }
        mIvStatus.setImageResource(statucIcon);
        mTvStatus.setText(statusContent);
        mTvStatus.setTextColor(statusTvColor);
    }

    private void loading() {
        mViewLoading.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);
    }

    private void showData() {
        mLayoutScroll.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
    }

    private void showError() {
        mViewError.setVisibility(View.VISIBLE);
        mViewLoading.setVisibility(View.GONE);
        mLayoutScroll.setVisibility(View.GONE);
    }
}
