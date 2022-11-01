package com.heiyu.mall.service;

import com.github.pagehelper.PageInfo;
import com.heiyu.mall.model.pojo.Category;
import com.heiyu.mall.model.pojo.Product;
import com.heiyu.mall.model.request.AddCategoryReq;
import com.heiyu.mall.model.request.AddProductReq;
import com.heiyu.mall.model.vo.CategoryVO;

import java.util.List;

/**
 * 描述：商铺service
 */
public interface ProductService {

    void add(AddProductReq addProductReq);

    void update(Product updateProduct);
}
