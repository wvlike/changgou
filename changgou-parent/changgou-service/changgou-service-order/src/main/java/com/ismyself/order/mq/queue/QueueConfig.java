package com.ismyself.order.mq.queue;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * package com.ismyself.order.mq.queue;
 *
 * @auther txw
 * @create 2019-09-10  19:24
 * @description：
 */
@Configuration
public class QueueConfig {

    /**
     * 队列1
     * 用于演示操作
     */
    @Bean
    public Queue orderDelayQueue(){
        return QueueBuilder.durable("orderDelayQueue")
                .withArgument("x-dead-letter-exchange","orderListenerExchange")     // 消息超时进入死信队列，绑定死信队列交换机
                .withArgument("x-dead-letter-routing-key","orderListenerQueue")  // 消息超时进入死信队列，绑定指定的routing-key
                .build();
    }

    /**
     * 队列2
     * durable 是否持久化
     * @return
     */
    @Bean
    public Queue orderListenerQueue(){
        return new Queue("orderListenerQueue",true);
    }

    //交换机
    @Bean
    public Exchange orderListenerExchange(){
        return new DirectExchange("orderListenerExchange");
    }

    //绑定
    @Bean
    public Binding listenerBinding(Queue orderListenerQueue,Exchange orderListenerExchange){
        return BindingBuilder.bind(orderListenerQueue).to(orderListenerExchange).with("orderListenerQueue").noargs();
    }



/*    //队列1延时队列
    @Bean
    public Queue delayQueue(){
        return QueueBuilder.durable("orderDelayQueue")
                .withArgument("x-dead-letter-exchange","listenerExchange")
                .withArgument("x-dead-letter-routing-key","orderListenerQueue")
                .build();
    }

    //监听队列
    @Bean
    public Queue listenerQueue(){
        return new Queue("listenerQueue");
    }

    //交换机
    @Bean
    public Exchange listenerExchange(){
        return new DirectExchange("listenerExchange");
    }

    //绑定
    @Bean
    public Binding listenerBinding(Queue listenerQueue,Exchange listenerExchange){
        return BindingBuilder.bind(listenerQueue).to(listenerExchange).with("orderListenerQueue").noargs();
    }*/


}
