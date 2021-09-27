package com.wanyue.live.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.live.R;
import com.wanyue.common.bean.LevelBean;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.interfaces.OnItemClickListener;
import com.wanyue.common.utils.CommonIconUtil;

import java.util.List;

/**
 * Created by  on 2018/10/16.
 */

public class LiveAdminListAdapter extends RecyclerView.Adapter<LiveAdminListAdapter.Vh> {

    private Context mContext;
    private List<UserBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<UserBean> mOnItemClickListener;

    public LiveAdminListAdapter(Context context, List<UserBean> list) {
        mContext=context;
        mInflater = LayoutInflater.from(context);
        mList = list;
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

    public void setOnItemClickListener(OnItemClickListener<UserBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<UserBean> list){
        mList=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_live_admin_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull Vh vh, int position, @NonNull List<Object> payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        vh.setData(mList.get(position), position, payload);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mAvatar;
        TextView mName;
        ImageView mSex;
        ImageView mLevel;
        View mBtnDel;

        public Vh(View itemView) {
            super(itemView);
            mAvatar =(ImageView) itemView.findViewById(R.id.avatar);
            mName =(TextView) itemView.findViewById(R.id.name);
            mSex = (ImageView) itemView.findViewById(R.id.sex);
            mLevel =(ImageView)  itemView.findViewById(R.id.level);
            mBtnDel = itemView.findViewById(R.id.btn_delete);
            mBtnDel.setOnClickListener(mOnClickListener);
        }

        void setData(UserBean bean, int position, Object payload) {
            mBtnDel.setTag(position);
            if (payload == null) {
                ImgLoader.displayAvatar(mContext,bean.getAvatar(), mAvatar);
                mName.setText(bean.getUserNiceName());
                mSex.setImageDrawable(CommonIconUtil.getSexDrawable(bean.getSex()));
                LevelBean levelBean =
  CommonAppConfig.getLevel(bean.getLevel());
                if (levelBean != null) {
                    ImgLoader.display(mContext,levelBean.getThumb(), mLevel);
                }
            }
        }
    }

    public void removeItem(String uid) {
        if (TextUtils.isEmpty(uid)) {
            return;
        }
        int position = -1;
        for (int i = 0, size = mList.size(); i < size; i++) {
            if (uid.equals(mList.get(i).getId())) {
                position = i;
                break;
            }
        }
        if (position >= 0) {
            mList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mList.size(), Constants.PAYLOAD);
        }
    }

    public void clear(){
        mList.clear();
        notifyDataSetChanged();
    }

    public void release(){
        if(mList!=null){
            mList.clear();
        }
        mOnClickListener=null;
        mOnItemClickListener=null;
    }
}
