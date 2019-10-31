package com.neuedu.controller.backend;

import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.RoleEnum;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.User;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manager/category/")
public class CategoryController {
    @Autowired
    ICategoryService iCategoryService;
    /**
     * 添加类别
     * */
    @RequestMapping("addCategory")
    public ServerResponse addCategory(Category category,HttpSession session){
        System.out.println(category);
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"请登录");
        if (user.getRole()== RoleEnum.ROLE_USER.getRole())
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足");
        return iCategoryService.addCategory(category);
    }
    /**
     * 修改类别
     * id
     * name
     * url
     * */
    @RequestMapping("updateCategory")
    public ServerResponse updateCategory(Category category,HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"请登录");
        if (user.getRole()== RoleEnum.ROLE_USER.getRole())
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足");
        return iCategoryService.updateCategory(category);
    }
    /**
     *
     * 查看平级类别
     * categoryId
     *
     * */
    @RequestMapping("/{categoryId}")
    public ServerResponse getCategoryById(@PathVariable("categoryId") Integer categoryId, HttpSession session){

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"请登录");
        if (user.getRole()== RoleEnum.ROLE_USER.getRole())
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足");


        return iCategoryService.getCategoryById(categoryId);
    }

    /**
     *
     * 递归查看类别
     * categoryId
     *
     * */
    @RequestMapping("/deep/{categoryId}")
    public ServerResponse deepCategory(@PathVariable("categoryId") Integer categoryId, HttpSession session){


        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"请登录");
        if (user.getRole()== RoleEnum.ROLE_USER.getRole())
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足");


        return iCategoryService.deepCategory(categoryId);
    }
}
