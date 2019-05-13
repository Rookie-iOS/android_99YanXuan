package com.jjshop.ui.activity.person;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.adapter.TeamSaleAdapter;
import com.jjshop.base.BaseActivity;
import com.jjshop.base.BasePresenter;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.UIUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TeamSalesActivity extends BaseActivity {


    @BindView(R.id.m_tv_title)
    TextView mTvTitle;
    @BindView(R.id.left)
    Button left;
    @BindView(R.id.right)
    Button right;
    @BindView(R.id.list)
    ListView list;
    @BindView(R.id.t_search)
    LinearLayout tSearch;
    @BindView(R.id.tv_search)
    EditText tvSearch;

    private String left_ri_qi;
    private String right_ri_qi;
    private boolean is_set_left_ri_qi;
    private boolean is_set_right_ri_qi;

    private List<String>mlist;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_team_sale_layout;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    public static void invoke(Context context) {
        Intent intent = new Intent(context, TeamSalesActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();

        is_set_left_ri_qi = false;
        mTvTitle.setText("团队销售统计");
        // 获取当前日历
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        left_ri_qi = right_ri_qi = simpleDateFormat.format(date);
        // 格式化
        left.setText(left_ri_qi);
        right.setText(right_ri_qi);
        // 搜索监听
        tvSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    search(tvSearch);
                    return true;
                }
                return false;
            }
        });
        // list
        View header = View.inflate(this, R.layout.team_sale_header_layout, null);
        list.addHeaderView(header);
        EditText h_search = header.findViewById(R.id.tv_search);
        h_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH){
                    search(h_search);
                    return true;
                }
                return false;
            }
        });

        mlist = new ArrayList<>();
        mlist.add("123");
        mlist.add("456");
        TeamSaleAdapter adapter = new TeamSaleAdapter(this,mlist);
        list.setAdapter(adapter);

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                View header = list.getChildAt(0);
                if (null == header) return;
                if (header.getHeight() > UIUtils.dip2px(48)) {
                    if (header.getHeight() + header.getTop() <= UIUtils.dip2px(44)) {
                        tSearch.setVisibility(View.VISIBLE);
                    } else {
                        tSearch.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @OnClick({R.id.m_title_back, R.id.left, R.id.right})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.m_title_back:
                finish();
                break;
            case R.id.left:
                is_set_left_ri_qi = true;
                showDateDialog(view, true);
                break;
            case R.id.right:
                is_set_right_ri_qi = true;
                showDateDialog(view, false);
                break;
        }
    }

    private void showDateDialog(View view, Boolean isLeft) {

        final String dateString;
        Calendar cal = Calendar.getInstance();
        final DatePickerDialog mDialog = new DatePickerDialog(this, null,
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        //手动设置按钮
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Button riliBtn = (Button) view;
                DatePicker datePicker = mDialog.getDatePicker();
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1;
                int day = datePicker.getDayOfMonth();
                riliBtn.setText(year + "-" + String.format("%02d", month) + "-" + String.format
                        ("%02d", day));
                if (is_set_left_ri_qi && is_set_right_ri_qi) {
                    // 获取左边按钮上的日期
                    String left_string = (String) left.getText();
                    String right_string = (String) right.getText();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date d1 = sdf.parse(left_string);
                        Date d2 = sdf.parse(right_string);
                        if (getTimeDistance(d1, d2) > 30) {
                            is_set_right_ri_qi = is_set_left_ri_qi = false;
                            JjToast.getInstance().toast("两个日期之间间隔不能超过30天");
                            // 重新设置当前日期
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            //获取当前时间
                            Date date = new Date(System.currentTimeMillis());
                            riliBtn.setText(simpleDateFormat.format(date));
                            return;
                        }
                        // 开始查询
                        // TODO: 2019/2/21 查询开始。。。。。。。。


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        mDialog.show();
    }

    private int getTimeDistance(Date beginDate, Date endDate) {
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        long beginTime = beginCalendar.getTime().getTime();
        long endTime = endCalendar.getTime().getTime();
        int betweenDays = (int) ((endTime - beginTime) / (1000 * 60 * 60 * 24));
        //先算出两时间的毫秒数之差大于一天的天数
        endCalendar.add(Calendar.DAY_OF_MONTH, -betweenDays);
        //使endCalendar减去这些天数，将问题转换为两时间的毫秒数之差不足一天的情况
        endCalendar.add(Calendar.DAY_OF_MONTH, -1);//再使endCalendar减去1天
        if (beginCalendar.get(Calendar.DAY_OF_MONTH) == endCalendar.get(Calendar.DAY_OF_MONTH))
            //比较两日期的DAY_OF_MONTH是否相等
            return betweenDays + 1;    //相等说明确实跨天了
        else
            return betweenDays + 0;    //不相等说明确实未跨天
    }

    private void search(EditText editText){

        Log.e("=====123","===="+ editText.getText().toString() +"==== 开始搜索");
        // 清空搜索框文字
        editText.setText("");
    }
}
