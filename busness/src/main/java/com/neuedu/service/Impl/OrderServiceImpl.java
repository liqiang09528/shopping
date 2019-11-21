package com.neuedu.service.Impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.TradeStatus;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayMonitorService;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayMonitorServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeWithHBServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.google.common.collect.Lists;
import com.neuedu.alipay.Main;
import com.neuedu.common.*;
import com.neuedu.dao.OrderMapper;
import com.neuedu.dao.Order_itemMapper;
import com.neuedu.dao.PayinfoMapper;
import com.neuedu.pojo.*;
import com.neuedu.service.ICartService;
import com.neuedu.service.IOrderService;
import com.neuedu.service.IProductService;
import com.neuedu.service.IShippingService;
import com.neuedu.utilss.BigDecimalUtils;
import com.neuedu.utilss.DateUtils;
import com.neuedu.vo.OrderItemVO;
import com.neuedu.vo.OrderVO;
import com.neuedu.vo.PayVO;
import com.neuedu.vo.ShippingVO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.swing.plaf.PanelUI;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    ICartService iCartService;
    @Autowired
    IProductService iProductService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    Order_itemMapper order_itemMapper;
    @Autowired
    IShippingService iShippingService;
    @Autowired
    PayinfoMapper payinfoMapper;
    @Autowired
    PageModel pageModel;

    @Value("${business.imageHost}")
    private String imageHost;


    @Override
    public ServerResponse createOrder(Integer userId, Integer shippingId) {
        //step1:参数校验
        if (userId==null||shippingId==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数不为空");

        //判断shipping是否存在
        //step2：根据用户Id查购物车已选择的商品List<cart>
        ServerResponse<List<Cart>> cartServerResponse = iCartService.findCartByUserIdAndCheked(userId);
        List<Cart> cartList = cartServerResponse.getData();
        if (cartList==null||cartList.size()==0)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"购物车为空或未选中");

        //step3:list<cart>-->list<Order_item>
        ServerResponse order_itemServerResponse=getCartOrder_item(userId,cartList);
        if (!order_itemServerResponse.isSuccess())
            return order_itemServerResponse;
        List<Order_item> order_itemList = (List<Order_item>) order_itemServerResponse.getData();

        //step4:创建Order实体类并保存到DB--order
        ServerResponse orderServerResponse = createOrder(userId, shippingId, order_itemList);

        if (!orderServerResponse.isSuccess())
            return orderServerResponse;
        Order order=(Order) orderServerResponse.getData();

        //step5:保存List<OrderItem>--order_item
        for (Order_item order_item:order_itemList){

            order_item.setOrderNo(order.getOrderNo());
        }
        ServerResponse saveOder_itemsServerResponse=saveOder_items(order_itemList);
        if (!saveOder_itemsServerResponse.isSuccess())
            return saveOder_itemsServerResponse;

        //step6:扣库存
        reduceStock(order_itemList);

        //step7：清空购物车下单的商品
        ServerResponse cartListServerResponse = iCartService.deleteBatch(cartList);
        if (!cartListServerResponse.isSuccess())
            return cartListServerResponse;
        //step8：返回OrderVO


        return assembleOrderVO(order,order_itemList,shippingId);
    }

    @Override
    public ServerResponse pay(Integer userId, Long orderNo) {
        //非空校验
        if (orderNo==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"订单编号不能为空");
        //判断订单编号是否存在
        Order order = orderMapper.findOrderByOrderNo(orderNo);

        if (order==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"订单不存在");


        return pay(order);
    }

    @Override
    public String callback(Map<String, String> parameterMap) {
        //订单号
        String orderNo=parameterMap.get("out_trade_no");
        //流水号
        String trade_no=parameterMap.get("trade_no");
        //支付状态
        String trade_status=parameterMap.get("trade_status");
        //付款时间
        String payment_time=parameterMap.get("gmt_payment");

        //step2:根据订单号查寻订单
        Order order = orderMapper.findOrderByOrderNo(Long.parseLong(orderNo));
        if (order==null)
            return "fail";
        if (trade_status.equals("TRADE_SUCCESS")) {
            //支付成功
            //修改订单状态
            order.setStatus(OrderStatusEnum.ORDER_PAYED.getStatus());
            order.setPaymentTime(DateUtils.strToDate(payment_time));
            int result = orderMapper.updateByPrimaryKeySelective(order);
            if (result<=0)
                return "fail";
        }

        //添加支付记录
        Payinfo payinfo=new Payinfo();
        payinfo.setOrderNo(Long.parseLong(orderNo));
        payinfo.setUserId(order.getUserId());
        payinfo.setPayPlatform(PaymentEnum.PAYMENT_ONLINE.getStatus());
        payinfo.setPlatformNumber(trade_no);
        payinfo.setPlatformStatus(trade_status);

        int pay_result = payinfoMapper.insertSelective(payinfo);
        if (pay_result<=0)
            return "fail";
        return "success";
    }

    /**
     *根据userId查询订单
     * */
    @Override
    public ServerResponse selectOrdedr(Integer userId) {

        List<Order> orderList = orderMapper.findOrderByUserId(userId);
        if (orderList==null||orderList.size()==0)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"没有订单");


        List<OrderVO> orderVOList =Lists.newArrayList();
        for (Order order:orderList){
            Long orderNo = order.getOrderNo();
            Integer shippingId = order.getShippingId();
            List<Order_item> order_itemList = order_itemMapper.findOrderItemByOrderNo(orderNo);
            ServerResponse orderVOServerResponse = assembleOrderVO(order, order_itemList, shippingId);
            OrderVO orderVO = (OrderVO) orderVOServerResponse.getData();
            orderVOList.add(orderVO);
        }

        pageModel.setList(orderVOList);
        return ServerResponse.serverResponseBySuccess(pageModel);
//        return assembleOrderVO(order,order_itemList,shippingId);

    }


    /**
     * 轮询是否支付
     * */
    @Override
    public ServerResponse isPay(Long orderNo) {
        List<String> payinfoStatusList = payinfoMapper.isPay(orderNo);
        if (payinfoStatusList.contains("TRADE_SUCCESS"))
            return ServerResponse.serverResponseBySuccess("success");
        return ServerResponse.serverResponseBySuccess("noPay");
    }

    //step8：返回OrderVO
    private ServerResponse assembleOrderVO(Order order, List<Order_item> order_itemList, Integer shippingId){
        OrderVO orderVO=new OrderVO();
        List<OrderItemVO> orderItemVOList=Lists.newArrayList();
        for (Order_item order_item:order_itemList){
            OrderItemVO order_itemVO = assembleOrderItemVO(order_item);
            orderItemVOList.add(order_itemVO);
        }
        orderVO.setOrderItemVoList(orderItemVOList);
        orderVO.setImageHost(imageHost);
        ServerResponse<Shipping> shippingServerResponse = iShippingService.findShippingById(shippingId);
        if (!shippingServerResponse.isSuccess())
            return shippingServerResponse;

        Shipping shipping = shippingServerResponse.getData();
        orderVO.setShippingId(shipping.getUserId());
        ShippingVO shippingVO = assembleShippingVO(shipping);
        orderVO.setShippingVo(shippingVO);
        orderVO.setReceiverName(shipping.getReceiverName());

        orderVO.setStatus(order.getStatus());
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.codeOf(order.getStatus());
        if (orderStatusEnum!=null)
        orderVO.setStatusDesc(orderStatusEnum.getDesc());

        orderVO.setPaymentType(order.getPaymentType());
        PaymentEnum paymentEnum = PaymentEnum.codeOf(order.getPaymentType());
        if (paymentEnum!=null)
            orderVO.setPaymentTypeDesc(paymentEnum.getDesc());

        orderVO.setOrderNo(order.getOrderNo());
        orderVO.setPayment(order.getPayment());
        orderVO.setPostage(order.getPostage());
        return ServerResponse.serverResponseBySuccess(orderVO);
    }

    //step6:扣库存
    public ServerResponse reduceStock(List<Order_item> order_itemList){
        for (Order_item order_item:order_itemList){
            Integer productId = order_item.getProductId();
            ServerResponse<ProductWithBLOBs> productServerResponse = iProductService.findProductById(productId);
            ProductWithBLOBs product = productServerResponse.getData();
            Integer stock=product.getStock()-order_item.getQuantity();
            ServerResponse reduceStockServerResponse = iProductService.reduceStock(productId, stock);
            if (!reduceStockServerResponse.isSuccess())
                return reduceStockServerResponse;
        }
        return ServerResponse.serverResponseBySuccess();
    }

//    step5:保存List<OrderItem>--order_item
     private ServerResponse saveOder_items(List<Order_item> order_itemList){
         int result = order_itemMapper.insertBatch(order_itemList);
         if (result!=order_itemList.size())
             return ServerResponse.serverResponseByError(ResponseCode.ERROR,"有些订单详情插入失败");
         return ServerResponse.serverResponseBySuccess();
     }

//    创建Order实体类并保存到DB--order
    private ServerResponse createOrder(Integer userId,Integer shipping,List<Order_item> order_itemList){
        Order order=new Order();
        order.setUserId(userId);
        order.setShippingId(shipping);
        order.setOrderNo(generatorOrderNo());
        order.setPayment(getOrdedrTotalPrice(order_itemList));
        order.setPaymentType(PaymentEnum.PAYMENT_ONLINE.getStatus());
        order.setPostage(0);
        order.setStatus(OrderStatusEnum.ORDER_NO_PAY.getStatus());

        int result = orderMapper.insertSelective(order);
        if (result<=0)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"插入失败");
        return ServerResponse.serverResponseBySuccess(order);
    }

    /**
     * 生成订单号
     * */
    private Long generatorOrderNo(){
        return System.currentTimeMillis()+new Random().nextInt(100);
    }
    /**
     * 计算订单总价格
     * */
    private BigDecimal getOrdedrTotalPrice(List<Order_item> order_itemList){

        BigDecimal bigDecimal=new BigDecimal("0");
        for (Order_item order_item:order_itemList){
            bigDecimal=BigDecimalUtils.add(bigDecimal.doubleValue(),order_item.getTotalPrice().doubleValue());
        }
        return bigDecimal;
    }


    /**
     * 根据购物车中选中的商品List<Cart>生成List<Order_item>
     * */
    private ServerResponse getCartOrder_item(Integer userId,List<Cart> cartList){
        List<Order_item> order_itemList=Lists.newArrayList();
        for (Cart cart:cartList){
            Order_item order_item=new Order_item();
            order_item.setUserId(userId);
            ServerResponse<ProductWithBLOBs> productServerRespose=iProductService.findProductById(cart.getProductId());
            if (!productServerRespose.isSuccess())
                return productServerRespose;
            ProductWithBLOBs product = productServerRespose.getData();
            if (product==null)//没有
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"id为"+cart.getProductId()+"的商品不存在");
            if (product.getStatus()!= ProductStatusEnum.PRODUCT_SALE.getStatus())
                return ServerResponse.serverResponseBySuccess(ResponseCode.ERROR,"id为"+cart.getProductId()+"的商品已下架");
            if (product.getStock()<cart.getQuantity())
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"id为"+cart.getProductId()+"的商品库存不足");
            order_item.setQuantity(cart.getQuantity());
            order_item.setCurrentUnitPrice(product.getPrice());
            order_item.setProductId(cart.getProductId());
            order_item.setProductImage(product.getMainImage());
            order_item.setProductName(product.getName());
            order_item.setTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),cart.getQuantity().doubleValue()));
            order_itemList.add(order_item);
        }
        return ServerResponse.serverResponseBySuccess(order_itemList);

    }

    /**
     * order_item生成order_itemVO
     * */
    private OrderItemVO assembleOrderItemVO(Order_item order_item){

        OrderItemVO orderItemVO=new OrderItemVO();
        if(order_item!=null){

            orderItemVO.setQuantity(order_item.getQuantity());
            orderItemVO.setCreateTime(DateUtils.dateToStr(order_item.getCreateTime()));
            orderItemVO.setCurrentUnitPrice(order_item.getCurrentUnitPrice());
            orderItemVO.setOrderNo(order_item.getOrderNo());
            orderItemVO.setProductId(order_item.getProductId());
            orderItemVO.setProductImage(order_item.getProductImage());
            orderItemVO.setProductName(order_item.getProductName());
            orderItemVO.setTotalPrice(order_item.getTotalPrice());

        }

        return orderItemVO;
    }
    /**
     * shipping生成shippingVO
     * */
    private ShippingVO assembleShippingVO(Shipping shipping){
        ShippingVO shippingVO=new ShippingVO();

        if(shipping!=null){
            shippingVO.setReceiverAddress(shipping.getReceiverAddress());
            shippingVO.setReceiverCity(shipping.getReceiverCity());
            shippingVO.setReceiverDistrict(shipping.getReceiverDistrict());
            shippingVO.setReceiverMobile(shipping.getReceiverMobile());
            shippingVO.setReceiverName(shipping.getReceiverName());
            shippingVO.setReceiverPhone(shipping.getReceiverPhone());
            shippingVO.setReceiverProvince(shipping.getReceiverProvince());
            shippingVO.setReceiverZip(shipping.getReceiverZip());
        }
        return shippingVO;
    }

    // 测试当面付2.0生成支付二维码
    public ServerResponse pay(Order order) {
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = String.valueOf(order.getOrderNo());

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "【睿乐购】平台商品支付";

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "购买商品共"+order.getPayment()+"元";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        //根据orderNo查询订单明细
        List<Order_item> order_itemList = order_itemMapper.findOrderItemByOrderNo(order.getOrderNo());
        if (order_itemList==null||order_itemList.size()==0)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"没有可购买的商品");


        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
       for (Order_item order_item:order_itemList){
           GoodsDetail goods1 = GoodsDetail.newInstance(String.valueOf(order_item.getProductId()), order_item.getProductName(), order_item.getCurrentUnitPrice().intValue(),
                   order_item.getQuantity());

       }



        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                //支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setNotifyUrl("http://de8pqg.natappfree.cc/order/callback")
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                // 需要修改为运行机器上的路径
                String filePath = String.format("E:/Product/Shopping/image/qr-%s.png",
                        response.getOutTradeNo());
                log.info("filePath:" + filePath);
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
                PayVO payVO=new PayVO(order.getOrderNo(),"http://img.qiang.com:8089/"+"qr-"+response.getOutTradeNo()+".png");
                return ServerResponse.serverResponseBySuccess(payVO);

            case FAILED:
                log.error("支付宝预下单失败!!!");
                break;

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
        return ServerResponse.serverResponseByError();
    }
    private static Log log = LogFactory.getLog(Main.class);

    // 支付宝当面付2.0服务
    private static AlipayTradeService tradeService;

    // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
    private static AlipayTradeService   tradeWithHBService;

    // 支付宝交易保障接口服务，供测试接口api使用，请先阅读readme.txt
    private static AlipayMonitorService monitorService;

    static {
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

        // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
        tradeWithHBService = new AlipayTradeWithHBServiceImpl.ClientBuilder().build();

        /** 如果需要在程序中覆盖Configs提供的默认参数, 可以使用ClientBuilder类的setXXX方法修改默认参数 否则使用代码中的默认设置 */
        monitorService = new AlipayMonitorServiceImpl.ClientBuilder()
                .setGatewayUrl("http://mcloudmonitor.com/gateway.do").setCharset("GBK")
                .setFormat("json").build();
    }

    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }
}
