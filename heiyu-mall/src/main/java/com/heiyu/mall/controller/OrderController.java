package com.heiyu.mall.controller;

import com.heiyu.mall.common.ApiRestResponse;
import com.heiyu.mall.model.request.CreateOrderReq;
import com.heiyu.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：  订单控制器
 */

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping("order/create")
    public ApiRestResponse create(@RequestBody CreateOrderReq createOrderReq){
        return null;
    }
}
