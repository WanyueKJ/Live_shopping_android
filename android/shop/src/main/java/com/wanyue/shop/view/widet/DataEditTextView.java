package com.wanyue.shop.view.widet;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

public class DataEditTextView<T> extends androidx.appcompat.widget.AppCompatEditText implements TextWatcher {
   private T data;
   private OnTextChangeListner<T> mOnTextChangeListner;

    public DataEditTextView(Context context) {
        super(context);
    }
    public DataEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public DataEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addTextChangedListener(this);
    }

    public void setData(T data) {
        this.data = data;
    }


    public void setOnTextChangeListner(OnTextChangeListner<T> onTextChangeListner) {
        mOnTextChangeListner = onTextChangeListner;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
    @Override
    public void afterTextChanged(Editable s) {
        if(mOnTextChangeListner!=null){
           mOnTextChangeListner.textChange(data,s.toString());
        }
    }




    public interface OnTextChangeListner<T>{
        public void textChange(T t,String text);
    }
}
