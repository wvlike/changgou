package com.changgou.util;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * package com.changgou.util;
 *
 * @auther txw
 * @create 2019-09-07  21:23
 * @description：
 */
public class AdminToken {

    public static String adminToken(){
        //证书路径
        String key_location = "changgou.jks";
        //访问证书的密码
        String key_password = "changgou";
        //访问证书内容的密码
        String keypwd = "changgou";
        //证书别名
        String alias = "changgou";

        //获取证书路径
        ClassPathResource resource = new ClassPathResource(key_location);

        //创建秘钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource,key_password.toCharArray());

        //获取公钥和私钥
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keypwd.toCharArray());

        //获取私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        //自定义Payload
        Map<String,Object> map = new HashMap<>();
        //赋予权限
        map.put("authorities",new String[]{"admin","oauth"});

        //生成Jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(map), new RsaSigner(privateKey));

        return jwt.getEncoded();
    }
}
