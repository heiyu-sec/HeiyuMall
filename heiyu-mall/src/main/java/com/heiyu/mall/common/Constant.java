package com.heiyu.mall.common;

import org.springframework.beans.factory.annotation.Value;

/**
 * 描述：常量值
 */
public class Constant {
    public static final String SALT="34dsdfDDD,0sss8";
    public static final String IMOOC_MALL_USER = "imooc_mall_user";
    @Value("${file.upload.dir}")
    public static String FILE_UPLOAD_DIR;
}


