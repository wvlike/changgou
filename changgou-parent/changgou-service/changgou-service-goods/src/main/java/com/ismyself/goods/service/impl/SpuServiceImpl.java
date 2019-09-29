package com.ismyself.goods.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ismyself.goods.dao.BrandMapper;
import com.ismyself.goods.dao.CategoryMapper;
import com.ismyself.goods.dao.SkuMapper;
import com.ismyself.goods.dao.SpuMapper;
import com.ismyself.goods.pojo.*;
import com.ismyself.goods.service.SpuService;
import entity.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.*;

/**
 * @Author:txw
 * @Description:Spu业务层接口实现类
 */
@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private IdWorker idWorker;

    /**
     * 逻辑删除
     *
     * @param id
     */
    @Override
    public void logicDelete(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu.getIsDelete().equals("1")){
            throw new RuntimeException("商品已删除，无法重复删除");
        }
        spu.setIsDelete("1");
        //执行
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 逻辑还原
     *
     * @param id
     */
    @Override
    public void restore(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu.getIsDelete().equals("0")){
            throw new RuntimeException("商品已还原，无法重复还原");
        }
        spu.setIsDelete("0");
        //执行
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 商品批量下架
     *
     * @param ids
     */
    @Override
    public int pullmany(Long[] ids) {
        Spu spu = new Spu();
        spu.setIsMarketable("0");
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //设置id
        criteria.andIn("id", Arrays.asList(ids));
        //未删除的
        criteria.andEqualTo("isDelete", "0");
        //已审核的
        criteria.andEqualTo("status", "1");
        //已上架的
        criteria.andEqualTo("isMarketable", "1");
        //执行
        return spuMapper.updateByExampleSelective(spu, example);
    }

    /**
     * 商品批量上架
     *
     * @param ids
     * @return
     */
    @Override
    public int putmany(Long[] ids) {
        //新建一个spu
        Spu spu = new Spu();
        spu.setIsMarketable("1");
        //新建一个Example
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        //未删除的
        criteria.andEqualTo("isDelete", "0");
        //已审核的
        criteria.andEqualTo("status", "1");
        //未上架的
        criteria.andEqualTo("isMarketable", "0");
        //执行
        return spuMapper.updateByExampleSelective(spu, example);
    }

    /**
     * 商品下架
     *
     * @param id
     */
    @Override
    public void pull(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu.getIsDelete().equals("0")) {
            throw new RuntimeException("商品已删除，无法下架");
        }
        //设置下架状态
        spu.setIsMarketable("0");
        //更新spu
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 商品上架
     *
     * @param id
     */
    @Override
    public void put(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu.getIsDelete().equals("0")) {
            throw new RuntimeException("商品已删除，无法上架");
        }
        if (!spu.getStatus().equals("1")) {
            throw new RuntimeException("商品未通过审核，无法上架");
        }
        //设置上架状态
        spu.setIsMarketable("1");
        //更新spu
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 商品审核
     *
     * @param id
     */
    @Override
    public void audit(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu.getIsDelete().equals("0")) {
            throw new RuntimeException("商品已删除，无法审核");
        }
        //设置审核状态
        spu.setStatus("1");
        //更新spu
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 根据spuId查询商品
     *
     * @param spuId
     * @return
     */
    @Override
    public Goods findGoodsById(Long spuId) {
        //根据spuId获取spu
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        //根据spuId查询skuList
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        //封装到Goods中
        Goods goods = new Goods();
        goods.setSpu(spu);
        goods.setSkuList(skuList);
        return goods;
    }

    /**
     * 商品增加、更新
     *
     * @param goods
     */
    @Override
    public void saveGoods(Goods goods) {
        Spu spu = goods.getSpu();
        Long sId = spu.getId();
        if (StringUtils.isEmpty(sId)) {
            //若为null，设置spu的id
            sId = idWorker.nextId();
            spu.setId(sId);
        } else {
            //为更新，先删除原来的spu和sku
            spuMapper.deleteByPrimaryKey(sId);
            Sku sku = new Sku();
            sku.setSpuId(sId);
            skuMapper.delete(sku);
        }
        //设置spu的货号
        String sn = "No1010";
        spu.setSn(sn);
        //获取category3Id
        Integer category3Id = spu.getCategory3Id();
        //保存spu
        spuMapper.insertSelective(spu);
        //获取category对象
        Category category = categoryMapper.selectByPrimaryKey(category3Id);
        //品牌name
        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
        Date date = new Date();
        List<Sku> skuList = goods.getSkuList();
        for (Sku sku : skuList) {
            //商品id
            sku.setId(idWorker.nextId());
            //获取名称
            String skuName = spu.getName();
            String spec = sku.getSpec();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Map<String, String> map = objectMapper.readValue(spec, Map.class);
                Set<String> keySet = map.keySet();
                for (String key : keySet) {
                    skuName += map.get(key) + " ";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //设置名称
            sku.setName(skuName);
            //创建时间
            sku.setCreateTime(date);
            //更新时间
            sku.setUpdateTime(date);
            //spuid
            sku.setSpuId(sId);
            //categoryId
            sku.setCategoryId(category3Id);
            //categoryName
            sku.setCategoryName(category.getName());
            //品牌brandName
            sku.setBrandName(brand.getName());
            //保存sku
            skuMapper.insertSelective(sku);
        }


    }

    /**
     * Spu条件+分页查询
     *
     * @param spu  查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Spu> findPage(Spu spu, int page, int size) {
        //分页
        PageHelper.startPage(page, size);
        //搜索条件构建
        Example example = createExample(spu);
        //执行搜索
        return new PageInfo<Spu>(spuMapper.selectByExample(example));
    }

    /**
     * Spu分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Spu> findPage(int page, int size) {
        //静态分页
        PageHelper.startPage(page, size);
        //分页查询
        return new PageInfo<Spu>(spuMapper.selectAll());
    }

    /**
     * Spu条件查询
     *
     * @param spu
     * @return
     */
    @Override
    public List<Spu> findList(Spu spu) {
        //构建查询条件
        Example example = createExample(spu);
        //根据构建的条件查询数据
        return spuMapper.selectByExample(example);
    }


    /**
     * Spu构建查询对象
     *
     * @param spu
     * @return
     */
    public Example createExample(Spu spu) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (spu != null) {
            // 主键
            if (!StringUtils.isEmpty(spu.getId())) {
                criteria.andEqualTo("id", spu.getId());
            }
            // 货号
            if (!StringUtils.isEmpty(spu.getSn())) {
                criteria.andEqualTo("sn", spu.getSn());
            }
            // SPU名
            if (!StringUtils.isEmpty(spu.getName())) {
                criteria.andLike("name", "%" + spu.getName() + "%");
            }
            // 副标题
            if (!StringUtils.isEmpty(spu.getCaption())) {
                criteria.andEqualTo("caption", spu.getCaption());
            }
            // 品牌ID
            if (!StringUtils.isEmpty(spu.getBrandId())) {
                criteria.andEqualTo("brandId", spu.getBrandId());
            }
            // 一级分类
            if (!StringUtils.isEmpty(spu.getCategory1Id())) {
                criteria.andEqualTo("category1Id", spu.getCategory1Id());
            }
            // 二级分类
            if (!StringUtils.isEmpty(spu.getCategory2Id())) {
                criteria.andEqualTo("category2Id", spu.getCategory2Id());
            }
            // 三级分类
            if (!StringUtils.isEmpty(spu.getCategory3Id())) {
                criteria.andEqualTo("category3Id", spu.getCategory3Id());
            }
            // 模板ID
            if (!StringUtils.isEmpty(spu.getTemplateId())) {
                criteria.andEqualTo("templateId", spu.getTemplateId());
            }
            // 运费模板id
            if (!StringUtils.isEmpty(spu.getFreightId())) {
                criteria.andEqualTo("freightId", spu.getFreightId());
            }
            // 图片
            if (!StringUtils.isEmpty(spu.getImage())) {
                criteria.andEqualTo("image", spu.getImage());
            }
            // 图片列表
            if (!StringUtils.isEmpty(spu.getImages())) {
                criteria.andEqualTo("images", spu.getImages());
            }
            // 售后服务
            if (!StringUtils.isEmpty(spu.getSaleService())) {
                criteria.andEqualTo("saleService", spu.getSaleService());
            }
            // 介绍
            if (!StringUtils.isEmpty(spu.getIntroduction())) {
                criteria.andEqualTo("introduction", spu.getIntroduction());
            }
            // 规格列表
            if (!StringUtils.isEmpty(spu.getSpecItems())) {
                criteria.andEqualTo("specItems", spu.getSpecItems());
            }
            // 参数列表
            if (!StringUtils.isEmpty(spu.getParaItems())) {
                criteria.andEqualTo("paraItems", spu.getParaItems());
            }
            // 销量
            if (!StringUtils.isEmpty(spu.getSaleNum())) {
                criteria.andEqualTo("saleNum", spu.getSaleNum());
            }
            // 评论数
            if (!StringUtils.isEmpty(spu.getCommentNum())) {
                criteria.andEqualTo("commentNum", spu.getCommentNum());
            }
            // 是否上架,0已下架，1已上架
            if (!StringUtils.isEmpty(spu.getIsMarketable())) {
                criteria.andEqualTo("isMarketable", spu.getIsMarketable());
            }
            // 是否启用规格
            if (!StringUtils.isEmpty(spu.getIsEnableSpec())) {
                criteria.andEqualTo("isEnableSpec", spu.getIsEnableSpec());
            }
            // 是否删除,0:未删除，1：已删除
            if (!StringUtils.isEmpty(spu.getIsDelete())) {
                criteria.andEqualTo("isDelete", spu.getIsDelete());
            }
            // 审核状态，0：未审核，1：已审核，2：审核不通过
            if (!StringUtils.isEmpty(spu.getStatus())) {
                criteria.andEqualTo("status", spu.getStatus());
            }
        }
        return example;
    }

    /**
     * 物理删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu.getIsDelete().equals("1")){
            throw  new RuntimeException("商品已经删除，无法重复删除！");
        }
        spuMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Spu
     *
     * @param spu
     */
    @Override
    public void update(Spu spu) {
        spuMapper.updateByPrimaryKey(spu);
    }

    /**
     * 增加Spu
     *
     * @param spu
     */
    @Override
    public void add(Spu spu) {
        spuMapper.insert(spu);
    }

    /**
     * 根据ID查询Spu
     *
     * @param id
     * @return
     */
    @Override
    public Spu findById(Long id) {
        return spuMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Spu全部数据
     *
     * @return
     */
    @Override
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }
}
