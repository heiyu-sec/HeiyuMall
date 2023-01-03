package com.heiyu.mall.service;

import com.heiyu.mall.model.vo.CartVO;

import java.util.List;

public interface EmailService {

    void sendSimpleMessage(String to,String subject,String text);


    Boolean saveEmailToRedis(String emailAddress, String verificationCode);
}
