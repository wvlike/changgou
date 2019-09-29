package com.changgou.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * package com.changgou.controller;
 *
 * @auther txw
 * @create 2019-09-08  16:48
 * @description：
 */
@Controller
@RequestMapping(value = "/oauth")
public class LoginRedirect {

    /***
     * 跳转到登录页面
     * @return
     */
    @GetMapping(value = "/login")
    public String login(){
        return "login";
    }
}
