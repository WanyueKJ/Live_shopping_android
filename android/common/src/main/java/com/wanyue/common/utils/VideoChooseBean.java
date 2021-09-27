package com.wanyue.common.utils;

public class VideoChooseBean {

    private String videoPath;
    private String videoName;
    private String coverPath;
    private long duration;
    private String durationString;

    public VideoChooseBean() {
    }

    public VideoChooseBean(String videoPath, String videoName, String coverPath, long duration, String durationString) {
        this.videoPath = videoPath;
        this.videoName = videoName;
        this.coverPath = coverPath;
        this.duration = duration;
        this.durationString = durationString;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getDurationString() {
        return durationString;
    }

    public void setDurationString(String durationString) {
        this.durationString = durationString;
    }
}
