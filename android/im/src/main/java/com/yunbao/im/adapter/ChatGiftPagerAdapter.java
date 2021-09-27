package com.yunbao.im.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.bean.ChatGiftBean;
import com.yunbao.im.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2018/7/11.
 */

public class ChatGiftPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<RecyclerView> mViewList;
    private static final int GIFT_COUNT = 10;//每页10个礼物
    private int mPage = -1;
    private ActionListener mActionListener;

    public ChatGiftPagerAdapter(Context context, List<ChatGiftBean> giftList) {
        mContext = context;
        mViewList = new ArrayList<>();
        int fromIndex = 0;
        int size = giftList.size();
        int pageCount = size / GIFT_COUNT;
        if (size % GIFT_COUNT > 0) {
            pageCount++;
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        String coinName = CommonAppConfig.getInstance().getCoinName();
        ChatGiftAdapter.ActionListener actionListener = new ChatGiftAdapter.ActionListener() {
            @Override
            public void onCancel() {
                if (mPage >= 0 && mPage < mViewList.size()) {
                    ChatGiftAdapter adapter = (ChatGiftAdapter) mViewList.get(mPage).getAdapter();
                    if (adapter != null) {
                        adapter.cancelChecked();
                    }
                }
            }

            @Override
            public void onItemChecked(ChatGiftBean bean) {
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
            List<ChatGiftBean> list = new ArrayList<>();
            for (int j = fromIndex; j < endIndex; j++) {
                ChatGiftBean bean = giftList.get(j);
                bean.setPage(i);
                list.add(bean);
            }
            ChatGiftAdapter adapter = new ChatGiftAdapter(mContext, inflater, list, coinName);
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
                ChatGiftAdapter adapter = (ChatGiftAdapter) recyclerView.getAdapter();
                if (adapter != null) {
                    adapter.release();
                }
            }
        }
    }

    public interface ActionListener {
        void onItemChecked(ChatGiftBean bean);
    }


    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }
}
