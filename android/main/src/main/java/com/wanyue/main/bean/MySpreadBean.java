package com.wanyue.main.bean;


public class MySpreadBean {
    private int icon;
    private String title;
    private int id;

    public MySpreadBean(int icon, String title, int id) {
        this.icon = icon;
        this.title = title;
        this.id = id;
    }




    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
