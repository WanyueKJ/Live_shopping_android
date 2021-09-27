package com.wanyue.live.adapter;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.bean.LiveGiftBean;
import com.wanyue.live.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2018/7/11.
 */

public class LiveGiftPagerAdapter<T extends LiveGiftBean> extends PagerAdapter {

    private Context mContext;
    private List<RecyclerView> mViewList;
    private static final int GIFT_COUNT = 10;//每页10个礼物
    private int mPage = -1;
    private ActionListener mActionListener;
    private int mGiftType;//礼物类型  0普通礼物  1道具  2背包

    public LiveGiftPagerAdapter(Context context, List<T> giftList, int giftType) {
        mContext = context;
        mGiftType = giftType;
        mViewList = new ArrayList<>();
        int fromIndex = 0;
        int size = giftList.size();
        int pageCount = size / GIFT_COUNT;
        if (size % GIFT_COUNT > 0) {
            pageCount++;
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        String coinName = CommonAppConfig.getCoinName();
        LiveGiftAdapter.ActionListener actionListener = new LiveGiftAdapter.ActionListener() {
            @Override
            public void onCancel() {
                if (mPage >= 0 && mPage < mViewList.size()) {
                    LiveGiftAdapter adapter = (LiveGiftAdapter) mViewList.get(mPage).getAdapter();
                    if (adapter != null) {
                        adapter.cancelChecked();
                    }
                }
            }

            @Override
            public void onItemChecked(LiveGiftBean bean) {
                mPage = bean.getPage();
                if (mActionListener != null) {
                    mActionListener.onItemChecked(bean);
                }
            }
        };
        for (int i = 0; i < pageCount; i++) {
            RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.view_gift_page, null, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 5, GridLayoutManager.VERTICAL, false));
            int endIndex = fromIndex + GIFT_COUNT;
            if (endIndex > size) {
                endIndex = size;
            }
            List<LiveGiftBean> list = new ArrayList<>();
            for (int j = fromIndex; j < endIndex; j++) {
                LiveGiftBean bean = giftList.get(j);
                bean.setPage(i);
                list.add(bean);
            }
            LiveGiftAdapter adapter = new LiveGiftAdapter(mContext, inflater, list, coinName, mGiftType);
            adapter.setActionListener(actionListener);
            recyclerView.setAdapter(adapter);
            mViewList.add(recyclerView);
            fromIndex = endIndex;
        }
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViewList.get(position);
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }

    public void release() {
        if (mViewList != null) {
            for (RecyclerView recyclerView : mViewList) {
                LiveGiftAdapter adapter = (LiveGiftAdapter) recyclerView.getAdapter();
                if (adapter != null) {
                    adapter.release();
                }
            }
        }
    }

    public interface ActionListener {
        void onItemChecked(LiveGiftBean bean);
    }


    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }


    public void reducePackageCount(int giftId, int count) {
        if (mGiftType != Constants.GIFT_TYPE_PACK) {
            return;
        }
        if (mViewList != null) {
            for (RecyclerView recyclerView : mViewList) {
                LiveGiftAdapter adapter = (LiveGiftAdapter) recyclerView.getAdapter();
                if (adapter != null && adapter.reducePackageCount(giftId, count)) {
                    return;
                }
            }
        }
    }
}
