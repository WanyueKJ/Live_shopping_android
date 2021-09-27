package com.wanyue.common.server.observer;

import android.app.Dialog;
import android.content.Context;

import com.wanyue.common.utils.DialogUitl;
import com.wanyue.common.utils.L;

import io.reactivex.disposables.Disposable;

public abstract class DialogObserver<T> extends DefaultObserver<T> {
    private Context context;
    private Dialog dialog;
    private int index;
    public DialogObserver(Context context){
        this.context=context;
    }
    @Override
    public void onSubscribe(Disposable d) {
        showDialog();
    }

    @Override
    public void onNext(T t) {
        disMissDialog();
        onNextTo(t);
    }

    public abstract void onNextTo(T t);


    protected  void showDialog(){
        if(context==null){
            return;
        }
        if(dialog!=null&&dialog.isShowing()){
            return;
        }
        dialog= DialogUitl.loadingDialog(context);
        dialog.show();
    }
    @Override
    public void onComplete() {
        super.onComplete();
        disMissDialog();
    }
    @Override
    public void onError(Throwable e) {
        super.onError(e);
        disMissDialog();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    protected  void disMissDialog(){
        if(dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
            L.e("disMissDialog=="+(++index));
        }
        dialog=null;
        context=null;

    }
}
