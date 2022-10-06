package com.heiyu.mall.controller;

import com.heiyu.mall.common.ApiRestResponse;
import com.heiyu.mall.common.Constant;
import com.heiyu.mall.exctption.ImoocMallExceptionEnum;
import com.heiyu.mall.model.pojo.User;
import com.heiyu.mall.model.request.AddCategoryReq;
import com.heiyu.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 描述：     目录Controller
 */
@Controller
public class CategoryController {
    @Autowired
    UserService userService;

    @PostMapping("admin/catgory/add")
    @ResponseBody
    public ApiRestResponse addCategory(HttpSession session, AddCategoryReq addCategoryReq){
            if(addCategoryReq.getName()==null||addCategoryReq.getParentId()==null||addCategoryReq.getOrderNum()==null||addCategoryReq.getType()==null){
                return ApiRestResponse.error(ImoocMallExceptionEnum.PARA_NOT_NULL);
            }
        User currentUser= (User)session.getAttribute(Constant.IMOOC_MALL_USER);
            if(currentUser ==null){
                return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
            }
            boolean adminRole= userService.checkAdminRole(currentUser);
                if(adminRole){

                }else {
                    return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN)
                }
    }
}
