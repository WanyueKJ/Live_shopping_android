package com.wanyue.live.presenter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import com.wanyue.common.HtmlConfig;
import com.wanyue.common.mob.MobShareUtil;
import com.wanyue.common.mob.ShareData;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.R;

/**
 * Created by  on 2018/10/30.
 */

public class UserHomeSharePresenter {

    private Context mContext;
    private MobShareUtil mMobShareUtil;
    private String mToUid;
    private String mToName;
    private String mToAvatarThumb;
    private String mFansNum;

    public UserHomeSharePresenter(Context context) {
        mContext = context;
        mMobShareUtil = new MobShareUtil();
    }

    public UserHomeSharePresenter setToUid(String toUid) {
        mToUid = toUid;
        return this;
    }

    public UserHomeSharePresenter setToName(String toName) {
        mToName = toName;
        return this;
    }

    public UserHomeSharePresenter setAvatarThumb(String avatarThumb) {
        mToAvatarThumb = avatarThumb;
        return this;
    }

    public UserHomeSharePresenter setFansNum(String fansNum) {
        mFansNum = fansNum;
        return this;
    }

    /**
     * 复制页面链接
     */
    public void copyLink() {
        if (TextUtils.isEmpty(mToUid)) {
            return;
        }
        String link = HtmlConfig.SHARE_HOME_PAGE + mToUid;
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", link);
        cm.setPrimaryClip(clipData);
        ToastUtil.show(R.string.copy_success);
    }


    /**
     * 分享页面链接
     */
    public void shareHomePage(String type) {
        if (TextUtils.isEmpty(mToUid)) {
            return;
        }
        ShareData data = new ShareData();
        String title = String.format(WordUtil.getString(R.string.home_page_share_1), mToName, WordUtil.getString(R.string.app_name));
        data.setTitle(title);
        String des = String.format(WordUtil.getString(R.string.home_page_share_2), mFansNum);
        data.setDes(des);
        data.setImgUrl(mToAvatarThumb);
        String webUrl = HtmlConfig.SHARE_HOME_PAGE + mToUid;
        data.setWebUrl(webUrl);
        mMobShareUtil.execute(type, data, null);
    }

    public void release() {
        if (mMobShareUtil != null) {
            mMobShareUtil.release();
        }
        mToUid = null;
        mToName = null;
        mToAvatarThumb = null;
        mFansNum = null;
    }
}
