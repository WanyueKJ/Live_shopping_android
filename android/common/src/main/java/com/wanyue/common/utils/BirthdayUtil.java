package com.wanyue.common.utils;

import com.wanyue.common.R;
import com.wanyue.common.bean.XingZuoBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by  on 2019/7/24.
 * 根据生日 啊、判断年龄和星座
 */

public class BirthdayUtil {

    private static final List<XingZuoBean> XZ_LIST;

    static {
        XZ_LIST = new ArrayList<>();
        XZ_LIST.add(new XingZuoBean(R.string.xingzuo_00, 3, 21, 4, 19));
        XZ_LIST.add(new XingZuoBean(R.string.xingzuo_01, 4, 20, 5, 20));
        XZ_LIST.add(new XingZuoBean(R.string.xingzuo_02, 5, 21, 6, 21));
        XZ_LIST.add(new XingZuoBean(R.string.xingzuo_03, 6, 22, 7, 22));
        XZ_LIST.add(new XingZuoBean(R.string.xingzuo_04, 7, 23, 8, 22));
        XZ_LIST.add(new XingZuoBean(R.string.xingzuo_05, 8, 23, 9, 22));
        XZ_LIST.add(new XingZuoBean(R.string.xingzuo_06, 9, 23, 10, 23));
        XZ_LIST.add(new XingZuoBean(R.string.xingzuo_07, 10, 24, 11, 22));
        XZ_LIST.add(new XingZuoBean(R.string.xingzuo_08, 11, 23, 12, 21));
        XZ_LIST.add(new XingZuoBean(R.string.xingzuo_09, 12, 22, 1, 19));
        XZ_LIST.add(new XingZuoBean(R.string.xingzuo_10, 1, 20, 2, 18));
        XZ_LIST.add(new XingZuoBean(R.string.xingzuo_11, 2, 19, 3, 20));
    }

    public static String getXinZuoName(int month, int day) {
        for (XingZuoBean bean : XZ_LIST) {
            if (bean.match(month, day)) {
                return WordUtil.getString(bean.getNameResId());
            }
        }
        return null;
    }

    public static int getAge(int year) {
        int nowYear = Calendar.getInstance().get(Calendar.YEAR);
        return nowYear - year;
    }


}
