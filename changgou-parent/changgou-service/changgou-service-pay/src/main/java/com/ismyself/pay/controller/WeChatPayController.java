package com.ismyself.pay.controller;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import com.ismyself.pay.service.WeChatPayService;
import entity.Result;
import entity.StatusCode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * package com.ismyself.pay.controller;
 *
 * @auther txw
 * @create 2019-09-10  15:30
 * @description：
 */
@RestController
@RequestMapping("/weixin/pay")
public class WeChatPayController {

    @Autowired
    private WeChatPayService weChatPayService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 创建二维码
     *
     * @param parameterMap
     * @return
     */
    @RequestMapping("/create/native")
    public Result createNative(@RequestParam Map<String,String> parameterMap) {
        Map resultMap = weChatPayService.createNative(parameterMap);
        return new Result(true, StatusCode.OK, "创建二维码成功", resultMap);
    }

    /**
     * 根据商户订单号查询支付结果
     *
     * @param outtradeno
     * @return
     */
    @GetMapping("/status/query")
    public Result<Map<String,String>> queryStatus(String outtradeno) {
        Map<String,String> resultMap = weChatPayService.queryPayStatus(outtradeno);
        return new Result<Map<String,String>>(true, StatusCode.OK, "查询结果成功", resultMap);
    }

    /**
     * 支付回调
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/notify/url")
    public String notifyUrl(HttpServletRequest request) {
        try {
            //返回结果的数据的输入流
            InputStream is = request.getInputStream();
            //输出流
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            //转换
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
            os.close();
            is.close();
            //获取字节数组
            byte[] resultBytes = os.toByteArray();
            //将字节数组转换成字符串
            String resultStr = new String(resultBytes, "utf-8");
            //转换成map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(resultStr);
//            System.out.println(resultMap);

            //获取交换机和队列
            String attach = resultMap.get("attach");
            Map<String,String> attachMap = JSON.parseObject(attach, Map.class);
            String exchage = attachMap.get("exchage");
            String routingkey = attachMap.get("routingkey");

            //将结果给rabbitMQ队列消息
            rabbitTemplate.convertAndSend(exchage, routingkey, JSON.toJSONString(resultMap));
            //返回给腾讯服务器的结果
            String codeMsg = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            return codeMsg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除订单
     *
     * @param outtradeno
     * @return
     */
    @GetMapping("/status/close")
    public Result<Map> closeOrder(String outtradeno) {
        Map resultMap = weChatPayService.closeOrder(outtradeno);
        return new Result<Map>(true, StatusCode.OK, "删除订单成功", resultMap);
    }

}
