package com.neuedu.controller.front;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.result.Result;
import com.google.common.collect.Maps;
import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.PageModel;
import com.neuedu.pojo.User;
import com.neuedu.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/order/")
public class OrderController {

    @Autowired
    IOrderService iOrderService;
    @Autowired
    PageModel pageModel;
    /**
     * 创建订单
     * */
    @RequestMapping("createOrder")
    public ServerResponse createOrder(Integer shippingId,
            HttpSession session){

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"请登录");
        ServerResponse serverResponse = iOrderService.createOrder(user.getId(), shippingId);
        Object a = serverResponse.getData();
        System.out.println(a);
        return serverResponse;

    }

    /**
     * 查询订单
     * */
    @RequestMapping("selectOrder")
    public ServerResponse selectOrder(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"请登录");

        return iOrderService.selectOrdedr(user.getId());
    }

    /**
     * 支付接口
     * */
    @RequestMapping("pay")
    public ServerResponse pay(Long orderNo,HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"请登录");

        return iOrderService.pay(user.getId(),orderNo);
    }


    /**
     * 回调是否支付
     * */
    @RequestMapping("isPay")
    public ServerResponse isPay(Long orderNo,HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"请登录");

        return iOrderService.isPay(orderNo);
    }

    /**
     * 支付宝服务器回调商家接口
     * */
    @RequestMapping("callback")
    public String alipay_callback(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String,String> signMap= Maps.newHashMap();
        Iterator<String> keySetIt = parameterMap.keySet().iterator();
        while (keySetIt.hasNext()){
            String key=keySetIt.next();
            String[] values = parameterMap.get(key);
            StringBuffer stringBuffer=new StringBuffer();
            if (values!=null&&values.length>0) {
                for (String value : values) {
                    stringBuffer.append(value + ",");
                }
            }
            stringBuffer.deleteCharAt(stringBuffer.length()-1);
            //System.out.println(stringBuffer);
            signMap.put(key,stringBuffer.toString());
        }

        //验签
        try {
            signMap.remove("sign_type");
            boolean result = AlipaySignature.rsaCheckV2(signMap, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if (result){

                return iOrderService.callback(signMap);
                //System.out.println("通过");
            }else {
                return "fail";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return "success";
    }
}
