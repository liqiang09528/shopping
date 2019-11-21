package com.neuedu.controller.front;

import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.User;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpSession;

@CrossOrigin
@RestController//返回json
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;
    /**
     * 登录
     * */
    @RequestMapping("/login/{username}/{password}")
    public ServerResponse login(@PathVariable("username") String username,
                                @PathVariable("password") String password,
                                HttpSession session){

        System.out.println(username+password);
        ServerResponse serverResponse=userService.login(username,password,1);
        if (serverResponse.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        }
        return serverResponse;
    }
    /**
     * 注册
     * */
    @RequestMapping("/register")
    public ServerResponse register(User user){

        System.out.println(user);
        return userService.register(user);
    }
    /**
     * 忘记密码
     * 根据username获取密保
     */
    @RequestMapping("/forget_get_question/{username}")
    public ServerResponse forget_get_question(@PathVariable("username") String username){
        return userService.forget_get_question(username);
    }
    /**
     * 忘记密码
     * 提交答案
     * */
    @RequestMapping("/forget_check_answer")
    public ServerResponse forget_check_answer(String username,String question,String answer){

        return userService.forget_check_answer(username,question,answer);

    }
    /**
     * 忘记密码
     * 修改密码
     * */
    @RequestMapping("/forget_reset_password")
    public ServerResponse forget_reset_password(String username,String newPassword,String forgetToken){

        return userService.forget_reset_password(username,newPassword,forgetToken);
    }
    /**
     * 登录状态下修改用户信息
     * */
    @RequestMapping("/update_information")
    public ServerResponse update_information(User user,HttpSession session){
        User loginUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (loginUser==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"未登录");
        user.setId(loginUser.getId());
        return userService.update_information(user);
    }
    /**
     * 登录状态下修改密码
     * */
    @RequestMapping("/reset_password")
    public ServerResponse reset_password(String oldPassword,String newPassword,HttpSession session){

        User loginUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (loginUser==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"未登录");


        return userService.reset_password(loginUser.getUsername(),oldPassword,newPassword);
    }
    @RequestMapping("/exit_login")
    public ServerResponse exit_login(HttpSession session){
        if (session!=null)
            session.removeAttribute(Const.CURRENT_USER);

            return ServerResponse.serverResponseBySuccess("退出成功");
    }


}
