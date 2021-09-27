package com.wanyue.live.music;

import android.text.TextUtils;

import com.wanyue.common.CommonAppConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by  on 2018/10/22.
 */

public class LrcParser {

    private static Pattern sPattern;

    static {
        sPattern = Pattern.compile("\\[(\\d{2}:\\d{2}\\.\\d{2})\\]");
    }

    public static List<LrcBean> getLrcListByMusicId(String musicId) {
        File file = new File(CommonAppConfig.MUSIC_PATH + musicId + ".lrc");
        if (!file.exists()) {
            return null;
        }
        BufferedReader br = null;
        String lrcString = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            lrcString = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (TextUtils.isEmpty(lrcString)) {
            return null;
        }
        return getLrcList(lrcString);
    }


    public static List<LrcBean> getLrcList(String lrcString) {
        List<LrcBean> list = new ArrayList<>();
        String[] arr = lrcString.split("\\[");
        for (String line : arr) {
            LrcBean bean = parserLine("[" + line);
            if (bean != null) {
                list.add(bean);
            }
        }
        return list;
    }


    /**
     * 利用正则表达式解析每行具体语句
     */
    private static LrcBean parserLine(String str) {
        if (!TextUtils.isEmpty(str) && !str.startsWith("[ti:") && !str.startsWith("[ar:") && !str.startsWith("[al:")) {
            Matcher matcher = sPattern.matcher(str);
            // 如果存在匹配项，则执行以下操作
            if (matcher.find()) {
                long startTime = strToLong(matcher.group(1));
                // 得到时间点后的歌词内容
                String lrc = "";
                String[] arr = sPattern.split(str);
                if (arr.length > 1) {
                    lrc = arr[1];
                }
                return new LrcBean(startTime, lrc);
            }
        }
        return null;
    }


    /**
     * 将解析得到的表示时间的字符转化为int型
     */
    private static long strToLong(String timeStr) {
        // 因为给如的字符串的时间格式为XX:XX.XX,返回的long要求是以毫秒为单位
        // 1:使用：分割 2：使用.分割
        String[] s = timeStr.split(":");
        int min = Integer.parseInt(s[0]);
        String[] ss = s[1].split("\\.");
        int sec = Integer.parseInt(ss[0]);
        int mill = Integer.parseInt(ss[1]);
        return min * 60 * 1000 + sec * 1000 + mill * 10;
    }
}
