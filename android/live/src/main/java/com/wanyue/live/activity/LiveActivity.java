package com.wanyue.live.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxj.xpopup.XPopup;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.AbsActivity;
import com.wanyue.common.bean.ConfigBean;
import com.wanyue.common.bean.LiveGiftBean;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.dialog.AbsDialogFragment;
import com.wanyue.common.event.CoinChangeEvent;
import com.wanyue.common.event.FollowEvent;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.interfaces.KeyBoardHeightChangeListener;
import com.wanyue.common.mob.MobCallback;
import com.wanyue.common.mob.MobShareUtil;
import com.wanyue.common.mob.ShareData;
import com.wanyue.common.utils.KeyBoardHeightUtil2;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.ProcessImageUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.WordFilterUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.R;
import com.wanyue.live.bean.GlobalGiftBean;
import com.wanyue.live.bean.LiveBean;
import com.wanyue.live.bean.LiveBuyGuardMsgBean;
import com.wanyue.live.bean.LiveChatBean;
import com.wanyue.live.bean.LiveDanMuBean;
import com.wanyue.live.bean.LiveEnterRoomBean;
import com.wanyue.live.bean.LiveGiftPrizePoolWinBean;
import com.wanyue.live.bean.LiveLuckGiftWinBean;
import com.wanyue.live.bean.LiveReceiveGiftBean;
import com.wanyue.live.bean.LiveUserGiftBean;
import com.wanyue.live.dialog.LiveAnthorUserDialogPop;
import com.wanyue.live.dialog.LiveInputDialogFragment;
import com.wanyue.live.dialog.LiveShareDialogFragment;
import com.wanyue.live.dialog.LiveShareSelectDialogFragment;
import com.wanyue.live.dialog.LiveUserManngerDialogFragment;
import com.wanyue.live.http.LiveHttpConsts;
import com.wanyue.live.http.LiveHttpUtil;
import com.wanyue.live.presenter.LiveLinkMicAnchorPresenter;
import com.wanyue.live.presenter.LiveLinkMicPkPresenter;
import com.wanyue.live.presenter.LiveLinkMicPresenter;
import com.wanyue.live.socket.SocketChatUtil;
import com.wanyue.live.socket.SocketClient;
import com.wanyue.live.socket.SocketMessageListener;
import com.wanyue.live.views.AbsLiveViewHolder;
import com.wanyue.live.views.LiveAdminListViewHolder;
import com.wanyue.live.views.LiveContributeViewHolder;
import com.wanyue.live.views.LiveEndViewHolder;
import com.wanyue.live.views.LiveRoomViewHolder;
import com.wanyue.live.views.LiveUserListViewHolder;
import com.wanyue.live.views.LiveWebViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashSet;

/**
 * Created by  on 2018/10/7.
 */

public abstract class LiveActivity extends AbsActivity implements SocketMessageListener, LiveShareDialogFragment.ActionListener, KeyBoardHeightChangeListener, AbsDialogFragment.LifeCycleListener {

    protected ViewGroup mContainer;
    protected ViewGroup mPageContainer;
    protected LiveRoomViewHolder mLiveRoomViewHolder;
    protected AbsLiveViewHolder mLiveBottomViewHolder;
    protected LiveContributeViewHolder mLiveContributeViewHolder;
    protected LiveWebViewHolder mLiveLuckGiftTipViewHolder;
    protected LiveWebViewHolder mLiveDaoGiftTipViewHolder;
    protected LiveAdminListViewHolder mLiveAdminListViewHolder;
    protected LiveEndViewHolder mLiveEndViewHolder;
    protected LiveLinkMicPresenter mLiveLinkMicPresenter;//???????????????????????????
    protected LiveLinkMicAnchorPresenter mLiveLinkMicAnchorPresenter;//???????????????????????????
    protected LiveLinkMicPkPresenter mLiveLinkMicPkPresenter;//???????????????PK??????
    protected SocketClient mSocketClient;
    protected LiveBean mLiveBean;
    protected int mLiveSDK;//sdk??????  0??????  1??????
    protected String mTxAppId;//??????sdkAppId
    protected boolean mIsAnchor;//???????????????
    protected int mSocketUserType;//socket????????????  30 ????????????  40 ?????????  50 ??????  60??????
    protected String mStream;
    protected String mLiveUid;
    protected String mDanmuPrice;//????????????
    protected String mCoinName;//????????????
    protected int mLiveType;//??????????????????  ?????? ?????? ?????? ?????????
    protected int mLiveTypeVal;//????????????,??????????????????????????????
    protected KeyBoardHeightUtil2 mKeyBoardHeightUtil;
    protected int mChatLevel;//??????????????????
    protected int mDanMuLevel;//??????????????????
    private MobShareUtil mMobShareUtil;
    private ProcessImageUtil mImageUtil;
    private boolean mFirstConnectSocket;//??????????????????????????????socket
    private boolean mChatRoomOpened;//????????????????????????????????????
    private HashSet<DialogFragment> mDialogFragmentSet;

    private LiveUserListViewHolder mLiveUserListViewHolder;


    @Override
    protected void main() {
        L.e("LiveActivity==" + hashCode());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mCoinName =
                CommonAppConfig.getCoinName();
        mIsAnchor = this instanceof LiveAnchorActivity;
        mPageContainer = (ViewGroup) findViewById(R.id.page_container);
        EventBus.getDefault().register(this);
        mImageUtil = new ProcessImageUtil(this);
        mDialogFragmentSet = new HashSet<>();
    }

    @Override
    protected boolean isStatusBarWhite() {
        return true;
    }

    public ViewGroup getPageContainer() {
        return mPageContainer;
    }

    public ProcessImageUtil getProcessImageUtil() {
        return mImageUtil;
    }

    @Override
    public void onDialogFragmentShow(AbsDialogFragment dialogFragment) {
        if (mDialogFragmentSet != null && dialogFragment != null) {
            mDialogFragmentSet.add(dialogFragment);
        }
    }

    @Override
    public void onDialogFragmentHide(AbsDialogFragment dialogFragment) {
        if (mDialogFragmentSet != null && dialogFragment != null) {
            mDialogFragmentSet.remove(dialogFragment);
        }
    }

    private void hideDialogs() {
        if (mDialogFragmentSet != null) {
            for (DialogFragment dialogFragment : mDialogFragmentSet) {
                if (dialogFragment != null) {
                    dialogFragment.dismissAllowingStateLoss();
                }
            }
        }
    }

    /**
     * ????????????socket?????????
     */

    @Override
    public void onConnect(boolean successConn) {
        if (successConn) {
            if (!mFirstConnectSocket) {
                mFirstConnectSocket = true;
                if (mLiveType == Constants.LIVE_TYPE_PAY || mLiveType == Constants.LIVE_TYPE_TIME) {
                    SocketChatUtil.sendUpdateVotesMessage(mSocketClient, mLiveTypeVal, 1);
                }
            }
        }
    }

    /**
     * ?????????socket??????
     */
    @Override
    public void onDisConnect() {

    }

    /**
     * ??????????????????
     */
    @Override
    public void onChat(LiveChatBean bean) {
        if (mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.insertChat(bean);
        }

    }

    /**
     * ??????????????????
     */

    @Override
    public void onLight(LiveChatBean liveChatBean) {
        if (mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.playLightAnim();
            mLiveRoomViewHolder.insertChat(liveChatBean);
        }
    }

    /**
     * ???????????????????????????
     */

    @Override
    public void onEnterRoom(LiveEnterRoomBean bean) {
        if (mLiveRoomViewHolder != null) {
            LiveUserGiftBean u = bean.getUserBean();
            mLiveRoomViewHolder.insertUser(null);
            mLiveRoomViewHolder.insertChat(bean.getLiveChatBean());
            mLiveRoomViewHolder.onEnterRoom(bean);
        }
    }

    /**
     * ??????????????????????????????
     */

    @Override
    public void onLeaveRoom(UserBean bean) {
        if (mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.removeUser(bean.getId());
        }
        if (mLiveLinkMicPresenter != null) {
            mLiveLinkMicPresenter.onAudienceLeaveRoom(bean);
        }
    }

    /**
     * ??????????????????
     */

    @Override
    public void onSendGift(LiveReceiveGiftBean bean) {
        /*if (CommonAppConfig.isTiBeautyEnable() && !"0".equals(bean.getStickerId())) {
            //mLiveRoomViewHolder.insertChat(bean.getLiveChatBean());
            if (mIsAnchor) {
                ((LiveAnchorActivity) mContext).showStickerGift(bean);
            }
        } else {
            mLiveRoomViewHolder.showGiftMessage(bean);
        }*/
        if (mLiveRoomViewHolder!=null){
            mLiveRoomViewHolder.showGiftMessage(bean);
        }
    }

    /**
     * pk ??????????????????
     *
     * @param leftGift  ??????????????????
     * @param rightGift ??????????????????
     */
    @Override
    public void onSendGiftPk(long leftGift, long rightGift) {
        if (mLiveLinkMicPkPresenter != null) {
            mLiveLinkMicPkPresenter.onPkProgressChanged(leftGift, rightGift);
        }
    }

    /**
     * ??????????????????
     */
    @Override
    public void onSendDanMu(LiveDanMuBean bean) {
        if (mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.showDanmu(bean);
        }
    }

    /**
     * ??????????????????????????????
     */
    @Override
    public void onLiveEnd() {
        hideDialogs();
    }

    /**
     * ?????????  ??????????????????
     */
    @Override
    public void onAnchorInvalid() {
        hideDialogs();
    }

    /**
     * ?????????????????????
     */
    @Override
    public void onSuperCloseLive() {
        hideDialogs();
    }

    /**
     * ??????
     */
    @Override
    public void onKick(String touid) {

    }

    /**
     * ??????
     */
    @Override
    public void onShutUp(String touid, int action, String content) {

    }

    /**
     * ????????????????????????
     */
    @Override
    public void onSetAdmin(String toUid, int isAdmin) {
        if (!TextUtils.isEmpty(toUid) && toUid.equals(CommonAppConfig.getUid())) {
            mSocketUserType = isAdmin == 1 ? Constants.SOCKET_USER_TYPE_ADMIN : Constants.SOCKET_USER_TYPE_NORMAL;
        }
    }

    /**
     * ??????????????????
     * @param content
     */
    @Override
    public void onWarn(String content) {
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     */
    @Override
    public void onChangeTimeCharge(int typeVal) {

    }


    /**
     * ?????????????????????  ?????????????????????????????????
     */
    @Override
    public void onAudienceApplyLinkMic(UserBean u) {
        if (mLiveLinkMicPresenter != null) {
            mLiveLinkMicPresenter.onAudienceApplyLinkMic(u);
        }
    }

    /**
     * ?????????????????????  ?????????????????????????????????socket
     */
    @Override
    public void onAnchorAcceptLinkMic() {
        if (mLiveLinkMicPresenter != null) {
            mLiveLinkMicPresenter.onAnchorAcceptLinkMic();
        }
    }

    /**
     * ?????????????????????  ?????????????????????????????????socket
     */
    @Override
    public void onAnchorRefuseLinkMic() {
        if (mLiveLinkMicPresenter != null) {
            mLiveLinkMicPresenter.onAnchorRefuseLinkMic();
        }
    }

    /**
     * ?????????????????????  ???????????????????????????????????????
     */
    @Override
    public void onAudienceSendLinkMicUrl(String uid, String uname, String playUrl) {
        if (mLiveLinkMicPresenter != null) {
            mLiveLinkMicPresenter.onAudienceSendLinkMicUrl(uid, uname, playUrl);
        }

    }

    /**
     * ?????????????????????  ???????????????????????????
     */
    @Override
    public void onAnchorCloseLinkMic(String touid, String uname) {
        if (mLiveLinkMicPresenter != null) {
            mLiveLinkMicPresenter.onAnchorCloseLinkMic(touid, uname);
        }
    }

    /**
     * ?????????????????????  ????????????????????????
     */
    @Override
    public void onAudienceCloseLinkMic(String uid, String uname) {
        if (mLiveLinkMicPresenter != null) {
            mLiveLinkMicPresenter.onAudienceCloseLinkMic(uid, uname);
        }
    }

    /**
     * ?????????????????????  ?????????????????????
     */
    @Override
    public void onAnchorNotResponse() {
        if (mLiveLinkMicPresenter != null) {
            mLiveLinkMicPresenter.onAnchorNotResponse();
        }
    }

    /**
     * ?????????????????????  ???????????????
     */
    @Override
    public void onAnchorBusy() {
        if (mLiveLinkMicPresenter != null) {
            mLiveLinkMicPresenter.onAnchorBusy();
        }
    }

    /**
     * ?????????????????????  ?????????????????????????????????????????????????????????
     */
    @Override
    public void onLinkMicAnchorApply(UserBean u, String stream) {
        //??????????????????????????????
    }

    /**
     * ?????????????????????  ???????????????????????????????????????????????????
     *
     * @param playUrl ???????????????????????????
     * @param pkUid   ???????????????uid
     */
    @Override
    public void onLinkMicAnchorPlayUrl(String pkUid, String playUrl) {
        if (mLiveLinkMicAnchorPresenter != null) {
            mLiveLinkMicAnchorPresenter.onLinkMicAnchorPlayUrl(pkUid, playUrl);
        }
        if (this instanceof LiveAudienceActivity) {
            ((LiveAudienceActivity) this).onLinkMicTxAnchor(true);
        }
    }

    /**
     * ?????????????????????  ?????????????????????
     */
    @Override
    public void onLinkMicAnchorClose() {
        if (mLiveLinkMicAnchorPresenter != null) {
            mLiveLinkMicAnchorPresenter.onLinkMicAnchorClose();
        }
        if (mLiveLinkMicPkPresenter != null) {
            mLiveLinkMicPkPresenter.onLinkMicPkClose();
        }
        if (this instanceof LiveAudienceActivity) {
            ((LiveAudienceActivity) this).onLinkMicTxAnchor(false);
        }
    }

    /**
     * ?????????????????????  ?????????????????????????????????
     */
    @Override
    public void onLinkMicAnchorRefuse() {
        //??????????????????????????????
    }

    /**
     * ?????????????????????  ??????????????????????????????
     */
    @Override
    public void onLinkMicAnchorNotResponse() {
        //??????????????????????????????
    }

    /**
     * ?????????????????????  ????????????????????????
     */
    @Override
    public void onlinkMicPlayGaming() {
        //??????????????????????????????
    }

    /**
     * ?????????????????????  ??????????????????????????????
     */
    @Override
    public void onLinkMicAnchorBusy() {
        //??????????????????????????????
    }

    /**
     * ???????????????PK  ????????????????????????????????????PK???????????????
     *
     * @param u      ?????????????????????
     * @param stream ???????????????stream
     */
    @Override
    public void onLinkMicPkApply(UserBean u, String stream) {
        //??????????????????????????????
    }

    /**
     * ???????????????PK ???????????????PK???????????????
     */
    @Override
    public void onLinkMicPkStart(String pkUid) {
        if (mLiveLinkMicPkPresenter != null) {
            mLiveLinkMicPkPresenter.onLinkMicPkStart(pkUid);
        }
    }

    /**
     * ???????????????PK  ???????????????????????????pk?????????
     */
    @Override
    public void onLinkMicPkClose() {
        if (mLiveLinkMicPkPresenter != null) {
            mLiveLinkMicPkPresenter.onLinkMicPkClose();
        }
    }

    /**
     * ???????????????PK  ??????????????????pk?????????
     */
    @Override
    public void onLinkMicPkRefuse() {
        //??????????????????????????????
    }

    /**
     * ???????????????PK   ??????????????????????????????
     */
    @Override
    public void onLinkMicPkBusy() {
        //??????????????????????????????
    }

    /**
     * ???????????????PK   ??????????????????????????????
     */
    @Override
    public void onLinkMicPkNotResponse() {
        //??????????????????????????????
    }

    /**
     * ???????????????PK   ???????????????PK???????????????
     */
    @Override
    public void onLinkMicPkEnd(String winUid) {
        if (mLiveLinkMicPkPresenter != null) {
            mLiveLinkMicPkPresenter.onLinkMicPkEnd(winUid);
        }
    }


    /**
     * ???????????????????????????
     */
    @Override
    public void onAudienceLinkMicExitRoom(String touid) {
        if (mLiveLinkMicPresenter != null) {
            mLiveLinkMicPresenter.onAudienceLinkMicExitRoom(touid);
        }
    }


    /**
     * ??????????????????
     */
    @Override
    public void onLuckGiftWin(LiveLuckGiftWinBean bean) {
        if (mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.onLuckGiftWin(bean);
        }
    }

    /**
     * ????????????
     */
    @Override
    public void onPrizePoolWin(LiveGiftPrizePoolWinBean bean) {
        if (mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.onPrizePoolWin(bean);
        }
    }


    /**
     * ????????????
     */
    @Override
    public void onPrizePoolUp(String level) {
        if (mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.onPrizePoolUp(level);
        }
    }

    /**
     * ????????????
     */
    @Override
    public void onGlobalGift(GlobalGiftBean bean) {
        if (mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.onGlobalGift(bean);
        }
    }

    /**
     * ????????????
     */
    public void openUserListView() {
        if (mLiveUserListViewHolder == null) {
            mLiveUserListViewHolder = new LiveUserListViewHolder(mContext, mPageContainer);
            mLiveUserListViewHolder.subscribeActivityLifeCycle();
            mLiveUserListViewHolder.addToParent();
        }
        mLiveUserListViewHolder.show();
    }


    /**
     * ?????????????????????
     */
    public void openChatWindow(String atName) {
        if (mKeyBoardHeightUtil == null) {
            mKeyBoardHeightUtil = new KeyBoardHeightUtil2(mContext, super.findViewById(android.R.id.content), this);
            mKeyBoardHeightUtil.start();
        }
        if (mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.chatScrollToBottom();
        }
        LiveInputDialogFragment fragment = new LiveInputDialogFragment();
        fragment.setLifeCycleListener(this);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.LIVE_DANMU_PRICE, mDanmuPrice);
        bundle.putString(Constants.COIN_NAME, mCoinName);
        bundle.putString(Constants.AT_NAME, atName);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "LiveInputDialogFragment");
    }



    public String getLiveUid() {
        return mLiveUid;
    }







    /**
     * ?????????????????????????????????
     */
    public void showAnchorUserDialog(int isFollowAnthor) {
        if (TextUtils.isEmpty(mLiveUid)|| StringUtil.equals(mLiveUid,CommonAppConfig.getUserBean().getId())) {
            return;
        }
        showUserDialog(mLiveUid,isFollowAnthor);
    }
    /**
     * ????????????????????????
     */
    private LiveAnthorUserDialogPop mLiveAnthorUserDialogPop;
    private void showUserDialog(String toUid,int isFollowAnthor) {
        if (!TextUtils.isEmpty(toUid)) {
            if(mLiveAnthorUserDialogPop!=null&&mLiveAnthorUserDialogPop.isShow()){
                return;
            }
            mLiveAnthorUserDialogPop=new LiveAnthorUserDialogPop(mContext);
            mLiveAnthorUserDialogPop.setFollow(isFollowAnthor);
            new XPopup.Builder(mContext)
                    .isDestroyOnDismiss(true) //???????????????????????????????????????????????????
                    .asCustom(mLiveAnthorUserDialogPop)
                    .show();
        }
    }
    public void showUserManngerDialog(String uid,int isFollowAnthor) {
        if(StringUtil.equals(uid,mLiveUid)){
            showAnchorUserDialog(isFollowAnthor);
            return;
        }
        LiveUserManngerDialogFragment liveUserManngerDialogFragment=new LiveUserManngerDialogFragment();
        Bundle bundle=new Bundle();
        bundle.putString(Constants.KEY_ID,uid);
        liveUserManngerDialogFragment.setArguments(bundle);
        liveUserManngerDialogFragment.show(((FragmentActivity)mContext).getSupportFragmentManager());
    }


    /**
     * ??? ?????? ??????
     */
    public void sendDanmuMessage(String content) {
        if (!mIsAnchor) {
            UserBean u =
                    CommonAppConfig.getUserBean();
            if (u != null && u.getLevel() < mDanMuLevel) {
                ToastUtil.show(String.format(WordUtil.getString(R.string.live_level_danmu_limit), mDanMuLevel));
                return;
            }
        }
        content = WordFilterUtil.getInstance().filter(content);
        LiveHttpUtil.sendDanmu(content, mLiveUid, mStream, mDanmuCallback);
    }


    private HttpCallback mDanmuCallback = new HttpCallback() {
        @Override
        public void onSuccess(int code, String msg, String[] info) {
            if (code == 0 && info.length > 0) {
                JSONObject obj = JSON.parseObject(info[0]);
                UserBean u =
                        CommonAppConfig.getUserBean();
                if (u != null) {
                    u.setLevel(obj.getIntValue("level"));
                    String coin = obj.getString("coin");
                    u.setCoin(coin);
                    onCoinChanged(coin);
                }
                SocketChatUtil.sendDanmuMessage(mSocketClient, obj.getString("barragetoken"));
            } else {
                ToastUtil.show(msg);
            }
        }
    };


    /**
     * ??? ?????? ??????
     */
    public void sendChatMessage(String content) {
        SocketChatUtil.sendChatMessage(mSocketClient, content, mIsAnchor, 0, mSocketUserType);
    }

    /**
     * ??? ?????? ??????
     */
    public void sendSystemMessage(String content) {
        SocketChatUtil.sendSystemMessage(mSocketClient, content);
    }

    /**
     * ??? ????????? ??????
     */
    public void sendGiftMessage(LiveGiftBean giftBean, String giftToken) {
        String liveName = "";
        if (mLiveBean != null) {
            liveName = mLiveBean.getUserNiceName();
        }
        SocketChatUtil.sendGiftMessage(mSocketClient, giftBean.getType(), giftToken, mLiveUid, liveName);
    }

    /**
     * ????????????????????????
     */
    public void kickUser(String toUid, String toName) {
        SocketChatUtil.sendKickMessage(mSocketClient, toUid, toName);
    }

    /**
     * ??????
     */
    public void setShutUp(String toUid, String toName, boolean isShut, String name) {
        SocketChatUtil.sendShutUpMessage(mSocketClient, toUid, toName, isShut, name);
    }

    /**
     * ??????????????????????????????
     */
    public void sendSetAdminMessage(boolean isSetAdmin, String toUid, String toName) {
        SocketChatUtil.sendSetAdminMessage(mSocketClient, isSetAdmin, toUid, toName);
    }


    /**
     * ?????????????????????
     */
    public void superCloseRoom() {
        SocketChatUtil.superCloseRoom(mSocketClient);
    }

    /**
     * ?????????????????????
     */
    public void sendUpdateVotesMessage(int deltaVal) {
        SocketChatUtil.sendUpdateVotesMessage(mSocketClient, deltaVal);
    }


    /**
     * ???????????????????????????
     */
    public void sendRedPackMessage() {
        SocketChatUtil.sendRedPackMessage(mSocketClient);
    }


    /**
     * ????????????????????????
     */
    public void openAddImpressWindow(String toUid) {

    }

    /**
     * ????????????????????????
     */
    public void openContributeWindow() {
        if (mLiveContributeViewHolder == null) {
            mLiveContributeViewHolder = new LiveContributeViewHolder(mContext, mPageContainer, mLiveUid);
            mLiveContributeViewHolder.subscribeActivityLifeCycle();
            mLiveContributeViewHolder.addToParent();
        }
        mLiveContributeViewHolder.show();
        if (CommonAppConfig.LIVE_ROOM_SCROLL && !mIsAnchor) {
            ((LiveAudienceActivity) this).setScrollFrozen(true);
        }
    }


    /**
     * ????????????????????????
     */
    public void openAdminListWindow() {
        if (mLiveAdminListViewHolder == null) {
            mLiveAdminListViewHolder = new LiveAdminListViewHolder(mContext, mPageContainer, mLiveUid);
            mLiveAdminListViewHolder.subscribeActivityLifeCycle();
            mLiveAdminListViewHolder.addToParent();
        }
        mLiveAdminListViewHolder.show();
    }

    /**
     * ??????????????????
     */
    protected boolean canBackPressed() {
        if (mLiveUserListViewHolder != null && mLiveUserListViewHolder.isShowed()) {
            mLiveUserListViewHolder.hide();
            return false;
        }
        if (mLiveContributeViewHolder != null && mLiveContributeViewHolder.isShowed()) {
            mLiveContributeViewHolder.hide();
            return false;
        }

        if (mLiveAdminListViewHolder != null && mLiveAdminListViewHolder.isShowed()) {
            mLiveAdminListViewHolder.hide();
            return false;
        }
        if (mLiveLuckGiftTipViewHolder != null && mLiveLuckGiftTipViewHolder.isShowed()) {
            mLiveLuckGiftTipViewHolder.hide();
            return false;
        }
        if (mLiveDaoGiftTipViewHolder != null && mLiveDaoGiftTipViewHolder.isShowed()) {
            mLiveDaoGiftTipViewHolder.hide();
            return false;
        }
        return true;
    }

    /**
     * ??????????????????
     */
    public void openShareWindow() {
        LiveShareSelectDialogFragment fragment = new LiveShareSelectDialogFragment();
        fragment.setLifeCycleListener(this);
        fragment.show(getSupportFragmentManager());
    }

    /**
     * ????????????????????????
     */
    @Override
    public void onItemClick(String type) {
        if (Constants.LINK.equals(type)) {
            copyLink();
        } else {
            shareLive(type, null);
        }
    }

    /**
     * ?????????????????????
     */
    private void copyLink() {
        if (TextUtils.isEmpty(mLiveUid)) {
            return;
        }
        ConfigBean configBean =
                CommonAppConfig.getConfig();
        if (configBean == null) {
            return;
        }
        String link = configBean.getLiveWxShareUrl() + mLiveUid;
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", link);
        cm.setPrimaryClip(clipData);
        ToastUtil.show(R.string.copy_success);
    }


    /**
     * ???????????????
     */
    public void shareLive(String type, MobCallback callback) {
        ConfigBean configBean =
                CommonAppConfig.getConfig();
        if (configBean == null) {
            return;
        }
        if (mMobShareUtil == null) {
            mMobShareUtil = new MobShareUtil();
        }
        ShareData data = new ShareData();
        data.setTitle(configBean.getLiveShareTitle());
        data.setDes(mLiveBean.getUserNiceName() + configBean.getLiveShareDes());
        data.setImgUrl(mLiveBean.getAvatarThumb());
        String webUrl = configBean.getDownloadApkUrl();
        if (Constants.MOB_WX.equals(type) || Constants.MOB_WX_PYQ.equals(type)) {
            webUrl = configBean.getLiveWxShareUrl() + mLiveUid;
        }
        data.setWebUrl(webUrl);
        mMobShareUtil.execute(type, data, callback);
    }

    /**
     * ????????????????????????
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFollowEvent(FollowEvent e) {
        if (!TextUtils.isEmpty(mLiveUid) && mLiveUid.equals(e.getToUid())) {
            if (mLiveRoomViewHolder != null) {
                mLiveRoomViewHolder.setAttention(e.getIsAttention());
            }
        }
    }



    /**
     * ??????????????????????????????
     */
    protected String getImUnReadCount() {
        return "";
    }

    /**
     * ??????????????????????????????
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCoinChangeEvent(CoinChangeEvent e) {
        onCoinChanged(e.getCoin());
        if (e.isChargeSuccess() && this instanceof LiveAudienceActivity) {
            ((LiveAudienceActivity) this).onChargeSuccess();
        }
    }

    public void onCoinChanged(String coin) {

    }

    /**
     * ?????????????????????
     */
    @Override
    public void onKeyBoardHeightChanged(int visibleHeight, int keyboardHeight) {
        if (mChatRoomOpened) {//????????????????????????????????????

        } else {
            if (mLiveRoomViewHolder != null) {
                mLiveRoomViewHolder.onKeyBoardChanged(visibleHeight, keyboardHeight);
            }
        }
    }

    @Override
    public boolean isSoftInputShowed() {
        if (mKeyBoardHeightUtil != null) {
            return mKeyBoardHeightUtil.isSoftInputShowed();
        }
        return false;
    }



    /**
     * ??????????????????
     */
    public boolean isGamePlaying() {
        return false;
    }


    /**
     * ??????????????????
     */
    public boolean isLinkMic() {
        return mLiveLinkMicPresenter != null && mLiveLinkMicPresenter.isLinkMic();
    }

    /**
     * ????????????????????????
     */
    public boolean isLinkMicAnchor() {
        return mLiveLinkMicAnchorPresenter != null && mLiveLinkMicAnchorPresenter.isLinkMic();
    }


    @Override
    protected void onPause() {
        if (mLiveLinkMicPresenter != null) {
            mLiveLinkMicPresenter.pause();
        }
        if (mLiveLinkMicAnchorPresenter != null) {
            mLiveLinkMicAnchorPresenter.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLiveLinkMicPresenter != null) {
            mLiveLinkMicPresenter.resume();
        }
        if (mLiveLinkMicAnchorPresenter != null) {
            mLiveLinkMicAnchorPresenter.resume();
        }
    }

    protected void release() {
        EventBus.getDefault().unregister(this);
        LiveHttpUtil.cancel(LiveHttpConsts.SEND_DANMU);
        if (mKeyBoardHeightUtil != null) {
            mKeyBoardHeightUtil.release();
        }
        if (mLiveLinkMicPresenter != null) {
            mLiveLinkMicPresenter.release();
        }
        if (mLiveLinkMicAnchorPresenter != null) {
            mLiveLinkMicAnchorPresenter.release();
        }
        if (mLiveLinkMicPkPresenter != null) {
            mLiveLinkMicPkPresenter.release();
        }
        if (mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.release();
        }

        if (mLiveContributeViewHolder != null) {
            mLiveContributeViewHolder.release();
        }
        if (mLiveLuckGiftTipViewHolder != null) {
            mLiveLuckGiftTipViewHolder.release();
        }
        if (mMobShareUtil != null) {
            mMobShareUtil.release();
        }
        if (mImageUtil != null) {
            mImageUtil.release();
        }
        if (mSocketClient != null) {
            mSocketClient.disConnect();
        }

        mKeyBoardHeightUtil = null;
        mLiveLinkMicPresenter = null;
        mLiveLinkMicAnchorPresenter = null;
        mLiveLinkMicPkPresenter = null;
        mLiveRoomViewHolder = null;
        mLiveContributeViewHolder = null;
        mLiveLuckGiftTipViewHolder = null;
        mMobShareUtil = null;
        mImageUtil = null;
        L.e("LiveActivity--------release------>");
    }

    @Override
    public void finish() {
        super.finish();
        release();
    }

    public String getStream() {
        return mStream;
    }

    public String getTxAppId() {
        return mTxAppId;
    }
}
