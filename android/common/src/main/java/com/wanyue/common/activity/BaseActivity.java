package com.wanyue.common.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.R;
import com.wanyue.common.business.acmannger.ActivityMannger;
import com.wanyue.common.proxy.ViewProxyMannger;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.HeybroadHelper;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.ResourceUtil;
import com.wanyue.common.utils.SystemUtil;
import com.wanyue.common.utils.WordUtil;
import butterknife.ButterKnife;
import io.reactivex.Observable;

/*为了不破坏旧代码的结构，重新定义了一个基类*/
public abstract class BaseActivity extends RxAppCompatActivity {
    protected ViewProxyMannger mViewProxyMannger;
    protected Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        SystemUtil.setTransparentStatusBar(this);
        setContentView(getLayoutId());
        setStatusHeight();
        if(shouldBindButterKinfe()){
           ButterKnife.bind(this);
        }
        View backView=findViewById(R.id.btn_back);
        if(backView!=null){
            backView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickBack();
                }
            });
        }
        addToActivityMannger();
        init();

    }

    protected void setTabBackGound(Drawable drawable) {
        View view=findViewById(R.id.vp_tab);
        if(view!=null){
           view.setBackground(drawable);
        }
    }

    protected void setTabBackGound(int color) {
        View view=findViewById(R.id.vp_tab);
        if(view!=null){
           view.setBackgroundColor(getResources().getColor(color));
        }
    }

    public void addToActivityMannger(){
        ActivityMannger.getInstance().addActivity(this);
    }
    public void removToAcitivyMannger(){
        ActivityMannger.getInstance().removeActivity(this);
    }


    /*有些界面因为不需要butternife所以可以进行修改,不让他进行绑定,减少辅助类的生成*/
    protected boolean shouldBindButterKinfe() {
        return false;
    }

    /*标记rootView根据手机屏幕状态栏的高度设置高度的padding*/
    public void setDefaultStatusBarPadding(){
     setDefaultStatusBarPadding(R.id.rootView);
    }

    /*标记id根据手机屏幕状态栏的高度设置高度的padding*/
    public void setDefaultStatusBarPadding(int id){
        View view=findViewById(id);
        if(view==null){
            return;
        }
        int statusHeight=CommonAppConfig.statuBarHeight();
        view.setPadding(view.getPaddingLeft(),statusHeight,view.getPaddingRight(),view.getPaddingBottom());
    }


    /*根据不同手机的状态栏设置高度*/
    private void setStatusHeight() {

        View  mFlTab=findViewById(R.id.vp_tab);
        int statusBarHeight= CommonAppConfig.statuBarHeight();
        if(statusBarHeight!=0&&mFlTab!=null){
           mFlTab.setPadding(0,statusBarHeight,0,0);
        }
    }

    public abstract void init();

    @Override
    public void finish() {
        super.finish();
        if(mViewProxyMannger!=null){
           mViewProxyMannger.onFinish();
        }
    }



    /*设置标题*/
    public void setTabTitle(int tabTitle){
       setTabTitle(WordUtil.getString(tabTitle));
    }


    /*绑定生命周期在销毁的时候,以下两种都可以用，效果是一样的*/
    public <T> Observable<T> bindClycleInOnDestory(Observable<T>observable) {
        return observable.compose(this.<T>bindUntilEvent(ActivityEvent.DESTROY));
    }

    /*绑定生命周期在销毁的时候,*/
    public final <T> LifecycleTransformer<T> bindUntilOnDestoryEvent() {
        return bindUntilEvent(ActivityEvent.DESTROY);
    }


    /*设置标题*/
    public void setTabTitle(String tabTitle){
        TextView  mTitleView=findViewById(R.id.titleView);
        if(mTitleView!=null){
            mTitleView.setText(tabTitle);
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewProxyMannger != null && mViewProxyMannger.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public abstract int getLayoutId();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mViewProxyMannger != null) {
            mViewProxyMannger.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*这两个方法的顺序千万不能变化*/
        launchfirstOntherStack();
        setIsBackGround();

        if (mViewProxyMannger != null) {
            mViewProxyMannger.onStart();
        }
    }

    private boolean mIsFirstResume=true;
    @Override
    protected void onResume() {
        super.onResume();
        if(mIsFirstResume){
           mIsFirstResume=false;
            onFirstResume();
        }

        if (mViewProxyMannger != null) {
            mViewProxyMannger.onResume();
        }
    }

    protected  void onFirstResume(){

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                View view = getCurrentFocus();
                HeybroadHelper.hideKeyboard(ev, view, BaseActivity.this);//调用方法判断是否需要隐藏键盘
                break;

            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mViewProxyMannger != null) {
            mViewProxyMannger.onPause();
        }
        if(isFinishing()){
           releaseOpportunity();
        }
    }



    private void launchfirstOntherStack(){
        ActivityMannger.getInstance().launchOntherStackToTopActivity(true,this);
    }

    public void setIsBackGround() {
        ActivityMannger.getInstance().setBackGround(SystemUtil.isBackground(this));
    }


    protected void releaseOpportunity(){
        if (mViewProxyMannger != null) {
            mViewProxyMannger.releaseOpportunity();
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        if(isFinishing()){
          releaseActivty();
        }
        L.e("onStop==="+getClass().getSimpleName());
        if (mViewProxyMannger != null) {
            mViewProxyMannger.onStop();
        }
        setIsBackGround();
    }

    public ViewProxyMannger getViewProxyMannger() {
        if (mViewProxyMannger == null) {
            mViewProxyMannger = new ViewProxyMannger(this);
        }else{
            mViewProxyMannger.attachActivity(this);
        }
        return mViewProxyMannger;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mViewProxyMannger != null) {
            mViewProxyMannger.onDestroy();
        }
        removToAcitivyMannger();
    }

    public void startActivity(Class<? extends Activity>cs){
        Intent intent=new Intent(this,cs);
        startActivity(intent);
    }

    public void startActivityForResult(Class<? extends Activity>cs,int requestCode){
        Intent intent=new Intent(this,cs);
        startActivityForResult(intent,requestCode);
    }

    protected void releaseActivty() {
    }


    public void clickBack() {
        onBackPressed();
    }
}
