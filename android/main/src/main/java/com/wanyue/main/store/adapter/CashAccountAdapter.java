package com.wanyue.main.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wanyue.common.Constants;
import com.wanyue.common.utils.CommonIconUtil;
import com.wanyue.main.R;
import com.wanyue.main.store.bean.CashAccountBean;

import java.util.ArrayList;
import java.util.List;

public class CashAccountAdapter extends RecyclerView.Adapter<CashAccountAdapter.Vh> {

    private List<CashAccountBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private View.OnClickListener mDeleteClickListener;
    private ActionListener mActionListener;
    private String mCashAccountId;

    public CashAccountAdapter(Context context, String cashAccountId) {
        mList = new ArrayList<>();
        mCashAccountId = cashAccountId;
        mInflater = LayoutInflater.from(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    int position = (int) tag;
                    mActionListener.onItemClick(mList.get(position), position);
                }
            }
        };
        mDeleteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    int position = (int) tag;
                    mActionListener.onItemDelete(mList.get(position), position);
                }
            }
        };
    }


    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_cash_account, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull Vh vh, int position, @NonNull List<Object> payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        vh.setData(mList.get(position), position, payload);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void removeItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mList.size(), Constants.PAYLOAD);
    }

    public void insertItem(CashAccountBean bean) {
        int position = mList.size();
        mList.add(bean);
        notifyItemInserted(position);
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public void setList(List<CashAccountBean> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    class Vh extends RecyclerView.ViewHolder {

        RadioButton mRadioButton;
        ImageView mIcon;
        TextView mAccount;
        View mBtnDelete;

        public Vh(View itemView) {
            super(itemView);
            mRadioButton = (RadioButton) itemView.findViewById(R.id.radioButton);
            mIcon = (ImageView) itemView.findViewById(R.id.icon);
            mAccount = itemView.findViewById(R.id.account);
            mBtnDelete = itemView.findViewById(R.id.btn_delete);
            itemView.setOnClickListener(mOnClickListener);
            mBtnDelete.setOnClickListener(mDeleteClickListener);
        }

        void setData(CashAccountBean bean, int position, Object payload) {
            itemView.setTag(position);
            mBtnDelete.setTag(position);
            if (payload == null) {
                mIcon.setImageResource(CommonIconUtil.getCashTypeIcon(bean.getType()));
                mAccount.setText(bean.getAccount());
            }
            mRadioButton.setChecked(bean.getId().equals(mCashAccountId));
        }
    }

    public interface ActionListener {
        void onItemClick(CashAccountBean bean, int position);

        void onItemDelete(CashAccountBean bean, int position);
    }

}
