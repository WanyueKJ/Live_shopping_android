package com.wanyue.shop.view.widet;

import android.util.SparseArray;

import androidx.annotation.LayoutRes;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public abstract class ViewGroupLayoutMutiBaseAdapter<T extends MultiItemEntity> extends ViewGroupLayoutBaseAdapter<T> {
    private SparseArray<Integer> layouts;

    public ViewGroupLayoutMutiBaseAdapter( List<T> list) {
        super(list);
    }

    protected void addItemType(int type, @LayoutRes int layoutResId) {
        if (layouts == null) {
            layouts = new SparseArray<>();
        }
        layouts.put(type, layoutResId);
    }

    @Override
    protected int getLayoutId(T t) {
        int itemType=t.getItemType();
        return layouts.get(itemType);
    }
}