package com.ismyself.goods.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ismyself.goods.dao.SkuMapper;
import com.ismyself.goods.pojo.Sku;
import com.ismyself.goods.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 * @Author:txw
 * @Description:Sku业务层接口实现类
 */
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuMapper skuMapper;


    /**
     * 根据封装的条件进行递增商品
     *
     * @param iecrmap
     */
    @Override
    public void iecrCount(Map<String, Object> iecrmap) {
        for (Map.Entry<String, Object> entry : iecrmap.entrySet()) {
            Long skuId = Long.valueOf(entry.getKey());
            Integer num = Integer.valueOf(entry.getValue().toString());
//数据库中每条记录都拥有行级锁,此时只能允许一个事务修改该记录，只有等该事务结束后，其他事务才能操作该记录
            skuMapper.iecrCount(skuId, num);
        }
    }

    /**
     * 根据封装的条件进行递减商品
     *
     * @param decrmap
     */
    @Override
    public void decrCount(Map<String, Object> decrmap) {

        for (Map.Entry<String, Object> entry : decrmap.entrySet()) {
            Long skuId = Long.valueOf(entry.getKey());
            Integer num = Integer.valueOf(entry.getValue().toString());
//数据库中每条记录都拥有行级锁,此时只能允许一个事务修改该记录，只有等该事务结束后，其他事务才能操作该记录

            int code = skuMapper.decrCount(skuId, num);

            System.out.println(1 / 0);

            //如果存在超卖就回滚
            if (code <= 0) {
                throw new RuntimeException("库存不足！！");
            }
        }
    }

    /**
     * 根据状态查询sku
     *
     * @param status
     * @return
     */
    @Override
    public List<Sku> findByStatus(String status) {
        //效果一样
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", status);
        return skuMapper.selectByExample(example);
/*        Sku sku = new Sku();
        return skuMapper.select(sku);*/
    }

    /**
     * Sku条件+分页查询
     *
     * @param sku  查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Sku> findPage(Sku sku, int page, int size) {
        //分页
        PageHelper.startPage(page, size);
        //搜索条件构建
        Example example = createExample(sku);
        //执行搜索
        return new PageInfo<Sku>(skuMapper.selectByExample(example));
    }

    /**
     * Sku分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Sku> findPage(int page, int size) {
        //静态分页
        PageHelper.startPage(page, size);
        //分页查询
        return new PageInfo<Sku>(skuMapper.selectAll());
    }

    /**
     * Sku条件查询
     *
     * @param sku
     * @return
     */
    @Override
    public List<Sku> findList(Sku sku) {
        //构建查询条件
        Example example = createExample(sku);
        //根据构建的条件查询数据
        return skuMapper.selectByExample(example);
    }


    /**
     * Sku构建查询对象
     *
     * @param sku
     * @return
     */
    public Example createExample(Sku sku) {
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        if (sku != null) {
            // 商品id
            if (!StringUtils.isEmpty(sku.getId())) {
                criteria.andEqualTo("id", sku.getId());
            }
            // 商品条码
            if (!StringUtils.isEmpty(sku.getSn())) {
                criteria.andEqualTo("sn", sku.getSn());
            }
            // SKU名称
            if (!StringUtils.isEmpty(sku.getName())) {
                criteria.andLike("name", "%" + sku.getName() + "%");
            }
            // 价格（分）
            if (!StringUtils.isEmpty(sku.getPrice())) {
                criteria.andEqualTo("price", sku.getPrice());
            }
            // 库存数量
            if (!StringUtils.isEmpty(sku.getNum())) {
                criteria.andEqualTo("num", sku.getNum());
            }
            // 库存预警数量
            if (!StringUtils.isEmpty(sku.getAlertNum())) {
                criteria.andEqualTo("alertNum", sku.getAlertNum());
            }
            // 商品图片
            if (!StringUtils.isEmpty(sku.getImage())) {
                criteria.andEqualTo("image", sku.getImage());
            }
            // 商品图片列表
            if (!StringUtils.isEmpty(sku.getImages())) {
                criteria.andEqualTo("images", sku.getImages());
            }
            // 重量（克）
            if (!StringUtils.isEmpty(sku.getWeight())) {
                criteria.andEqualTo("weight", sku.getWeight());
            }
            // 创建时间
            if (!StringUtils.isEmpty(sku.getCreateTime())) {
                criteria.andEqualTo("createTime", sku.getCreateTime());
            }
            // 更新时间
            if (!StringUtils.isEmpty(sku.getUpdateTime())) {
                criteria.andEqualTo("updateTime", sku.getUpdateTime());
            }
            // SPUID
            if (!StringUtils.isEmpty(sku.getSpuId())) {
                criteria.andEqualTo("spuId", sku.getSpuId());
            }
            // 类目ID
            if (!StringUtils.isEmpty(sku.getCategoryId())) {
                criteria.andEqualTo("categoryId", sku.getCategoryId());
            }
            // 类目名称
            if (!StringUtils.isEmpty(sku.getCategoryName())) {
                criteria.andEqualTo("categoryName", sku.getCategoryName());
            }
            // 品牌名称
            if (!StringUtils.isEmpty(sku.getBrandName())) {
                criteria.andEqualTo("brandName", sku.getBrandName());
            }
            // 规格
            if (!StringUtils.isEmpty(sku.getSpec())) {
                criteria.andEqualTo("spec", sku.getSpec());
            }
            // 销量
            if (!StringUtils.isEmpty(sku.getSaleNum())) {
                criteria.andEqualTo("saleNum", sku.getSaleNum());
            }
            // 评论数
            if (!StringUtils.isEmpty(sku.getCommentNum())) {
                criteria.andEqualTo("commentNum", sku.getCommentNum());
            }
            // 商品状态 1-正常，2-下架，3-删除
            if (!StringUtils.isEmpty(sku.getStatus())) {
                criteria.andEqualTo("status", sku.getStatus());
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
        skuMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Sku
     *
     * @param sku
     */
    @Override
    public void update(Sku sku) {
        skuMapper.updateByPrimaryKey(sku);
    }

    /**
     * 增加Sku
     *
     * @param sku
     */
    @Override
    public void add(Sku sku) {
        skuMapper.insert(sku);
    }

    /**
     * 根据ID查询Sku
     *
     * @param id
     * @return
     */
    @Override
    public Sku findById(Long id) {
        return skuMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Sku全部数据
     *
     * @return
     */
    @Override
    public List<Sku> findAll() {
        return skuMapper.selectAll();
    }
}
