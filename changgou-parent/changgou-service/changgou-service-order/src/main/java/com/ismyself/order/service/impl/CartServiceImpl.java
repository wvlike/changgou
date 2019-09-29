package com.ismyself.order.service.impl;

import com.ismyself.goods.feign.SkuFeign;
import com.ismyself.goods.feign.SpuFeign;
import com.ismyself.goods.pojo.Sku;
import com.ismyself.goods.pojo.Spu;
import com.ismyself.order.pojo.OrderItem;
import com.ismyself.order.service.CartService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * package com.ismyself.order.service.impl;
 *
 * @auther txw
 * @create 2019-09-07  19:15
 * @description：
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SpuFeign spuFeign;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 封装OrderItem对象
     * @param num
     * @param sku
     * @param spu
     * @return
     */
    public OrderItem sku2OrderItem(Integer num, Sku sku, Spu spu) {
        OrderItem orderItem = new OrderItem();
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        orderItem.setSpuId(spu.getId());
        orderItem.setSkuId(sku.getId());
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);
        orderItem.setMoney(num * sku.getPrice());
        orderItem.setImage(sku.getImage());
        orderItem.setWeight(num * sku.getWeight());
        return orderItem;
    }

    /**
     * 添加购物车
     * @param num
     * @param id
     * @param username
     */
    @Override
    public void add(Integer num, Long id, String username) {
        //当购物车的商品num<=0就删除该商品
        if(num <= 0){
            redisTemplate.boundHashOps("Cart_" + username).delete(id);
            //如果此时购物车数量为空，则连购物车一起移除
            Long size = redisTemplate.boundHashOps("Cart_" + username).size();
            if(size==null || size<=0){
                redisTemplate.delete("Cart_" + username);
            }
            return;
        }
        //根据商品id查询
        Result<Sku> skuResult = skuFeign.findById(id);
        if (skuResult!=null && skuResult.getData() != null) {
            Sku sku = skuResult.getData();
            Result<Spu> spuResult = spuFeign.findById(sku.getSpuId());
            Spu spu = spuResult.getData();

            //封装成OrderItem对象
            OrderItem orderItem = sku2OrderItem(num, sku, spu);

            //存入redis中，要求为hash，其Cart_username 唯一，即用户名唯一
            redisTemplate.boundHashOps("Cart_" + username).put(id, orderItem);
        }
    }


    /**
     * 根据用户名查询OrderItem集合  商品
     *
     * @param username
     * @return
     */
    @Override
    public List<OrderItem> list(String username) {
        //根据用户名查询
        return redisTemplate.boundHashOps("Cart_" + username).values();
    }
}
