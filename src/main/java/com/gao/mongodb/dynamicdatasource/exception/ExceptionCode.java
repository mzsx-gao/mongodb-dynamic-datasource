package com.gao.mongodb.dynamicdatasource.exception;

/**
 * 名称: ExceptionCode
 * 描述: 异常码
 *
 * @author gaoshudian
 * @date 2020-06-09 09:40
 */
public enum  ExceptionCode {

    EXCEPTION_CODE("500","数据源配置异常");

    // 状态码
    private String code;
    // 状态说明
    private String msg;

    ExceptionCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
