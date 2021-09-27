package com.wanyue.shop.bean;

import android.text.TextUtils;
import android.util.ArrayMap;

import com.alibaba.fastjson.JSON;
import com.wanyue.common.bean.commit.CommitEntity;
import com.wanyue.common.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class AddressCommitBean extends CommitEntity {
    private int id;
    private String name;
    private String phone;
    private String province;
    private String city;
    private String area;
    private String address;
    private String cityInfo;
    private PccInfo pcc;
    private int isDefault;
    private String cityId;

    @Override
    public boolean observerCondition() {
        return fieldNotEmpty(name)&&fieldNotEmpty(phone)&&fieldNotEmpty(area)&&fieldNotEmpty(address);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
        observer();
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
        observer();
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
        observer();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        observer();
    }

    public String getCityInfo() {
        cityInfo= StringUtil.contact(province,",","\t",city,",","\t",area,"\t");
        return cityInfo;
    }


    public PccInfo getPcc() {
        if(pcc==null){
            pcc=new PccInfo();
            pcc.setCity(city);
            pcc.setProvince(province);
            pcc.setDistrict(area);
            pcc.setCity_id(cityId);
         /*   pcc=StringUtil.contact("{","province:",province, "\t",
                    "city:",city, "\t",
                    "district:",area
                    ,"}"
                    );*//*
            Map<String,String>map=new HashMap<>();
            map.put("province",province);
            map.put("city",city);
            map.put("district",area);
            map.put("city_id",cityId);
            pcc= map;*/
        }
        return pcc;
    }


    public int getIsDefault() {
        return isDefault;
    }


    public String getCityId() {
        return cityId;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;

    }

    public void copy(AddressInfoBean addressInfoBean){
        id=addressInfoBean.getId();
        name=addressInfoBean.getName();
        phone=addressInfoBean.getPhone();
        province=addressInfoBean.getProvince();
        city=addressInfoBean.getCity();
        area=addressInfoBean.getArea();
        address=addressInfoBean.getAddress();
        isDefault=addressInfoBean.getIsDefault();
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

}
