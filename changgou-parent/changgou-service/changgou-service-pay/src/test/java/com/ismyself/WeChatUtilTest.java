package com.ismyself;

import com.github.wxpay.sdk.WXPayUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * package com.ismyself;
 *
 * @auther txw
 * @create 2019-09-10  14:54
 * @description：
 */
public class WeChatUtilTest {

    @Test
    public void testStr(){
        String str = WXPayUtil.generateNonceStr();
        System.out.println(str);
    }

    @Test
    public void testMapToXml() throws Exception {
        Map<String,String> map = new HashMap<>();
        map.put("id", "No.1");
        map.put("address","深圳");
        map.put("description","哈哈哈");
        String xmlStr = WXPayUtil.mapToXml(map);
        System.out.println(xmlStr);

        System.out.println("\n");
        String xmlKeyStr = WXPayUtil.generateSignedXml(map, "wvlike");
        System.out.println("加了秘钥之后： \n"+xmlKeyStr);

        Map<String, String> xmlKeyMap = WXPayUtil.xmlToMap(xmlKeyStr);
        System.out.println(xmlKeyMap);



    }

}
