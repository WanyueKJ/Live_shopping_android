package com.yunbao.im.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.custom.ItemDecoration;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.im.R;
import com.yunbao.im.adapter.ImChatChooseImageAdapter;
import com.yunbao.im.bean.ChatChooseImageBean;
import com.yunbao.im.utils.ImageUtil;

import java.io.File;
import java.util.List;

/**
 * Created by  on 2018/7/16.
 * 聊天时候选择图片
 */

public class ChatChooseImageActivity extends AbsActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private ImChatChooseImageAdapter mAdapter;
    private ImageUtil mImageUtil;
    private View mNoData;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_choose_img;
    }

    @Override
    protected void main() {
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 1, 1);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRecyclerView.addItemDecoration(decoration);
        mNoData = findViewById(R.id.no_data);
        mImageUtil = new ImageUtil();
        mImageUtil.getLocalImageList(new CommonCallback<List<ChatChooseImageBean>>() {
            @Override
            public void callback(List<ChatChooseImageBean> list) {
                if (list.size() == 0) {
                    if (mNoData.getVisibility() != View.VISIBLE) {
                        mNoData.setVisibility(View.VISIBLE);
                    }
                } else {
                    mAdapter = new ImChatChooseImageAdapter(mContext, list);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_cancel) {
            onBackPressed();

        } else if (i == R.id.btn_send) {
            sendImage();

        }
    }

    private void sendImage() {
        if (mAdapter != null) {
            File file = mAdapter.getSelectedFile();
            if (file != null && file.exists()) {
                Intent intent = new Intent();
                intent.putExtra(Constants.SELECT_IMAGE_PATH, file.getAbsolutePath());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                ToastUtil.show(WordUtil.getString(R.string.im_please_choose_image));
            }
        } else {
            ToastUtil.show(WordUtil.getString(R.string.im_no_image));
        }
    }


    @Override
    protected void onDestroy() {
        mImageUtil.release();
        super.onDestroy();
    }


}
