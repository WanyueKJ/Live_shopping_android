package com.wanyue.live.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanyue.common.adapter.RefreshAdapter;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.R;
import com.wanyue.live.bean.LiveAdminRoomBean;

/**
 * Created by  on 2019/4/27.
 */

public class LiveAdminRoomAdapter extends RefreshAdapter<LiveAdminRoomBean> {

    private String mSuffix;
    private View.OnClickListener mOnClickListener;

    public LiveAdminRoomAdapter(Context context) {
        super(context);
        mSuffix = WordUtil.getString(R.string.live_admin_room);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick((LiveAdminRoomBean) tag, 0);
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_live_admin_room, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        ((Vh) vh).setData(mList.get(position));
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mName;

        public Vh(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(LiveAdminRoomBean bean) {
            itemView.setTag(bean);
            mName.setText(StringUtil.contact(bean.getUserNiceName(), mSuffix));
        }

    }
}
