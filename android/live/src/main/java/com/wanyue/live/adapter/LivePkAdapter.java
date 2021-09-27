package com.wanyue.live.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.adapter.RefreshAdapter;
import com.wanyue.common.bean.LevelBean;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.utils.CommonIconUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.R;
import com.wanyue.live.bean.LivePkBean;

/**
 * Created by  on 2018/11/15.
 */

public class LivePkAdapter extends RefreshAdapter<LivePkBean> {

    private View.OnClickListener mOnClickListener;
    private String mLivePkInviteString;//邀请连麦
    private String mLivePkInviteString2;//已邀请

    public LivePkAdapter(Context context) {
        super(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick((LivePkBean) tag, 0);
                }
            }
        };
        mLivePkInviteString = WordUtil.getString(R.string.live_pk_invite);
        mLivePkInviteString2 = WordUtil.getString(R.string.live_pk_invite_2);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_live_pk, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        ((Vh) vh).setData(mList.get(position));
    }


    class Vh extends RecyclerView.ViewHolder {

        ImageView mAvatar;
        TextView mName;
        ImageView mSex;
        ImageView mLevel;
        TextView mBtnInvite;

        public Vh(View itemView) {
            super(itemView);
            mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            mName = (TextView) itemView.findViewById(R.id.name);
            mSex = (ImageView) itemView.findViewById(R.id.sex);
            mLevel = (ImageView) itemView.findViewById(R.id.level);
            mBtnInvite = (TextView) itemView.findViewById(R.id.btn_invite);
            mBtnInvite.setOnClickListener(mOnClickListener);
        }

        void setData(LivePkBean bean) {
            mBtnInvite.setTag(bean);
            ImgLoader.display(mContext,bean.getAvatar(), mAvatar);
            mName.setText(bean.getUserNiceName());
            mSex.setImageDrawable(CommonIconUtil.getSexDrawable(bean.getSex()));
            LevelBean levelBean =null;
  /*CommonAppConfig.getAnchorLevel(bean.getLevelAnchor());*/
            if (levelBean != null) {
                ImgLoader.display(mContext,levelBean.getThumb(), mLevel);
            }
            if (bean.isLinkMic()) {
                mBtnInvite.setText(mLivePkInviteString2);
                mBtnInvite.setEnabled(false);
            } else {
                mBtnInvite.setText(mLivePkInviteString);
                mBtnInvite.setEnabled(true);
            }
        }
    }
}
