package com.wanyue.common.bean;

import android.view.View;

import com.alibaba.fastjson.annotation.JSONField;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.utils.StringUtil;

/**
 * Created by  on 2018/10/12.
 */

public class ChatGiftBean {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_DELUXE = 1;
    public static final int MARK_NORMAL = 0;
    public static final int MARK_HOT = 1;
    public static final int MARK_GUARD = 2;

    private int id;
    private int type;//0 普通礼物 1是豪华礼物
    private int mark;// 0 普通  1热门  2守护
    private String name;
    private String price;
    private String icon;
    private boolean checked;
    private int page;
    private View mView;
    private String swf;
    private String swftype;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    @JSONField(name = "giftname")
    public String getName() {
        return name;
    }

    @JSONField(name = "giftname")
    public void setName(String name) {
        this.name = name;
    }

    @JSONField(name = "needcoin")
    public String getPrice() {
        return price;
    }

    @JSONField(name = "needcoin")
    public void setPrice(String price) {
        this.price = price;
    }

    @JSONField(name = "gifticon")
    public String getIcon() {
        return icon;
    }

    @JSONField(name = "gifticon")
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        mView = view;
    }

    public String getSwf() {
        return swf;
    }

    public void setSwf(String swf) {
        this.swf = swf;
    }

    public String getSwftype() {
        return swftype;
    }

    public void setSwftype(String swftype) {
        this.swftype = swftype;
    }

    public ChatReceiveGiftBean valueToChatReceiveGiftBean(){
        UserBean userBean=CommonAppConfig.getUserBean();
        ChatReceiveGiftBean chatReceiveGiftBean=new ChatReceiveGiftBean();
        chatReceiveGiftBean.setGiftIcon(icon);
        chatReceiveGiftBean.setGiftName(name);
        chatReceiveGiftBean.setGiftId(Integer.toString(id));
        chatReceiveGiftBean.setAvatar(userBean.getAvatar());
        chatReceiveGiftBean.setUserNiceName(userBean.getUserNiceName());
        chatReceiveGiftBean.setUid(userBean.getId());
        chatReceiveGiftBean.setGif(type);
        if(StringUtil.isInt(swftype)){
         chatReceiveGiftBean.setGitType(Integer.parseInt(swftype));
        }
        chatReceiveGiftBean.setGifUrl(swf);
        return chatReceiveGiftBean;
    }
}
