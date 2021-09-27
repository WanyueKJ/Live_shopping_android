package com.wanyue.shop.view.widet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import androidx.annotation.IdRes;
import com.google.android.flexbox.FlexboxLayout;

public class FlexRadioGroup extends FlexboxLayout {
    private int mCheckedId = -1;
    private boolean mProtectFromCheckedChange = false;
    private OnCheckedChangeListener mOnCheckedChangeListener; //RadioButton 更改状态时的监听
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;


    public FlexRadioGroup(Context context) {
        super(context);
        init();
    }

    public FlexRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }


    @Override
    public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof RadioButton) {
            final RadioButton button = (RadioButton) child;
            if (button.isChecked()) {
                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedId(button.getId());
            }
        }

        super.addView(child, index, params);
    }

    /**
     * 设置Checked状态到View中
     *
     * @param viewId
     * @param checked
     */
    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof RadioButton) {
            ((RadioButton) checkedView).setChecked(checked);
        }
    }

    private void setCheckedId(@IdRes int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(mCheckedId);
        }
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    public void check(@IdRes int id) {
        // don't even bother
        if (id != -1 && (id == mCheckedId)) {
            return;
        }

        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false);
        }

        if (id != -1) {
            setCheckedStateForView(id, true);
        }

        setCheckedId(id);
    }

    public void clearCheck() {
        check(-1);
    }

    public interface OnCheckedChangeListener {
        public void onCheckedChanged(@IdRes int checkedId);
    }

    @IdRes
    public int getCheckedRadioButtonId() {
        return mCheckedId;
    }


    /**
     * 此监听器在{@link PassThroughHierarchyChangeListener#onChildViewAdded}中设置了监听
     * 当RadioButton被点击时调用{@link CheckedStateTracker#onCheckedChanged}方法
     */
    private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }

            mProtectFromCheckedChange = true;
            if (mCheckedId != -1) {//把原来选中的Check设置为false
                setCheckedStateForView(mCheckedId, false);
            }
            mProtectFromCheckedChange = false;

            int id = buttonView.getId();
            setCheckedId(id);
        }
    }

    /**
     * 当布局添加或者删除View时的监听器
     */
    private class PassThroughHierarchyChangeListener implements
            ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        /**
         * 子View添加时，调用此方法
         */
        @Override
        public void onChildViewAdded(View parent, View child) {
            if (parent == FlexRadioGroup.this && child instanceof RadioButton) { //如果子View没有Id，则生产一个id给view
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    id = ViewIdGenerator.generateViewId();
                    child.setId(id);
                }
                //
                /**
                 *原RadioGroup中调用的是{@link RadioButton#setOnCheckedChangeWidgetListener}
                 * 但由于被隐藏，无法调用，反射也无法找到，故此使用{@link  RadioButton#setOnCheckedChangeListener }
                 * 造成的后果就是子view中不能再去使用{@link  RadioButton#setOnCheckedChangeListener }监听器
                 */

                ((RadioButton) child).setOnCheckedChangeListener(
                        mChildOnCheckedChangeListener);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onChildViewRemoved(View parent, View child) {
            if (parent == FlexRadioGroup.this && child instanceof RadioButton) {
                ((RadioButton) child).setOnCheckedChangeListener(null);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }


}