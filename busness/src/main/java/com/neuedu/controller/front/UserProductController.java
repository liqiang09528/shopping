package com.neuedu.controller.front;


import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.RoleEnum;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.User;
import com.neuedu.service.IProductService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/product/")
public class UserProductController {

    @Autowired
    IProductService iProductService;

    @RequestMapping("isHot")
    public ServerResponse isHot(){
        return iProductService.isHot();
    }

    /**
     * 商品详情
     * */
    @RequestMapping("/{id}")
    public ServerResponse detail(@PathVariable("id") Integer id,HttpSession session){
       

        return iProductService.detail(id);
    };
}
