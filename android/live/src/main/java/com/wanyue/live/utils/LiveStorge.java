package com.wanyue.live.utils;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.live.bean.LiveBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by  on 2018/6/9.
 */

public class LiveStorge {

    private static LiveStorge sInstance;
    private Map<String, List<LiveBean>> mMap;

    private LiveStorge() {
        mMap = new HashMap<>();
    }

    public static LiveStorge getInstance() {
        if (sInstance == null) {
            synchronized (LiveStorge.class) {
                if (sInstance == null) {
                    sInstance = new LiveStorge();
                }
            }
        }
        return sInstance;
    }

    public void put(String key, List<LiveBean> list) {
        if (CommonAppConfig.LIVE_ROOM_SCROLL) {
            if (mMap != null) {
                mMap.put(key, list);
            }
        }
    }


    public List<LiveBean> get(String key) {
        if (mMap != null) {
            return mMap.get(key);
        }
        return null;
    }

    public void remove(String key) {
        if (mMap != null) {
            mMap.remove(key);
        }
    }


    public void clear() {
        if (mMap != null) {
            mMap.clear();
        }
    }

}
