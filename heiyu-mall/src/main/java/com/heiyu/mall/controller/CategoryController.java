package com.heiyu.mall.controller;

import com.github.pagehelper.PageInfo;
import com.heiyu.mall.common.ApiRestResponse;
import com.heiyu.mall.common.Constant;
import com.heiyu.mall.exctption.ImoocMallExceptionEnum;
import com.heiyu.mall.filter.UserFilter;
import com.heiyu.mall.model.pojo.Category;
import com.heiyu.mall.model.pojo.User;
import com.heiyu.mall.model.request.AddCategoryReq;
import com.heiyu.mall.model.request.UpdateCategoryReq;
import com.heiyu.mall.model.vo.CategoryVO;
import com.heiyu.mall.service.CategoryService;
import com.heiyu.mall.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * 描述：     目录Controller
 */
@Controller
public class CategoryController {
    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    /**
     * 后台添加目录
     */
    @ApiOperation("后台添加目录")
    @PostMapping("admin/category/add")
    @ResponseBody
    public ApiRestResponse addCategory(HttpSession session,
                                       @Valid @RequestBody AddCategoryReq addCategoryReq) {
        //User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
        User currentUser = UserFilter.currentUser;
        if (currentUser == null) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
        }
        //校验是否是管理员
        boolean adminRole = userService.checkAdminRole(currentUser);
        if (adminRole) {
            //是管理员，执行操作
            categoryService.add(addCategoryReq);
            return ApiRestResponse.success();
        } else {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
        }
    }
    @ApiOperation("后台更新目录")
    @PostMapping("admin/category/update")
    @ResponseBody
    public ApiRestResponse updateCategory(@Valid @RequestBody UpdateCategoryReq updateCategoryReq,HttpSession session){
        //User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
        User currentUser = UserFilter.currentUser;
        if (currentUser == null) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
        }
        //校验是否是管理员
        boolean adminRole = userService.checkAdminRole(currentUser);
        if (adminRole) {
            //是管理员，执行操作
            Category category= new Category();
            BeanUtils.copyProperties(updateCategoryReq,category);
            categoryService.update(category);
            return ApiRestResponse.success();
        } else {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
        }
    }

    @ApiOperation("后台删除目录")
    @PostMapping("admin/category/delete")
    @ResponseBody
    public ApiRestResponse deleteCategory(@RequestParam Integer id){
         categoryService.delete(id);
        return ApiRestResponse.success();
    }
    @ApiOperation("后台目录列表")
    @PostMapping("admin/category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForAdmin(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo = categoryService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("前台目录列表")
    @RequestMapping("category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForCustomer(){
        List<CategoryVO> categoryVOS = categoryService.listCategoryForCustomer(0);
        return ApiRestResponse.success(categoryVOS);
    }


}
