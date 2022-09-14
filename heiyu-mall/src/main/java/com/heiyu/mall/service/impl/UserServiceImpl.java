package com.heiyu.mall.service.impl;

import com.heiyu.mall.model.dao.UserMapper;
import com.heiyu.mall.model.pojo.User;
import com.heiyu.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述：     UserService实现类
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public User getUser() {
        return userMapper.selectByPrimaryKey(1);
    }
}
