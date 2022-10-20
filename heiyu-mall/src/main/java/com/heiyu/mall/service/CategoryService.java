package com.heiyu.mall.service;

import com.github.pagehelper.PageInfo;
import com.heiyu.mall.model.pojo.Category;
import com.heiyu.mall.model.request.AddCategoryReq;
import com.heiyu.mall.model.vo.CategoryVO;

import java.util.List;

public interface CategoryService {
    void add(AddCategoryReq addCategoryReq);

    void update(Category updateCategory);

    void delete(Integer id);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    List<CategoryVO> listCategoryForCustomer();

    void recursivlyFindCategories(List<CategoryVO> categoryVOArrayList, Integer parentId);


}
