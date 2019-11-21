package com.neuedu.controller.front;


import com.neuedu.area.GetConnection;
import com.neuedu.common.AreaURL;
import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;
import com.neuedu.pojo.User;
import com.neuedu.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;

@RequestMapping("/shipping/")
@RestController
public class ShippingController {

    @Autowired
    IShippingService iShippingService;

    /**
     * 添加地址
     * */
    @RequestMapping("add")
    public ServerResponse add(Shipping shipping, HttpSession session){

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"请登录");
        shipping.setUserId(user.getId());

        return iShippingService.add(shipping);

    }

    /**
     * 根据地址查邮编
     * */
    @RequestMapping("selectPostByAddress")
    public String selectPostByAddress(Shipping shipping){

        String address=shipping.getReceiverProvince()+shipping.getReceiverCity()+shipping.getReceiverDistrict();
        System.out.println(address);
        return GetConnection.getConnection(AreaURL.AREAURL+address);
    }

    /**
     *根据用户名查询地址
     * */
    @RequestMapping("selectShippingByUserId")
    public ServerResponse selectShippingByUserId(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"请登录");
        return iShippingService.selectShippingByUserId(user.getId());
    }


    @RequestMapping("selectShippingByShippingId")
    public ServerResponse selectShippingByShippingId(Integer shippingId,HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"请登录");
        return iShippingService.findShippingById(shippingId);
    }

}
