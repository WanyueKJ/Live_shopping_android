package com.wanyue.main.bean;

import java.util.List;

public class MainUserSectionBean {
    private int id=-1;
    private String title;
    private List<MenuBean>mMenuBeanList;
    private String rightTitle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MenuBean> getMenuBeanList() {
        return mMenuBeanList;
    }

    public void setMenuBeanList(List<MenuBean> menuBeanList) {
        mMenuBeanList = menuBeanList;
    }

    public String getRightTitle() {
        return rightTitle;
    }

    public void setRightTitle(String rightTitle) {
        this.rightTitle = rightTitle;
    }


}
