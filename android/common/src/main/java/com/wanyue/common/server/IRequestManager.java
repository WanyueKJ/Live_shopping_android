package com.wanyue.common.server;

import com.wanyue.common.server.entity.BaseOriginalResponse;
import com.wanyue.common.server.entity.BaseSimpleReponse;
import com.wanyue.common.server.entity.BaseSingleResponse;

import java.util.List;
import java.util.Map;
import io.reactivex.Observable;

/*
   create by cfw
* */

public interface IRequestManager {
    /*get请求*/
    <T> Observable<List<T>> get(String url, Map<String, Object> map, Class<T> cs, boolean showMsg);
    /*post请求*/
    <T> Observable<List<T>> post(String url, Map<String, Object> map, Class<T> cs, boolean showMsg);
    /*拆出头元素的作为最终数据的post请求*/
    <T> Observable<T> valueGet(String url, Map<String, Object> map, Class<T> cs, boolean showMsg);
    /*拆出头元素的作为最终数据的get请求*/
    <T> Observable<T> valuePost(String url, Map<String, Object> map, Class<T> cs, boolean showMsg);
    /*提交请求，针对只需要关注是否成功*/
    Observable<Boolean> commit(String url, Map<String, Object> map, boolean showMsg);
    /*返回原始的jsonObjec*/
    public Observable<BaseOriginalResponse> originalGetRequest(String url, Map<String, Object> map);
    public Observable<BaseOriginalResponse> originalPostRequest(String url, Map<String, Object> map);
    /*取消请求*/
    void cancle(String tag);
}