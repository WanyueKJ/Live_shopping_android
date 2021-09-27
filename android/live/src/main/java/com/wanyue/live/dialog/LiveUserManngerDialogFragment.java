package com.wanyue.live.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.makeramen.roundedimageview.RoundedImageView;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.AbsActivity;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.dialog.AbsDialogFragment;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.http.ParseHttpCallback;
import com.wanyue.common.interfaces.CommonCallback;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.ViewUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.R;
import com.wanyue.live.activity.LiveActivity;
import com.wanyue.live.bean.LiveUserActionBean;
import com.wanyue.live.bean.LiveUserBean;
import com.wanyue.live.http.LiveHttpConsts;
import com.wanyue.live.http.LiveHttpUtil;
import com.wanyue.live.model.LiveModel;

public class LiveUserManngerDialogFragment extends AbsDialogFragment implements View.OnClickListener {
    private static final int TYPE_AUD_AUD = 1;//观众点别的观众
    private static final int TYPE_ANC_AUD = 2;//主播点观众
    private static final int TYPE_AUD_SELF = 4;//观众点自己
    private static final int TYPE_ANC_SELF = 5;//主播点自己
    private static final int SETTING_ACTION_SELF = 0;//设置 自己点自己
    private static final int SETTING_ACTION_AUD = 30;//设置 普通观众点普通观众 或所有人点超管
    private static final int SETTING_ACTION_ADM = 40;//设置 房间管理员点普通观众
    private static final int SETTING_ACTION_ANC_AUD = 501;//设置 主播点普通观众
    private static final int SETTING_ACTION_ANC_ADM = 502;//设置 主播点房间管理员
    private ImageView mBtnClose;
    private RoundedImageView mImgAvator;
    private TextView mTvUserName;
    private TextView mBtnShutUp;
    private TextView mBtnKicked;
    private TextView mBtnAction;
    private String mId;
    private String mLiveUid;
    private LiveUserBean mLiveUserBean;
    private boolean mSetAdmin;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_live_user_mannger;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }


    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = DpUtil.dp2px(240);
        params.height =DpUtil.dp2px(180);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }
    @Override
    public void init() {
        super.init();
        mBtnClose =  findViewById(R.id.btn_close);
        mImgAvator =  findViewById(R.id.img_avator);
        mTvUserName =  findViewById(R.id.tv_user_name);
        mBtnShutUp = findViewById(R.id.btn_shut_up);
        mBtnKicked =  findViewById(R.id.btn_kicked);
        mBtnAction =  findViewById(R.id.btn_action);
        mBtnShutUp.setOnClickListener(this);
        mBtnKicked.setOnClickListener(this);
        mBtnClose.setOnClickListener(this);
        mBtnAction.setOnClickListener(this);
        mId= getArguments().getString(Constants.KEY_ID);
        getUseData();
    }

    private void getUseData() {
       mLiveUid= LiveModel.getContextLiveUid(getActivity());
        LiveHttpUtil.getLiveUser(mId, mLiveUid, new ParseHttpCallback<JSONObject>() {
            @Override
            public void onSuccess(int code, String msg, JSONObject info) {
                if(isSuccess(code)&&info!=null){
                    mLiveUserBean=  JSON.parseObject(info.toJSONString(),LiveUserBean.class);
                    setUseData(mLiveUserBean);
                    setView(info.getIntValue("action"));

                }
            }
        });
    }

    private void setView(int action){
        if (action==SETTING_ACTION_ANC_AUD){
            ViewUtil.setVisibility(mBtnKicked,View.VISIBLE);
            ViewUtil.setVisibility(mBtnShutUp,View.VISIBLE);
            ViewUtil.setVisibility(mBtnAction,View.VISIBLE);
            mSetAdmin=true;
        }else if (action==SETTING_ACTION_ANC_ADM){
            if (mBtnAction!=null){
                mBtnAction.setText(WordUtil.getString(R.string.live_cancel_manage));
            }
            mSetAdmin=false;
            ViewUtil.setVisibility(mBtnAction,View.VISIBLE);
        }else if (action==SETTING_ACTION_ADM){
            ViewUtil.setVisibility(mBtnKicked,View.VISIBLE);
            ViewUtil.setVisibility(mBtnShutUp,View.VISIBLE);
        }
    }

    private void setUseData(LiveUserBean info) {
        ImgLoader.display(getActivity(),info.getAvatar(),mImgAvator);
        mTvUserName.setText(info.getUserNiceName());
        if(info.getIsshut()==1){
            mBtnShutUp.setText(R.string.live_shut_cancle);
        }else{
            mBtnShutUp.setText(R.string.live_shut_up);
        }
    }

    @Override
    public void onClick(View v) {
        if(!ClickUtil.canClick()){
            return;
        }
        int id=v.getId();
        if(id==R.id.btn_kicked){
            kicked();
        }else if(id==R.id.btn_close){
           dismiss();
        }else if(id==R.id.btn_shut_up){
            shutUp();
        }else if(id==R.id.btn_action){
            setAdmin();
        }
    }

    /**
     * 设置或取消管理员
     */
    private void setAdmin() {
        if (mLiveUserBean==null){
            return;
        }
        LiveHttpUtil.setAdmin(mLiveUid, mId,mSetAdmin, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
                if (isSuccess(code)) {
                    //int res = JSON.parseObject(info[0]).getIntValue("isadmin");
                    /*if (res == 1) {//被设为管理员
                        mAction = SETTING_ACTION_ANC_ADM;
                    } else {//被取消管理员
                        mAction = SETTING_ACTION_ANC_AUD;
                    }*/
                    ((LiveActivity) mContext).sendSetAdminMessage(mSetAdmin, mId, mLiveUserBean.getUserNiceName());
                    dismiss();
                }
            }
        });
    }

    private void kicked() {
        if(mLiveUserBean==null){
            return;
        }
        LiveHttpUtil.kicking(LiveModel.getContextLiveUid(getActivity()),mId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (isSuccess(code)) {
                    ToastUtil.show(getString(R.string.live_kicked3,mLiveUserBean.getUserNiceName()));
                    ((LiveActivity) mContext).kickUser(mId, mLiveUserBean.getUserNiceName());
                    dismiss();
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }

    private void shutUp() {
        if(mLiveUserBean==null){
            return;
        }
       int isShut= mLiveUserBean.getIsshut();
       final boolean isNeedShut=isShut!=1;
       if (isNeedShut){
           setShutUp();
       }else {
           shutUp(isNeedShut,"",WordUtil.getString(R.string.live_admin_shut_up_cancel),null);
       }
    }
    private LiveUserActionDialogFragment mUserActionDialogFragment;

    private void setShutUp(){
        if (mUserActionDialogFragment==null){
            mUserActionDialogFragment=new LiveUserActionDialogFragment();
            mUserActionDialogFragment.setCallBack(new LiveUserActionDialogFragment.OnActionClickListener() {
                @Override
                public void onItemClick(DialogFragment dialog, LiveUserActionBean bean) {
                    shutUp(true,bean.getId(),bean.getName(),dialog);
                }
            });
        }
        mUserActionDialogFragment.show(((AbsActivity) mContext).getSupportFragmentManager(), "ChatAction");
    }

    private void shutUp(final boolean isNeedShut, String shutId, final String name, final DialogFragment dialogFragment){
        LiveHttpUtil.setShutUp(LiveModel.getContextLiveUid(getActivity()),mLiveUserBean.getId(),isNeedShut,shutId,new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (isSuccess(code)) {
                    changeShutUpState(isNeedShut);
                    ((LiveActivity) mContext).setShutUp(mLiveUserBean.getId(), mLiveUserBean.getUserNiceName(), isNeedShut,name);
                    if (isNeedShut&&dialogFragment!=null){
                        dialogFragment.dismiss();
                    }
                    dismiss();
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }


    private void changeShutUpState(boolean isShuted) {
        if(mLiveUserBean!=null){
            int state=isShuted?1:0;
            mLiveUserBean.setIsshut(state);
        }
        if(isShuted){
            ToastUtil.show("禁言成功");
            mBtnShutUp.setText(R.string.live_shut_cancle);
        }else{
            ToastUtil.show("取消禁言成功");
            mBtnShutUp.setText(R.string.live_shut_up);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LiveHttpUtil.cancel(LiveHttpConsts.KICKING);
        LiveHttpUtil.cancel(LiveHttpConsts.SET_SHUT_UP);
        LiveHttpUtil.cancel(LiveHttpConsts.GET_LIVE_USER);
    }
}
