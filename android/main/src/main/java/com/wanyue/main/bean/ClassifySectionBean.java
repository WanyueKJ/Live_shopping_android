package com.wanyue.main.bean;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.wanyue.common.utils.L;

public class ClassifySectionBean extends SectionEntity<ClassifyBean> {
    private int navtionIndex;

    public ClassifySectionBean(boolean isHeader,String header) {
        super(isHeader, header);
    }
    public ClassifySectionBean(ClassifyBean classifyBean) {
        super(classifyBean);
    }

    public void setIndex(int index) {
        this.navtionIndex = index;
        L.e("navtionIndex=="+navtionIndex);
    }

    public int getIndex() {
        return navtionIndex;
    }
}
