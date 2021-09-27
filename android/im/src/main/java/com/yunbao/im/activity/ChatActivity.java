package com.yunbao.im.activity;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;

import com.wanyue.common.activity.AbsActivity;
import com.yunbao.im.R;
import com.yunbao.im.bean.ImUserBean;
import com.yunbao.im.views.ChatListViewHolder;

/**
 * Created by  on 2018/10/24.
 */

public class ChatActivity extends AbsActivity {

    private ChatListViewHolder mChatListViewHolder;

    public static void forward(Context context) {
        context.startActivity(new Intent(context, ChatActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_list;
    }

    @Override
    protected void main() {
        mChatListViewHolder = new ChatListViewHolder(mContext, (ViewGroup) findViewById(R.id.root), ChatListViewHolder.TYPE_ACTIVITY);
        mChatListViewHolder.setActionListener(new ChatListViewHolder.ActionListener() {
            @Override
            public void onCloseClick() {
                onBackPressed();
            }

            @Override
            public void onItemClick(ImUserBean bean) {
                ChatRoomActivity.forward(mContext, bean, true, false, true, false);
            }
        });
        mChatListViewHolder.addToParent();
        mChatListViewHolder.loadData();

    }

    @Override
    protected void onDestroy() {
        if (mChatListViewHolder != null) {
            mChatListViewHolder.release();
        }
        super.onDestroy();
    }
}
