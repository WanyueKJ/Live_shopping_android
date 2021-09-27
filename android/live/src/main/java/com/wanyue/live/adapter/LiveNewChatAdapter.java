package com.wanyue.live.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanyue.common.CommonApplication;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.utils.BitmapUtil;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.ResourceUtil;
import com.wanyue.live.R;
import com.wanyue.live.bean.LiveChatBean;

import java.util.List;

/**
 * 直播间聊天
 */
public class LiveNewChatAdapter extends BaseRecyclerAdapter<LiveChatBean, BaseReclyViewHolder> {
    private int mNormalColor;
    private int mFollowColor;

    public LiveNewChatAdapter(List<LiveChatBean> data) {
        super(data);
        mNormalColor = ResourceUtil.getColor(CommonApplication.sInstance, R.color.yellow_7a);
        mFollowColor = ResourceUtil.getColor(CommonApplication.sInstance, R.color.yellow_f5);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_recly_live_chat_new;
    }

    @Override
    protected void convert(BaseReclyViewHolder helper, LiveChatBean item) {
        helper.addOnClickListener(R.id.tv_user_name);
        ImageView ivId = helper.getView(R.id.iv_identity);
        TextView tvUserName = helper.getView(R.id.tv_user_name);
        TextView tvContent = helper.getView(R.id.tv_content);
        if (item.getType() == LiveChatBean.SYSTEM) {
            if (ivId != null && ivId.getVisibility() == View.VISIBLE) {
                ivId.setVisibility(View.GONE);
            }
            if (tvUserName != null && tvUserName.getVisibility() == View.VISIBLE) {
                tvUserName.setVisibility(View.GONE);
            }
            convertSystem(tvContent, item);
        } else {
            setIdImg(ivId, item);
            if (tvUserName.getVisibility() != View.VISIBLE) {
                tvUserName.setVisibility(View.VISIBLE);
            }
            switch (item.getType()) {
                case LiveChatBean.NORMAL:
                    convertNormal(tvUserName, tvContent, item);
                    break;
                /*case LiveChatBean.SYSTEM:
                    convertSystem(tvUserName, tvContent, item);
                    break;*/
                case LiveChatBean.ENTER_ROOM:
                    convertEnterRoom(tvUserName, tvContent, item);
                    break;
                case LiveChatBean.LIGHT:
                    convertLight(tvUserName, tvContent, item);
                    break;
                default:
                    break;
            }

        }
    }

    private void convertLight(TextView tvUserName, TextView tvContent, LiveChatBean item) {
        int baseColor = item.getIsFollow() == 1 ? mFollowColor : mNormalColor;
        tvContent.setTextColor(baseColor);
        tvUserName.setTextColor(baseColor);
        tvUserName.setText(item.getUserNiceName());
        tvContent.setText(getTypeSPanTag(item.getContent()));
    }

    private void convertEnterRoom(TextView tvUserName, TextView tvContent, LiveChatBean item) {
        int baseColor = item.getIsFollow() == 1 ? mFollowColor : mNormalColor;
        tvUserName.setText(item.getUserNiceName());
        tvContent.setTextColor(baseColor);
        tvUserName.setTextColor(baseColor);
        tvContent.setText(item.getContent());
    }


    private void convertNormal(TextView tvUserName, TextView tvContent, LiveChatBean item) {
        boolean isFollow = item.getIsFollow() == 1;
        int baseColor = isFollow ? mFollowColor : mNormalColor;
        tvUserName.setTextColor(baseColor);
        tvUserName.setText(String.format("%s:", item.getUserNiceName()));
        if (isFollow) {
            tvContent.setTextColor(baseColor);
        } else {
            tvContent.setTextColor(Color.WHITE);
        }
        tvContent.setText(item.getContent());
    }

    private void convertSystem( TextView tvContent, LiveChatBean item) {
        tvContent.setText(item.getContent());
        tvContent.setTextColor(mNormalColor);
    }

    private void setIdImg(ImageView ivId, LiveChatBean item) {
        if (item.isAnchor()) {
            if (ivId != null) {
                if (ivId.getVisibility() != View.VISIBLE) {
                    ivId.setVisibility(View.VISIBLE);
                }
                ivId.setImageResource(R.mipmap.icon_live_anchor);
            }
        } else if (item.isManager()) {
            if (ivId != null) {
                if (ivId.getVisibility() != View.VISIBLE) {
                    ivId.setVisibility(View.VISIBLE);
                }
                ivId.setImageResource(R.mipmap.icon_live_manage);
            }
        } else if (item.isSuperManager()) {
            if (ivId != null) {
                if (ivId.getVisibility() != View.VISIBLE) {
                    ivId.setVisibility(View.VISIBLE);
                }
                ivId.setImageResource(R.mipmap.icon_live_chaoguan);
            }
        }else {
            if (ivId != null) {
                if (ivId.getVisibility() == View.VISIBLE) {
                    ivId.setVisibility(View.GONE);
                }
            }
        }
    }

    protected CharSequence getTypeSPanTag(String content) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(content + "\t");
        SpannableString spannableString = new SpannableString("\t \t");
        int dpSize = DpUtil.dp2px(15);
        Drawable drawable = BitmapUtil.zoomDrawable(CommonApplication.sInstance.getResources(), R.mipmap.icon_like_image, dpSize);
        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        spannableString.setSpan(imageSpan, 1, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(spannableString);
        return ssb;
    }


}
