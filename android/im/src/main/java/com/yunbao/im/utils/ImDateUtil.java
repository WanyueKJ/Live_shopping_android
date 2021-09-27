package com.yunbao.im.utils;


import com.yunbao.im.bean.TimeInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by  on 2018/7/14.
 */

public class ImDateUtil {

    private static final long INTERVAL = 60000;

    private static final SimpleDateFormat SDF1;
    private static final SimpleDateFormat SDF2;
    private static final SimpleDateFormat SDF3;

    static {
        SDF1 = new SimpleDateFormat("aa hh:mm");
        SDF2 = new SimpleDateFormat("MM-dd aa hh:mm");
        SDF3 = new SimpleDateFormat("MM-dd aa hh:mm");
    }

    public static String getTimestampString(long time) {
        if (isSameDay(time)) {
            return SDF1.format(new Date(time));
        } else if (isYesterday(time)) {
            return SDF2.format(new Date(time));
        } else {
            return SDF3.format(new Date(time));
        }
    }

    private static boolean isSameDay(long inputTime) {
        TimeInfo tStartAndEndTime = getTodayStartAndEndTime();
        if (inputTime > tStartAndEndTime.getStartTime() && inputTime < tStartAndEndTime.getEndTime())
            return true;
        return false;
    }


    private static boolean isYesterday(long inputTime) {
        TimeInfo yStartAndEndTime = getYesterdayStartAndEndTime();
        if (inputTime > yStartAndEndTime.getStartTime() && inputTime < yStartAndEndTime.getEndTime())
            return true;
        return false;
    }


    /**
     * 获取今天00:00:00~23:59:59 开始和结束的时间戳
     */
    private static TimeInfo getTodayStartAndEndTime() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar1.getTime();
        long startTime = startDate.getTime();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.HOUR_OF_DAY, 23);
        calendar2.set(Calendar.MINUTE, 59);
        calendar2.set(Calendar.SECOND, 59);
        calendar2.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar2.getTime();
        long endTime = endDate.getTime();
        TimeInfo info = new TimeInfo();
        info.setStartTime(startTime);
        info.setEndTime(endTime);
        return info;
    }

    /**
     * 获取昨天00:00:00~23:59:59 开始和结束的时间戳
     */
    private static TimeInfo getYesterdayStartAndEndTime() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE, -1);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);

        Date startDate = calendar1.getTime();
        long startTime = startDate.getTime();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DATE, -1);
        calendar2.set(Calendar.HOUR_OF_DAY, 23);
        calendar2.set(Calendar.MINUTE, 59);
        calendar2.set(Calendar.SECOND, 59);
        calendar2.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar2.getTime();
        long endTime = endDate.getTime();
        TimeInfo info = new TimeInfo();
        info.setStartTime(startTime);
        info.setEndTime(endTime);
        return info;
    }

    /**
     * 判断两个时间戳是否小于 INTERVAL
     */
    public static boolean isCloseEnough(long time1, long time2) {
        return Math.abs(time2 - time1) < INTERVAL;
    }
}
