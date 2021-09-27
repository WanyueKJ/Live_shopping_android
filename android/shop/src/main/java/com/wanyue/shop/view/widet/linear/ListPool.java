package com.wanyue.shop.view.widet.linear;

import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;

public class ListPool {
    private ArrayMap<View, BaseReclyViewHolder>mHolderMap;
    public ListPool() {
       mHolderMap = new ArrayMap<>();
    }

    public void put(View view,BaseReclyViewHolder baseViewHolder){
        if(mHolderMap==null){
           mHolderMap = new ArrayMap<>();
        }
        removeToParent(view);
        mHolderMap.put(view,baseViewHolder);
    }

    public void removeToParent(View view){
        if(view!=null&&view.getParent()!=null){
          ViewGroup viewGroup= (ViewGroup) view.getParent();
          viewGroup.removeView(view);
        }
    }
    public BaseReclyViewHolder getCacheHolder() {
        if(mHolderMap==null||mHolderMap.size()<=0){
           return null;
        }
        BaseReclyViewHolder baseViewHolder=mHolderMap.get(0);
       return mHolderMap.remove(baseViewHolder);
    }

}
