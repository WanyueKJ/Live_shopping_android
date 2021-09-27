package com.wanyue.main.store.bean;

public class ProfitRecordBean {
    private String id;	//number	必须
    private String  money;	//string	必须		金额
    private String  orderno;	//string	必须		订单号
    private String  status;	//number	必须		状态
    private String addtime;	//string	必须		时间
    private String  title;	//string	必须		标题
    private String status_txt;	//string	必须		状态文字


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus_txt() {
        return status_txt;
    }

    public void setStatus_txt(String status_txt) {
        this.status_txt = status_txt;
    }
}
