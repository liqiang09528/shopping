package com.neuedu.service.Impl;

import com.google.common.collect.Lists;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CartMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.ProductWithBLOBs;
import com.neuedu.service.ICartService;
import com.neuedu.service.IProductService;
import com.neuedu.utilss.BigDecimalUtils;
import com.neuedu.vo.CartProductVO;
import com.neuedu.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;

@Service
public class CartserviceImpl implements ICartService {

    @Autowired
    IProductService iProductService;
    @Autowired
    CartMapper cartMapper;
    @Value("${business.imageHost}")
    private String imageHost;


    @Override
    /**
     * 添加商品到购物车
     * */
    public ServerResponse addCart(Integer userId, Integer productId, Integer count) {
        //step1：非空校验
        if (productId==null||count==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数不为空");
        //step2：判断商品是否存在
        ServerResponse<ProductWithBLOBs> product = iProductService.findProductById(productId);

        if (!product.isSuccess()||product.getData().getStock()<=0)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品不存在或已经卖完");
        //step3：判断商品是否在购物车中

        Cart cart = cartMapper.findCartByUserIdAndProductId(userId, productId);
        if (cart==null){
            cart=new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            //bug
            cart.setQuantity(count);
            cart.setChecked(1);
            int result = cartMapper.insertSelective(cart);
            if (result<=0)
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"添加失败");
        }else {
            cart.setQuantity(cart.getQuantity()+count);
            int result = cartMapper.updateByPrimaryKeySelective(cart);
            if (result<=0)
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"添加失败");
        }
        //step4：封装CartVO
        CartVO cartVO = getCartVO(userId);
        return ServerResponse.serverResponseBySuccess(cartVO);
    }

    /**
     * 通过userId查找该用户选中的商品
     * */
    @Override
    public ServerResponse<List<Cart>> findCartByUserIdAndCheked(Integer userId) {
        List<Cart> cartList = cartMapper.findCartByUserIdAndChecked(userId);


        return ServerResponse.serverResponseBySuccess(cartList);
    }

    /**
     * 清空已下单的商品
     * */
    @Override
    public ServerResponse deleteBatch(List<Cart> cartList) {
        if (cartList==null||cartList.size()==0)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"要删除的购物车商品为零");
        Integer result = cartMapper.deleteBatch(cartList);
        if (result!=cartList.size())
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"部分商品没有删除");
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse selectCartByUserId(Integer userId) {
        CartVO cartVO = getCartVO(userId);
        return ServerResponse.serverResponseBySuccess(cartVO);
    }

    /**
     * 增加减少购物车商品数量&删除商品
     * */
    @Override
    public ServerResponse addAndReduceAndDelete(Integer id, Integer symbol, Integer quantity) {

        if (id==null||symbol==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数不能为空");
        if(symbol==-2){
            //删除
            int a = cartMapper.deleteByPrimaryKey(id);
            if (a<=0){
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"删除失败");
            }
            return ServerResponse.serverResponseBySuccess();
        }
        Integer a = cartMapper.addAndReduce(id, symbol);
        if (a==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"增加减少失败");
        return ServerResponse.serverResponseBySuccess();
    }

    /**
     * select_one
     * */
    @Override
    public ServerResponse select_one(Integer id,Integer checked) {

        Cart cart=new Cart();
        cart.setId(id);
        cart.setChecked(checked);
        int a = cartMapper.updateByPrimaryKeySelective(cart);
        if (a<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"选择失败");
        }
        return ServerResponse.serverResponseBySuccess();
    }

    private CartVO getCartVO(Integer userId){

        CartVO cartVO=new CartVO();
        //step1:根据userid查询该用户购物车信息
        List<Cart> cartList = cartMapper.findCartsByUserId(userId);
        if (cartList==null||cartList.size()==0)
            return cartVO;
        //总价格
        BigDecimal cartTotalPrice=new BigDecimal("0");
        //step2：list<cart>-->list<CartProductVO>

        List<CartProductVO> cartProductVOList= Lists.newArrayList();
        int limit_quantity=0;
        String limitQuantity;
        for (Cart cart:cartList){
            CartProductVO cartProductVO=new CartProductVO();
            cartProductVO.setId(cart.getId());
            cartProductVO.setUserId(cart.getUserId());
            cartProductVO.setProductId(cart.getProductId());
            cartProductVO.setQuantity(cart.getQuantity());
            ServerResponse<ProductWithBLOBs> serverResponse = iProductService.findProductById(cart.getProductId());
            if (serverResponse.isSuccess()){
                ProductWithBLOBs product = serverResponse.getData();
                if (product.getStock()>=cart.getQuantity()){
                    limit_quantity=cart.getQuantity();
                    limitQuantity="LIMIT_SUM_SUCCESS";
                }else {
                    limit_quantity=product.getStock();
                    limitQuantity="LIMIT_SUM_FAIL";
                }
                cartProductVO.setImageHost(imageHost);
                cartProductVO.setProductName(product.getName());
                cartProductVO.setProductSubtitle(product.getSubtitle());
                cartProductVO.setProductMainImage(product.getMainImage());
                cartProductVO.setProductPrice(product.getPrice());
                cartProductVO.setProductStatus(product.getStatus());
                cartProductVO.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(), cart.getQuantity()*1.0));
                cartProductVO.setProductStock(product.getStock());
                cartProductVO.setProductChecked(cart.getChecked());
                cartProductVO.setQuantity(limit_quantity);
                cartProductVO.setLimitQuantity(limitQuantity);
                cartProductVOList.add(cartProductVO);
                if (cart.getChecked()==1)
                    cartTotalPrice=BigDecimalUtils.add(cartTotalPrice.doubleValue(),cartProductVO.getProductTotalPrice().doubleValue());

            }
        }

        //step3:计算购物车总价格
         cartVO.setCarttotalprice(cartTotalPrice);
        //step4：判断是否全选
        Integer isAllChecked = cartMapper.isAllChecked(userId);

        if (isAllChecked==0)

            cartVO.setIsallchecked(true);
        else
            cartVO.setIsallchecked(false);
        //step5：构建cartVO
        cartVO.setCartProductVOList(cartProductVOList);

        return cartVO;
    }
}
