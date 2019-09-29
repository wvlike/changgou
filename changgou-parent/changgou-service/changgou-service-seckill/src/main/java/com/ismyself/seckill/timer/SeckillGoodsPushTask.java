package com.ismyself.seckill.timer;

import com.ismyself.seckill.dao.SeckillGoodsMapper;
import com.ismyself.seckill.pojo.SeckillGoods;
import entity.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * package com.ismyself.seckill.timer;
 *
 * @auther txw
 * @create 2019-09-11  21:16
 * @description：
 */
@Component
public class SeckillGoodsPushTask {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    /****
     * 每10秒执行一次，将
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void loadGoodsPushRedis() {
        //从数据库获取从现在开始日期的秒杀列表
        List<Date> dates = DateUtil.getDateMenus();
        for (Date date : dates) {
            Example example = new Example(SeckillGoods.class);

            Example.Criteria criteria = example.createCriteria();
            //startTime>= date  , date + 2 < endTime
            criteria.andGreaterThanOrEqualTo("startTime", date);
            criteria.andLessThan("endTime", DateUtil.addDateHour(date, 2));
            //status = 1
            criteria.andEqualTo("status", "1");
            //库存大于0
            criteria.andGreaterThan("stockCount", 0);
            //将时间转换
            String extName = DateUtil.data2str(date, DateUtil.PATTERN_YYYYMMDDHH);
            //redis中没有该商品
            Set keys = redisTemplate.boundHashOps("SeckillGoods_" + extName).keys();
            if (keys != null && keys.size() > 0) {
                criteria.andNotIn("id", keys);
            }
            //先查询出结果
            List<SeckillGoods> seckillGoodsList = seckillGoodsMapper.selectByExample(example);
            //存入redis中
            for (SeckillGoods seckillGoods : seckillGoodsList) {
                System.out.println(seckillGoods);
                redisTemplate.boundHashOps("SeckillGoods_" + extName).put(seckillGoods.getId(), seckillGoods);
                //存入队列
                pushSeckillNums(seckillGoods);
            }
        }
    }

    //将同一商品存入redis，数组个数为该商品的库存数
    public void pushSeckillNums(SeckillGoods seckillGoods) {
        Integer num = seckillGoods.getNum();
        Long[] ids = new Long[num];
        for (int i = 0; i < num; i++) {
            ids[i] = seckillGoods.getId();
        }
        //将商品队列存入redis
        redisTemplate.boundListOps("SeckillGoodsCountList_" + seckillGoods.getId()).leftPushAll(ids);
    }

}
