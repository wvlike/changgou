package com.ismyself.seckill.mq.listener;

import com.alibaba.fastjson.JSON;
import com.ismyself.pay.feign.WeChatPayFeign;
import com.ismyself.seckill.dao.SeckillGoodsMapper;
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
 * @create 2019-09-14  17:15
 * @description：
 */
@Component
@RabbitListener(queues = "${mq.pay.queue.seckillorder}")
public class SeckillMessageListener {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private WeChatPayFeign weChatPayFeign;

    @RabbitHandler
    public void getMessage(String msg) {
        Map<String, String> resultMap = JSON.parseObject(msg, Map.class);
        String return_code = resultMap.get("return_code");
        if ("SUCCESS".equals(return_code)) {
            //返回状态码
            String result_code = resultMap.get("result_code");
            //商户订单号
            String out_trade_no = resultMap.get("out_trade_no");
            //支付完成时间
            String time_end = resultMap.get("time_end");
            //获取用户名
            String attach = resultMap.get("attach");
            Map<String,String> attachMap = JSON.parseObject(attach, Map.class);
            String username = attachMap.get("username");
            //流水号
            String transaction_id = resultMap.get("transaction_id");
            //业务结果
            if ("SUCCESS".equals(result_code)) {
                //支付成功,更新订单
                seckillOrderService.updateSeckill(username,time_end,transaction_id);

                //更新redis状态
                SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundHashOps("UserQueueStatus").get(username);
                seckillStatus.setStatus(5);
                redisTemplate.boundHashOps("UserQueueStatus").put(username,seckillStatus);

            } else {
                //支付失败
                //关闭微信
                Result<Map> result = weChatPayFeign.closeOrder(out_trade_no);
                Map<String,String> data = result.getData();
                //不是成功
                if (!data.get("trade_state").equals("SUCCESS")){
                    //删除订单
                    seckillOrderService.deleteSeckill(username);
                }
                //否则还是按成功算
            }
        }
    }




}
