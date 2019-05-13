package com.jjshop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jjshop.R;
import com.jjshop.ui.activity.home.PayActivity;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.PermissionUtil;
import com.jjshop.utils.PreUtils;
import com.jjshop.utils.StringUtil;
import com.jjshop.utils.UIUtils;
import com.jjshop.utils.glide_img.GlideUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

public class HongbaoDialog extends AppCompatDialogFragment implements View.OnClickListener {

    private Context mContext;
    private ImageView erweima;
    private RelativeLayout bgcontainer;
    private static volatile HongbaoDialog commonDialog;


    public static HongbaoDialog build() {
        synchronized (CartNumDelDialog.class) {
            commonDialog = new HongbaoDialog();
        }
        return commonDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getContext(), R.style.bottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_hongbao_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        setCancelable(false);
        initView(dialog);
        return dialog;
    }

    public HongbaoDialog showView(FragmentManager manager, String kaquan) {

        Bundle bundle = new Bundle();
        bundle.putString("kaquan", kaquan);
        this.setArguments(bundle);

        FragmentTransaction ft = manager.beginTransaction();
        ft.add(commonDialog, "hongbao");
        ft.commitAllowingStateLoss();
        return commonDialog;
    }

    private void initView(Dialog dialog) {

        // 获取屏幕宽高
        int screen_w = UIUtils.getWidth();
        bgcontainer = dialog.findViewById(R.id.center_bg);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bgcontainer
                .getLayoutParams();
        params.width = screen_w * 660 / 750;
        params.height = 910 * params.width / 660;
        bgcontainer.setLayoutParams(params);

        // 关闭按钮
        dialog.findViewById(R.id.m_iv_close).setOnClickListener(this);
        // 保存并分享按钮
        ImageView save = dialog.findViewById(R.id.hongbao_save);
        RelativeLayout.LayoutParams saveParams = (RelativeLayout.LayoutParams) save
                .getLayoutParams();
        saveParams.width = params.width;
        saveParams.height = params.width * 146 / 951;
        save.setLayoutParams(saveParams);
        save.setOnClickListener(this);
        // 设置头像MarginTop
        ImageView head = dialog.findViewById(R.id.head_icon);
        GlideUtil.getInstence().loadCirleImage(mContext, head, PreUtils.getString(mContext,
                PreUtils.USER_IMG), R.mipmap.img_person_default_head);
        RelativeLayout.LayoutParams headParams = (RelativeLayout.LayoutParams) head
                .getLayoutParams();
        headParams.topMargin = 20 * params.height / 91;
        head.setLayoutParams(headParams);

        TextView name = dialog.findViewById(R.id.user_name);
        TextPaint paint = name.getPaint();
        paint.setFakeBoldText(true);
        name.setText(PreUtils.getString(mContext, PreUtils.USER_NICKNAME));

        // 生成二维码
        erweima = dialog.findViewById(R.id.er_wei_ma);
        createErweiMa();
        // 设置红包
        TextView hongbaoword = dialog.findViewById(R.id.hong_bao_word);
        TextPaint wordPaint = hongbaoword.getPaint();
        wordPaint.setFakeBoldText(true);

        Bundle arguments = getArguments();
        String hongbaoMoney = arguments.getString("kaquan");
        String attributeString = "我送了你 " + hongbaoMoney + "元 99严选商城大红包，快去购物吧~";
        SpannableString spannableString = new SpannableString(attributeString);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#fff55c"));
        spannableString.setSpan(colorSpan, attributeString.indexOf(hongbaoMoney), attributeString
                .indexOf(hongbaoMoney) + hongbaoMoney.length() + 1, Spanned
                .SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.5f), attributeString.indexOf(hongbaoMoney)
                , attributeString.indexOf(hongbaoMoney) + hongbaoMoney.length(), Spanned
                        .SPAN_EXCLUSIVE_EXCLUSIVE);
        hongbaoword.setText(spannableString);
    }

    private void createErweiMa() {

        String shop_ImgString = "https://yxds.999d.com/m/index/buys?shop=" + PreUtils.getString
                (mContext, PreUtils.SHOP_ID);
        if (StringUtil.isEmpty(shop_ImgString)) return;
        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        // 设置编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 设置容错率
        hints.put(EncodeHintType.ERROR_CORRECTION, "H");
        // 设置边距
        hints.put(EncodeHintType.MARGIN, "1");
        try {
            BitMatrix bitMatrix = new QRCodeWriter().encode(shop_ImgString, BarcodeFormat
                    .QR_CODE, 88, 88, hints);
            int[] pixels = new int[88 * 88];
            for (int y = 0; y < 88; y++) {
                for (int x = 0; x < 88; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * 88 + x] = Color.BLACK;
                    } else {
                        pixels[y * 88 + x] = Color.WHITE;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(88, 88, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, 88, 0, 0, 88, 88);
            erweima.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_iv_close:
                dismiss();
                break;
            case R.id.hongbao_save:
                saveImageForShare();
                break;
        }
    }

    private void saveImageForShare() {

        // 判断是否有权限
        if (!PermissionUtil.build().checkPermission(getContext(), PermissionUtil
                .WRITE_EXTERNAL_STORAGE)) {
            return;
        }
        if (!PermissionUtil.build().checkPermission(getContext(), PermissionUtil
                .READ_EXTERNAL_STORAGE)) {
            return;
        }
        Bitmap bm = Bitmap.createBitmap(bgcontainer.getWidth(), bgcontainer.getHeight(), Bitmap
                .Config.ARGB_8888);
        bgcontainer.draw(new Canvas(bm));
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File
                .separator + "99";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bm.compress(Bitmap.CompressFormat.PNG, 60, fos);
            fos.flush();
            fos.close();
            //把文件插入到系统图库
            MediaStore.Images.Media.insertImage(mContext.getContentResolver(), file
                    .getAbsolutePath(), fileName, null);
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                // 保存成功
                JjToast.getInstance().toast("保存图片成功");
                // 跳转微信
                UMImage image = new UMImage(mContext, bm);//bitmap文件
                image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
                image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
                image.compressFormat = Bitmap.CompressFormat.PNG;
                image.setThumb(new UMImage(mContext, bm));
                PayActivity payActivity = (PayActivity) mContext;
                new ShareAction(payActivity).setPlatform(SHARE_MEDIA.WEIXIN).withText("")
                        .withMedia(image).share();
                dismiss();
            } else {
                JjToast.getInstance().toast("保存图片失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
