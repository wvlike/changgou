package com.ismyself.order.mq.listener;

import com.alibaba.fastjson.JSON;
import com.ismyself.order.service.OrderService;
import com.ismyself.pay.feign.WeChatPayFeign;
import entity.Result;
import interceptor.FeignInterceptor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * package com.ismyself.order.mq.queue;
 *
 * @auther txw
 * @create 2019-09-10  20:06
 * @description：
 */
@Component
@RabbitListener(queues = "orderListenerQueue")
public class DelayMessageListener {

    @Autowired
    private WeChatPayFeign weChatPayFeign;

    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void getDelayMessage(String msg) {
        //msg为订单号
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date()));
        //调用pay微服务，获取定订单支付状态
        Result<Map<String, String>> result = weChatPayFeign.queryStatus(msg);
        Map<String, String> dataMap = result.getData();
        //交易状态
        /*SUCCESS—支付成功
        REFUND—转入退款
        NOTPAY—未支付
        CLOSED—已关闭*/
        String trade_state = dataMap.get("trade_state");
        //支付时间
        String time_end = dataMap.get("time_end");
        //流水号
        String transaction_id = dataMap.get("transaction_id");

        if ("SUCCESS".equals(trade_state)) {
            //修改支付成功状态
            orderService.updateStatus(msg, time_end, transaction_id);

        } else {
            //关闭微信支付  msg为订单号
            weChatPayFeign.closeOrder(msg);
            //失败回滚订单
            orderService.rollback(msg);
        }
    }
}
