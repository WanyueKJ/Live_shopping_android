package com.wanyue.live.bean;

/**
 * Created by  on 2019/8/27.
 */

public class LuckPanBean {

    /**
     * id : 2
     * name : 四叶之草
     * thumb : http://yblive.yunbaozb.com/gift_62.png
     * nums : 13
     */

    private String id;
    private String thumb;
    private int nums;
    private String name;
    private String addtime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}
