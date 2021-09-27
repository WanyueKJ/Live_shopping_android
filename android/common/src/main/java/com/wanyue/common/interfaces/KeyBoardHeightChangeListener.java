package com.wanyue.common.interfaces;

/**
 * Created by  on 2018/11/8.
 */

public interface KeyBoardHeightChangeListener {
    void onKeyBoardHeightChanged(int visibleHeight, int keyboardHeight);

    boolean isSoftInputShowed();
}
