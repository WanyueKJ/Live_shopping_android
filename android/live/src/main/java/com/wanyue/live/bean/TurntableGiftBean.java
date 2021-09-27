package com.wanyue.live.bean;

public class TurntableGiftBean {
    /**
     * id : 1
     * type : 1
     * type_val : 200
     * thumb : http://livenewtest.yunbaozb.com/public/app/pay/coin.png
     */

    private String id;
    private int type;
    private String type_val;
    private String thumb;
    private int nums;
    private String name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getType_val() {
        return type_val;
    }

    public void setType_val(String type_val) {
        this.type_val = type_val;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            if(obj instanceof TurntableGiftBean){
                TurntableGiftBean turntableGiftBean= (TurntableGiftBean) obj;
                return id.equals(turntableGiftBean.id);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return super.equals(obj);
    }
    public int getNums() {
        return nums;
    }
    public void setNums(int nums) {
        this.nums = nums;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
