package com.wanyue.live.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.api.CommonAPI;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.event.FollowEvent;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.http.ParseHttpCallback;
import com.wanyue.common.http.ParseSingleHttpCallback;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.ResourceUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.R;
import com.wanyue.live.activity.LiveActivity;
import com.wanyue.live.activity.LiveAudienceActivity;
import com.wanyue.live.activity.LiveStoreActivity;
import com.wanyue.live.http.LiveHttpConsts;
import com.wanyue.live.http.LiveHttpUtil;
import com.wanyue.live.model.LiveModel;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.view.pop.BaseCenterPopView;
import com.wanyue.shop.view.view.GoodsHandleNoCartViewProxy;
import com.wanyue.shop.view.view.GoodsHandleViewProxy;

import org.greenrobot.eventbus.EventBus;

import static com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext;

public class LiveAnthorUserDialogPop extends BaseCenterPopView implements View.OnClickListener {
    private static final int SETTING_ACTION_SUP = 60;//设置 超管点主播
    private static final int TYPE_AUD_ANC = 3;//观众点主播
    private ImageView mBtnClose;
    private ImageView mImgAvator;
    private TextView mTvUserName;
    private TextView mTvId;
    private TextView mBtnPrivateMsg;
    private TextView mBtnFollow;
    private TextView mTvFollowFans;
    private UserBean mUserBean;
    private int mIsFollow;
    private TextView mBtnStore;
    private TextView mActionClose;
    private String mLiveUid;



    public LiveAnthorUserDialogPop(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_live_anthor_user;
    }

    @Override
    protected void init() {
        super.init();
        mBtnClose = (ImageView) findViewById(R.id.btn_close);
        mImgAvator = (ImageView) findViewById(R.id.img_avator);
        mTvUserName = (TextView) findViewById(R.id.tv_user_name);
        mTvId = (TextView) findViewById(R.id.tv_id);
        mBtnPrivateMsg = (TextView) findViewById(R.id.btn_private_msg);
        mBtnFollow = (TextView) findViewById(R.id.btn_follow);
        mTvFollowFans = (TextView) findViewById(R.id.tv_follow_fans);
        mActionClose = (TextView) findViewById(R.id.btn_close_live);
        mBtnStore = (TextView) findViewById(R.id.btn_store);

        mBtnPrivateMsg.setOnClickListener(this);
        mBtnFollow.setOnClickListener(this);
        mBtnClose.setOnClickListener(this);
        mActionClose.setOnClickListener(this);
        mBtnStore.setOnClickListener(this);
        checkFollowButtonState();
        getData();
    }


    public void setFollow(int follow) {
        mIsFollow = follow;
    }

    private void setView(int action) {
        if (action == SETTING_ACTION_SUP) {
            if (mActionClose != null) {
                mActionClose.setVisibility(VISIBLE);
            }
        } else {
            if (mBtnPrivateMsg != null) {
                mBtnPrivateMsg.setVisibility(VISIBLE);
            }
            if (mBtnFollow != null) {
                mBtnFollow.setVisibility(VISIBLE);
            }
            if (mBtnStore != null) {
                mBtnStore.setVisibility(VISIBLE);
            }
        }
    }


    private void getData() {
        mLiveUid = LiveModel.getContextLiveUid((FragmentActivity) getContext());
        LiveHttpUtil.getLiveUser(mLiveUid, mLiveUid, new ParseHttpCallback<JSONObject>() {
            @Override
            public void onSuccess(int code, String msg, JSONObject info) {
                if (isSuccess(code) && info != null) {
                    mUserBean= JSON.parseObject(String.valueOf(info),UserBean.class);
                    setUseData(mUserBean);
                    setView(info.getIntValue("action"));
                    //ViewUtil.setVisibility(mBtnKicked,View.VISIBLE);
                    //ViewUtil.setVisibility(mBtnShutUp,View.VISIBLE);
                }
            }
        });
    }

    private void checkFollowButtonState() {
        if (mIsFollow == 1) {
            mBtnFollow.setText(R.string.following);
            mBtnFollow.setBackground(ResourceUtil.getDrawable(R.drawable.bg_color_gray1_radius_15, true));
        } else {
            mBtnFollow.setText(R.string.follow);
            mBtnFollow.setBackground(ResourceUtil.getDrawable(R.drawable.bg_color_global_radius_15, true));
        }
    }


    private void setUseData(UserBean info) {
        ImgLoader.display(getContext(), info.getAvatar(), mImgAvator);
        mTvUserName.setText(info.getUserNiceName());
        mTvId.setText("ID:" + info.getId());
        int fansCount = info.getFans();
        int followCount = info.getFollows();
        String tip = WordUtil.getString(R.string.attent_follow_count, Integer.toString(followCount), StringUtil.toWan(fansCount));
        mTvFollowFans.setText(tip);
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        LiveHttpUtil.cancel(LiveHttpConsts.GET_LIVE_USER);
    }

    @Override
    public void onClick(View v) {
        if (!ClickUtil.canClick()) {
            return;
        }
        int id = v.getId();
        if (id == R.id.btn_store) {
            toStore();
        } else if (id == R.id.btn_close) {
            dismiss();
        } else if (id == R.id.btn_follow) {
            toggleFollow();
        } else if (id == R.id.btn_private_msg) {
            toPrivateMsg();
        }else if (id == R.id.btn_close_live) {
            closeLive();
        }
    }

    /**
     * 超管关闭直播间
     */
    private void closeLive() {
        dismiss();
        LiveHttpUtil.superCloseRoom(mLiveUid, 0,LiveModel.getContextStream((FragmentActivity) getContext()), new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
                if (isSuccess(code)) {
                    ((LiveActivity) getContext()).superCloseRoom();
                }
            }
        });
    }

    private void toPrivateMsg() {
        ToastUtil.show(R.string.coming_soon);
    }

    private void toggleFollow() {
        if (mUserBean == null) {
            return;
        }
        CommonAPI.setAttention(mIsFollow, mUserBean.getId(), new ParseSingleHttpCallback<Integer>("isattent") {
            @Override
            public void onSuccess(Integer data) {
                mIsFollow = data;
                if (mIsFollow == 1) {
                    ToastUtil.show(R.string.follow_succ);
                } else {
                    ToastUtil.show(R.string.cancle_follow_succ);
                }
                EventBus.getDefault().post(new FollowEvent(mUserBean.getId(), data));
                checkFollowButtonState();
            }
        });
    }

    private void toStore() {
        Context context = getContext();
        if (context != null && context instanceof LiveAudienceActivity) {
            ((LiveAudienceActivity) context).openWindowTag();
        }
        LiveStoreActivity.forward(getContext(), mUserBean);
    }
}
