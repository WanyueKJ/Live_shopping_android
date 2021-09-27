package com.wanyue.shop.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import com.wanyue.common.utils.StringUtil;

public class AddressInfoBean implements Parcelable {
    private int id;

    @SerializedName("real_name")
    @JSONField(name = "real_name")
    private String name;


    private String phone;

    private String province;
    private String city;

    @SerializedName("district")
    @JSONField(name = "district")
    private String area;

    private String detailArea;

    @SerializedName("detail")
    @JSONField(name = "detail")
    private String address;

    @SerializedName("is_default")
    @JSONField(name = "is_default")
    private int isDefault;

    private String namePhoneShowInfo;


    public AddressInfoBean(int id, String name, String phone, String area, String address, int isDefault) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.area = area;
        this.address = address;
        this.isDefault = isDefault;
    }

    public AddressInfoBean(){

    }


    protected AddressInfoBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
        phone = in.readString();
        province = in.readString();
        city = in.readString();
        area = in.readString();
        detailArea = in.readString();
        address = in.readString();
        isDefault = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(area);
        dest.writeString(detailArea);
        dest.writeString(address);
        dest.writeInt(isDefault);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddressInfoBean> CREATOR = new Creator<AddressInfoBean>() {
        @Override
        public AddressInfoBean createFromParcel(Parcel in) {
            return new AddressInfoBean(in);
        }

        @Override
        public AddressInfoBean[] newArray(int size) {
            return new AddressInfoBean[size];
        }
    };

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


    public String getNamePhoneShowInfo() {
        if(namePhoneShowInfo==null){
           namePhoneShowInfo=StringUtil.contact("收货人:\t"+name,"\t\t",phone);
        }
        return namePhoneShowInfo;
    }

    public String getDetailArea() {
        if(TextUtils.isEmpty(detailArea)){
           detailArea= StringUtil.contact("收货地址:\t",province,"\t",city,"\t",area,"\t","\t",address);
        }
        return detailArea;
    }


    public String getPcc() {
        if(TextUtils.isEmpty(detailArea)){
            detailArea= StringUtil.contact(province,",",city,",",area);
        }
        return detailArea;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getIsDefault() {
        return isDefault;
    }
    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
}
