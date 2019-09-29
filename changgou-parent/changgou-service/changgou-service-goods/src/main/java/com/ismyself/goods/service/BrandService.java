package com.ismyself.goods.service;

import com.github.pagehelper.PageInfo;
import com.ismyself.goods.pojo.Brand;


import java.util.List;

/**
 * package com.ismyself.goods.service;
 *
 * @auther txw
 * @create 2019-08-24  19:41
 * @description：品牌的service
 */
public interface BrandService {
    /**
     * 查询所有品牌
     *
     * @return
     */
    List<Brand> findAll();

    /**
     * 根据id查询品牌
     *
     * @param id
     * @return
     */
    Brand findById(Integer id);

    /**
     * 保存品牌
     *
     * @param brand
     */
    void save(Brand brand);

    /**
     * 更新品牌
     *
     * @param brand
     */
    void update(Brand brand);

    /**
     * 根据id删除品牌
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 根据条件查询品牌
     *
     * @param brand
     * @return
     */
    List<Brand> findByBrand(Brand brand);

    /**
     * 分页查询所有品牌
     *
     * @param pageNum
     * @param size
     * @return
     */
    PageInfo<Brand> findPage(Integer pageNum, Integer size);

    /**
     * 根据条件分页查询品牌
     *
     * @param brand
     * @param pageNum
     * @param size
     * @return
     */
    PageInfo<Brand> findPageByBrand(Brand brand, Integer pageNum, Integer size);

    /**
     * 根据分类ID查询Brand
     *
     * @param categoryId
     * @return
     */
    List<Brand> findByCategotyId(Integer categoryId);
}
