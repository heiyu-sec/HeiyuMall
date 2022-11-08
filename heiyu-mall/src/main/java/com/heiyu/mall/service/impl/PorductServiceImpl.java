package com.heiyu.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heiyu.mall.common.Constant;
import com.heiyu.mall.exctption.ImoocMallException;
import com.heiyu.mall.exctption.ImoocMallExceptionEnum;
import com.heiyu.mall.model.dao.ProductMapper;
import com.heiyu.mall.model.pojo.Product;
import com.heiyu.mall.model.query.ProductListQuery;
import com.heiyu.mall.model.request.AddProductReq;
import com.heiyu.mall.model.request.ProductListReq;
import com.heiyu.mall.model.vo.CategoryVO;
import com.heiyu.mall.service.CategoryService;
import com.heiyu.mall.service.ProductService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：商品服务实现类
 */
@Service
public class PorductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryService categoryService;
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

    @Override
    public void delete(Integer id){
        Product productOld = productMapper.selectByPrimaryKey(id);
        //查不到该记录，无法删除
        if(productOld ==null ){
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        }
        int count = productMapper.deleteByPrimaryKey(id);
        if(count==0){
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus){
    productMapper.batchUpdateSellStatus(ids,sellStatus);
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Product> products = productMapper.selectListForAdmin();
        PageInfo pageInfo = new PageInfo(products);
        return pageInfo;
    }

    @Override
    public Product detail(Integer id){
        Product product = productMapper.selectByPrimaryKey(id);
        return product;

    }
    @Override
    public PageInfo list(ProductListReq productListReq){
        //构建query对象
        ProductListQuery productListQuery = new ProductListQuery();

        //搜索处理
        if(!StringUtils.isEmpty(productListQuery.getKeyword())){
            String keyword = new StringBuilder().append("%").append(productListReq.getKeyword()).append("%").toString();
            productListQuery.setKeyword(keyword);
        }
        //目录处理：如果查某个目录下的商品，不仅要查出该目录的，还要吧子目录下所有的商品都查出来，所以要拿到一个目录id的list
        if(productListReq.getCategoryId()!=null){
            List<CategoryVO> categoryVOSList = categoryService.listCategoryForCustomer(productListReq.getCategoryId());
            ArrayList<Integer> categoryIds = new ArrayList<>();
            categoryIds.add(productListReq.getCategoryId());
            getCategoryIds(categoryVOSList,categoryIds);
            productListQuery.setCategoryIds(categoryIds);
        }

        //排序处理
        String orderBy = productListReq.getOrderBy();
        if(Constant.ProductListOrderBy.FRICE_ASC_DESC.contains(orderBy)){
            PageHelper.startPage(productListReq.getPageNum(),productListReq.getPageSize(),orderBy);
        }else {
            PageHelper.startPage(productListReq.getPageNum(),productListReq.getPageSize());
        }

        List<Product> productList = productMapper.selectList(productListQuery);
        PageInfo pageInfo = new PageInfo(productList);
        return pageInfo;
    }

    private void getCategoryIds(List<CategoryVO> categoryVOSList,ArrayList<Integer> categoryIds){
        for (int i = 0; i < categoryVOSList.size(); i++) {
            CategoryVO categoryVO =  categoryVOSList.get(i);
            if(categoryVO !=null){
                categoryIds.add(categoryVO.getId());
                getCategoryIds(categoryVO.getChildCategory(),categoryIds);
            }
            
        }
    }
}
