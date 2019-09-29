package com.ismyself.seckill.task;

import com.alibaba.fastjson.JSON;
import com.ismyself.seckill.dao.SeckillGoodsMapper;
import com.ismyself.seckill.pojo.SeckillGoods;
import com.ismyself.seckill.pojo.SeckillOrder;
import entity.IdWorker;
import entity.SeckillStatus;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * package com.ismyself.seckill.task;
 *
 * @auther txw
 * @create 2019-09-12  17:37
 * @description：
 */
@Component
public class MultiThreadingCreateOrder {


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //监听队列
    @Value("${mq.pay.queue.delayseckillordertimer}")
    private String queueDelay;


    @Async
    public void createOrder() {
        try {
            Thread.sleep(10000);

            //从redis中取出商品
            SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundListOps("SeckillOrderQueue").rightPop();

            //没有该队列的商品
            if (seckillStatus == null) {
                return;
            }

            //从队列中取出商品队列
            Object ids = redisTemplate.boundListOps("SeckillGoodsCountList_" + seckillStatus.getGoodsId()).rightPop();
            if (ids == null) {
                //说明已经卖完了，清理队列
                clearOrderQueue(seckillStatus.getUsername());
                //返回
                return;
            }

            //时间区间
            String time = seckillStatus.getTime();
            //用户登录名
            String username = seckillStatus.getUsername();
            //用户抢购商品
            Long goodId = seckillStatus.getGoodsId();


            //先查询商品有没有
            SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("SeckillGoods_" + time).get(goodId);
            if (seckillGoods == null || seckillGoods.getStockCount() <= 0) {
                throw new RuntimeException("已售罄");
            }
            //创建秒杀订单
            SeckillOrder seckillOrder = new SeckillOrder();
            seckillOrder.setId(idWorker.nextId());
            seckillOrder.setSeckillId(seckillGoods.getId());
            seckillOrder.setMoney(seckillGoods.getCostPrice());
            seckillOrder.setUserId(username);
            seckillOrder.setCreateTime(new Date());
            seckillOrder.setStatus("0");
            //将其存入redis
            redisTemplate.boundHashOps("SeckillOrder").put(username, seckillOrder);
            //就将其数量更新减少一个
            seckillGoods.setNum(seckillGoods.getNum() - 1);

            //获取商品队列的数量
            Long size = redisTemplate.boundListOps("SeckillGoodsCountList_" + seckillStatus.getGoodsId()).size();

            //根据数量判断若是最后一个就将其删除，且存入mysql数据库
            //if (seckillGoods.getStockCount() <= 0) {
            if (size <= 0) {
                //同步数量
                seckillGoods.setStockCount(size.intValue());
                //同步数据到MySQL
                seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
                //删除该秒杀商品的详情数据
                redisTemplate.boundHashOps("SeckillGoods_" + time).delete(goodId);

            } else {
                //更新
                redisTemplate.boundHashOps("SeckillGoods_" + time).put(goodId, seckillGoods);
            }

            //更新下单状态
            seckillStatus.setOrderId(idWorker.nextId());
            seckillStatus.setMoney(Float.valueOf(seckillGoods.getCostPrice()));
            seckillStatus.setStatus(2);
            redisTemplate.boundHashOps("UserQueueStatus").put(username, seckillStatus);

            //秒杀成功，设置延时队列
            rabbitTemplate.convertAndSend(queueDelay, (Object) JSON.toJSONString(seckillStatus), new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    //延时时间
                    message.getMessageProperties().setExpiration("10000");
                    return message;
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除预约队列
    private void clearOrderQueue(String username) {
        //删除排队信息
        redisTemplate.boundHashOps("UserQueueCount").delete(username);
        //删除抢单状态
        redisTemplate.boundHashOps("UserQueueStatus").delete(username);
    }


}
