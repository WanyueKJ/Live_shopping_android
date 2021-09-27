package com.wanyue.live.http;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.http.BaseHttpCallBack;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.http.HttpClient;
public class LiveShopAPI {


    /*获取直播间在售数量*/
    public static void getShopSaleNum(String liveUid, BaseHttpCallBack httpCallBack){
        HttpClient.getInstance().get( LiveHttpConsts.SHOP_SALE_NUMS, LiveHttpConsts.SHOP_SALE_NUMS)
                .params("liveuid",liveUid)
                .execute(httpCallBack);
    }



    /*获取直播间在售商品*/
    public static void getSale(int p, String liveUid, HttpCallback callback) {
        HttpClient.getInstance().get(LiveHttpConsts.GET_SALE, LiveHttpConsts.GET_SALE)
                .params("page", p)
                .params("liveuid", liveUid)
                .execute(callback);
    }

    /*添加或者移除在售商品*/
    public static void shopSetSale(String goodsid, int issale, HttpCallback callback) {
        HttpClient.getInstance().post(LiveHttpConsts.SET_SALE, LiveHttpConsts.SET_SALE)
                .params("productid", goodsid)
                .params("issale", issale)
                .execute(callback);
    }


    /**
     * 直播间添加商品 搜索商品
     *
     */


    public static void searchLiveGoodsList(int p, String keyword, HttpCallback callback) {
        HttpClient.getInstance().get("Shop.searchShopGoods", LiveHttpConsts.SEARCH_LIVE_GOODS_LIST)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("token",
                        CommonAppConfig.getToken())
                .params("keywords", keyword)
                .params("p", p)
                .execute(callback);
    }


    /**
     * 主播获取直播间的商品
     */

    public static void getLiveGoods(int p, String keyword, BaseHttpCallBack callback) {
        HttpClient.getInstance().get( LiveHttpConsts.GET_SHOP_LIST, LiveHttpConsts.GET_SHOP_LIST)
                .params("keyword", keyword)
                .params("page", p)
                .execute(callback);
    }


    /**
     * 主播获取直播间的商品数
     */

    public static void getLiveGoodsNums(String stream,BaseHttpCallBack baseHttpCallBack){
        HttpClient.getInstance().get( LiveHttpConsts.LIVE_GOODS_NUMS, LiveHttpConsts.LIVE_GOODS_NUMS)
                .params("stream", stream)
                .execute(baseHttpCallBack);
    }



}
