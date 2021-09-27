package com.wanyue.common.bean;

public class MeiyanConfig {


    /**
     美白：skin_whiting
     磨皮：skin_smooth
     红润：skin_tenderness
     饱和：skin_saturation


     眉毛：eye_brow
     大眼：big_eye
     眼距：eye_length
     眼角：eye_corner
     开眼角：eye_alat
     瘦脸：face_lift
     削脸：face_shave
     嘴形：mouse_lift
     瘦鼻：nose_lift
     下巴：chin_lift
     额头：forehead_lift
     长鼻：lengthen_noseLift
     亮度:brightness

     0 = "美白"
     1 = "磨皮"
     2 = "红润"
     3 = "大眼"
     3 = "亮度"
     4 = "眉毛"
     5 = "眼距"
     6 = "眼角"
     7 = "瘦脸"
     8 = "嘴型"
     9 = "瘦鼻"
     10 = "下巴"
     11 = "额头"
     12 = "长鼻"
     13 = "削脸"
     14 = "开眼角"






     */

    private int skin_whiting;
    private int skin_smooth;
    private int skin_tenderness;
    private int skin_saturation;
    private int eye_brow;
    private int big_eye;
    private int eye_length;
    private int eye_corner;
    private int eye_alat;
    private int face_lift;
    private int face_shave;
    private int mouse_lift;
    private int nose_lift;
    private int chin_lift;
    private int forehead_lift;
    private int lengthen_noseLift;
    private int brightness;


    public MeiyanConfig(int skin_whiting, int skin_smooth, int skin_tenderness, int brightness, int skin_saturation, int eye_brow, int big_eye, int eye_length, int eye_corner, int eye_alat, int face_lift, int face_shave, int mouse_lift, int nose_lift, int chin_lift, int forehead_lift, int lengthen_noseLift) {
        this.skin_whiting = skin_whiting;
        this.skin_smooth = skin_smooth;
        this.skin_tenderness = skin_tenderness;
        this.brightness = brightness;
        this.skin_saturation = skin_saturation;
        this.eye_brow = eye_brow;
        this.big_eye = big_eye;
        this.eye_length = eye_length;
        this.eye_corner = eye_corner;
        this.eye_alat = eye_alat;
        this.face_lift = face_lift;
        this.face_shave = face_shave;
        this.mouse_lift = mouse_lift;
        this.nose_lift = nose_lift;
        this.chin_lift = chin_lift;
        this.forehead_lift = forehead_lift;
        this.lengthen_noseLift = lengthen_noseLift;
    }

    public int getSkin_whiting() {
        return skin_whiting;
    }

    public void setSkin_whiting(int skin_whiting) {
        this.skin_whiting = skin_whiting;
    }

    public int getSkin_smooth() {
        return skin_smooth;
    }

    public void setSkin_smooth(int skin_smooth) {
        this.skin_smooth = skin_smooth;
    }

    public int getSkin_tenderness() {
        return skin_tenderness;
    }

    public void setSkin_tenderness(int skin_tenderness) {
        this.skin_tenderness = skin_tenderness;
    }

    public int getSkin_saturation() {
        return skin_saturation;
    }

    public void setSkin_saturation(int skin_saturation) {
        this.skin_saturation = skin_saturation;
    }

    public int getEye_brow() {
        return eye_brow;
    }

    public void setEye_brow(int eye_brow) {
        this.eye_brow = eye_brow;
    }

    public int getBig_eye() {
        return big_eye;
    }

    public void setBig_eye(int big_eye) {
        this.big_eye = big_eye;
    }

    public int getEye_length() {
        return eye_length;
    }

    public void setEye_length(int eye_length) {
        this.eye_length = eye_length;
    }

    public int getEye_corner() {
        return eye_corner;
    }

    public void setEye_corner(int eye_corner) {
        this.eye_corner = eye_corner;
    }

    public int getEye_alat() {
        return eye_alat;
    }

    public void setEye_alat(int eye_alat) {
        this.eye_alat = eye_alat;
    }

    public int getFace_lift() {
        return face_lift;
    }

    public void setFace_lift(int face_lift) {
        this.face_lift = face_lift;
    }

    public int getFace_shave() {
        return face_shave;
    }

    public void setFace_shave(int face_shave) {
        this.face_shave = face_shave;
    }

    public int getMouse_lift() {
        return mouse_lift;
    }

    public void setMouse_lift(int mouse_lift) {
        this.mouse_lift = mouse_lift;
    }

    public int getNose_lift() {
        return nose_lift;
    }

    public void setNose_lift(int nose_lift) {
        this.nose_lift = nose_lift;
    }

    public int getChin_lift() {
        return chin_lift;
    }

    public void setChin_lift(int chin_lift) {
        this.chin_lift = chin_lift;
    }

    public int getForehead_lift() {
        return forehead_lift;
    }

    public void setForehead_lift(int forehead_lift) {
        this.forehead_lift = forehead_lift;
    }

    public int getLengthen_noseLift() {
        return lengthen_noseLift;
    }

    public void setLengthen_noseLift(int lengthen_noseLift) {
        this.lengthen_noseLift = lengthen_noseLift;
    }




    public int [] getDataArray(){
        int[]array={skin_whiting,skin_smooth,skin_tenderness,
                brightness,
                big_eye,eye_brow,
                eye_length,eye_corner,eye_alat
                ,face_lift,face_shave,mouse_lift,
                nose_lift,chin_lift,forehead_lift,lengthen_noseLift
            };
        return array;
    }

}
