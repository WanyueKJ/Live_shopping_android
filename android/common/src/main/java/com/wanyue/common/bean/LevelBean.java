package com.wanyue.common.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by  on 2018/10/9.
 */

public class LevelBean {
    private int level;
    private String thumb;

    @JSONField(name = "level")
    public int getLevel() {
        return level;
    }

    @JSONField(name = "level")
    public void setLevel(int level) {
        this.level = level;
    }

    @JSONField(name = "thumb")
    public String getThumb() {
        return thumb;
    }

    @JSONField(name = "thumb")
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

}
