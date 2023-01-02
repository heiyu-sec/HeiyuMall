package com.heiyu.mall.service.impl;

import com.heiyu.mall.common.Constant;
import com.heiyu.mall.exctption.ImoocMallException;
import com.heiyu.mall.exctption.ImoocMallExceptionEnum;
import com.heiyu.mall.model.dao.UserMapper;
import com.heiyu.mall.model.pojo.User;
import com.heiyu.mall.service.UserService;
import com.heiyu.mall.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

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

    @Override
    public void register(String userName, String password) throws ImoocMallException, NoSuchAlgorithmException {
        //查询用户名是否存在，不允许重名
      User result=  userMapper.selectByName(userName);
      if(result != null){
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
      }

      //写到数据库
        User user= new User();
        user.setUsername(userName);
        //user.setPassword(password);
        user.setPassword(MD5Utils.getMD5Str(password));
        int count = userMapper.insertSelective(user);
        if(count==0){
            throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);
        }
    }
    @Override
    public User login(String userName, String password) throws ImoocMallException {
        String md5Password = null;
        try {
            md5Password = MD5Utils.getMD5Str(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        User user = userMapper.selectLogin(userName, md5Password);
        if (user == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_PASSWORD);
        }
        return user;
    }
    @Override
    public void updateInformation(User user) throws ImoocMallException {
        //更新个性签名
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 1) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }
    @Override
    public boolean checkAdminRole(User user) {
        //1是普通用户，2是管理员
        return user.getRole().equals(2);
    }

    @Override
    public boolean checkEmailRegistered(String emailAddress){
        User user = userMapper.selectOneByEmailAddress(emailAddress);
        if(user!=null){
            return false;
        }
        return true;
    }
}
