package com.wanyue.common.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2017/8/14.
 */

public class UserBean implements Parcelable {

    protected String id;
    protected String userNiceName;
    protected String avatar;
    protected String avatarThumb;
    protected int sex;
    protected String signature;
    protected String coin;
    protected String votes;
    protected String consumption;
    protected String votestotal;
    protected String province;
    protected String city;
    protected String location;//所在地
    protected String birthday;
    protected int level;
    protected int levelAnchor;
    protected int lives;
    protected int follows;
    protected int fans;
    protected boolean vip;
    protected Liang liang;
    protected Car car;
    private String integral;
    private int isshop;

    @SerializedName("now_money")
    @JSONField(name ="now_money")
    private double balance; //余额

    @JSONField(name = "uid")

    public String getId() {
        return id;
    }

    @JSONField(name = "uid")
    public void setId(String id) {
        this.id = id;
    }

    @JSONField(name = "nickname")
    public String getUserNiceName() {
        return userNiceName;
    }

    @JSONField(name = "nickname")
    public void setUserNiceName(String userNiceName) {
        this.userNiceName = userNiceName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JSONField(name = "avatar")
    public String getAvatarThumb() {
        return avatarThumb;
    }

    @JSONField(name = "avatar")
    public void setAvatarThumb(String avatarThumb) {
        this.avatarThumb = avatarThumb;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getConsumption() {
        return consumption;
    }

    public void setConsumption(String consumption) {
        this.consumption = consumption;
    }

    public String getVotestotal() {
        return votestotal;
    }

    public void setVotestotal(String votestotal) {
        this.votestotal = votestotal;
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


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getLevel() {
        if (level == 0) {
            level = 1;
        }
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @JSONField(name = "level_anchor")
    public int getLevelAnchor() {
        return levelAnchor;
    }

    @JSONField(name = "level_anchor")
    public void setLevelAnchor(int levelAnchor) {
        this.levelAnchor = levelAnchor;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public boolean getVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public Liang getLiang() {
        return liang;
    }

    public void setLiang(Liang liang) {
        this.liang = liang;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }



    /**
     * 显示靓号
     */

    public String getLiangNameTip() {
        if (this.liang != null) {
            String liangName = this.liang.getName();
            if (!TextUtils.isEmpty(liangName) && !"0".equals(liangName)) {
               // return WordUtil.getString(R.string.live_liang) + ":" + liangName;
            }
        }
        return "ID:" + this.id;
    }

    /**
     * 获取靓号
     */
    public String getGoodName() {
        if (this.liang != null) {
            return this.liang.getName();
        }
        return "0";
    }

    public int getVipType() {
        return 0;
    }


    public UserBean() {
    }


    protected UserBean(Parcel in) {
        this.id = in.readString();
        this.userNiceName = in.readString();
        this.avatar = in.readString();
        this.avatarThumb = in.readString();
        this.sex = in.readInt();
        this.signature = in.readString();
        this.coin = in.readString();
        this.votes = in.readString();
        this.consumption = in.readString();
        this.votestotal = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.location = in.readString();
        this.birthday = in.readString();
        this.level = in.readInt();
        this.levelAnchor = in.readInt();
        this.lives = in.readInt();
        this.follows = in.readInt();
        this.fans = in.readInt();
        this.vip =  in.readByte() != 0;
        this.liang = in.readParcelable(Liang.class.getClassLoader());
        this.car = in.readParcelable(Car.class.getClassLoader());
        this.integral = in.readString();
        this.balance =in.readDouble();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userNiceName);
        dest.writeString(this.avatar);
        dest.writeString(this.avatarThumb);
        dest.writeInt(this.sex);
        dest.writeString(this.signature);
        dest.writeString(this.coin);
        dest.writeString(this.votes);
        dest.writeString(this.consumption);
        dest.writeString(this.votestotal);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.location);
        dest.writeString(this.birthday);
        dest.writeInt(this.level);
        dest.writeInt(this.levelAnchor);
        dest.writeInt(this.lives);
        dest.writeInt(this.follows);
        dest.writeInt(this.fans);
        dest.writeByte((byte) (vip ? 1 : 0));
        dest.writeParcelable(this.liang, flags);
        dest.writeParcelable(this.car, flags);
        dest.writeString(this.integral);
        dest.writeDouble(this.balance);

    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }

        @Override
        public UserBean createFromParcel(Parcel in) {
            return new UserBean(in);
        }
    };


    public static class Vip implements Parcelable {
        protected int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public Vip() {

        }

        public Vip(Parcel in) {
            this.type = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.type);
        }

        public static final Creator<Vip> CREATOR = new Creator<Vip>() {
            @Override
            public Vip[] newArray(int size) {
                return new Vip[size];
            }

            @Override
            public Vip createFromParcel(Parcel in) {
                return new Vip(in);
            }
        };
    }

    public static class Liang implements Parcelable {
        protected String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Liang() {

        }


        public Liang(Parcel in) {
            this.name = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
        }

        public static final Creator<Liang> CREATOR = new Creator<Liang>() {

            @Override
            public Liang createFromParcel(Parcel in) {
                return new Liang(in);
            }

            @Override
            public Liang[] newArray(int size) {
                return new Liang[size];
            }
        };

    }

    public static class Car implements Parcelable {
        protected int id;
        protected String swf;
        protected float swftime;
        protected String words;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSwf() {
            return swf;
        }

        public void setSwf(String swf) {
            this.swf = swf;
        }

        public float getSwftime() {
            return swftime;
        }

        public void setSwftime(float swftime) {
            this.swftime = swftime;
        }

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }

        public Car() {

        }

        public Car(Parcel in) {
            this.id = in.readInt();
            this.swf = in.readString();
            this.swftime = in.readFloat();
            this.words = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.swf);
            dest.writeFloat(this.swftime);
            dest.writeString(this.words);
        }


        public static final Creator<Car> CREATOR = new Creator<Car>() {
            @Override
            public Car[] newArray(int size) {
                return new Car[size];
            }

            @Override
            public Car createFromParcel(Parcel in) {
                return new Car(in);
            }
        };
    }


    public int getIsshop() {
        return isshop;
    }

    public void setIsshop(int isshop) {
        this.isshop = isshop;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;

    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }
}
