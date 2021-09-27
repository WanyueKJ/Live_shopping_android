package com.wanyue.main.bean;

import java.util.List;
public class CommissionBankBean {
    private String commissionCount;
    private String minPrice;
    private List<String> extractBank;

    public String getCommissionCount() {
        return commissionCount;
    }

    public void setCommissionCount(String commissionCount) {
        this.commissionCount = commissionCount;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public List<String> getExtractBank() {
        return extractBank;
    }

    public void setExtractBank(List<String> extractBank) {
        this.extractBank = extractBank;
    }
}
