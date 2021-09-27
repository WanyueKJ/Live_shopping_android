package com.wanyue.common.interfaces;

/**
 * Created by  on 2018/9/26.
 */

public interface LifeCycleListener {

    void onCreate();

    void onStart();

    void onReStart();

    void onResume();

    void onPause();

    void onStop();

    void releaseActivty();

    void onDestroy();
}
