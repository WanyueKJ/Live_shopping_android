package com.wanyue.live.dialog;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import com.wanyue.common.Constants;
import com.wanyue.common.dialog.AbsDialogFragment;
import com.wanyue.common.interfaces.OnItemClickListener;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.live.R;
import com.wanyue.live.activity.LiveAnchorActivity;
import com.wanyue.live.adapter.LiveFunctionAdapter;
import com.wanyue.live.interfaces.LiveFunctionClickListener;

/**
 * Created by  on 2018/10/9.
 */

public class LiveFunctionDialogFragment extends AbsDialogFragment implements OnItemClickListener<Integer> {

    private LiveFunctionClickListener mFunctionClickListener;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_live_function;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog2;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        params.y = DpUtil.dp2px(50);
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        boolean hasGame = false;
        boolean openFlash = false;
        Bundle bundle = getArguments();
        if (bundle != null) {
            openFlash = bundle.getBoolean(Constants.OPEN_FLASH, false);
        }
        RecyclerView recyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        LiveFunctionAdapter adapter = new LiveFunctionAdapter(mContext, hasGame, openFlash);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    public void setFunctionClickListener(LiveFunctionClickListener functionClickListener) {
        mFunctionClickListener = functionClickListener;
    }

    @Override
    public void onItemClick(Integer bean, int position) {
        dismiss();
        if (mFunctionClickListener != null) {
            mFunctionClickListener.onClick(bean);
        }
    }

    @Override
    public void onDestroy() {
        mFunctionClickListener = null;
        ((LiveAnchorActivity) mContext).setBtnFunctionDark();
        super.onDestroy();
    }
}
