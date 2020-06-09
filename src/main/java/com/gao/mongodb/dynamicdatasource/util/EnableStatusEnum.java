package com.gao.mongodb.dynamicdatasource.util;

public enum EnableStatusEnum {

    ENABLE(2,"启用"),
    DISABLE(1,"禁用");

    private int status;

    private String desc;

    EnableStatusEnum(int status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public int status(){
        return this.status;
    }

    public String desc(){
        return this.desc;
    }
}
