package com.neuedu.controller;

import com.neuedu.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;



@PropertySource(value = "classpath:application.yml") //自定义属性：路径、
@RestController//返回json
public class Login {

//    @Value("${limit.minaaa}")
//    private BigDecimal min;
//    @Value("${limit.maxaaa}")
//    private BigDecimal max;
//
//    @Autowired
//    IUserDao iUserDao;
//    @RequestMapping(value = "/login")//url
//    public User login(String username, String password){
//
//        System.out.println(username+password);
//        User a = iUserDao.findByUsernameAndPassword(username, password);
//        return a;
//    }

}
