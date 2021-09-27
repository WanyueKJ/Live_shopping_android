package com.wanyue.live.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.adapter.RefreshAdapter;
import com.wanyue.common.bean.LevelBean;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.utils.CommonIconUtil;
import com.wanyue.live.R;
import com.wanyue.live.bean.LiveShutUpBean;

import java.util.List;

/**
 * Created by  on 2019/4/27.
 */

public class LiveShutUpAdapter extends RefreshAdapter<LiveShutUpBean> {

    private View.OnClickListener mOnClickListener;

    public LiveShutUpAdapter(Context context) {
        super(context);
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_shut_up, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position, @NonNull List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh) vh).setData(mList.get(position), position, payload);
    }

    public void removeItem(String uid) {
        if (TextUtils.isEmpty(uid)) {
            return;
        }
        int position = -1;
        for (int i = 0, size = mList.size(); i < size; i++) {
            if (uid.equals(mList.get(i).getUid())) {
                position = i;
                break;
            }
        }
        if (position >= 0) {
            mList.remove(position);
//            notifyItemRemoved(position);
//            notifyItemRangeChanged(position, mList.size(), Constants.PAYLOAD);
            notifyDataSetChanged();
        }
    }


    class Vh extends RecyclerView.ViewHolder {

        ImageView mAvatar;
        TextView mName;
        ImageView mSex;
        ImageView mLevel;
        View mBtnDel;

        public Vh(View itemView) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.avatar);
            mName = itemView.findViewById(R.id.name);
            mSex = itemView.findViewById(R.id.sex);
            mLevel = itemView.findViewById(R.id.level);
            mBtnDel = itemView.findViewById(R.id.btn_delete);
            mBtnDel.setOnClickListener(mOnClickListener);
        }

        void setData(LiveShutUpBean bean, int position, Object payload) {
            mBtnDel.setTag(position);
            if (payload == null) {
                ImgLoader.displayAvatar(mContext, bean.getAvatar(), mAvatar);
                mName.setText(bean.getUserNiceName());
                mSex.setImageDrawable(CommonIconUtil.getSexDrawable(bean.getSex()));
                LevelBean levelBean =
                CommonAppConfig.getLevel(bean.getLevel());
                if (levelBean != null) {
                    ImgLoader.display(mContext, levelBean.getThumb(), mLevel);
                }
            }
        }
    }
}
