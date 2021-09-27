package com.yunbao.im.business;

import android.content.Context;
import android.view.View;

/*mvp模式的View层实现Presnter和view层的分离,方便以后更换SDK*/
public interface IVideoCallView<T extends View> {
    /*退出房间*/
    public void onExitRoom();
    /*Presnter层提供环境变量*/
    public Context getContext();
    /*Presnter层聊天者的窗口*/
    public T getVideoView(String id);
    /*Presnter层提供自己的主窗口*/
    public T getMainVideoView();
    /*监听视频另一方是否开启推流视频*/
    public void ontherOpenVideo(boolean isOpen);

}
