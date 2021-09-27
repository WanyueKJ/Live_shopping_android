package com.wanyue.live.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxj.xpopup.XPopup;
import com.opensource.svgaplayer.SVGAImageView;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.api.CommonAPI;
import com.wanyue.common.event.FollowEvent;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.http.CommonHttpConsts;
import com.wanyue.common.http.CommonHttpUtil;
import com.wanyue.common.http.ParseSingleHttpCallback;
import com.wanyue.common.interfaces.CommonCallback;
import com.wanyue.common.mob.MobShareUtil;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.ViewUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.common.views.AbsViewHolder;
import com.wanyue.live.R;
import com.wanyue.live.activity.LiveActivity;
import com.wanyue.live.activity.LiveAnchorActivity;
import com.wanyue.live.activity.LiveAudienceActivity;
import com.wanyue.live.adapter.LiveNewChatAdapter;
import com.wanyue.live.bean.GlobalGiftBean;
import com.wanyue.live.bean.LiveChatBean;
import com.wanyue.live.bean.LiveDanMuBean;
import com.wanyue.live.bean.LiveEnterRoomBean;
import com.wanyue.live.bean.LiveGiftPrizePoolWinBean;
import com.wanyue.live.bean.LiveLuckGiftWinBean;
import com.wanyue.live.bean.LiveReceiveGiftBean;
import com.wanyue.live.bean.LiveUserGiftBean;
import com.wanyue.live.custom.LiveLightView;
import com.wanyue.live.custom.TopGradual;
import com.wanyue.live.dialog.LiveAnthorUserDialogPop;
import com.wanyue.live.dialog.LiveUserDialogFragment;
import com.wanyue.live.dialog.LiveUserManngerDialogFragment;
import com.wanyue.live.event.LiveGoodsChangeEvent;
import com.wanyue.live.http.LiveHttpConsts;
import com.wanyue.live.http.LiveHttpUtil;
import com.wanyue.live.http.LiveShopAPI;
import com.wanyue.live.presenter.LiveDanmuPresenter;
import com.wanyue.live.presenter.LiveGiftAnimPresenter;
import com.wanyue.live.presenter.LiveLightAnimPresenter;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.List;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by  on 2018/10/9.
 * 直播间公共逻辑
 */

public class LiveRoomViewHolder extends AbsViewHolder implements View.OnClickListener {

    private int mOffsetY;
    private ViewGroup mRoot;
    private ImageView mAvatar;
    private ImageView mLevelAnchor;
    private TextView mName;
    private TextView mID;
    private View mBtnFollow;
    private RecyclerView mChatRecyclerView;
    private LiveNewChatAdapter mLiveChatAdapter;
    private String mLiveUid;
    private String mStream;
    private LiveLightAnimPresenter mLightAnimPresenter;
    private LiveDanmuPresenter mLiveDanmuPresenter;
    private LiveGiftAnimPresenter mLiveGiftAnimPresenter;
    private LiveRoomHandler mLiveRoomHandler;
    private ParseSingleHttpCallback mParseHttpCallback; //直播间人数
    protected int mUserListInterval;//用户列表刷新时间的间隔
    private GifImageView mGifImageView;
    private SVGAImageView mSVGAImageView;
    private ViewGroup mLiveGiftPrizePoolContainer;
    private TextView mTvFollowCount;
    private TextView mTvUserCount;
    private TextView mTvGoodsNum;
    private int mLiveNum;
    private int mLiveLikes;
    private int mLiveGoodsNum;
    private ViewGroup mGroup1;
    private LiveActivity mLiveActivity;


    public LiveRoomViewHolder(Context context, ViewGroup parentView, GifImageView gifImageView, SVGAImageView svgaImageView, ViewGroup liveGiftPrizePoolContainer) {
        super(context, parentView);
        mGifImageView = gifImageView;
        mSVGAImageView = svgaImageView;
        mLiveGiftPrizePoolContainer = liveGiftPrizePoolContainer;
        mLiveActivity=((LiveActivity)mContext);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_room;
    }

    @Override
    public void init() {
        mRoot =  findViewById(R.id.root);
        mAvatar =  findViewById(R.id.avatar);
        mLevelAnchor =  findViewById(R.id.level_anchor);
        mName = findViewById(R.id.name);
        mID =  findViewById(R.id.id_val);
        mBtnFollow = findViewById(R.id.btn_follow);
        mTvUserCount =  findViewById(R.id.tv_user_count);
        mTvFollowCount =findViewById(R.id.tv_follow_count);
        mTvGoodsNum =  findViewById(R.id.tv_goods_num);
        mGroup1 = (ViewGroup) findViewById(R.id.group_1);

        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) mGroup1.getLayoutParams();
        params.topMargin=CommonAppConfig.statuBarHeight()+ DpUtil.dp2px(6);
        //聊天栏
        mChatRecyclerView = findViewById(R.id.chat_recyclerView);
        mChatRecyclerView.setHasFixedSize(true);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mChatRecyclerView.addItemDecoration(new TopGradual());
        mLiveChatAdapter = new LiveNewChatAdapter(null);
        mLiveChatAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(mLiveChatAdapter==null){
                    return;
                }
                if(view.getId()==R.id.tv_user_name){
                    LiveChatBean bean=mLiveChatAdapter.getItem(position);
                    if (mLiveActivity!=null&&bean!=null&&!StringUtil.equals(bean.getId(),CommonAppConfig.getUid())){
                        mLiveActivity.showUserManngerDialog(bean.getId(),mIsFollowAnthor);
                    }

                }
            }
        });
        mChatRecyclerView.setAdapter(mLiveChatAdapter);
        mBtnFollow.setOnClickListener(this);
        mAvatar.setOnClickListener(this);
        mTvUserCount.setOnClickListener(this);

        findViewById(R.id.btn_goods).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
        if(mContext!=null&&mContext instanceof LiveAudienceActivity){
            mRoot.setOnClickListener(this);
        }


        mLightAnimPresenter = new LiveLightAnimPresenter(mContext, mParentView);
        mLiveRoomHandler = new LiveRoomHandler(this);
        mParseHttpCallback= new ParseSingleHttpCallback<Integer>("nums") {
            @Override
            public void onSuccess(Integer data) {
                if(data!=null){
                  setLiveUserNum(data);
                }
            }
        };

        getLikeNums();
        getGoodsSaleNums();
        setLiveUserNum(0);
    }

    ParseSingleHttpCallback mGoodsHttpCallBack;

    private void getGoodsSaleNums() {
        if(mGoodsHttpCallBack==null){
           mGoodsHttpCallBack =new ParseSingleHttpCallback<Integer>("nums") {
                @Override
                public void onSuccess(Integer data) {
                    if(data!=null){
                        setGoodsNum(data);
                    }
                }
            };
        }
        LiveHttpUtil.cancel(LiveHttpConsts.LIVE_GOODS_NUMS);
        LiveShopAPI.getLiveGoodsNums(mStream,mGoodsHttpCallBack);
        startGoodsSaleNums();

    }

    private void startGoodsSaleNums() {
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.sendEmptyMessageAtTime(LiveRoomHandler.WHAT_SALE_GOODS, getNextTime(10000));
        }
    }

    public void setGoodsNum(Integer data) {
        if(mTvGoodsNum!=null&&data!=null){
           mLiveGoodsNum=data;
           mTvGoodsNum.setText(Integer.toString(mLiveGoodsNum));
        }
    }

    private ParseSingleHttpCallback mLikeHttpCallBack;

    private void getLikeNums() {
        if(mLikeHttpCallBack==null){
            mLikeHttpCallBack =new ParseSingleHttpCallback<Integer>("nums") {
                @Override
                public void onSuccess(Integer data) {
                    if(data!=null){
                        setLiveNums(data);
                    }
                }
            };
        }
        LiveHttpUtil.cancel(LiveHttpConsts.GET_LIVE_LIKES);
        LiveHttpUtil.getLiveLikes(mStream,mLikeHttpCallBack);
        startAnchorLike();
    }


    public void setLiveNums(int data) {
        mLiveLikes=data;
        if(mTvFollowCount!=null){
          mTvFollowCount.setText(Integer.toString(data));
        }
    }



    /*设置直播人数*/
    public void setLiveUserNum(int liveNum) {
        if(liveNum<0){
           liveNum =0;
        }
        mLiveNum=liveNum;
        ViewUtil.setVisibility(mTvUserCount,View.VISIBLE);
        mTvUserCount.setText(WordUtil.getString(R.string.person_num,mLiveNum));
    }

    /**
     * 显示主播头像
     */

    public void setAvatar(String url) {
        if (mAvatar != null) {
            ImgLoader.displayAvatar(mContext, url, mAvatar);
        }
    }


    /**
     * 关闭直播
     */

    private void close() {
        if(mContext instanceof LiveAnchorActivity){
            ((LiveAnchorActivity) mContext).closeLive();
        }else{
            ((LiveAudienceActivity) mContext).close();
        }
    }

    /**
     * 显示主播等级
     */
    public void setAnchorLevel(int anchorLevel) {
        if (mLevelAnchor != null) {
        }
    }

    /**
     * 显示用户名
     */

    public void setName(String name) {
        if (mName != null) {
            mName.setText(name);
        }
    }

    /**
     * 显示房间号
     */

    public void setRoomNum(String roomNum) {
        if (mID != null) {
            mID.setText(roomNum);
        }
    }

    /**
     * 显示直播标题
     */

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            if (mLiveGiftAnimPresenter == null) {
                mLiveGiftAnimPresenter = new LiveGiftAnimPresenter(mContext, mContentView, mGifImageView, mSVGAImageView, mLiveGiftPrizePoolContainer);
            }
            mLiveGiftAnimPresenter.showLiveTitleAnim(title);
        }
    }


    /**
     * 显示是否关注
     */

    private int mIsFollowAnthor;
    public void setAttention(int attention) {
        mIsFollowAnthor=attention;
        if (mBtnFollow != null) {
            if (attention == 0) {
                if (mBtnFollow.getVisibility() != View.VISIBLE) {
                    mBtnFollow.setVisibility(View.VISIBLE);
                }
            } else {
                if (mBtnFollow.getVisibility() == View.VISIBLE) {
                    mBtnFollow.setVisibility(View.GONE);
                }
            }
        }
    }

    public int getIsFollowAnthor() {
        return mIsFollowAnthor;
    }



    public void setLiveInfo(String liveUid, String stream, int userListInterval) {
        mLiveUid = liveUid;
        mStream = stream;
        mUserListInterval = userListInterval;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.root) {
            light();
        }
        if (!canClick()) {
            return;
        }
        if (i == R.id.avatar) {
            if (mLiveActivity!=null){
                mLiveActivity.showAnchorUserDialog(mIsFollowAnthor);
            }
        } else if (i == R.id.btn_follow) {
            follow();
        } else if (i == R.id.btn_goods) {
            openShopGoods();
        }else if(i==R.id.btn_close){
            close();
        }else if(i==R.id.tv_user_count){
            openUserList();
        }
    }


    private void openUserList(){
        if(mContext instanceof LiveActivity){
            ((LiveActivity) mContext).openUserListView();
        }
    }

    private void openShopGoods() {
        if(mContext instanceof LiveAnchorActivity){
           ((LiveAnchorActivity) mContext).openShopGoods();
        }
    }

    /**
     * 关注主播
     */
    private void follow() {
        if (TextUtils.isEmpty(mLiveUid)) {
            return;
        }
        CommonAPI.setAttention(mLiveUid, new ParseSingleHttpCallback<Integer>("isattent") {
            @Override
            public void onSuccess(Integer isAttention) {
                setAttention(isAttention);
                if (isAttention == 1) {
                    EventBus.getDefault().post(new FollowEvent(mLiveUid,isAttention));
                    ToastUtil.show(R.string.follow_succ);
                }
            }
        });
    }

    /**
     * 用户进入房间，用户列表添加该用户
     */
    public void insertUser(String uid) {
        if(TextUtils.isEmpty(uid)){
            return;
        }
        //setLiveUserNum(++mLiveNum);
    }


    /**
     * 用户离开房间，用户列表删除该用户
     */
    public void removeUser(String uid) {
       // setLiveUserNum(--mLiveNum);
    }


    /**
     * 刷新用户列表
     */
    private void refreshUserList() {
        if (!TextUtils.isEmpty(mLiveUid) && mParseHttpCallback != null) {
            LiveHttpUtil.cancel(LiveHttpConsts.GET_USER_NUMS);
            LiveHttpUtil.getUserNums(mStream, mParseHttpCallback);
            startRefreshUserList();
        }
    }

    /**
     * 开始刷新用户列表
     */
    public void startRefreshUserList() {
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.sendEmptyMessageAtTime(LiveRoomHandler.WHAT_REFRESH_USER_LIST, getNextTime(mUserListInterval > 0 ? mUserListInterval : 10000));
        }
    }

    /**
     * 添加聊天消息到聊天栏
     */

    public void insertChat(LiveChatBean bean) {
        if (mLiveChatAdapter != null) {
            mLiveChatAdapter.addData(bean);
            if(mChatRecyclerView!=null){
             int lastItemPosition = ((LinearLayoutManager)mChatRecyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
               int size=mLiveChatAdapter.size();
                if (lastItemPosition != size - 1) {
                    mChatRecyclerView.smoothScrollToPosition(size);
                } else {
                    mChatRecyclerView.scrollToPosition(size);
                }
            }

        }
    }

    /**
     * 播放飘心动画
     */
    public void playLightAnim() {
        if (mLightAnimPresenter != null) {
            mLightAnimPresenter.play();
        }
    }

    /**
     * 点亮
     */
    private void light() {
        ((LiveAudienceActivity) mContext).light();
    }


    public void setOffsetY(int offsetY) {
        LiveLightView.sOffsetY = offsetY;
        mOffsetY = offsetY;
    }



    /**
     * 键盘高度变化
     */
    public void onKeyBoardChanged(int visibleHeight, int keyBoardHeight) {
        if (mRoot != null) {
            if (keyBoardHeight == 0) {
                mRoot.setTranslationY(0);
                return;
            }
            if (mOffsetY == 0) {
                mRoot.setTranslationY(-keyBoardHeight);
                return;
            }
            if (mOffsetY > 0 && mOffsetY < keyBoardHeight) {
                mRoot.setTranslationY(mOffsetY - keyBoardHeight);
            }
        }
    }

    /**
     * 聊天栏滚到最底部
     */
    public void chatScrollToBottom() {
        if(mChatRecyclerView!=null&&mLiveChatAdapter!=null){
            int position= mLiveChatAdapter.size();
            int postion= position>0?position-1:0;
            mChatRecyclerView.scrollToPosition(postion);
        }
    }

    /**
     * 用户进入房间 金光一闪,坐骑动画
     */
    public void onEnterRoom(LiveEnterRoomBean bean) {
        if (bean == null) {
            return;
        }
        /*if (mLiveEnterRoomAnimPresenter != null) {
            mLiveEnterRoomAnimPresenter.enterRoom(bean);
        }*/
    }

    /**
     * 显示弹幕
     */
    public void showDanmu(LiveDanMuBean bean) {

        if (mLiveDanmuPresenter == null) {
            mLiveDanmuPresenter = new LiveDanmuPresenter(mContext, mParentView);
        }
        mLiveDanmuPresenter.showDanmu(bean);
    }



    /**
     * 直播间贡献榜窗口
     */
    private void openContributeWindow() {
        ((LiveActivity) mContext).openContributeWindow();
    }


    /**
     * 显示礼物动画
     */
    public void showGiftMessage(LiveReceiveGiftBean bean) {
        //mVotes.setText(bean.getVotes());
        if (mLiveGiftAnimPresenter == null) {
            mLiveGiftAnimPresenter = new LiveGiftAnimPresenter(mContext, mContentView, mGifImageView, mSVGAImageView, mLiveGiftPrizePoolContainer);
        }
        mLiveGiftAnimPresenter.showGiftAnim(bean);
    }

    /**
     * 增加主播映票数
     *
     * @param deltaVal 增加的映票数量
     */
    public void updateVotes(String deltaVal) {

    }

    public ViewGroup getInnerContainer() {
        return (ViewGroup) findViewById(R.id.inner_container);
    }


    /**
     * 主播显示直播时间
     */
    private void showAnchorLiveTime() {
        /*if (mLiveTimeTextView != null) {
            mAnchorLiveTime += 1000;
            mLiveTimeTextView.setText(StringUtil.getDurationText(mAnchorLiveTime));
            startAnchorLiveTime();
        }*/
    }

    public void startAnchorLiveTime() {
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.sendEmptyMessageAtTime(LiveRoomHandler.WHAT_ANCHOR_LIVE_TIME, getNextTime(1000));
        }
    }


    /**
     * 主播开始飘心
     */
    public void startAnchorLight() {
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.removeMessages(LiveRoomHandler.WHAT_ANCHOR_LIGHT);
            mLiveRoomHandler.sendEmptyMessageAtTime(LiveRoomHandler.WHAT_ANCHOR_LIGHT, getNextTime(1000));
        }
    }


    /**
     * 主播checkLive
     */
    public void startAnchorCheckLive() {
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.removeMessages(LiveRoomHandler.WHAT_ANCHOR_CHECK_LIVE);
            mLiveRoomHandler.sendEmptyMessageAtTime(LiveRoomHandler.WHAT_ANCHOR_CHECK_LIVE, getNextTime(60000));
        }

    }

    /**
     * 主播checkLive
     */
    private void anchorCheckLive() {
        if (mContext instanceof LiveAnchorActivity) {
            ((LiveAnchorActivity) mContext).checkLive();
            startAnchorCheckLive();
        }
    }


    /**
     * 主播切后台，50秒后关闭直播
     */
    public void anchorPause() {
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.sendEmptyMessageAtTime(LiveRoomHandler.WHAT_ANCHOR_PAUSE, getNextTime(50000));
        }
    }

    private long getNextTime(int time) {
        long now = SystemClock.uptimeMillis();
        if (time < 1000) {
            return now + time;
        }
        return now + time + -now % 1000;
    }


    /**
     * 主播切后台后又回到前台
     */


    public void anchorResume() {
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.removeMessages(LiveRoomHandler.WHAT_ANCHOR_PAUSE);
        }
    }

    /**
     * 主播结束直播
     */
    private void anchorEndLive() {
        if (mContext instanceof LiveAnchorActivity) {
            ((LiveAnchorActivity) mContext).endLive();
        }
    }

    /**
     * 主播飘心
     */
    private void anchorLight() {
        if (mContext instanceof LiveAnchorActivity) {
            ((LiveAnchorActivity) mContext).light();
            startAnchorLight();
        }
    }


    /**
     * 幸运礼物中奖
     */
    public void onLuckGiftWin(LiveLuckGiftWinBean bean) {
        if (mLiveGiftAnimPresenter == null) {
            mLiveGiftAnimPresenter = new LiveGiftAnimPresenter(mContext, mContentView, mGifImageView, mSVGAImageView, mLiveGiftPrizePoolContainer);
        }
        mLiveGiftAnimPresenter.showLuckGiftWinAnim(bean);
    }


    /**
     * 奖池中奖
     */
    public void onPrizePoolWin(LiveGiftPrizePoolWinBean bean) {
        if (mLiveGiftAnimPresenter == null) {
            mLiveGiftAnimPresenter = new LiveGiftAnimPresenter(mContext, mContentView, mGifImageView, mSVGAImageView, mLiveGiftPrizePoolContainer);
        }
        mLiveGiftAnimPresenter.showPrizePoolWinAnim(bean);
    }

    /**
     * 奖池升级
     */
    public void onPrizePoolUp(String level) {
        if (mLiveGiftAnimPresenter == null) {
            mLiveGiftAnimPresenter = new LiveGiftAnimPresenter(mContext, mContentView, mGifImageView, mSVGAImageView, mLiveGiftPrizePoolContainer);
        }
    }


    /**
     * 全站礼物
     */


    public void onGlobalGift(GlobalGiftBean bean) {
        if (mLiveGiftAnimPresenter == null) {
            mLiveGiftAnimPresenter = new LiveGiftAnimPresenter(mContext, mContentView, mGifImageView, mSVGAImageView, mLiveGiftPrizePoolContainer);
        }
        mLiveGiftAnimPresenter.showGlobalGift(bean);
    }



    @Override
    public void release() {
        //EventBus.getDefault().unregister(this);
        LiveHttpUtil.cancel(LiveHttpConsts.GET_USER_NUMS);
        LiveHttpUtil.cancel(LiveHttpConsts.TIME_CHARGE);
        CommonHttpUtil.cancel(CommonHttpConsts.SET_ATTENTION);
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.release();
        }
        mLiveRoomHandler = null;
        if (mLightAnimPresenter != null) {
            mLightAnimPresenter.release();
        }
        if (mLiveGiftAnimPresenter != null) {
            mLiveGiftAnimPresenter.release();
        }
    }

    public void clearData() {
        LiveHttpUtil.cancel(LiveHttpConsts.GET_USER_LIST);
        LiveHttpUtil.cancel(LiveHttpConsts.GET_LIVE_LIKES);
        LiveHttpUtil.cancel(LiveHttpConsts.LIVE_GOODS_NUMS);
        LiveHttpUtil.cancel(LiveHttpConsts.TIME_CHARGE);
        CommonHttpUtil.cancel(CommonHttpConsts.SET_ATTENTION);
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.removeCallbacksAndMessages(null);
        }
        if (mAvatar != null) {
            mAvatar.setImageDrawable(null);
        }
        if (mLevelAnchor != null) {
            mLevelAnchor.setImageDrawable(null);
        }
        if (mName != null) {
            mName.setText("");
        }
        if (mID != null) {
            mID.setText("");
        }

        if (mLiveDanmuPresenter != null) {
            mLiveDanmuPresenter.release();
            mLiveDanmuPresenter.reset();
        }
        if (mLiveGiftAnimPresenter != null) {
            mLiveGiftAnimPresenter.cancelAllAnim();
        }
    }

    public void setVotes(String votestotal) {

    }

    private static class LiveRoomHandler extends Handler {
        private LiveRoomViewHolder mLiveRoomViewHolder;
        private static final int WHAT_REFRESH_USER_LIST = 1;
        private static final int WHAT_ANCHOR_LIVE_TIME = 3;//直播间主播计时
        private static final int WHAT_ANCHOR_PAUSE = 4;//主播切后台
        private static final int WHAT_ANCHOR_LIGHT = 5;//主播飘心
        private static final int WHAT_ANCHOR_CHECK_LIVE = 6;//主播checkLive
        private static final int WHAT_ANCHOR_LIKE = 7;//点赞
        private static final int WHAT_SALE_GOODS = 8;//商品

        public LiveRoomHandler(LiveRoomViewHolder liveRoomViewHolder) {
            mLiveRoomViewHolder = new WeakReference<>(liveRoomViewHolder).get();
        }

        @Override
        public void handleMessage(Message msg) {
            if (mLiveRoomViewHolder != null) {
                switch (msg.what) {
                    case WHAT_REFRESH_USER_LIST:
                        mLiveRoomViewHolder.refreshUserList();
                        break;
                    case WHAT_ANCHOR_LIVE_TIME:
                        mLiveRoomViewHolder.showAnchorLiveTime();
                        break;
                    case WHAT_ANCHOR_PAUSE:
                        //mLiveRoomViewHolder.anchorEndLive();
                        break;
                    case WHAT_ANCHOR_LIGHT:
                        mLiveRoomViewHolder.anchorLight();
                        break;
                    case WHAT_ANCHOR_CHECK_LIVE:
                        mLiveRoomViewHolder.anchorCheckLive();
                        break;
                    case WHAT_ANCHOR_LIKE:
                        mLiveRoomViewHolder.getLikeNums();
                        break;
                    case WHAT_SALE_GOODS:
                        mLiveRoomViewHolder.getGoodsSaleNums();
                    default:
                        break;
                }
            }
        }

        public void release() {
            removeCallbacksAndMessages(null);
            mLiveRoomViewHolder = null;
        }
    }

    private void startAnchorLike() {
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.sendEmptyMessageAtTime(LiveRoomHandler.WHAT_ANCHOR_LIKE, getNextTime(10000));
        }
    }


}
