package com.changgou.wvlike;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.parameters.P;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.jwt.crypto.sign.RsaSigner;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * package com.changgou.wvlike;
 *
 * @auther txw
 * @create 2019-09-06  17:04
 * @description：
 */
public class CreateJwtTestDemo {

    @Test
    public void testCreateToken(){
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
        map.put("name","zhangsan");
        map.put("address","sz");
        map.put("age",18);

        //生成Jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(map),new RsaSigner(privateKey));

        System.out.println(jwt.getEncoded());
    }


    @Test
    public void testGet(){
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhZGRyZXNzIjoic3oiLCJuYW1lIjoiemhhbmdzYW4iLCJhZ2UiOjE4fQ.eF_fY6o9mOx4jsyOCZKAwmFYPR0MR_PkhDlk2DRZuehc6RfgcwJctwMeQ-PnqiDTDpXbV6nFXxQyxFI7raVLwzeh62bo9FZET5d8e-pZTa_ZZs6d7cGNQL2GrhrL0aN57LjvE6as3296nJMiErIezkQ6AdCWV1dewoGcDms50pYr-ZI-SGJ3onsG9zQ5BPctGahrFSR4k_YUnHgRm7FnzXa9LABOVAsUqhsgbcWPkiKlsDiOyp-AulviDXF-ukft5QXDRTuS0gJ-lgOe8fHOb2g3l0lRcrG1_KmfHQHqo5__hWOQHgNnUWY_B19na8TVWxwT5fmXliwA2KEQhyBZ-w";

        String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlbzTAOx6Y7IdD91ZnxZ/wHcUE2POhJSR7RrLaJiLU2MPwt71CuPRhu9bN2pMdTU/7Vfb5rIEqBSrvTDzxT3LMy1T3qiAsVmRwNv01g8HlHyUPbEHG7LvtpDoNJKNGco2WPRC3sVaRzD0OGxB+ti+tcBWKnUdvGkIeMCyQd8ec6bJHBdSJu1k64EwrE8cwU5YHIV9RoRhZW+NiZFP8WCMQ+QlcfsLE0Iru+2hKnMcAtI3KYgTcuJuaub1sL314JeccRfK9fILlj4Ncp0ZcXIahyfALEazVveyAS4ULg8LbdU3FXutYWhbgn9kJhoktAY6zhzdFC1bOa8Z5FXN8NqPowIDAQAB-----END PUBLIC KEY-----";

        Jwt jwt = JwtHelper.decodeAndVerify(token,new RsaVerifier(publicKey));

        System.out.println(jwt.getClaims());


    }


    @Test
    public void testDecode() throws UnsupportedEncodingException {
        String str = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9";
        byte[] decode = Base64.getDecoder().decode(str);
        System.out.println(new String(decode,"UTF-8"));
    }
}
