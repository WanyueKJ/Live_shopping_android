package com.wanyue.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 敏感词过滤
 */
public class WordFilterUtil {

    private Map<String, Object> mMap;
    private StringBuilder mStringBuilder;

    private static WordFilterUtil sInstance;

    private WordFilterUtil() {
        mStringBuilder = new StringBuilder();
    }


    public static WordFilterUtil getInstance() {
        if (sInstance == null) {
            synchronized (WordFilterUtil.class) {
                if (sInstance == null) {
                    sInstance = new WordFilterUtil();
                }
            }
        }
        return sInstance;
    }


    /**
     * 生成关键词字典库
     */
    public void initWordMap(List<String> wordList) {
        if (wordList == null || wordList.size() == 0) {
            return;
        }
        mMap = new HashMap<>(wordList.size());
        Map<String, Object> curMap = null;
        for (String word : wordList) {
            curMap = mMap;
            for (int i = 0, len = word.length(); i < len; i++) {
                String wordChar = String.valueOf(word.charAt(i));
                Map<String, Object> subMap = (Map<String, Object>) curMap.get(wordChar);
                if (subMap == null) {
                    subMap = new HashMap<>(2);
                    subMap.put("isEnd", "0");
                    curMap.put(wordChar, subMap);
                }
                curMap = subMap;
                if (i == len - 1) {
                    curMap.put("isEnd", "1");
                }
            }
        }

    }

    /**
     * 检查是否包含敏感词
     *
     * @param content
     * @param beginIndex
     * @return 敏感词的长度
     */
    private int checkWord(String content, int beginIndex) {
        boolean isEnd = false;
        int wordLength = 0;
        Map<String, Object> curMap = mMap;
        for (int i = beginIndex, len = content.length(); i < len; i++) {
            String wordChar = String.valueOf(content.charAt(i));
            curMap = (Map<String, Object>) curMap.get(wordChar);
            if (curMap == null) {
                break;
            } else {
                wordLength++;
                if ("1".equals(curMap.get("isEnd"))) {
                    isEnd = true;
                }
            }
        }

        if (!isEnd) {
            wordLength = 0;
        }
        return wordLength;
    }

    /**
     * 过滤文本
     *
     * @param content
     * @return 过滤后的文本
     */
    public String filter(String content) {
        if (mMap == null || mMap.size() == 0) {
            return content;
        }
        List<String> list = null;
        for (int i = 0, len = content.length(); i < len; i++) {
            int wordLength = checkWord(content, i);
            if (wordLength > 0) {
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(content.substring(i, i + wordLength));
                i += wordLength - 1;
            }
        }
        if (list != null) {
            for (String word : list) {
                String replaceString = getReplaceString(word.length());
                content = content.replace(word, replaceString);
            }
        }
        return content;
    }


    private String getReplaceString(int len) {
        mStringBuilder.delete(0, mStringBuilder.length());
        for (int i = 0; i < len; i++) {
            mStringBuilder.append("*");
        }
        return mStringBuilder.toString();
    }

}
