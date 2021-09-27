package com.wanyue.live.http;

import com.wanyue.common.http.HttpCallback;
import com.wanyue.live.music.LiveMusicBean;

/**
 * Created by  on 2018/10/20.
 */

public abstract class MusicUrlCallback extends HttpCallback {

    private LiveMusicBean mLiveMusicBean;

    public void setLiveMusicBean(LiveMusicBean bean) {
        mLiveMusicBean = bean;
    }

    public LiveMusicBean getLiveMusicBean() {
        return mLiveMusicBean;
    }
}
