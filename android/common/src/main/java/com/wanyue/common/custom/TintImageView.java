package com.wanyue.common.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

public class TintImageView extends androidx.appcompat.widget.AppCompatImageView {
    private Drawable mTintDrawable;
    public TintImageView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setTint();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }


        return super.onTouchEvent(event);
    }

    private void setTint() {
       Drawable drawable= getDrawable();




    }
}
