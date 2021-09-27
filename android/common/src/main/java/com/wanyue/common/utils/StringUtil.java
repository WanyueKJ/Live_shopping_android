package com.wanyue.common.utils;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.R;
import com.wanyue.common.http.HttpClient;

import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by  on 2018/9/28.
 */

public class StringUtil {
    private static final String KEY = "1ecxXyLRB.COdrAi:q09Z62ash-QGn8VFNIlb=fM/D74WjS_EUzYuw?HmTPvkJ3otK5gp";

    private static DecimalFormat sDecimalFormat;
    private static DecimalFormat sDecimalFormat2;
    // private static Pattern sPattern;
    private static Pattern sIntPattern;
    private static Random sRandom;
    private static StringBuilder sStringBuilder;
    private static StringBuilder sTimeStringBuilder;


    static {
        sDecimalFormat = new DecimalFormat("#.#");
        sDecimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        sDecimalFormat2 = new DecimalFormat("#.##");
        sDecimalFormat2.setRoundingMode(RoundingMode.DOWN);
        //sPattern = Pattern.compile("[\u4e00-\u9fa5]");
        sIntPattern = Pattern.compile("^[-\\+]?[\\d]*$");
        sRandom = new Random();
        sStringBuilder = new StringBuilder();
        sTimeStringBuilder = new StringBuilder();
    }

    public static String format(double value) {
        return sDecimalFormat.format(value);
    }

    public static String toMoney(double m) {
        sStringBuilder.delete(0, sStringBuilder.length());
        String money=String.valueOf(m);
        if (money.contains(".")){
            String[] strings=money.split("\\.");
            sStringBuilder.append(strings[0]);
            if (!strings[1].startsWith("0")){
                sStringBuilder.append(".");
                sStringBuilder.append(strings[1]);
            }
        }else {
            return money;
        }
        return sStringBuilder.toString();
    }

    /**
     * 把数字转化成多少万
     */
    public static String toWan(long num) {
        if (num < 10000) {
            return String.valueOf(num);
        }
        return sDecimalFormat.format(num / 10000d) + "W";
    }


    /**
     * 把数字转化成多少万
     */
    public static String toWan2(long num) {
        if (num < 10000) {
            return String.valueOf(num);
        }
        return sDecimalFormat.format(num / 10000d);
    }

    /**
     * 把数字转化成多少万
     */
    public static String toWan3(long num) {
        if (num < 10000) {
            return String.valueOf(num);
        }
        return sDecimalFormat2.format(num / 10000d) + "w";
    }

//    /**
//     * 判断字符串中是否包含中文
//     */
//    public static boolean isContainChinese(String str) {
//        Matcher m = sPattern.matcher(str);
//        if (m.find()) {
//            return true;
//        }
//        return false;
//    }

    /**
     * 判断一个字符串是否是数字
     */
    public static boolean isInt(String str) {
        if(TextUtils.isEmpty(str)){
            return false;
        }
        return sIntPattern.matcher(str).matches();
    }

    public static boolean isBoolean(String str) {
        if(TextUtils.isEmpty(str)){
            return false;
        }
        return str.equals("false")||str.equals("true");
    }


    /**
     * 把一个long类型的总毫秒数转成时长
     */
    public static String getDurationText(long mms) {
        int hours = (int) (mms / (1000 * 60 * 60));
        int minutes = (int) ((mms % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) ((mms % (1000 * 60)) / 1000);
        sTimeStringBuilder.delete(0, sTimeStringBuilder.length());
        if (hours > 0) {
            if (hours < 10) {
                sTimeStringBuilder.append("0");
            }
            sTimeStringBuilder.append(String.valueOf(hours));
            sTimeStringBuilder.append(":");
        }
        if (minutes > 0) {
            if (minutes < 10) {
                sTimeStringBuilder.append("0");
            }
            sTimeStringBuilder.append(String.valueOf(minutes));
            sTimeStringBuilder.append(":");
        } else {
            sTimeStringBuilder.append("00:");
        }
        if (seconds > 0) {
            if (seconds < 10) {
                sTimeStringBuilder.append("0");
            }
            sTimeStringBuilder.append(String.valueOf(seconds));
        } else {
            sTimeStringBuilder.append("00");
        }
        return sTimeStringBuilder.toString();
    }


    /**
     * 把一个long类型的总毫秒数转成时长
     */
    public static String getDurationText2(long mms) {
        int hours = (int) (mms / (1000 * 60 * 60));
        int minutes = (int) ((mms % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) ((mms % (1000 * 60)) / 1000);
        sTimeStringBuilder.delete(0, sTimeStringBuilder.length());
        if (hours < 10) {
            sTimeStringBuilder.append("0");
        }
        sTimeStringBuilder.append(String.valueOf(hours));
        sTimeStringBuilder.append(":");
        if (minutes > 0) {
            if (minutes < 10) {

                sTimeStringBuilder.append("0");
            }
            sTimeStringBuilder.append(String.valueOf(minutes));
            sTimeStringBuilder.append(":");
        } else {
            sTimeStringBuilder.append("00:");
        }
        if (seconds > 0) {
            if (seconds < 10) {
                sTimeStringBuilder.append("0");
            }
            sTimeStringBuilder.append(String.valueOf(seconds));
        } else {
            sTimeStringBuilder.append("00");
        }
        return sTimeStringBuilder.toString();
    }


    /**
     * 把一个long类型的总毫秒数转成时长
     */
    public static String getDurationText3(long mms) {
        int hours = (int) (mms / (1000 * 60 * 60));
        int minutes = (int) ((mms % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) ((mms % (1000 * 60)) / 1000);
        sTimeStringBuilder.delete(0, sTimeStringBuilder.length());
        if (hours > 0) {
            if (hours < 10) {
                sTimeStringBuilder.append("0");
            }
            sTimeStringBuilder.append(String.valueOf(hours));
            sTimeStringBuilder.append(":");
        }
        if (minutes > 0) {
            if (minutes < 10) {
                sTimeStringBuilder.append("0");
            }
            sTimeStringBuilder.append(String.valueOf(minutes));
            sTimeStringBuilder.append(":");
        } else {
            sTimeStringBuilder.append("00:");
        }
        if (seconds > 0) {
            if (seconds < 10) {
                sTimeStringBuilder.append("0");
            }
            sTimeStringBuilder.append(String.valueOf(seconds));
        } else {
            sTimeStringBuilder.append("00");
        }
        return sTimeStringBuilder.toString();
    }


    /**
     * 把一个long类型的总毫秒数转成时长
     */
    public static String getDurationText4(long mms) {
        int hours = (int) (mms / (1000 * 60 * 60));
        int minutes = (int) ((mms % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) ((mms % (1000 * 60)) / 1000);
        sTimeStringBuilder.delete(0, sTimeStringBuilder.length());
        if (hours > 0) {
            sTimeStringBuilder.append(String.valueOf(hours));
            sTimeStringBuilder.append(WordUtil.getString(R.string.time_hour));
        }
        if (minutes > 0) {
            sTimeStringBuilder.append(String.valueOf(minutes));
            sTimeStringBuilder.append(WordUtil.getString(R.string.time_minute));
        }
        if (seconds > 0) {
            sTimeStringBuilder.append(String.valueOf(seconds));
            sTimeStringBuilder.append(WordUtil.getString(R.string.time_second));
        }
        return sTimeStringBuilder.toString();
    }


    /**
     * 设置视频输出路径
     */
    public static String generateVideoOutputPath() {
        String outputDir = CommonAppConfig.VIDEO_PATH;
        File outputFolder = new File(outputDir);
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }
        return outputDir + generateFileName() + ".mp4";
    }

    /**
     * 获取随机文件名
     */
    public static String generateFileName() {
        return "android_" + CommonAppConfig.getUid() + "_" + DateFormatUtil.getVideoCurTimeString() + sRandom.nextInt(9999);
    }

    /**
     * 多个字符串拼接
     */
    public static String contact(Object... args) {
        sStringBuilder.delete(0, sStringBuilder.length());
        for (Object s : args) {
            sStringBuilder.append(s);
        }
        return sStringBuilder.toString();
    }

    public static String contact(List args) {
        sStringBuilder.delete(0, sStringBuilder.length());
        for (Object s : args) {
            sStringBuilder.append(s.toString());
            sStringBuilder.append(",");
        }
        if(sStringBuilder.length()>0){
           sStringBuilder.deleteCharAt(sStringBuilder.length()-1);
        }
        return sStringBuilder.toString();
    }

    /*自动排序签名,输入的时候不用考虑顺序,避免人工排序带来的失误和时间消耗*/
    public static String createSign(Map<String,Object>map,String...keyArray){
        if(map==null||keyArray==null||keyArray.length==0) {
            return null;
        }
        List<String> wordList = Arrays.asList(keyArray);
        if(!ListUtil.haveData(wordList)){
            return null;
        }
        Collections.sort(wordList,new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });
        sStringBuilder.delete(0, sStringBuilder.length());
        for(String key:wordList){
            Object obj=map.get(key);
            sStringBuilder.append(key)
            .append("=");
            if(obj!=null){
                sStringBuilder.append(obj);
            }
            sStringBuilder.append("&");
        }
        sStringBuilder.append(HttpClient.SALT);
        return MD5Util.getMD5(sStringBuilder.toString());
    }

    public static boolean equals(String str1,String str2){
        if(TextUtils.isEmpty(str1)||TextUtils.isEmpty(str2)){
            //DebugUtil.sendException("两个字符串不能为空，str1=="+str1+"&&str2=="+str2);
            return false;
        }
        boolean equals=str1.equals(str2);
        return equals;
    }

    /*解密url*/
    public static String decryptUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        if (sStringBuilder == null) {
            sStringBuilder = new StringBuilder();
        }
        sStringBuilder.delete(0, sStringBuilder.length());
        for (int i = 0, len1 = url.length(); i < len1; i++) {
            for (int j = 0, len2 = KEY.length(); j < len2; j++) {
                if (url.charAt(i) == KEY.charAt(j)) {
                    if (j - 1 < 0) {
                        sStringBuilder.append(KEY.charAt(len2 - 1));
                    } else {
                        sStringBuilder.append(KEY.charAt(j - 1));
                    }
                }
            }
        }
        return sStringBuilder.toString();
    }


    public static String getPrice(String price){
        if(TextUtils.isEmpty(price)){
            return "";
        }
        return "¥"+price;
    }
    public static List<String> split(String string){
       string=string.replace(" ","");
       StringBuilder stringBuilder=new StringBuilder(string);
       stringBuilder.deleteCharAt(0);
       stringBuilder.deleteCharAt(stringBuilder.length()-1);
       String result=stringBuilder.toString();
       String[]data=result.split(",");
      return ListUtil.asList(data);
    }
    public static String splitJoint(String[]array){
        if(array==null||array.length<=0) {
            return null;
        }
        StringBuilder builder=new StringBuilder();

        for(String temp:array){
            builder.append(temp)
            .append(",");
        }
        int delLength=builder.length()-1;
        builder.deleteCharAt(delLength);
        return builder.toString();
    }


    /*返回html图片集合*/
    public static List<String> returnImageUrlsFromHtml(String htmlCode) {
        List<String> imageSrcList = new ArrayList<String>();
        if(TextUtils.isEmpty(htmlCode)){
            return imageSrcList;
        }
        Pattern p = Pattern.compile("<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic|\\b)\\b)[^>]*>", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlCode);
        String quote = null;
        String src = null;
        while (m.find()) {
            quote = m.group(1);
            src = (quote == null || quote.trim().length() == 0) ? m.group(2).split("//s+")[0] : m.group(2);
            imageSrcList.add(src);
        }
        if (imageSrcList == null || imageSrcList.size() == 0) {
            Log.e("imageSrcList","资讯中未匹配到图片链接");
            return null;
        }
        return imageSrcList;
    }



    public static boolean equalsContainNull(String str1,String str2){
        if(TextUtils.isEmpty(str1)&&TextUtils.isEmpty(str2)){
            return true;
        }else if(!TextUtils.isEmpty(str1)&&!TextUtils.isEmpty(str2)){
            return str1.equals(str2);
        }else {
            return false;
        }
    }

    private static DecimalFormat df;
    public static String getFormatPrice(double price) {
        if(price==0){
            return "¥ 0";
        }
        if(df==null){
            df = new DecimalFormat("#0.00");
        }
        String formatPrice="¥ "+df.format(price);
        return formatPrice;
    }

    public static boolean contains(String key, String tempKeyChild) {
        if(TextUtils.isEmpty(key)||tempKeyChild==null){
            DebugUtil.sendException("key=="+key+"&&tempKeyChild="+tempKeyChild);
            return false;
        }
        return key.contains(tempKeyChild);
    }
}
