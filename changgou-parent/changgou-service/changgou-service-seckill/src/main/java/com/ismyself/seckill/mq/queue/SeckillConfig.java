package com.ismyself.seckill.mq.queue;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * package com.ismyself.seckill.mq.queue;
 *
 * @auther txw
 * @create 2019-09-14  17:05
 * @description：
 */
@Configuration
public class SeckillConfig {

    //监听队列
    @Value("${mq.pay.queue.seckillordertimer}")
    private String queueListener;

    //监听队列
    @Value("${mq.pay.queue.delayseckillordertimer}")
    private String queueDelay;

    //交换机
    @Value("${mq.pay.exchange.seckillorder}")
    private String delayExchange;

    //路由
    @Value("${mq.pay.routing.seckillkeytimer}")
    private String delayRoutingKey;


    //延时队列
    @Bean
    public Queue queueDelay(){
        return QueueBuilder.durable(queueDelay)
                .withArgument("x-dead-letter-exchange",delayExchange)       // 消息超时进入死信队列，绑定死信队列交换机
                .withArgument("x-dead-letter-routing-key",queueListener)    // 消息超时进入死信队列，绑定指定的routing-key
                .build();
    }

    //监听队列
    @Bean
    public Queue queueListener(){
        return new Queue(queueListener);
    }

    //交换机
    @Bean
    public Exchange seckillDelayExchange(){
        return new DirectExchange(delayExchange);
    }

    //绑定交换机
    @Bean
    public Binding bindingDelayExchageToListener(Queue queueListener,Exchange seckillDelayExchange){
        return BindingBuilder.bind(queueListener).to(seckillDelayExchange).with(delayRoutingKey).noargs();
    }

}
