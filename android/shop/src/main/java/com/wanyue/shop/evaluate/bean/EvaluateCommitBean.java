package com.wanyue.shop.evaluate.bean;


import android.text.TextUtils;
import com.wanyue.common.utils.ListUtil;
import java.util.List;


public class EvaluateCommitBean {
  /*  unique	text	是		订单的unique
    comment	text	是		产品评论
    pics	text	否		评论图片，(多个图片请用,(英文逗号)隔开)
    product_score	text	否	5	产品分数 0-5(不传默认5分)
    service_score	text	否	5	服务分数 0-5(不传默认5分)*/

    private String unique;
    private String comment;
    private String pics;
    private int product_score;
    private int service_score;


    public String getUnique() {
        return unique;
    }
    public void setUnique(String unique) {
        this.unique = unique;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isEdit(){
        return false;
    }

    private boolean isSelectContent() {
        return !TextUtils.isEmpty(comment);
    }


    public boolean isSelectStar(){
        return product_score>0||service_score>0;
    }

    public String getPics() {
        return pics;
    }
    public void setPics(String pics) {
        this.pics = pics;
    }
    public int getProduct_score() {
        return product_score;
    }
    public void setProduct_score(int product_score) {
        this.product_score = product_score;
    }
    public int getService_score() {
        return service_score;
    }

    public void setService_score(int service_score) {
        this.service_score = service_score;
    }
}
