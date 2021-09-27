package com.yunbao.im.utils;

public  class Parser{
        private  long hour = 0;
        private  long minute = 0;
        private  long second = 0;
        private  long day= 0;

        private  boolean dayNotAlready = false;
        private  boolean hourNotAlready = false;
        private  boolean minuteNotAlready = false;
        private  boolean secondNotAlready = false;

        private StringBuilder stringBuilder;
        public Parser(){
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
        return stringBuilder.
                append(getTimeString(hour)).append(":")
                .append(getTimeString(minute))
                .append(":")
                .append(getTimeString(second));
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
    }