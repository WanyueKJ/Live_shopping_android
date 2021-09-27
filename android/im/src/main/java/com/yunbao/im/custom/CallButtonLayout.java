package com.yunbao.im.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunbao.common.utils.DpUtil;
import com.yunbao.im.R;

public class CallButtonLayout extends LinearLayout {
    private int resouceId;
    private Drawable resouceDrawable;
    private String title;
    private float imgSize;
    private int textColor;


    private TextView tvTitle;
    private ImageView imgIcon;

    public CallButtonLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_call_button,this,true);
        tvTitle=findViewById(R.id.tv_text);
        imgIcon=findViewById(R.id.img_icon);
        tvTitle.setText(title);
        if(resouceDrawable!=null){
            imgIcon.setImageDrawable(resouceDrawable);
        }else {
            imgIcon.setImageResource(resouceId);
        }

        if(imgSize!=0){
            imgIcon.getLayoutParams().height= (int) imgSize;
            imgIcon.getLayoutParams().width= (int) imgSize;
        }
        if(textColor!=0)
        tvTitle.setTextColor(textColor);
    }

    public CallButtonLayout(Context context,  AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CallButtonLayout);
        resouceDrawable = ta.getDrawable(R.styleable.CallButtonLayout_call_icon);
        title = ta.getString(R.styleable.CallButtonLayout_call_text);
        imgSize= ta.getDimension(R.styleable.CallButtonLayout_call_size, DpUtil.dp2px(70));
        textColor= ta.getColor(R.styleable.CallButtonLayout_call_text_color, context.getResources().getColor(R.color.gray1));
        ta.recycle();
        init(context);
    }
    public CallButtonLayout(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CallButtonLayout);
        resouceDrawable = ta.getDrawable(R.styleable.CallButtonLayout_call_icon);
        title = ta.getString(R.styleable.CallButtonLayout_call_text);
        imgSize= ta.getDimension(R.styleable.CallButtonLayout_call_size,DpUtil.dp2px(70));
        textColor= ta.getColor(R.styleable.CallButtonLayout_call_text_color, context.getResources().getColor(R.color.gray1));
        ta.recycle();
        init(context);
    }

    public CallButtonLayout setImageICon(int resouceId){
        imgIcon.setImageResource(resouceId);
        return this;
    }

    public CallButtonLayout setText(String text){
        tvTitle.setText(text);
        return this;
    }


    /*设置button的数据*/
    public void setButtonEntity( CallButtonEntity callButtonEntity){
        if(callButtonEntity!=null){
            setImageICon(callButtonEntity.icon);
            setText(callButtonEntity.title);
            setOnClickListener(callButtonEntity.onClickListener);
        }else{
            setImageICon(0);
            setText(null);
            setOnClickListener(null);
        }

    }

    public static class CallButtonEntity{
        public String title;
        private int icon;
        private OnClickListener onClickListener;

        public CallButtonEntity(String title, int icon, OnClickListener onClickListener) {
            this.title = title;
            this.icon = icon;
            this.onClickListener = onClickListener;
        }

    }
}
