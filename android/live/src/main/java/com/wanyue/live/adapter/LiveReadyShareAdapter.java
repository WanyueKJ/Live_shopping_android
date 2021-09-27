package com.wanyue.live.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.bean.ConfigBean;
import com.wanyue.common.mob.MobBean;
import com.wanyue.live.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2018/10/7.
 */

public class LiveReadyShareAdapter extends RecyclerView.Adapter<LiveReadyShareAdapter.Vh> {


    private List<MobBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private int mCheckedPosition = -1;

    public LiveReadyShareAdapter(Context context) {
        mList = new ArrayList<>();
        ConfigBean configBean =
  CommonAppConfig.getConfig();
        if (configBean != null) {
            List<MobBean> list = MobBean.getLiveReadyShareTypeList(configBean.getShareType());
            mList.addAll(list);
        }
        mInflater = LayoutInflater.from(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null) {
                    int position = (int) tag;
                    if (position == mCheckedPosition) {
                        mList.get(position).setChecked(false);
                        notifyItemChanged(position);
                        mCheckedPosition = -1;
                    } else {
                        if (mCheckedPosition >= 0 && mCheckedPosition < mList.size()) {
                            mList.get(mCheckedPosition).setChecked(false);
                            notifyItemChanged(mCheckedPosition);
                        }
                        mList.get(position).setChecked(true);
                        notifyItemChanged(position);
                        mCheckedPosition = position;
                    }
                }
            }
        };
    }

    public String getShareType() {
        if (mCheckedPosition >= 0 && mCheckedPosition < mList.size()) {
            return mList.get(mCheckedPosition).getType();
        }
        return null;
    }


    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_live_ready_share, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh vh, int position) {
        vh.setData(mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Vh extends RecyclerView.ViewHolder {
        ImageView mImg;

        public Vh(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.img);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(MobBean bean, int position) {
            itemView.setTag(position);
            if (bean.isChecked()) {
                mImg.setImageResource(bean.getIcon1());
            } else {
                mImg.setImageResource(bean.getIcon2());
            }
        }
    }


}
