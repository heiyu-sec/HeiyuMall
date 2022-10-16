package com.heiyu.mall.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.heiyu.mall.exctption.ImoocMallException;
import com.heiyu.mall.exctption.ImoocMallExceptionEnum;
import com.heiyu.mall.model.dao.CategoryMapper;
import com.heiyu.mall.model.pojo.Category;
import com.heiyu.mall.model.request.AddCategoryReq;
import com.heiyu.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述：     目录分类Service实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public void add(AddCategoryReq addCategoryReq){
        Category category = new Category();
        BeanUtils.copyProperties(addCategoryReq,category);
        Category categoryOld = categoryMapper.selectByName(addCategoryReq.getName());
        if(categoryOld!=null){
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }
        int count = categoryMapper.insertSelective(category);
        if(count ==0){
            throw new ImoocMallException(ImoocMallExceptionEnum.CREATE_FAILED);

        }
    }
    @Override
    public void update(Category updateCategory){
        if (updateCategory.getName()!=null) {
            Category categoryOld = categoryMapper.selectByName(updateCategory.getName());
            if (categoryOld !=null&&categoryOld.getId().equals(updateCategory.getId())){
                throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
            }
        }
        int count=categoryMapper.updateByPrimaryKeySelective(updateCategory);
        if (count==0){
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }
}
