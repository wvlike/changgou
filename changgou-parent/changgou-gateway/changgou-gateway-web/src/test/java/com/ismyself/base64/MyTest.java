package com.ismyself.base64;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashSet;

/**
 * package com.ismyself.base64;
 *
 * @auther txw
 * @create 2019-09-20  9:26
 * @description：
 */
public class MyTest {

    @Test
    public void test01(){
        String s1 = "Programming";
        String s2 = new String("Programming");
        System.out.println(s2==s1);
        System.out.println(3&5);
        System.out.println(3|5);
        int length = s1.length();
        String[] arr = new String[]{};
        int length1 = arr.length;

        Integer a = 30;
        Integer b = 30;
        Integer c = new Integer(30);
        Integer d = 300;
        Integer e = 300;
        int f = 30;
        System.out.println(a == b);   //true
        System.out.println(a == c);   //false
        System.out.println(d == e);   //false
        System.out.println(c == f);   //true
        System.out.println(a == f);   //true

    }

    @Test
    public void test03(){
        LinkedHashSet<Integer> linkedHashSet = new LinkedHashSet<>();
        Collections.addAll(linkedHashSet,1,6,2,3,9,7,55,12,455);
        System.out.println(linkedHashSet);
//        Collections.sort(linkedHashSet);

    }
}
