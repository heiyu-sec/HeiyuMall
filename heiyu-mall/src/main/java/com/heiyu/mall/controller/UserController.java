package com.heiyu.mall.controller;

import com.heiyu.mall.common.ApiRestResponse;
import com.heiyu.mall.common.Constant;
import com.heiyu.mall.exctption.ImoocMallException;
import com.heiyu.mall.exctption.ImoocMallExceptionEnum;
import com.heiyu.mall.model.pojo.User;
import com.heiyu.mall.service.UserService;


import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 描述：     用户控制器
 */
@Controller
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("/")
    @ResponseBody
    public User personal2Page(){
        return userService.getUser();
    }
    @GetMapping("/test")
    @ResponseBody
    public User personalPage(){
        return userService.getUser();
    }
    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(@RequestParam("userName") String userName, @RequestParam("password") String password) throws ImoocMallException, NoSuchAlgorithmException {
        if (StringUtils.isEmpty(userName)){
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)){
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }
        //密码长度不能少于8为
        if(password.length()<8){
            return ApiRestResponse.error(ImoocMallExceptionEnum.PASSWORD_TOO_SHORT);
        }
        userService.register(userName,password);
        return  ApiRestResponse.success();
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    @ResponseBody
    public ApiRestResponse login(@RequestParam("userName") String userName,
                                 @RequestParam("password") String password, HttpSession session)
            throws ImoocMallException {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName, password);
        //保存用户信息时，不保存密码
        user.setPassword(null);
        session.setAttribute(Constant.IMOOC_MALL_USER, user);
        return ApiRestResponse.success(user);
    }

    /**
     * 更新个性签名
     */
    @PostMapping("/user/update")
    @ResponseBody
    public ApiRestResponse updateUserInfo(HttpSession session, @RequestParam String signature)
            throws ImoocMallException {
        User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
        }
        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateInformation(user);
        return ApiRestResponse.success();
    }
    /**
     * 登出，清除session
     */
    @PostMapping("/user/logout")
    @ResponseBody
    public ApiRestResponse logout(HttpSession session) {
        session.removeAttribute(Constant.IMOOC_MALL_USER);
        return ApiRestResponse.success();
    }
    /**
     * 管理员登录接口
     */
    @PostMapping("/adminLogin")
    @ResponseBody
    public ApiRestResponse adminLogin(@RequestParam("userName") String userName,
                                      @RequestParam("password") String password, HttpSession session)
            throws ImoocMallException {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName, password);
        //校验是否是管理员
        if (userService.checkAdminRole(user)) {
            //是管理员，执行操作
            //保存用户信息时，不保存密码
            user.setPassword(null);
            session.setAttribute(Constant.IMOOC_MALL_USER, user);
            return ApiRestResponse.success(user);
        } else {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
        }
    }

}