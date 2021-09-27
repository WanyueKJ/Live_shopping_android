package com.wanyue.live.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanyue.common.interfaces.OnItemClickListener;
import com.wanyue.live.R;
import com.wanyue.live.bean.LiveChatBean;
import com.wanyue.live.utils.LiveTextRender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2018/10/10.
 */

public class LiveChatAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<LiveChatBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<LiveChatBean> mOnItemClickListener;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    public LiveChatAdapter(Context context) {
        mContext=context;
        mList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null) {
                    LiveChatBean bean = (LiveChatBean) tag;
                    if (bean.getType() != LiveChatBean.SYSTEM && mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(bean, 0);
                    }
                }
            }
        };
    }

    public void setOnItemClickListener(OnItemClickListener<LiveChatBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LiveChatBean.RED_PACK) {
            return new RedPackVh(mInflater.inflate(R.layout.item_live_chat_red_pack, parent, false));
        } else {
            return new Vh(mInflater.inflate(R.layout.item_live_chat, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        if (vh instanceof Vh) {
            ((Vh) vh).setData(mList.get(position));
        } else if (vh instanceof RedPackVh) {
            ((RedPackVh) vh).setData(mList.get(position));
        }
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

    class RedPackVh extends RecyclerView.ViewHolder {

        TextView mTextView;

        public RedPackVh(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }

        void setData(LiveChatBean bean) {
            mTextView.setText(bean.getContent());
        }
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mTextView;

        public Vh(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(LiveChatBean bean) {
            itemView.setTag(bean);
            if (bean.getType() == LiveChatBean.SYSTEM) {
                mTextView.setTextColor(0xffff6131);
                mTextView.setText(bean.getContent());
            } else {
                if (bean.getType() == LiveChatBean.ENTER_ROOM || bean.getType() == LiveChatBean.LIGHT) {
                    mTextView.setTextColor(0xffc8c8c8);
                } else {
                    mTextView.setTextColor(0xffffffff);
                }
                LiveTextRender.render(mContext,mTextView, bean);
            }
        }
    }

    public void insertItem(LiveChatBean bean) {
        if (bean == null) {
            return;
        }
        int size = mList.size();
        mList.add(bean);
        notifyItemInserted(size);
        int lastItemPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
        if (lastItemPosition != size - 1) {
            mRecyclerView.smoothScrollToPosition(size);
        } else {
            mRecyclerView.scrollToPosition(size);
        }
    }

    public void scrollToBottom() {
        if (mList.size() > 0) {
            mRecyclerView.smoothScrollToPosition(mList.size() - 1);
        }
    }

    public void clear(){
        if(mList!=null){
            mList.clear();
        }
        notifyDataSetChanged();
    }
}
