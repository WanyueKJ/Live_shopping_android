package com.wanyue.common.dialog;

import android.graphics.Color;
import androidx.annotation.Nullable;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wanyue.common.R;
import com.wanyue.common.utils.DpUtil;

public class BottomDealFragment extends AbsDialogFragment implements View.OnClickListener {
    protected ViewGroup btnContainer;
    protected DialogButton []dialogButtonArray;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_bottom_deal;
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
    public void init() {
        btnContainer=findViewById(R.id.ll_btn_container);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        if(dialogButtonArray!=null){
           createLoop();
        }
    }

    private void createLoop() {
       int height= DpUtil.dp2px(50);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                height
                );

        LinearLayout.LayoutParams lineParam=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        );
        int length=dialogButtonArray.length;
        for(int i=0;i<length;i++){
            DialogButton dialogButton=dialogButtonArray[i];
            if(i!=0&&length>1){
              createLine(lineParam);
            }
             createButton(params,dialogButton);
        }
    }

    private void createLine(LinearLayout.LayoutParams lineParam) {
        View view=new View(mContext);
        view.setLayoutParams(lineParam);
        view.setBackgroundColor(Color.parseColor("#EEEEEE"));
        btnContainer.addView(view);
    }

    private void createButton(LinearLayout.LayoutParams params, final DialogButton dialogButton) {
        TextView textView=new TextView(new ContextThemeWrapper(getActivity(),R.style.text_bottom_style));
        textView.setLayoutParams(params);
        textView.setText(dialogButton.getTitle());
        if(dialogButton.textColor!=-1){
            textView.setTextColor(dialogButton.textColor);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                dialogButton.clickListnter.click(v);

            }
        });
        btnContainer.addView(textView);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.btn_cancel){
           dismiss();
        }
    }

    public interface ClickListnter{
        public void click(View view);
    }

    public void setDialogButtonArray(DialogButton... dialogButtonArray) {
        this.dialogButtonArray = dialogButtonArray;
    }

    public static class DialogButton{
        private String title;
        private ClickListnter clickListnter;
        private int textColor=-1;
        public DialogButton(@Nullable String title, @Nullable ClickListnter clickListnter) {
            this.title = title;
            this.clickListnter = clickListnter;
        }
        public DialogButton(@Nullable String title, @Nullable ClickListnter clickListnter,int textColor) {
            this.title = title;
            this.clickListnter = clickListnter;
            this.textColor=textColor;
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public ClickListnter getClickListnter() {
            return clickListnter;
        }
        public void setClickListnter(ClickListnter clickListnter) {
            this.clickListnter = clickListnter;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext=null;
        ViewGroup viewGroup= (ViewGroup)mRootView;
        if(viewGroup!=null){
            viewGroup.removeAllViews();
            mRootView=null;
        }
        if(btnContainer!=null){
           btnContainer.removeAllViews();
        }
        btnContainer=null;
        dialogButtonArray=null;
    }
}
