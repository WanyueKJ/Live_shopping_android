package com.wanyue.common.mob;

/**
 * Created by  on 2018/9/21.
 */

public class LoginData {

    private String mType;
    private String mOpenID;
    private String mUnionid;
    private String mNickName;
    private String mAvatar;
    private int mFlag; //第三方标识,0PC，1QQ，2微信，3新浪，4facebook，5twitter
    private String province;
    private String country;
    private String gender;
    private String city;



    public LoginData() {

    }

    public LoginData(String type, String openID, String unionid,String nickName, String avatar,int flag) {
        mType = type;
        mOpenID = openID;
        mNickName = nickName;
        mAvatar = avatar;
        mFlag =flag;
        mUnionid=unionid;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getOpenID() {
        return mOpenID;
    }

    public void setOpenID(String openID) {
        mOpenID = openID;
    }

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String nickName) {
        mNickName = nickName;
    }


    public String getUnionid() {
        return mUnionid;
    }

    public void setUnionid(String unionid) {
        mUnionid = unionid;
    }

    public String getAvatar() {
        return mAvatar;
    }


    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getFlag() {
        return mFlag;
    }

    public void setFlag(int flag) {
        mFlag = flag;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
