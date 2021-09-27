package com.wanyue.shop.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.bean.GoodsBean;
import com.wanyue.common.http.BaseHttpCallBack;
import com.wanyue.common.http.HttpClient;
import com.wanyue.common.server.MapBuilder;
import com.wanyue.common.server.RequestFactory;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.shop.bean.AddressCommitBean;
import com.wanyue.shop.bean.AddressInfoBean;
import com.wanyue.shop.bean.GoodsParseBean;
import com.wanyue.shop.bean.GoodsSearchArgs;
import com.wanyue.shop.bean.GoodsTypeBean;
import com.wanyue.shop.bean.KeywordBean;
import com.wanyue.shop.bean.OrderBean;
import com.wanyue.shop.bean.OrderConfirmBean;
import com.wanyue.shop.bean.OrderStatementBean;
import com.wanyue.shop.bean.RefundCommitBean;
import com.wanyue.shop.bean.ShopcartParseBean;
import com.wanyue.shop.bean.StoreGoodsBean;
import com.wanyue.shop.evaluate.bean.EvaluateBean;
import com.wanyue.shop.evaluate.bean.EvaluateCommitBean;

import java.util.List;
import java.util.Map;
import io.reactivex.Observable;

public class ShopAPI {
    public static final String PRODUCT_LIST="products"; //商品列表
    public static final String PRODUCT_HOT="product/hot"; //热门商品
    public static final String PRODUCT_DETAIL="product/detail/"; //商品详情
    public static final String CART_COUNT="cart/count"; //购物车数量
    public static final String SHOP_CART_LIST="cart/list"; //购物车列表
    public static final String ADD_SHOP_CART="cart/add"; //添加购物车
    public static final String DELETE_SHOP_CART="cart/del"; //删除添加购物车
    public static final String MODIFY_CART_NUM="cart/num"; //修改购物车数量
    public static final String PRODUCT_CODE="product/code/"; //获取二维码
    public static final String PRODUCT_BATCH_COLLECT="collect/all"; //批量收藏
    public static final String DEFAULT_ADDRESS="address/default"; //获取默认地址
    public static final String SET_DEFAULT_ADDRESS="address/default/set"; //获取默认地址
    public static final String ADDRESS_LIST="address/list"; //获取默认地址
    public static final String ADDRESS_DELETE="address/del"; //删除地址
    public static final String ADD_UPDATE_ADDR="address/edit"; //删除地址
    public static final String ORDER_CONFIRM="order/confirm"; //确认订单
    public static final String GET_COUPON_LIST="coupons"; //确认订单
    public static final String RECEIVE_COUPON="coupon/receive"; //领取优惠券
    public static final String ORDER_COUPON_LIST="coupons/order/"; //提交订单选择优惠券
    public static final String ORDER_COMPUTED="order/computed/"; //计算订单价格
    public static final String ORDER_LIST="order/list"; //订单列表
    public static final String ORDER_DATA="order/data"; //订单统计数据
    public static final String ORDER_DETAIL="order/detail/"; //订单详情
    public static final String ORDER_CANCEL="order/cancel"; //订单取消
    public static final String ORDER_PAY="order/pay"; //订单支付
    public static final String ORDER_CREATE="order/create/"; //订单创建
    public static final String COLLECT_USER_LIST="collect/user"; //商品收藏列表
    public static final String COLLECT_DELETE="collect/del"; //收藏删除
    public static final String ORDER_COMMENT="order/comment"; //订单评价
    public static final String DEL_ORDER="order/del"; //订单删除
    public static final String REFUND_REASON="order/refund/reason"; //退款理由
    public static final String REFUND_VERIFY="order/refund/verify"; //申请退款
    public static final String VIEW_LOGISTICS="order/express/"; //查看物流
    public static final String ORDER_AGAIN="order/again"; //再来一单
    public static final String EVALUATE_LIST="reply/list/"; //再来一单
    public static final String EVALUATE_CONFIG="reply/config/"; //商品评价统计
    public static final String SEARCH_KEYWORD="search/keyword"; //商品搜索关键词
    public static final String SHOP_SALE="shopsale"; //在售列表
    public static final String SHOP_SERVICE="shopservice"; //在线客服
    public static final String ORDER_TAKE="order/take"; //确认收货


    /*获取商品列表*

    sid	否		二级分类编号
cid	否		一级分类编号（！）
keyword	否		搜索
priceOrder	否		价格排序
salesOrder	否		销量排序
news	否		是否新品
page	否		分页参数起始值
limit	否		分页数步长值
    /
     */



   //商品列表
    public static Observable<List<GoodsTypeBean>> getProductList(GoodsSearchArgs goodsSearchArgs, int p){
        if(goodsSearchArgs==null){
           goodsSearchArgs=new GoodsSearchArgs();
        }
        Map<String,Object> map= MapBuilder.factory().put("page",p).
                put("sid",goodsSearchArgs.sid).
                put("cid",goodsSearchArgs.cid).
                put("keyword",goodsSearchArgs.keyword).
                put("priceOrder",goodsSearchArgs.priceCondition).
                put("salesOrder",goodsSearchArgs.saleCondition).
                put("news",goodsSearchArgs.isNew).
                build();
        return RequestFactory.getRequestManager().get(PRODUCT_LIST,map, GoodsTypeBean.class,false);
    }
    //商品列表
    public static Observable<List<GoodsBean>> getProductList(String keyword, int p){

        Map<String,Object> map= MapBuilder.factory().put("page",p).
                put("keyword",keyword).
                build();
        return RequestFactory.getRequestManager().get(PRODUCT_LIST,map, GoodsBean.class,false);
    }

    //热门商品
    public static Observable<List<GoodsBean>> getProductHotList( int p,int limit){
        Map<String,Object> map= MapBuilder.factory().put("page",p).
                put("limit",limit).
                build();
        return RequestFactory.getRequestManager().get(PRODUCT_HOT,map, GoodsBean.class,false);
    }


    //获取商品详情
    public static void getGoodsDetail(String goodsId,BaseHttpCallBack baseHttpCallBack){
        HttpClient.getInstance().get(PRODUCT_DETAIL+goodsId,PRODUCT_DETAIL)
                .execute(baseHttpCallBack);
    }


    //获取购物车数量 isToalNum=true 返回的是购物车总数量,false返回的是购物车商品种类数目
    public static void getShopCartCount(boolean isToalNum,BaseHttpCallBack baseHttpCallBack){
        HttpClient.getInstance().get(CART_COUNT,CART_COUNT)
                .params("numType",isToalNum)
                .execute(baseHttpCallBack);
    }


    public static void getShopCartCount(BaseHttpCallBack baseHttpCallBack){
        getShopCartCount(true,baseHttpCallBack);
    }



    //动态设置是否收藏
    public static Observable<Boolean> judgeCollect(boolean shouldCollect, int isSeckill, String id){
       String  category=isSeckill==1?"product_seckill":"product";
       String path=shouldCollect?"collect/add":"collect/del";
        Map<String,Object> map= MapBuilder.factory().put("id",id)
        .put("category",category).build();
        return RequestFactory.getRequestManager().commit(path,map,false);
    }

    /*添加购物车*/
    public static void addShopCart(String productId,
                                   int cartNum,
                                   String uniqueId,
                                   String combinationId,
                                   String secKillId,
                                   String bargainId,
                                   int addType,
                                   BaseHttpCallBack baseHttpCallBack){
        HttpClient.getInstance().post(ADD_SHOP_CART,ADD_SHOP_CART)
                .params("productId",productId)
                .params("cartNum",cartNum)
                .params("uniqueId",uniqueId)
                .params("combinationId",combinationId)
                .params("secKillId",secKillId)
                .params("bargainId",bargainId)
                .params("new",addType)
                .execute(baseHttpCallBack);
    }







    /*获取购物车列表*/
    public static Observable<ShopcartParseBean> getShopCartData(){
        return RequestFactory.getRequestManager().valueGet(SHOP_CART_LIST,null,ShopcartParseBean.class,false);
    }


    /*获取默认地址*/
    public static void getDefaultAddress(BaseHttpCallBack baseHttpCallBack){
        HttpClient.getInstance().get(DEFAULT_ADDRESS,DEFAULT_ADDRESS)
                .execute(baseHttpCallBack);
    }


    /*删除购物车*/
    public static Observable<Boolean>delCart(String[] cartid){
        Map<String,Object> parm= MapBuilder.factory()
                .put("ids",cartid)
                .build();
        return RequestFactory.getRequestManager().commit(DELETE_SHOP_CART,parm,false);
    }

    /*修改购物车数量*/
    public static Observable<Boolean>modifyCartNum(String cartid,int num){
        Map<String,Object> parm= MapBuilder.factory()
                .put("id",cartid)
                .put("number",num)
                .build();
        return RequestFactory.getRequestManager().commit(MODIFY_CART_NUM,parm,false);
    }

    /*获取推广二维码*/
    public static void getGoodsCode(String id,BaseHttpCallBack baseHttpCallBack){
        HttpClient.getInstance().get(PRODUCT_CODE+id,PRODUCT_CODE)
                .params("user_type","routine")
                .execute(baseHttpCallBack);
    }

    /*批量收藏商品*/
    public static Observable<Boolean>batchCollect(String[] idArray,String category){
        Map<String,Object> parm= MapBuilder.factory()
                .put("id",idArray)
                .put("category",category)
                .build();
        return RequestFactory.getRequestManager().commit(PRODUCT_BATCH_COLLECT,parm,false);
    }








    /*确认订单*/
    public static void orderConfirm(String cartId,BaseHttpCallBack baseHttpCallBack){
        HttpClient.getInstance().post(ORDER_CONFIRM,ORDER_CONFIRM)
                .params("cartId",cartId)
                .execute(baseHttpCallBack);
    }
    /*确认收货*/
    public static void orderTake(String uni,BaseHttpCallBack baseHttpCallBack){
        HttpClient.getInstance().post(ORDER_TAKE,ORDER_TAKE)
                .params("uni",uni)
                .execute(baseHttpCallBack);
    }







    public static void cancle(String tag){
        HttpClient.getInstance().cancel(tag);
    }

    /*添加地址*/
    public static Observable<Boolean> addAddress(AddressCommitBean addressCommitBean) {
        Map<String,Object> parm= MapBuilder.factory()
                .put("is_default",addressCommitBean.getIsDefault())
                .put("real_name",addressCommitBean.getName())
                .put("address[province]",addressCommitBean.getProvince())
                .put("address[city]",addressCommitBean.getCity())
                .put("address[district]",addressCommitBean.getArea())
                .put("address[city_id]",addressCommitBean.getCityId())
                .put("phone",addressCommitBean.getPhone())
                .put("post_code","")
                .put("id",addressCommitBean.getId())
                .put("detail",addressCommitBean.getAddress())
                .build();
        return RequestFactory.getRequestManager().commit(ADD_UPDATE_ADDR,parm,false);
    }

    /*设置默认地址*/
    public static Observable<Boolean> setDefaultAddress(int id) {
        Map<String,Object> parm= MapBuilder.factory()
                .put("id",id)
                .build();
        return RequestFactory.getRequestManager().commit(SET_DEFAULT_ADDRESS,parm,false);
    }

    /*获取地址列表*/
    public static Observable<List<AddressInfoBean>> getAddressInfoList(int page){
        Map<String,Object> map= MapBuilder.factory()
                .put("page",page)
                .put("limit",20)
                .build();
        return RequestFactory.getRequestManager().get(ADDRESS_LIST,map, AddressInfoBean.class,false);
    }

    /*获取收藏商品列表*/
    public static Observable<List<StoreGoodsBean>> getCollectGoodsList(int page){
        Map<String,Object> map= MapBuilder.factory()
                .put("page",page)
                .put("limit",20)
                .build();
        return RequestFactory.getRequestManager().get(COLLECT_USER_LIST,map, StoreGoodsBean.class,false);
    }

    /*删除地址*/
    public static Observable<Boolean> deleteAddress(int id) {
        Map<String,Object> parm= MapBuilder.factory()
                .put("id",id)
                .build();
        return RequestFactory.getRequestManager().commit(ADDRESS_DELETE,parm,false);
    }

   /* type	是		类型 0待付款1待发货2待收货3待评价4已完成-3退款
    page	否		页码
    limit	否		每页数量
    search	否		关键词
    status	否		状态 0自己1代销2店铺*/


    /*订单列表*/
    public static Observable<List<OrderBean>> getOrderList(int type,int page,String search,int status){
        Map<String,Object> map= MapBuilder.factory()
                .put("type",type)
                .put("page",page)
                .put("limit",20)
                .put("search",search)
                .put("status",status)
                .build();
        return RequestFactory.getRequestManager().get(ORDER_LIST,map, OrderBean.class,false);
    }

    //订单统计
    public static Observable<OrderStatementBean> getMyOrderStatement(){
        return RequestFactory.getRequestManager().valueGet(ORDER_DATA,null, OrderStatementBean.class,false);
    }



    public static Observable<OrderStatementBean> getOrderStatement(int status){
        Map<String,Object> map= MapBuilder.factory()
                .put("status",status).build();
        return RequestFactory.getRequestManager().valueGet(ORDER_DATA,map, OrderStatementBean.class,false);
    }

    //订单详情
    public static void getOrderDetail(String id,int status,BaseHttpCallBack baseHttpCallBack){
        HttpClient.getInstance().get(ORDER_DETAIL+id,PRODUCT_CODE)
                .params("status",status)
                .execute(baseHttpCallBack);
    }

    //订单取消
    public static void cancleOrder(String id,BaseHttpCallBack baseHttpCallBack){
        HttpClient.getInstance().post(ORDER_CANCEL,ORDER_CANCEL)
                .params("id",id)
                .execute(baseHttpCallBack);
    }

    //商品收藏取消
    public static Observable<Boolean> cancleCollectGoods(String id, String category){
        Map<String,Object> parm= MapBuilder.factory()
                .put("id",id)
                .put("category",category)
                .build();
        return RequestFactory.getRequestManager().commit(COLLECT_DELETE,parm,false);

    }


    //订单价格计算
    public static void orderComputed(String key,
            int addressId,String couponId,String payType,int useIntegral,BaseHttpCallBack httpCallBack){
        HttpClient.getInstance().post(ORDER_COMPUTED+key,ORDER_COMPUTED)
                .params("addressId",addressId)
                .params("couponId",couponId)
                .params("payType",payType)
                .params("useIntegral",useIntegral)
                .execute(httpCallBack);
    }


    //订单支付
    public static void orderPay(String id,String paytype,BaseHttpCallBack baseHttpCallBack){
        HttpClient.getInstance().post(ORDER_PAY,ORDER_PAY)
                .params("uni",id)
                .params("paytype",paytype)
                .params("from","android")
                .execute(baseHttpCallBack);
    }


    //订单删除
    public static Observable<Boolean> deleteOrder(String orderId){
        Map<String,Object> map= MapBuilder.factory()
                .put("uni",orderId)
                .build();
        return RequestFactory.getRequestManager().commit(DEL_ORDER,map,false);
    }


    //订单评价
    public static Observable<Boolean> orderComment(EvaluateCommitBean evaluateCommitBean){
        String json= JSON.toJSONString(evaluateCommitBean);
        Map<String,Object>map=JSON.parseObject(json,Map.class);
        return RequestFactory.getRequestManager().commit(ORDER_COMMENT,map,false);
    }


    //退款理由
    public static Observable<List<String>> refundReason(){
        return RequestFactory.getRequestManager().get(REFUND_REASON,null,String.class,false);
    }


    //退款
    public static Observable<Boolean> refundOrder(RefundCommitBean refundCommitBean){
        Map<String,Object>map=MapBuilder.factory().
                put("text",refundCommitBean.getText()).
                put("refund_reason_wap_img",refundCommitBean.getRefund_reason_wap_img()).
                put("refund_reason_wap_explain",refundCommitBean.getRefund_reason_wap_explain()).
                put("uni",refundCommitBean.getUni()).build();
        return RequestFactory.getRequestManager().commit(REFUND_VERIFY,map,true);
    }

    //查看物流
    public static void viewLogistics(String orderId,BaseHttpCallBack baseHttpCallBack){
        HttpClient.getInstance().get(VIEW_LOGISTICS+orderId,VIEW_LOGISTICS)
                .execute(baseHttpCallBack);
    }


    /*再来一单*/
    public static void againOrder(String id,
                                   BaseHttpCallBack baseHttpCallBack){
        HttpClient.getInstance().post(ORDER_AGAIN,ORDER_AGAIN)
                .params("uni",id)
                .execute(baseHttpCallBack);
    }


    //热门搜索关键词
    public static Observable<List<String>> getKeywordList(){
        return RequestFactory.getRequestManager().get(SEARCH_KEYWORD,null,String.class,false);
    }


    //商品评论列表
    public static Observable<List<JSONObject>> getEvaluateList(String goodsId, int p, int type){
        Map<String,Object>map=MapBuilder.factory()
                .put("page",p)
                .put("type",type)
                .put("limit",20).build();
        return RequestFactory.getRequestManager().get(EVALUATE_LIST+goodsId,map, JSONObject.class,false);
    }


    //在售列表
    public static Observable<List<GoodsBean>> getSaleList(String liveuid, int p){
        Map<String,Object>map=MapBuilder.factory()
                .put("page",p)
                .put("liveuid",liveuid)
                .put("limit",20).build();
        return RequestFactory.getRequestManager().get(SHOP_SALE,map, GoodsBean.class,false);
    }

    //商品评论统计
    public static Observable<JSONObject> getEvaluateConfig(String goodsId){
        return RequestFactory.getRequestManager().valueGet(EVALUATE_CONFIG+goodsId,null, JSONObject.class,false);
    }



    //获取客服
    public static void getShopService(String storeId,BaseHttpCallBack baseHttpCallBack){
        HttpClient.getInstance().get(SHOP_SERVICE,SHOP_SERVICE)
                .params("mer_id",storeId)
                .execute(baseHttpCallBack);
    }



   /* addressId	text	是		地址编号
    couponId	text	是		优惠券 json {店铺ID:优惠券ID}
    payType	text	是		支付方式 微信weixin 余额yue
    useIntegral	text	否		是否积分抵扣 0否1是
    mark	text	否		备注 json {店铺ID:备注}
    from	text	是		支付来源 小程序:routine H5:weixinh5 APP:android/ios
    liveuid	text	否	0	主播ID 直播间购买*/



    //订单创建
    public static void orderCreate(OrderConfirmBean orderConfirmBean, BaseHttpCallBack baseHttpCallBack){
        int addressId=orderConfirmBean.getAddrId();
        int useIntegral=orderConfirmBean.getIsUseCode();
        String payType=orderConfirmBean.getPayType();
        String mark=orderConfirmBean.getMarkJson();
        String coupon=orderConfirmBean.getCouponJson();
        String liveUid=orderConfirmBean.getLiveUid();
        HttpClient.getInstance().post(ORDER_CREATE+orderConfirmBean.getOrderKey(),ORDER_CREATE)
                .params("addressId",addressId)
                .params("useIntegral",useIntegral)
                .params("couponId",coupon)
                .params("mark",mark)
                .params("payType",payType)
                .params("liveuid",liveUid)
                .params("from","android")
                .execute(baseHttpCallBack);
    }



}
