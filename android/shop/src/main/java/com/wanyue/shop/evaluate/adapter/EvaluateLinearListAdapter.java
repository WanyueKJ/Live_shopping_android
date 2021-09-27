package com.wanyue.shop.evaluate.adapter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.utils.BitmapUtil;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.ViewUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.evaluate.bean.EvaluateBean2;
import com.wanyue.shop.view.widet.RatingStar;
import com.wanyue.shop.view.widet.ViewGroupLayoutBaseAdapter;
import com.wanyue.shop.view.widet.linear.ListFlexboxLayout;
import com.wanyue.shop.view.widet.linear.ListPool;
import java.util.List;

public class EvaluateLinearListAdapter extends ViewGroupLayoutBaseAdapter<EvaluateBean2> {
    private ListPool mListPool;
    private Bitmap starNormal;
    private Bitmap starFocus;

    private ViewGroupLayoutBaseAdapter.OnItemClickListener<String> mStringOnItemClickListener;

    public EvaluateLinearListAdapter(List<EvaluateBean2> list, Resources resources) {
        super(list);
        int size = DpUtil.dp2px(9);
        starNormal = BitmapUtil.thumbImageWithMatrix(resources, R.drawable.icon_evaluate_default, size, size);
        starFocus = BitmapUtil.thumbImageWithMatrix(resources, R.drawable.icon_evaluate_select, size, size);
    }

    @Override
    public void convert(@NonNull BaseReclyViewHolder helper, EvaluateBean2 item) {
        helper.setImageUrl(item.getAvatar(),R.id.img_avator);
        helper.setText(R.id.tv_user_name,item.getNickname())
                .setText(R.id.tv_time,item.getTimeAndSuk())
                .setText(R.id.tv_content,item.getComment());
        List<String>list=item.getPics();

        String replyContent=item.getMerchant_reply_content();
        View imgArrowTop=helper.getView(R.id.img_arrow_top);
        TextView tvReply=helper.getView(R.id.tv_reply);
        RatingStar star=helper.getView(R.id.star);
        star.setPosition(item.getStar()-1);
        star.setNormalImg(starNormal);
        star.setFocusImg(starFocus);

        ListFlexboxLayout listView=helper.getView(R.id.list_item);
        if(mListPool==null){
            mListPool=new ListPool();
        }
        listView.setListPool(mListPool);
        if(listView.getAdapter()==null){
            EvaluateListChildAdapter adapter=new EvaluateListChildAdapter(list);
            adapter.setOnItemClickListener(mStringOnItemClickListener);
            listView.setAdapter(adapter);
        }else{
            EvaluateListChildAdapter adapter= (EvaluateListChildAdapter) listView.getAdapter();
            adapter.setData(list);
        }

        if(TextUtils.isEmpty(replyContent)){
            ViewUtil.setVisibility(imgArrowTop,View.GONE);
            ViewUtil.setVisibility(tvReply,View.GONE);
        }else{
            ViewUtil.setVisibility(imgArrowTop,View.VISIBLE);
            ViewUtil.setVisibility(tvReply,View.VISIBLE);
            tvReply.setText(item.getReplyContent2());
        }
    }

    public void setStringOnItemClickListener(ViewGroupLayoutBaseAdapter.OnItemClickListener<String> stringOnItemClickListener) {
        mStringOnItemClickListener = stringOnItemClickListener;
    }

    @Override
    protected int getLayoutId(EvaluateBean2 evaluateBean) {
        return R.layout.item_recly_evaluate_list;
    }



}
