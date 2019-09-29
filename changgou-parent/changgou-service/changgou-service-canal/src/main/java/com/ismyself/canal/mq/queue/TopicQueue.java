package com.ismyself.canal.mq.queue;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * package com.ismyself.canal.mq.queue;
 *
 * @auther txw
 * @create 2019-09-05  14:37
 * @description：
 * 用于交换机和队列的绑定
 */
@Configuration
public class TopicQueue {

    public static final String TOPIC_QUEUE_SPU = "topic.queue.spu";
    public static final String TOPIC_EXCHANGE_SPU = "topic.exchange.spu";

    /**
     * 设置队列
     * @return
     */
    @Bean("topicQueueSpu")
    public Queue topicQueueSpu(){
        return new Queue(TOPIC_QUEUE_SPU);
    }

    /**
     * 设置交换机
     * @return
     */
    @Bean("topicExchange")
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE_SPU);
    }

    /**
     * 将队列绑定到交换机
     * @param topicQueueSpu
     * @param topicExchange
     * @return
     */
    @Bean
    public Binding topicBinding1(@Qualifier("topicQueueSpu") Queue topicQueueSpu, @Qualifier("topicExchange") Exchange topicExchange){
        return BindingBuilder.bind(topicQueueSpu).to(topicExchange).with(TOPIC_QUEUE_SPU).noargs();
    }

}
