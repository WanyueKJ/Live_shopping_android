package com.yunbao.im.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.bean.ChatReceiveGiftBean;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.common.utils.ClickUtil;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.GiftTextRender;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.MD5Util;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.im.R;
import com.yunbao.im.bean.ChatInfoBean;
import com.yunbao.im.bean.ImChatImageBean;
import com.yunbao.im.bean.ImMessageBean;
import com.yunbao.im.bean.ImMsgLocationBean;
import com.yunbao.im.custom.ChatVoiceLayout;
import com.yunbao.im.custom.MyImageView;
import com.yunbao.im.interfaces.SendMsgResultCallback;
import com.yunbao.im.utils.ImDateUtil;
import com.yunbao.im.utils.ImMessageUtil;
import com.yunbao.im.utils.ImTextRender;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2018/10/25.
 */

public class ImRoomAdapter extends RecyclerView.Adapter {

    private static final byte TYPE_TEXT_LEFT = 1;
    private static final byte TYPE_TEXT_RIGHT = 2;
    private static final byte TYPE_IMAGE_LEFT = 3;
    private static final byte TYPE_IMAGE_RIGHT = 4;
    private static final byte TYPE_VOICE_LEFT = 5;
    private static final byte TYPE_VOICE_RIGHT = 6;
    private static final byte TYPE_LOCATION_LEFT = 7;
    private static final byte TYPE_LOCATION_RIGHT = 8;
    private static final byte TYPE_GIFT_LEFT = 9;
    private static final byte TYPE_GIFT_RIGHT = 10;

    private static final byte TYPE_CALL_LEFT = 11;
    private static final byte TYPE_CALL_RIGHT = 12;

    private Context mContext;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private UserBean mUserBean;
    private UserBean mToUserBean;
    private String mToUid;
    private String mTxMapAppKey;
    private String mTxMapAppSecret;
    private List<ImMessageBean> mList;
    private LayoutInflater mInflater;
    private String mUserAvatar;
    private String mToUserAvatar;
    private long mLastMessageTime;
    private ActionListener mActionListener;
    private View.OnClickListener mOnImageClickListener;
    private int[] mLocation;
    private ValueAnimator mAnimator;
    private ChatVoiceLayout mChatVoiceLayout;
    private View.OnClickListener mOnVoiceClickListener;
    private CommonCallback<File> mVoiceFileCallback;
    private Drawable mChatVideoDrawable1;
    private Drawable mChatVideoDrawable2;
    private Drawable mChatVoiceDrawable1;
    private Drawable mChatVoiceDrawable2;


    public ImRoomAdapter(Context context, String toUid, UserBean toUserBean) {
        mContext = context;
        mList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
        mUserBean = CommonAppConfig.getInstance().getUserBean();
        mToUserBean = toUserBean;
        mToUid = toUid;
        mTxMapAppKey = CommonAppConfig.getInstance().getTxMapAppKey();
        mTxMapAppSecret = CommonAppConfig.getInstance().getTxMapAppSecret();
        mUserAvatar = mUserBean.getAvatarThumb();
        mToUserAvatar = mToUserBean.getAvatarThumb();
        mLocation = new int[2];
        mOnImageClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickUtil.canClick()) {
                    return;
                }
                MyImageView imageView = (MyImageView) v;
                imageView.getLocationOnScreen(mLocation);
                if (mActionListener != null) {
                    mActionListener.onImageClick(imageView, mLocation[0], mLocation[1]);
                }
            }
        };
        mVoiceFileCallback = new CommonCallback<File>() {
            @Override
            public void callback(File file) {
                if (mRecyclerView != null) {
                    mRecyclerView.setLayoutFrozen(true);
                }
                if (mAnimator != null) {
                    mAnimator.start();
                }
                if (mActionListener != null) {
                    mActionListener.onVoiceStartPlay(file);
                }
            }
        };
        mOnVoiceClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!ClickUtil.canClick()) {
                    return;
                }
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                final int position = (int) tag;
                ImMessageBean bean = mList.get(position);
                if (mChatVoiceLayout != null) {
                    if (mRecyclerView != null) {
                        mRecyclerView.setLayoutFrozen(false);
                    }
                    mChatVoiceLayout.cancelAnim();
                    if (mChatVoiceLayout.getImMessageBean() == bean) {//同一个消息对象
                        mChatVoiceLayout = null;
                        if (mActionListener != null) {
                            mActionListener.onVoiceStopPlay();
                        }
                    } else {
                        ImMessageUtil.getInstance().setVoiceMsgHasRead(bean, new Runnable() {
                            @Override
                            public void run() {
                                notifyItemChanged(position, Constants.PAYLOAD);
                            }
                        });
                        mChatVoiceLayout = (ChatVoiceLayout) v;
                        ImMessageUtil.getInstance().getVoiceFile(bean, mVoiceFileCallback);
                    }
                } else {
                    ImMessageUtil.getInstance().setVoiceMsgHasRead(bean, new Runnable() {
                        @Override
                        public void run() {
                            notifyItemChanged(position, Constants.PAYLOAD);
                        }
                    });
                    mChatVoiceLayout = (ChatVoiceLayout) v;
                    ImMessageUtil.getInstance().getVoiceFile(bean, mVoiceFileCallback);
                }
            }
        };
        mAnimator = ValueAnimator.ofFloat(0, 900);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(700);
        mAnimator.setRepeatCount(-1);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                if (mChatVoiceLayout != null) {
                    mChatVoiceLayout.animate((int) (v / 300));
                }
            }
        });
        mChatVideoDrawable1 = ContextCompat.getDrawable(mContext, R.mipmap.icon_chat_video_1);
        mChatVideoDrawable2 = ContextCompat.getDrawable(mContext, R.mipmap.icon_chat_video_2);
        mChatVoiceDrawable1 = ContextCompat.getDrawable(mContext, R.mipmap.icon_chat_voice_1);
        mChatVoiceDrawable2 = ContextCompat.getDrawable(mContext, R.mipmap.icon_chat_voice_2);
    }

    /**
     * 停止语音动画
     */
    public void stopVoiceAnim() {
        if (mChatVoiceLayout != null) {
            mChatVoiceLayout.cancelAnim();
        }
        mChatVoiceLayout = null;
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutFrozen(false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ImMessageBean msg = mList.get(position);
        switch (msg.getType()) {
            case ImMessageBean.TYPE_TEXT:
                if (msg.isFromSelf()) {
                    return TYPE_TEXT_RIGHT;
                } else {
                    return TYPE_TEXT_LEFT;
                }
            case ImMessageBean.TYPE_IMAGE:
                if (msg.isFromSelf()) {
                    return TYPE_IMAGE_RIGHT;
                } else {
                    return TYPE_IMAGE_LEFT;
                }
            case ImMessageBean.TYPE_VOICE:
                if (msg.isFromSelf()) {
                    return TYPE_VOICE_RIGHT;
                } else {
                    return TYPE_VOICE_LEFT;
                }
            case ImMessageBean.TYPE_LOCATION:
                if (msg.isFromSelf()) {
                    return TYPE_LOCATION_RIGHT;
                } else {
                    return TYPE_LOCATION_LEFT;
                }
            case ImMessageBean.TYPE_CALL:
                if (msg.isFromSelf()) {
                    return TYPE_CALL_RIGHT;
                } else {
                    return TYPE_CALL_LEFT;
                }
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TEXT_LEFT:
                return new TextVh(mInflater.inflate(R.layout.item_chat_text_left, parent, false));
            case TYPE_TEXT_RIGHT:
                return new SelfTextVh(mInflater.inflate(R.layout.item_chat_text_right, parent, false));
            case TYPE_IMAGE_LEFT:
                return new ImageVh(mInflater.inflate(R.layout.item_chat_image_left, parent, false));
            case TYPE_IMAGE_RIGHT:
                return new SelfImageVh(mInflater.inflate(R.layout.item_chat_image_right, parent, false));
            case TYPE_VOICE_LEFT:
                return new VoiceVh(mInflater.inflate(R.layout.item_chat_voice_left, parent, false));
            case TYPE_VOICE_RIGHT:
                return new SelfVoiceVh(mInflater.inflate(R.layout.item_chat_voice_right, parent, false));
            case TYPE_LOCATION_LEFT:
                return new LocationVh(mInflater.inflate(R.layout.item_chat_location_left, parent, false));
            case TYPE_LOCATION_RIGHT:
                return new SelfLocationVh(mInflater.inflate(R.layout.item_chat_location_right, parent, false));
            case TYPE_GIFT_LEFT:
                return new GiftVh(mInflater.inflate(R.layout.item_chat_gift_left, parent, false));
            case TYPE_GIFT_RIGHT:
                return new GiftVh(mInflater.inflate(R.layout.item_chat_gift_right, parent, false));
            case TYPE_CALL_LEFT:
                return new CallVh(mInflater.inflate(R.layout.item_chat_chat_left, parent, false));
            case TYPE_CALL_RIGHT:
                return new CallVh(mInflater.inflate(R.layout.item_chat_chat_right, parent, false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position, List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh) vh).setData(mList.get(position), position, payload);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
    }

    public int insertItem(ImMessageBean bean) {
        if (mList != null && bean != null) {
            int size = mList.size();
            mList.add(bean);
            notifyItemInserted(size);
            int lastItemPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
            if (lastItemPosition != size - 1) {
                mRecyclerView.smoothScrollToPosition(size);
            } else {
                mRecyclerView.scrollToPosition(size);
            }
            return size;
        }
        return -1;
    }

    public void insertSelfItem(final ImMessageBean bean) {
        bean.setLoading(true);
        final int position = insertItem(bean);
        if (position != -1) {
            ImMessageUtil.getInstance().sendMessage(mToUid, bean, new SendMsgResultCallback() {
                @Override
                public void onSendFinish(boolean success) {
                    bean.setLoading(false);
                    if (!success) {
                        bean.setSendFail(true);
                        //消息发送失败
                        ToastUtil.show(WordUtil.getString(R.string.im_msg_send_failed));
                        L.e("IM---消息发送失败--->");
                    }
                    notifyItemChanged(position, Constants.PAYLOAD);
                }
            });
        }
    }

    public ImChatImageBean getChatImageBean(ImMessageBean imMessageBean) {
        List<ImMessageBean> list = new ArrayList<>();
        int imagePosition = 0;
        for (int i = 0, size = mList.size(); i < size; i++) {
            ImMessageBean bean = mList.get(i);
            if (bean.getType() == ImMessageBean.TYPE_IMAGE) {
                list.add(bean);
                if (bean == imMessageBean) {
                    imagePosition = list.size() - 1;
                }
            }
        }
        return new ImChatImageBean(list, imagePosition);
    }

    public void setList(List<ImMessageBean> list) {
        if (mList != null && list != null && list.size() > 0) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void scrollToBottom() {
        if (mList.size() > 0 && mLayoutManager != null) {
            mLayoutManager.scrollToPositionWithOffset(mList.size() - 1, -DpUtil.dp2px(20));
        }
    }

    public ImMessageBean getLastMessage() {
        if (mList == null || mList.size() == 0) {
            return null;
        }
        return mList.get(mList.size() - 1);
    }

    class Vh extends RecyclerView.ViewHolder {
        ImageView mAvatar;
        TextView mTime;
        ImMessageBean mImMessageBean;

        public Vh(View itemView) {
            super(itemView);
            mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            mTime = (TextView) itemView.findViewById(R.id.time);
        }

        void setData(ImMessageBean bean, int position, Object payload) {
            mImMessageBean = bean;
            if (payload == null) {
                if (bean.isFromSelf()) {
                    ImgLoader.display(mContext, mUserAvatar, mAvatar);
                } else {
                    ImgLoader.display(mContext, mToUserAvatar, mAvatar);
                }
                if (position == 0) {
                    mLastMessageTime = bean.getTime();
                    if (mTime.getVisibility() != View.VISIBLE) {
                        mTime.setVisibility(View.VISIBLE);
                    }
                    mTime.setText(ImDateUtil.getTimestampString(mLastMessageTime));
                } else {
                    if (ImDateUtil.isCloseEnough(bean.getTime(), mLastMessageTime)) {
                        if (mTime.getVisibility() == View.VISIBLE) {
                            mTime.setVisibility(View.GONE);
                        }
                    } else {
                        mLastMessageTime = bean.getTime();
                        if (mTime.getVisibility() != View.VISIBLE) {
                            mTime.setVisibility(View.VISIBLE);
                        }
                        mTime.setText(ImDateUtil.getTimestampString(mLastMessageTime));
                    }
                }
            }
        }
    }

    class TextVh extends Vh {
        TextView mText;

        public TextVh(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.text);
        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (payload == null) {
                String text = ImMessageUtil.getInstance().getMessageText(bean);
                if (!TextUtils.isEmpty(text)) {
                    mText.setText(ImTextRender.renderChatMessage(text));
                }
            }
        }
    }

    class SelfTextVh extends TextVh {

        View mFailIcon;
        View mLoading;

        public SelfTextVh(View itemView) {
            super(itemView);
            mFailIcon = itemView.findViewById(R.id.icon_fail);
            mLoading = itemView.findViewById(R.id.loading);
        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (bean.isLoading()) {
                if (mLoading.getVisibility() != View.VISIBLE) {
                    mLoading.setVisibility(View.VISIBLE);
                }
            } else {
                if (mLoading.getVisibility() == View.VISIBLE) {
                    mLoading.setVisibility(View.INVISIBLE);
                }
            }
            if (bean.isSendFail()) {
                if (mFailIcon.getVisibility() != View.VISIBLE) {
                    mFailIcon.setVisibility(View.VISIBLE);
                }
            } else {
                if (mFailIcon.getVisibility() == View.VISIBLE) {
                    mFailIcon.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    class ImageVh extends Vh {

        MyImageView mImg;
        CommonCallback<File> mCommonCallback;
        ImMessageBean mImMessageBean;

        public ImageVh(View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.img);
            mImg.setOnClickListener(mOnImageClickListener);
            mCommonCallback = new CommonCallback<File>() {
                @Override
                public void callback(File file) {
                    if (mImMessageBean != null && mImg != null) {
                        mImMessageBean.setImageFile(file);
                        mImg.setFile(file);
                        ImgLoader.display(mContext, file, mImg);
                    }
                }
            };
        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (payload == null) {
                mImMessageBean = bean;
                mImg.setImMessageBean(bean);
                File imageFile = bean.getImageFile();
                if (imageFile != null) {
                    mImg.setFile(imageFile);
                    ImgLoader.display(mContext, imageFile, mImg);
                } else {
                    ImMessageUtil.getInstance().displayImageFile(mContext, bean, mCommonCallback);
                }
            }
        }
    }

    class SelfImageVh extends ImageVh {

        View mFailIcon;
        View mLoading;

        public SelfImageVh(View itemView) {
            super(itemView);
            mFailIcon = itemView.findViewById(R.id.icon_fail);
            mLoading = itemView.findViewById(R.id.loading);
        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (bean.isLoading()) {
                if (mLoading.getVisibility() != View.VISIBLE) {
                    mLoading.setVisibility(View.VISIBLE);
                }
            } else {
                if (mLoading.getVisibility() == View.VISIBLE) {
                    mLoading.setVisibility(View.INVISIBLE);
                }
            }
            if (bean.isSendFail()) {
                if (mFailIcon.getVisibility() != View.VISIBLE) {
                    mFailIcon.setVisibility(View.VISIBLE);
                }
            } else {
                if (mFailIcon.getVisibility() == View.VISIBLE) {
                    mFailIcon.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    class VoiceVh extends Vh {

        TextView mDuration;
        View mRedPoint;
        ChatVoiceLayout mChatVoiceLayout;

        public VoiceVh(View itemView) {
            super(itemView);
            mRedPoint = itemView.findViewById(R.id.red_point);
            mDuration = (TextView) itemView.findViewById(R.id.duration);
            mChatVoiceLayout = itemView.findViewById(R.id.voice);
            mChatVoiceLayout.setOnClickListener(mOnVoiceClickListener);
        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (payload == null) {
                mDuration.setText(bean.getVoiceDuration() + "s");
                mChatVoiceLayout.setTag(position);
                mChatVoiceLayout.setImMessageBean(bean);
                mChatVoiceLayout.setDuration(bean.getVoiceDuration());
            }
            if (bean.isRead()) {
                if (mRedPoint.getVisibility() == View.VISIBLE) {
                    mRedPoint.setVisibility(View.INVISIBLE);
                }
            } else {
                if (mRedPoint.getVisibility() != View.VISIBLE) {
                    mRedPoint.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    class SelfVoiceVh extends Vh {

        TextView mDuration;
        ChatVoiceLayout mChatVoiceLayout;
        View mFailIcon;
        View mLoading;

        public SelfVoiceVh(View itemView) {
            super(itemView);
            mDuration = (TextView) itemView.findViewById(R.id.duration);
            mChatVoiceLayout = itemView.findViewById(R.id.voice);
            mChatVoiceLayout.setOnClickListener(mOnVoiceClickListener);
            mFailIcon = itemView.findViewById(R.id.icon_fail);
            mLoading = itemView.findViewById(R.id.loading);
        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (payload == null) {
                mDuration.setText(bean.getVoiceDuration() + "s");
                mChatVoiceLayout.setTag(position);
                mChatVoiceLayout.setImMessageBean(bean);
                mChatVoiceLayout.setDuration(bean.getVoiceDuration());
            }
            if (bean.isLoading()) {
                if (mLoading.getVisibility() != View.VISIBLE) {
                    mLoading.setVisibility(View.VISIBLE);
                }
            } else {
                if (mLoading.getVisibility() == View.VISIBLE) {
                    mLoading.setVisibility(View.INVISIBLE);
                }
            }
            if (bean.isSendFail()) {
                if (mFailIcon.getVisibility() != View.VISIBLE) {
                    mFailIcon.setVisibility(View.VISIBLE);
                }
            } else {
                if (mFailIcon.getVisibility() == View.VISIBLE) {
                    mFailIcon.setVisibility(View.INVISIBLE);
                }
            }
        }
    }


    class LocationVh extends Vh {

        TextView mTitle;
        TextView mAddress;
        ImageView mMap;

        public LocationVh(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mAddress = (TextView) itemView.findViewById(R.id.address);
            mMap = (ImageView) itemView.findViewById(R.id.map);
        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (payload == null) {
                ImMsgLocationBean locationBean = ImMessageUtil.getInstance().getMessageLocation(bean);
                if (locationBean == null) {
                    return;
                }
                try {
                    JSONObject obj = JSON.parseObject(locationBean.getAddress());
                    mTitle.setText(obj.getString("name"));
                    mAddress.setText(obj.getString("info"));
                } catch (Exception e) {
                    mTitle.setText("");
                    mAddress.setText("");
                }
                int zoom = locationBean.getZoom();
                if (zoom > 18 || zoom < 4) {
                    zoom = 16;
                }
                double lat = locationBean.getLat();
                double lng = locationBean.getLng();
                //腾讯地图生成静态图接口
                String sign = MD5Util.getMD5("/ws/staticmap/v2/?center=" + lat + "," + lng + "&key=" + mTxMapAppKey + "&scale=2&size=200*120&zoom=" + zoom + mTxMapAppSecret);

                String staticMapUrl = "https://apis.map.qq.com/ws/staticmap/v2/?center=" + lat + "," + lng + "&size=200*120&scale=2&zoom=" + zoom + "&key=" + mTxMapAppKey + "&sig=" + sign;
                ImgLoader.display(mContext, staticMapUrl, mMap);
            }
        }
    }

    class SelfLocationVh extends LocationVh {

        View mFailIcon;
        View mLoading;

        public SelfLocationVh(View itemView) {
            super(itemView);
            mFailIcon = itemView.findViewById(R.id.icon_fail);
            mLoading = itemView.findViewById(R.id.loading);
        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (bean.isLoading()) {
                if (mLoading.getVisibility() != View.VISIBLE) {
                    mLoading.setVisibility(View.VISIBLE);
                }
            } else {
                if (mLoading.getVisibility() == View.VISIBLE) {
                    mLoading.setVisibility(View.INVISIBLE);
                }
            }
            if (bean.isSendFail()) {
                if (mFailIcon.getVisibility() != View.VISIBLE) {
                    mFailIcon.setVisibility(View.VISIBLE);
                }
            } else {
                if (mFailIcon.getVisibility() == View.VISIBLE) {
                    mFailIcon.setVisibility(View.INVISIBLE);
                }
            }
        }
    }


    class GiftVh extends Vh {

        ImageView mGiftIcon;
        TextView mGiftName;
        TextView mGiftCount;

        public GiftVh(View itemView) {
            super(itemView);
            mGiftIcon = itemView.findViewById(R.id.gift_icon);
            mGiftName = itemView.findViewById(R.id.gift_name);
            mGiftCount = itemView.findViewById(R.id.gift_count);
        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (payload == null) {
                ChatReceiveGiftBean giftBean = bean.getGiftBean();
                if (giftBean == null) {
                    return;
                }
                ImgLoader.display(mContext, giftBean.getGiftIcon(), mGiftIcon);
                mGiftName.setText(giftBean.getGiftName());
                mGiftCount.setText(GiftTextRender.renderGiftCount2(giftBean.getGiftCount()));
            }
        }
    }

    class CallVh extends Vh {
        ImageView mIcon;
        TextView mText;

        public CallVh(View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.text);
            mIcon = itemView.findViewById(R.id.icon);
        }
        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            ChatInfoBean chatInfoBean=bean.getChatInfoBean();
            if (payload == null) {
                if (chatInfoBean != null) {
                    mText.setText(chatInfoBean.getContent());
                    if (chatInfoBean.getCallType() == Constants.CHAT_TYPE_VIDEO) {
                        if (bean.isFromSelf()) {
                            mIcon.setImageDrawable(mChatVideoDrawable1);
                        } else {
                            mIcon.setImageDrawable(mChatVideoDrawable2);
                        }
                    } else {
                        if (bean.isFromSelf()) {
                            mIcon.setImageDrawable(mChatVoiceDrawable1);
                        } else {
                            mIcon.setImageDrawable(mChatVoiceDrawable2);
                        }
                    }
                }
            }
        }
    }


    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public interface ActionListener {
        void onImageClick(MyImageView imageView, int x, int y);

        void onVoiceStartPlay(File voiceFile);

        void onVoiceStopPlay();
    }

    public void release() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        mActionListener = null;
        mOnImageClickListener = null;
        mOnVoiceClickListener = null;
        mContext = null;
    }

    public void clear() {
        if (mList != null) {
            mList.clear();
        }
        notifyDataSetChanged();
    }

}
