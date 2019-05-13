package com.jjshop.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjshop.R;
import com.jjshop.utils.glide_img.GlideUtil;
import com.jjshop.utils.UIUtils;
import com.jjshop.utils.glide_img.RoundedCornersTransformation;

/**
 * Created by Administrator on 2018/4/5.
 */

public class JJmailImageShareDialog extends AppCompatDialogFragment {

    public interface OnJJmailShareClickListener{
        void left();
        void right();
    }

    public JJmailImageShareDialog() {
    }

    private OnJJmailShareClickListener onJJmailShareClickListener;
    public void setOnJJmailShareClickListener(OnJJmailShareClickListener onJJmailShareClickListener){
        this.onJJmailShareClickListener = onJJmailShareClickListener;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        String img = "";
        if (bundle !=null){
            img = bundle.getString("img");
        }
        Dialog dialog = new Dialog(getContext(), R.style.bottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_jjmail_share_img);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        setCancelable(false);

        ImageView mIvImageShare = (ImageView) dialog.findViewById(R.id.iv_share);
        ImageView mIvImageClose = (ImageView) dialog.findViewById(R.id.iv_close);
        TextView mTvLeft = (TextView) dialog.findViewById(R.id.tv_left);
        TextView mTvRight = (TextView) dialog.findViewById(R.id.tv_right);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIvImageShare.getLayoutParams();
        params.width = UIUtils.getWidth() - UIUtils.dip2px(40);
        params.height = params.width * 661 / 408;
        mIvImageShare.setLayoutParams(params);

        GlideUtil.getInstence().loadRoundImage(getContext(), mIvImageShare, img,
                RoundedCornersTransformation.CornerType.TOP);

        mIvImageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mTvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onJJmailShareClickListener != null){
                    onJJmailShareClickListener.left();
                }
                dismiss();
            }
        });


        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onJJmailShareClickListener != null){
                    onJJmailShareClickListener.right();
                }
                dismiss();
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
}
