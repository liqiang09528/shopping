package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;

public interface IShippingService {

    /**
     * 添加&修改地址
     * */
    public ServerResponse add(Shipping shipping);

    /**
     * 根据id查地址详细信息
     * */
    public ServerResponse findShippingById(Integer shippingId);

    /**
     *根据用户名查询地址
     * */
    public ServerResponse selectShippingByUserId(Integer userId);


}
