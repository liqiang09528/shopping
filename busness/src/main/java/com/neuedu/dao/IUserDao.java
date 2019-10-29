package com.neuedu.dao;

import com.neuedu.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserDao {

    //判断用户是否存在
    public int isExistsUsername(@Param("username") String username);


    //判断用户名和密码是否正确
    public User findByUsernameAndPassword(@Param("username") String username,
                                          @Param("password") String password);

    //注册
    public void UserRegister(User user);

    public void mapSelect();
}
