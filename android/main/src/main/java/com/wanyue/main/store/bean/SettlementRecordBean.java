package com.wanyue.main.store.bean;

public class SettlementRecordBean {
   private String order_id;//	string	必须		订单号
   private String money;	//string	必须		金额
   private String settle_time;	//string	必须		时间
   private String title;	//string	必须		标题
   private String  status;

   private String order;


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getSettle_time() {
        return settle_time;
    }

    public void setSettle_time(String settle_time) {
        this.settle_time = settle_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder() {
        if(order==null){
            order="订单"+order_id;
        }
        return order;
    }
}
