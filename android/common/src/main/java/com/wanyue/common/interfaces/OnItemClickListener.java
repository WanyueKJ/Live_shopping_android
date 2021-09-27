package com.wanyue.common.interfaces;

/**
 * Created by  on 2017/8/9.
 * RecyclerView的Adapter点击事件
 */

public interface OnItemClickListener<T> {
    void onItemClick(T bean, int position);
}
