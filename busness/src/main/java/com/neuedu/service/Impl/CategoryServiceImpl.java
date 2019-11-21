package com.neuedu.service.Impl;

import com.google.common.collect.Sets;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.pojo.Category;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 添加品类
     * */
    @Override
    public ServerResponse addCategory(Category category) {
        if (category==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数不能为空");
        int result = categoryMapper.insert(category);
        if (result<=0)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"添加品类失败");

        return ServerResponse.serverResponseBySuccess();
    }

    /**
     * 修改品类
     * */
    @Override
    public ServerResponse updateCategory(Category category) {
        if (category == null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR, "参数不能为空");
        int result=categoryMapper.updateByPrimaryKeySelective(category);
        if (result<=0)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"修改品类失败");
        return ServerResponse.serverResponseBySuccess();

    }

    /**
     * 平级查询
     * */
        @Override
    public ServerResponse getCategoryById(Integer categoryId) {
        if (categoryId==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR, "参数不能为空");
            List<Category> categoryList = categoryMapper.selectCategoryById(categoryId);

            return ServerResponse.serverResponseBySuccess(categoryList,"成功");
    }

    /**
     * 递归查询
     * */
    @Override
    public ServerResponse deepCategory(Integer categoryId) {
        if (categoryId==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR, "参数不能为空");

        Set<Category> categorySet= Sets.newHashSet();
        Set<Category> categorySet1 = findAllChildCategory(categorySet, categoryId);
        Set<Integer> categoryIds=Sets.newHashSet();
        Iterator<Category> categorySetIt = categorySet1.iterator();
        while (categorySetIt.hasNext()){
            Category category = categorySetIt.next();
            categoryIds.add(category.getId());
        }
        return ServerResponse.serverResponseBySuccess(categoryIds,"成功");
    }

    @Override
    public ServerResponse<Category> selectCategory(Integer categoryId) {
        if (categoryId==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"不能为空");
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        return ServerResponse.serverResponseBySuccess(category);
    }

    public Set<Category> findAllChildCategory(Set<Category> categorySet,Integer categoryId){

        //查看categoryId的类别信息

        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if (category!=null)
            categorySet.add(category);
        List<Category> categorylist = categoryMapper.selectCategoryById(categoryId);
        if (categorylist!=null&&categorylist.size()>=0){
            for (Category category1:categorylist){
                findAllChildCategory(categorySet,category1.getId());
            }
        }
        return categorySet;
    }
}
