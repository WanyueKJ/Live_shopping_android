package com.wanyue.common.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileWriter;

public class LogUtil {

    public static void print(File file, String content) {
        if (file == null || TextUtils.isEmpty(content)) {
            return;
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(file, true);
            writer.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}