package com.wanyue.common.http;

import android.app.Dialog;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.wanyue.common.R;
import com.wanyue.common.utils.JsonUtil;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.ToastUtil;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.ParameterizedType;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

public abstract class ParseSingleHttpCallback<T> extends BaseHttpCallBack {
    private Class<T>mCs;
    private String mKey;
    public ParseSingleHttpCallback(@NotNull String key) {
        super();
        this.mKey=key;
        mCs= (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public JsonBean convertResponse(okhttp3.Response response) throws Throwable {
        String jsonString=response.body().string();
        return JSON.parseObject(jsonString, JsonBean.class);
    }

    @Override
    public void onSuccess2(Response<JsonBean> response) {
        JsonBean bean = response.body();
        if (bean == null) {
            onError();
            return;
        }
        if(!checkingStatus(bean)){
            return;
        }
        String dataString=bean.getData();
        int jsontype= JsonUtil.getJsonType(dataString);
        if(jsontype== JsonUtil.JSON_TYPE_OBJECT){
            T t=null;
            JSONObject jsonObject=JSON.parseObject(dataString);
            if(mCs==Integer.class){
             Integer data=jsonObject.getInteger(mKey);
             if(data!=null){
               onSuccess((T) data);
             }
            }else if(mCs==String.class){
              onSuccess((T) jsonObject.getString(mKey));
            }
        }else if(jsontype== JsonUtil.JSON_TYPE_NULL){
            onError();
        }else{
            L.e("ParseHttpCallback ，onError（）data not a JSONObject");
            onError();
        }
    }

    @Override
    public void onError(Response<JsonBean> response) {
        Throwable t = response.getException();
        L.e("网络请求错误---->" + t.getClass() + " : " + t.getMessage());
        if (t instanceof SocketTimeoutException || t instanceof ConnectException || t instanceof UnknownHostException || t instanceof UnknownServiceException || t instanceof SocketException) {
            ToastUtil.show(R.string.load_failure);
        }
        onError();
    }
    @Override
    public void onError() {

    }

    public abstract void onSuccess(T data);

    @Override
    public void onStart(Request<JsonBean, ? extends Request> request) {
        onStart();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onFinish() {
    }

    @Override
    public Dialog createLoadingDialog() {
        return null;
    }
    @Override
    public boolean showLoadingDialog() {
        return false;
    }
}
