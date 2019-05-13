package com.jjshop.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.listener.OnCommonCallBackIm;
import com.jjshop.utils.CommonUtils;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.PermissionUtil;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.glide_img.GlideUtil;
import com.jjshop.utils.UIUtils;
import com.jjshop.utils.glide_img.RoundedCornersTransformation;

/**
 * 显示二维码图片
 */

public class JJmailImageSaveDialog extends AppCompatDialogFragment {

    public interface OnSaveImgListener{
        void saveSuccess(boolean saveSuccess);
    }

    public JJmailImageSaveDialog() {
    }

    private OnSaveImgListener onSaveImgListener;
    public void setOnSaveImgListener(OnSaveImgListener onSaveImgListener){
        this.onSaveImgListener = onSaveImgListener;
    }

    TextView mTvLeft;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        String img = "";
        if (bundle !=null){
            img = bundle.getString("img");
        }
        // 创建Dialog
        Dialog dialog = new Dialog(getContext(), R.style.bottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_jjmail_save_img);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        setCancelable(false);

        final ImageView mIvImageShare = (ImageView) dialog.findViewById(R.id.iv_share);
        ImageView mIvImageClose = (ImageView) dialog.findViewById(R.id.iv_close);
        mTvLeft = (TextView) dialog.findViewById(R.id.tv_left);
        mIvImageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // 设置图片的大小
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIvImageShare.getLayoutParams();
        params.width = UIUtils.getWidth() - UIUtils.dip2px(40);
        params.height = params.width * 661 / 408;
        mIvImageShare.setLayoutParams(params);

        GlideUtil.getInstence().loadRoundImage(getContext(), mIvImageShare, img,
                RoundedCornersTransformation.CornerType.TOP);

        final String finalImg = img;
        mTvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null == getContext() || !(getContext() instanceof Activity) || StringUtil.isEmpty(finalImg)){
                    return;
                }
                // 判断是否有权限
                if(!PermissionUtil.build().checkPermission(getContext(), PermissionUtil.WRITE_EXTERNAL_STORAGE)){
                    return;
                }
                if(!PermissionUtil.build().checkPermission(getContext(), PermissionUtil.READ_EXTERNAL_STORAGE)){
                    return;
                }
                mTvLeft.setText("保存中···");
                mTvLeft.setEnabled(false);
                mTvLeft.setClickable(false);
                saveImgToPic(getContext(), finalImg);
            }
        });

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);

        return dialog;
    }

    // 保存图片到相册
    public void saveImgToPic(final Context context, String img){
        if(null == context || StringUtil.isEmpty(img)){
            return;
        }
        CommonUtils.build().downLoadImgToAlbum(context, img, new OnCommonCallBackIm() {
            @Override
            public void onSuccess(Object o) {
                if(null == o || !(o instanceof Boolean)){
                    return;
                }
                boolean b = (boolean) o;
                mTvLeft.setText("保存图片到相册");
                mTvLeft.setEnabled(true);
                mTvLeft.setClickable(true);
                if(null != onSaveImgListener){
                    onSaveImgListener.saveSuccess(b);
                }
                if(b){
                    dismiss();
                }
            }

            @Override
            public void onError(String msg) {
                mTvLeft.setText("保存图片到相册");
                mTvLeft.setEnabled(true);
                mTvLeft.setClickable(true);
                JjToast.getInstance().toast("保存失败,稍后重试");
            }
        });
    }
}
