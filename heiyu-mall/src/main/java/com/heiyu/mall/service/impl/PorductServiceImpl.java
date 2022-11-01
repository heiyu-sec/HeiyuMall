package com.heiyu.mall.service.impl;

import com.heiyu.mall.exctption.ImoocMallException;
import com.heiyu.mall.exctption.ImoocMallExceptionEnum;
import com.heiyu.mall.model.dao.ProductMapper;
import com.heiyu.mall.model.pojo.Product;
import com.heiyu.mall.model.request.AddProductReq;
import com.heiyu.mall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述：商品服务实现类
 */
@Service
public class PorductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;

    @Override
    public void add(AddProductReq addProductReq){
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq,product);
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if(productOld!=null){
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.insertSelective(product);
        if(count==0){
            throw new ImoocMallException(ImoocMallExceptionEnum.CREATE_FAILED);
        }

    }

    @Override
    public void update(Product updateProduct){
        Product productOld = productMapper.selectByName(updateProduct.getName());
        //同名且不同ID， 不能修改
        if(productOld!=null && !productOld.getId().equals(updateProduct.getId())){
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.updateByPrimaryKeySelective(updateProduct);
        if(count==0){
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }

    }

}
