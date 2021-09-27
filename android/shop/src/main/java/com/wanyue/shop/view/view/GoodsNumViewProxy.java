package com.wanyue.shop.view.view;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.utils.ResourceUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.R2;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/*商品数量+-相关*/
public class GoodsNumViewProxy extends BaseGoodItemViewProxy {
    protected int mMaxNum=-1;
    protected int currentNum;
    protected int mDefaultNum=1;
    protected int mMinNum=1;
    protected TextView mBtnGoodsReduce;
    protected EditText mTvGoodsNum;
    protected TextView mBtnGoodsAdd;
    protected NumberChangeListnter mNumberChangeListnter;

    private boolean mEableDelay;

    private MyHandler mHandler;
    @Override
    public int getLayoutId() {
      return R.layout.view_goods_num;
    }
    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        mBtnGoodsReduce = findViewById(R.id.btn_goods_reduce);
        mTvGoodsNum =  findViewById(R.id.tv_goods_num);
        mBtnGoodsAdd =  findViewById(R.id.btn_goods_add);
        currentNum=mDefaultNum;
        mTvGoodsNum.setText(Integer.toString(mDefaultNum));
        checkReduceButtonEable(currentNum);
        if(mEableDelay){
           mHandler = new MyHandler(this);
        }
    }
    public void setDefaultNum(int defaultNum) {
        mDefaultNum = defaultNum;
        if(mTvGoodsNum!=null){
          mTvGoodsNum.setText(Integer.toString(mDefaultNum));
        }
    }
    public void setMaxNum(int maxNum) {
        mMaxNum = maxNum;
    }
    public void setMinNum(int minNum) {
        mMinNum = minNum;
    }

    @Override
    protected boolean shouldBindButterKinfe() {
        return true;
    }
    public interface NumberChangeListnter<T extends GoodsNumViewProxy>{
        public void change(T goodsNumViewProxy,int num);
    }

    @OnClick(R2.id.btn_goods_add)
    public void goodsNumAdd(){
        int tempNum=currentNum+1;
        mTvGoodsNum.setText(Integer.toString(tempNum));
        mTvGoodsNum.clearFocus();
    }
    @OnClick(R2.id.btn_goods_reduce)
    public void goodsNumReduce(){
        int tempNum=currentNum-1;
        mTvGoodsNum.setText(Integer.toString(tempNum));
        mTvGoodsNum.clearFocus();
    }

    @OnTextChanged(value = R2.id.tv_goods_num, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void watchGoodsInputChange(CharSequence sequence, int start, int before, int count) {
        String string=sequence.toString();
        if(TextUtils.isEmpty(string)){
            refreashChange();
            return;
          // mTvGoodsNum.setText(Integer.toString(1));
        }
        if(!StringUtil.isInt(string)){
            mTvGoodsNum.setText(Integer.toString(currentNum));
            return;
        }
        int number=Integer.parseInt(string);
        if(mMaxNum>0&&number>mMaxNum){
           mTvGoodsNum.setText(Integer.toString(mMaxNum));
           return;
        }
        if(number<mMinNum){
          mTvGoodsNum.setText(Integer.toString(mMinNum));
        }
        if(number!=currentNum){
           currentNum=number;
           if(mEableDelay){
              refreashChange();
           }else{
              sendChange();
           }
            checkReduceButtonEable(number);
        }
        move();
    }

    private void refreashChange() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendEmptyMessageDelayed(0, 500);
        }
    }

    private void move() {
        if(mTvGoodsNum!=null&&mTvGoodsNum.isFocused()){
           mTvGoodsNum.setSelection(mTvGoodsNum.getText().length());
        }

    }

    private void sendChange() {
        if(mNumberChangeListnter!=null){
            mNumberChangeListnter.change(this,currentNum);
        }
    }


    private void checkReduceButtonEable(int number) {
        if(number<=mMinNum){
            setReduceButtonEable(false);
        }else{
            setReduceButtonEable(true);
        }
    }

    public void setNumberChangeListnter(NumberChangeListnter numberChangeListnter) {
        mNumberChangeListnter = numberChangeListnter;
    }


    /*是否过滤频率过快的请求*/
    public void setEableDelay(boolean eableDelay) {
        mEableDelay = eableDelay;
    }

    /*设置减少按钮是否允许点击*/
    private void setReduceButtonEable(boolean enable) {
        if(mBtnGoodsReduce==null||mBtnGoodsReduce.isEnabled()==enable){
            return;
        }
        mBtnGoodsReduce.setEnabled(enable);
        int color=0;
        Drawable drawable=null;
        if(enable){
            color= ResourceUtil.getColor(getActivity(),R.color.gray1);
            drawable=ResourceUtil.getDrawable(R.drawable.bg_button_reduce,true);
        }else{
            color= ResourceUtil.getColor(getActivity(),R.color.gray_dc);
            drawable=ResourceUtil.getDrawable(R.drawable.bg_button_reduce_enable_cancle,true);
        }
        mBtnGoodsReduce.setTextColor(color);
        mBtnGoodsReduce.setBackground(drawable);
    }

    private static class MyHandler extends Handler {
        private GoodsNumViewProxy mGoodsNumViewProxy;

        public MyHandler(GoodsNumViewProxy goodsNumViewProxy) {
            mGoodsNumViewProxy = goodsNumViewProxy;
        }

        @Override
        public void handleMessage(Message msg) {
            if (mGoodsNumViewProxy != null) {
                mGoodsNumViewProxy.sendChange();
            }
        }

        public void release() {
            mGoodsNumViewProxy = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNumberChangeListnter=null;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.release();
        }
        mHandler = null;
    }
}
