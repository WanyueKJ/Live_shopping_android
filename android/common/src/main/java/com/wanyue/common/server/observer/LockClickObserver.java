package com.wanyue.common.server.observer;

import android.view.View;

import io.reactivex.disposables.Disposable;


/*将点击控件锁住避免多次网络请求*/
public abstract class LockClickObserver<T> extends DefaultObserver<T> {
     private View mLockView;

    public LockClickObserver(View lockView) {
        mLockView = lockView;
    }
    @Override
    public void onSubscribe(Disposable d) {
        locked();
        super.onSubscribe(d);
    }

    @Override
    public void onNext(T t) {
        unLocked();
        onSucc(t);
    }

    private void locked(){
        if(mLockView!=null){
           mLockView.setEnabled(false);
        }
   }
   public void unLocked(){
       if(mLockView!=null){
          mLockView.setEnabled(true);
       }
       mLockView=null;
   }

    @Override
    public void onComplete() {
        unLocked();
        super.onComplete();
    }
    @Override
    public void onError(Throwable e) {
        unLocked();
        super.onError(e);
    }

    public abstract void onSucc(T t);
}
