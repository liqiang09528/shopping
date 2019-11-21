package com.neuedu.service.Impl;

import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingServiceImpl implements IShippingService {


    @Autowired
    ShippingMapper shippingMapper;
    @Override
    public ServerResponse add(Shipping shipping) {


        //step1:非空校验
        if (shipping==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数为空");
        //step2：添加&更新
        Integer shippingId=shipping.getId();
        if (shippingId==null) {
            int result = shippingMapper.insertSelective(shipping);
            if (result<=0)
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"添加地址失败");
            else
                return ServerResponse.serverResponseBySuccess(shipping.getId());
        }
        else{
            int result=shippingMapper.updateByPrimaryKeySelective(shipping);

            if (result<=0)
                 return ServerResponse.serverResponseByError(ResponseCode.ERROR,"跟新地址失败");
            else
               return ServerResponse.serverResponseBySuccess(shipping.getId());
        }

    }

    @Override
    public ServerResponse findShippingById(Integer shippingId) {
        if (shippingId==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"地址Id不能为空");
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        if (shipping==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"地址不存在");
        return ServerResponse.serverResponseBySuccess(shipping);
    }

    @Override
    public ServerResponse selectShippingByUserId(Integer userId) {

        List<Shipping> shippingList = shippingMapper.selectShippingByUserId(userId);
        return ServerResponse.serverResponseBySuccess(shippingList);
    }


}
