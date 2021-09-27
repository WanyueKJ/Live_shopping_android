package com.wanyue.shop.evaluate.bean;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.alibaba.fastjson.JSON;
import com.wanyue.common.utils.ResourceUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.shop.R;
import java.util.ArrayList;
import java.util.List;
/**
 * Copyright 2020 bejson.com
 /**
 * Auto-generated: 2020-07-16 10:37:26
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

public class EvaluateBean {
        private String add_time;
        private String avatar;
        private String comment;
        private String merchant_reply_content;
        private String merchant_reply_time;
        private String nickname;
        private int product_score;
        private int service_score;
        private int star;
        private String suk;
        private List<String> pictureList;

        private String timeAndSuk;

        private SpannableStringBuilder replyContent2;


        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
        public String getAdd_time() {
            return add_time;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
        public String getAvatar() {
            return avatar;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
        public String getComment() {
            return comment;
        }

        public void setMerchant_reply_content(String merchant_reply_content) {
            this.merchant_reply_content = merchant_reply_content;
        }
        public String getMerchant_reply_content() {
            return merchant_reply_content;
        }

        public void setMerchant_reply_time(String merchant_reply_time) {
            this.merchant_reply_time = merchant_reply_time;
        }
        public String getMerchant_reply_time() {
            return merchant_reply_time;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
        public String getNickname() {
            return nickname;
        }


    public void setPictureList(List<String> pictureList) {
        this.pictureList = pictureList;
    }



    public void setProduct_score(int product_score) {
            this.product_score = product_score;
        }
        public int getProduct_score() {
            return product_score;
        }

        public void setService_score(int service_score) {
            this.service_score = service_score;
        }
        public int getService_score() {
            return service_score;
        }

        public void setStar(int star) {
            this.star = star;
        }
        public int getStar() {
            return star;
        }

        public void setSuk(String suk) {
            this.suk = suk;
        }
        public String getSuk() {
            return suk;
        }


    public List<String> getPictureList() {
        return pictureList;
    }


    public String getTimeAndSuk() {
            if(timeAndSuk==null){
               timeAndSuk= StringUtil.contact(add_time,"\t"+suk);
            }
         return timeAndSuk;
    }

    public SpannableStringBuilder getReplyContent2() {
        if(!TextUtils.isEmpty(merchant_reply_content)){
            String serviceName= WordUtil.getString(R.string.service_name);
            String totalString=serviceName+merchant_reply_content;
            SpannableStringBuilder styled = SpannableStringBuilder.valueOf(totalString);
            ForegroundColorSpan highlightSpan =
                    new ForegroundColorSpan(ResourceUtil.getColor(R.color.global));
            styled.setSpan(highlightSpan,
                    0, 0 + serviceName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            replyContent2=styled;
        }
        return replyContent2;
    }
}
