package com.wanyue.live.music;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanyue.common.Constants;
import com.wanyue.common.custom.ItemSlideHelper;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.live.R;
import com.wanyue.live.custom.MusicProgressTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by  on 2017/9/2.
 */

public class LiveMusicAdapter extends RecyclerView.Adapter<LiveMusicAdapter.Vh> implements ItemSlideHelper.Callback {

    private Context mContext;
    private List<LiveMusicBean> mList;
    private LayoutInflater mInflater;
    private RecyclerView mRecyclerView;
    private View.OnClickListener mChooseClickListener;
    private View.OnClickListener mDeleteClickListener;
    private ActionListener mActionListener;
    private HashMap<String, Integer> mMap;

    public LiveMusicAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
        mMap = new HashMap<>();
        mChooseClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    int position = (int) tag;
                    LiveMusicBean bean = mList.get(position);
                    if (bean.getProgress() == 100) {
                        mActionListener.onChoose(bean);
                    } else if (bean.getProgress() == 0) {
                        mMap.put(bean.getId(), position);
                        mActionListener.onDownLoad(bean);
                    }
                }
            }
        };
        mDeleteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null) {
                    int position = (int) tag;
                    LiveMusicBean bean = mList.get(position);
                    mList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mList.size(), Constants.PAYLOAD);
                    if (mActionListener != null) {
                        mActionListener.onItemRemoved(bean.getId(), mList.size());
                    }
                }
            }
        };
    }

    @Override
    public Vh onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_live_music, parent, false));
    }

    @Override
    public void onBindViewHolder(Vh vh, int position) {
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

    public void setList(List<LiveMusicBean> list) {
        mMap.clear();
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        mMap.clear();
        mList.clear();
        notifyDataSetChanged();
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mMusicName;
        TextView mArtist;
        MusicProgressTextView mProgressTextView;
        View mBtnDelete;
        View mLine;

        public Vh(View itemView) {
            super(itemView);
            mMusicName = (TextView) itemView.findViewById(R.id.music_name);
            mArtist = (TextView) itemView.findViewById(R.id.artist);
            mProgressTextView = (MusicProgressTextView) itemView.findViewById(R.id.ptv);
            mProgressTextView.setOnClickListener(mChooseClickListener);
            mLine = itemView.findViewById(R.id.line);
            mBtnDelete = itemView.findViewById(R.id.btn_delete);
            mBtnDelete.setOnClickListener(mDeleteClickListener);
        }

        void setData(LiveMusicBean bean, int position, Object payload) {
            if (payload == null) {
                mMusicName.setText(bean.getName());
                mArtist.setText(bean.getArtist());
            }
            mProgressTextView.setTag(position);
            mBtnDelete.setTag(position);
            if (position == mList.size() - 1) {
                if (mLine.getVisibility() == View.VISIBLE) {
                    mLine.setVisibility(View.INVISIBLE);
                }
            } else {
                if (mLine.getVisibility() != View.VISIBLE) {
                    mLine.setVisibility(View.VISIBLE);
                }
            }
            mProgressTextView.setProgress(bean.getProgress());
        }
    }

    public void updateItem(String musicId, int progress) {
        if (TextUtils.isEmpty(musicId) || mMap == null || mMap.size() == 0 || mList == null || mList.size() == 0) {
            return;
        }
        Integer position = mMap.get(musicId);
        if (position != null && position >= 0 && position < mList.size()) {
            LiveMusicBean bean = mList.get(position);
            if (bean != null && musicId.equals(bean.getId())) {
                bean.setProgress(progress);
                notifyItemChanged(position, Constants.PAYLOAD);
                if (progress == 100) {
                    mMap.remove(musicId);
                }
            }
        }
    }

    public void release() {
        if (mList != null) {
            mList.clear();
        }
        if (mMap != null) {
            mMap.clear();
        }
        mContext = null;
        mActionListener = null;
        mChooseClickListener = null;
        mDeleteClickListener = null;
    }


    public void setActionListener(ActionListener listener) {
        mActionListener = listener;
    }

    public interface ActionListener {
        /**
         * 选中歌曲
         */
        void onChoose(LiveMusicBean bean);

        /**
         * 下载歌曲
         */
        void onDownLoad(LiveMusicBean bean);

        /**
         * 删除歌曲
         */
        void onItemRemoved(String musicId, int listSize);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mRecyclerView.addOnItemTouchListener(new ItemSlideHelper(mContext, this));
    }

    @Override
    public int getHorizontalRange(RecyclerView.ViewHolder holder) {
        return DpUtil.dp2px(60);
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


}
