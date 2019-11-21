package com.neuedu.controller.front;

import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.User;
import com.neuedu.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    ICartService iCartService;
    /**
     * 添加商品到购物车
     * */
    @RequestMapping("add/{productId}/{count}")
    public ServerResponse addCart(@PathVariable("productId") Integer productId,
                                  @PathVariable("count") Integer count, HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"请登录");
        return iCartService.addCart(user.getId(),productId,count);
    }

    /**
     * 用户查找购物车
     * */
    @RequestMapping("select")
    public ServerResponse selectCart(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"请登录");
        return iCartService.selectCartByUserId(user.getId());
    }

    /**
     * 购物车商品数量加减&删除
     * */
    @RequestMapping("rule")
    public ServerResponse addAndReduceAndDelete(@RequestParam("id") Integer id,
                                                @RequestParam("symbol") Integer symbol,
                                                @RequestParam(value = "quantity",defaultValue ="1") Integer quantity,
                                                HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"请登录");
        ServerResponse serverResponse = iCartService.addAndReduceAndDelete(id, symbol, quantity);
        if (!serverResponse.isSuccess())
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"失败");
        return this.selectCart(session);
    }

    /**
     * select_one
     * */
    @RequestMapping("select_one")
    public ServerResponse select_one(@RequestParam("id") Integer id,
                                     @RequestParam("checked") Integer checked,HttpSession session){
        ServerResponse serverResponse = iCartService.select_one(id,checked);
        if (!serverResponse.isSuccess())
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"失败");

        return this.selectCart(session);
    }

}
