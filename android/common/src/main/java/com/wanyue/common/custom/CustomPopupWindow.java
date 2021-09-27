package com.wanyue.common.custom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

public  class CustomPopupWindow {
    private PopupWindow mPopupWindow;
    private View contentview;
    private  Context mContext;
    public CustomPopupWindow(Builder builder) {
        mContext= builder.mContext;
        contentview = LayoutInflater.from(builder.mContext).inflate(builder.contentviewid,null);
        mPopupWindow =
                new PopupWindow(contentview,builder.width,builder.height,builder.fouse);
        //需要跟 setBackGroundDrawable 结合
        mPopupWindow.setOutsideTouchable(builder.outsidecancel);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setAnimationStyle(builder.animstyle);

    }



    public static void clear(){

    }

    /**
     * popup 消失
     */

    public void dismiss(){
        if (mPopupWindow != null&& mPopupWindow.isShowing()){
            mPopupWindow.dismiss();

        }
    }


    /**
     * 根据id获取view
     * @param viewid
     * @return
     */
    public <T extends View>T getItemView(int viewid){
        if (mPopupWindow != null){
            return this.contentview.findViewById(viewid);
        }
        return null;
    }

    /**
     * 根据父布局，显示位置
     * @param rootviewid
     * @param gravity
     * @param x
     * @param y
     * @return
     */

    public CustomPopupWindow showAtLocation(int rootviewid,int gravity,int x,int y){
        if (mPopupWindow != null){
            View rootview = LayoutInflater.from(mContext).inflate(rootviewid,null);
            mPopupWindow.showAtLocation(rootview,gravity,x,y);
        }
        return this;
    }
    /**
     * 根据id获取view ，并显示在该view的位置
     * @param targetviewId
     * @param gravity
     * @param offx
     * @param offy
     * @return
     */
    public CustomPopupWindow showAsLaction(int targetviewId,int gravity,int offx,int offy){
        if (mPopupWindow != null){
            View targetview = LayoutInflater.from(mContext).inflate(targetviewId,null);
            mPopupWindow.showAsDropDown(targetview,gravity,offx,offy);

        }
        return this;
    }
    /**
     * 显示在 targetview 的不同位置
     * @param targetview
     * @param gravity
     * @param offx
     * @param offy
     * @return
     */
    public CustomPopupWindow showAsLaction(View targetview,int gravity,int offx,int offy){
        if (mPopupWindow != null){
            mPopupWindow.showAsDropDown(targetview,offx,offy,gravity);
        }
        return this;
    }

    /**
     * 根据id设置焦点监听
     * @param viewid
     * @param listener
     */
    public void setOnFocusListener(int viewid,View.OnFocusChangeListener listener){
        View view = getItemView(viewid);
        view.setOnFocusChangeListener(listener);
    }



    public void setDisMissListner(PopupWindow.OnDismissListener disMissListner) {
        if(mPopupWindow!=null){
           mPopupWindow.setOnDismissListener(disMissListner);
        }
    }

    /**
     * builder 类
     */
    
    public static class Builder{
        private int contentviewid;
        private int width;
        private int height;
        private boolean fouse;
        private boolean outsidecancel;
        private int animstyle;
        private Context mContext;
        public Builder(Context context){
            mContext = context;
        }
        public Builder setContentView(int contentviewid){
            this.contentviewid = contentviewid;
            return this;
        }
        public Builder setwidth(int width){
            this.width = width;
            return this;
        }
        public Builder setheight(int height){
            this.height = height;
            return this;
        }
        public Builder setFouse(boolean fouse){
            this.fouse = fouse;
            return this;
        }
        public Builder setOutSideCancel(boolean outsidecancel){
            this.outsidecancel = outsidecancel;
            return this;
        }
        public Builder setAnimationStyle(int animstyle){
            this.animstyle = animstyle;
            return this;
        }



        public CustomPopupWindow builder(){
           return new CustomPopupWindow(this);
        }
    }

}