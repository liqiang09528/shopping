package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Cart;

import java.util.List;

public interface ICartService {


    /**
     * 添加商品到购物车
     * */
    public ServerResponse addCart(Integer userId,Integer productId,Integer count);

    /**
     * 根据用户Id查看用户已选择的商品
     * */
    public ServerResponse<List<Cart>> findCartByUserIdAndCheked(Integer userId);

    /**
     * 清空已下单的商品
     * */
    public ServerResponse deleteBatch(List<Cart> cartList);

    /**
     * 查看用户购物车
     * */
    public ServerResponse selectCartByUserId(Integer userId);
    /**
     * 增加减少购物车商品数量&删除商品
     * */
    public ServerResponse addAndReduceAndDelete(Integer id,Integer symbol,Integer quantity);


    /**
     * select_one
     * */
    public ServerResponse select_one(Integer id,Integer checked);
}
