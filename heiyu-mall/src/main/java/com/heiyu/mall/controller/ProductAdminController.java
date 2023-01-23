package com.heiyu.mall.controller;

import com.github.pagehelper.PageInfo;
import com.heiyu.mall.common.ApiRestResponse;
import com.heiyu.mall.common.Constant;
import com.heiyu.mall.common.ValidList;
import com.heiyu.mall.exctption.ImoocMallException;
import com.heiyu.mall.exctption.ImoocMallExceptionEnum;
import com.heiyu.mall.model.pojo.Product;
import com.heiyu.mall.model.request.AddProductReq;
import com.heiyu.mall.model.request.UpdateProductReq;
import com.heiyu.mall.service.ProductService;
import com.heiyu.mall.service.UploadService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

/**
 * 描述：后台商铺管理Controller
 */

@RestController
@Validated
public class ProductAdminController {
    @Autowired
    ProductService productService;

    @Autowired
    UploadService uploadService;

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

    @ApiOperation("后台更新商品")
    @PostMapping("/admin/product/update")
    public ApiRestResponse updateProduct(@Valid@RequestBody UpdateProductReq updateProductReq){
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq,product);
        productService.update(product);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台删除商品")
    @PostMapping("/admin/product/delete")
    public ApiRestResponse deleteProduct(@RequestParam Integer id){
        productService.delete(id);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台批量上下架接口")
    @PostMapping("/admin/product/batchUpdateSellStatus")
    public ApiRestResponse batchUpdateSellStatus(@RequestParam Integer[] ids,@RequestParam Integer sellStatus){
        productService.batchUpdateSellStatus(ids,sellStatus);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台商品列表接口")
    @PostMapping("/admin/product/list")
    public ApiRestResponse list(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo = productService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }


    @ApiOperation("后台批量上传商品接口")
    @PostMapping("/admin/upload/product")
    public ApiRestResponse uploadProduct(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String newFileName = uploadService.getNewFileName(multipartFile);
        //创建文件
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        uploadService.createFile(multipartFile, fileDirectory, destFile);
        productService.addProductByExcel(destFile);
        return ApiRestResponse.success();
    }

    @PostMapping("/admin/upload/image")
    public ApiRestResponse uploadImage(HttpServletRequest httpServletRequest,
                                       @RequestParam("file") MultipartFile file) throws IOException {
        String result = uploadService.uploadImage(file);
        return ApiRestResponse.success(result);
    }
    @ApiOperation("后台批量更新商品")
    @PostMapping("/admin/product/batchUpdate")
    public ApiRestResponse batchUpdateProduct(@Valid @RequestBody List<UpdateProductReq> updateProductReqList) {
        for (int i = 0; i < updateProductReqList.size(); i++) {
            UpdateProductReq updateProductReq = updateProductReqList.get(i);
            //方法一，手动校验
            if (updateProductReq.getPrice() < 1) {
                throw new ImoocMallException(ImoocMallExceptionEnum.PRICE_TOO_LOW);
            }
            if (updateProductReq.getStock() > 10000) {
                throw new ImoocMallException(ImoocMallExceptionEnum.STOCK_TOO_MANY);
            }
            Product product = new Product();
            BeanUtils.copyProperties(updateProductReq, product);
            productService.update(product);
        }
        return ApiRestResponse.success();
    }

    @ApiOperation("后台批量更新商品，ValidList验证")
    @PostMapping("/admin/product/batchUpdate2")
    public ApiRestResponse batchUpdateProduct2(@Valid @RequestBody ValidList<UpdateProductReq> updateProductReqList) {
        for (int i = 0; i < updateProductReqList.size(); i++) {
            UpdateProductReq updateProductReq = updateProductReqList.get(i);
            Product product = new Product();
            BeanUtils.copyProperties(updateProductReq, product);
            productService.update(product);
        }
        return ApiRestResponse.success();
    }

    @ApiOperation("后台批量更新商品，@Validated验证")
    @PostMapping("/admin/product/batchUpdate3")
    public ApiRestResponse batchUpdateProduct3(@Valid @RequestBody List<UpdateProductReq> updateProductReqList) {
        for (int i = 0; i < updateProductReqList.size(); i++) {
            UpdateProductReq updateProductReq = updateProductReqList.get(i);
            Product product = new Product();
            BeanUtils.copyProperties(updateProductReq, product);
            productService.update(product);
        }
        return ApiRestResponse.success();
    }
}