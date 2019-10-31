package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.ProductWithBLOBs;


public interface IProductService {

    public ServerResponse addOrUpdadte(ProductWithBLOBs product);
}
