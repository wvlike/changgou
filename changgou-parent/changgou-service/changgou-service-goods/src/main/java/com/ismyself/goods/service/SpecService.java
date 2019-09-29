package com.ismyself.goods.service;

import com.github.pagehelper.PageInfo;
import com.ismyself.goods.pojo.Spec;

import java.util.List;

/**
 * package com.ismyself.goods.service;
 *
 * @auther txw
 * @create 2019-08-26  12:16
 * @description：规格的service
 */
public interface SpecService {

    /**
     * 根据categotyId查询所有规格
     *
     * @param categotyId
     * @return
     */
    List<Spec> findByCategotyId(Integer categotyId);


    /**
     * 查询所有规格
     *
     * @return
     */
    List<Spec> findAll();

    /**
     * 根据id查询规格
     *
     * @param id
     * @return
     */
    Spec findById(Integer id);

    /**
     * 保存规格
     *
     * @param spec
     */
    void save(Spec spec);

    /**
     * 更新规格
     *
     * @param spec
     */
    void update(Spec spec);

    /**
     * 根据id删除规格
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 分页查询所有规格
     *
     * @param pageNum
     * @param size
     * @return
     */
    PageInfo<Spec> findPage(Integer pageNum, Integer size);

    /**
     * 根据条件查询规格
     *
     * @param spec
     * @return
     */
    List<Spec> findBySpec(Spec spec);

    /**
     * 根据条件分页查询规格
     *
     * @param spec
     * @param pageNum
     * @param size
     * @return
     */
    PageInfo<Spec> findPageBySpec(Spec spec, Integer pageNum, Integer size);

}
