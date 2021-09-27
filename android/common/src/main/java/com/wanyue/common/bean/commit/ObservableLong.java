package com.wanyue.common.bean.commit;

import com.wanyue.common.utils.FieldUtil;

public class ObservableLong extends BaseObservableField<Long> {
    public ObservableLong(CommitEntity commitEntity) {
        super(commitEntity);
    }
    @Override
    public Long changeData(String s) {
      return FieldUtil.parseLong(s);
    }
}
