package com.wanyue.live.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.http.ParseHttpCallback;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.views.AbsViewHolder;
import com.wanyue.live.R;
import com.wanyue.live.activity.LiveAnchorActivity;
import com.wanyue.live.activity.LiveAudienceActivity;
import com.wanyue.live.bean.LiveBean;
import com.wanyue.live.http.LiveHttpConsts;
import com.wanyue.live.http.LiveHttpUtil;


/**
 * Created by  on 2018/10/9.
 */

public class LiveEndViewHolder extends AbsViewHolder implements View.OnClickListener {

    private ImageView mAvatar1;
    private ImageView mAvatar2;
    private TextView mName;
    private TextView mDuration;//直播时长
    private TextView mWatchNum;//观看人数

    public LiveEndViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_end;
    }

    @Override
    public void init() {
        mAvatar1 = (ImageView) findViewById(R.id.avatar_1);
        mAvatar2 = (ImageView) findViewById(R.id.avatar_2);
        mName = (TextView) findViewById(R.id.name);
        mDuration = (TextView) findViewById(R.id.duration);
        mWatchNum = (TextView) findViewById(R.id.watch_num);
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    public void showData(LiveBean liveBean, final String stream) {
        if (liveBean != null) {
            mName.setText(liveBean.getUserNiceName());
            ImgLoader.displayBlur(mContext, liveBean.getAvatar(), mAvatar1);
            ImgLoader.displayAvatar(mContext, liveBean.getAvatar(), mAvatar2);
        }
        mParentView.postDelayed(new Runnable() {
            @Override
            public void run() {
                LiveHttpUtil.getLiveEndInfo(stream, new ParseHttpCallback<JSONObject>() {
                    @Override
                    public void onSuccess(int code, String msg, JSONObject info) {
                        if (isSuccess(code)&&info!=null) {
                            mDuration.setText(info.getString("length"));
                            mWatchNum.setText(StringUtil.toWan(info.getLongValue("nums")));
                        }
                    }
                });
            }
        }, 500);
    }


    @Override
    public void onClick(View v) {
        if (mContext instanceof LiveAnchorActivity) {
            ((LiveAnchorActivity) mContext).superBackPressed();
        } else if (mContext instanceof LiveAudienceActivity) {
            ((LiveAudienceActivity) mContext).exitLiveRoom();
        }
    }


    @Override
    public void onDestroy() {
        LiveHttpUtil.cancel(LiveHttpConsts.GET_LIVE_END_INFO);
    }

}
