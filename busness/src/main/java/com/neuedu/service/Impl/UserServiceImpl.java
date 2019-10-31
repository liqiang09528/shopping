package com.neuedu.service.Impl;

import com.neuedu.common.ResponseCode;
import com.neuedu.common.RoleEnum;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserMapper;
import com.neuedu.pojo.User;
import com.neuedu.service.IUserService;
import com.neuedu.utilss.MD5Utils;
import com.neuedu.utilss.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public ServerResponse register(User user) {


        //step1:参数校验
        if (user==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL, "参数不能为空");
        }

        //step2：用户名是否存在

        int usernameResult = userMapper.isExistsUsername(user.getUsername());

        if (usernameResult>0){
            return ServerResponse.serverResponseByError(ResponseCode.USERNAME_EXISTS, "用户名已存在");
        }
        //stem3：邮箱是否存在

        int emailResult = userMapper.isExistsUsername(user.getUsername());

        if (emailResult>0){
            return ServerResponse.serverResponseByError(ResponseCode.EMAIL_EXISTS, "邮箱已存在");
        }
        //step4：MD5密码加密，设置用户角色
        user.setPassword(MD5Utils.getMD5Code(user.getPassword()));
        user.setRole(RoleEnum.ROLE_USER.getRole());
        //step5：注册
        int insertResult=userMapper.insert(user);
        if (insertResult<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR, "注册失败");
        }
        //step6：返回

        return ServerResponse.serverResponseBySuccess();




    }

    @Override
    public ServerResponse login(String username, String password,int type) {
        //step1:参数校验
        if (username==null||username.equals(""))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名不能为空");
        if (password==null||password.equals(""))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"密码不能为空");
        //step2:判段用户名是否存在
        int usernameResult = userMapper.isExistsUsername(username);
        if (usernameResult<=0)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名不存在");
        //step3:密码加密
        password=MD5Utils.getMD5Code(password);
        //step4:登录
        User user = userMapper.findUserByUsernameAndPassword(username, password);
        if (user==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"密码错误");
        if (type==0){//管理员
            if (user.getRole()==RoleEnum.ROLE_USER.getRole())
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"登录权限不足");
        }
         return ServerResponse.serverResponseBySuccess(user);
    }

    @Override
    public ServerResponse forget_get_question(String username) {
        //step1:非空校验
            if (username==null||username.equals(""))
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名不能为空");
        //step2：根据用户名进行查询
        String question = userMapper.forget_get_question(username);
        if (question==null||question.equals(""))
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"密保问题不存在");
        //step3：返回结果
        return ServerResponse.serverResponseBySuccess(question);
    }

    @Override
    public ServerResponse forget_check_answer(String username, String question, String answer) {
        //step1:非空校验
        if (username==null||username.equals(""))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名不能为空");
        if (question==null||question.equals(""))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"密保不能为空");
        if (answer==null||answer.equals(""))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"答案不能为空");
        //step2：校验答案
        int result = userMapper.forget_check_answer(username, question, answer);
        if (result<=0)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"答案错误");
        //step3:生成token
        String token= UUID.randomUUID().toString();
        TokenCache.set("username"+username,token);
        return ServerResponse.serverResponseBySuccess(token);
    }

    @Override
    public ServerResponse forget_reset_password(String username, String newPassword, String forgetToken) {
        //step1:非空校验
        if (username==null||username.equals(""))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名不能为空");
        if (newPassword==null||newPassword.equals(""))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"新密码不能为空");
        if (forgetToken==null||forgetToken.equals(""))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"chooke不能为空");
        //step2:是否修改的自己的账号
        String token = TokenCache.get("username" + username);
        if (token==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"不能修改别人的密码或者token已过期");

        if (!token.equals(forgetToken))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"无效的token");

        //step3:修改
        int result = userMapper.reset_password(username, MD5Utils.getMD5Code(newPassword));

        if (result<=0)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"密码修改失败");
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse update_information(User user) {
        //step1：参数非空校验
        if (user==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数不能为空");
        //step2:如果修改密码，则加密
        if (user.getPassword()!=null)
            user.setPassword(MD5Utils.getMD5Code(user.getPassword()));
        int result = userMapper.updateUserByActivate(user);
        if (result<=0)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"修改失败");
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse reset_password(String username,String oldPassword, String newPassword) {
        //step1:非空校验
        if (oldPassword==null||oldPassword.equals(""))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"旧密码不能为空");
        if (newPassword==null||newPassword.equals(""))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"新密码不能为空");
        //step2：验证旧密码是否正确
        User user = userMapper.findUserByUsernameAndPassword(username, MD5Utils.getMD5Code(oldPassword));
        if (user==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"密码错误");

        //step3：修改密码
        int result = userMapper.reset_password(username, MD5Utils.getMD5Code(newPassword));

        if (result<=0)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"密码修改失败");
        return ServerResponse.serverResponseBySuccess();
    }
}
