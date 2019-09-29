package com.ismyself.goods.service;

import com.github.pagehelper.PageInfo;
import com.ismyself.goods.pojo.Category;

import java.util.List;

/**
 * package com.ismyself.goods.service;
 *
 * @auther txw
 * @create 2019-08-27  21:08
 * @description：种类的service层
 */
public interface CategoryService {

    /**
     * 查询所有种类
     *
     * @return
     */
    List<Category> findAll();

    /**
     * 根据id查询种类
     *
     * @param id
     * @return
     */
    Category findById(Integer id);

    /**
     * 保存种类
     *
     * @param category
     */
    void save(Category category);

    /**
     * 删除种类
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 更新种类
     *
     * @param category
     */
    void update(Category category);

    /**
     * 根据parentId父id查询所有
     *
     * @param parentId
     * @return
     */
    List<Category> findByParentId(Integer parentId);

    /**
     * 根据条件查询
     *
     * @param category
     * @return
     */
    List<Category> findByCategory(Category category);

    /**
     * 分页查询所有
     *
     * @param pageNum
     * @param size
     * @return
     */
    PageInfo<Category> findPage(Integer pageNum, Integer size);

    /**
     * 根据条件分页查询
     *
     * @param category
     * @param pageNum
     * @param size
     * @return
     */
    PageInfo<Category> findPageByCategory(Category category, Integer pageNum, Integer size);

}
