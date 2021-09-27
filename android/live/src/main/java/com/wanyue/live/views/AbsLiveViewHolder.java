package com.wanyue.live.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanyue.common.views.AbsViewHolder;
import com.wanyue.live.R;
import com.wanyue.live.activity.LiveActivity;

/**
 * Created by  on 2018/10/9.
 */

public abstract class AbsLiveViewHolder extends AbsViewHolder implements View.OnClickListener {

    private TextView mRedPoint;

    public AbsLiveViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public void init() {
        View btnChat=findViewById(R.id.btn_chat);
        //View btnMsg=findViewById(R.id.btn_msg);
        if(btnChat!=null){
           btnChat.setOnClickListener(this);
        }
        /*if(btnMsg!=null){
            btnMsg.setOnClickListener(this);
        }*/
        mRedPoint = (TextView) findViewById(R.id.red_point);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
       /* if (i == R.id.btn_msg) {
            ((LiveActivity) mContext).openChatListWindow();

        } else*/
       if (i == R.id.btn_chat) {
            ((LiveActivity) mContext).openChatWindow(null);
        }
    }

    public void setUnReadCount(String unReadCount) {
        if (mRedPoint != null) {
            if ("0".equals(unReadCount)) {
                if (mRedPoint.getVisibility() == View.VISIBLE) {
                    mRedPoint.setVisibility(View.INVISIBLE);
                }
            } else {
                if (mRedPoint.getVisibility() != View.VISIBLE) {
                    mRedPoint.setVisibility(View.VISIBLE);
                }
            }
            mRedPoint.setText(unReadCount);
        }
    }


}
