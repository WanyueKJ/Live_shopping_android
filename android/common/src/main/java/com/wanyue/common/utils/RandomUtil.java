package com.wanyue.common.utils;

import java.util.Random;

/**
 * Created by  on 2018/10/11.
 */

public class RandomUtil {
    private static Random sRandom;

    static {
        sRandom = new Random();
    }

    public static int nextInt(int bound) {
        return sRandom.nextInt(bound);
    }
}
