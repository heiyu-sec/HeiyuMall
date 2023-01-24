package com.heiyu.mall.service;

import com.github.pagehelper.PageInfo;
import com.heiyu.mall.model.request.CreateOrderReq;
import com.heiyu.mall.model.vo.CartVO;
import com.heiyu.mall.model.vo.OrderStatisticsVO;
import com.heiyu.mall.model.vo.OrderVO;

import java.util.Date;
import java.util.List;

public interface OrderService {


    String create(CreateOrderReq createOrderReq);

    OrderVO detail(String orderNo);

    PageInfo listForCustomer(Integer pageNum, Integer pageSize);

    void cancel(String orderNo);

    String qrcode(String orderNo);

    void pay(String orderNo);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    void deliver(String orderNo);

    //发货
    void finish(String orderNo);

    List<OrderStatisticsVO> statistics(Date startDate, Date endDate);
}
