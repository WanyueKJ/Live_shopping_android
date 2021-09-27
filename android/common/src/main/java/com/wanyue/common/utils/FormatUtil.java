package com.wanyue.common.utils;

import java.text.DecimalFormat;

public class FormatUtil {
    private static DecimalFormat decimalFormat;
    static {
        decimalFormat=new DecimalFormat(".00");
    }

    public static String format(float value){
       return decimalFormat.format(value);
    }
}
