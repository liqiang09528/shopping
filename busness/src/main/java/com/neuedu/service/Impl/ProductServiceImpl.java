package com.neuedu.service.Impl;

import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.ProductWithBLOBs;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    ProductMapper productMapper;
    @Override
    public ServerResponse addOrUpdadte(ProductWithBLOBs product) {
        if (product==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数不能为空");
        Integer productId = product.getId();
        if (productId==null){//添加
            int result=productMapper.insertSelective(product);
            if (result<=0)
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"添加失败");

        }else {//更新

            int result1 = productMapper.updateByPrimaryKeySelective(product);
            if (result1<=0)
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"修改失败");
        }
        return ServerResponse.serverResponseBySuccess();

    }
}
