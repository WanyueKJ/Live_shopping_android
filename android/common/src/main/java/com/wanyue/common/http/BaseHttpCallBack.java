package com.wanyue.common.http;

import android.app.Activity;
import android.app.Dialog;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.tencent.bugly.crashreport.CrashReport;
import com.wanyue.common.R;
import com.wanyue.common.business.JumpInterceptor;
import com.wanyue.common.business.acmannger.ActivityMannger;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.RouteUtil;
import com.wanyue.common.utils.ToastUtil;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

public abstract class BaseHttpCallBack extends AbsCallback<JsonBean> {
    private Dialog mLoadingDialog;

    public static final int SUCCESS=200;
    public static final int NO_LOGIN=410000;
    public static final int EXPIRE_LOGIN=410001;
    public static final int LOGIN_ERROR=410002;

    @Override
    public JsonBean convertResponse(okhttp3.Response response) throws Throwable {
        String jsonString=response.body().string();
        return JSON.parseObject(jsonString, JsonBean.class);
    }
    public static boolean isSuccess(int code){
        return SUCCESS==code;
    }

    @Override
    public void onSuccess(Response<JsonBean> response) {
        try {
            onSuccess2(response);
        }catch (Exception e){
            e.printStackTrace();
            CrashReport.postCatchedException(e);
        }
    }

    public abstract void onSuccess2(Response<JsonBean> response);

    @Override
    public void onError(Response<JsonBean> response) {
        Throwable t = response.getException();
        L.e("网络请求错误---->" + t.getClass() + " : " + t.getMessage());
        if (t instanceof SocketTimeoutException || t instanceof ConnectException || t instanceof UnknownHostException || t instanceof UnknownServiceException || t instanceof SocketException) {
            ToastUtil.show(R.string.load_failure);
        }
        disMissDialog();
        onError();
    }

    private void disMissDialog() {
        if (showLoadingDialog() && mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void onStart(Request<JsonBean, ? extends Request> request) {
        onStart();
    }

    public void onStart() {
        if (showLoadingDialog()) {
            if (mLoadingDialog == null) {
                mLoadingDialog = createLoadingDialog();
            }
            mLoadingDialog.show();
        }
    }

    @Override
    public void onFinish() {
        if (showLoadingDialog() && mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    public Dialog createLoadingDialog() {
        return null;
    }
    protected  boolean checkingStatus(JsonBean jsonBean){
        int code=jsonBean.getStatus();
        if(code==NO_LOGIN||code==EXPIRE_LOGIN||code==LOGIN_ERROR){
            JumpInterceptor.shouldInterceptor(code);
            ToastUtil.show(jsonBean.getMsg());
            return false;
        }
        return true;
    }

    public boolean showLoadingDialog() {
        return false;
    }

    public void onError() {

    }

}
