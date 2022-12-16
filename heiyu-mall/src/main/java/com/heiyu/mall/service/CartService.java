package com.heiyu.mall.service;

import com.heiyu.mall.exctption.ImoocMallException;
import com.heiyu.mall.model.pojo.User;
import com.heiyu.mall.model.vo.CartVO;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface CartService {
    List<CartVO> list(Integer userId);

    List<CartVO> add(Integer userId, Integer productId, Integer count);

    List<CartVO> update(Integer userId, Integer productId, Integer count);
}
