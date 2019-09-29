package com.ismyself.order.mq.listener;

import com.alibaba.fastjson.JSON;
import com.ismyself.order.pojo.Order;
import com.ismyself.order.pojo.OrderItem;
import com.ismyself.order.service.OrderService;
import com.ismyself.pay.feign.WeChatPayFeign;
import entity.Result;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * package com.ismyself.order.mq.queue;
 *
 * @auther txw
 * @create 2019-09-10  17:37
 * @description：
 */
@Component
@RabbitListener(queues = "${mq.pay.queue.order}")
public class OrderMessageListener {

    @Autowired
    private OrderService orderService;

    @Autowired
    private WeChatPayFeign weChatPayFeign;


    /**
     * 用于监听订单结果
     *
     * @param msg
     */
    @RabbitHandler
    public void getMessage(String msg) {
        Map<String, String> resultMap = JSON.parseObject(msg, Map.class);
        String resultCode = resultMap.get("result_code");
        if (resultCode.equals("SUCCESS")) {
            //获取支付结果
            String result_code = resultMap.get("result_code");
            String outTradeNo = resultMap.get("out_trade_no");
            if ("SUCCESS".equals(result_code)) {
                //成功，就执行修改订单状态
                orderService.updateStatus(outTradeNo, resultMap.get("time_end"), resultMap.get("transaction_id"));

            } else {
                //关闭微信支付，即调用关闭微信支付接口
                Result<Map> result = weChatPayFeign.closeOrder(outTradeNo);
                Map<String, String> resultData = result.getData();
                //失败就删除订单，回库商品
                if (!"SUCCESS".equals(resultData.get("trade_state"))) {
                    //回滚订单
                    orderService.rollback(outTradeNo);
/*
                //逻辑删除订单
                Order order = orderMapper.selectByPrimaryKey(id);
                order.setIsDelete("1");
                orderMapper.updateByPrimaryKey(order);
                //根据订单id查询商品
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(id);
                List<OrderItem> orderItemList = orderItemMapper.select(orderItem);
                Map<String, Object> orderMap = new HashMap<>();
                for (OrderItem item : orderItemList) {
                    //将skuId与每个sku的数量添加到map中
                    orderMap.put(item.getSkuId().toString(),item.getNum().toString());
                }
                //调用goods微服务，商品增加
                skuFeign.iecrCount(orderMap);*/


                }

                //否则还是按成功算
            }
        }
    }

}
