package com.wanyue.common.bean.commit;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

public abstract class BaseObservableField<T> {
    private TextView textView;
    protected T data;
    private CommitEntity mCommitEntity;
    public BaseObservableField(CommitEntity commitEntity){
        mCommitEntity=commitEntity;
    }

    public T getData() {
        return data;
    }

    /*view和实体类属性进行数据双向绑定*/
    public void bind(TextView textView){
        this.textView=textView;
        if(textView!=null){
            textView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    inputData(s);
                }
            });
        }
    }
    /*data!=null&&newData!=null&&!newData.equals(data)&&*/

    private void inputData(Editable s) {
        T newData=changeData(s.toString());
        data=newData;
        if(mCommitEntity!=null){
           mCommitEntity.observer();
        }
    }

    public void  setData(T t){
        String content=t==null?null:t.toString();
        this.data=t;
        if(textView!=null){
           textView.setText(content);
        }
    }
    @NotNull
    @Override
    public String toString() {
        return data==null?null:data.toString();
    }

    public void release(){
        mCommitEntity=null;
        textView=null;
        data=null;
    }
    public abstract T changeData(String s);
}
