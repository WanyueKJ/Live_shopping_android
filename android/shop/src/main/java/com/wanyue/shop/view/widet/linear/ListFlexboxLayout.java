package com.wanyue.shop.view.widet.linear;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.utils.DebugUtil;
import com.wanyue.shop.view.widet.ViewGroupLayoutBaseAdapter;

public class ListFlexboxLayout extends FlexboxLayout implements IListView  {
    private ViewGroupLayoutBaseAdapter adapter;
    private int itemPadding;
    private ListPool mListPool;
    private int maxLine=4;


    public ListFlexboxLayout(Context context) {
        super(context);
        setFlexWrap(FlexWrap.WRAP);
    }
    public ListFlexboxLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFlexWrap(FlexWrap.WRAP);
    }

    public ListFlexboxLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFlexWrap(FlexWrap.WRAP);
    }

    public void setAdapter(ViewGroupLayoutBaseAdapter adapter) {
        this.adapter = adapter;
        if(adapter!= null){
            adapter.attach(this);
        }
        // setAdapter 时添加 view
        bindView();
    }

    @Override
    public ViewGroup getGroup() {
        return this;
    }

    @Override
    public ViewGroupLayoutBaseAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void bindView() {
        if (adapter == null) {
            return;
        }
        if(mListPool==null){
            noPoolSet();
        }else{
            havePoolSet();
        }
    }

    private void havePoolSet() {
        int adapterCount=adapter.getCount();
        int viewChildCount=getChildCount();
        int offectSize=adapterCount-viewChildCount;
        if(adapterCount==0&&viewChildCount==0){
            return;
        }
        if(viewChildCount>adapterCount){
            int absSize= Math.abs(offectSize);
            for(int index=0;index<absSize;index++){
                View view= getChildAt(viewChildCount-(index+1));
                mListPool.removeToParent(view);
                BaseReclyViewHolder baseReclyViewHolder=adapter.getAndRemoveViewHolder(view);
                if(baseReclyViewHolder==null){
                    DebugUtil.sendException("BaseReclyViewHolder not Null");
                    mListPool.removeToParent(view);
                }else{
                    mListPool.put(view,baseReclyViewHolder);
                }
            }

        } else if(adapterCount>viewChildCount){
            for(int i=0;i<offectSize;i++){
                BaseReclyViewHolder baseReclyViewHolder=mListPool.getCacheHolder();
                if(baseReclyViewHolder==null||baseReclyViewHolder.convertView==null){
                    View v = adapter.getView(i);
                    addViewSetPadding(v);
                }else{
                    View view=baseReclyViewHolder.convertView;
                    addView(view);
                    adapter.putViewHolder(view,baseReclyViewHolder);
                }
            }
        }
        for(int i = 0; i < adapterCount; i++){
            View v = getChildAt(i);
            adapter.bindView(v,i,adapter.getItem(i));
        }
    }

    private void noPoolSet() {
        int adapterCount=adapter.getCount();
        removeAllViews();
        for (int i = 0; i < adapterCount; i++) {
            final View v = adapter.getView(i);
            addViewSetPadding(v);
            adapter.bindView(v,i,adapter.getItem(i));
        }
    }

    public void setListPool(ListPool listPool) {
        mListPool = listPool;
    }

    private void addViewSetPadding(View v) {
        addView(v);
    }
}
