package com.wanyue.live.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProviders;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.opensource.svgaplayer.SVGAImageView;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.business.acmannger.ActivityMannger;
import com.wanyue.common.event.LoginInvalidEvent;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.http.BaseHttpCallBack;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.utils.DateFormatUtil;
import com.wanyue.common.utils.DialogUitl;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.ProcessResultUtil;
import com.wanyue.common.utils.SystemUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.R;
import com.wanyue.live.bean.LiveBean;
import com.wanyue.live.bean.LiveKsyConfigBean;
import com.wanyue.live.bean.LiveReceiveGiftBean;
import com.wanyue.live.dialog.LiveFunctionDialogFragment;
import com.wanyue.live.dialog.LiveLinkMicListDialogFragment;
import com.wanyue.live.dialog.LiveShopDialogFragment;
import com.wanyue.live.event.LinkMicTxMixStreamEvent;
import com.wanyue.live.http.LiveHttpConsts;
import com.wanyue.live.http.LiveHttpUtil;
import com.wanyue.live.interfaces.LiveFunctionClickListener;
import com.wanyue.live.interfaces.LivePushListener;
import com.wanyue.live.model.LiveModel;
import com.wanyue.live.socket.SocketChatUtil;
import com.wanyue.live.socket.SocketClient;
import com.wanyue.live.views.AbsLivePushViewHolder;
import com.wanyue.live.views.LiveAnchorViewHolder;
import com.wanyue.live.views.LiveEndViewHolder;
import com.wanyue.live.views.LiveGoodsAddViewHolder;
import com.wanyue.live.views.LiveMusicViewHolder;
import com.wanyue.live.views.LivePushTxViewHolder;
import com.wanyue.live.views.LiveReadyViewHolder;
import com.wanyue.live.views.LiveRoomViewHolder;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import pl.droidsonroids.gif.GifImageView;


/**
 * Created by  on 2018/10/7.
 * 主播直播间
 */

public class LiveAnchorActivity extends LiveActivity implements LiveFunctionClickListener {

    private static final String TAG = "LiveAnchorActivity";
    private LiveGoodsAddViewHolder mLiveGoodsAddViewHolder;
    private boolean mFirstResume=true;

    public static void forward(Context context, int liveSdk, LiveKsyConfigBean bean) {
        Intent intent = new Intent(context, LiveAnchorActivity.class);
        intent.putExtra(Constants.LIVE_SDK, liveSdk);
        intent.putExtra(Constants.LIVE_KSY_CONFIG, bean);
        context.startActivity(intent);
    }

    private ViewGroup mRoot;
    private ViewGroup mContainerWrap;
    private AbsLivePushViewHolder mLivePushViewHolder;
    private LiveReadyViewHolder mLiveReadyViewHolder;
    private LiveAnchorViewHolder mLiveAnchorViewHolder;
    private LiveMusicViewHolder mLiveMusicViewHolder;
    private boolean mStartPreview;//是否开始预览
    private boolean mStartLive;//是否开始了直播
    private boolean mBgmPlaying;//是否在播放背景音乐
    private HttpCallback mCheckLiveCallback;
    private int mReqCount;
    private int haveStore=1;
    private boolean mPaused;
    private boolean mNeedCloseLive = true;
    private ProcessResultUtil mProcessResultUtil;

    private LiveModel mLiveModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_anchor;
    }

    @Override
    protected void main() {
        super.main();

        Intent intent = getIntent();
        mLiveSDK = intent.getIntExtra(Constants.LIVE_SDK, Constants.LIVE_SDK_TX);
        mRoot = (ViewGroup) findViewById(R.id.root);
        mSocketUserType = Constants.SOCKET_USER_TYPE_ANCHOR;
        UserBean u =CommonAppConfig.getUserBean();

        mLiveUid = u.getId();
        mLiveBean = new LiveBean();
        mLiveBean.setUid(mLiveUid);
        mLiveBean.setUserNiceName(u.getUserNiceName());
        mLiveBean.setAvatar(u.getAvatar());
        mLiveBean.setAvatarThumb(u.getAvatarThumb());
        mLiveBean.setLevelAnchor(u.getLevelAnchor());
        mLiveBean.setGoodNum(u.getGoodName());
        mLiveBean.setCity(u.getCity());

        mLiveModel= ViewModelProviders.of(this).get(LiveModel.class);
        mLiveModel.setLiveBean(mLiveBean);

        mContainerWrap = findViewById(R.id.container_wrap);
        mContainer =  findViewById(R.id.container);
        //添加开播前设置控件
        mLiveReadyViewHolder = new LiveReadyViewHolder(mContext, mContainer, mLiveSDK, haveStore);
        mLiveReadyViewHolder.addToParent();
        mLiveReadyViewHolder.subscribeActivityLifeCycle();
        mProcessResultUtil = new ProcessResultUtil(this);
    }

    /*开启预览*/
    private void startPreView() {
        mLivePushViewHolder = new LivePushTxViewHolder(mContext, (ViewGroup) findViewById(R.id.preview_container));
        mLivePushViewHolder.setLivePushListener(new LivePushListener() {
            @Override
            public void onPreviewStart() {
                //开始预览回调
                mStartPreview = true;
            }

            @Override
            public void onPushStart() {
                //开始推流回调
                LiveHttpUtil.changeLive(mStream);
            }
            @Override
            public void onPushFailed() {
                //推流失败回调
                ToastUtil.show(R.string.live_push_failed);
            }
        });
        mLivePushViewHolder.addToParent();
        mLivePushViewHolder.subscribeActivityLifeCycle();
    }

    public boolean isStartPreview() {
        return mStartPreview;
    }

    /**
     * 主播直播间功能按钮点击事件
     *
     * @param functionID
     */
    @Override
    public void onClick(int functionID) {
        switch (functionID) {
            case Constants.LIVE_FUNC_BEAUTY://美颜
                beauty();
                break;
            case Constants.LIVE_FUNC_CAMERA://切换镜头
                toggleCamera();
                break;
            case Constants.LIVE_FUNC_FLASH://切换闪光灯
                toggleFlash();
                break;
            case Constants.LIVE_FUNC_SHARE://分享
                openShareWindow();
                break;
            case Constants.LIVE_FUNC_LINK_MIC://连麦
                openLinkMicAnchorWindow();
                break;
        }
    }


    public void openShop(boolean isOpen) {
        if (mLiveAnchorViewHolder != null) {
            mLiveAnchorViewHolder.setShopBtnVisible(isOpen);
        }
    }


    //打开相机前执行
    public void beforeCamera() {
        if (mLivePushViewHolder != null) {
            mLivePushViewHolder.setOpenCamera(true);
        }
    }

    @Override
    protected void releaseActivty() {
        super.releaseActivty();
        ActivityMannger.getInstance().releaseBaseActivity(this);
    }

    /**
     * 切换镜头
     */
    public void toggleCamera() {
        if (mLivePushViewHolder != null) {
            mLivePushViewHolder.toggleCamera();
        }
    }

    /**
     * 切换闪光灯
     */
    public void toggleFlash() {
        if (mLivePushViewHolder != null) {
            mLivePushViewHolder.toggleFlash();
        }
    }

    /**
     * 设置美颜
     */

    public void beauty() {
        if(mLivePushViewHolder==null){
            return;
        }
    }

    /**
     * 飘心
     */
    public void light() {
        if (mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.playLightAnim();
        }
    }



    /**
     * 关闭背景音乐
     */
    public void stopBgm() {
        if (mLiveMusicViewHolder != null) {
            mLiveMusicViewHolder.release();
        }
        mLiveMusicViewHolder = null;
        mBgmPlaying = false;
    }

    public boolean isBgmPlaying() {
        return mBgmPlaying;
    }


    /**
     * 打开功能弹窗
     */
    public void showFunctionDialog() {
        LiveFunctionDialogFragment fragment = new LiveFunctionDialogFragment();
        fragment.setLifeCycleListener(this);
        Bundle bundle = new Bundle();
        boolean hasGame = false;
        bundle.putBoolean(Constants.OPEN_FLASH, mLivePushViewHolder != null && mLivePushViewHolder.isFlashOpen());
        fragment.setArguments(bundle);
        fragment.setFunctionClickListener(this);
        fragment.show(getSupportFragmentManager(), "LiveFunctionDialogFragment");
    }

    /**
     * 打开主播连麦窗口
     */

    private void openLinkMicAnchorWindow() {
        if (mLiveLinkMicAnchorPresenter != null && !mLiveLinkMicAnchorPresenter.canOpenLinkMicAnchor()) {
            return;
        }
        LiveLinkMicListDialogFragment fragment = new LiveLinkMicListDialogFragment();
        fragment.setLifeCycleListener(this);
        fragment.show(getSupportFragmentManager(), "LiveLinkMicListDialogFragment");
    }

    /**
     * 打开选择游戏窗口
     */
    private void openGameWindow() {
        if (isLinkMic() || isLinkMicAnchor()) {
            ToastUtil.show(R.string.live_link_mic_cannot_game);
            return;
        }

    }

    /**
     * 关闭游戏
     */
    public void closeGame() {

    }

    /**
     * 开播成功
     *
     * @param data createRoom返回的数据
     */
    public void startLiveSuccess(String data, int liveType, int liveTypeVal) {
        mLiveType = liveType;
        mLiveTypeVal = liveTypeVal;
        //处理createRoom返回的数据
        JSONObject obj = JSON.parseObject(data);
        mStream = obj.getString("stream");
        mDanmuPrice = obj.getString("barrage_fee");
        String playUrl = obj.getString("pull");
        L.e("createRoom----播放地址--->" + playUrl);
        mLiveBean.setPull(playUrl);
        mTxAppId = obj.getString("tx_appid");
        if (mLiveModel!=null){
            mLiveModel.setStream(mStream);
        }
        //移除开播前的设置控件，添加直播间控件
        if (mLiveReadyViewHolder != null) {
            mLiveReadyViewHolder.removeFromParent();
            mLiveReadyViewHolder.release();
        }
        mLiveReadyViewHolder = null;
        if (mLiveRoomViewHolder == null) {
            mLiveRoomViewHolder = new LiveRoomViewHolder(mContext, mContainer, (GifImageView) findViewById(R.id.gift_gif), (SVGAImageView) findViewById(R.id.gift_svga), mContainerWrap);
            mLiveRoomViewHolder.addToParent();
            mLiveRoomViewHolder.subscribeActivityLifeCycle();
            mLiveRoomViewHolder.setLiveInfo(mLiveUid, mStream, obj.getIntValue("userlist_time") * 1000);
            UserBean u =
  CommonAppConfig.getUserBean();
            if (u != null) {
                mLiveRoomViewHolder.setRoomNum(u.getLiangNameTip());
                mLiveRoomViewHolder.setName(u.getUserNiceName());
                mLiveRoomViewHolder.setAvatar(u.getAvatar());
                mLiveRoomViewHolder.setAnchorLevel(u.getLevelAnchor());
            }
            mLiveRoomViewHolder.startAnchorLight();
        }
        if (mLiveAnchorViewHolder == null) {
            mLiveAnchorViewHolder = new LiveAnchorViewHolder(mContext, mContainer);
            mLiveAnchorViewHolder.addToParent();

            mLiveAnchorViewHolder.subscribeActivityLifeCycle();
            mLiveAnchorViewHolder.setUnReadCount(((LiveActivity) mContext).getImUnReadCount());
        }
        mLiveBottomViewHolder = mLiveAnchorViewHolder;

        //连接socket
        if (mSocketClient == null) {
            mSocketClient = new SocketClient(obj.getString("chatserver"), this);
            if (mLiveLinkMicPresenter != null) {
                mLiveLinkMicPresenter.setSocketClient(mSocketClient);
            }
            if (mLiveLinkMicAnchorPresenter != null) {
                mLiveLinkMicAnchorPresenter.setSocketClient(mSocketClient);
                mLiveLinkMicAnchorPresenter.setPlayUrl(playUrl);
                mLiveLinkMicAnchorPresenter.setSelfStream(mStream);
            }
            if (mLiveLinkMicPkPresenter != null) {
                mLiveLinkMicPkPresenter.setSocketClient(mSocketClient);
                mLiveLinkMicPkPresenter.setLiveUid(mLiveUid);
                mLiveLinkMicPkPresenter.setSelfStream(mStream);
            }
        }
        mSocketClient.connect(mLiveUid, mStream,obj.getString("token"));

        //开始推流
        if (mLivePushViewHolder != null) {
            mLivePushViewHolder.startPush(obj.getString("push"));
        }
        //开始显示直播时长
        if (mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.startAnchorLiveTime();
            mLiveRoomViewHolder.startAnchorCheckLive();
        }
        mStartLive = true;
        mLiveRoomViewHolder.startRefreshUserList();

    }

    /**
     * 关闭直播
     */
    public void closeLive() {
        DialogUitl.showSimpleDialog(mContext, WordUtil.getString(R.string.live_end_live), new DialogUitl.SimpleCallback() {
            @Override
            public void onConfirmClick(Dialog dialog, String content) {
                endLive();
            }
        });
    }

    /**
     * 结束直播
     */

    public void endLive() {
        //请求关播的接口
        LiveHttpUtil.stopLive(mStream, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (BaseHttpCallBack.isSuccess(code)) {
                    SocketChatUtil.sendEndLiveRoom(mSocketClient);
                    stopLiveSucc();
                } else {
                    ToastUtil.show(msg);
                }
            }
            @Override
            public boolean showLoadingDialog() {
                return true;
            }
            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(mContext, WordUtil.getString(R.string.live_end_ing));
            }
        });
    }

    private void stopLiveSucc() {
        //断开socket
        if (mSocketClient != null) {
            mSocketClient.disConnect();
        }
        if (mLiveEndViewHolder == null) {
            mLiveEndViewHolder = new LiveEndViewHolder(mContext, mRoot);
            mLiveEndViewHolder.subscribeActivityLifeCycle();
            mLiveEndViewHolder.addToParent();
            mLiveEndViewHolder.showData(mLiveBean, mStream);
        }
        release();
        mStartLive = false;
    }


    @Override
    public void onBackPressed() {
         if(mLiveReadyViewHolder!=null){
            mLiveReadyViewHolder.onBackPressed();
            return;
         }

        if (mStartLive) {
            if (!canBackPressed()) {
                return;
            }
            closeLive();
        } else {
            if (mLivePushViewHolder != null) {
                mLivePushViewHolder.release();
            }
            if (mLiveLinkMicPresenter != null) {
                mLiveLinkMicPresenter.release();
            }
            mLivePushViewHolder = null;
            mLiveLinkMicPresenter = null;
            superBackPressed();
        }
    }

    private long mLastClickBackTime;
    public void superBackPressed() {
        if(mLiveEndViewHolder!=null){
        /* Intent intent=new Intent(this,LiveAnchorActivity.class);
         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         startActivity(intent);*/
         super.onBackPressed();
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public void release() {
        LiveHttpUtil.cancel(LiveHttpConsts.CHANGE_LIVE);
        LiveHttpUtil.cancel(LiveHttpConsts.STOP_LIVE);
        LiveHttpUtil.cancel(LiveHttpConsts.LIVE_PK_CHECK_LIVE);
        LiveHttpUtil.cancel(LiveHttpConsts.SET_LINK_MIC_ENABLE);
        if (mLiveReadyViewHolder != null) {
            mLiveReadyViewHolder.release();
        }
        if (mLiveMusicViewHolder != null) {
            mLiveMusicViewHolder.release();
        }
        if (mLivePushViewHolder != null) {
            mLivePushViewHolder.release();
        }
        if (mLiveLinkMicPresenter != null) {
            mLiveLinkMicPresenter.release();
        }

        mLiveMusicViewHolder = null;
        mLiveReadyViewHolder = null;
        mLivePushViewHolder = null;
        mLiveLinkMicPresenter = null;
        super.release();
    }

    @Override
    protected void onDestroy() {
        LiveHttpUtil.cancel(LiveHttpConsts.ANCHOR_CHECK_LIVE);
        super.onDestroy();
        L.e("LiveAnchorActivity-------onDestroy------->");
    }


    @Override
    protected void onPause() {
        if (mNeedCloseLive && mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.anchorPause();
        }
        super.onPause();
        sendSystemMessage(WordUtil.getString(R.string.live_anchor_leave));
        mPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPaused) {
            if (mLiveRoomViewHolder != null) {
                mLiveRoomViewHolder.anchorResume();
            }
            sendSystemMessage(WordUtil.getString(R.string.live_anchor_come_back));
            //CommonHttpUtil.checkTokenInvalid();
        }
        mPaused = false;
        mNeedCloseLive = true;


        if(!mFirstResume){
            return;
        }
        mFirstResume=false;
        askPermissions();
    }

    private void askPermissions() {
        if(mProcessResultUtil!=null){
            checkPermissions(new Runnable() {
                @Override
                public void run() {
                    startPreView();
                }
            });
        }
    }


    private void checkPermissions(Runnable runnable) {
        mProcessResultUtil.requestPermissions(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,

        }, runnable);
    }


    /**
     * 直播间  主播登录失效
     */
    @Override
    public void onAnchorInvalid() {
        super.onAnchorInvalid();
        endLive();
    }

    /**
     * 超管/后台监控关闭直播间
     */
    @Override
    public void onSuperCloseLive() {
        stopLiveSucc();
        DialogUitl.showSimpleTipDialog(mContext, WordUtil.getString(R.string.live_illegal));
    }

    @Override
    public void onWarn(String content) {
        DialogUitl.showSimpleTipDialog(mContext, content);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginInvalidEvent(LoginInvalidEvent e) {
        release();
    }


    public void setBtnFunctionDark() {
        if (mLiveAnchorViewHolder != null) {
            mLiveAnchorViewHolder.setBtnFunctionDark();
        }
    }

    /**
     * 主播与主播连麦  主播收到其他主播发过来的连麦申请
     */
    @Override
    public void onLinkMicAnchorApply(UserBean u, String stream) {
        if (mLiveLinkMicAnchorPresenter != null) {
            mLiveLinkMicAnchorPresenter.onLinkMicAnchorApply(u, stream);
        }
    }

    /**
     * 主播与主播连麦  对方主播拒绝连麦的回调
     */
    @Override
    public void onLinkMicAnchorRefuse() {
        if (mLiveLinkMicAnchorPresenter != null) {
            mLiveLinkMicAnchorPresenter.onLinkMicAnchorRefuse();
        }
    }

    /**
     * 主播与主播连麦  对方主播无响应的回调
     */
    @Override
    public void onLinkMicAnchorNotResponse() {
        if (mLiveLinkMicAnchorPresenter != null) {
            mLiveLinkMicAnchorPresenter.onLinkMicNotResponse();
        }
    }

    /**
     * 主播与主播连麦  对方主播正在游戏
     */
    @Override
    public void onlinkMicPlayGaming() {
        if (mLiveLinkMicAnchorPresenter != null) {
            mLiveLinkMicAnchorPresenter.onlinkMicPlayGaming();
        }
    }


    /**
     * 主播与主播连麦  对方主播正在忙的回调
     */
    @Override
    public void onLinkMicAnchorBusy() {
        if (mLiveLinkMicAnchorPresenter != null) {
            mLiveLinkMicAnchorPresenter.onLinkMicAnchorBusy();
        }
    }

    /**
     * 发起主播连麦申请
     *
     * @param pkUid  对方主播的uid
     * @param stream 对方主播的stream
     */
    public void linkMicAnchorApply(final String pkUid, final String stream) {
        if (isBgmPlaying()) {
            DialogUitl.showSimpleDialog(mContext, WordUtil.getString(R.string.link_mic_close_bgm), new DialogUitl.SimpleCallback() {
                @Override
                public void onConfirmClick(Dialog dialog, String content) {
                    stopBgm();
                    checkLiveAnchorMic(pkUid, stream);
                }
            });
        } else {
            checkLiveAnchorMic(pkUid, stream);
        }
    }

    /**
     * 发起主播连麦申请
     *
     * @param pkUid  对方主播的uid
     * @param stream 对方主播的stream
     */
    private void checkLiveAnchorMic(final String pkUid, String stream) {
        LiveHttpUtil.livePkCheckLive(pkUid, stream, mStream, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    if (mLiveSDK == Constants.LIVE_SDK_TX) {
                        String playUrl = null;
                        JSONObject obj = JSON.parseObject(info[0]);
                        if (obj != null) {
                            String accUrl = obj.getString("pull");//自己主播的低延时流
                            if (!TextUtils.isEmpty(accUrl)) {
                                playUrl = accUrl;
                            }
                        }
                        if (mLiveLinkMicAnchorPresenter != null) {
                            mLiveLinkMicAnchorPresenter.applyLinkMicAnchor(pkUid, playUrl, mStream);
                        }
                    } else {
                        if (mLiveLinkMicAnchorPresenter != null) {
                            mLiveLinkMicAnchorPresenter.applyLinkMicAnchor(pkUid, null, mStream);
                        }
                    }
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }

    /**
     * 设置连麦pk按钮是否可见
     */
    public void setPkBtnVisible(boolean visible) {
        if (mLiveAnchorViewHolder != null) {
            if (visible) {
                if (mLiveLinkMicAnchorPresenter.isLinkMic()) {
                }
            } else {
            }
        }
    }

    /**
     * 发起主播连麦pk
     */

    public void applyLinkMicPk() {
        String pkUid = null;
        if (mLiveLinkMicAnchorPresenter != null) {
            pkUid = mLiveLinkMicAnchorPresenter.getPkUid();
        }
        if (!TextUtils.isEmpty(pkUid) && mLiveLinkMicPkPresenter != null) {
            mLiveLinkMicPkPresenter.applyLinkMicPk(pkUid, mStream);
        }
    }

    /**
     * 主播与主播PK  主播收到对方主播发过来的PK申请的回调
     *
     * @param u      对方主播的信息
     * @param stream 对方主播的stream
     */
    @Override
    public void onLinkMicPkApply(UserBean u, String stream) {
        if (mLiveLinkMicPkPresenter != null) {
            mLiveLinkMicPkPresenter.onLinkMicPkApply(u, stream);
        }
    }

    /**
     * 主播与主播PK  对方主播拒绝pk的回调
     */
    @Override
    public void onLinkMicPkRefuse() {
        if (mLiveLinkMicPkPresenter != null) {
            mLiveLinkMicPkPresenter.onLinkMicPkRefuse();
        }
    }

    /**
     * 主播与主播PK   对方主播正在忙的回调
     */
    @Override
    public void onLinkMicPkBusy() {
        if (mLiveLinkMicPkPresenter != null) {
            mLiveLinkMicPkPresenter.onLinkMicPkBusy();
        }
    }

    /**
     * 主播与主播PK   对方主播无响应的回调
     */
    @Override
    public void onLinkMicPkNotResponse() {
        if (mLiveLinkMicPkPresenter != null) {
            mLiveLinkMicPkPresenter.onLinkMicPkNotResponse();
        }
    }

    /**
     * 腾讯sdk连麦时候主播混流
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLinkMicTxMixStreamEvent(LinkMicTxMixStreamEvent e) {
        if (mLivePushViewHolder != null && mLivePushViewHolder instanceof LivePushTxViewHolder) {
            ((LivePushTxViewHolder) mLivePushViewHolder).onLinkMicTxMixStreamEvent(e.getType(), e.getToStream());
        }
    }

    /**
     * 主播checkLive
     */
    public void checkLive() {
        if (mCheckLiveCallback == null) {
            mCheckLiveCallback = new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if (isSuccess(code) && info.length > 0) {
                        int isLive = JSON.parseObject(info[0]).getIntValue("islive");
                        if(isLive==0){
                           endLive();
                        }
                    }
                }
            };
        }
        mReqCount++;
        LiveHttpUtil.anchorCheckLive( mStream, mCheckLiveCallback);
    }




    /**
     * 打开商品窗口
     */
    public void openGoodsWindow() {
        LiveShopDialogFragment fragment = new LiveShopDialogFragment();
        fragment.setLifeCycleListener(this);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.LIVE_UID, mLiveUid);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "LiveShopDialogFragment");
    }


    public void forwardAddGoods() {
        if (mLiveGoodsAddViewHolder == null) {
            mLiveGoodsAddViewHolder = new LiveGoodsAddViewHolder(mContext, mPageContainer);
            mLiveGoodsAddViewHolder.subscribeActivityLifeCycle();
            mLiveGoodsAddViewHolder.addToParent();
        }
        mLiveGoodsAddViewHolder.show();
    }


    @Override
    protected boolean canBackPressed() {
        if (mLiveGoodsAddViewHolder != null && mLiveGoodsAddViewHolder.isShowed()) {
            mLiveGoodsAddViewHolder.hide();
            return false;
        }
        return super.canBackPressed();
    }


    /**
     * 显示道具礼物
     */
    public void showStickerGift(LiveReceiveGiftBean bean) {

    }

    public void openShopGoods() {

    }

}
