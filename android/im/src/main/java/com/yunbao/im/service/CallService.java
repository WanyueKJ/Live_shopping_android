package com.yunbao.im.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.WindowManager;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.im.business.ICallPresnter;
import com.yunbao.im.business.VideoCallPresneter;
import com.yunbao.im.views.call.AbsCallViewHolder;
import com.yunbao.im.views.call.AudioCallViewHolder;
import com.yunbao.im.views.call.VideoCallViewHolder;
import org.greenrobot.eventbus.EventBus;
import static com.yunbao.common.Constants.CALL_TYPE;
import static com.yunbao.common.Constants.CHAT_TYPE_VIDEO;
import static com.yunbao.common.Constants.DATA;
import static com.yunbao.common.Constants.ROLE;
import static com.yunbao.common.Constants.ROOM_ID;

@Route(path = RouteUtil.PATH_CALL_SERVICE)
public class CallService extends Service {
    private UserBean userBean;
    protected ICallPresnter callPresnter;
    protected AbsCallViewHolder callViewHolder;
    private WindowManager mWindowManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void init(Intent intent) {
        int role=intent.getIntExtra(ROLE,0);
        int roomId=intent.getIntExtra(ROOM_ID,0);
        int callType=intent.getIntExtra(CALL_TYPE,0);
        userBean=intent.getParcelableExtra(DATA);

        if(roomId==0){
          stopSelf();
        }
        if(callType==CHAT_TYPE_VIDEO){
            //callViewHolder=new VideoCallViewHolder(this,null,roomId);
            callViewHolder.addToParent();
            callPresnter.isVideo(true);
        }else {
           // callViewHolder = new AudioCallViewHolder(this, null, roomId);
            callPresnter.isVideo(false);
            callViewHolder.addToParent();
        }
            callPresnter=new VideoCallPresneter(callViewHolder,role);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
