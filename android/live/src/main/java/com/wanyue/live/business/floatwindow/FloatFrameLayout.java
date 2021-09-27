package com.wanyue.live.business.floatwindow;

import android.content.Context;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.ViewUtil;
import com.wanyue.live.R;

public class FloatFrameLayout extends FrameLayout implements View.OnClickListener {
    private float mTouchStartX;
    private float mTouchStartY;
    private float mLastX;
    private float mLastY;
    private float mMaxX = 0;
    private float mMaxY = 0;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWmParams;

    private OnNoTouchClickListner onNoTouchClickListner;
    private float startRawX;
    private float startRawY;

    private boolean mLock;

    public FloatFrameLayout(Context context) {
        super(context);
        init(context);
    }

    public FloatFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FloatFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setWmParams(WindowManager.LayoutParams params) {
        mWmParams = params;
    }

    public void upDateWmParams(WindowManager.LayoutParams params) {
        mWmParams = params;
        mWindowManager.updateViewLayout(this, mWmParams);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取相对屏幕的坐标，即以屏幕左上角为原点
        mLastX = event.getRawX();
        mLastY = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取相对View的坐标，即以此View左上角为原点
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();

                startRawX = event.getRawX();
                startRawY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:

                float offectX = (int) Math.abs(startRawX - mLastX);
                float offectY = (int) Math.abs(startRawY - mLastY);

                Log.e("TAG", "offectX==" + offectX);
                Log.e("TAG", "offectY==" + offectY);

                if (offectX < 20 && offectY < 20 && onNoTouchClickListner != null) {
                    if(mLock){
                      return false;
                    }
                    onNoTouchClickListner.click(this);
                    return false;
                }
                mTouchStartX = 0;
                mTouchStartY = 0;
                break;
        }
        return true;
    }


    private void init(Context context) {
        addCloseView(context);
        setClipToOutline(true);
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int radius= DpUtil.dp2px(5);
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
            }
        });
        mWindowManager = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
    }

    private void addCloseView(Context context) {
        int size= DpUtil.dp2px(25);
        int margin=DpUtil.dp2px(0);
        int padding=DpUtil.dp2px(6);
        ImageView btnClose=new ImageView(context);
        btnClose.setImageResource(R.mipmap.icon_close_x);
        btnClose.setPadding(padding,padding,padding,padding);
        btnClose.setOnClickListener(this);
        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(size,size);
        params.gravity=Gravity.RIGHT;
        params.setMargins(margin,margin,margin,margin);
        addView(btnClose,params);

    }

    private void updateViewPosition() {
        if (mWmParams != null) {
            mMaxX = mWindowManager.getDefaultDisplay().getWidth() - this.getWidth();
            mMaxY = mWindowManager.getDefaultDisplay().getHeight() - this.getHeight();
            //更新浮动窗口位置参数
            float newX = (mLastX - mTouchStartX);
            float newY = (mLastY - mTouchStartY);
            //以屏幕左上角为原点
            mWmParams.gravity = Gravity.LEFT | Gravity.TOP;
            //不能移除屏幕最左边和最上边
            if (newX < 0) {
                newX = 0;
            }
            if (newY < 0) {
                newY = 0;
            }
            //不能移出屏幕最右边和最下边
            if (newX > mMaxX) {
                newX = mMaxX;
            }
            if (newY > mMaxY) {
                newY = mMaxY;
            }
            mWmParams.x = (int) newX;
            mWmParams.y = (int) newY;
            mWindowManager.updateViewLayout(this, mWmParams);  //刷新显示
        }
    }


    private float tempX;
    private float tempY;

    public void center(int size) {
        tempX = mWmParams.x;
        tempY = mWmParams.y;

        mWmParams.x = (int) 0;
        mWmParams.y = (int) 0;
        mWmParams.width = size;
        mWmParams.height = size;

        mLastX = 0;
        mLastY = 0;
        mWmParams.gravity = Gravity.CENTER;
        mWindowManager.updateViewLayout(this, mWmParams);
    }


    public void center() {
        tempX = mWmParams.x;
        tempY = mWmParams.y;

        mWmParams.x = (int) 0;
        mWmParams.y = (int) 0;
        mWmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWmParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        mLastX = 0;
        mLastY = 0;
        mWmParams.gravity = Gravity.CENTER;
        mWindowManager.updateViewLayout(this, mWmParams);

    }


    public void left() {
        mWmParams.x = (int) tempX;
        mWmParams.y = (int) tempY;
        mLastX = 0;
        mLastY = 0;

        mWmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;


        mWmParams.gravity = Gravity.LEFT | Gravity.TOP;
        mWindowManager.updateViewLayout(this, mWmParams);
    }

    public void setOnNoTouchClickListner(OnNoTouchClickListner onNoTouchClickListner) {
        this.onNoTouchClickListner = onNoTouchClickListner;

    }

    public boolean isLock() {
        return mLock;
    }

    public void setLock(boolean lock) {
        mLock = lock;
    }

    public void performTouchClick(){
        if( this.onNoTouchClickListner!=null){
            this.onNoTouchClickListner.click(this);
        }
    }

    public void setView(View view) {
        ViewUtil.removeToParent(view);
        addView(view);
    }

    public void setView(View view,int position) {
        ViewUtil.removeToParent(view);
        addView(view,position);
    }

    @Override
    public void onClick(View v) {
        if(onNoTouchClickListner!=null&&!mLock){
           onNoTouchClickListner.close(v);
        }
    }



    public interface OnNoTouchClickListner {
        public void click(View view);
        public void close(View view);
    }

}
