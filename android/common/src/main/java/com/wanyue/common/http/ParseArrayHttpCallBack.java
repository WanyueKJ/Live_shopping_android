package com.wanyue.common.http;

import android.app.Dialog;

import com.alibaba.fastjson.JSON;
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
import java.util.ArrayList;
import java.util.List;

public abstract class ParseArrayHttpCallBack <T> extends BaseHttpCallBack {
    private Class<T> mCs;
    public ParseArrayHttpCallBack() {
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

        String data=bean.getData();
        int jsontype= JsonUtil.getJsonType(data);
        if(jsontype== JsonUtil.JSON_TYPE_ARRAY){
            List<T> t=JSON.parseArray(data,mCs);
            onSuccess(bean.getStatus(),bean.getMsg(),t);
        }else if(jsontype==JsonUtil.JSON_TYPE_OBJECT){
           T t=JSON.parseObject(data,mCs);
           List<T> array=new ArrayList<>(1);
           array.add(t);
           onSuccess(bean.getStatus(),bean.getMsg(),array);
        }else if(jsontype==JsonUtil.JSON_TYPE_NULL){
            onSuccess(bean.getStatus(),bean.getMsg(),null);
        }else{
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

    public abstract void onSuccess(int code, String msg, List<T> info);




}
