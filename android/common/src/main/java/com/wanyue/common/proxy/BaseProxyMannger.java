package com.wanyue.common.proxy;

import android.content.Intent;

import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.FragmentActivity;

import com.wanyue.common.utils.L;
import java.util.ArrayList;
import java.util.List;

public  abstract class BaseProxyMannger implements LifeFragmentLisnter {
    protected FragmentActivity mActivity;
    protected ArrayMap<String, BaseViewProxy> mViewProxyArrayMap;
    protected ArrayMap<ViewGroup,ViewProxyStack>mStackArrayMap;
    protected LayoutInflater mLayoutInflater;
    protected int mCurrentActivityState=LifeObserver.CREATE;
    protected BaseViewProxy mUserVisibleViewProxy;    //存储viewpager当前显示的viewProxy

    public BaseProxyMannger(FragmentActivity activity) {
        mActivity = activity;
    }
    /*根据tag获取BaseViewProxy*/
    public   <T extends BaseViewProxy> BaseViewProxy getViewProxyByTag(String tag){
        if(mViewProxyArrayMap!=null){
            return mViewProxyArrayMap.get(tag);
        }
        return null;
    }

    public void attachActivity(FragmentActivity activity){
        mActivity=activity;
    }

    protected void watchActivityLife(int lifeState){
        this.mCurrentActivityState=lifeState;
    }

    /*加入viewProxy*/
    public boolean  addViewProxy(ViewGroup contentView, @Nullable BaseViewProxy viewProxy, @Nullable String tag){
        L.e("addViewProxy=="+viewProxy.getClass().getSimpleName());
        if(mViewProxyArrayMap==null){
            mViewProxyArrayMap=new ArrayMap<>();
        }
        BaseViewProxy cacheViewProxy=mViewProxyArrayMap.get(tag);
        if(cacheViewProxy!=null||viewProxy.isAdd()){
            return false;
        }
        viewProxy.setAdd(true);
        viewProxy.setViewProxyMannger(this);
        viewProxy.setParentLayoutGroup(contentView);
        viewProxy.onAttach(mActivity);
        mViewProxyArrayMap.put(tag,viewProxy);
        viewProxy.onCreate();
        if(mCurrentActivityState==LifeObserver.START){  //当activity处于onResume状 viewProxy.onStart();
            viewProxy.onStart();
        }else if(mCurrentActivityState==LifeObserver.RESUME){
            viewProxy.onStart();
            viewProxy.onResume();
        }
        return true;
    }

    /*解除ViewProxy*/
    public BaseProxyMannger removeViewProxy(@Nullable BaseViewProxy baseViewProxy){
        if(baseViewProxy==null||!baseViewProxy.isAdd()||!mViewProxyArrayMap.containsValue(baseViewProxy)){
            return this;
        }
        int size=mViewProxyArrayMap.size();
        for(int i=0;i<size;i++){
            if(mViewProxyArrayMap.valueAt(i)==baseViewProxy){
                mViewProxyArrayMap.removeAt(i);
                break;
            }
        }
        if(mCurrentActivityState==LifeObserver.RESUME){
            baseViewProxy.onPause();
            baseViewProxy.onStop();
        }
        if(baseViewProxy.isInit()){
           baseViewProxy.onDestroy();
        }
        return this;
    }

    /*加入回退栈*/
    public boolean  addStack(ViewGroup container,@Nullable BaseViewProxy baseViewProxy,String tag){
        if(!addViewProxy(container,baseViewProxy,tag)){
            return false;
        }
        addStackCollection(container,baseViewProxy);
        return true;
    }

    private void addStackCollection(ViewGroup container, BaseViewProxy baseViewProxy) {
        if(mStackArrayMap==null){
            mStackArrayMap=new ArrayMap<>();
        }
        ViewProxyStack viewProxyStack= mStackArrayMap.get(container);
        if(viewProxyStack==null){
            viewProxyStack=new ViewProxyStack();
            mStackArrayMap.put(container,viewProxyStack);
        }
        baseViewProxy.setStack(true);
        viewProxyStack.addStack(baseViewProxy);
    }


    public  BaseProxyMannger changeToStack(@Nullable BaseViewProxy baseViewProxy){
        if(!baseViewProxy.isAdd()||baseViewProxy.isStack()){
            return this;
        }
        addStackCollection(baseViewProxy.getParentLayoutGroup(),baseViewProxy);
        return this;
    }


    protected void  popStack(@Nullable BaseViewProxy baseViewProxy){
        if(baseViewProxy==null){
            return;
        }
        View groupView=baseViewProxy.getParentLayoutGroup();
        if(groupView==null){
            return;
        }
        ViewProxyStack viewProxyStack= mStackArrayMap.get(groupView);
        if(viewProxyStack==null){
            return;
        }
        int size=viewProxyStack.popStack(baseViewProxy);
        if(size==0){
           removeViewStack(groupView);
        }
        removeViewProxy(baseViewProxy);
    }


    /*判断viewProxy只是存在*/
    private boolean checkViewProxyExist(BaseViewProxy baseViewProxy) {
        return mViewProxyArrayMap.containsValue(baseViewProxy);
    }

    public BaseProxyMannger show(ViewGroup container,@Nullable BaseViewProxy baseViewProxy,String tag){
        if(!checkViewProxyIsInit(baseViewProxy)){
           addViewProxy(container,baseViewProxy,tag);
        }
        baseViewProxy.onHiddenChanged(false);
        return this;
    }

    private boolean checkViewProxyIsInit(BaseViewProxy baseViewProxy) {
        return baseViewProxy.isAdd()&&baseViewProxy.isInit();
    }

    /*动态的切换隐藏*/
    public BaseProxyMannger hide(@Nullable BaseViewProxy baseViewProxy){
        if(baseViewProxy==null||!baseViewProxy.isAdd()||!baseViewProxy.isInit()){
            return this;
        }
        baseViewProxy.onHiddenChanged(true);
        return this;
    }

    public int size(){
        int size=mViewProxyArrayMap==null?0:mViewProxyArrayMap.size();
        return size;
    }

    @Override
    public void onCreate() {
        mCurrentActivityState=LifeObserver.CREATE;
       int size= size();
        for(int i=0;i<size;i++){
            mViewProxyArrayMap.valueAt(i).onCreate();
        }

    }
    @Override
    public void onStart() {
        mCurrentActivityState=LifeObserver.START;
        int size= size();
        for(int i=0;i<size;i++){
            mViewProxyArrayMap.valueAt(i).onStart();
        }
    }

    @Override
    public void onReStart() {
        int size= size();
        for(int i=0;i<size;i++){
            mViewProxyArrayMap.valueAt(i).onReStart();
        }
    }

    @Override
    public void onResume() {
        mCurrentActivityState=LifeObserver.RESUME;
        int size= size();
        for(int i=0;i<size;i++){
            mViewProxyArrayMap.valueAt(i).onResume();
        }
    }

    @Override
    public void onPause() {
        mCurrentActivityState=LifeObserver.PAUSE;
        int size= size();
        for(int i=0;i<size;i++){
            mViewProxyArrayMap.valueAt(i).onPause();
        }
    }

    @Override
    public void onStop() {
        mCurrentActivityState=LifeObserver.STOP;
        int size= size();
        for(int i=0;i<size;i++){
            mViewProxyArrayMap.valueAt(i).onStop();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }


    @Override
    public void onDestroy(){
        mCurrentActivityState=LifeObserver.DESTROY;
        int size= size();
        for(int i=0;i<size;i++){
            mViewProxyArrayMap.valueAt(i).onDestroy();
        }
        mActivity=null;
        mLayoutInflater=null;
        if(mViewProxyArrayMap!=null){
            mViewProxyArrayMap.clear();
            mViewProxyArrayMap=null;
        }
        if(mStackArrayMap!=null){
            mStackArrayMap.clear();
        }

        mUserVisibleViewProxy=null;
    }

    @Override
    public void onFinish() {
        int size= size();
        for(int i=0;i<size;i++){
           mViewProxyArrayMap.valueAt(i).onFinish();
        }
    }

    @Override
    public void releaseOpportunity() {
        int size= size();
        for(int i=0;i<size;i++){
            mViewProxyArrayMap.valueAt(i).releaseOpportunity();
        }
    }

    public void checkToRemoveFromStack(@Nullable BaseViewProxy baseViewProxy){
        if(!baseViewProxy.isStack()||
                baseViewProxy.getParentLayoutGroup()==null
                ||mStackArrayMap.get(baseViewProxy.getParentLayoutGroup())==null){
            return;
        }
      ViewProxyStack viewProxyStack=  mStackArrayMap.get(baseViewProxy.getParentLayoutGroup());

      int size=viewProxyStack.popStack(baseViewProxy);
      if(size==0){
          removeViewStack(baseViewProxy.getParentLayoutGroup());

      }
    }
    /*移除所有依赖这个view的代理器,防止内存泄漏*/
    public  void removeAllBindThisView(View containerView){
        if(containerView==null||mViewProxyArrayMap==null){
            return;
        }
        int size=mViewProxyArrayMap.size();
        List<BaseViewProxy>removeList=new ArrayList<>(size);
        for(int i=0;i<size;i++) {
            BaseViewProxy baseViewProxy = mViewProxyArrayMap.valueAt(i);
            View groupView = baseViewProxy.getParentLayoutGroup();
            if (groupView!=null&&groupView==containerView){
                removeList.add(baseViewProxy);
            }
        }

        for(BaseViewProxy baseViewProxy:removeList){
            removeViewProxy(baseViewProxy);
        }
    }

    /*移除整个zhan*/
    private void removeViewStack(View view) {
        if(mStackArrayMap==null){
            return;
        }
        mStackArrayMap.remove(view);
    }

    @Override
    public boolean onBackPressed() {
        if(mViewProxyArrayMap==null){
            return false;
        }
        int size=mViewProxyArrayMap.size();
        for(int i=0;i<size;i++){
            boolean interceptBackPress= mViewProxyArrayMap.valueAt(i).onBackPressed();
            if(interceptBackPress){     //是否拦截回退方法
               return interceptBackPress;
            }
        }
        return false;
    }
    protected  abstract void startActivityForResult(@RequiresPermission Intent intent, int requestCode, BaseViewProxy baseViewProxy);

    public abstract LayoutInflater getLayoutInflater();

    public void setUserVisibleViewProxy(BaseViewProxy baseViewProxy) {
        this.mUserVisibleViewProxy=baseViewProxy;
    }

    public BaseViewProxy getUserVisibleViewProxy() {
        return mUserVisibleViewProxy;
    }
}
