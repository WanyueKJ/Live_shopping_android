package com.wanyue.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.recyclerview.widget.RecyclerView;

import com.wanyue.common.interfaces.OnItemClickListener;
import com.wanyue.common.utils.ClickUtil;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2018/6/7.
 */

public abstract class RefreshAdapter<T> extends RecyclerView.Adapter {

    protected Context mContext;
    protected List<T> mList;
    protected LayoutInflater mInflater;
    protected RecyclerView mRecyclerView;
    protected OnItemClickListener<T> mOnItemClickListener;

    public RefreshAdapter(Context context) {
        this(context, new ArrayList<T>());
    }

    public RefreshAdapter(Context context, List<T> list) {
        mList = list;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        setHasStableIds(true);
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onAttachedToRecyclerView( RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setList(List<T> list) {
        if (mList != null) {
            mList.clear();
            mList.addAll(list);
        }
    }

    public void refreshData(List<T> list) {
        if (mRecyclerView != null && list != null) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void insertList(List<T> list) {
        if (mRecyclerView != null && mList != null && list != null && list.size() > 0) {
            int p = mList.size();
            mList.addAll(list);
            notifyItemRangeInserted(p, list.size());
        }
    }

    public void clearData() {
        if (mRecyclerView != null && mList != null) {
            mList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected boolean canClick() {
        return ClickUtil.canClick();
    }

    public List<T> getList() {
        return mList;
    }
}
