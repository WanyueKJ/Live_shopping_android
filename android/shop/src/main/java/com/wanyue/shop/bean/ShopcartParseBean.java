package com.wanyue.shop.bean;

import java.util.List;

public class ShopcartParseBean {
    private List<ShopCartStoreBean> valid;
    private List<ShopCartBean>invalid;

    public List<ShopCartStoreBean> getValid() {
        return valid;
    }

    public void setValid(List<ShopCartStoreBean> valid) {
        this.valid = valid;
    }

    public List<ShopCartBean> getInvalid() {
        return invalid;
    }

    public void setInvalid(List<ShopCartBean> invalid) {
        this.invalid = invalid;
    }
}
