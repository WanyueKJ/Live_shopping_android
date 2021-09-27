package com.yunbao.im.business;


/*mvp模式的Presnter层实现Presnter和view层的分离,方便以后更换SDK*/
public interface ICallPresnter {
    /*初始化*/
    public void init();
    /*退出房间*/
    public void exitRoom();
    /*进入房间*/
    public void enterRoom(int roomId);
    /*是否开启前置摄像头*/
    public void isFront(boolean isFront);
    /*开启关闭摄像头*/
    public void openCamera(boolean isOpen);
    /*是否开启免提*/
    public void isHandsFree(boolean isHandsFree);
    /*是否开启静音*/
    public void isMute(boolean isMute);
    /*是视频模式还是音频模式*/
    public void isVideo(boolean isVideo);
    /*获取配置的state*/
    public CallLivingState getCallState();
    /*开启本地预览*/
    public void startSDKLocalPreview(boolean isPreview);
    /*设置View层*/
    public void setCallView(IVideoCallView callView);
    /*资源释放方法*/
    public void release();
}
