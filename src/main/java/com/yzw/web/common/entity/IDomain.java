package com.yzw.web.common.entity;

import java.io.Serializable;
import java.util.Date;

public interface IDomain extends Serializable {
    Date getCreateDate();

    void setCreateDate(Date paramDate);

    Date getUpdateDate();

    void setUpdateDate(Date paramDate);

    Date getDeleteDate();

    void setDeleteDate(Date paramDate);

    String getCreateUserLabel();

    void setCreateUserLabel(String paramString);

    String getUpdateUserLabel();

    void setUpdateUserLabel(String paramString);

    String getDeleteUserLabel();

    void setDeleteUserLabel(String paramString);

    Integer getRecordVersion();

    void setRecordVersion(Integer paramInteger);

    String getTenantId();

    void setTenantId(String tenantId);
}
