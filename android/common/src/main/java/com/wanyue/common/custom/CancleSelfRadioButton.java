package com.wanyue.common.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class CancleSelfRadioButton extends RadioButton {

    private boolean canCancleSelf;
    public CancleSelfRadioButton(Context context) {
        super(context);
    }

    public CancleSelfRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CancleSelfRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CancleSelfRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setCanCancleSelf(boolean canCancleSelf) {
        this.canCancleSelf = canCancleSelf;
    }

    @Override
    public void toggle() {
        if(canCancleSelf){
            setChecked(!isChecked());
            if (!isChecked()) {
                ((RadioGroup) getParent()).clearCheck();
            }
        }else{
            super.toggle();
        }
    }

}
