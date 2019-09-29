package com.ismyself.canal.mq.send;

import com.alibaba.fastjson.JSON;
import entity.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * package com.ismyself.canal.mq.send;
 *
 * @auther txw
 * @create 2019-09-05  15:01
 * @description：
 * 用于发送消息
 */
@Component
public class TopicMessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送指定交换机和队列的消息
     * @param message
     */
    public void sendMessage(Message message){
        rabbitTemplate.convertAndSend(message.getExechange(),message.getRoutekey(), JSON.toJSONString(message));
    }

}
