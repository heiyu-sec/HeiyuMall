package com.heiyu.mall.service;

import com.heiyu.mall.exctption.ImoocMallException;
import com.heiyu.mall.model.pojo.User;

import java.security.NoSuchAlgorithmException;

public interface UserService {
    User getUser();

    void register(String userName,String password) throws ImoocMallException, NoSuchAlgorithmException;

    User login(String userName, String password) throws ImoocMallException;
}
