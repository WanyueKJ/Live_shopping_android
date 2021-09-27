package com.wanyue.live.music;

/**
 * Created by  on 2018/10/22.
 */

public class LrcBean {

    private int index;
    private long startTime;//歌词的起始时间
    private long endTime;//歌词的结束时间
    private float duration;//歌词的持续时长
    private String lrc;//歌词
    private float progress;//歌词进度

    public LrcBean(long startTime, String lrc) {
        this.startTime = startTime;
        this.lrc = lrc;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
        this.duration = endTime - this.startTime;
    }

    public float getDuration() {
        return duration;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
}
