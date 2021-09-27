package com.wanyue.common.proxy;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.FragmentActivity;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.CommonApplication;
import com.wanyue.common.R;
import com.wanyue.common.utils.L;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public  abstract class BaseViewProxy implements LifeFragmentLisnter {
    public static final String VIEW_POSITION="view_position";

    private FragmentActivity mActivity;
    protected ViewGroup mContentView;
    private ViewGroup mParentLayoutGroup;

    private BaseProxyMannger mViewProxyMannger;
    private BaseProxyMannger mViewProxyChildMannger;
    private boolean mIsAdd; //是否添加进mannger
    private boolean mIsInit; //是否初始化
    private boolean mIsFirstStart=true; //是否是第一次启动
    private boolean isStack; //是否是堆栈模式
    protected boolean mIsUserVisble; //是否对用户可见
    private boolean mIsAddViewPager;  //是否添加到ViewPager
    protected boolean mIsFirstUserVisble=true;

    protected ArrayMap<String, Object> mArgMap;  //保存参数进入
    private Unbinder mUnbinder;

    private BaseViewProxy mParentViewProxy;

    public  BaseViewProxy(){
    }

    public BaseViewProxy getParentViewProxy() {
        return mParentViewProxy;
    }
    public void setParentViewProxy(BaseViewProxy parentViewProxy) {
        mParentViewProxy = parentViewProxy;
    }

     void setStack(boolean stack) {
        isStack = stack;
    }

    public boolean isStack() {
        return isStack;
    }

    /*标记rootView根据手机屏幕状态栏的高度设置高度的padding*/
    public void setDefaultStatusBarPadding(){
        setDefaultStatusBarPadding(R.id.rootView);
    }

    public void setDefaultStatusBarPadding(int rootId){
        View view=findViewById(rootId);
        if(view==null){
            L.e("动态设置状态栏距离需要在根节点标注rootView的标记");
            return;
        }
        int statusHeight= CommonAppConfig.statuBarHeight();
        view.setPadding(view.getPaddingLeft(),statusHeight,view.getPaddingRight(),view.getPaddingBottom());
    }

    public void onSaveInstanceState(@NonNull Bundle outState){

    }


    public boolean isAddViewPager() {
        return mIsAddViewPager;
    }

    public void setAddViewPager(boolean addViewPager) {
        mIsAddViewPager = addViewPager;
    }

    public void finish() {
       Activity activity=getActivity();
       if(activity!=null){
          activity.finish();
       }
    }

    @Override
    public void releaseOpportunity() {
        L.e("releaseOpportunity=="+this.getClass().getSimpleName());
        if(mViewProxyChildMannger!=null){
           mViewProxyChildMannger.releaseOpportunity();
        }
    }

    public ArrayMap<String, Object> getArgMap() {
        if(mArgMap==null){
           mArgMap =new ArrayMap<>();
        }
        return mArgMap;
    }
    public void setArgMap(ArrayMap argMap){
        mArgMap=argMap;
    }



    public BaseProxyMannger getViewProxyChildMannger() {
        if(mViewProxyChildMannger==null){
           mViewProxyChildMannger=new ViewProxyChildMannger(mActivity,this,mViewProxyMannger);
        }
        return mViewProxyChildMannger;
    }
    protected Resources getResources() {
        return CommonApplication.sInstance.getResources();
    }

    public BaseProxyMannger getViewProxyMannger() {
        return mViewProxyMannger;
    }

    /**
     * 被挂载到Activity时的回调方法
     */

    protected void onAttach(Activity activity) {
        this.mActivity = (FragmentActivity) activity;
    }


    public void onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container){
        mContentView= (ViewGroup) inflater.inflate(getLayoutId(),container,false);
        addViewToParent(container);
        initView(mContentView);
    }

    public void addViewToParent(ViewGroup container) {
        Integer position= mArgMap!=null? (Integer) mArgMap.get(VIEW_POSITION) :null;
        if(position==null){
            container.addView(mContentView);
        }else{
            container.addView(mContentView,position);
        }
    }

    public boolean isAdd() {
        return mIsAdd;
    }



    /*有些简单控件的界面不需要butternife降低复杂度,不让他进行绑定,减少辅助类的生成*/
    protected boolean shouldBindButterKinfe() {
        return false;
    }

    protected void initView(ViewGroup contentView){
        if(shouldBindButterKinfe()){
           mUnbinder=ButterKnife.bind(this,contentView);
        }
    }

    public boolean isInit() {
        return mIsInit;
    }

    /*模仿activity启动方式*/
    public void startViewProxy(ArrayMap<String,Object>argMap, @Nullable Class<? extends BaseViewProxy>cs, String tag){
        try {
            BaseViewProxy baseViewProxy= cs.newInstance();
            baseViewProxy.setArgMap(argMap);
            getViewProxyMannger().changeToStack(this);
            if(tag==null){
               tag=baseViewProxy.getDefaultTag();
            }
            mViewProxyMannger.addStack(mParentLayoutGroup,baseViewProxy,tag);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    /*向里面传入参数*/
    public BaseViewProxy putArgs(@Nullable String key,@Nullable Object object){
     if(object==null){
         return this;
     }
     if(mArgMap==null){
        mArgMap=new ArrayMap<>();
     }
        mArgMap.put(key,object);
     return this;
    }

    public void setAdd(boolean add) {
        mIsAdd = add;
    }

    public ViewGroup getParentLayoutGroup() {
        return mParentLayoutGroup;
    }

    public void setParentLayoutGroup(ViewGroup parentLayoutGroup) {
        mParentLayoutGroup = parentLayoutGroup;
    }


    /*设置layoutId*/
  public abstract int getLayoutId();

    @Override
    public void onCreate() {
        if(mIsInit){
            return;
        }
        mIsInit=true;
        onCreateView(mViewProxyMannger.getLayoutInflater(),mParentLayoutGroup);
        if(mViewProxyChildMannger!=null){
           mViewProxyChildMannger.onCreate();
        }
    }


    /*设置为父容器添加view的顺序*/
    public void setAddPosition(int position){
        getArgMap().put(VIEW_POSITION,position);
    }

    @Override
    public void onStart() {
        if(mIsFirstStart){
            firstStart();
            mIsFirstStart=false;
        }
        if(mViewProxyChildMannger!=null){
            mViewProxyChildMannger.onStart();
        }
    }

    protected void firstStart() {
    }

    @Override
    public void onReStart() {
        if(mViewProxyChildMannger!=null){
            mViewProxyChildMannger.onReStart();
        }
    }
    @Override
    public void onResume() {
        if(mViewProxyChildMannger!=null){
            mViewProxyChildMannger.onResume();
        }
    }
    @Override
    public void onPause() {
        if(mViewProxyChildMannger!=null){
            mViewProxyChildMannger.onPause();
        }

    }
    @Override
    public void onStop() {
        if(mViewProxyChildMannger!=null){
            mViewProxyChildMannger.onStop();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

    /*从父容器中将组件移除*/
    public void onRemoveAtParent(){
        if(mParentLayoutGroup!=null&&mContentView!=null&&mContentView.getParent()!=null&&mContentView.getParent()==mParentLayoutGroup){
           mParentLayoutGroup.removeView(mContentView);
        }
    }

    protected <T extends View> T findViewById(int res) {
        return mContentView.findViewById(res);
    }

    public void setOnClickListner(int id, View.OnClickListener clickListener){
        View view=findViewById(id);
        if(view!=null){
            view.setOnClickListener(clickListener);
        }
    }



    public void onAddAtParent(){
        if(mContentView==null||mParentLayoutGroup==null||mContentView.getParent()!=null){
            return;
        }
        mParentLayoutGroup.addView(mContentView);
    }

    @Override
    public void onDestroy() {
        mIsFirstStart=true;
        if(mViewProxyMannger!=null){
           mViewProxyMannger.checkToRemoveFromStack(this);
        }

        if(mViewProxyChildMannger!=null){
           mViewProxyChildMannger.onDestroy();
           mViewProxyChildMannger=null;
        }

        onRemoveAtParent();
        mActivity=null;
        mContentView=null;
        mParentLayoutGroup=null;
        mArgMap=null;
        mViewProxyMannger=null;
        mIsAdd=false;
        mIsInit=false;
        if(mUnbinder!=null){
           mUnbinder.unbind();
           mUnbinder=null;
        }
           isStack=false;
    }

    @Override
    public void onFinish() {
        if(mViewProxyChildMannger!=null){
           mViewProxyChildMannger.onFinish();
        }
    }

    public boolean isFirstStart() {
        return mIsFirstStart;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    public FragmentActivity getActivity() {
        return (FragmentActivity) mActivity;
    }

    /*获取actvity参数*/
    public Intent getIntent(){
        if(mActivity!=null){
            return mActivity.getIntent();
        }
        return null;
    }

    public void startActivity(Class<? extends Activity>cs){
        Intent intent=new Intent(mActivity,cs);
        mActivity.startActivity(intent);
    }

    /*showHide*/
    public void onHiddenChanged(boolean hidden){
        if(mContentView==null||mContentView.getParent()==null){
            return;
        }
        if(hidden){
            mContentView.setVisibility(View.INVISIBLE);
        }else{
            mContentView.setVisibility(View.VISIBLE);
        }
    }

    /*viewPager调用*/
    public void setUserVisibleHint(boolean isVisibleToUser){

        mIsUserVisble=isVisibleToUser;
        if(isVisibleToUser){
            if(mViewProxyMannger!=null){
               mViewProxyMannger.setUserVisibleViewProxy(this);
            }
            if(mViewProxyChildMannger==null){
                return;
            }
            BaseViewProxy baseViewProxy=  mViewProxyChildMannger.getUserVisibleViewProxy();
            if(baseViewProxy!=null){
               baseViewProxy.setUserVisibleHint(true);
            }

        }
    }

    public boolean isUserVisble() {
        return mIsUserVisble;
    }

    public void setViewProxyMannger(BaseProxyMannger viewProxyMannger) {
        mViewProxyMannger = viewProxyMannger;
    }


    public void startActivityForResult(@RequiresPermission Intent intent, int requestCode) {
        if(mViewProxyMannger!=null){
           mViewProxyMannger.startActivityForResult(intent,requestCode,this);
        }
    }


    protected void popStack() {
        getViewProxyMannger().popStack(this);
    }


    public ViewGroup getContentView() {
        return mContentView;
    }

    public String getDefaultTag(){
        return Integer.toString(this.hashCode());
    }

}
