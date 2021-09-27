package com.wanyue.live.bean;

import com.wanyue.common.Constants;
import com.wanyue.live.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2018/10/8.
 * 直播房间类型
 */

public class LiveRoomTypeBean {

    private int mId;
    private String mName;
    private int mCheckedIcon;
    private int mUnCheckedIcon;
    private boolean mChecked;

    public LiveRoomTypeBean() {
    }

    public LiveRoomTypeBean(int id, String name) {
        mId = id;
        mName = name;
    }

    public LiveRoomTypeBean(int id, String name, int checkedIcon, int unCheckedIcon) {
        mId = id;
        mName = name;
        mCheckedIcon = checkedIcon;
        mUnCheckedIcon = unCheckedIcon;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getCheckedIcon() {
        return mCheckedIcon;
    }

    public void setCheckedIcon(int checkedIcon) {
        mCheckedIcon = checkedIcon;
    }

    public int getUnCheckedIcon() {
        return mUnCheckedIcon;
    }

    public void setUnCheckedIcon(int unCheckedIcon) {
        mUnCheckedIcon = unCheckedIcon;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    public static List<LiveRoomTypeBean> getLiveTypeList(String[][] liveTypes) {
        List<LiveRoomTypeBean> list = new ArrayList<>();
        if (liveTypes != null) {
            for (String[] arr : liveTypes) {
                LiveRoomTypeBean bean = new LiveRoomTypeBean(Integer.parseInt(arr[0]), arr[1]);
                switch (bean.getId()) {
                    case Constants.LIVE_TYPE_NORMAL:
                        bean.setCheckedIcon(R.mipmap.icon_live_type_normal_1);
                        bean.setUnCheckedIcon(R.mipmap.icon_live_type_normal_2);
                        break;
                    case Constants.LIVE_TYPE_PWD:
                        bean.setCheckedIcon(R.mipmap.icon_live_type_pwd_1);
                        bean.setUnCheckedIcon(R.mipmap.icon_live_type_pwd_2);
                        break;
                    case Constants.LIVE_TYPE_PAY:
                        bean.setCheckedIcon(R.mipmap.icon_live_type_pay_1);
                        bean.setUnCheckedIcon(R.mipmap.icon_live_type_pay_2);
                        break;
                    case Constants.LIVE_TYPE_TIME:
                        bean.setCheckedIcon(R.mipmap.icon_live_type_time_1);
                        bean.setUnCheckedIcon(R.mipmap.icon_live_type_time_2);
                        break;
                }
                list.add(bean);
            }
        }
        return list;
    }
}
