package com.wanyue.main.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.utils.DebugUtil;
import com.wanyue.common.utils.ViewUtil;
import com.wanyue.main.R;
import com.wanyue.main.bean.MainUserSectionBean;
import com.wanyue.main.bean.MenuBean;
import java.util.List;

public class MainUserAdapter extends BaseRecyclerAdapter<MainUserSectionBean, BaseReclyViewHolder> {
    private OnItemClickListener mMenuClickListner;

    public MainUserAdapter(List<MainUserSectionBean> data) {
        super(data);
    }
    @Override
    public int getLayoutId() {
        return R.layout.item_recly_main_user;
    }

    @Override
    protected void convert(@NonNull BaseReclyViewHolder helper, MainUserSectionBean item) {
        helper.setText(R.id.tv_tip,item.getTitle());
        String rightTitle=item.getRightTitle();
        TextView btn_check_deail=helper.getView(R.id.btn_check_deail);
        helper.setOnChildClickListner(R.id.btn_check_deail,mOnClickListener);
        if(!TextUtils.isEmpty(rightTitle)){
           ViewUtil.setVisibility(btn_check_deail, View.VISIBLE);
           helper.setText(R.id.btn_check_deail,item.getRightTitle());
        }else{
            ViewUtil.setVisibility(btn_check_deail, View.INVISIBLE);
        }
        List<MenuBean>list=item.getMenuBeanList();
        RecyclerView recyclerView=helper.getView(R.id.reclyView);
        if(recyclerView==null){
            DebugUtil.sendException("RecyclerView==null");
            return;
        }
        if(recyclerView.getAdapter()==null){
           initRecyclerView(recyclerView,list);
        }else{
            MenuUserAdapter adapter= (MenuUserAdapter) recyclerView.getAdapter();
            adapter.setData(list);
        }
    }

    public void setMenuClickListner(OnItemClickListener menuClickListner) {
        mMenuClickListner = menuClickListner;
    }

    private void initRecyclerView(RecyclerView recyclerView, List<MenuBean>list) {
        GridLayoutManager gridLayoutManager=new GridLayoutManager(recyclerView.getContext(),4){
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        MenuUserAdapter menuUserAdapter=new MenuUserAdapter(list);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(menuUserAdapter);
        menuUserAdapter.setOnItemClickListener(mMenuClickListner);
    }

}
