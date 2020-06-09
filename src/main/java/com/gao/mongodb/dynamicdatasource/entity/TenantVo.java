package com.gao.mongodb.dynamicdatasource.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class TenantVo{

    /**
     * 描述： 租户CODE
     */
    private String tenantCode;
    /**
     * 模块数据库名，对应一个应用模块有多个数据源的场景
     */
    //TODO
    private String database="";

}
