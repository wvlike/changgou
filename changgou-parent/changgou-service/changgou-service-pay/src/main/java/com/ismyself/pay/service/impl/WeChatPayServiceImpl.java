package com.ismyself.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import com.ismyself.pay.service.WeChatPayService;
import entity.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * package com.ismyself.pay.service.impl;
 *
 * @auther txw
 * @create 2019-09-09  21:46
 * @description：
 */
@Service
public class WeChatPayServiceImpl implements WeChatPayService {

    @Value("${weixin.appid}")
    private String appid;
    @Value("${weixin.partner}")
    private String partner;
    @Value("${weixin.partnerkey}")
    private String partnerkey;
    @Value("${weixin.notifyurl}")
    private String notifyurl;

    /**
     * 创建二维码
     *
     * outtradeno  客户端自定义订单编号
     * totalfee     交易金额，单位 分
     * @param parameterMap
     * @return
     */
    @Override
    public Map createNative(Map<String,String> parameterMap) {
        try {
            String outtradeno = parameterMap.get("outtradeno");
            String totalfee = parameterMap.get("totalfee");
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("appid", appid);                                //公众账号ID
            paramMap.put("mch_id", partner);                             //商户号
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());    //随机字符串
            paramMap.put("body", "涂酱商城");                            //商品描述
            paramMap.put("out_trade_no", outtradeno);                  //商户订单号
            paramMap.put("total_fee", totalfee);                        //标价金额
            paramMap.put("spbill_create_ip", "127.0.0.1");               //终端ip
            paramMap.put("notify_url", notifyurl);                       //通知地址
            paramMap.put("trade_type", "NATIVE");                        //交易类型

            //自定义属性
            String username = parameterMap.get("username");
            String exchage = parameterMap.get("exchage"); //交换机
            String routingkey = parameterMap.get("routingkey"); //路由

            Map<String,String> attachMap = new HashMap<>();
            attachMap.put("exchage",exchage);
            attachMap.put("routingkey",routingkey);
            if (!StringUtils.isEmpty(username)){
                attachMap.put("username",username);
            }
            //携带自定义参数
            paramMap.put("attach", JSON.toJSONString(attachMap));   //存入paramMap中

            //转成携带签名的Xml格式Str
            String paramStr = WXPayUtil.generateSignedXml(paramMap, partnerkey);
            //接口连接
            String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            //获取请求结果的map
            Map<String, String> resultMap = getHttpMap(paramStr, url);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询订单是否支付成功
     *
     * @param out_trade_no
     * @return
     */
    @Override
    public Map queryPayStatus(String out_trade_no) {
        try {
            //封装需要的数据
            Map<String,String> paramMap = new HashMap<>();
            paramMap.put("appid",appid);                //公众账号ID
            paramMap.put("mch_id",partner);             //商户号
            paramMap.put("out_trade_no",out_trade_no);  //商户订单号
            paramMap.put("nonce_str",WXPayUtil.generateNonceStr());//随机字符串
            //转成携带签名的Xml格式Str
            String paramStr = WXPayUtil.generateSignedXml(paramMap, partnerkey);
            //请求地址
            String url = "https://api.mch.weixin.qq.com/pay/orderquery";
            //获取请求结果的map
            Map<String, String> resultMap = getHttpMap(paramStr, url);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取请求结果的map
     *
     * @param paramStr
     * @param url
     * @return
     * @throws Exception
     */
    private Map<String, String> getHttpMap(String paramStr, String url) throws Exception {
        //创建HttpClient对象；
        HttpClient httpClient = new HttpClient(url);
        //是否为Https请求
        httpClient.setHttps(true);
        //设置提交的参数
        httpClient.setXmlParam(paramStr);
        //发送请求，post方式
        httpClient.post();
        //响应的结果
        String resultStr = httpClient.getContent();
        //转换成Map格式
        return WXPayUtil.xmlToMap(resultStr);
    }

    /**
     * 关闭订单
     *
     * @param out_trade_no
     * @return
     */
    @Override
    public Map closeOrder(String out_trade_no) {
        try {
            //封装需要的数据
            Map<String,String> paramMap = new HashMap<>();
            paramMap.put("appid",appid);                //公众账号ID
            paramMap.put("mch_id",partner);             //商户号
            paramMap.put("out_trade_no",out_trade_no);  //商户订单号
            paramMap.put("nonce_str",WXPayUtil.generateNonceStr());//随机字符串
            //转成携带签名的Xml格式Str
            String paramStr = WXPayUtil.generateSignedXml(paramMap, partnerkey);
            //关闭订单的url
            String url = "https://api.mch.weixin.qq.com/pay/closeorder";
            //获取请求结果的map
            Map<String, String> resultMap = getHttpMap(paramStr, url);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
