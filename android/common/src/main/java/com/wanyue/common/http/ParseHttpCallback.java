package com.wanyue.common.http;

import android.app.Dialog;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.wanyue.common.R;
import com.wanyue.common.utils.JsonUtil;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.ToastUtil;
import java.lang.reflect.ParameterizedType;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;


public abstract class ParseHttpCallback<T> extends BaseHttpCallBack {
    private Class<T> mCs;
    public ParseHttpCallback() {
        mCs= (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    @Override
    public JsonBean convertResponse(okhttp3.Response response) throws Throwable {
        String jsonString=response.body().string();
        return JSON.parseObject(jsonString, JsonBean.class);
    }

    @Override
    public void onSuccess2(Response<JsonBean> response) {
        try {
            JsonBean bean = response.body();
            if (bean == null) {
                onError();
                return;
            }
            if(!checkingStatus(bean)){
                return;
            }
            String data=bean.getData();
            L.e("ParseHttpCallback ，data ="+data);
            int jsontype= JsonUtil.getJsonType(data);
            if(jsontype== JsonUtil.JSON_TYPE_OBJECT){
                T t=null;
                if(mCs== JSONObject.class){
                    t=(T)JSON.parseObject(data);
                }else if(mCs== JSONArray.class){
                    t=(T)JSON.parseArray(data);
                }
                else{
                    t=JSON.parseObject(data,mCs);
                }
                onSuccess(bean.getStatus(),bean.getMsg(),t);
            }else if(jsontype== JsonUtil.JSON_TYPE_NULL){
                onSuccess(bean.getStatus(),bean.getMsg(),null);
            }else{
                L.e("ParseHttpCallback ，onError（）data not a JSONObject");
                onError();
            }
        }catch (Exception e){
            e.printStackTrace();
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

    public abstract void onSuccess(int code, String msg, T info);

    @Override
    public void onStart(Request<JsonBean, ? extends Request> request) {
        onStart();
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
