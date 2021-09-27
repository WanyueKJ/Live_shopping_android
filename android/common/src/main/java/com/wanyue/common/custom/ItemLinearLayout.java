package com.wanyue.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wanyue.common.R;
import com.wanyue.common.glide.ImgLoader;

public class ItemLinearLayout extends LinearLayout {

    ImageView imgContent;
    private String imgContentUrl;
    ImageView imgArrow;
    private Drawable leftIcon;

    ImageView imgThumb;
    TextView tvName;
    TextView tvSelectContent;

    private String leftTitle;
    private String rightHint;
    private String rightContent;
    private int rightContentGravity;
    private boolean isShowArrow;
    private boolean isEdit;

    public ItemLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public ItemLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ItemLinearLayout);
        leftTitle = ta.getString(R.styleable.ItemLinearLayout_leftTitle);
        rightHint = ta.getString(R.styleable.ItemLinearLayout_rightHint);
        rightContent = ta.getString(R.styleable.ItemLinearLayout_rightHint);
        leftIcon = ta.getDrawable(R.styleable.ItemLinearLayout_left_Icon);
        rightContentGravity = parseGravity(ta.getString(R.styleable.ItemLinearLayout_rightContentGravity));
        isShowArrow = ta.getBoolean(R.styleable.ItemLinearLayout_showArrow, true);
        imgContentUrl = ta.getString(R.styleable.ItemLinearLayout_imgContentUrl);
        isEdit= ta.getBoolean(R.styleable.ItemLinearLayout_isEdit, false);
        String boldStyle=ta.getString(R.styleable.ItemLinearLayout_textStyle);
        init(context);
        setTextStyle(boldStyle);
        ta.recycle();
    }


    private void setTextStyle(String boldStyle) {
        if(TextUtils.isEmpty(boldStyle)){
            return;
        }
        if(boldStyle.equals("bold")){
            tvSelectContent.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        }


    }

    public void setRightContent(String rightContent) {
        this.rightContent = rightContent;
        tvSelectContent.setText(rightContent);

    }

    public void setImgContentUrl(String imgContentUrl) {
        this.imgContentUrl = imgContentUrl;
        if(!TextUtils.isEmpty(this.imgContentUrl)){
            ImgLoader.display(getContext(),this.imgContentUrl,imgContent);
        }
        imgContent.setVisibility(View.VISIBLE);
    }

    private int parseGravity(String string) {
        if (string == null) {
            return 0;
        }
        if (string.equals("right")) {
            return Gravity.RIGHT;
        } else if (string.equals("left")) {
            return Gravity.LEFT;
        }
        return 0;
    }



    public String getRightContent() {
        return tvSelectContent.getText().toString();
    }


    public void setLeftIcon(int iconId){
        imgThumb.setImageResource(iconId);
    }

    public  void setLeftTitle(String content){
            this.leftTitle=content;
            tvName.setText(leftTitle);
    }


    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widet_item, this, true);
        imgContent=findViewById(R.id.img_content);
        imgArrow=findViewById(R.id.img_arrow);
        imgThumb=findViewById(R.id.img_thumb);
        tvName=findViewById(R.id.tv_name);
        tvSelectContent=findViewById(R.id.tv_select_content);


        tvName.setText(leftTitle);
        tvSelectContent.setHint(rightHint);
        if (leftIcon != null) {
            imgThumb.setVisibility(VISIBLE);
        }
        imgThumb.setImageDrawable(leftIcon);
        if (rightContentGravity != 0) {
            tvSelectContent.setGravity(rightContentGravity);
        }
        if (!isShowArrow) {
            imgArrow.setVisibility(GONE);
        }
        tvSelectContent.setText(rightContent);
        if(!TextUtils.isEmpty(imgContentUrl)){
            ImgLoader.display(getContext(),imgContentUrl,imgContent);
            imgContent.setVisibility(View.VISIBLE);
        }
        if(isEdit){
            tvSelectContent.setFocusableInTouchMode(true);
            tvSelectContent.setFocusable(true);
            tvSelectContent.requestFocus();
        }else{
            //tvSelectContent.setFocusable(false);
            tvSelectContent.setFocusableInTouchMode(false);
            tvSelectContent.setEnabled(false);
        }
    }
    public ItemLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setItem(Item item) {
        imgThumb.setImageResource(item.getIconLeft());
        tvName.setText(item.getTitle());
        tvSelectContent.setText(item.getContent());
        if (item.isShouldShowArrow()) {
            imgArrow.setVisibility(VISIBLE);
        } else {
            imgArrow.setVisibility(INVISIBLE);
        }

        Integer gravity = item.getGravity();
        if (gravity != null) {
            tvSelectContent.setGravity(gravity);
        }
    }

    public void setContent(String content) {
        tvSelectContent.setText(content);
    }

    public void setContentAndNoDataIsGone(String content) {
        if(TextUtils.isEmpty(content)){
            setVisibility(View.GONE);
        }else{
           tvSelectContent.setText(content);
        }
    }


    public void setContentTextColor(int resColor) {
        //int color= getResources().getColor(resColor);
        tvSelectContent.setTextColor(resColor);
    }

    public static class Item {
        private int iconLeft;
        private String title;
        private String content;
        private boolean shouldShowArrow;
        private Integer gravity;

        public int getIconLeft() {
            return iconLeft;
        }

        public void setIconLeft(int iconLeft) {
            this.iconLeft = iconLeft;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isShouldShowArrow() {
            return shouldShowArrow;
        }

        public void setShouldShowArrow(boolean shouldShowArrow) {
            this.shouldShowArrow = shouldShowArrow;
        }

        public Integer getGravity() {
            return gravity;
        }

        public void setGravity(Integer gravity) {
            this.gravity = gravity;
        }
    }

    public abstract static class TextAfterChanger implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    public void addTextChange(TextAfterChanger textAfterChanger){
        tvSelectContent.addTextChangedListener(textAfterChanger);
    }

}
