package com.neuedu.pojo;


import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Data
@Component
public class User {



    private  String id;
    private  String username;

    private  String password;

    private  String email;

    private  String phone;

    private  String question;

    private  String answer;

    private  String role;

    private  String create_time;

    private  String update_time;

    private  String ip;

//    @Autowired //bytype
//    @Qualifier("order") //mingcheng

   // @Resource(name = "orderinfd")//byname

    private Cart cart;
    public User(){}

    public User(String id, String username, String password, String email, String phone, String question, String answer, String role, String create_time, String update_time,String ip) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.question = question;
        this.answer = answer;
        this.role = role;
        this.create_time = create_time;
        this.update_time = update_time;
        this.ip=ip;
    }

    @PostConstruct
    public void init(){
        System.out.println("==init==");
    }
    @PreDestroy
    public void destory(){
        System.out.println("==destory==");
    }


}
