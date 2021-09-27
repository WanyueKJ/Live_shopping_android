package com.wanyue.shop.model;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.OrderStatementBean;
import com.wanyue.shop.business.ShopEvent;
import com.wanyue.shop.business.ShopState;

public class OrderModel extends ViewModel {
    private static OrderStatementBean orderStatementBean;
    public static void freshOrderStatement(){
        ShopAPI.getMyOrderStatement().subscribe(new DefaultObserver<OrderStatementBean>() {
            @Override
            public void onNext(OrderStatementBean temp) {
                orderStatementBean=temp;
                LiveEventBus.get(ShopEvent.ORDER_ORDER_STATEMENT).post(orderStatementBean);
            }
        });
    }
    /*观察订单统计变化,粘性监听保证消息不丢失*/
    public static void watchOrderStatementChange(LifecycleOwner lifecycleOwner, Observer<OrderStatementBean>observer){
        LiveEventBus.get(ShopEvent.ORDER_ORDER_STATEMENT,OrderStatementBean.class).observeSticky(lifecycleOwner,observer);
    }

    public static void setOrderStatementBean(OrderStatementBean orderStatementBean) {
        if(orderStatementBean==null){
            return;
        }
        OrderModel.orderStatementBean = orderStatementBean;
        LiveEventBus.get(ShopEvent.ORDER_ORDER_STATEMENT).post(orderStatementBean);
    }

    /*发送订单变化*/
    public static void sendOrderChangeEvent(String orderId){
        freshOrderStatement();
        LiveEventBus.get(ShopEvent.ODRER_NOTIFY,String.class).post(orderId);
    }

    /*观察订单变化*/
    public static void watchOrderChangeEvent(LifecycleOwner lifecycleOwner, Observer<String>observer){
        LiveEventBus.get(ShopEvent.ODRER_NOTIFY,String.class).observe(lifecycleOwner,observer);
    }

}
