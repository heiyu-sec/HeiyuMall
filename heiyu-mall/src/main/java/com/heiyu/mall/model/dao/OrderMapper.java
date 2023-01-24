package com.heiyu.mall.model.dao;

import com.heiyu.mall.model.pojo.Order;
import com.heiyu.mall.model.query.OrderStatisticsQuery;
import com.heiyu.mall.model.vo.OrderStatisticsVO;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByOrderNo(String orderNo);

    List<Order> selectForCustomer(Integer userId);

    List<Order> selectAllForAdmin();

    List<OrderStatisticsVO> selectOrderStatistics(@Param("query") OrderStatisticsQuery query);
}