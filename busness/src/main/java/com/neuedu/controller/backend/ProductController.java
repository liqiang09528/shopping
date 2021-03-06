package com.neuedu.controller.backend;

import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.RoleEnum;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.ProductWithBLOBs;
import com.neuedu.pojo.User;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manager/product/")
public class ProductController {

    @Autowired
    IProductService iProductService;
    /**
     * 商品更新&添加
     * */
    @RequestMapping("save")
    public ServerResponse addOrUpdadte(ProductWithBLOBs product, HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"请登录");
        if (user.getRole()== RoleEnum.ROLE_USER.getRole())
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足");
        return iProductService.addOrUpdadte(product);
    }

    /**
     * 搜索
     * */
    @RequestMapping("search")
    public ServerResponse search(@RequestParam(name = "id",required = false)String id,
                                 @RequestParam(name = "name",required = false)String name,
                                 @RequestParam(name = "pageNum",required = false,defaultValue = "1")int pageNum,
                                 @RequestParam(name = "pageSize",required = false,defaultValue = "10")int pageSize,
                                 HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"请登录");
        if (user.getRole()== RoleEnum.ROLE_USER.getRole())
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足");
        return iProductService.search(id,name,pageNum,pageSize);
    }

    /**
     * 商品详情
     * */
    @RequestMapping("/{id}")
    public ServerResponse detail(@PathVariable("id") Integer id,HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"请登录");
        if (user.getRole()== RoleEnum.ROLE_USER.getRole())
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足");

        return iProductService.detail(id);
    };
}
