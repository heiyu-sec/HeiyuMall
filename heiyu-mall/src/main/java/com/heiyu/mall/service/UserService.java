package com.heiyu.mall.service;

import com.heiyu.mall.exctption.ImoocMallException;
import com.heiyu.mall.model.pojo.User;

import java.security.NoSuchAlgorithmException;

public interface UserService {
    User getUser();

    void register(String userName,String password,String emailAddress) throws ImoocMallException, NoSuchAlgorithmException;

    User login(String userName, String password) throws ImoocMallException;

    void updateInformation(User user) throws ImoocMallException;

    boolean checkAdminRole(User user);

    boolean checkEmailRegistered(String emailAddress);
}
