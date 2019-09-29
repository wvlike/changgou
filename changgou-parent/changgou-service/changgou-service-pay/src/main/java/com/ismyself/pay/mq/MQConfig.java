package com.ismyself.pay.mq;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * package com.ismyself.pay.mq;
 *
 * @auther txw
 * @create 2019-09-10  17:23
 * @description：
 */
@Configuration
public class MQConfig {

    /**
     * 服务配置文件中的对象
     */
    @Autowired
    private Environment env;

    //创建队列
    @Bean
    public Queue orderQueue(){
//        return new Queue("queue.order");
        return new Queue(env.getProperty("mq.pay.queue.order"));
    }
    //创建交换机
    @Bean
    public Exchange orderExchange(){
//        return new DirectExchange("exchange.order");
        return new DirectExchange(env.getProperty("mq.pay.exchange.order"));
    }

    //绑定队列到交换机
    @Bean
    public Binding orderBinding(Queue orderQueue, Exchange orderExchange){
//        return BindingBuilder.bind(orderQueue).to(orderExchange).with("queue.order").noargs();
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(env.getProperty("mq.pay.routing.key")).noargs();
    }




    //创建秒杀服务器的队列******************************************************************

    //创建队列
    @Bean
    public Queue scekillOrderQueue(){
        return new Queue(env.getProperty("mq.pay.queue.seckillorder"));
    }

    //创建交换机
    @Bean
    public Exchange scekillOrderExchange(){
        return new DirectExchange(env.getProperty("mq.pay.exchange.seckillorder"));
    }

    //绑定队列到交换机
    @Bean
    public Binding scekillOrderBinding(Queue scekillOrderQueue, Exchange scekillOrderExchange){
        return BindingBuilder.bind(scekillOrderQueue).to(scekillOrderExchange).with(env.getProperty("mq.pay.routing.seckillkey")).noargs();
    }

}
