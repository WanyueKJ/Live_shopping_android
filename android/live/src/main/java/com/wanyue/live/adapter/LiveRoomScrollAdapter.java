package com.wanyue.live.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.live.R;
import com.wanyue.live.bean.LiveBean;

import java.util.List;

/**
 * Created by  on 2018/12/13.
 */

public class LiveRoomScrollAdapter extends RecyclerView.Adapter<LiveRoomScrollAdapter.Vh> {

    private Context mContext;
    private List<LiveBean> mList;
    private LayoutInflater mInflater;
    private int mCurPosition;
    private boolean mFirstLoad;
    private SparseArray<Vh> mMap;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ActionListener mActionListener;

    public LiveRoomScrollAdapter(Context context, List<LiveBean> list, int curPosition) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(context);
        mCurPosition = curPosition;
        mFirstLoad = true;
        mMap = new SparseArray<>();
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_live_room, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh vh, int position) {
        vh.setData(mList.get(position), position);
        if (mFirstLoad) {
            mFirstLoad = false;
            vh.hideCover();
            vh.onPageSelected(true);
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Vh extends RecyclerView.ViewHolder {

        ViewGroup mContainer;
        ImageView mCover;
        LiveBean mLiveBean;

        public Vh(View itemView) {
            super(itemView);
            mContainer = itemView.findViewById(R.id.container);
            mCover = itemView.findViewById(R.id.cover);
        }

        void setData(LiveBean bean, int position) {
            mLiveBean = bean;
            mMap.put(position, this);
            ImgLoader.displayBlur(mContext, bean.getThumb(), mCover);
        }

        void onPageOutWindow() {
            if (mCover != null && mCover.getVisibility() != View.VISIBLE) {
                mCover.setVisibility(View.VISIBLE);
            }
            if (mActionListener != null) {
                mActionListener.onPageOutWindow(mLiveBean.getUid());
            }
        }

        void onPageSelected(boolean first) {
            if (mActionListener != null) {
                mActionListener.onPageSelected(mLiveBean, mContainer, first);
            }
        }

        void hideCover() {
            if (mCover != null && mCover.getVisibility() == View.VISIBLE) {
                mCover.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void hideCover() {
        Vh vh = mMap.get(mCurPosition);
        if (vh != null) {
            vh.hideCover();
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull Vh vh) {
        vh.onPageOutWindow();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        mLayoutManager.setInitialPrefetchItemCount(4);
        recyclerView.scrollToPosition(mCurPosition);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int position = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (position >= 0 && mCurPosition != position) {
                    Vh vh = mMap.get(position);
                    if (vh != null) {
                        vh.onPageSelected(false);
                    }
                    mCurPosition = position;
                }
            }
        });
    }

    public void scrollNextPosition() {
        if (mRecyclerView != null && mList != null) {
            if (mCurPosition < mList.size() - 1) {
                mCurPosition++;
                mRecyclerView.scrollToPosition(mCurPosition);
                Vh vh = mMap.get(mCurPosition);
                if (vh != null) {
                    vh.onPageSelected(false);
                }
            } else {
                ToastUtil.show(R.string.live_room_last);
            }
        }
    }

    public interface ActionListener {
        void onPageSelected(LiveBean liveBean, ViewGroup container, boolean first);

        void onPageOutWindow(String liveUid);
    }


    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

}
