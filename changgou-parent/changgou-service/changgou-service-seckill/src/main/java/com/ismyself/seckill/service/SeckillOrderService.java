package com.ismyself.seckill.service;

import com.github.pagehelper.PageInfo;
import com.ismyself.seckill.pojo.SeckillOrder;
import entity.SeckillStatus;

import java.util.List;

/**
 * @Author:txw
 * @Description:SeckillOrder业务层接口
 */
public interface SeckillOrderService {

    /**
     * 删除订单
     * @param username
     */
    void deleteSeckill(String username);


    /**
     * 更新订单
     * @param username
     * @param endTime
     * @param transactionId
     */
    void updateSeckill(String username, String endTime, String transactionId);

    /***
     * 抢单状态查询
     * @param username
     */
    SeckillStatus queryStatus(String username);

    /**
     * 根据商品id、用户名、秒杀开始时间下单
     *
     * @param username
     * @param goodId
     * @param time
     * @return
     */
    Boolean orderSeckillGoods(String username, Long goodId, String time);


    /***
     * SeckillOrder多条件分页查询
     * @param seckillOrder
     * @param page
     * @param size
     * @return
     */
    PageInfo<SeckillOrder> findPage(SeckillOrder seckillOrder, int page, int size);

    /***
     * SeckillOrder分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<SeckillOrder> findPage(int page, int size);

    /***
     * SeckillOrder多条件搜索方法
     * @param seckillOrder
     * @return
     */
    List<SeckillOrder> findList(SeckillOrder seckillOrder);

    /***
     * 删除SeckillOrder
     * @param id
     */
    void delete(Long id);

    /***
     * 修改SeckillOrder数据
     * @param seckillOrder
     */
    void update(SeckillOrder seckillOrder);

    /***
     * 新增SeckillOrder
     * @param seckillOrder
     */
    void add(SeckillOrder seckillOrder);

    /**
     * 根据ID查询SeckillOrder
     *
     * @param id
     * @return
     */
    SeckillOrder findById(Long id);

    /***
     * 查询所有SeckillOrder
     * @return
     */
    List<SeckillOrder> findAll();
}
