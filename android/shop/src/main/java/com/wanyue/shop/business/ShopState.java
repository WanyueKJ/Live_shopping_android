package com.wanyue.shop.business;

import com.wanyue.shop.bean.OrderBean;
import com.wanyue.shop.bean.OrderStatus;

public class ShopState {

    public static final int ORDER_STATE_WAIT_PAY=0;//待付款
    public static final int ORDER_STATE_WAIT_DELIVERED=1;//待发货
    public static final int ORDER_STATE_WAIT_RECEIVE=2;//待收货
    public static final int ORDER_STATE_WAIT_EVALUATE=3;//待评价
    public static final int ORDER_STATE_COMPELETE=4;//待付款
    public static final int ORDER_STATE_REFUND=-3;//退款






    public static final String ORDER_LOGISTICS_SEND="send";//送货
    public static final String ORDER_LOGISTICS_EXPRESS="express";//发货


    public static final int COMMENTS_TOTAL=0;//好评
    public static final int COMMENTS_GOODS=1;//好评
    public static final int COMMENTS_NORMAL=2;//中评
    public static final int COMMENTS_BAD=3;//差评



    public static final int REFUND_STATE_ING=0;//退款进行中
    public static final int REFUND_STATE_COMPELETE=2;//已退款
    public static final int REFUND_STATE_REFUSE=0;//拒绝退款




    public static boolean isRefundOrder(OrderBean orderBean){
        OrderStatus orderStatus=orderBean.getOrderStatus();
        if(orderStatus!=null){
          return orderStatus.getType()==ShopState.ORDER_STATE_REFUND;
        }
        return false;
    }


    public static final int ORDER_BUY_SELF=0;//自己买的订单
    public static final int ORDER_CONSIGNMENT=1;//自己代卖的订单
    public static final int ORDER_SELL_STORE=2;//自己店铺的



    public static final String PRODUCT_TYPE_NORMAL = "product"; //普通产品
    public static final String PRODUCT_TYPE_SECKILL = "product_seckill";//秒杀产品
    public static final String PRODUCT_DEFAULT = PRODUCT_TYPE_NORMAL;//秒杀产品
}
