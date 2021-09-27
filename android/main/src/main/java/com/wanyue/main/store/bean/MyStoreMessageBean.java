package com.wanyue.main.store.bean;

public class MyStoreMessageBean {
    /*├─ unshipped	number	非必须		待发货
├─ received	number	非必须		待收货
├─ evaluated	number	非必须		待评价
├─ shop_t	string	非必须		今日收益
├─ shop_all	string	非必须		总收益
├─ shop_ok	string	非必须		已结算
├─ shop_no	string	非必须		未结算*/
   private int unshipped;
   private int received;
   private int evaluated;
    private String  shop_t;
    private String shop_all;
    private String shop_ok;
    private String shop_no;

    public int getUnshipped() {
        return unshipped;
    }

    public void setUnshipped(int unshipped) {
        this.unshipped = unshipped;
    }

    public int getReceived() {
        return received;
    }

    public void setReceived(int received) {
        this.received = received;
    }

    public int getEvaluated() {
        return evaluated;
    }

    public void setEvaluated(int evaluated) {
        this.evaluated = evaluated;
    }

    public String getShop_t() {
        return shop_t;
    }

    public void setShop_t(String shop_t) {
        this.shop_t = shop_t;
    }

    public String getShop_all() {
        return shop_all;
    }

    public void setShop_all(String shop_all) {
        this.shop_all = shop_all;
    }

    public String getShop_ok() {
        return shop_ok;
    }

    public void setShop_ok(String shop_ok) {
        this.shop_ok = shop_ok;
    }

    public String getShop_no() {
        return shop_no;
    }

    public void setShop_no(String shop_no) {
        this.shop_no = shop_no;
    }


}
