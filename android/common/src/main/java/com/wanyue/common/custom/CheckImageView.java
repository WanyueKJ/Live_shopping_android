package com.wanyue.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.wanyue.common.R;

public class CheckImageView extends androidx.appcompat.widget.AppCompatImageView implements Checkable {
    public static final int MODE_HTTP_STATE=1;
    public static final int MODE_LOCAL_STATE=2;
    private int state=MODE_LOCAL_STATE;

    private Drawable[]imageResource;
    private boolean isChecked;
    private OnCheckClickListner checkClickListner;

    private boolean enableClick;

    private int position;
    private Object obj;

    public CheckImageView(Context context) {
        super(context);
        init(context);
    }

    public CheckImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrDrawable(context,attrs);
        init(context);
    }


    private void getAttrDrawable(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CheckImageView);
        Drawable drawable=ta.getDrawable(R.styleable.CheckImageView_deault_image);
        if(drawable!=null) {
            addDrawable(drawable,0);
        }
        drawable= ta.getDrawable(R.styleable.CheckImageView_select_image);
        enableClick=ta.getBoolean(R.styleable.CheckImageView_enable_click,true);
        if(drawable!=null) {
            addDrawable(drawable,1);
        }
        ta.recycle();
    }

    public CheckImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrDrawable(context,attrs);
        init(context);
    }

    public void addDrawable(Drawable drawable,int index){
        if(imageResource==null){
           imageResource=new Drawable[2];
        }
        imageResource[index]=drawable;
    }

    public Drawable getImageDrawable(int index) {
        if(imageResource!=null){
            return imageResource[index];
        }
        return null;
    }

    private void init(Context context) {
        refeshUI();
        if(state==MODE_LOCAL_STATE&&enableClick){
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( checkClickListner!=null){
                        change();
                        checkClickListner.onCheckClick((CheckImageView) v,isChecked);

                    }
                }
            });
        }else if(state==MODE_HTTP_STATE&&enableClick){
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkClickListner!=null){
                        checkClickListner.onCheckClick((CheckImageView) v,isChecked);
                    }
                }
            });
        }
    }

    public boolean isHaveDrawable(){
        return imageResource!=null&&imageResource.length>=2&&imageResource[0]!=null;
    }

    public void refeshUI() {
        if(imageResource==null){
            return;
        }
        if(isChecked){
            setImageDrawable(imageResource[1]);
        }else{
            setImageDrawable(imageResource[0]);
        }
    }

    public void change(){
        isChecked=!isChecked;
        refeshUI();
     }

    public void addImageResouce(int defaultImg,int selectImg){
        imageResource[0]= ContextCompat.getDrawable(getContext(),defaultImg);
        imageResource[1]= ContextCompat.getDrawable(getContext(),selectImg);
        refeshUI();
    }


    public void addImageResouce(Drawable[] image){
        if(image==null||(imageResource!=null&&imageResource==image)){
            return;
        }
        imageResource=image;
        refeshUI();
    }



    public void setCheckClickListner(OnCheckClickListner checkClickListner){
        this.checkClickListner=checkClickListner;
    }



    /**
     * @return The current checked state of the view
     */
    @Override
    public boolean isChecked() {
        return isChecked;
    }

    /**
     * Change the checked state of the view to the inverse of its current state
     */
    @Override
    public void toggle() {
        isChecked=!isChecked;
        refeshUI();
    }

    public static interface OnCheckClickListner{
        public void onCheckClick(CheckImageView view, boolean isChecked);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
        refeshUI();
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Object getObj() {
        return obj;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
