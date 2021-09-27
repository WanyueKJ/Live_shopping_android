package com.wanyue.common.custom;

import android.content.Context;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;

/**
 * Created by  on 2018/10/1.
 *
 * 问题：
 * AppBarLayout + RecycleView 滑动后，item 在一段时间内无法点击的问题
 * 1. 快速滑动 RecycleView 后，立即去点击 item 往往没有反应，第二次点击或者等待片刻后点击可以生效。
 * 2. 缓慢滑动后，立即点击 item，发现大多数情况下是有反应的。
 *
 * 现象：
 * 重写 AppBarLayout.Behavior 打印 log，发现在快速滑动到顶部和底部之后，
 * AppBarLayout 在一段时间内还处于 Fling 状态，那么我们想办法把这段无效的 Fling 干掉就好了。
 *
 * 结论：
 * 最后翻找 google 的时候发现这是 google 在修复上个版本嵌套滑动的时候引进来的新 bug。。。
 */

public class FixAppBarLayoutBehavior extends AppBarLayout.Behavior {

    public FixAppBarLayoutBehavior() {
        super();
    }

    public FixAppBarLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        stopNestedScrollIfNeeded(dyUnconsumed, child, target, type);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        stopNestedScrollIfNeeded(dy, child, target, type);
    }

    private void stopNestedScrollIfNeeded(int dy, AppBarLayout child, View target, int type) {
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            final int currOffset = getTopAndBottomOffset();
            if ((dy < 0 && currOffset == 0) || (dy > 0 && currOffset == -child.getTotalScrollRange())) {
                ViewCompat.stopNestedScroll(target, ViewCompat.TYPE_NON_TOUCH);
            }
        }
    }


}
