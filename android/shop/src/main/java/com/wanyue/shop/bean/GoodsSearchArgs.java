package com.wanyue.shop.bean;

import android.os.Parcel;
import android.os.Parcelable;

public  class GoodsSearchArgs  implements Parcelable {
        public String classId;
        public String priceCondition;
        public String saleCondition;
        public int   isNew;
        public String sid;
        public String cid;
        public String keyword;
        public String className;

        public GoodsSearchArgs(){

        }
        protected GoodsSearchArgs(Parcel in) {
                classId = in.readString();
                priceCondition = in.readString();
                saleCondition = in.readString();
                isNew = in.readInt();
                sid = in.readString();
                cid = in.readString();
                keyword = in.readString();
                className= in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(classId);
                dest.writeString(priceCondition);
                dest.writeString(saleCondition);
                dest.writeInt(isNew);
                dest.writeString(sid);
                dest.writeString(cid);
                dest.writeString(keyword);
                dest.writeString(className);
        }

        @Override
        public int describeContents() {
                return 0;
        }

        public static final Creator<GoodsSearchArgs> CREATOR = new Creator<GoodsSearchArgs>() {
                @Override
                public GoodsSearchArgs createFromParcel(Parcel in) {
                        return new GoodsSearchArgs(in);
                }

                @Override
                public GoodsSearchArgs[] newArray(int size) {
                        return new GoodsSearchArgs[size];
                }
        };
}

