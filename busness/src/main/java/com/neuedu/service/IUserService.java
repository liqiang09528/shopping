package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

public interface IUserService {

    /**
     * 注册接口
     * */
    public ServerResponse register(User user);

    /**
     * 登录接口
     * */
    public ServerResponse login(String username,String password,int type);

    /**
     * 忘记密码
     * 根据username获取密保
     */

    public ServerResponse forget_get_question(@PathVariable("username") String username);
    /**
     * 忘记密码
     * 提交答案
     * */

    public ServerResponse forget_check_answer(String username,String question,String answer);
    /**
     * 忘记密码
     * 修改密码
     * */

    public ServerResponse forget_reset_password(String username,String newPassword,String chooke);

    /**
     * 登录状态下修改用户信息
     * */
    public ServerResponse update_information(User user);
    /**
     * 登录状态修改用户密码
     * */
    public ServerResponse reset_password(String username,String oldPassword,String newPassword);

}
