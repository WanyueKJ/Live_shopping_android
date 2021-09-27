package com.yunbao.im.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;
import com.yunbao.common.Constants;
import com.yunbao.common.custom.ItemSlideHelper;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.im.R;
import com.yunbao.im.bean.ImUserBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2018/10/24.
 */

public class ImListAdapter extends RecyclerView.Adapter implements ItemSlideHelper.Callback {

    private static final int HEAD = -1;
    private static final int ANCHOR = -2;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private List<ImUserBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private View.OnClickListener mOnDeleteClickListener;
    private ActionListener mActionListener;
    private View mHeadView;

    public ImListAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
        mList.add(new ImUserBean());
        mInflater = LayoutInflater.from(context);
        mHeadView = mInflater.inflate(R.layout.item_im_list_head, null, false);
        mHeadView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null) {
                    int position = (int) tag;
                    ImUserBean bean = mList.get(position);
                    if (bean.getUnReadCount() != 0) {
                        bean.setUnReadCount(0);
                        notifyItemChanged(position, Constants.PAYLOAD);
                    }
                    if (mActionListener != null) {
                        mActionListener.onItemClick(bean);
                    }
                }
            }
        };
        mOnDeleteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null) {
                    int position = (int) tag;
                    ImUserBean bean = mList.get(position);
                    removeItem(position);
                    if (mActionListener != null) {
                        mActionListener.onItemDelete(bean, mList.size());
                    }
                }
            }
        };
    }

    public View getHeadView() {
        return mHeadView;
    }

    public void setList(List<ImUserBean> list) {
        if (list != null && list.size() > 0) {
            mList.clear();
            mList.add(new ImUserBean());
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void setFollow(String toUid, int attent) {
        if (!TextUtils.isEmpty(toUid)) {
            for (ImUserBean bean : mList) {
                if (toUid.equals(bean.getId())) {
                    //bean.setAttent(attent);
                    break;
                }
            }
        }
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    private void removeItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mList.size());
    }

    public void removeItem(String toUid) {
        if (TextUtils.isEmpty(toUid)) {
            return;
        }
        int position = getPosition(toUid);
        if (position >= 0 && position < mList.size()) {
            ImUserBean bean = mList.get(position);
            removeItem(position);
            if (mActionListener != null) {
                mActionListener.onItemDelete(bean, mList.size());
            }
        }
    }

    public int getPosition(String uid) {
        for (int i = 1, size = mList.size(); i < size; i++) {
            if (mList.get(i).getId().equals(uid)) {
                return i;
            }
        }
        return -1;
    }

    public void updateItem(String lastMessage, String lastTime, int unReadCount, int position) {
        if (position >= 0 && position < mList.size()) {
            ImUserBean bean = mList.get(position);
            if (bean != null) {
                bean.setHasConversation(true);
                bean.setLastMessage(lastMessage);
                bean.setLastTime(lastTime);
                bean.setUnReadCount(unReadCount);
                notifyItemChanged(position, Constants.PAYLOAD);
            }
        }
    }

    public void insertItem(ImUserBean bean) {
        int position = mList.size();
        mList.add(bean);
        notifyItemInserted(position);
    }

    public void resetAllUnReadCount() {
        if (mList != null && mList.size() > 0) {
            for (ImUserBean bean : mList) {
                bean.setUnReadCount(0);
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD;
        } else if (mList.get(position).isAnchorItem()) {
            return ANCHOR;
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEAD) {
            ViewParent viewParent = mHeadView.getParent();
            if (viewParent != null) {
                ((ViewGroup) viewParent).removeView(mHeadView);
            }
            HeadVh headVh = new HeadVh(mHeadView);
            headVh.setIsRecyclable(false);
            return headVh;
        } else if (viewType == ANCHOR) {
            return new AnchorVh(mInflater.inflate(R.layout.item_im_list_anchor, parent, false));
        }
        return new Vh(mInflater.inflate(R.layout.item_im_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position, @NonNull List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        if (vh instanceof Vh) {
            ((Vh) vh).setData(mList.get(position), position, payload);
        } else if (vh instanceof AnchorVh) {
            ((AnchorVh) vh).setData(mList.get(position), position, payload);
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    class HeadVh extends RecyclerView.ViewHolder {

        public HeadVh(View itemView) {
            super(itemView);
        }
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mAvatar;
        TextView mName;
        ImageView mVip;
        TextView mMsg;
        TextView mTime;
        View mBtnDelete;
        TextView mRedPoint;

        public Vh(View itemView) {
            super(itemView);
            mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            mName = (TextView) itemView.findViewById(R.id.name);
            mVip = (ImageView) itemView.findViewById(R.id.vip);
            mMsg = (TextView) itemView.findViewById(R.id.msg);
            mTime = (TextView) itemView.findViewById(R.id.time);
            mRedPoint = (TextView) itemView.findViewById(R.id.red_point);
            mBtnDelete = itemView.findViewById(R.id.btn_delete);
            itemView.setOnClickListener(mOnClickListener);
            mBtnDelete.setOnClickListener(mOnDeleteClickListener);
        }

        void setData(ImUserBean bean, int position, Object payload) {
            itemView.setTag(position);
            mBtnDelete.setTag(position);
            if (payload == null) {
                ImgLoader.display(mContext, bean.getAvatar(), mAvatar);
                mName.setText(bean.getUserNiceName());
//                if (bean.isVip()) {
//                    if (mVip.getVisibility() != View.VISIBLE) {
//                        mVip.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    if (mVip.getVisibility() == View.VISIBLE) {
//                        mVip.setVisibility(View.INVISIBLE);
//                    }
//                }
            }
            mMsg.setText(bean.getLastMessage());
            mTime.setText(bean.getLastTime());
            if (bean.getUnReadCount() > 0) {
                if (mRedPoint.getVisibility() != View.VISIBLE) {
                    mRedPoint.setVisibility(View.VISIBLE);
                }
                mRedPoint.setText(String.valueOf(bean.getUnReadCount()));
            } else {
                if (mRedPoint.getVisibility() == View.VISIBLE) {
                    mRedPoint.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    class AnchorVh extends RecyclerView.ViewHolder {

        ImageView mAvatar;
        TextView mName;
        ImageView mVip;
        TextView mMsg;
        TextView mTime;
        TextView mRedPoint;
        View mBtnPriChat;

        public AnchorVh(View itemView) {
            super(itemView);
            mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            mName = (TextView) itemView.findViewById(R.id.name);
            mVip = (ImageView) itemView.findViewById(R.id.vip);
            mMsg = (TextView) itemView.findViewById(R.id.msg);
            mTime = (TextView) itemView.findViewById(R.id.time);
            mRedPoint = (TextView) itemView.findViewById(R.id.red_point);
            mBtnPriChat = itemView.findViewById(R.id.btn_pri_chat);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(ImUserBean bean, int position, Object payload) {
            itemView.setTag(position);
            if (payload == null) {
                ImgLoader.display(mContext, bean.getAvatar(), mAvatar);
                mName.setText(bean.getUserNiceName());
//                if (bean.isVip()) {
//                    if (mVip.getVisibility() != View.VISIBLE) {
//                        mVip.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    if (mVip.getVisibility() == View.VISIBLE) {
//                        mVip.setVisibility(View.INVISIBLE);
//                    }
//                }
            }
            mMsg.setText(bean.getLastMessage());
            if (bean.isHasConversation()) {
                if (mBtnPriChat.getVisibility() == View.VISIBLE) {
                    mBtnPriChat.setVisibility(View.INVISIBLE);
                }
                mTime.setText(bean.getLastTime());
                if (bean.getUnReadCount() > 0) {
                    if (mRedPoint.getVisibility() != View.VISIBLE) {
                        mRedPoint.setVisibility(View.VISIBLE);
                    }
                    mRedPoint.setText(String.valueOf(bean.getUnReadCount()));
                } else {
                    if (mRedPoint.getVisibility() == View.VISIBLE) {
                        mRedPoint.setVisibility(View.INVISIBLE);
                    }
                }
            } else {
                mTime.setText("");
                if (mRedPoint.getVisibility() == View.VISIBLE) {
                    mRedPoint.setVisibility(View.INVISIBLE);
                }
                if (mBtnPriChat.getVisibility() != View.VISIBLE) {
                    mBtnPriChat.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mRecyclerView.addOnItemTouchListener(new ItemSlideHelper(mContext, this));
    }


    @Override
    public int getHorizontalRange(RecyclerView.ViewHolder vh) {
        if (vh instanceof HeadVh || vh instanceof AnchorVh) {
            return 0;
        }
        return DpUtil.dp2px(70);
    }

    @Override
    public RecyclerView.ViewHolder getChildViewHolder(View childView) {
        if (mRecyclerView != null && childView != null) {
            return mRecyclerView.getChildViewHolder(childView);
        }
        return null;
    }

    @Override
    public View findTargetView(float x, float y) {
        return mRecyclerView.findChildViewUnder(x, y);
    }

    @Override
    public boolean useLeftScroll() {
        return true;
    }

    @Override
    public void onLeftScroll(RecyclerView.ViewHolder holder) {

    }

    public interface ActionListener {

        void onItemClick(ImUserBean bean);

        void onItemDelete(ImUserBean bean, int size);
    }

}
