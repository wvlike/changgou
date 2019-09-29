package com.ismyself.goods.service;

import com.github.pagehelper.PageInfo;
import com.ismyself.goods.pojo.Para;
import java.util.List;

/**
 * package com.ismyself.goods.service;
 *
 * @auther txw
 * @create 2019-08-26  12:17
 * @description：参数的service
 */
public interface ParaService {

    /**
     * 根据categoryId查询所有参数
     * @param categoryId
     * @return
     */
    List<Para> findParasByCategoryId(Integer categoryId);

    /**
     * 查询所有参数
     * @return
     */
    List<Para> findAll();

    /**
     * 根据id查询参数
     * @param id
     * @return
     */
    Para findById(Integer id);

    /**
     * 保存参数
     * @param para
     */
    void save(Para para);

    /**
     * 更新参数
     * @param para
     */
    void update(Para para);

    /**
     * 根据id删除参数
     * @param id
     */
    void delete(Integer id);

    /**
     * 分页查询所有参数
     * @param pageNum
     * @param size
     * @return
     */
    PageInfo<Para> findPage(Integer pageNum, Integer size);

    /**
     * 根据条件查询参数
     * @param para
     * @return
     */
    List<Para> findByPara(Para para);

    /**
     * 根据条件分页查询参数
     * @param para
     * @param pageNum
     * @param size
     * @return
     */
    PageInfo<Para> findPageByPara(Para para,Integer pageNum,Integer size);

}
