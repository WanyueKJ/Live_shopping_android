package com.yunbao.im.views.call;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.yunbao.common.CommonAppContext;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.views.AbsViewHolder;
import com.yunbao.im.R;
import com.yunbao.im.business.CallIMHelper;
import com.yunbao.im.business.ICallPresnter;
import com.yunbao.im.business.IExpotFlowContainer;
import com.yunbao.im.business.IVideoCallView;
import com.yunbao.im.business.WindowAddHelper;
import com.yunbao.im.custom.FloatFrameLayout;
import io.reactivex.functions.Consumer;

public abstract class AbsCallViewHolder extends AbsViewHolder implements View.OnClickListener, IVideoCallView<TXCloudVideoView>, IExpotFlowContainer {

    protected ICallPresnter presnter;
    protected CallWaitViewHolder callWaitViewHolder;
    protected int roomId;
    protected UserBean userBean;
    private WindowAddHelper windowAddHelper;
    public AbsCallViewHolder(Context context, ViewGroup parentView, int roomId,UserBean userBean) {
        super(context, parentView, roomId,userBean);
    }

    @Override
    public void init() {
        setOnClickListner(R.id.btn_close,this);
        setOnClickListner(R.id.btn_narrow,this);
        windowAddHelper=new WindowAddHelper((FragmentActivity) mContext);
    }

    public void showWaitViewHolder(int state) {
        callWaitViewHolder=new CallWaitViewHolder(mContext, (ViewGroup)mContentView,stateCall(),userBean);
        callWaitViewHolder.setAbsCallViewHolder(this);
        callWaitViewHolder.addToParent();
        callWaitViewHolder.changeState(state);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.btn_close){
            if(presnter!=null){
               presnter.exitRoom();
            }
            presnter.exitRoom();
        }else if(id==R.id.btn_narrow){
            checkPermissonOpenNarrow();
        }
    }
    /*开启悬浮窗口*/
    protected  void openNarrow(){
        WindowManager.LayoutParams layoutParams=windowAddHelper.createDefaultWindowsParams(0,100);
        View view=exportFlowView();
        final  WindowManager windowManager  = (WindowManager) CommonAppContext.sInstance
                .getSystemService(CommonAppContext.sInstance.WINDOW_SERVICE);
        if(view!=null){
            layoutParams.width=view.getWidth();
            layoutParams.height=view.getHeight();

            FloatFrameLayout windowsFloatLayout=new FloatFrameLayout(mContext);
            windowsFloatLayout.setView(view);
            windowsFloatLayout.setWmParams(layoutParams);
            windowManager.addView(windowsFloatLayout,layoutParams);
            getActivity().moveTaskToBack(false);//acitivty退到后台
            windowsFloatLayout.setOnNoTouchClickListner(new FloatFrameLayout.OnNoTouchClickListner() {
                @Override
                public void click(View view) {
                    windowAddHelper.moveToFront(mContext);
                    windowManager.removeView(view);
                    AbsCallViewHolder.this.restoreFlowView((FloatFrameLayout) view);
                }
            });
        }
    }

    /*检查悬浮窗权限*/
    private void checkPermissonOpenNarrow() {
       if( windowAddHelper!=null){
           windowAddHelper.checkOverLay().subscribe(new Consumer<Boolean>() {
               @Override
               public void accept(Boolean aBoolean) throws Exception {
                   if(aBoolean){
                     openNarrow();
                   }
               }
           });
       }
    }


    public ICallPresnter getPresnter() {
        return presnter;
    }

    protected  void removeWait(){
        if(callWaitViewHolder!=null){
            callWaitViewHolder.removeFromParent();
            callWaitViewHolder.onDestroy();
            callWaitViewHolder=null;
        }
    }



    @Override
    protected void processArguments(Object... args) {
        super.processArguments(args);
        this.roomId= (int) args[0];
        userBean= (UserBean) args[1];
    }

    public void accept(){
        if(presnter!=null){
           presnter.init();
           presnter.enterRoom(roomId);
           removeWait();
        }
    }

    public void acceptAndBrocast(){
        accept();
        CallIMHelper.sendAccept(stateCall(),userBean.getId());
    }

    public void refuseAndBrocast(){
        CallIMHelper.sendRefuse(stateCall(),userBean.getId());
        refuse();
    }

    public void  refuse(){
        if(presnter!=null){
            presnter.exitRoom();
        }
    }

    public void cancleAndBrocast(){
        cancle();
        CallIMHelper.sendCancle(stateCall(),userBean.getId());
    }

    public void cancle(){
        if(presnter!=null){
            presnter.exitRoom();
        }
    }

    public void setCallPresnter(ICallPresnter presnter) {
        this.presnter = presnter;
    }

    @Override
    public void onExitRoom() {
        ((Activity)(mContext)).finish();
    }
    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presnter=null;
    }
    /*当前的类代表的通话类型,用于传入通话等待界面,确定通话UI*/
    public abstract int stateCall();

}
