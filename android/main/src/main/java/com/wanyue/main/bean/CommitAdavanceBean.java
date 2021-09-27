package com.wanyue.main.bean;

public class CommitAdavanceBean{
    private String alipay_code;//支付宝账号
    private String extract_type;//提现方式 bank = 银行卡 alipay = 支付宝 weixin=微信
    private String money;
    private String name;
    private String bankname;
    private String cardnum;
    private String weixin;


    public String getAlipay_code() {
        return alipay_code;
    }


    public void setAlipay_code(String alipay_code) {
        this.alipay_code = alipay_code;
    }

    public String getExtract_type() {
        return extract_type;
    }

    public void setExtract_type(String extract_type) {
        this.extract_type = extract_type;
    }

    public String getMoney() {
        return money;
    }
    public void setMoney(String money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public String getWeixin() {
        return weixin;
    }
    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }
}
