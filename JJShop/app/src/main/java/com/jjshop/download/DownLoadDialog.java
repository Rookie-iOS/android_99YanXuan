package com.jjshop.download;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.listener.OnCommonCallBackIm;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.InvokeMarketUtil;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.PermissionUtil;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.httputil.HttpHelper;

import java.io.File;

/**
 * 作者：张国庆
 * 时间：2018/7/11
 */

public class DownLoadDialog {

    private static volatile DownLoadDialog dialog = null;

    private DownLoadDialog(){}

    public static DownLoadDialog getInstence(){
        if(null == dialog){
            synchronized (DownLoadDialog.class){
                if(null == dialog){
                    dialog = new DownLoadDialog();
                }
            }
        }
        return dialog;
    }

    public static final int UPDATE_TRUE = 1;
    public static final int UPDATE_FALSE = 2;
    private final String APK_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private final String APK_NAME = "99Yanxuan.apk";
    private Context mContext;
    private File mApkFile;

    private Dialog mDialog;
    private TextView tvTitle, tvInfo, tvLeft, tvRight, tvPro, tvSetPermission;
    private TextView tvLoadingLeft, tvLoadingRight, tvDownMsg;
    private ProgressBar progressBar;
    private LinearLayout layoutDownload, layoutUpdate;
    private RelativeLayout layoutPermission;
    private CheckBox mCheckBox;

    public void show(final Context context, final int updateType, final String url, final String info, String version){
        if(null != mDialog && mDialog.isShowing()){
            mDialog.dismiss();
        }
        mDialog = new Dialog(context, R.style.all_dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_download_layout, null);
        mDialog.setContentView(view);
        mDialog.show();
        mDialog.setCancelable(false);
        layoutDownload = view.findViewById(R.id.m_layout_download);
        layoutUpdate = view.findViewById(R.id.m_layout_update);
        tvTitle = view.findViewById(R.id.m_tv_title);
        tvInfo = view.findViewById(R.id.m_tv_info);
        mCheckBox = view.findViewById(R.id.m_checkBox);
        tvLeft = view.findViewById(R.id.m_tv_left);
        tvRight = view.findViewById(R.id.m_tv_right);
        //下载相关初始化
        progressBar = view.findViewById(R.id.m_progress);
        tvPro = view.findViewById(R.id.m_tv_pro);
        tvDownMsg = view.findViewById(R.id.m_tv_woanload_msg);
        tvLoadingLeft = view.findViewById(R.id.m_tv_loading_left);
        tvLoadingRight = view.findViewById(R.id.m_tv_loading_right);
        // 下载完成打开未知来源权限相关
        layoutPermission = view.findViewById(R.id.m_layout_permission);
        tvSetPermission = view.findViewById(R.id.m_tv_set_permission);
        // 初始化数据
        tvTitle.setText("发现v" + version  + "版本更新,更新内容:");
        tvInfo.setText(info);
        // 是否强制更新
        if(updateType == UPDATE_TRUE){
            tvLeft.setText("立即退出");
            tvLoadingLeft.setText("立即退出");
            mCheckBox.setVisibility(View.GONE);
        }else{
            tvLeft.setText("稍后再说");
            tvLoadingLeft.setText("稍后再说");
            if(PreUtils.getBoolean(context, PreUtils.IS_SHOW_DOWNLOAD, false)){
                mCheckBox.setVisibility(View.GONE);
            }else{
                mCheckBox.setVisibility(View.VISIBLE);
            }
        }

        // 提示更新的左边按钮
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(updateType == UPDATE_TRUE){// 如果是强制更新，直接退出app
                    System.exit(0);
                }
            }
        });

        // 去下载或者去安装
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(InvokeMarketUtil.build().invokeMarket(context)){// 跳转到对应市场
//                    dismiss();
//                    return;
//                }
                if(tvRight.getText().toString().equals("去安装")){
                    installApk();
                    return;
                }

                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    JjToast.getInstance().toast("SD卡不可用");
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//如果是6.0以上的
                    if(!PermissionUtil.build().checkPermission(context, PermissionUtil.READ_EXTERNAL_STORAGE)){
                        return;
                    }
                    if(!PermissionUtil.build().checkPermission(context, PermissionUtil.WRITE_EXTERNAL_STORAGE)){
                        return;
                    }
                    setDownloadStyle(context, url);
                    return;
                }
                setDownloadStyle(context, url);

            }
        });

        // 是否不再提示更新
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                PreUtils.setBoolean(context, PreUtils.IS_SHOW_DOWNLOAD, b);
            }
        });

        // 设置未知来源权限
        tvSetPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionUtil.build().settingPackageInstall(context);
            }
        });

        // 下载失败时的左边按钮
        tvLoadingLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(updateType == UPDATE_TRUE){// 如果是强制更新，直接退出app
                    System.exit(0);
                }
            }
        });

        // 下载失败时的重新下载按钮
        tvLoadingRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvDownMsg.setText("新版本正在更新请稍后...");
                tvLoadingLeft.setVisibility(View.GONE);
                tvLoadingRight.setVisibility(View.GONE);
                setDownloadStyle(context, url);
            }
        });
    }

    public void dismiss(){
        if(null != mDialog && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

    // 点击升级显示下载中布局
    private void setDownloadStyle(Context context, String url){
        layoutDownload.setVisibility(View.VISIBLE);
        layoutUpdate.setVisibility(View.GONE);
        progressBar.setProgress(0);
        tvPro.setText("0%");
        downloadApk(context, url);
    }

    /**
     * 下载apk
     * @param context
     * @param url       apk地址
     */
    private void downloadApk(final Context context, String url){
        HttpHelper.bulid().getRequestFile(url, APK_PATH, APK_NAME, new OnCommonCallBackIm() {
            @Override
            public void onSuccess(Object o) {
                if(null == context || null == o || !(o instanceof File)){
                    return;
                }
                final File file = (File) o;
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mContext = context;
                        mApkFile = file;
                        if(!PermissionUtil.build().canRequestPackageInstalls(context)){
                            layoutUpdate.setVisibility(View.GONE);
                            layoutDownload.setVisibility(View.GONE);
                            layoutPermission.setVisibility(View.VISIBLE);
                            return;
                        }
                        if(initLayout()){
                            CommonUtils.build().installApk(context, file);
                        }
                    }
                });

            }

            @Override
            public void onProgress(float progress, long total) {
                if(null == context || null == progressBar || null == tvPro){
                    return;
                }
                int pro = (int) (progress * 100);
                progressBar.setProgress(pro);
                tvPro.setText(pro + "%");

            }

            @Override
            public void onError(String msg) {
                if(null == context){
                    return;
                }
                if(null != tvDownMsg && null != tvLoadingLeft && null != tvLoadingRight){
                    tvDownMsg.setText("下载失败");
                    tvLoadingLeft.setVisibility(View.VISIBLE);
                    tvLoadingRight.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    // 安装APK
    public void installApk(){
        if(null == mContext || null == mApkFile){
            return;
        }
        if(!PermissionUtil.build().canRequestPackageInstalls(mContext)){
            return;
        }
        if(!initLayout()){
            return;
        }
        // 安装apk
        CommonUtils.build().installApk(mContext, mApkFile);
    }

    // 显示提示升级布局
    private boolean initLayout(){
        if(null == tvRight || null == layoutUpdate || null == layoutDownload || null == layoutPermission){
            return false;
        }
        tvRight.setText("去安装");
        layoutUpdate.setVisibility(View.VISIBLE);
        layoutDownload.setVisibility(View.GONE);
        layoutPermission.setVisibility(View.GONE);
        return true;
    }

    // 删除老的apk
    public void delOldApk(Context context){
        if(null == context){
            return;
        }
        if(!PermissionUtil.build().checkPermission(context, PermissionUtil.READ_EXTERNAL_STORAGE, false)){
            return;
        }
        if(!PermissionUtil.build().checkPermission(context, PermissionUtil.WRITE_EXTERNAL_STORAGE, false)){
            return;
        }
        File dir = new File(APK_PATH);
        if (!dir.exists()) {
            return;
        }
        File file = new File(dir, APK_NAME);
        if (file.exists()) {
            file.delete();
        }
    }
}
