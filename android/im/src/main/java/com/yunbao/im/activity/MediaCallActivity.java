package com.yunbao.im.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.event.AcceptCallEvent;
import com.yunbao.common.event.CancleCallEvent;
import com.yunbao.common.event.RefuseCallEvent;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.im.R;
import com.yunbao.im.business.ICallPresnter;
import com.yunbao.im.business.VideoCallPresneter;
import com.yunbao.im.config.CallConfig;
import com.yunbao.im.event.VideoAllCloseEvent;
import com.yunbao.im.views.call.AbsCallViewHolder;
import com.yunbao.im.views.call.AudioCallViewHolder;
import com.yunbao.im.views.call.VideoCallViewHolder;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import static com.yunbao.common.Constants.CALL_TYPE;
import static com.yunbao.common.Constants.DATA;
import static com.yunbao.common.Constants.ROLE;
import static com.yunbao.common.Constants.ROOM_ID;

@Route(path = RouteUtil.PATH_CALL_ACTIVITY)
public class MediaCallActivity extends AbsActivity {
    public static boolean isBusy;
    protected FrameLayout container;
    protected ICallPresnter callPresnter;
    protected AbsCallViewHolder callViewHolder;
    private UserBean userBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void main() {
        super.main();
        isBusy=true;
        EventBus.getDefault().register(this);
        container = findViewById(R.id.container);
        Intent intent=getIntent();
        int role=intent.getIntExtra(ROLE,0);
        int roomId=intent.getIntExtra(ROOM_ID,0);
        int callType=intent.getIntExtra(CALL_TYPE,0);
        userBean=intent.getParcelableExtra(DATA);
        if(roomId==0){
            finish();
        }
        if(callType== Constants.CHAT_TYPE_VIDEO){
            callViewHolder=new VideoCallViewHolder(this,container,roomId,userBean);
            callViewHolder.addToParent();
            callViewHolder.subscribeActivityLifeCycle();
        }else {
            callViewHolder = new AudioCallViewHolder(this, container, roomId,userBean);
            callViewHolder.addToParent();
            callViewHolder.subscribeActivityLifeCycle();
        }
            callPresnter=new VideoCallPresneter(callViewHolder,role);
            callPresnter.isVideo(callType==Constants.CHAT_TYPE_VIDEO);
            callViewHolder.setCallPresnter(callPresnter);

        int state=role==Constants.ROLE_ANTHOR? CallConfig.CALL:CallConfig.WAIT;
        /*弹出等待界面*/
        callViewHolder.showWaitViewHolder(state);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_media_call;
    }

    public static void forward(Context context, int role, String roomId, int callType, UserBean userBean){
        Intent intent=new Intent(context,MediaCallActivity.class);
        intent.putExtra(ROLE,role);
        intent.putExtra(ROOM_ID,roomId);
        intent.putExtra(CALL_TYPE,callType);
        intent.putExtra(DATA,userBean);
        context.startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void registerAcceptCallEvent(AcceptCallEvent event){
        callViewHolder.accept();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void registerCancleCallEvent(CancleCallEvent event){
        callViewHolder.cancle();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void registerRefuseCallEvent(RefuseCallEvent event){
        callViewHolder.refuse();
    }
    /*两方视频窗口都停止推流的时候会回调到这里*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void registerCloseEvent(VideoAllCloseEvent event){

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isBusy=false;
        callPresnter.release();
        EventBus.getDefault().unregister(this);
    }

}
