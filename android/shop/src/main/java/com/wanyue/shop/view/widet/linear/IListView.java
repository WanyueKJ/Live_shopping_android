package com.wanyue.shop.view.widet.linear;

import android.content.Context;
import android.view.ViewGroup;
import com.wanyue.shop.view.widet.ViewGroupLayoutBaseAdapter;

public interface IListView {
    Context getContext();
    ViewGroup getGroup();
    ViewGroupLayoutBaseAdapter getAdapter();
    void bindView();
}
