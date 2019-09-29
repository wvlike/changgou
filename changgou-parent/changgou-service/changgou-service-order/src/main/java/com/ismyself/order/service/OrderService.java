package com.ismyself.order.service;

import com.github.pagehelper.PageInfo;
import com.ismyself.order.pojo.Order;

import java.util.List;

/**
 * @Author:txw
 * @Description:Order业务层接口
 */
public interface OrderService {

    /**
     * 订单回滚
     * @param id
     */
    void rollback(String id);

    /**
     * 根据订单id修改订单支付状态
     * @param orderId
     * @param paytime       支付时间
     * @param transactionid 交易流水号
     */
    void updateStatus(String orderId,String paytime,String transactionid) ;

    /**
     * 逻辑删除订单，即修改删除状态
     * @param id
     */
    void deleteOrder(String id);

    /***
     * Order多条件分页查询
     * @param order
     * @param page
     * @param size
     * @return
     */
    PageInfo<Order> findPage(Order order, int page, int size);

    /***
     * Order分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Order> findPage(int page, int size);

    /***
     * Order多条件搜索方法
     * @param order
     * @return
     */
    List<Order> findList(Order order);

    /***
     * 删除Order
     * @param id
     */
    void delete(String id);

    /***
     * 修改Order数据
     * @param order
     */
    void update(Order order);

    /***
     * 新增Order
     * @param order
     * @return
     */
    long add(Order order);

    /**
     * 根据ID查询Order
     * @param id
     * @return
     */
     Order findById(String id);

    /***
     * 查询所有Order
     * @return
     */
    List<Order> findAll();
}
