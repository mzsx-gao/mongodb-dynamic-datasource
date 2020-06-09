package com.gao.mongodb.dynamicdatasource.exception;

import lombok.Data;

/**
 * 名称: DynamicDatasourceException
 * 描述: 动态数据源自定义异常
 *
 * @author gaoshudian
 * @date 2020-06-09 09:38
 */
@Data
public class DynamicDatasourceException extends RuntimeException{

    private String code;
    private String msg;

    public DynamicDatasourceException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public DynamicDatasourceException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
