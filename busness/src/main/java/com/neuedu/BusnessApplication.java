package com.neuedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@MapperScan("com.neuedu.dao")
@PropertySource(value = "classpath:application.yml") //自定义属性：路径、
public class BusnessApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusnessApplication.class, args);
    }

}
