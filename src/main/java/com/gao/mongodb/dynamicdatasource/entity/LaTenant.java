package com.gao.mongodb.dynamicdatasource.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode
@Document(collection = "laTenant")
public class LaTenant implements Serializable {

    @Id
    @Field("id")
    private String id;
    /**
     * 描述： 租户CODE
     */
    private String tenantCode;

    /**
     * 描述： 租户NAME
     */
    private String tenantName;
    /**
     * 描述： 租户状态, 1禁用 2启用
     */
    private int tenantStatus;
    /**
     * 应用名称
     */
    private String application;

    /**
     * 数据库连接信息
     */
    private String uri;
    private String username;
    private String password;
    private String authdatabase;
    private String database;

}
