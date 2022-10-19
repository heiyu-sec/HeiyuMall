package com.heiyu.mall.service;

import com.heiyu.mall.model.pojo.Category;
import com.heiyu.mall.model.request.AddCategoryReq;

public interface CategoryService {
    void add(AddCategoryReq addCategoryReq);

    void update(Category updateCategory);

    void delete(Integer id);
}
