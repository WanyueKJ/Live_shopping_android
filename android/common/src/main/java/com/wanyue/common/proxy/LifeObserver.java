package com.wanyue.common.proxy;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import android.util.Log;

public class LifeObserver implements LifecycleObserver {
    private static final String TAG="LifeObserver";
    public static final int CREATE=1;
    public static final int START=2;
    public static final int RESUME=3;
    public static final int PAUSE=4;
    public static final int STOP=5;
    public static final int DESTROY=6;
    protected int mState;
    private int mHashCode;


    public LifeObserver(){
        LifecycleOwner lifecycleOwner;

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreated(){
        Log.d(TAG, "onCreated: ");
        mState=CREATE;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart(){
        Log.d(TAG, "onStart: ");
        mState=START;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume(){
        Log.d(TAG, "onResume: ");
        mState=RESUME;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause(){
        Log.d(TAG, "onPause: ");
        mState=CREATE;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop(){
        Log.d(TAG, "onStop: ");
        mState=CREATE;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    public void onAny(Lifecycle.Event event){//此方法可以有参数，但类型必须如两者之一(LifecycleOwner owner,Lifecycle.Event event)
        Log.d(TAG, "onAny: ");
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestory(){//此方法可以有参数，但类型必须如两者之一(LifecycleOwner owner,Lifecycle.Event event)
        Log.d(TAG, "ONDESTROY: ");
        mState=CREATE;
    }
}