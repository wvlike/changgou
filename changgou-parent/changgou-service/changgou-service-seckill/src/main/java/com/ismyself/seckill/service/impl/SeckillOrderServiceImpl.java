package com.ismyself.seckill.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ismyself.seckill.dao.SeckillGoodsMapper;
import com.ismyself.seckill.dao.SeckillOrderMapper;
import com.ismyself.seckill.pojo.SeckillGoods;
import com.ismyself.seckill.pojo.SeckillOrder;
import com.ismyself.seckill.service.SeckillOrderService;
import com.ismyself.seckill.task.MultiThreadingCreateOrder;
import entity.DateUtil;
import entity.SeckillStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author:txw
 * @Description:SeckillOrder业务层接口实现类
 */
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private MultiThreadingCreateOrder multiThreadingCreateOrder;


    /**
     * 删除订单
     *
     * @param username
     */
    @Override
    public void deleteSeckill(String username) {
        //先删除秒杀
        redisTemplate.boundHashOps("SeckillOrder").delete(username);
        //先查询排队信息
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundHashOps("UserQueueStatus").get(username);
        //删除预约队列
        clearOrderQueue(username);
        String namespace = "SeckillGoods_" + seckillStatus.getTime();
        //获取该商品的秒杀剩余数量
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(namespace).get(seckillStatus.getGoodsId());
        if (seckillGoods == null) {
            //为空说明为最后一个
            seckillGoods = seckillGoodsMapper.selectByPrimaryKey(seckillStatus.getGoodsId());
            //更新数据库
            seckillGoods.setNum(1);
            seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
        } else {
            //更新redis队列即可
            seckillGoods.setNum(seckillGoods.getNum() + 1);
        }
        //更新redis
        redisTemplate.boundHashOps(namespace).put(seckillStatus.getGoodsId(), seckillGoods);

        //将商品队列存入redis
        redisTemplate.boundListOps("SeckillGoodsCountList_" + seckillGoods.getId()).leftPushAll(seckillStatus.getGoodsId());

    }

    //删除预约队列
    private void clearOrderQueue(String username) {
        //删除排队信息
        redisTemplate.boundHashOps("UserQueueCount").delete(username);
        //删除抢单状态
        redisTemplate.boundHashOps("UserQueueStatus").delete(username);
    }

    /**
     * 更新订单
     *
     * @param username
     * @param endTime
     * @param transactionId
     */
    @Override
    public void updateSeckill(String username, String endTime, String transactionId) {
        try {
            SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps("SeckillOrder").get(username);
            seckillOrder.setStatus("1");    //已支付
            DateFormat df = new SimpleDateFormat(DateUtil.PATTERN_YYYYMMDDHH);
            seckillOrder.setPayTime(df.parse(endTime));//支付时间
            seckillOrder.setTransactionId(transactionId);//流水号
            //更新redis订单
            redisTemplate.boundHashOps("SeckillOrder").put(username, seckillOrder);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /***
     * 抢单状态查询
     * @param username
     */
    @Override
    public SeckillStatus queryStatus(String username) {
        return (SeckillStatus) redisTemplate.boundHashOps("UserQueueStatus").get(username);
    }

    /**
     * 根据商品id、用户名、秒杀开始时间下单
     *
     * @param username
     * @param goodId
     * @param time
     * @return
     */
    @Override
    public Boolean orderSeckillGoods(String username, Long goodId, String time) {
        //防止秒杀商品重复排队，使用到redis的一个自增机制,返回自增的值
        //使用用户id作为key，因为用户id不可重复
        Long count = redisTemplate.boundHashOps("UserQueueCount").increment(username, 1);
        //若返回的值大于1，说明重排队
        if (count > 1) {
            //100表示重复排队
            throw new RuntimeException("100");
        }
        //排队信息封装
        SeckillStatus seckillStatus = new SeckillStatus(username, new Date(), 1, goodId, time);
        //将秒杀抢单信息存入到Redis中,这里采用List方式存储,List本身是一个队列
        redisTemplate.boundListOps("SeckillOrderQueue").leftPush(seckillStatus);

        //抢单状态存入redis
        redisTemplate.boundHashOps("UserQueueStatus").put(username, seckillStatus);
        //多线程操作排队
        multiThreadingCreateOrder.createOrder();
        return true;
    }


    /**
     * SeckillOrder条件+分页查询
     *
     * @param seckillOrder 查询条件
     * @param page         页码
     * @param size         页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<SeckillOrder> findPage(SeckillOrder seckillOrder, int page, int size) {
        //分页
        PageHelper.startPage(page, size);
        //搜索条件构建
        Example example = createExample(seckillOrder);
        //执行搜索
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectByExample(example));
    }

    /**
     * SeckillOrder分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<SeckillOrder> findPage(int page, int size) {
        //静态分页
        PageHelper.startPage(page, size);
        //分页查询
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectAll());
    }

    /**
     * SeckillOrder条件查询
     *
     * @param seckillOrder
     * @return
     */
    @Override
    public List<SeckillOrder> findList(SeckillOrder seckillOrder) {
        //构建查询条件
        Example example = createExample(seckillOrder);
        //根据构建的条件查询数据
        return seckillOrderMapper.selectByExample(example);
    }


    /**
     * SeckillOrder构建查询对象
     *
     * @param seckillOrder
     * @return
     */
    public Example createExample(SeckillOrder seckillOrder) {
        Example example = new Example(SeckillOrder.class);
        Example.Criteria criteria = example.createCriteria();
        if (seckillOrder != null) {
            // 主键
            if (!StringUtils.isEmpty(seckillOrder.getId())) {
                criteria.andEqualTo("id", seckillOrder.getId());
            }
            // 秒杀商品ID
            if (!StringUtils.isEmpty(seckillOrder.getSeckillId())) {
                criteria.andEqualTo("seckillId", seckillOrder.getSeckillId());
            }
            // 支付金额
            if (!StringUtils.isEmpty(seckillOrder.getMoney())) {
                criteria.andEqualTo("money", seckillOrder.getMoney());
            }
            // 用户
            if (!StringUtils.isEmpty(seckillOrder.getUserId())) {
                criteria.andEqualTo("userId", seckillOrder.getUserId());
            }
            // 创建时间
            if (!StringUtils.isEmpty(seckillOrder.getCreateTime())) {
                criteria.andEqualTo("createTime", seckillOrder.getCreateTime());
            }
            // 支付时间
            if (!StringUtils.isEmpty(seckillOrder.getPayTime())) {
                criteria.andEqualTo("payTime", seckillOrder.getPayTime());
            }
            // 状态，0未支付，1已支付
            if (!StringUtils.isEmpty(seckillOrder.getStatus())) {
                criteria.andEqualTo("status", seckillOrder.getStatus());
            }
            // 收货人地址
            if (!StringUtils.isEmpty(seckillOrder.getReceiverAddress())) {
                criteria.andEqualTo("receiverAddress", seckillOrder.getReceiverAddress());
            }
            // 收货人电话
            if (!StringUtils.isEmpty(seckillOrder.getReceiverMobile())) {
                criteria.andEqualTo("receiverMobile", seckillOrder.getReceiverMobile());
            }
            // 收货人
            if (!StringUtils.isEmpty(seckillOrder.getReceiver())) {
                criteria.andEqualTo("receiver", seckillOrder.getReceiver());
            }
            // 交易流水
            if (!StringUtils.isEmpty(seckillOrder.getTransactionId())) {
                criteria.andEqualTo("transactionId", seckillOrder.getTransactionId());
            }
        }
        return example;
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        seckillOrderMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改SeckillOrder
     *
     * @param seckillOrder
     */
    @Override
    public void update(SeckillOrder seckillOrder) {
        seckillOrderMapper.updateByPrimaryKey(seckillOrder);
    }

    /**
     * 增加SeckillOrder
     *
     * @param seckillOrder
     */
    @Override
    public void add(SeckillOrder seckillOrder) {
        seckillOrderMapper.insert(seckillOrder);
    }

    /**
     * 根据ID查询SeckillOrder
     *
     * @param id
     * @return
     */
    @Override
    public SeckillOrder findById(Long id) {
        return seckillOrderMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询SeckillOrder全部数据
     *
     * @return
     */
    @Override
    public List<SeckillOrder> findAll() {
        return seckillOrderMapper.selectAll();
    }
}
