package com.heiyu.mall.service;

import com.heiyu.mall.model.pojo.User;

public interface UserService {
    User getUser();

    void register(String userName,String password);
}
