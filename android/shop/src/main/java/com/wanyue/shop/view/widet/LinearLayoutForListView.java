package com.wanyue.shop.view.widet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wanyue.shop.view.widet.linear.IListView;

public class LinearLayoutForListView extends LinearLayout implements IListView {
    private ViewGroupLayoutBaseAdapter adapter;
    private int itemPadding;
    private boolean isSingleItemtType;

    public LinearLayoutForListView(Context context) {
        super(context);
    }

    public LinearLayoutForListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearLayoutForListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(ViewGroupLayoutBaseAdapter adapter) {
        this.adapter = adapter;
        if(adapter!= null){
           adapter.attach(this);
        }
        isSingleItemtType=adapter.isSingleItemtype();
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

    /**
     * 绑定 adapter 中所有的 view
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void bindView() {
        if (adapter == null) {
            return;
        }
        int adapterCount=adapter.getCount();
        removeAllViews();
        for (int i = 0; i < adapterCount; i++) {
            final View v = adapter.getView(i);
            addView(v);
            adapter.bindView(v,i,adapter.getItem(i));
            LinearLayout.LayoutParams layoutParams= (LayoutParams) v.getLayoutParams();
            int orientation=getOrientation();
            if(orientation==VERTICAL){
               layoutParams.bottomMargin=layoutParams.bottomMargin+itemPadding;
            }else if(orientation==HORIZONTAL){
               layoutParams.rightMargin=layoutParams.rightMargin+itemPadding;
            }
        }
    }

    public void setItemPadding(int itemPadding) {
        this.itemPadding = itemPadding;
    }

}