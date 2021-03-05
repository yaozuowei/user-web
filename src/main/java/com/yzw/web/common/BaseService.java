package com.yzw.web.common;


import com.github.pagehelper.PageHelper;
import com.yzw.web.common.entity.IDomain;
import com.yzw.web.common.util.StringUtil;

import java.util.Date;

/**
 * @author Yao
 */
public abstract class BaseService {

    public <T extends IDomain> void initModel(String operator, T t) {
        t.setCreateUserLabel(operator);
        t.setUpdateUserLabel(operator);
        t.setRecordVersion(Integer.valueOf(1));
        t.setCreateDate(new Date());
        t.setUpdateDate(new Date());
    }

    public <T extends IDomain> void updateModel(String operator, T t) {
        t.setUpdateUserLabel(operator);
        t.setRecordVersion(t.getRecordVersion() + 1);
        t.setUpdateDate(new Date());
    }

    public <T extends IDomain> void deleteModel(String operator, T t) {
        t.setDeleteUserLabel(operator);
        t.setRecordVersion(t.getRecordVersion() + 1);
        t.setDeleteDate(new Date());
    }



}
