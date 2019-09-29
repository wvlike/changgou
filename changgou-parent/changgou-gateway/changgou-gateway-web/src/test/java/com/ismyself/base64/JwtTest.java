package com.ismyself.base64;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * package com.ismyself.base64;
 *
 * @auther txw
 * @create 2019-09-04  18:48
 * @description：
 */
public class JwtTest {

    /**
     * eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiJuaWNlIiwiaWF0IjoxNTY3NTk0NDAyfQ.clVoA4BpFT-cMitCk2ZyRvqsq2C5ETRlXtBNkJLsEYQ
     * 实现Jwt加密
     */
    @Test
    public void testCreateJwt(){
        Map<String,Object> map = new HashMap<>();
        map.put("name","笑笑");
        map.put("age",18);
        JwtBuilder builder = Jwts.builder()
                .setId("888")                       //设置唯一编号
                .setSubject("nice")                 //设置主题  可以为JSON数据
                .setIssuedAt(new Date())            //设置签发时间
                .setExpiration(new Date(new Date().getTime()+40000))     //设置过期时间
                .addClaims(map)                     //自定义内容
                .signWith(SignatureAlgorithm.HS256,"wvlike");   //设置签名，使用HS256算法，并设置SecretKey(字符串)
        System.out.println(builder.compact());

    }

    @Test
    public void testParseJwt(){
//        String compactJwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiJuaWNlIiwiaWF0IjoxNTY3NTk0NDAyfQ.clVoA4BpFT-cMitCk2ZyRvqsq2C5ETRlXtBNkJLsEYQ";
        String compactJwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiJuaWNlIiwiaWF0IjoxNTY3NTk1NTYyLCJleHAiOjE1Njc1OTU2MDIsIm5hbWUiOiLnrJHnrJEiLCJhZ2UiOjE4fQ.mdtL44jQ5MSo9vA3Sd5cXALTEbG85PyXjdmPa-GIgu4";
        Claims claims = Jwts.parser()
                .setSigningKey("wvlike")
                .parseClaimsJws(compactJwt)
                .getBody();
        System.out.println(claims);
    }
}
