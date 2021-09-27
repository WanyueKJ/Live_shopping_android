package com.wanyue.common.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpMethod;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.bean.ConfigBean;
import com.wanyue.common.business.JumpInterceptor;
import com.wanyue.common.interfaces.CommonCallback;
import com.wanyue.common.server.RxUtils;
import com.wanyue.common.server.SeverConfig;
import com.wanyue.common.server.entity.BaseArrayResponse;
import com.wanyue.common.server.entity.BaseOriginalResponse;
import com.wanyue.common.server.entity.BaseSimpleReponse;
import com.wanyue.common.server.entity.BaseSingleResponse;
import com.wanyue.common.server.generic.ParameterizedTypeImpl;
import com.wanyue.common.utils.MD5Util;
import com.wanyue.common.utils.ToastUtil;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class CommonHttpUtil {

    /**
     * 初始化
     */
    public static void init() {
        HttpClient.getInstance().init();
    }

    /**
     * 取消网络请求
     */
    public static void cancel(String tag) {
        HttpClient.getInstance().cancel(tag);
    }


    private static String appendUrl(String url) {
       return UrlMap.getUrl(url);
    }



    //不做任何操作的HttpCallback
    public static final HttpCallback NO_CALLBACK = new HttpCallback() {
        @Override
        public void onSuccess(int code, String msg, String[] info) {

        }
    };



    public static void getConfig(CommonCallback<ConfigBean> callback) {

    }









    /**
     * 使用腾讯定位sdk获取 位置信息
     *
     * @param lng 经度
     * @param lat 纬度
     * @param poi 是否要查询POI
     */
    public static void getAddressInfoByTxLocaitonSdk(final double lng, final double lat, final int poi, int pageIndex, String tag, final HttpCallback commonCallback) {
        String txMapAppKey = CommonAppConfig.getTxMapAppKey();
        String s = "/ws/geocoder/v1/?get_poi=" + poi + "&key=" + txMapAppKey + "&location=" + lat + "," + lng
                + "&poi_options=address_format=short;radius=1000;page_size=20;page_index=" + pageIndex + ";policy=5" + CommonAppConfig.getTxMapAppSecret();
        String sign = MD5Util.getMD5(s);
        OkGo.<String>get("https://apis.map.qq.com/ws/geocoder/v1/")
                .params("location", lat + "," + lng)
                .params("get_poi", poi)
                .params("poi_options", "address_format=short;radius=1000;page_size=20;page_index=" + pageIndex + ";policy=5")
                .params("key", txMapAppKey)
                .params("sig", sign)
                .tag(tag)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        JSONObject obj = JSON.parseObject(response.body());
                        if (obj != null && commonCallback != null) {
                            commonCallback.onSuccess(obj.getIntValue("status"), "", new String[]{obj.getString("result")});
                        }
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (commonCallback != null) {
                            commonCallback.onError();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (commonCallback != null) {
                            commonCallback.onFinish();
                        }
                    }
                });
    }

    /**
     * 使用腾讯地图API进行搜索
     *
     * @param lng 经度
     * @param lat 纬度
     */
    public static void searchAddressInfoByTxLocaitonSdk(final double lng, final double lat, String keyword, int pageIndex, final HttpCallback commonCallback) {

        String txMapAppKey = CommonAppConfig.getTxMapAppKey();
        String s = "/ws/place/v1/search?boundary=nearby(" + lat + "," + lng + ",1000)&key=" + txMapAppKey + "&keyword=" + keyword + "&orderby=_distance&page_index=" + pageIndex +
                "&page_size=20" + CommonAppConfig.getTxMapAppSecret();
        String sign = MD5Util.getMD5(s);
        OkGo.<String>get("https://apis.map.qq.com/ws/place/v1/search")
                .params("keyword", keyword)
                .params("boundary", "nearby(" + lat + "," + lng + ",1000)&orderby=_distance&page_size=20&page_index=" + pageIndex)
                .params("key", txMapAppKey)
                .params("sig", sign)
                .tag(CommonHttpConsts.GET_MAP_SEARCH)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        JSONObject obj = JSON.parseObject(response.body());
                        if (obj != null && commonCallback != null) {
                            commonCallback.onSuccess(obj.getIntValue("status"), "", new String[]{obj.getString("data")});
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (commonCallback != null) {
                            commonCallback.onError();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (commonCallback != null) {
                            commonCallback.onFinish();
                        }
                    }
                });
    }




    /*url 是默认的tag可以显式的取消*/
    public  static <T> Observable<List<T>> request(HttpMethod httpMethod, String url, HttpParams params, final Class<T>cs, final boolean showMsg){
        url=appendUrl(url);
        Type type =null;
        if(SeverConfig.jsonType==SeverConfig.FAST_JSON){
            type= new com.alibaba.fastjson.util.ParameterizedTypeImpl(new Type[]{cs}, null, BaseArrayResponse.class);
        }else{
            type= new ParameterizedTypeImpl(BaseArrayResponse.class, new Class[]{cs});
        }
        Observable<BaseArrayResponse<T>> observable= RxUtils.request(httpMethod, url, type,params,true);
        return observable.map(new Function<BaseArrayResponse<T>, List<T>>() {
            @Override
            public List<T> apply(BaseArrayResponse<T> baseArrayResponse) throws Exception {
                return baseArrayResponse.getData();
            }
        });
    }

    public  static <T> Observable<List<T>> get(String url, HttpParams params,Class<T>cs,boolean showMsg){
        return request(HttpMethod.GET,url,params,cs,showMsg);
    }

    public  static <T> Observable<List<T>> post(String url, HttpParams params,Class<T>cs,boolean showMsg){
        return request(HttpMethod.POST,url,params,cs,showMsg);
    }

    public  static <T> Observable<T> getSingle(String url, HttpParams params,Class<T>cs,boolean showMsg){
        return requestSingle(HttpMethod.GET,url,params,cs,showMsg);
    }

    public  static <T> Observable<T> postSingle(String url, HttpParams params,Class<T>cs,boolean showMsg){
        return requestSingle(HttpMethod.POST,url,params,cs,showMsg);
    }

    private static <T> Observable<T> requestSingle(HttpMethod httpMethod, String url, HttpParams params, Class<T> cs,final  boolean showMsg) {
        url=appendUrl(url);
        Type type =null;
        if(SeverConfig.jsonType==SeverConfig.FAST_JSON){
        }else{
            type= new ParameterizedTypeImpl(BaseSingleResponse.class, new Class[]{cs});
        }
        Observable<BaseSingleResponse<T>> observable= RxUtils.request(httpMethod, url, type,params,false);
        return  observable.map(new Function<BaseSingleResponse<T>, T>() {
            @Override
            public T apply(BaseSingleResponse<T> baseSingleResponse) throws Exception {
                if(showMsg){
                    ToastUtil.show(baseSingleResponse.getMsg());
                }
                return baseSingleResponse.getData();
            }
        });
    }


    public  static  io.reactivex.Observable<Boolean> commit(String url, HttpParams params,final boolean showMsg){
        url=appendUrl(url);
        Type type = new ParameterizedTypeImpl(BaseSimpleReponse.class, new Class[]{Void.class});
        Observable<BaseSimpleReponse> observable=RxUtils.request(HttpMethod.POST, url, type,params,false);
       return observable.map(new Function<BaseSimpleReponse, Boolean>() {
           @Override
           public Boolean apply(BaseSimpleReponse baseSimpleReponse) throws Exception {
               if(showMsg){
                   ToastUtil.show(baseSimpleReponse.getMsg());
               }
               return baseSimpleReponse.getStatus()==BaseHttpCallBack.SUCCESS;
           }
       });
    }

    public  static  Observable<BaseOriginalResponse> originalRequest(HttpMethod method,String url, HttpParams params){
        url=appendUrl(url);
        Observable<BaseOriginalResponse> observable=RxUtils.request(method, url, BaseOriginalResponse.class,params,false);
        return observable;
    }


    public  static  Observable<BaseOriginalResponse> originalGetRequest(String url, HttpParams params){
        return originalRequest(HttpMethod.GET,url,params);
    }

    public  static  Observable<BaseOriginalResponse> originalPostRequest(String url, HttpParams params){
        return originalRequest(HttpMethod.POST,url,params);
    }
}




