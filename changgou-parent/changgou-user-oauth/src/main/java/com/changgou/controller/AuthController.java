package com.changgou.controller;


import com.changgou.service.AuthService;
import com.changgou.util.AuthToken;
import com.changgou.util.CookieUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/*****
 * @Author: shenkunlin
 * @Date: 2019/7/7 16:42
 * @Description: com.changgou.oauth.controller
 ****/
@RestController
@RequestMapping(value = "/user")
public class AuthController {

    //客户端ID
    @Value("${auth.clientId}")
    private String clientId;

    //秘钥
    @Value("${auth.clientSecret}")
    private String clientSecret;


    @Autowired
    AuthService authService;

    /***
     * 账号密码登录
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login")
    public Result login(String username,String password) throws Exception{
        //调用userLoginService实现登录
        String grant_type = "password";
        AuthToken authToken = authService.login(username, password, clientId, clientSecret, grant_type);
        if(authToken!=null){
            return new Result(true, StatusCode.OK,"登录成功！",authToken);
        }
        return new Result(false,StatusCode.LOGINERROR,"登录失败！");
    }


}
