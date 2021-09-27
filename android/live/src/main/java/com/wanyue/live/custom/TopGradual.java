package com.wanyue.live.custom;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Xfermode;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by  on 2018/1/27.
 * RecyclerView顶部渐变的itemDecoration
 */

public class TopGradual extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private Xfermode mXfermode;
    private LinearGradient mLinearGradient;
    private int mLayerId;

    public TopGradual() {
        mPaint = new Paint();
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        mLinearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, 50.0f, new int[]{0, Color.BLACK}, null, Shader.TileMode.CLAMP);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);
        mPaint.setXfermode(mXfermode);
        mPaint.setShader(mLinearGradient);
        canvas.drawRect(0.0f, 0.0f, parent.getRight(), 100.0f, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(mLayerId);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        mLayerId = c.saveLayer(0.0f, 0.0f, (float) parent.getWidth(), (float) parent.getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);
    }
}
