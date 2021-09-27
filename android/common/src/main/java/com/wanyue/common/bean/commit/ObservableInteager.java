package com.wanyue.common.bean.commit;

import com.wanyue.common.utils.FieldUtil;

public class ObservableInteager extends BaseObservableField<Integer>{
    public ObservableInteager(CommitEntity commitEntity) {
        super(commitEntity);
    }
    @Override
    public Integer changeData(String s) {
       return FieldUtil.parseInt(s);
    }
}
