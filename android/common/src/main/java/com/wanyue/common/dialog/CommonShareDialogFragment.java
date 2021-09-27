package com.wanyue.common.dialog;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import com.wanyue.common.interfaces.OnItemClickListener;
import com.wanyue.common.mob.MobBean;
import com.wanyue.common.R;
import com.wanyue.common.adapter.CommonShareAdapter;

/**
 * Created by  on 2018/10/19.
 * 直播分享弹窗
 */

public class CommonShareDialogFragment extends AbsDialogFragment implements OnItemClickListener<MobBean> {

    private RecyclerView mRecyclerView;
    private ActionListener mActionListener;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_live_share;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog;
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
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        CommonShareAdapter adapter = initAdapter();
        adapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(adapter);
    }

    protected CommonShareAdapter initAdapter() {
        return new CommonShareAdapter(getActivity());
    }

    @Override
    public void onItemClick(MobBean bean, int position) {
        if(!canClick()){
            return;
        }
        dismiss();
        if(mActionListener!=null){
            mActionListener.onItemClick(bean.getType());
        }
    }

    public interface ActionListener{
        void onItemClick(String type);
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext=null;
        mActionListener=null;
    }
}
