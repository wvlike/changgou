package com.ismyself.seckill.mq.listener;

import com.alibaba.fastjson.JSON;
import com.ismyself.pay.feign.WeChatPayFeign;
import com.ismyself.seckill.service.SeckillOrderService;
import entity.Result;
import entity.SeckillStatus;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * package com.ismyself.seckill.mq.listener;
 *
 * @auther txw
 * @create 2019-09-14  21:07
 * @description：
 */
@Component
@RabbitListener(queues = "${mq.pay.queue.seckillordertimer}")
public class SeckillDelayMessageListener {

    @Autowired
    private WeChatPayFeign weChatPayFeign;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitHandler
    public void getMessage(String msg) {
        SeckillStatus seckillStatus = JSON.parseObject(msg, SeckillStatus.class);
        //如果此时redis中没有用户排队信息，表明该订单已经处理，如果有用户排队信息，则表示用户
        //尚未完成支付，关闭订单【微信支付等】
        Object username = redisTemplate.boundHashOps("UserQueueStatus").get(seckillStatus.getUsername());
        if (username != null) {
            //商品id
            Long orderId = seckillStatus.getOrderId();
            //先调用微信服务器查询订单支付情况
            Result<Map<String, String>> result = weChatPayFeign.queryStatus(String.valueOf(orderId));
            Map<String, String> resultData = result.getData();
            String trade_state = resultData.get("trade_state");
            if (!"SUCCESS".equals(trade_state)) {
                //关闭微信支付
                weChatPayFeign.closeOrder(String.valueOf(orderId));
                //删除订单,回滚
                seckillOrderService.deleteSeckill(seckillStatus.getUsername());
            }
            //成功就同步数据库

        }
    }
}
