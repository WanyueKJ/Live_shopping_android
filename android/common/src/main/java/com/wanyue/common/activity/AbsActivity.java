package com.wanyue.common.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.R;
import com.wanyue.common.business.acmannger.ActivityMannger;
import com.wanyue.common.interfaces.LifeCycleListener;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2017/8/3.
 */

public abstract class AbsActivity extends AppCompatActivity {

    protected String mTag;
    protected Context mContext;
    protected List<LifeCycleListener> mLifeCycleListeners;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTag = this.getClass().getSimpleName();
        getInentParams();
        setStatusBar();
        setContentView(getLayoutId());
        setStatusHeight();
        mContext = this;
        mLifeCycleListeners = new ArrayList<>();
        main(savedInstanceState);
        if (mLifeCycleListeners != null) {
            for (LifeCycleListener listener : mLifeCycleListeners) {
                listener.onCreate();
            }
        }
        addToActivityMannger();
    }

    public void addToActivityMannger(){
        ActivityMannger.getInstance().addActivity(this);
    }

    protected void getInentParams(){

    }

    protected abstract int getLayoutId();

    protected void main(Bundle savedInstanceState) {
        main();
    }

    protected void main() {
        init();
    }

    protected void init() {
    }


    protected boolean isStatusBarWhite() {
        return false;
    }

    protected void setTitle(String title) {
        TextView titleView = (TextView) findViewById(R.id.titleView);
        if (titleView != null) {
            titleView.setText(title);
        }
    }


    public void backClick(View v) {
        if (v.getId() == R.id.btn_back) {
            onBackPressed();
        }
    }

    protected boolean canClick() {
        return ClickUtil.canClick();
    }


    /**
     * 设置透明状态栏
     */
    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (isStatusBarWhite()) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0);
        }
    }
    /*根据不同手机的状态栏设置高度*/
    private void setStatusHeight() {

        View  mFlTab=findViewById(R.id.vp_tab);
        int statusBarHeight= CommonAppConfig.statuBarHeight();
        if(statusBarHeight!=0&&mFlTab!=null){
            mFlTab.setPadding(0,statusBarHeight,0,0);
        }
    }


    @Override
    protected void onDestroy() {
        if (mLifeCycleListeners != null) {
            for (LifeCycleListener listener : mLifeCycleListeners) {
                listener.onDestroy();
            }
            mLifeCycleListeners.clear();
            mLifeCycleListeners = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*这两个方法的顺序千万不能变化*/
        launchfirstOntherStack();
        setIsBackGround();
        if (mLifeCycleListeners != null) {
            for (LifeCycleListener listener : mLifeCycleListeners) {
                listener.onStart();
            }
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mLifeCycleListeners != null) {
            for (LifeCycleListener listener : mLifeCycleListeners) {
                listener.onReStart();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLifeCycleListeners != null) {
            for (LifeCycleListener listener : mLifeCycleListeners) {
                listener.onResume();
            }
        }
        //友盟统计
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLifeCycleListeners != null) {
            for (LifeCycleListener listener : mLifeCycleListeners) {
                listener.onPause();
            }
        }
        //友盟统计
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isFinishing()){
           releaseActivty();
        }
        if (mLifeCycleListeners != null) {
            for (LifeCycleListener listener : mLifeCycleListeners) {
                listener.onStop();
            }
        }
        setIsBackGround();
    }



    private void launchfirstOntherStack(){
        ActivityMannger.getInstance().launchOntherStackToTopActivity(true,this);
    }

    public void setIsBackGround() {
        ActivityMannger.getInstance().setBackGround(SystemUtil.isBackground(this));
    }




    protected  void releaseActivty(){
        L.e("执行releaseActivty()方法,当前Activity=="+getClass().getSimpleName());
        if (mLifeCycleListeners != null) {
            for (LifeCycleListener listener : mLifeCycleListeners) {
                listener.releaseActivty();
            }
        }
        removeToAcitivyMannger();
    }

    public void addLifeCycleListener(LifeCycleListener listener) {
        if (mLifeCycleListeners != null && listener != null) {
            mLifeCycleListeners.add(listener);
        }
    }

    public void addAllLifeCycleListener(List<LifeCycleListener> listeners) {
        if (mLifeCycleListeners != null && listeners != null) {
            mLifeCycleListeners.addAll(listeners);
        }
    }

    public void removeLifeCycleListener(LifeCycleListener listener) {
        if (mLifeCycleListeners != null) {
            mLifeCycleListeners.remove(listener);
        }
    }


    public void startActivity(Class<? extends Activity>cs){
        Intent intent=new Intent(this,cs);
        startActivity(intent);
    }

    public void removeToAcitivyMannger(){
        ActivityMannger.getInstance().removeActivity(this);
    }


}
