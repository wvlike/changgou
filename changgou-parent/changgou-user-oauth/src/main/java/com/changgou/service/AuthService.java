package com.changgou.service;


import com.changgou.util.AuthToken;

/*****
 * @Author: shenkunlin
 * @Date: 2019/7/7 16:23
 * @Description: com.changgou.oauth.service
 ****/
public interface AuthService {

    /***
     * 授权认证方法
     */
    AuthToken login(String username, String password, String clientId, String clientSecret, String grant_type) throws Exception;

}
