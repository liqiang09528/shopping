package com.neuedu.service;

import com.neuedu.common.ServerResponse;

import java.util.Map;

public interface IOrderService {

    /**
     * 创建订单
     * */
    public ServerResponse createOrder(Integer userId,Integer shippingId);

    /**
     * 支付
     * */
    public ServerResponse pay(Integer userId,Long orderNo);


    /**
     * 支付回调
     * */
    public String callback(Map<String,String> parameterMap);

    /**
     *查询订单
     * */
    public ServerResponse selectOrdedr(Integer userId);

    /**
     *轮询是否支付
     */
    public ServerResponse isPay(Long orderNo);
}
