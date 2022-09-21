package com.heiyu.mall.controller;

import com.heiyu.mall.common.ApiRestResponse;
import com.heiyu.mall.exctption.ImoocMallException;
import com.heiyu.mall.exctption.imoocMailExceptionEnum;
import com.heiyu.mall.model.pojo.User;
import com.heiyu.mall.service.UserService;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 描述：     用户控制器
 */
@Controller
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/test")
    @ResponseBody
    public User personalPage(){
        return userService.getUser();
    }
    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(@RequestParam("userName") String userName, @RequestParam("password") String password) throws ImoocMallException {
        if (StringUtils.isEmptyOrWhitespaceOnly(userName)){
            return ApiRestResponse.error(imoocMailExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmptyOrWhitespaceOnly(password)){
            return ApiRestResponse.error(imoocMailExceptionEnum.NEED_PASSWORD);
        }
        //密码长度不能少于8为
        if(password.length()<8){
            return ApiRestResponse.error(imoocMailExceptionEnum.PASSWORD_TOO_SHORT);
        }
        userService.register(userName,password);
        return  ApiRestResponse.success();
    }
}