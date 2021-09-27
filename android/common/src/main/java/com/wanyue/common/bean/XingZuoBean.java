package com.wanyue.common.bean;

/**
 * Created by  on 2019/7/24.
 */

public class XingZuoBean {

    private int mNameResId;
    private int mStartMonth;
    private int mStartDay;
    private int mEndMonth;
    private int mEndDay;

    public XingZuoBean(int nameResId, int startMonth, int startDay, int endMonth, int endDay) {
        mNameResId = nameResId;
        mStartMonth = startMonth;
        mStartDay = startDay;
        mEndMonth = endMonth;
        mEndDay = endDay;
    }


    /**
     * <string name="xingzuo_00">白羊座(3.21-4.19)</string>
     * <string name="xingzuo_01">金牛座(4.20-5.20)</string>
     * <string name="xingzuo_02">双子座(5.21-6.21)</string>
     * <string name="xingzuo_03">巨蟹座(6.22-7.22)</string>
     * <string name="xingzuo_04">狮子座(7.23-8.22)</string>
     * <string name="xingzuo_05">处女座(8.23-9.22)</string>
     * <string name="xingzuo_06">天秤座(9.23-10.23)</string>
     * <string name="xingzuo_07">天蝎座(10.24-11.22)</string>
     * <string name="xingzuo_08">射手座(11.23-12.21)</string>
     * <string name="xingzuo_09">摩羯座(12.22-1.19)</string>
     * <string name="xingzuo_10">水瓶座(1.20-2.18)</string>
     * <string name="xingzuo_11">双鱼座(2.19-3.20)</string>
     *
     * @param month
     * @param day
     * @return
     */


    public boolean match(int month, int day) {
        return month == mStartMonth && mStartDay <= day
                || month == mEndMonth && mEndDay >= day;
    }

    public int getNameResId() {
        return mNameResId;
    }
}
