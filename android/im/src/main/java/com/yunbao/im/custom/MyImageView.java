package com.yunbao.im.custom;

import android.content.Context;
import android.util.AttributeSet;

import com.makeramen.roundedimageview.RoundedImageView;
import com.yunbao.im.bean.ImMessageBean;

import java.io.File;

/**
 * Created by  on 2018/6/7.
 */

public class MyImageView extends RoundedImageView {

    private File mFile;
    private ImMessageBean mImMessageBean;

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public File getFile() {
        return mFile;
    }

    public void setFile(File file) {
        mFile = file;
    }

    public ImMessageBean getImMessageBean() {
        return mImMessageBean;
    }

    public void setImMessageBean(ImMessageBean imMessageBean) {
        mImMessageBean = imMessageBean;
    }
}
