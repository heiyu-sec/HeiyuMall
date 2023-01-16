package com.heiyu.mall.service;

import com.github.pagehelper.PageInfo;
import com.heiyu.mall.model.pojo.Category;
import com.heiyu.mall.model.pojo.Product;
import com.heiyu.mall.model.request.AddCategoryReq;
import com.heiyu.mall.model.request.AddProductReq;
import com.heiyu.mall.model.request.ProductListReq;
import com.heiyu.mall.model.vo.CategoryVO;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 描述：商铺service
 */
public interface ProductService {

    void add(AddProductReq addProductReq);

    void update(Product updateProduct);

    void delete(Integer id);


    void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    Product detail(Integer id);

    PageInfo list(ProductListReq productListReq);

    void addProductByExcel(File destFile) throws IOException;
}
