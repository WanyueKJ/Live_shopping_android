package com.wanyue.shop.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import com.wanyue.common.utils.StringUtil;

/*订单统计*/
public class OrderStatementBean {
 /*   ├─ order_count	number	非必须		订单支付没有退款 数量
├─ sum_price	number	非必须		订单支付没有退款 支付总金额
├─ unpaid_count	number	非必须		订单待支付 数量
├─ unshipped_count	number	非必须		订单待发货 数量
├─ received_count	number	非必须		订单待收货 数量
├─ evaluated_count	number	非必须		订单待待评价 数量
├─ complete_count	number	非必须		订单待已完成 数量*/

    @SerializedName("order_count")
    @JSONField(name="order_count")
    private int orderCount;

    @SerializedName("sum_price")
    @JSONField(name="sum_price")
    private String sumPrice;

    @SerializedName("unpaid_count")
    @JSONField(name="unpaid_count")
    private int unpaidCount;

    @SerializedName("unshipped_count")
    @JSONField(name="unshipped_count")
    private int unShippedCount;

    @SerializedName("received_count")
    @JSONField(name="received_count")
    private int receivedCount;

    @SerializedName("evaluated_count")
    @JSONField(name="evaluated_count")
    private int evaluatedCount;


    @SerializedName("complete_count")
    @JSONField(name="complete_count")
    private int completeCount;

    @SerializedName("refund_count")
    @JSONField(name="refund_count")
    private int refundCount;


    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public String getSumPrice() {
        return StringUtil.getPrice(sumPrice);
    }

    public void setSumPrice(String sumPrice) {
        this.sumPrice = sumPrice;
    }

    public int getUnpaidCount() {
        return unpaidCount;
    }

    public void setUnpaidCount(int unpaidCount) {
        this.unpaidCount = unpaidCount;
    }

    public int getUnShippedCount() {
        return unShippedCount;
    }

    public void setUnShippedCount(int unShippedCount) {
        this.unShippedCount = unShippedCount;
    }

    public int getReceivedCount() {
        return receivedCount;
    }

    public void setReceivedCount(int receivedCount) {
        this.receivedCount = receivedCount;
    }

    public int getEvaluatedCount() {
        return evaluatedCount;
    }

    public void setEvaluatedCount(int evaluatedCount) {
        this.evaluatedCount = evaluatedCount;
    }

    public int getCompleteCount() {
        return completeCount;
    }

    public void setCompleteCount(int completeCount) {
        this.completeCount = completeCount;
    }


    public int getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(int refundCount) {
        this.refundCount = refundCount;
    }
}
