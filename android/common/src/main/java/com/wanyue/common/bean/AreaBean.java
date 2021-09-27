package com.wanyue.common.bean;

import org.jetbrains.annotations.NotNull;

public class AreaBean {
    private String province;
    private String city;
    private String county;

    public AreaBean(String province, String city, String county) {
        this.province = province;
        this.city = city;
        this.county = county;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCounty() {
        return county;
    }
    public void setCounty(String county) {
        this.county = county;
    }
    @NotNull
    @Override
    public String toString() {
        StringBuilder builder=new StringBuilder();
        try {
            builder.append(province).append(" ")
                    .append(city).append(" ")
                    .append(county).append(" ");
        }catch (Exception e){
            e.printStackTrace();
        }
        return builder.toString();
    }
}
