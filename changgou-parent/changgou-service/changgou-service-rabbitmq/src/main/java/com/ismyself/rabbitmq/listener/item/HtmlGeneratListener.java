package com.ismyself.rabbitmq.listener.item;

import com.alibaba.fastjson.JSON;
import com.ismyself.item.feign.PageFeign;
import entity.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * package com.ismyself.rabbitmq.queue.item;
 *
 * @auther txw
 * @create 2019-09-05  15:12
 * @description：
 */
@Component
@RabbitListener(queues = "topic.queue.spu")//监听的消息的队列名
public class HtmlGeneratListener {

    @Autowired
    private PageFeign pageFeign;

    /**
     * 用于接收消息后执行的方法
     * @param msg
     */
    @RabbitHandler
    public void getInfo(String msg){
        Message message = JSON.parseObject(msg, Message.class);
        //当code=2，修改，就调用feign执行生成模板操作
        if (message.getCode() == 2){
            pageFeign.createHTML(Long.valueOf(message.getContent().toString()));
        }
    }

}
