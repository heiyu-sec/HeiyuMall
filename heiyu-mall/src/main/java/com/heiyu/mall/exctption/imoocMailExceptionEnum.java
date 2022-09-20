package com.heiyu.mall.exctption;

/**
 * 描述：     异常枚举
 */
public enum imoocMailExceptionEnum {
    NEED_USER_NAME(10001,"用户名不能为空"),
    NEED_PASSWORD(10002,"密码不能为空"),
    PASSWORD_TOO_SHORT(10003,"密码长度太短，不能小于8位");
    /**
     * 异常码
     */
    Integer code;
    /**
     * 异常信息
     */
    String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    imoocMailExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
