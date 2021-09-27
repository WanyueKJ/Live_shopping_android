package com.wanyue.common.server;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.wanyue.common.http.CommonHttpUtil;
import com.wanyue.common.http.UrlMap;
import com.wanyue.common.server.entity.BaseOriginalResponse;
import com.wanyue.common.server.entity.BaseSingleResponse;
import com.wanyue.common.utils.DebugUtil;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import io.reactivex.Observable;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class OkGoRequestMannger implements IRequestManager {
    private static OkGoRequestMannger sOkGoRequestMannger;

    private OkGoRequestMannger() {
    }
    @Override
    public <T> Observable<List<T>> get(String url, Map<String, Object> map, Class<T> cs, boolean showMsg) {
        return CommonHttpUtil.get(url,parse(map),cs,showMsg);
    }
    @Override
    public <T> Observable<List<T>> post(String url, Map<String, Object> map, Class<T> cs, boolean showMsg) {
        return CommonHttpUtil.post(url,parse(map),cs,showMsg);
    }

    @Override
    public <T> Observable<T> valueGet(String url, Map<String, Object> map, Class<T> cs, boolean showMsg) {
        return CommonHttpUtil.getSingle(url,parse(map),cs,showMsg);
    }

    @Override
    public <T> Observable<T> valuePost(String url, Map<String, Object> map, Class<T> cs, boolean showMsg) {
        return CommonHttpUtil.postSingle(url,parse(map),cs,showMsg);
    }

    @Override
    public Observable<Boolean> commit(String url, Map<String, Object> map, boolean showMsg) {
        return CommonHttpUtil.commit(url,parse(map),showMsg);
    }

    @Override
    public Observable<BaseOriginalResponse> originalGetRequest(String url, Map<String, Object> map) {
        return CommonHttpUtil.originalGetRequest(url,parse(map));
    }


    @Override
    public Observable<BaseOriginalResponse> originalPostRequest(String url, Map<String, Object> map) {
        return CommonHttpUtil.originalPostRequest(url,parse(map));
    }

    @Override
    public void cancle(String tag) {
       String totalTag= UrlMap.getUrl(tag);
       if(!TextUtils.isEmpty(totalTag)){
           CommonHttpUtil.cancel(totalTag);
           return;
       }
        DebugUtil.sendException("tag=="+tag+"totalTag=="+totalTag);
    }

    public static OkGoRequestMannger getInstance(){
        if(sOkGoRequestMannger==null){
            synchronized (OkGoRequestMannger.class){
                sOkGoRequestMannger=new OkGoRequestMannger();
            }
        }
        return sOkGoRequestMannger;
    }

    public static HttpParams parse(Map<String, Object> map){
        if(map==null){
            return null;
        }
        HttpParams httpParams=new HttpParams();
        Set<String> set= map.keySet();
       Iterator<String> iterator= set.iterator();
       while (iterator.hasNext()){
        String key=iterator.next();
        Object value=map.get(key);
        if(value==null){
            continue;
        }
        if(value instanceof Boolean){
            httpParams.put(key,(Boolean)value);

        }else if(value instanceof Integer){
            httpParams.put(key,(Integer)value);
        }else if(value instanceof String){
            httpParams.put(key,(String)value);
        }else if(value instanceof Double){
            httpParams.put(key,(Double)value);
        }else if(value instanceof Float){
            httpParams.put(key,(Float)value);
        }else  if(value instanceof File){
            httpParams.put(key,(File)value);
        }else if(value instanceof Long){
            httpParams.put(key,(Long)value);
        }
        else if(value instanceof Character){
            httpParams.put(key,(Character)value);
        }else if(value instanceof String[]){
            String[]valueRealy= (String[]) value;
          /*  String json=StringUtil.splitJoint(valueRealy);
            L.e("json=="+json);*/
            StringBuilder stringBuilder=new StringBuilder();
            int size=valueRealy.length;
            for(int i=0;i<size;i++){
                stringBuilder.delete(0, stringBuilder.length());
                stringBuilder.append(key).
                append("[")
                .append(i)
                .append("]");
               httpParams.put(stringBuilder.toString(),valueRealy[i]);
            }
        }else if(value instanceof Map){
            Map<String,String>stringMap= (Map<String, String>) value;
            httpParams.put(key,stringMap.toString());
        }
        else{
            List<Object>array=new ArrayList<>(1);
            array.add(value);
            String json=JSON.toJSONString(value);
            L.e("json=="+json);
            httpParams.put(key,json);
        }
       }
        return httpParams;
    }

}
