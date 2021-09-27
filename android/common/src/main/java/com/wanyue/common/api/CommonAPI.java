package com.wanyue.common.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.bean.ConfigBean;
import com.wanyue.common.bean.TxLocationPoiBean;
import com.wanyue.common.http.BaseHttpCallBack;
import com.wanyue.common.http.CommonHttpConsts;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.http.HttpClient;
import com.wanyue.common.http.JsonBean;
import com.wanyue.common.interfaces.CommonCallback;
import com.wanyue.common.server.MapBuilder;
import com.wanyue.common.server.RequestFactory;
import com.wanyue.common.upload.FileBundle;
import com.wanyue.common.utils.JsonUtil;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.MD5Util;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;

public class CommonAPI {
    public static final String CITY_LIST="city_list"; //获取城市列表
    public static final String BALANCE="user/balance"; //用户余额
    public static final String UP_IMAGE="upload/image"; //用户余额
    public static final String CONFIG="config"; //公共配置

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
        GetRequest getRequest= OkGo.<String>get("https://apis.map.qq.com/ws/geocoder/v1/")
                .params("location", lat + "," + lng)
                .params("get_poi", poi)
                .params("poi_options", "address_format=short;radius=1000;page_size=20;page_index=" + pageIndex + ";policy=5")
                .params("key", txMapAppKey)
                .params("sig", sign)
                .tag(tag);

        L.e("getRequest=="+getRequest.getParams().toString());
        getRequest.execute(new StringCallback() {
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

    /*转化为rxjava流的形式*/
    public static Observable<List<TxLocationPoiBean>> obseverAddressInfoByTxLocaitonSdk(final int poi, final int pageIndex, final  String tag){
        return Observable.create(new ObservableOnSubscribe<List<TxLocationPoiBean>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<TxLocationPoiBean>> e) throws Exception {
                getAddressInfoByTxLocaitonSdk(CommonAppConfig.getLng(), CommonAppConfig.getLat(), poi, pageIndex, tag, new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if(code==0&&info.length>0){
                            List<TxLocationPoiBean>list= JsonUtil.getJsonToList(JsonUtil.getString(info[0],"pois"),TxLocationPoiBean.class);
                            e.onNext(list);
                        }
                        e.onComplete();
                    }
                    @Override
                    public void onError() {
                        super.onError();
                        e.onComplete();
                    }
                });
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




    /**
     * QQ登录的时候 获取unionID 与PC端互通的时候用
     */
    public static void getQQLoginUnionID(String accessToken, final CommonCallback<String> commonCallback) {
        OkGo.<String>get("https://graph.qq.com/oauth2.0/me?access_token=" + accessToken + "&unionid=1")
                .tag(CommonHttpConsts.GET_QQ_LOGIN_UNION_ID)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (commonCallback != null) {
                            String data = response.body();
                            data = data.substring(data.indexOf("{"), data.lastIndexOf("}") + 1);
                            L.e("getQQLoginUnionID------>" + data);
                            JSONObject obj = JSON.parseObject(data);
                            commonCallback.callback(obj.getString("unionid"));
                        }
                    }
                });
    }


    /**
     * 充值页面，我的钻石
     */

    public static void getBalance(HttpCallback callback) {
        HttpClient.getInstance().post("getinfo", CommonHttpConsts.GET_BALANCE)
                .params("uid", CommonAppConfig.getUid())
                .params("token", CommonAppConfig.getToken())
               // .params("type", 1)
                .execute(callback);
    }


    /**
     * 用支付宝充值 的时候在服务端生成订单号
     *
     * @param callback
     */
    public static void getAliOrder(String parmas, HttpCallback callback) {
        HttpClient.getInstance().post(parmas, CommonHttpConsts.GET_ALI_ORDER)
                .execute(callback);
    }

    /**
     * 用微信支付充值 的时候在服务端生成订单号
     *
     * @param callback
     */
    public static void getWxOrder(String parmas, HttpCallback callback) {
        HttpClient.getInstance().post(parmas, CommonHttpConsts.GET_WX_ORDER)
                .execute(callback);
    }



    /**
     * google支付回调
     */

    public static void checkGooglePay(String callbackUrl, String OriginalJson, String Signature, String OrderId, String order, HttpCallback callback) {
        OkGo.<JsonBean>post(callbackUrl)
                .params("signed_data", OriginalJson)
                .params("signature", Signature)
                .params("google_orderid", OrderId)
                .params("orderid", order)
                .execute(callback);
    }


    //不做任何操作的HttpCallback
    public static final HttpCallback NO_CALLBACK = new HttpCallback() {
        @Override
        public void onSuccess(int code, String msg, String[] info) {

        }
    };

    /**
     * 上传文件 获取七牛云token的接口
     */
    public static void getUploadQiNiuToken(HttpCallback callback) {
        HttpClient.getInstance().get("Upload.getQiniuToken", CommonHttpConsts.GET_UPLOAD_QI_NIU_TOKEN)
                .params("uid", CommonAppConfig.getUid())
                .params("token", CommonAppConfig.getToken())
                .execute(callback);
    }

    public static void upload(File file, BaseHttpCallBack httpCallback){
        HttpClient.getInstance().post(UP_IMAGE,UP_IMAGE)
          .params("file",file)
         .isMultipart(true).execute(httpCallback);
    }


    public static Observable<JSONObject> upload(File file){
        L.e("file=="+file.exists());
        Map<String,Object> map= MapBuilder.factory().put("file",file).build();
        return RequestFactory.getRequestManager().valuePost(UP_IMAGE,map, JSONObject.class,false);
    }

    public static Observable<FileBundle> upload(final FileBundle fileBundle){
        Map<String,Object> map= MapBuilder.factory().put("file",fileBundle.file).build();
        return
        RequestFactory.getRequestManager().valuePost(UP_IMAGE,map, JSONObject.class,false).map(new Function<JSONObject, FileBundle>() {
            @Override
            public FileBundle apply(JSONObject jsonObject) throws Exception {
                if(jsonObject!=null){
                   fileBundle.url= jsonObject.getString("url");
                }
                return fileBundle;
            }
        });
    }


    public static void setAttention(int isFollow,String id, BaseHttpCallBack callback) {
        String url=isFollow==1?"attent/del":"attent/add";
        HttpClient.getInstance().post(url, CommonHttpConsts.SET_ATTENTION)
                .params("touid", id)
                .execute(callback);
    }


    public static void setAttention(String id, BaseHttpCallBack callback) {
        HttpClient.getInstance().post("attent/add", CommonHttpConsts.SET_ATTENTION)
                .params("touid", id)
                .execute(callback);
    }

    public static void delAttention(String id, BaseHttpCallBack callback) {
        HttpClient.getInstance().post("attent/del", CommonHttpConsts.SET_ATTENTION)
                .params("touid", id)
                .execute(callback);
    }




    /*获取城市列表*/
    public static void getCityInfo(BaseHttpCallBack baseHttpCallBack){
        HttpClient.getInstance().get(CITY_LIST,CITY_LIST)
                .execute(baseHttpCallBack);
    }
    /*获取余额*/
    public static Observable<JSONObject>getBalance(){
        return RequestFactory.getRequestManager().valueGet(BALANCE,null, JSONObject.class,false);
    }
    public static void cancel(String tag)
    {
        HttpClient.getInstance().cancel(tag);
    }

    public static Observable<ConfigBean> getConfig() {
        return RequestFactory.getRequestManager().valueGet(CONFIG,null, ConfigBean.class,false);
    }
}
