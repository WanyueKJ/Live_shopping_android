package com.wanyue.shop.view.widet;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

public class ThreeCheckImageView extends AppCompatImageView {
    private  boolean enableClick;
    private  OnCheckChangeClickListner mOnCheckChangeClickListner;

    public static final int UN_CHECKED=1;
    public static final int INDETERMINATE_CHECKED=2;
    public static final int CHECKED=3;

    private int mCheck=UN_CHECKED;

    private Drawable[]mDrawableList;

    public ThreeCheckImageView(Context context) {
        super(context);
        init(context);
    }

    public ThreeCheckImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseXml(context,attrs);
        init(context);

    }

    public ThreeCheckImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseXml(context,attrs);

        init(context);
    }

    private void parseXml(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, com.wanyue.common.R.styleable.CheckImageView);
        Drawable drawable=ta.getDrawable(com.wanyue.common.R.styleable.CheckImageView_deault_image);
        if(drawable!=null) {
            addDrawable(drawable,0);
        }
        drawable=ta.getDrawable(com.wanyue.common.R.styleable.CheckImageView_indeterminate_image);
        if(drawable!=null) {
            addDrawable(drawable,1);
        }
        drawable= ta.getDrawable(com.wanyue.common.R.styleable.CheckImageView_select_image);
        if(drawable!=null) {
           addDrawable(drawable,2);
        }
        enableClick=ta.getBoolean(com.wanyue.common.R.styleable.CheckImageView_enable_click,true);
    }

    public void addDrawable(Drawable drawable,int index){
        if(mDrawableList==null){
           mDrawableList=new Drawable[3];
        }
           mDrawableList[index]=drawable;
    }

    public Drawable getImageDrawable(int index) {
        if(mDrawableList!=null){
            return mDrawableList[index];
        }
        return null;
    }


    /*设置选中状态*/
    public void setChecked(int check){
        setCheckedNotifyChange(check,true);
    }




    public void setCheckedNotifyChange(int check,boolean needNotify){
        mCheck=check;
        if(mOnCheckChangeClickListner!=null&&needNotify){
           mOnCheckChangeClickListner.onCheckChange(this,mCheck);
        }
        refeshUI();
    }

    public void setOnCheckChangeClickListner(OnCheckChangeClickListner onCheckChangeClickListner) {
        mOnCheckChangeClickListner = onCheckChangeClickListner;
    }

    public void refeshUI() {
        if(mDrawableList==null){
            return;
        }
        if(mCheck==UN_CHECKED){
            setImageDrawable(mDrawableList[0]);
        }else if(mCheck==INDETERMINATE_CHECKED){
            setImageDrawable(mDrawableList[1]);
        }else if(mCheck==CHECKED){
            setImageDrawable(mDrawableList[2]);
        }
    }

    public int getCheck() {
        return mCheck;
    }

    private void init(Context context){
        refeshUI();
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(enableClick){
                   change();
               }
            }
        });
    }

    public void change() {
        if(mCheck==UN_CHECKED){
           mCheck=INDETERMINATE_CHECKED;
        }else if(mCheck==INDETERMINATE_CHECKED){
            mCheck=CHECKED;
        }else if(mCheck==CHECKED){
            mCheck=UN_CHECKED;
        }
        if(mOnCheckChangeClickListner!=null){
            mOnCheckChangeClickListner.onCheckChange(this,mCheck);
        }
        refeshUI();
    }

    public  interface OnCheckChangeClickListner{
        public void onCheckChange(View view, int state);
    }

}
