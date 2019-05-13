package com.jjshop.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jjshop.bean.JJEvent;
import com.jjshop.utils.httputil.HttpUrl;
import com.jjshop.utils.JjToast;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ysx on 2018/5/19.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{

    private static final String TAG = WXPayEntryActivity.class.getSimpleName();

    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, HttpUrl.WECHAT_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {
    }


    @Override
    public void onResp(BaseResp baseResp) {
        int errCode = baseResp.errCode;
        Log.d(TAG, "errCode--->" + errCode);
        String result = "";
        if(baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (errCode) {
                case 0: // 支付成功
                    result = "支付成功";
                    EventBus.getDefault().post(new JJEvent(JJEvent.PAY_SUCCESS));
                    break;
                case -1: // 支付失败
                    result = "支付失败，请重试";
                    EventBus.getDefault().post(new JJEvent(JJEvent.PAY_FAIL));
                    break;
                case -2: // 用户取消支付
                    result = "支付取消";
                    EventBus.getDefault().post(new JJEvent(JJEvent.PAY_CANCEL));
                    break;
            }
            JjToast.getInstance().toast(result);
            finish();
        }
    }
}
