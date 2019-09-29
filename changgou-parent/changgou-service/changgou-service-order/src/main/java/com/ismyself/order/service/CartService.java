package com.ismyself.order.service;

import com.ismyself.order.pojo.OrderItem;

import java.util.List;

/**
 * package com.ismyself.order.service;
 *
 * @auther txw
 * @create 2019-09-07  19:14
 * @description：
 */
public interface CartService {

    /**
     * 添加购物车
     * @param num
     * @param id
     * @param username
     */
    void add(Integer num,Long id,String username);

    /**
     * 根据用户名查询OrderItem集合  商品
     * @param username
     * @return
     */
    List<OrderItem> list(String username);
}
