package com.heiyu.mall.controller;

import com.heiyu.mall.common.ApiRestResponse;
import com.heiyu.mall.common.Constant;
import com.heiyu.mall.exctption.ImoocMallException;
import com.heiyu.mall.exctption.ImoocMallExceptionEnum;
import com.heiyu.mall.model.pojo.Product;
import com.heiyu.mall.model.request.AddProductReq;
import com.heiyu.mall.model.request.UpdateProductReq;
import com.heiyu.mall.service.ProductService;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * 描述：后台商铺管理Controller
 */

@RestController
public class ProductAdminController {
    @Autowired
    ProductService productService;

    @PostMapping("admin/product/add")
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq) {
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }

    @PostMapping("admin/upload/file")
    public ApiRestResponse upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称uuid
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + suffixName;
        //创建文件
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        if (!fileDirectory.exists()) {
            if (!fileDirectory.mkdir()) {
                throw new ImoocMallException(ImoocMallExceptionEnum.MKDIR_FAILED);
            }
        }
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return ApiRestResponse.success(getHost(new URI(httpServletRequest.getRequestURL()+""))+"/images/"+newFileName);
        } catch (URISyntaxException e) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.UPLOAD_FAILED);
        }
    }

    private URI getHost(URI uri){
        URI effectiveURI;
        try {
            effectiveURI= new URI(uri.getScheme(),uri.getUserInfo(),uri.getHost(),uri.getPort(),null,null,null);
        } catch (URISyntaxException e) {
            effectiveURI=null;
        }
        return effectiveURI;
    }

    @PostMapping("admin/product/update")
    public ApiRestResponse updateProduct(@Valid@RequestBody UpdateProductReq updateProductReq){
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq,product);
        productService.update(product);
        return ApiRestResponse.success();
    }
}