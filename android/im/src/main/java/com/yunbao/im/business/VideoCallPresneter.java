package com.yunbao.im.business;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.tencent.liteav.TXLiteAVCode;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.im.config.CallConfig;
import com.yunbao.im.config.GenerateTestUserSig;
import java.lang.ref.WeakReference;
import static com.tencent.trtc.TRTCCloudDef.TRTCRoleAnchor;

public class VideoCallPresneter implements ICallPresnter {
    private TRTCCloud trtcCloud;
    private TRTCCloudListenerImpl trtcCloudListener;
    private TRTCCloudDef.TRTCParams tRTCParams;
    private IVideoCallView<TXCloudVideoView> iVideoCallView;
    private CallLivingState callState;
    private int tcRole;

    public VideoCallPresneter(@NonNull IVideoCallView iVideoCallView, int isRole) {
        this.iVideoCallView=iVideoCallView;
        tcRole=parseRole(isRole);
        callState=new CallLivingState();
        initParm();
    }

    private int parseRole(int isRole) {
       /* if(tcRole== Constants.ROLE_ANTHOR){
            return TRTCCloudDef.TRTCRoleAnchor;
        }else{
            return TRTCCloudDef.TRTCRoleAudience;
        }*/
        return TRTCRoleAnchor;
    }

    private void initParm() {
        String userId= CommonAppConfig.getInstance().getUid();
        String userSig= GenerateTestUserSig.genTestUserSig(userId);
        tRTCParams = new TRTCCloudDef.TRTCParams(GenerateTestUserSig.SDKAPPID,
                userId, userSig, -1, "", "");
        tRTCParams.role = tcRole;
    }
    public void init(){
        trtcCloudListener=new TRTCCloudListenerImpl(this);
        trtcCloud = TRTCCloud.sharedInstance(iVideoCallView.getContext());
        trtcCloud.setListener(trtcCloudListener);
        trtcCloud.setVideoEncoderParam(CallConfig.createDefaultBigEncParam());
    }

    public void release(){
        if (trtcCloud == null){
            return;
        }
        trtcCloud.setListener(null);
        trtcCloud.stopAllRemoteView();
        trtcCloud.stopLocalPreview();
        trtcCloud = null;
        iVideoCallView=null;
        TRTCCloud.destroySharedInstance();
    }


    /*腾讯sdk回调监听*/
    static class TRTCCloudListenerImpl extends TRTCCloudListener {
        private WeakReference<VideoCallPresneter> presneterReference;
        public TRTCCloudListenerImpl(VideoCallPresneter presneter) {
            super();
            this.presneterReference=new WeakReference<>(presneter);
        }
        // 错误通知是要监听的，错误通知意味着 SDK 不能继续运行了
        @Override
        public void onError(int errCode, String errMsg, Bundle extraInfo) {
            L.e("sdk callback onError");
            ToastUtil.show("error-code=="+errCode+"&&errMsg="+errMsg);
            VideoCallPresneter presneter = presneterReference.get();
            if(presneter!=null)
            if (errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL) {
                presneter.exitRoom();
             }
      }

        @Override
        public void onUserExit(String s, int i) {
            super.onUserExit(s, i);
            ToastUtil.show("UID=="+s);
            if(i==0){
                VideoCallPresneter presneter = presneterReference.get();
                if(presneter!=null)
                 presneter.exitRoom();
            }
        }

        public void onEnterRoom(long l) {
            if(l>=0){
                VideoCallPresneter presneter = presneterReference.get();
                if(presneter!=null){
                   presneter.enterRoomSuccess();
                }
            }
        }
        @Override
        public void onUserEnter(String s) {
            super.onUserEnter(s);
            L.e("用户进入房间了=="+s);
        }
        @Override
        public void onExitRoom(int i) {
            super.onExitRoom(i);
            VideoCallPresneter presneter = presneterReference.get();
            if(presneter!=null&&presneter.iVideoCallView!=null){
                presneter.iVideoCallView.onExitRoom();
            }
        }

        @Override
        public void onUserVideoAvailable(String userId, boolean available) {
            VideoCallPresneter presneter = presneterReference.get();
            /*当是视频模式的时候进行如下操作*/
            if(presneter!=null&&presneter.getCallState().isVideo){
                if(available&&presneter.callState.isVideo){
                    TXCloudVideoView remoteView =presneter.iVideoCallView.getVideoView(userId);
                    if(remoteView!=null){
                        if(presneter.trtcCloud==null){
                            return;
                        }
                        presneter.trtcCloud.setRemoteViewFillMode(userId, TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FILL);
                        presneter.trtcCloud.startRemoteView(userId, remoteView);
                    }
                        presneter.iVideoCallView.ontherOpenVideo(true);
                }else{
                    if(presneter.trtcCloud==null||presneter.iVideoCallView==null){
                        return;
                    }
                        presneter.iVideoCallView.ontherOpenVideo(false);
                    CallLivingState state=presneter.getCallState();
                        if(!state.isOpenCamera){
                            presneter.trtcCloud.stopRemoteView(userId);
                        }
                }
            }
        }
    }


    /*进入房间成功*/
    private void enterRoomSuccess() {
        TimeModel.getInstance().start();
        ToastUtil.show("进入房间=="+tRTCParams.roomId);
        trtcCloud.startLocalAudio();
        openCamera(true);
        getCallState().isEnterRoom=true;
    }

    /*没登录成功的时候直接调用view层的退出方法,登录成功后等待sdk正常退出*/
    public void exitRoom(){
        if(getCallState().isEnterRoom){
            trtcCloud.exitRoom();
        }else if(iVideoCallView!=null){
            iVideoCallView.onExitRoom();
        }
    }

    @Override
    public void enterRoom(int roomId) {
        if(tRTCParams==null||trtcCloud==null){
            return;
        }
        tRTCParams.roomId=roomId;
        trtcCloud.enterRoom(tRTCParams,TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL);
    }

    @Override
    public void isFront(boolean isFront) {
        if(trtcCloud==null)
            return;
        getCallState().isFront=isFront;
        trtcCloud.switchCamera();
    }

    @Override
    public void openCamera(boolean isOpen) {
        if(trtcCloud==null)
            return;
        CallLivingState callState= getCallState();
        callState.isOpenCamera=isOpen;
        trtcCloud.muteLocalVideo(!isOpen); //相反的
    }

    @Override
    public void isHandsFree(boolean isHandsFree) {
        if(trtcCloud==null)
            return;
        getCallState().isHandsFree=isHandsFree;
        if (isHandsFree) {
            trtcCloud.setAudioRoute(TRTCCloudDef.TRTC_AUDIO_ROUTE_SPEAKER);
        } else {
            trtcCloud.setAudioRoute(TRTCCloudDef.TRTC_AUDIO_ROUTE_EARPIECE);
        }
    }

    @Override
    public void isMute(boolean isMute) {
        if(trtcCloud==null)
            return;
        getCallState().isMute=isMute;
        trtcCloud.muteLocalAudio(isMute);
    }

    @Override
    public CallLivingState getCallState() {

        if(callState==null){
            callState=new CallLivingState();
        }

        return callState;
    }



    @Override
    public void startSDKLocalPreview(boolean isPreview) {
        if(trtcCloud==null)
            return ;
        CallLivingState callState=getCallState();
        callState.isOpenCamera=isPreview;
        callState.isPreView=isPreview;
        if (isPreview){
            TXCloudVideoView txCloudVideoView= iVideoCallView.getMainVideoView();
            trtcCloud.setLocalViewFillMode(TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FILL);
            trtcCloud.startLocalPreview(true, txCloudVideoView);
        }else{
            trtcCloud.stopLocalPreview();
        }
    }
    @Override
    public void setCallView(IVideoCallView callView) {
        this.iVideoCallView=callView;
    }

    @Override
    public void isVideo(boolean isVideo) {
        getCallState().isVideo=isVideo;
    }
}
