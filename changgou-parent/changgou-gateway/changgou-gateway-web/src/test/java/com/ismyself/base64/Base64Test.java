package com.ismyself.base64;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * package com.ismyself.base64;
 *
 * @auther txw
 * @create 2019-09-04  18:36
 * @description：
 */
public class Base64Test {

    /**
     * Base64加密
     */
    @Test
    public void testEncoder() {
        String str = "123789456";//MTIzNzg5NDU2
        byte[] encode = Base64.getEncoder().encode(str.getBytes());
        String strEncode = null;
        try {
            strEncode = new String(encode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(strEncode);
    }

    /**
     * Base64解密
     */
    @Test
    public void testDncoder() {
        String strDncode = "MTIzNzg5NDU2";
        byte[] decode = Base64.getDecoder().decode(strDncode.getBytes());
        String strDecode = null;
        try {
            strDecode = new String(decode,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(strDecode);
    }
}
