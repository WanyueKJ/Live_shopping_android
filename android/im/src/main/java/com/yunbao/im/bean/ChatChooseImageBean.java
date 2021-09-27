package com.yunbao.im.bean;

import java.io.File;

/**
 * Created by  on 2018/7/17.
 */

public class ChatChooseImageBean {
    private File mImageFile;
    private boolean mChecked;

    public ChatChooseImageBean(File imageFile) {
        mImageFile = imageFile;
    }

    public File getImageFile() {
        return mImageFile;
    }

    public void setImageFile(File imageFile) {
        mImageFile = imageFile;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }
}
