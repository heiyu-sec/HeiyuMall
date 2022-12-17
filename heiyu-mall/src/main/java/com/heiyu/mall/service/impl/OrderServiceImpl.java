package com.heiyu.mall.service.impl;

import com.heiyu.mall.common.Constant;
import com.heiyu.mall.exctption.ImoocMallException;
import com.heiyu.mall.exctption.ImoocMallExceptionEnum;
import com.heiyu.mall.filter.UserFilter;
import com.heiyu.mall.model.request.CreateOrderReq;
import com.heiyu.mall.model.vo.CartVO;
import com.heiyu.mall.service.CartService;
import com.heiyu.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述：  订单Service实现类
 */

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    CartService cartService;

    public String create(CreateOrderReq createOrderReq){
        //拿到用户ID
        Integer userId = UserFilter.currentUser.getId();
        //从购物车查找已勾选的商品
        List<CartVO> cartVOList = cartService.list(userId);
        ArrayList<CartVO> cartVOListTemp = new ArrayList<>();
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            if (cartVO.getSelected().equals(Constant.Cart.CHECKED)){
                cartVOListTemp.add(cartVO);
            }
        }
        cartVOList = cartVOListTemp;
        //如果购物车已勾选的为空，就报错
        if(CollectionUtils.isEmpty(cartVOList)){
            throw new ImoocMallException(ImoocMallExceptionEnum.CART_EMPTY);
        }
        //判断商品是否存在，上下架状态，库存
        //把购物车对象转化为订单item对象

        //扣库存

        //把购物车中的已勾选的商品删除

        //生成订单

        //生成订单号，有独立的规则

        //循环保存每个商品的order_item表

        //把结果返回
    }
}
