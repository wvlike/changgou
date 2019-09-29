package com.ismyself.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ismyself.goods.feign.SkuFeign;
import com.ismyself.order.dao.OrderItemMapper;
import com.ismyself.order.dao.OrderMapper;
import com.ismyself.order.pojo.Order;
import com.ismyself.order.pojo.OrderItem;
import com.ismyself.order.service.OrderService;
import com.ismyself.user.feign.UserFeign;
import entity.IdWorker;
import entity.Result;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author:txw
 * @Description:Order业务层接口实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 增加Order
     * <p>
     * 收货人信息是前端传过来封装好了
     *
     * @param order
     * @return
     */
    @Override
    @GlobalTransactional
    public long add(Order order) {
        //预约添加订单orderMapper
        long code = idWorker.nextId();
        System.out.println(code);
        order.setId(String.valueOf(code)); //id
        List<OrderItem> orderItemList = new ArrayList<>();  //用来存储OrderItem
        //获取商品的ids
        List<Long> orderIds = order.getOrderIds();
        //循环ids

        for (Long id : orderIds) {
            //从redis中查询该id的商品，然后存入集合中
            OrderItem orderItem = (OrderItem) redisTemplate.boundHashOps("Cart_" + order.getUsername()).get(id);
            orderItemList.add(orderItem);
            //然后将该商品删除
            redisTemplate.boundHashOps("Cart_" + order.getUsername()).delete(id);
            //该商品的库存减一
        }
        int totalNum = 0;
        int totalMoney = 0;
        for (OrderItem orderItem : orderItemList) {
            totalNum += orderItem.getNum();
            totalMoney += orderItem.getMoney();
        }
        order.setTotalNum(totalNum);            //总数量
        order.setTotalMoney(totalMoney);          //总金额
        order.setPayMoney(totalMoney);            //实付金额
        order.setPayType("1");          //付款方式
        order.setCreateTime(new Date());//创建时间
        order.setUpdateTime(order.getCreateTime());//更新时间
        order.setBuyerRate("0");        //买家是否评价
        order.setSourceType("1");       //订单来源
        order.setOrderStatus("0");      //订单状态
        order.setPayStatus("0");        //订单支付状态
        order.setConsignStatus("0");    //订单是否发货
        order.setIsDelete("0");         //订单是否删除
        //保存订单
        orderMapper.insertSelective(order);

        Map<String, Object> decrmap = new HashMap<>();
        //预约添加该订单的商品orderItemMapper

        for (OrderItem orderItem : orderItemList) {
            orderItem.setId(String.valueOf(idWorker.nextId()));     //商品唯一id
            orderItem.setOrderId(order.getId());       //对应的订单id
            orderItem.setIsReturn("0");     //是否退货
            //保存该订单对应的商品
            orderItemMapper.insertSelective(orderItem);
            decrmap.put(String.valueOf(orderItem.getSkuId()), orderItem.getNum());

        }

        //调用商品库存递减
        Result result = skuFeign.decrCount(decrmap);
        System.out.println(result);

//        redisTemplate.boundValueOps("Sd").setIfAbsent();
//        redisTemplate.boundValueOps("ss").getAndSet();

        //给用户添加积分
        userFeign.addUserPoints(10);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date()));

        rabbitTemplate.convertAndSend("orderDelayQueue", (Object) order.getId(), new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置延时时间。即过了这个时间就进入私信队列
                message.getMessageProperties().setExpiration("10000");
                return message;
            }
        });
        return code;
    }

    /**
     * 订单回滚
     *
     * @param id
     */
    @Override
    public void rollback(String id) {
        //逻辑删除订单
        Order order = orderMapper.selectByPrimaryKey(id);
        order.setIsDelete("1");
        orderMapper.updateByPrimaryKey(order);
        //根据订单id查询商品
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(id);
        List<OrderItem> orderItemList = orderItemMapper.select(orderItem);
        Map<String, Object> orderMap = new HashMap<>();
        for (OrderItem item : orderItemList) {
            //将skuId与每个sku的数量添加到map中
            orderMap.put(item.getSkuId().toString(), item.getNum().toString());
        }
        //调用goods微服务，商品增加
        skuFeign.iecrCount(orderMap);
        //用户微服务积分回滚
//        userFeign.addUserPoints(-10);
    }

    /**
     * 根据订单id修改订单支付状态
     *
     * @param orderId
     * @param transactionid 交易流水号
     */
    @Override
    public void updateStatus(String orderId, String paytime, String transactionid) {
        //根据订单id查询
        Order order = orderMapper.selectByPrimaryKey(orderId);
        //设置支付时间
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Date payTime = null;
        try {
            payTime = df.parse(paytime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        order.setPayTime(payTime);
        //设置支付状态，已经支付
        order.setPayStatus("1");
        //设置交易流水号
        order.setTransactionId(transactionid);
        //更新order订单
        orderMapper.updateByPrimaryKey(order);
    }

    /**
     * 逻辑删除订单，即修改删除状态
     *
     * @param id
     */
    @Override
    public void deleteOrder(String id) {
        //根据订单id查询
        Order order = orderMapper.selectByPrimaryKey(id);
        //修改状态
        order.setIsDelete("1");
        //更新order订单
        orderMapper.updateByPrimaryKey(order);
    }

    /**
     * Order条件+分页查询
     *
     * @param order 查询条件
     * @param page  页码
     * @param size  页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Order> findPage(Order order, int page, int size) {
        //分页
        PageHelper.startPage(page, size);
        //搜索条件构建
        Example example = createExample(order);
        //执行搜索
        return new PageInfo<Order>(orderMapper.selectByExample(example));
    }

    /**
     * Order分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Order> findPage(int page, int size) {
        //静态分页
        PageHelper.startPage(page, size);
        //分页查询
        return new PageInfo<Order>(orderMapper.selectAll());
    }

    /**
     * Order条件查询
     *
     * @param order
     * @return
     */
    @Override
    public List<Order> findList(Order order) {
        //构建查询条件
        Example example = createExample(order);
        //根据构建的条件查询数据
        return orderMapper.selectByExample(example);
    }


    /**
     * Order构建查询对象
     *
     * @param order
     * @return
     */
    public Example createExample(Order order) {
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        if (order != null) {
            // 订单id
            if (!StringUtils.isEmpty(order.getId())) {
                criteria.andEqualTo("id", order.getId());
            }
            // 数量合计
            if (!StringUtils.isEmpty(order.getTotalNum())) {
                criteria.andEqualTo("totalNum", order.getTotalNum());
            }
            // 金额合计
            if (!StringUtils.isEmpty(order.getTotalMoney())) {
                criteria.andEqualTo("totalMoney", order.getTotalMoney());
            }
            // 优惠金额
            if (!StringUtils.isEmpty(order.getPreMoney())) {
                criteria.andEqualTo("preMoney", order.getPreMoney());
            }
            // 邮费
            if (!StringUtils.isEmpty(order.getPostFee())) {
                criteria.andEqualTo("postFee", order.getPostFee());
            }
            // 实付金额
            if (!StringUtils.isEmpty(order.getPayMoney())) {
                criteria.andEqualTo("payMoney", order.getPayMoney());
            }
            // 支付类型，1、在线支付、0 货到付款
            if (!StringUtils.isEmpty(order.getPayType())) {
                criteria.andEqualTo("payType", order.getPayType());
            }
            // 订单创建时间
            if (!StringUtils.isEmpty(order.getCreateTime())) {
                criteria.andEqualTo("createTime", order.getCreateTime());
            }
            // 订单更新时间
            if (!StringUtils.isEmpty(order.getUpdateTime())) {
                criteria.andEqualTo("updateTime", order.getUpdateTime());
            }
            // 付款时间
            if (!StringUtils.isEmpty(order.getPayTime())) {
                criteria.andEqualTo("payTime", order.getPayTime());
            }
            // 发货时间
            if (!StringUtils.isEmpty(order.getConsignTime())) {
                criteria.andEqualTo("consignTime", order.getConsignTime());
            }
            // 交易完成时间
            if (!StringUtils.isEmpty(order.getEndTime())) {
                criteria.andEqualTo("endTime", order.getEndTime());
            }
            // 交易关闭时间
            if (!StringUtils.isEmpty(order.getCloseTime())) {
                criteria.andEqualTo("closeTime", order.getCloseTime());
            }
            // 物流名称
            if (!StringUtils.isEmpty(order.getShippingName())) {
                criteria.andEqualTo("shippingName", order.getShippingName());
            }
            // 物流单号
            if (!StringUtils.isEmpty(order.getShippingCode())) {
                criteria.andEqualTo("shippingCode", order.getShippingCode());
            }
            // 用户名称
            if (!StringUtils.isEmpty(order.getUsername())) {
                criteria.andLike("username", "%" + order.getUsername() + "%");
            }
            // 买家留言
            if (!StringUtils.isEmpty(order.getBuyerMessage())) {
                criteria.andEqualTo("buyerMessage", order.getBuyerMessage());
            }
            // 是否评价
            if (!StringUtils.isEmpty(order.getBuyerRate())) {
                criteria.andEqualTo("buyerRate", order.getBuyerRate());
            }
            // 收货人
            if (!StringUtils.isEmpty(order.getReceiverContact())) {
                criteria.andEqualTo("receiverContact", order.getReceiverContact());
            }
            // 收货人手机
            if (!StringUtils.isEmpty(order.getReceiverMobile())) {
                criteria.andEqualTo("receiverMobile", order.getReceiverMobile());
            }
            // 收货人地址
            if (!StringUtils.isEmpty(order.getReceiverAddress())) {
                criteria.andEqualTo("receiverAddress", order.getReceiverAddress());
            }
            // 订单来源：1:web，2：app，3：微信公众号，4：微信小程序  5 H5手机页面
            if (!StringUtils.isEmpty(order.getSourceType())) {
                criteria.andEqualTo("sourceType", order.getSourceType());
            }
            // 交易流水号
            if (!StringUtils.isEmpty(order.getTransactionId())) {
                criteria.andEqualTo("transactionId", order.getTransactionId());
            }
            // 订单状态,0:未完成,1:已完成，2：已退货
            if (!StringUtils.isEmpty(order.getOrderStatus())) {
                criteria.andEqualTo("orderStatus", order.getOrderStatus());
            }
            // 支付状态,0:未支付，1：已支付，2：支付失败
            if (!StringUtils.isEmpty(order.getPayStatus())) {
                criteria.andEqualTo("payStatus", order.getPayStatus());
            }
            // 发货状态,0:未发货，1：已发货，2：已收货
            if (!StringUtils.isEmpty(order.getConsignStatus())) {
                criteria.andEqualTo("consignStatus", order.getConsignStatus());
            }
            // 是否删除
            if (!StringUtils.isEmpty(order.getIsDelete())) {
                criteria.andEqualTo("isDelete", order.getIsDelete());
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
    public void delete(String id) {
        orderMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Order
     *
     * @param order
     */
    @Override
    public void update(Order order) {
        orderMapper.updateByPrimaryKey(order);
    }


    /**
     * 根据ID查询Order
     *
     * @param id
     * @return
     */
    @Override
    public Order findById(String id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Order全部数据
     *
     * @return
     */
    @Override
    public List<Order> findAll() {
        return orderMapper.selectAll();
    }
}
