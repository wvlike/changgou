import entity.BCrypt;
import org.junit.Test;

/**
 * package com.ismyself.base64;
 *
 * @auther txw
 * @create 2019-09-04  20:06
 * @description：
 */
public class BCryptTest {

    @Test
    public void test(){
        String hashpw = BCrypt.hashpw("1234", BCrypt.gensalt());
        System.out.println(hashpw);
        System.out.println(0x80000000);
    }
}
