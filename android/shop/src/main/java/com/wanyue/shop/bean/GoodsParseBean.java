package com.wanyue.shop.bean;

import android.util.ArrayMap;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import com.wanyue.common.bean.GoodsBean;
import com.wanyue.common.bean.SpecsValueBean;
import com.wanyue.shop.evaluate.bean.EvaluateBean;
import com.wanyue.shop.evaluate.bean.EvaluateBean2;

import java.util.List;

/*解析接口字段*/
public class GoodsParseBean {

    @SerializedName("good_list")
    @JSONField(name ="good_list")
    private List<GoodsBean> goodsList; //为你推荐
    private List<SpecsBean> productAttr; //规格

    /*这块的数据类型是变化的,不能直接解析会出错,arrayMap保证map的顺序是有序的*/
    private ArrayMap<String, SpecsValueBean> productRealyValue;//规格对应的值;
    private String replyChance;
    private String replyCount;

    @SerializedName("storeInfo")
    @JSONField(name ="storeInfo")
    private StoreGoodsBean goodsInfo;
    private EvaluateBean2 reply;

    public List<GoodsBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsBean> goodsList) {
        this.goodsList = goodsList;
    }

    public List<SpecsBean> getProductAttr() {
        return productAttr;
    }

    public void setProductAttr(List<SpecsBean> productAttr) {
        this.productAttr = productAttr;
    }


    public ArrayMap<String, SpecsValueBean> getProductRealyValue() {
        return productRealyValue;
    }

    public void setProductRealyValue(ArrayMap<String, SpecsValueBean> productRealyValue) {
        this.productRealyValue = productRealyValue;
    }

    public EvaluateBean2 getReply() {
        return reply;
    }

    public void setReply(EvaluateBean2 reply) {
        this.reply = reply;
    }

    public String getReplyChance() {
        return replyChance;
    }

    public void setReplyChance(String replyChance) {
        this.replyChance = replyChance;
    }

    public String getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(String replyCount) {
        this.replyCount = replyCount;
    }

    public StoreGoodsBean getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(StoreGoodsBean goodsInfo) {
        this.goodsInfo = goodsInfo;
    }
}
