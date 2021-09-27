package com.wanyue.live.music;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by  on 2017/9/2.
 */

public class LiveMusicBean {

    private String id;
    private String name;
    private String artist;
    private int progress;//下载进度

    @JSONField(name = "audio_id")
    public String getId() {
        return id;
    }

    @JSONField(name = "audio_id")
    public void setId(String id) {
        this.id = id;
    }

    @JSONField(name = "audio_name")
    public String getName() {
        return name;
    }

    @JSONField(name = "audio_name")
    public void setName(String name) {
        this.name = name;
    }

    @JSONField(name = "artist_name")
    public String getArtist() {
        return artist;
    }

    @JSONField(name = "artist_name")
    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

}
