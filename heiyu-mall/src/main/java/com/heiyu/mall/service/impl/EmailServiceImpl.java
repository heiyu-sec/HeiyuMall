package com.heiyu.mall.service.impl;

import com.heiyu.mall.common.Constant;
import com.heiyu.mall.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * 描述：  emailservice实现类
 */

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(Constant.EMAIL_FROM);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        mailSender.send(simpleMailMessage);
    }
}
