package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.ProductWithBLOBs;
import org.springframework.web.bind.annotation.RequestParam;


public interface IProductService {

    public ServerResponse addOrUpdadte(ProductWithBLOBs product);

    public ServerResponse search(String id,
                                 String name,
                                 int pageNum,
                                 int pageSize);

    public ServerResponse detail(Integer id);

    /**
     * 根据id查询商品信息（库存）
     * */
    public ServerResponse<ProductWithBLOBs> findProductById(Integer productId);



    /**
     * 扣库存
     * */
    public ServerResponse reduceStock(Integer productId,Integer stock);

    /**
     * 热销推荐商品
     * */
    public ServerResponse isHot();
}
