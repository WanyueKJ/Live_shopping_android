package com.yunbao.im.utils;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

import com.yunbao.common.CommonAppContext;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.FaceUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by  on 2018/10/11.
 */

public class ImTextRender {

    private static ForegroundColorSpan sColorSpan1;
    private static AbsoluteSizeSpan sFontSizeSpan4;
    private static final int FACE_WIDTH;
    private static final int VIDEO_FACE_WIDTH;
    private static final String REGEX = "\\[([\u4e00-\u9fa5\\w])+\\]";
    private static final Pattern PATTERN;

    static {
        sColorSpan1 = new ForegroundColorSpan(0xffc8c8c8);
        sFontSizeSpan4 = new AbsoluteSizeSpan(12, true);
        FACE_WIDTH = DpUtil.dp2px(20);
        VIDEO_FACE_WIDTH = DpUtil.dp2px(16);
        PATTERN = Pattern.compile(REGEX);
    }



    /**
     * 聊天表情
     */
    public static CharSequence getFaceImageSpan(String content, int imgRes) {
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        Drawable faceDrawable = ContextCompat.getDrawable(CommonAppContext.sInstance, imgRes);
        faceDrawable.setBounds(0, 0, FACE_WIDTH, FACE_WIDTH);
        ImageSpan imageSpan = new ImageSpan(faceDrawable, ImageSpan.ALIGN_BOTTOM);
        builder.setSpan(imageSpan, 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 聊天内容，解析里面的表情
     */
    public static CharSequence renderChatMessage(String content) {
        Matcher matcher = PATTERN.matcher(content);
        boolean hasFace = false;
        SpannableStringBuilder builder = null;
        while (matcher.find()) {
            // 获取匹配到的具体字符
            String key = matcher.group();
            Integer imgRes = FaceUtil.getFaceImageRes(key);
            if (imgRes != null && imgRes != 0) {
                hasFace = true;
                if (builder == null) {
                    builder = new SpannableStringBuilder(content);
                }
                Drawable faceDrawable = ContextCompat.getDrawable(CommonAppContext.sInstance, imgRes);
                if (faceDrawable != null) {
                    faceDrawable.setBounds(0, 0, FACE_WIDTH, FACE_WIDTH);
                    ImageSpan imageSpan = new ImageSpan(faceDrawable, ImageSpan.ALIGN_BOTTOM);
                    // 匹配字符串的开始位置
                    int startIndex = matcher.start();
                    builder.setSpan(imageSpan, startIndex, startIndex + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        if (hasFace) {
            return builder;
        } else {
            return content;
        }
    }


    /**
     * 评论内容，解析里面的表情
     */
    public static CharSequence renderVideoComment(String content, String addTime) {
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        Matcher matcher = PATTERN.matcher(content);
        while (matcher.find()) {
            // 获取匹配到的具体字符
            String key = matcher.group();
            Integer imgRes = FaceUtil.getFaceImageRes(key);
            if (imgRes != null && imgRes != 0) {
                Drawable faceDrawable = ContextCompat.getDrawable(CommonAppContext.sInstance, imgRes);
                if (faceDrawable != null) {
                    faceDrawable.setBounds(0, 0, VIDEO_FACE_WIDTH, VIDEO_FACE_WIDTH);
                    ImageSpan imageSpan = new ImageSpan(faceDrawable, ImageSpan.ALIGN_BOTTOM);
                    // 匹配字符串的开始位置
                    int startIndex = matcher.start();
                    builder.setSpan(imageSpan, startIndex, startIndex + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        int startIndex = builder.length();
        builder.append(addTime);
        int endIndex = startIndex + addTime.length();
        builder.setSpan(sColorSpan1, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(sFontSizeSpan4, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

}
