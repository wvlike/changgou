import org.junit.Test;

/**
 * package PACKAGE_NAME;
 *
 * @auther txw
 * @create 2019-09-02  17:09
 * @description：
 */
public class MyTest {

    @Test
    public void test(){
        String str = "0-1000元";
        str = str.substring(0,str.indexOf("元"));
        System.out.println(str);
    }
}
