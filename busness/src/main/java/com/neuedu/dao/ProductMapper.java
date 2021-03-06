package com.neuedu.dao;

import com.neuedu.pojo.Product;
import com.neuedu.pojo.ProductWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ProductMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated
     */
    int insert(ProductWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated
     */
    int insertSelective(ProductWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated
     */
    ProductWithBLOBs selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ProductWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated
     */
    int updateByPrimaryKeyWithBLOBs(ProductWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Product record);

    /**
     *根据商品id或名字查询商品
     * */
     List<ProductWithBLOBs> findProductByNameAndId(@Param("id") String id,
                                                 @Param("name") String name);
     /**
      * 扣库存
      * */
     int reduceStock(@Param("id") Integer productId,
                     @Param("stock") Integer stock);

     /**
      * 商品热销推荐
      * */
     List<ProductWithBLOBs> findProductByIsHot();
}