package com.wanyue.main.bean;

import com.wanyue.common.bean.GoodsBean;

import java.util.List;

public class GreateSelectBean {
    private String tips;
    private List<GoodsBean>list;


    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public List<GoodsBean> getList() {
        return list;
    }

    public void setList(List<GoodsBean> list) {
        this.list = list;
    }
}
