package com.wanyue.live.dialog;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanyue.common.dialog.AbsDialogFragment;


/**
 * Created by  on 2018/10/15.
 * 直播间个人资料弹窗
 */

public class LiveUserDialogFragment extends AbsDialogFragment implements View.OnClickListener {
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected int getDialogStyle() {
        return 0;
    }

    @Override
    protected boolean canCancel() {
        return false;
    }

    @Override
    protected void setWindowAttributes(Window window) {

    }

   /* private static final int TYPE_AUD_AUD = 1;//观众点别的观众
    private static final int TYPE_ANC_AUD = 2;//主播点观众
    private static final int TYPE_AUD_ANC = 3;//观众点主播
    private static final int TYPE_AUD_SELF = 4;//观众点自己
    private static final int TYPE_ANC_SELF = 5;//主播点自己

    private static final int SETTING_ACTION_SELF = 0;//设置 自己点自己
    private static final int SETTING_ACTION_AUD = 30;//设置 普通观众点普通观众 或所有人点超管
    private static final int SETTING_ACTION_ADM = 40;//设置 房间管理员点普通观众
    private static final int SETTING_ACTION_SUP = 60;//设置 超管点主播
    private static final int SETTING_ACTION_ANC_AUD = 501;//设置 主播点普通观众
    private static final int SETTING_ACTION_ANC_ADM = 502;//设置 主播点房间管理员
    private ViewGroup mBottomContainer;
    private ImageView mAvatar;
    private ImageView mLevelAnchor;
    private ImageView mLevel;
    private TextView mLevelText;
    private TextView mLevelAnchorText;
    private ImageView mSex;
    private TextView mName;
    private TextView mID;
    private TextView mCity;
    private LinearLayout mImpressGroup;
    private TextView mFollow;
    private TextView mSign;
    private TextView mFans;
    private TextView mConsume;//消费
    private TextView mVotes;//收入
    private TextView mConsumeTip;
    private TextView mVotesTip;
    private String mLiveUid;
    private String mStream;
    private String mToUid;
    private TextView mFollowText;
    private ImageView mFollowImage;
    private int mType;
    private int mAction;
    private String mToName;//对方的名字
    private UserBean mUserBean;
    private boolean mFollowing;
    private Drawable mFollowDrawable;
    private Drawable mUnFollowDrawable;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_live_user;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog2;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        window.setWindowAnimations(R.style.bottomToTopAnim);
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mLiveUid = bundle.getString(Constants.LIVE_UID);
        mToUid = bundle.getString(Constants.TO_UID);
        if (TextUtils.isEmpty(mLiveUid) || TextUtils.isEmpty(mToUid)) {
            return;
        }
        mStream = bundle.getString(Constants.STREAM);
        mBottomContainer = mRootView.findViewById(R.id.bottom_container);
        mAvatar = (ImageView) mRootView.findViewById(R.id.avatar);
        mLevelAnchor = (ImageView) mRootView.findViewById(R.id.anchor_level);
        mLevel = (ImageView) mRootView.findViewById(R.id.level);
        mLevelText = mRootView.findViewById(R.id.level_text);
        mLevelAnchorText = mRootView.findViewById(R.id.level_anchor_text);
        mSex = (ImageView) mRootView.findViewById(R.id.sex);
        mName = (TextView) mRootView.findViewById(R.id.name);
        mID = (TextView) mRootView.findViewById(R.id.id_val);
        mCity = (TextView) mRootView.findViewById(R.id.city);
        mSign = mRootView.findViewById(R.id.sign);
        mImpressGroup = (LinearLayout) mRootView.findViewById(R.id.impress_group);
        mFollow = (TextView) mRootView.findViewById(R.id.follow);
        mFans = (TextView) mRootView.findViewById(R.id.fans);
        mConsume = (TextView) mRootView.findViewById(R.id.consume);
        mVotes = (TextView) mRootView.findViewById(R.id.votes);
        mConsumeTip = (TextView) mRootView.findViewById(R.id.consume_tip);
        mVotesTip = (TextView) mRootView.findViewById(R.id.votes_tip);
        // mRootView.findViewById(R.id.btn_close).setOnClickListener(this);
        getType();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View bottomView = null;
        if (mType == TYPE_AUD_ANC) {
            if (mImpressGroup.getVisibility() != View.VISIBLE) {
                mImpressGroup.setVisibility(View.VISIBLE);
            }
            bottomView = inflater.inflate(R.layout.dialog_live_user_bottom_1, mBottomContainer, false);
        } else if (mType == TYPE_AUD_AUD) {
            bottomView = inflater.inflate(R.layout.dialog_live_user_bottom_1, mBottomContainer, false);
        } else if (mType == TYPE_ANC_AUD) {
            bottomView = inflater.inflate(R.layout.dialog_live_user_bottom_2, mBottomContainer, false);
        } else if (mType == TYPE_AUD_SELF) {
            bottomView = inflater.inflate(R.layout.dialog_live_user_bottom_3, mBottomContainer, false);
        }
        if (bottomView != null) {
            mFollowDrawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_user_home_follow_1);
            mUnFollowDrawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_user_home_follow_0);
            mBottomContainer.addView(bottomView);
            mFollowText = bottomView.findViewById(R.id.follow_text);
            mFollowImage = (ImageView) findViewById(R.id.follow_img);
            View btnFollow = bottomView.findViewById(R.id.btn_follow);
            if (btnFollow != null) {
                btnFollow.setOnClickListener(this);
            }
            View btnPriMsg = bottomView.findViewById(R.id.btn_pri_msg);
            if (btnPriMsg != null) {
                ConfigBean configBean =
  CommonAppConfig.getConfig();
                boolean priMsgSwitchOpen = configBean != null && configBean.getPriMsgSwitch() == 1;
                if (priMsgSwitchOpen) {
                    btnPriMsg.setOnClickListener(this);
                } else {
                    btnPriMsg.setVisibility(View.GONE);
                }
            }
            View btnHomePage = bottomView.findViewById(R.id.btn_home_page);
            if (btnHomePage != null) {
                btnHomePage.setOnClickListener(this);
            }
        }
        View btnAt = findViewById(R.id.btn_at);
        if (btnAt != null) {
            btnAt.setOnClickListener(this);
        }
        loadData();
    }

    private void getType() {
        String uid =
  CommonAppConfig.getUid();
        if (mToUid.equals(mLiveUid)) {
            if (mLiveUid.equals(uid)) {//主播点自己
                mType = TYPE_ANC_SELF;
            } else {//观众点主播
                mType = TYPE_AUD_ANC;
            }
        } else {
            if (mLiveUid.equals(uid)) {//主播点观众
                mType = TYPE_ANC_AUD;
            } else {
                if (mToUid.equals(uid)) {//观众点自己
                    mType = TYPE_AUD_SELF;
                } else {//观众点别的观众
                    mType = TYPE_AUD_AUD;
                }
            }
        }
    }

    private void loadData() {
        LiveHttpUtil.getLiveUser(mToUid, mLiveUid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    showData(info[0]);
                }
            }
        });
    }

    private void showData(String data) {
        JSONObject obj = JSON.parseObject(data);
        mUserBean = JSON.toJavaObject(obj, UserBean.class);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        mID.setText(mUserBean.getLiangNameTip());
        mToName = obj.getString("user_nicename");
        mName.setText(mToName);
        mCity.setText(obj.getString("city"));
        ImgLoader.displayAvatar(mContext, obj.getString("avatar"), mAvatar);
        int levelAnchor = obj.getIntValue("level_anchor");
        int level = obj.getIntValue("level");
        mSign.setText(obj.getString("signature"));
        LevelBean anchorLevelBean = appConfig.getAnchorLevel(obj.getIntValue("level_anchor"));
        if (anchorLevelBean != null) {
            ImgLoader.display(mContext, anchorLevelBean.getBgIcon(), mLevelAnchor);
        }
        LevelBean levelBean = appConfig.getLevel(obj.getIntValue("level"));
        if (levelBean != null) {
            ImgLoader.display(mContext, levelBean.getBgIcon(), mLevel);
        }
        mLevelAnchorText.setText(String.valueOf(levelAnchor));
        mLevelText.setText(String.valueOf(level));
        mSex.setImageResource(CommonIconUtil.getSexIcon(obj.getIntValue("sex")));
        mFollow.setText(LiveTextRender.renderLiveUserDialogData(obj.getLongValue("follows")));
        mFans.setText(LiveTextRender.renderLiveUserDialogData(obj.getLongValue("fans")));
        mConsume.setText(LiveTextRender.renderLiveUserDialogData(obj.getLongValue("consumption")));
        mVotes.setText(LiveTextRender.renderLiveUserDialogData(obj.getLongValue("votestotal")));
        mConsumeTip.setText(WordUtil.getString(R.string.live_user_send) + appConfig.getCoinName());
        mVotesTip.setText(WordUtil.getString(R.string.live_user_get) + appConfig.getVotesName());
        if (mType == TYPE_AUD_ANC) {
            showImpress(obj.getString("label"));
        }
        mFollowing = obj.getIntValue("isattention") == 1;
        if (mFollowText != null) {
            mFollowText.setText(mFollowing ? WordUtil.getString(R.string.following) : WordUtil.getString(R.string.follow));
        }
        if (mFollowImage != null) {
            mFollowImage.setImageDrawable(mFollowing ? mFollowDrawable : mUnFollowDrawable);
        }
        mAction = obj.getIntValue("action");
        if (mAction == SETTING_ACTION_AUD) {//设置 普通观众点普通观众 或所有人点超管
            View btnReport = mRootView.findViewById(R.id.btn_report);
            btnReport.setVisibility(View.VISIBLE);
            btnReport.setOnClickListener(this);
        } else if (mAction == SETTING_ACTION_ADM//设置 房间管理员点普通观众
                || mAction == SETTING_ACTION_SUP//设置 超管点主播
                || mAction == SETTING_ACTION_ANC_AUD//设置 主播点普通观众
                || mAction == SETTING_ACTION_ANC_ADM) {//设置 主播点房间管理员
            View btnSetting = mRootView.findViewById(R.id.btn_setting);
            btnSetting.setVisibility(View.VISIBLE);
            btnSetting.setOnClickListener(this);
        }
    }

    private void showImpress(String impressJson) {
        List<ImpressBean> list = JSON.parseArray(impressJson, ImpressBean.class);
        if (list.size() > 2) {
            list = list.subList(0, 2);
        }
        LayoutInflater inflater = LayoutInflater.from(mContext);
        for (int i = 0, size = list.size(); i < size; i++) {
            MyTextView myTextView = (MyTextView) inflater.inflate(R.layout.view_impress_item_2, mImpressGroup, false);
            ImpressBean bean = list.get(i);
            bean.setCheck(1);
            myTextView.setBean(bean);
            mImpressGroup.addView(myTextView);
        }
        TextView textView = (TextView) inflater.inflate(R.layout.view_impress_item_add, mImpressGroup, false);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImpress();
            }
        });
        mImpressGroup.addView(textView);
    }


    *//**
     * 添加主播印象
     *//*
    private void addImpress() {
        dismiss();
        ((LiveActivity) mContext).openAddImpressWindow(mLiveUid);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_close) {
            dismiss();

        } else if (i == R.id.btn_follow) {
            setAttention();

        } else if (i == R.id.btn_pri_msg) {
            openChatRoomWindow();

        } else if (i == R.id.btn_home_page) {
            forwardHomePage();

        } else if (i == R.id.btn_setting) {
            setting();

        } else if (i == R.id.btn_report) {
            report();

        } else if (i == R.id.btn_at) {
            if (!TextUtils.isEmpty(mToName)) {
                dismiss();
                ((LiveActivity) mContext).openChatWindow(mToName);
            }

        }
    }

    *//**
     * 打开私信聊天窗口
     *//*
    private void openChatRoomWindow() {
        if (mUserBean != null) {
            dismiss();
            ImMessageUtil.getInstance().markAllMessagesAsRead(mToUid, true);
            ((LiveActivity) mContext).openChatRoomWindow(mUserBean, mFollowing);
        }
    }

    *//**
     * 关注
     *//*
    private void setAttention() {
        CommonHttpUtil.setAttention(mToUid, mAttentionCallback);
    }

    private CommonCallback<Integer> mAttentionCallback = new CommonCallback<Integer>() {

        @Override
        public void callback(Integer isAttention) {
            mFollowing = isAttention == 1;
            if (mFollowText != null) {
                mFollowText.setText(mFollowing ? R.string.following : R.string.follow);
            }
            if (mFollowImage != null) {
                mFollowImage.setImageDrawable(mFollowing ? mFollowDrawable : mUnFollowDrawable);
            }
            if (isAttention == 1 && mLiveUid.equals(mToUid)) {//关注了主播
                ((LiveActivity) mContext).sendSystemMessage(

  CommonAppConfig.getUserBean().getUserNiceName() + WordUtil.getString(R.string.live_follow_anchor));
            }
        }
    };

    *//**
     * 跳转到个人主页
     *//*
    private void forwardHomePage() {
        dismiss();
        RouteUtil.forwardUserHome(mContext, mToUid, true, mLiveUid);
    }

    *//**
     * 举报
     *//*
    private void report() {
        LiveReportActivity.forward(mContext, mToUid);
    }

    *//**
     * 设置
     *//*
    private void setting() {
        List<Integer> list = new ArrayList<>();
        switch (mAction) {
            case SETTING_ACTION_ADM://设置 房间管理员点普通观众
                list.add(R.string.live_setting_kick);
                list.add(R.string.live_setting_gap);
                list.add(R.string.live_setting_gap_2);
                break;
            case SETTING_ACTION_SUP://设置 超管点主播
                list.add(R.string.live_setting_close_live);
                list.add(R.string.live_setting_close_live_2);
                list.add(R.string.live_setting_forbid_account);
                break;
            case SETTING_ACTION_ANC_AUD://设置 主播点普通观众
                list.add(R.string.live_setting_kick);
                list.add(R.string.live_setting_gap);
                list.add(R.string.live_setting_gap_2);
                list.add(R.string.live_setting_admin);
                list.add(R.string.live_setting_admin_list);
                break;
            case SETTING_ACTION_ANC_ADM://设置 主播点房间管理员
                list.add(R.string.live_setting_kick);
                list.add(R.string.live_setting_gap);
                list.add(R.string.live_setting_gap_2);
                list.add(R.string.live_setting_admin_cancel);
                list.add(R.string.live_setting_admin_list);
                break;
        }

        DialogUitl.showStringArrayDialog(mContext, list.toArray(new Integer[list.size()]), mArrayDialogCallback);
    }

    private DialogUitl.StringArrayDialogCallback mArrayDialogCallback = new DialogUitl.StringArrayDialogCallback() {
        @Override
        public void onItemClick(String text, int tag) {
            if (tag == R.string.live_setting_kick) {
                kick();

            } else if (tag == R.string.live_setting_gap) {//永久禁言
                setShutUp();

            } else if (tag == R.string.live_setting_gap_2) {//本场禁言
                setShutUp2();

            } else if (tag == R.string.live_setting_admin || tag == R.string.live_setting_admin_cancel) {
                setAdmin();

            } else if (tag == R.string.live_setting_admin_list) {
                adminList();

            } else if (tag == R.string.live_setting_close_live) {
                closeLive();

            } else if (tag == R.string.live_setting_forbid_account) {
                forbidAccount();

            } else if (tag == R.string.live_setting_close_live_2) {//禁用直播
                closeLive2();
            }
        }
    };

    *//**
     * 查看管理员列表
     *//*
    private void adminList() {
        dismiss();
        ((LiveActivity) mContext).openAdminListWindow();
    }

    *//**
     * 踢人
     *//*
    private void kick() {
        LiveHttpUtil.kicking(mLiveUid, mToUid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    ((LiveActivity) mContext).kickUser(mToUid, mToName);
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }

    *//**
     * 永久禁言
     *//*
    private void setShutUp() {
        LiveHttpUtil.setShutUp(mLiveUid, "0", 0, mToUid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    ((LiveActivity) mContext).setShutUp(mToUid, mToName, 0);
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }

    *//**
     * 本场禁言
     *//*
    private void setShutUp2() {
        LiveHttpUtil.setShutUp(mLiveUid, mStream, 1, mToUid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    ((LiveActivity) mContext).setShutUp(mToUid, mToName, 1);
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }


    *//**
     * 设置或取消管理员
     *//*
    private void setAdmin() {
        LiveHttpUtil.setAdmin(mLiveUid, mToUid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    int res = JSON.parseObject(info[0]).getIntValue("isadmin");
                    if (res == 1) {//被设为管理员
                        mAction = SETTING_ACTION_ANC_ADM;
                    } else {//被取消管理员
                        mAction = SETTING_ACTION_ANC_AUD;
                    }
                    ((LiveActivity) mContext).sendSetAdminMessage(res, mToUid, mToName);
                }
            }
        });
    }


    *//**
     * 超管关闭直播间
     *//*
    private void closeLive() {
        dismiss();
        LiveHttpUtil.superCloseRoom(mLiveUid, 0, mSuperCloseRoomCallback);
    }

    *//**
     * 超管关闭直播间并禁止主播直播
     *//*
    private void closeLive2() {
        dismiss();
        LiveHttpUtil.superCloseRoom(mLiveUid, 1, mSuperCloseRoomCallback);
    }

    *//**
     * 超管关闭直播间并禁用主播账户
     *//*
    private void forbidAccount() {
        dismiss();
        LiveHttpUtil.superCloseRoom(mLiveUid, 2, mSuperCloseRoomCallback);
    }

    private HttpCallback mSuperCloseRoomCallback = new HttpCallback() {
        @Override
        public void onSuccess(int code, String msg, String[] info) {
            if (code == 0) {
                ToastUtil.show(JSON.parseObject(info[0]).getString("msg"));
                ((LiveActivity) mContext).superCloseRoom();
            } else {
                ToastUtil.show(msg);
            }
        }
    };

    @Override
    public void onDestroy() {
        LiveHttpUtil.cancel(LiveHttpConsts.GET_LIVE_USER);
        if (mAvatar != null) {
            ImgLoader.clear(mContext, mAvatar);
            mAvatar.setImageDrawable(null);
        }
        mAvatar = null;
        super.onDestroy();
    }*/
}
