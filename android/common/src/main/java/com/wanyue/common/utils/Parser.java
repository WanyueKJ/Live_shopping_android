package com.wanyue.common.utils;

import android.text.TextUtils;

public  class Parser{
    public static final int MODE_SECOND=1;
    public static final int MODE_MINUTE=2;
    public static final int MODE_HOUR=3;

    public static final int MODE_DEFAULT=0;
    private int model=MODE_DEFAULT;

        private  long hour = 0;
        private  long minute = 0;
        private  long second = 0;
        private  long day= 0;

        private  boolean dayNotAlready = false;
        private  boolean hourNotAlready = false;
        private  boolean minuteNotAlready = false;
        private  boolean secondNotAlready = false;

        private StringBuilder stringBuilder;
        private String beforeString;
        private String afterString;
        public Parser(){
        }

    public void setModel(int model) {
        this.model = model;
    }

    private void initStringBuilder() {
            if(stringBuilder==null){
               stringBuilder=new StringBuilder();
            }else {
               stringBuilder.setLength(0);
            }
    }

    public  String parse(long totalSecond){
            return parseStringBuilder(totalSecond).toString();
        }


    public  StringBuilder parseStringBuilder(long totalSecond){
        clear();
        if (totalSecond > 0) {
            secondNotAlready = true;
            second = totalSecond;
            if (second >= 60) {
                minuteNotAlready = true;
                minute = second / 60;
                second = second % 60;
                if (minute >= 60) {
                    hourNotAlready = true;
                    hour = minute / 60;
                    minute = minute % 60;
                    if (hour > 24) {
                        dayNotAlready = true;
                        day = hour / 24;
                        hour = hour % 24;
                    }
                }
            }
        }
        if(!TextUtils.isEmpty(beforeString)){
            stringBuilder.append(beforeString);
        }
        if(model>=MODE_HOUR){
            stringBuilder.append(getTimeString(hour)).append(":");
        }

        if(model>=MODE_MINUTE){
            stringBuilder .append(getTimeString(minute))
                    .append(":");
        }

        if(model<MODE_MINUTE&&totalSecond==60){
            stringBuilder
                    .append("60");
        }else{
            stringBuilder
                   .append(getTimeString(second));
        }


        if(!TextUtils.isEmpty(afterString)){
            stringBuilder.append(afterString);
        }
        return stringBuilder;
    }




    public void clear(){
            hour=0;
            minute=0;
            initStringBuilder();
        }

        public String getTimeString(long time){
            if(time<10){
               return "0"+time;
            }else{
                return ""+time;
            }
        }
    public void setBeforeString(String beforeString) {
            this.beforeString=beforeString;
    }

    public void setAfterString(String afterString) {
        this.afterString = afterString;
    }
}