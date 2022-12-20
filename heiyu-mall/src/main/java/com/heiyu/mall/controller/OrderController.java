package com.heiyu.mall.controller;

import com.github.pagehelper.PageInfo;
import com.heiyu.mall.common.ApiRestResponse;
import com.heiyu.mall.model.request.CreateOrderReq;
import com.heiyu.mall.model.vo.OrderVO;
import com.heiyu.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 描述：  订单控制器
 */

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping("order/create")
    @ApiOperation("创建订单")
    public ApiRestResponse create(@RequestBody CreateOrderReq createOrderReq){
        String orderNo = orderService.create(createOrderReq);
        return ApiRestResponse.success(orderNo);
    }

    @GetMapping("order/detail")
    @ApiOperation("前台订单详情")
    public ApiRestResponse detail(@RequestParam String orderNo){
        OrderVO orderVO = orderService.detail(orderNo);
        return ApiRestResponse.success(orderVO);
    }

    @GetMapping("order/list")
    @ApiOperation("前台订单列表")
    public ApiRestResponse list(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo = orderService.listForCustomer(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }


    @PostMapping("order/cancel")
    @ApiOperation("取消订单")
    public ApiRestResponse cancel(@RequestParam String orderNo){
        orderService.cancel(orderNo);
        return ApiRestResponse.success();
    }

    /**
     * 生成二维码
     */
    @PostMapping("order/qrcode")
    @ApiOperation("生成二维码")
    public ApiRestResponse qrcode(@RequestParam String orderNo){
        String pngAddress = orderService.qrcode(orderNo);
        return ApiRestResponse.success(pngAddress);
    }
}
