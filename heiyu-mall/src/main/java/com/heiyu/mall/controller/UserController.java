package com.heiyu.mall.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.heiyu.mall.common.ApiRestResponse;
import com.heiyu.mall.common.Constant;
import com.heiyu.mall.exctption.ImoocMallException;
import com.heiyu.mall.exctption.ImoocMallExceptionEnum;
import com.heiyu.mall.filter.UserFilter;
import com.heiyu.mall.model.pojo.User;

import com.heiyu.mall.service.EmailService;
import com.heiyu.mall.service.UserService;


import com.heiyu.mall.util.EmailUtil;

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
import java.util.Date;

/**
 * 描述：     用户控制器
 */
@Controller
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

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

    /**
     * 注册
     */
    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(@RequestParam("userName") String userName,
                                    @RequestParam("password") String password, @RequestParam("emailAddress") String emailAddress,  @RequestParam("verificationCode") String verificationCode) throws ImoocMallException, NoSuchAlgorithmException {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }
        //密码长度不能少于8位
        if (password.length() < 8) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.PASSWORD_TOO_SHORT);
        }
        if (StringUtils.isEmpty(emailAddress)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_EMAIL_ADDRESS);
        }
        if (StringUtils.isEmpty(verificationCode)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_VERIFICATION_CODE);
        }
        //如果邮箱已注册，则不允许再次注册
        boolean emailPassed = userService.checkEmailRegistered(emailAddress);
        if (!emailPassed) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.EMAIL_ALREADY_BEEN_REGISTERED);
        }
        //校验邮箱和验证码是否匹配
        Boolean passEmailAndCode = emailService.checkEmailAndCode(emailAddress, verificationCode);
        if (!passEmailAndCode) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.WRONG_VERIFICATION_CODE);
        }
        userService.register(userName, password, emailAddress);
        return ApiRestResponse.success();
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
        //User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
        User currentUser = UserFilter.currentUser;
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

    @GetMapping("/loginWithJwt")
    @ResponseBody
    public ApiRestResponse loginWithJwt(@RequestParam String userName, @RequestParam String password) {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName, password);
        //保存用户信息时，不保存密码
        user.setPassword(null);
        Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);
        String token = JWT.create()
                .withClaim(Constant.USER_NAME, user.getUsername())
                .withClaim(Constant.USER_ID, user.getId())
                .withClaim(Constant.USER_ROLE, user.getRole())
                //过期时间
                .withExpiresAt(new Date(System.currentTimeMillis() + Constant.EXPIRE_TIME))
                .sign(algorithm);
        return ApiRestResponse.success(token);
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



    /**
     * 发送邮件
     */
    @PostMapping("/user/sendEmail")
    @ResponseBody
    public ApiRestResponse sendEmail(@RequestParam("emailAddress") String emailAddress)
            throws ImoocMallException {

        //检查邮件地址是否有效，检查是否已注册
        boolean validEmailAddress = EmailUtil.isValidEmailAddress(emailAddress);
        if (validEmailAddress){
            boolean emailPassed = userService.checkEmailRegistered(emailAddress);
            if (!emailPassed){
                return ApiRestResponse.error(ImoocMallExceptionEnum.EMAIL_ALREADY_BEEN_REGISTERED);
            } else {
                String verificationCode = EmailUtil.genVerificationCode();
                Boolean saveEmailToRedis = emailService.saveEmailToRedis(emailAddress, verificationCode);
                if(saveEmailToRedis){
                emailService.sendSimpleMessage(emailAddress,Constant.EMAIL_SUBJECT,"欢迎注册，您的验证码是"+verificationCode);
                return ApiRestResponse.success();
                }else {
                    return ApiRestResponse.error(ImoocMallExceptionEnum.EMAIL_ALREADY_BEEN_SEND);

                }
            }
        }else {
                return ApiRestResponse.error(ImoocMallExceptionEnum.WRONG_EMAIL);
        }
        }

    /**
     * 管理员登录接口
     */
    @GetMapping("/adminLoginWithJwt")
    @ResponseBody
    public ApiRestResponse adminLoginWithJwt(@RequestParam("userName") String userName,
                                             @RequestParam("password") String password)
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
            Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);
            String token = JWT.create()
                    .withClaim(Constant.USER_NAME, user.getUsername())
                    .withClaim(Constant.USER_ID, user.getId())
                    .withClaim(Constant.USER_ROLE, user.getRole())
                    //过期时间
                    .withExpiresAt(new Date(System.currentTimeMillis() + Constant.EXPIRE_TIME))
                    .sign(algorithm);
            return ApiRestResponse.success(token);
        } else {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
        }
    }
    }

