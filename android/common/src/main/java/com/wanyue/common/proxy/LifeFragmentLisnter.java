package com.wanyue.common.proxy;

import android.content.Intent;
import androidx.annotation.Nullable;

public interface LifeFragmentLisnter {

    public void onCreate();
    public  void onStart();
    public void onReStart();
    public void onResume();
    public void onPause();
    public void onStop();

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);
    public boolean onBackPressed();

    public void onFinish();
    /*资源释放时机*/
    public void releaseOpportunity();
    public void onDestroy();

}
