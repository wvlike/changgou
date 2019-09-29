package com.ismyself.goods.service;

import com.github.pagehelper.PageInfo;
import com.ismyself.goods.pojo.Template;

import java.util.List;

/**
 * package com.ismyself.goods.service;
 *
 * @auther txw
 * @create 2019-08-25  20:15
 * @description：模板的service
 */
public interface TemplateService {

    /**
     * 根据categoryId查询模板
     * @param categoryId
     * @return
     */
    Template findByCategoryId(Integer categoryId);

    /**
     * 查询所有模板
     * @return
     */
    List<Template> findAll();

    /**
     * 根据id查询模板
     * @param id
     * @return
     */
    Template findById(Integer id);

    /**
     * 保存模板
     * @param template
     */
    void save(Template template);

    /**
     * 更新模板
     * @param template
     */
    void update(Template template);

    /**
     * 根据id删除模板
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据条件查询模板
     * @param template
     * @return
     */
    List<Template> findByTemplate(Template template);

    /**
     * 分页查询所有模板
     *
     * @param pageNum
     * @param size
     * @return
     */
    PageInfo<Template> findPage(Integer pageNum, Integer size);

    /**
     * 根据条件分页查询模板
     * @param template
     * @param pageNum
     * @param size
     * @return
     */
    PageInfo<Template> findPageByTemplate(Template template,Integer pageNum,Integer size);

}
