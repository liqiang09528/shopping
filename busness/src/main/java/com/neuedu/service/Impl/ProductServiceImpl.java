package com.neuedu.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.ProductWithBLOBs;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IProductService;
import com.neuedu.utilss.DateUtils;
import com.neuedu.vo.ProductDetailVO;
import com.neuedu.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    ICategoryService iCategoryService;
    @Autowired
    ProductMapper productMapper;
    @Value("${business.imageHost}")
    private String imageHost;

    @Override
    public ServerResponse addOrUpdadte(ProductWithBLOBs product) {
        if (product==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数不能为空");
        //subimages 1.png,2.png,3.png
        //step2:设置商品的主图 sub_images --> 1.jpg,2.jpg,3.png
        String subImages=product.getSubImages();
        if(subImages!=null&&!subImages.equals("")){
            String[] subImageArr=subImages.split(",");
            if(subImageArr.length>0){
                //设置商品的主图
                product.setMainImage(subImageArr[0]);
            }
        }
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

    @Override
    public ServerResponse search(String id, String name, int pageNum, int pageSize) {
        if (name!=null)
            name="%"+name+"%";
        PageHelper.startPage(pageNum,pageSize);
        List<ProductWithBLOBs> productlist = productMapper.findProductByNameAndId(id, name);

        List<ProductListVO> productListVOList= Lists.newArrayList();
        //productlist-->productListVOList
        if (productlist!=null&&productlist.size()>0){
            for (ProductWithBLOBs productWithBLOBs:productlist){
                ProductListVO productListVO=assembleProductListVO(productWithBLOBs);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo=new PageInfo(productListVOList);

        return ServerResponse.serverResponseBySuccess(pageInfo);
    }

    @Override
    public ServerResponse detail(Integer id) {
        if (id==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品id不能为空");
        ProductWithBLOBs productWithBLOBs = productMapper.selectByPrimaryKey(id);
        if (productWithBLOBs==null)
            return ServerResponse.serverResponseBySuccess();
        //product-->VO
        ProductDetailVO productDetailVO=assembleProductDetailVO(productWithBLOBs);
        return ServerResponse.serverResponseBySuccess(productDetailVO);
    }

    @Override
    public ServerResponse<ProductWithBLOBs> findProductById(Integer productId) {
        if (productId==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品id不能为空");
        ProductWithBLOBs productWithBLOBs = productMapper.selectByPrimaryKey(productId);

        if (productWithBLOBs==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品不存在");
        //product-->VO

        return ServerResponse.serverResponseBySuccess(productWithBLOBs);
    }

    /**
     * 扣库存
     * */
    @Override
    public ServerResponse reduceStock(Integer productId, Integer stock) {
        int result = productMapper.reduceStock(productId, stock);
        if (result<=0)
            return ServerResponse.serverResponseBySuccess(ResponseCode.ERROR,"扣库存失败");
        return ServerResponse.serverResponseBySuccess();
    }

    /**
     * 热销商品
     * */
    @Override
    public ServerResponse isHot() {

        List<ProductWithBLOBs> productList = productMapper.findProductByIsHot();
        if (productList==null&&productList.size()<=0)
            return ServerResponse.serverResponseBySuccess(ResponseCode.ERROR,"没有热销商品");
        List<ProductDetailVO> productDetailVOList = Lists.newArrayList();
        for (ProductWithBLOBs productWithBLOBs:productList){
            ProductDetailVO productDetailVO=assembleProductDetailVO(productWithBLOBs);
            productDetailVOList.add(productDetailVO);
        }
        
        return ServerResponse.serverResponseBySuccess(productDetailVOList);
    }

    private ProductListVO assembleProductListVO(ProductWithBLOBs product){
        ProductListVO productListVO=new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());


        return  productListVO;
    }private ProductDetailVO assembleProductDetailVO(ProductWithBLOBs product){


        ProductDetailVO productDetailVO=new ProductDetailVO();
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setCreateTime(DateUtils.dateToStr(product.getCreateTime()));
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setImageHost(imageHost);
        productDetailVO.setName(product.getName());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setId(product.getId());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setUpdateTime(DateUtils.dateToStr(product.getUpdateTime()));
        // Category category= categoryMapper.selectByPrimaryKey(product.getCategoryId());
        ServerResponse<Category> serverResponse=iCategoryService.selectCategory(product.getCategoryId());
        Category category=serverResponse.getData();
        if(category!=null){
            productDetailVO.setParentCategoryId(category.getParentId());
        }
        return productDetailVO;
    }
}

