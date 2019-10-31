package com.neuedu.common;

/**
 * 维护状态码
 * */
public class ResponseCode {
    /**
     * 成功
     * */
    public static final int SUCESS=0;
    /**
     * 失败
     * */
    public static final int ERROR=100;

    /**
     * 参数不能为空
     * */
    public static final int PARAM_NOT_NULL=1;
    public static final int USERNAME_EXISTS =2 ;
    public static final int EMAIL_EXISTS =3 ;

    public static final int NOT_LOGIN =4 ;
}
