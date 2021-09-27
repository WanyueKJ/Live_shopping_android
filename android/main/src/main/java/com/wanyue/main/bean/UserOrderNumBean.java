package com.wanyue.main.bean;

public class UserOrderNumBean {
  /*    ├─ noBuy	number	非必须		未支付订单数量
├─ noPostage	string	非必须		未发货订单数量
├─ noTake	number	非必须		未收货订单数量
├─ noReply	number	非必须		未评论订单数量
├─ noPink	number	非必须		拼团的订单数量
├─ noRefund	number	非必须		退款的订单数量*/


    private int noBuy;
    private int noPostage;
    private int noTake;
    private int noReply;
    private int noPink;
    private int noRefund;

    public int getNoBuy() {
        return noBuy;
    }

    public void setNoBuy(int noBuy) {
        this.noBuy = noBuy;
    }

    public int getNoPostage() {
        return noPostage;
    }

    public void setNoPostage(int noPostage) {
        this.noPostage = noPostage;
    }

    public int getNoTake() {
        return noTake;
    }

    public void setNoTake(int noTake) {
        this.noTake = noTake;
    }

    public int getNoReply() {
        return noReply;
    }

    public void setNoReply(int noReply) {
        this.noReply = noReply;
    }

    public int getNoPink() {
        return noPink;
    }

    public void setNoPink(int noPink) {
        this.noPink = noPink;
    }

    public int getNoRefund() {
        return noRefund;
    }

    public void setNoRefund(int noRefund) {
        this.noRefund = noRefund;
    }




}
