package com.heiyu.mall.service;

import com.heiyu.mall.model.request.CreateOrderReq;
import com.heiyu.mall.model.vo.CartVO;

import java.util.List;

public interface OrderService {


    String create(CreateOrderReq createOrderReq);
}
