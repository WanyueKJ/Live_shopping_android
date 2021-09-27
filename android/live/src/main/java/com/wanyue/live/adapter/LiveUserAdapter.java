package com.wanyue.live.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.wanyue.common.adapter.RefreshAdapter;
import com.wanyue.common.bean.UserBean;
import com.wanyue.live.R;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.live.bean.LiveUserListBean;


import java.util.ArrayList;
import java.util.List;

/**
 * 用户列表
 */

public class LiveUserAdapter extends RefreshAdapter<LiveUserListBean> {
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    public LiveUserAdapter(Context context) {
        super(context);

        mList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null) {
                    int position = (int) tag;
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mList.get(position), position);
                    }
                }
            }
        };
    }



    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_user_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position, @NonNull List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh)vh).setData(mList.get(position), position, payload);
    }



    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mAvatar;
        TextView mName;
        TextView mCoin;


        public Vh(View itemView) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.iv_head);
            mName =  itemView.findViewById(R.id.tv_name);
            mCoin =  itemView.findViewById(R.id.tv_coin);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(LiveUserListBean userBean, int position, Object payload) {
            itemView.setTag(position);
            if (payload == null) {
                ImgLoader.displayAvatar(mContext, userBean.getAvatar(), mAvatar);
                mName.setText(userBean.getNickname());
                mCoin.setText(userBean.getTotal());
            }
        }

    }


}
