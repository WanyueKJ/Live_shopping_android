package com.yunbao.im.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yunbao.common.Constants;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.im.R;
import com.yunbao.im.bean.ChatChooseImageBean;

import java.io.File;
import java.util.List;

/**
 * Created by  on 2018/6/20.
 * 聊天时候选择图片的Adapter
 */

public class ImChatChooseImageAdapter extends RecyclerView.Adapter<ImChatChooseImageAdapter.Vh> {

    private Context mContext;
    private static final int POSITION_NONE = -1;
    private List<ChatChooseImageBean> mList;
    private LayoutInflater mInflater;
    private int mSelectedPosition;
    private Drawable mCheckedDrawable;
    private Drawable mUnCheckedDrawable;
    private View.OnClickListener mOnClickListener;

    public ImChatChooseImageAdapter(Context context, List<ChatChooseImageBean> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(context);
        mSelectedPosition = POSITION_NONE;
        mCheckedDrawable = ContextCompat.getDrawable(context, R.mipmap.icon_checked);
        mUnCheckedDrawable = ContextCompat.getDrawable(context, R.mipmap.icon_checked_none);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                int position = (int) tag;
                if (position == mSelectedPosition) {
                    return;
                }
                if (mSelectedPosition == POSITION_NONE) {
                    mList.get(position).setChecked(true);
                    notifyItemChanged(position, Constants.PAYLOAD);
                } else {
                    mList.get(mSelectedPosition).setChecked(false);
                    mList.get(position).setChecked(true);
                    notifyItemChanged(mSelectedPosition, Constants.PAYLOAD);
                    notifyItemChanged(position, Constants.PAYLOAD);
                }
                mSelectedPosition = position;
            }
        };
    }

    @Override
    public Vh onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_chat_choose_img, parent, false));
    }
    @Override
    public void onBindViewHolder(Vh vh, int position) {

    }
    @Override
    public void onBindViewHolder(Vh vh, int position, List<Object> payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        vh.setData(mList.get(position), position, payload);
    }

    public File getSelectedFile() {
        if (mSelectedPosition != POSITION_NONE) {
            return mList.get(mSelectedPosition).getImageFile();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mCover;
        ImageView mImg;

        public Vh(View itemView) {
            super(itemView);
            mCover = (ImageView) itemView.findViewById(R.id.cover);
            mImg = (ImageView) itemView.findViewById(R.id.img);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(ChatChooseImageBean bean, int position, Object payload) {
            itemView.setTag(position);
            if (payload == null) {
                ImgLoader.display(mContext, bean.getImageFile(), mCover);
            }
            mImg.setImageDrawable(bean.isChecked() ? mCheckedDrawable : mUnCheckedDrawable);
        }
    }

}
