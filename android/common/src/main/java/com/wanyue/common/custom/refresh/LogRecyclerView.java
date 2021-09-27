package com.wanyue.common.custom.refresh;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import com.wanyue.common.utils.L;

public class LogRecyclerView extends RecyclerView {
    public LogRecyclerView(@NonNull Context context) {
        super(context);
    }

    public LogRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LogRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        L.e("onMesure===LogRecyclerView");
        super.onMeasure(widthSpec, heightSpec);
    }
}
