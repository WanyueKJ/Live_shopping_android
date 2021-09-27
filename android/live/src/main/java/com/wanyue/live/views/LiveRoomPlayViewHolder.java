package com.wanyue.live.views;

import android.content.Context;
import android.view.ViewGroup;

import com.wanyue.common.views.AbsViewHolder;
import com.wanyue.live.interfaces.ILiveLinkMicViewHolder;

/**
 * Created by  on 2018/10/25.
 */

public abstract class LiveRoomPlayViewHolder extends AbsViewHolder implements ILiveLinkMicViewHolder {

    public LiveRoomPlayViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    public abstract void play(String url);

    public abstract void stopPlay();

    public abstract void stopPlay2();

    public abstract void resumePlay();

    public abstract void pausePlay();

    public abstract void hideCover();

    public abstract void release();

    public abstract void setCover(String coverUrl);

}
