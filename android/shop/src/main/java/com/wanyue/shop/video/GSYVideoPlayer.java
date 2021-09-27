package com.wanyue.shop.video;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.wanyue.common.custom.CheckImageView;
import com.wanyue.shop.R;

public class GSYVideoPlayer extends StandardGSYVideoPlayer {
    private CheckImageView mBtnControll;
    private CheckImageView mBtnAudio;


    public GSYVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public GSYVideoPlayer(Context context) {
        super(context);
    }

    public GSYVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        mBtnAudio = (CheckImageView) findViewById(R.id.btn_audio);
        mBtnControll =findViewById(R.id.btn_controll);
        mBtnControll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               clickStartIcon();
            }
        });
        mBtnAudio.setCheckClickListner(new CheckImageView.OnCheckClickListner() {
            @Override
            public void onCheckClick(CheckImageView view, boolean isChecked) {
                GSYVideoManager.instance().setNeedMute(isChecked);
            }
        });

    }

    @Override
    protected void updateStartImage() {
        super.updateStartImage();
        if (mCurrentState == CURRENT_STATE_PLAYING) {
            mBtnControll.setChecked(true);
        } else if (mCurrentState == CURRENT_STATE_ERROR) {
            mBtnControll.setChecked(false);
        } else {
            mBtnControll.setChecked(false);
        }
    }

    @Override
    public void release() {
        super.release();
        GSYVideoManager.instance().setNeedMute(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_video_layout;
    }
}
