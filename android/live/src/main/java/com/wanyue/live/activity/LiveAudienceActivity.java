package com.wanyue.live.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.opensource.svgaplayer.SVGAImageView;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.business.acmannger.ActivityMannger;
import com.wanyue.common.custom.MyViewPager;
import com.wanyue.common.custom.bubble.BubbleDialog;
import com.wanyue.common.custom.bubble.BubbleLayout;
import com.wanyue.common.custom.bubble.Util;
import com.wanyue.common.http.CommonHttpConsts;
import com.wanyue.common.http.CommonHttpUtil;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.http.ParseSingleHttpCallback;
import com.wanyue.common.pay.PayCallback;
import com.wanyue.common.pay.PayPresenter;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.DialogUitl;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.RandomUtil;
import com.wanyue.common.utils.RouteUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.ViewUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.R;
import com.wanyue.live.adapter.LiveRoomScrollAdapter;
import com.wanyue.live.bean.LiveBean;
import com.wanyue.live.bean.LiveUserGiftBean;
import com.wanyue.live.business.floatwindow.FloatFrameLayout;
import com.wanyue.live.business.floatwindow.Utils;
import com.wanyue.live.business.floatwindow.WindowAddHelper;
import com.wanyue.live.dialog.LiveGiftDialogFragment;
import com.wanyue.live.dialog.LiveGoodsDialogFragment;
import com.wanyue.live.event.LinkMicTxAccEvent;
import com.wanyue.live.event.LiveRoomChangeEvent;
import com.wanyue.live.http.LiveHttpConsts;
import com.wanyue.live.http.LiveHttpUtil;
import com.wanyue.live.model.LiveModel;
import com.wanyue.live.presenter.LiveLinkMicAnchorPresenter;
import com.wanyue.live.presenter.LiveLinkMicPkPresenter;
import com.wanyue.live.presenter.LiveLinkMicPresenter;
import com.wanyue.live.receiver.HomeWatcherReceiver;
import com.wanyue.live.socket.SocketChatUtil;
import com.wanyue.live.socket.SocketClient;
import com.wanyue.live.utils.LiveStorge;
import com.wanyue.live.views.LiveAudienceViewHolder;
import com.wanyue.live.views.LiveEndViewHolder;
import com.wanyue.live.views.LivePlayTxViewHolder;
import com.wanyue.live.views.LiveRoomPlayViewHolder;
import com.wanyue.live.views.LiveRoomViewHolder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by  on 2018/10/10.
 */

public class LiveAudienceActivity extends LiveActivity {

    private static final String TAG = "LiveAudienceActivity";
    private LiveModel mLiveModel;
    private int mIsAttention;
    private ViewGroup mPlayContainer;
    private FrameLayout mContainer;


    public static void forward(Context context, LiveBean liveBean, String data) {
        Intent intent = new Intent(context, LiveAudienceActivity.class);
        intent.putExtra(Constants.LIVE_BEAN, liveBean);
        intent.putExtra(Constants.DATA, data);
        context.startActivity(intent);
    }

    private boolean mUseScroll = true;
    private String mKey;
    private int mPosition;
    private RecyclerView mRecyclerView;
    private LiveRoomScrollAdapter mRoomScrollAdapter;
    private View mMainContentView;
    private MyViewPager mViewPager;
    private ViewGroup mSecondPage;//默认显示第二页
    private FrameLayout mContainerWrap;
    private LiveRoomPlayViewHolder mLivePlayViewHolder;
    private LiveAudienceViewHolder mLiveAudienceViewHolder;
    private boolean mEnd;
    private boolean mCoinNotEnough;//余额不足
    private boolean mLighted;
    private PayPresenter mPayPresenter;

    @Override
    protected void getInentParams() {
        Intent intent = getIntent();
        mLiveSDK = intent.getIntExtra(Constants.LIVE_SDK, Constants.LIVE_SDK_KSY);
        L.e(TAG, "直播sdk----->" + (mLiveSDK == Constants.LIVE_SDK_KSY ? "金山云" : "腾讯云"));
        mKey = intent.getStringExtra(Constants.LIVE_KEY);
        if (TextUtils.isEmpty(mKey)) {
            mUseScroll = false;
        }
        mPosition = intent.getIntExtra(Constants.LIVE_POSITION, 0);
        mLiveType = intent.getIntExtra(Constants.LIVE_TYPE, Constants.LIVE_TYPE_NORMAL);
        mLiveTypeVal = intent.getIntExtra(Constants.LIVE_TYPE_VAL, 0);
        mLiveBean = intent.getParcelableExtra(Constants.LIVE_BEAN);


        mLiveModel = ViewModelProviders.of(this).get(LiveModel.class);
        mLiveModel.setLiveBean(mLiveBean);

        ActivityMannger.getInstance().setTargetActivty(this);
    }

    private boolean isUseScroll() {
        return mUseScroll && CommonAppConfig.LIVE_ROOM_SCROLL;
    }

    @Override
    public <T extends View> T findViewById(@IdRes int id) {
        if (isUseScroll()) {
            if (mMainContentView != null) {
                return mMainContentView.findViewById(id);
            }
        }
        return super.findViewById(id);
    }

    @Override
    protected int getLayoutId() {
        if (isUseScroll()) {
            return R.layout.activity_live_audience_2;
        }
        return R.layout.activity_live_audience;
    }

    public void setScrollFrozen(boolean frozen) {
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutFrozen(frozen);
        }
    }


    @Override
    protected void main() {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        if (isUseScroll()) {
            mRecyclerView = super.findViewById(R.id.recyclerView);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            mMainContentView = LayoutInflater.from(mContext).inflate(R.layout.activity_live_audience, null, false);
        }
        super.main();
        mPlayContainer = (ViewGroup) findViewById(R.id.play_container);
        mContainer = (FrameLayout) findViewById(R.id.container);
        mLivePlayViewHolder = new LivePlayTxViewHolder(mContext, mPlayContainer);
        mLivePlayViewHolder.addToParent();
        mLivePlayViewHolder.subscribeActivityLifeCycle();
        mViewPager = (MyViewPager) findViewById(R.id.viewPager);
        mSecondPage = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.view_audience_page, mViewPager, false);
        mContainerWrap = mSecondPage.findViewById(R.id.container_wrap);
        mContainer = mSecondPage.findViewById(R.id.container);
        mLiveRoomViewHolder = new LiveRoomViewHolder(mContext, mContainer, (GifImageView) mSecondPage.findViewById(R.id.gift_gif), (SVGAImageView) mSecondPage.findViewById(R.id.gift_svga), mContainerWrap);
        mLiveRoomViewHolder.addToParent();
        mLiveRoomViewHolder.subscribeActivityLifeCycle();
        mLiveAudienceViewHolder = new LiveAudienceViewHolder(mContext, mContainer);
        mLiveAudienceViewHolder.addToParent();
        mLiveAudienceViewHolder.setUnReadCount(getImUnReadCount());
        mLiveBottomViewHolder = mLiveAudienceViewHolder;
        mViewPager.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                if (position == 0) {
                    View view = new View(mContext);
                    view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    container.addView(view);
                    return view;
                } else {
                    container.addView(mSecondPage);
                    return mSecondPage;
                }
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            }
        });
        mViewPager.setCurrentItem(1);
        mLiveLinkMicPresenter = new LiveLinkMicPresenter(mContext, mLivePlayViewHolder, false, mLiveSDK, mLiveAudienceViewHolder.getContentView());
        mLiveLinkMicAnchorPresenter = new LiveLinkMicAnchorPresenter(mContext, mLivePlayViewHolder, false, mLiveSDK, null);
        mLiveLinkMicPkPresenter = new LiveLinkMicPkPresenter(mContext, mLivePlayViewHolder, false, null);
        if (isUseScroll()) {
            List<LiveBean> list = LiveStorge.getInstance().get(mKey);
            mRoomScrollAdapter = new LiveRoomScrollAdapter(mContext, list, mPosition);
            mRoomScrollAdapter.setActionListener(new LiveRoomScrollAdapter.ActionListener() {
                @Override
                public void onPageSelected(LiveBean liveBean, ViewGroup container, boolean first) {
                    L.e(TAG, "onPageSelected----->" + liveBean);
                    if (mMainContentView != null && container != null) {
                        ViewParent parent = mMainContentView.getParent();
                        if (parent != null) {
                            ViewGroup viewGroup = (ViewGroup) parent;
                            if (viewGroup != container) {
                                viewGroup.removeView(mMainContentView);
                                container.addView(mMainContentView);
                            }
                        } else {
                            container.addView(mMainContentView);
                        }
                    }

                }

                @Override
                public void onPageOutWindow(String liveUid) {
                    L.e(TAG, "onPageOutWindow----->" + liveUid);
                    if (TextUtils.isEmpty(mLiveUid) || mLiveUid.equals(liveUid)) {
                        LiveHttpUtil.cancel(LiveHttpConsts.CHECK_LIVE);
                        LiveHttpUtil.cancel(LiveHttpConsts.ENTER_ROOM);
                        LiveHttpUtil.cancel(LiveHttpConsts.ROOM_CHARGE);
                        clearRoomData();
                    }
                }
            });
            mRecyclerView.setAdapter(mRoomScrollAdapter);
        }
        if (mLiveBean != null) {
            setLiveRoomData(mLiveBean);
            enterRoom();
        }
    }


    public void scrollNextPosition() {
        if (mRoomScrollAdapter != null) {
            mRoomScrollAdapter.scrollNextPosition();
        }
    }


    private void setLiveRoomData(LiveBean liveBean) {
        mLiveUid = liveBean.getUid();
        mStream = liveBean.getStream();
        mLivePlayViewHolder.setCover(liveBean.getThumb());
        mLivePlayViewHolder.play(liveBean.getPull());
        mLiveAudienceViewHolder.setLiveInfo(mLiveUid, mStream);
        mLiveRoomViewHolder.setAvatar(liveBean.getAvatar());
        mLiveRoomViewHolder.setAnchorLevel(liveBean.getLevelAnchor());
        mLiveRoomViewHolder.setName(liveBean.getUserNiceName());
        mLiveRoomViewHolder.setRoomNum(liveBean.getLiangNameTip());
        // mLiveRoomViewHolder.setTitle(liveBean.getTitle());
        mLiveLinkMicPkPresenter.setLiveUid(mLiveUid);
        mLiveLinkMicPresenter.setLiveUid(mLiveUid);
        mLiveAudienceViewHolder.setShopOpen(true);
        if (mLiveModel!=null){
            mLiveModel.setStream(mStream);
        }
    }

    private void clearRoomData() {
        if (mSocketClient != null) {
            mSocketClient.disConnect();
        }
        mSocketClient = null;
        if (mLivePlayViewHolder != null) {
            mLivePlayViewHolder.stopPlay();
        }
        if (mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.clearData();
        }

        if (mLiveEndViewHolder != null) {
            mLiveEndViewHolder.removeFromParent();
        }
        if (mLiveLinkMicPresenter != null) {
            mLiveLinkMicPresenter.clearData();
        }
        if (mLiveLinkMicAnchorPresenter != null) {
            mLiveLinkMicAnchorPresenter.clearData();
        }
        if (mLiveLinkMicPkPresenter != null) {
            mLiveLinkMicPkPresenter.clearData();
        }
    }


    private void enterRoom() {
        String jsonData = getIntent().getStringExtra(Constants.DATA);
        JSONObject obj = JSON.parseObject(jsonData);
        paserLiveData(obj);
    }

    private void paserLiveData(JSONObject obj) {
        L.e("enterroom="+obj.toJSONString());
        mDanmuPrice = obj.getString("barrage_fee");
        mSocketUserType = obj.getIntValue("usertype");
        mChatLevel = obj.getIntValue("speak_limit");
        mDanMuLevel = obj.getIntValue("barrage_limit");
        //连接socket
        mSocketClient = new SocketClient(obj.getString("chatserver"), LiveAudienceActivity.this);
        if (mLiveLinkMicPresenter != null) {
            mLiveLinkMicPresenter.setSocketClient(mSocketClient);
        }
        mSocketClient.connect(mLiveUid, mStream, obj.getString("token"));
        LiveModel.setShutUpContext(LiveAudienceActivity.this, obj.getIntValue("ishut") == 1);
        if (mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.setLiveInfo(mLiveUid, mStream, obj.getIntValue("userlist_time") * 1000);
            mLiveRoomViewHolder.setVotes(obj.getString("votestotal"));
            mIsAttention = obj.getIntValue("isattent");
            mLiveRoomViewHolder.setAttention(mIsAttention);
            List<LiveUserGiftBean> list = JSON.parseArray(obj.getString("userlists"), LiveUserGiftBean.class);
            // mLiveRoomViewHolder.setUserList(list);
            mLiveRoomViewHolder.setLiveNums(obj.getIntValue("likes"));
            mLiveRoomViewHolder.setGoodsNum(obj.getIntValue("goods"));
            mLiveRoomViewHolder.setLiveUserNum(obj.getIntValue("nums"));
            mLiveRoomViewHolder.startRefreshUserList();
        }
    }

    /**
     * 打开礼物窗口
     */
    public void openGiftWindow() {
        if (TextUtils.isEmpty(mLiveUid) || TextUtils.isEmpty(mStream)) {
            return;
        }
        LiveGiftDialogFragment fragment = new LiveGiftDialogFragment();
        fragment.setLifeCycleListener(this);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.LIVE_UID, mLiveUid);
        bundle.putString(Constants.LIVE_STREAM, mStream);
        fragment.setArguments(bundle);
        fragment.show(((LiveAudienceActivity) mContext).getSupportFragmentManager(), "LiveGiftDialogFragment");
    }


    /**
     * 结束观看
     */
    private void endPlay() {
        if (mEnd) {
            return;
        }
        mEnd = true;
        //断开socket
        if (mSocketClient != null) {
            mSocketClient.disConnect();
        }
        mSocketClient = null;
        //结束播放
        if (mLivePlayViewHolder != null) {
            mLivePlayViewHolder.release();
        }
        mLivePlayViewHolder = null;
        release();
    }

    @Override
    protected void release() {
        if (mPayPresenter != null) {
            mPayPresenter.release();
        }
        mPayPresenter = null;
        LiveHttpUtil.cancel(LiveHttpConsts.CHECK_LIVE);
        LiveHttpUtil.cancel(LiveHttpConsts.ENTER_ROOM);
        LiveHttpUtil.cancel(LiveHttpConsts.ROOM_CHARGE);
        CommonHttpUtil.cancel(CommonHttpConsts.GET_BALANCE);
        super.release();
        if (mRoomScrollAdapter != null) {
            mRoomScrollAdapter.setActionListener(null);
        }
        mRoomScrollAdapter = null;
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        ActivityMannger.getInstance().setOnLaunchListner(null);
        ActivityMannger.getInstance().setTargetActivty(null);
        ActivityMannger.getInstance().removeActivity(this);

        clearFloatWindowState();
        disposable();
        unregisterReceiverHomeWatch();
        if (mLiveAudienceViewHolder != null) {
            mLiveAudienceViewHolder.clearAnim();
        }
    }
    /**
     * 超管/后台监控关闭直播间
     */
    @Override
    public void onSuperCloseLive() {
       onLiveEnd();
    }
    /**
     * 观众收到直播结束消息
     */
    @Override
    public void onLiveEnd() {
        super.onLiveEnd();
        endPlay();
        if (mViewPager != null) {
            if (mViewPager.getCurrentItem() != 1) {
                mViewPager.setCurrentItem(1, false);
            }
            mViewPager.setCanScroll(false);
        }
        if (mLiveEndViewHolder == null) {
            mLiveEndViewHolder = new LiveEndViewHolder(mContext, mSecondPage);
            mLiveEndViewHolder.subscribeActivityLifeCycle();
            mLiveEndViewHolder.addToParent();
        }
        mLiveEndViewHolder.showData(mLiveBean, mStream);
        if (isUseScroll()) {
            if (mRecyclerView != null) {
                mRecyclerView.setLayoutFrozen(true);
            }
        }
    }

    /**
     * 观众收到踢人消息
     */

    @Override
    public void onKick(String touid) {
        if (!TextUtils.isEmpty(touid) && touid.equals(
                CommonAppConfig.getUid())) {//被踢的是自己
            exitLiveRoom();
            ToastUtil.show(WordUtil.getString(R.string.live_kicked_2));
        }
    }

    /**
     * 观众收到禁言消息
     */
    @Override
    public void onShutUp(String touid, int action, String content) {
        if (!StringUtil.equals(touid, CommonAppConfig.getUid())) {
            return;
        }
        L.e("onShutUp-content="+content);
        //ToastUtil.show(WordUtil.getString(R.string.live_shutup_tip,content));
        LiveModel.setShutUpContext(this, action == 1);
    }

    @Override
    public void onBackPressed() {
        close();
    }

    public void onBackAndFinish() {
        if (!mEnd && !canBackPressed()) {
            return;
        }
        exitLiveRoom();
    }

    /**
     * 退出直播间
     */

    public void exitLiveRoom() {
        endPlay();
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.e("LiveAudienceActivity-------onDestroy------->");
    }

    /**
     * 点亮
     */

    public void light() {
        if (!mLighted) {
            mLighted = true;
            SocketChatUtil.sendLightMessage(mSocketClient, 1 + RandomUtil.nextInt(6));
        }
        if (mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.playLightAnim();
        }
        setLikes();

    }


    private ParseSingleHttpCallback mParseSetLikeCallback;

    private void setLikes() {
        if (!ClickUtil.canClick()) {
            return;
        }
        if (mParseSetLikeCallback == null) {
            mParseSetLikeCallback = new ParseSingleHttpCallback<Integer>("nums") {
                @Override
                public void onSuccess(Integer data) {
                    if (mLiveRoomViewHolder != null) {
                        mLiveRoomViewHolder.setLiveNums(data);
                    }
                }
            };
        }
        LiveHttpUtil.setLiveLikes(mStream, mParseSetLikeCallback);
    }


    /**
     * 计时收费更新主播映票数
     */
    public void roomChargeUpdateVotes() {
        sendUpdateVotesMessage(mLiveTypeVal);
    }

    /**
     * 暂停播放
     */
    public void pausePlay() {
        if (mLivePlayViewHolder != null) {
            mLivePlayViewHolder.pausePlay();
        }
    }

    /**
     * 恢复播放
     */

    public void resumePlay() {
        if (mLivePlayViewHolder != null) {
            mLivePlayViewHolder.resumePlay();
        }
    }

    /**
     * 充值成功
     */

    public void onChargeSuccess() {
        if (mLiveType == Constants.LIVE_TYPE_TIME) {
            if (mCoinNotEnough) {
                mCoinNotEnough = false;
                LiveHttpUtil.roomCharge(mLiveUid, mStream, new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0) {
                            roomChargeUpdateVotes();
                            if (mLiveRoomViewHolder != null) {
                                resumePlay();
                            }
                        } else {
                            if (code == 1008) {//余额不足
                                mCoinNotEnough = true;
                                DialogUitl.showSimpleDialog(mContext, WordUtil.getString(R.string.live_coin_not_enough), false,
                                        new DialogUitl.SimpleCallback2() {
                                            @Override
                                            public void onConfirmClick(Dialog dialog, String content) {
                                                RouteUtil.forwardMyCoin();
                                            }

                                            @Override
                                            public void onCancelClick() {
                                                exitLiveRoom();
                                            }
                                        });
                            }
                        }
                    }
                });
            }
        }
    }

    public void setCoinNotEnough(boolean coinNotEnough) {
        mCoinNotEnough = coinNotEnough;
    }


    @Override
    protected void releaseActivty() {
        super.releaseActivty();

        disposable();
    }

    /**
     * 腾讯sdk连麦时候切换低延时流
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLinkMicTxAccEvent(LinkMicTxAccEvent e) {
        if (mLivePlayViewHolder != null && mLivePlayViewHolder instanceof LivePlayTxViewHolder) {
            ((LivePlayTxViewHolder) mLivePlayViewHolder).onLinkMicTxAccEvent(e.isLinkMic());
        }
    }

    /**
     * 腾讯sdk时候主播连麦回调
     *
     * @param linkMic true开始连麦 false断开连麦
     */
    public void onLinkMicTxAnchor(boolean linkMic) {
        if (mLivePlayViewHolder != null && mLivePlayViewHolder instanceof LivePlayTxViewHolder) {
            ((LivePlayTxViewHolder) mLivePlayViewHolder).setAnchorLinkMic(linkMic, 5000);
        }
    }


    /**
     * 打开充值窗口
     */
    public void openChargeWindow() {
        if (mPayPresenter == null) {
            mPayPresenter = new PayPresenter(this);
            mPayPresenter.setServiceNameAli(Constants.PAY_BUY_COIN_ALI);
            mPayPresenter.setServiceNameWx(Constants.PAY_BUY_COIN_WX);
            //mPayPresenter.setAliCallbackUrl(HtmlConfig.ALI_PAY_COIN_URL);
            mPayPresenter.setPayCallback(new PayCallback() {
                @Override
                public void onSuccess() {
                    if (mPayPresenter != null) {
                        mPayPresenter.checkPayResult();
                    }
                }

                @Override
                public void onFailed(int code) {

                }
            });
        }
        /*LiveChargeDialogFragment fragment = new LiveChargeDialogFragment();
        fragment.setLifeCycleListener(this);
        fragment.setPayPresenter(mPayPresenter);
        fragment.show(getSupportFragmentManager(), "ChatChargeDialogFragment");*/
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLiveRoomChangeEvent(LiveRoomChangeEvent e) {
        LiveBean liveBean = e.getLiveBean();
        if (liveBean != null) {
            LiveHttpUtil.cancel(LiveHttpConsts.CHECK_LIVE);
            LiveHttpUtil.cancel(LiveHttpConsts.ENTER_ROOM);
            LiveHttpUtil.cancel(LiveHttpConsts.ROOM_CHARGE);
            clearRoomData();

            setLiveRoomData(liveBean);
            mLiveType = e.getLiveType();
            mLiveTypeVal = e.getLiveTypeVal();
            enterRoom();
        }
    }

    @Override
    public void sendChatMessage(String content) {
        int isAttention = mLiveRoomViewHolder == null ? 0 : mLiveRoomViewHolder.getIsFollowAnthor();
        SocketChatUtil.sendChatMessage(mSocketClient, content, mIsAnchor, isAttention,mSocketUserType);
    }


    /**
     * 打开商品窗口
     */

    public void openGoodsWindow() {
        LiveGoodsDialogFragment fragment = new LiveGoodsDialogFragment();
        fragment.setLifeCycleListener(this);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.LIVE_UID, mLiveUid);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "LiveGoodsDialogFragment");
    }

    public void openMoreWindow(View view) {
        BubbleLayout bl = new BubbleLayout(this);
        //bl.setBubbleColor(Color.GRAY);
        //bl.setShadowColor(Color.WHITE);
        bl.setLookLength(Util.dpToPx(this, 10));
        bl.setLookWidth(Util.dpToPx(this, 10));


        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_live_audience_more, null);
        final BubbleDialog dialog = new BubbleDialog(this)
                .setBubbleContentView(contentView)
                .setClickedView(view)
                .setBubbleLayout(bl);
        dialog.show();
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickUtil.canClick()) {
                    return;
                }
                dialog.dismiss();
                openWindowTag();
                LiveReportActivity.forward(mContext, mLiveUid);
            }
        });
    }


    public void openWindowTag() {
        checkPermissonOpenNarrow(this, false, true);
        startActivity(WindowActivity.class);
    }

    //关闭直播间
    public void close() {
        checkPermissonOpenNarrow(this, true, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        //unregisterReceiverHomeWatch();
    }
    /*--------------------------           悬浮窗代码          ---------------------------------*/

    /*检查悬浮窗权限并安全弹出悬浮窗*/
    private WindowAddHelper mWindowAddHelper;
    private WindowManager mWindowManager;
    private boolean isFloatAtWindow;
    private Disposable mDisposable;
    private FloatFrameLayout mWindowsFloatLayout;
    private HomeWatcherReceiver mHomeWatcherReceiver;

    private void initReceiver() {
        if (mHomeWatcherReceiver == null) {
            mHomeWatcherReceiver = new HomeWatcherReceiver();
            /*监听home键的广播*/
            mHomeWatcherReceiver.setShortHomeClickLitner(new HomeWatcherReceiver.ShortHomeClickLitner() {
                @Override
                public void shortClick() {
                    ActivityMannger.getInstance().setBackGround(true);
                    checkPermissonOpenNarrow(mContext, false, false);
                }
            });
            registerReceiverHomeWatch();
        }
    }

    public void checkPermissonOpenNarrow(Context context, boolean needRequestPermisson, final boolean needLockTouch) {
        if (mWindowAddHelper == null) {
            mWindowAddHelper = new WindowAddHelper(this);
        }
        mWindowAddHelper.checkOverLay(this, new Predicate<Boolean>() {
            @Override
            public boolean test(Boolean aBoolean) throws Exception {
                if (!aBoolean) {
                    onBackAndFinish();
                }
                return aBoolean;
            }
        }, needRequestPermisson).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    openNarrow(needLockTouch);
                } else if (!needLockTouch) {
                    onBackAndFinish();
                }
            }
        });
    }

    /*初始化windowMannger*/
    private void initWindowMannger() {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager)
                    getSystemService(WINDOW_SERVICE);
            ActivityMannger.getInstance().setOnLaunchListner(new ActivityMannger.OnLaunchListner() {
                @Override
                public boolean launchFromBackGround() {
                    if (isFloatAtWindow) {
                        return false;
                    } else if (!mWindowAddHelper.isDrawOverLay()) {
                        checkPermissonOpenNarrow(ActivityMannger.getInstance().getMainStackTopActivity(), true, false);
                    }
                    return true;
                }
            });
        }
    }

    private void openNarrow(final boolean needLockTouch) {
        //initReceiver();
        isFloatAtWindow = true;
        initWindowMannger();
        final View view = exportFlowView();
        final WindowManager.LayoutParams layoutParams = mWindowAddHelper.createDefaultWindowsParams(0, 100);
        if (view != null) {
            moveTaskToBack(false);//当前界面退到后台
            int time = delayToFloatWindowTime();
            if (time <= 0) {
                createNarrowWindow(layoutParams, view, needLockTouch);
            } else {
                mDisposable = Observable.timer(time, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        createNarrowWindow(layoutParams, view, needLockTouch);
                    }
                });
            }
        }
    }

    private void createNarrowWindow(WindowManager.LayoutParams layoutParams, View view, boolean needLockTouch) {
        mWindowsFloatLayout = new FloatFrameLayout(mContext);
        mWindowsFloatLayout.setLock(needLockTouch);
        makeParm(mWindowsFloatLayout, layoutParams, view);
        mWindowsFloatLayout.setView(view, 0);
        mWindowsFloatLayout.setWmParams(layoutParams);
        mWindowManager.addView(mWindowsFloatLayout, layoutParams);
        mWindowsFloatLayout.setOnNoTouchClickListner(new FloatFrameLayout.OnNoTouchClickListner() {
            @Override
            public void click(View view) {
                restoreVideoFromWindowFlat(view);
            }

            @Override
            public void close(View view) {
                onBackAndFinish();
            }
        });
    }


    private void makeParm(FloatFrameLayout windowsFloatLayout, WindowManager.LayoutParams layoutParams, View view) {
        Utils.initFloatParamList(this);
        layoutParams.width = Utils.subWidth != 0 ? Utils.subWidth : view.getWidth();
        layoutParams.height = Utils.subHeight != 0 ? Utils.subHeight + DpUtil.dp2px(20) : view.getHeight();
    }

    /*恢复界面控件到activity界面*/
    private void restoreVideoFromWindowFlat(View view) {
        if (mWindowManager == null || !isFloatAtWindow) {
            return;
        }
        try {
            isFloatAtWindow = false;
            mWindowManager.removeView(view);
            /*从前台点击*/
            if (!ActivityMannger.getInstance().isBackGround()) {
                ActivityMannger.getInstance().launchOntherStackToTopActivity(false, ActivityMannger.getInstance().getMainStackTopActivity());
            }
            if (!isDestroyed()) {
                restoreFlowView((FloatFrameLayout) view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disposable() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void addToActivityMannger() {
        ActivityMannger.getInstance().addActivityByNewStack(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        addToActivityMannger();
        if (isFloatAtWindow) {
            restoreVideoFromWindowFlat();
        }
    }

    public void restoreVideoFromWindowFlat() {
        disposable();
        if (mWindowsFloatLayout != null) {
            restoreVideoFromWindowFlat(mWindowsFloatLayout);
        }
    }


    private void restoreFlowView(FloatFrameLayout floatView) {
        if (floatView == null || floatView.getChildCount() <= 0) {
            return;
        }
        View view = floatView.getChildAt(0);
        ViewUtil.removeToParent(view);
        mPlayContainer.addView(view);
    }

    private View exportFlowView() {
        return mPlayContainer.getChildAt(0);
    }

    protected int delayToFloatWindowTime() {
        return 1;
    }

    private void clearFloatWindowState() {
        if (mWindowsFloatLayout != null && mWindowManager != null && isFloatAtWindow) {
            try {
                mWindowManager.removeViewImmediate(mWindowsFloatLayout);
                mWindowsFloatLayout = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void registerReceiverHomeWatch() {
        try {
            registerReceiver(mHomeWatcherReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unregisterReceiverHomeWatch() {
        try {
            mContext.unregisterReceiver(mHomeWatcherReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
