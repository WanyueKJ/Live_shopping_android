package com.wanyue.common.upload;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by  on 2019/4/16.
 */

public class UploadBean {
    private File mOriginFile;//要被上传的源文件
    private String mRemoteFileName;//上传成功后在云存储上的文件名字
    private String mRemoteAccessUrl;//上传成功后在云存储上的访问地址
    private boolean mSuccess;//是否上传成功了

    public UploadBean() {
    }

    public UploadBean(File originFile) {
        mOriginFile = originFile;
    }

    public File getOriginFile() {
        return mOriginFile;
    }

    public void setOriginFile(File originFile) {
        mOriginFile = originFile;
    }

    public String getRemoteFileName() {
        return mRemoteFileName;
    }

    public void setRemoteFileName(String remoteFileName) {
        mRemoteFileName = remoteFileName;
    }

    public String getRemoteAccessUrl() {
        return mRemoteAccessUrl;
    }

    public void setRemoteAccessUrl(String remoteAccessUrl) {
        mRemoteAccessUrl = remoteAccessUrl;
    }

    public boolean isSuccess() {
        return mSuccess;
    }

    public void setSuccess(boolean success) {
        mSuccess = success;
    }

    public void setEmpty() {
        mOriginFile = null;
        mRemoteFileName = null;
        mRemoteAccessUrl = null;
    }
    public boolean isEmpty() {
        return mOriginFile == null && mRemoteFileName == null && mRemoteAccessUrl == null;
    }

    @NotNull
    @Override
    public String toString() {
        if(mRemoteFileName!=null){
            return mRemoteFileName;
        }
        return super.toString();

    }
}
