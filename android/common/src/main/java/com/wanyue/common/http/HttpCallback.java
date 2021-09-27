package com.wanyue.common.http;

import android.app.Dialog;
import com.alibaba.fastjson.JSON;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.wanyue.common.R;
import com.wanyue.common.utils.JsonUtil;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.ToastUtil;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by  on 2017/8/7.
 */


public abstract class HttpCallback extends BaseHttpCallBack {

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
        int jsontype=JsonUtil.getJsonType(dataString);
        if(jsontype== JsonUtil.JSON_TYPE_OBJECT){
            String[]data={dataString};
            onSuccess(bean.getStatus(),bean.getMsg(),data);
        }else if(jsontype==JsonUtil.JSON_TYPE_ARRAY){
            String[]stringList=JSON.parseObject(dataString,String[].class);
            onSuccess(bean.getStatus(),bean.getMsg(),stringList);
        }else if(jsontype==JsonUtil.JSON_TYPE_NULL){
            onSuccess(bean.getStatus(),bean.getMsg(),null);
        }else{
            onError();
        }
    }

    public abstract void onSuccess(int code, String msg, String[] info);



}
