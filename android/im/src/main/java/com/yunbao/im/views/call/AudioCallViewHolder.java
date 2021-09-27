package com.yunbao.im.views.call;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.yunbao.common.Constants;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.custom.CheckImageView;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.im.R;
import com.yunbao.im.business.TimeModel;
import com.yunbao.im.custom.FloatFrameLayout;

public class AudioCallViewHolder extends AbsCallViewHolder {

    private RoundedImageView imgAvator;
    private TextView tvName;
    private TextView tvState;
    private LinearLayout btnMute;
    private CheckImageView ciMute;
    private LinearLayout btnHandsFree;
    private CheckImageView ciHandsFrees;
    private TextView tvCameraToggle;
    private LinearLayout btnFlowTime;
    private TextView tvFlowTime;


    private TimeModel.TimeListner timeListner;


    public AudioCallViewHolder(Context context, ViewGroup parentView, int roomId, UserBean userBean) {
        super(context, parentView, roomId, userBean);
    }

    @Override
    public int stateCall() {
        return Constants.CHAT_TYPE_AUDIO;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.view_audio_call;
    }


    @Override
    public void init() {
        super.init();
        imgAvator = (RoundedImageView) findViewById(R.id.img_avator);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvState = (TextView) findViewById(R.id.tv_state);
        btnMute = (LinearLayout) findViewById(R.id.btn_mute);
        ciMute = (CheckImageView) findViewById(R.id.ci_mute);
        btnHandsFree = (LinearLayout) findViewById(R.id.btn_hands_free);
        ciHandsFrees = (CheckImageView) findViewById(R.id.ci_hands_frees);
        tvCameraToggle = (TextView) findViewById(R.id.tv_camera_toggle);
        btnFlowTime = (LinearLayout) findViewById(R.id.btn_flow_time);
        tvFlowTime = (TextView) findViewById(R.id.tv_flow_time);



        timeListner=new TimeModel.TimeListner() {
            @Override
            public void time(String string) {
                tvState.setText(string);
                if(tvFlowTime.getVisibility()==View.VISIBLE){
                   tvFlowTime.setText(string);
                }
            }
        };
        setOnClickListner(R.id.btn_hands_free,this);
        setOnClickListner(R.id.btn_mute,this);
        TimeModel.getInstance().addTimeListner(timeListner);
        setUserData();
    }

    private void setUserData() {
        if(userBean!=null){
            ImgLoader.display(mContext,userBean.getAvatar(),imgAvator);
            tvName.setText(userBean.getUserNiceName());
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id=v.getId();
        if(id==R.id.btn_mute){
            boolean isMute= !presnter.getCallState().isMute;
            presnter.isMute(isMute);
            ciMute.setChecked(isMute);
        }else if(id==R.id.btn_hands_free){
            boolean isHandsFree= !presnter.getCallState().isHandsFree; //callPresnter层存储相关变量进行toggle
            presnter.isHandsFree(isHandsFree);
            ciHandsFrees.setChecked(isHandsFree);
        }
    }

    @Override
    public TXCloudVideoView getVideoView(String id) {
        return null;
    }
    @Override
    public TXCloudVideoView getMainVideoView() {
        return null;
    }

    @Override
    public void ontherOpenVideo(boolean isOpen) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TimeModel.getInstance().removeTimeListner(timeListner);
    }

    @Override
    public View exportFlowView() {
        btnFlowTime.setVisibility(View.VISIBLE);
        return btnFlowTime;
    }

    @Override
    public void restoreFlowView(FloatFrameLayout floatFrameLayout) {
        try {
            floatFrameLayout.removeView(btnFlowTime);
            ViewGroup viewGroup= (ViewGroup) mContentView;
            viewGroup.addView(btnFlowTime);
            btnFlowTime.setVisibility(View.INVISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
