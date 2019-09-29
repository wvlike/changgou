package com.ismyself.goods.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ismyself.goods.dao.CategoryMapper;
import com.ismyself.goods.dao.TemplateMapper;
import com.ismyself.goods.pojo.Category;
import com.ismyself.goods.pojo.Template;
import com.ismyself.goods.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * package com.ismyself.goods.service.impl;
 *
 * @auther txw
 * @create 2019-08-25  20:18
 * @description：模板的service实现类
 */
@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateMapper templateMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    public Example getTemplateExample(Template template) {
        Example example = new Example(Template.class);
        Example.Criteria criteria = example.createCriteria();
        if (template != null) {
            //id
            if (!StringUtils.isEmpty(template.getId())) {
                criteria.andEqualTo("id", template.getId());
            }
            //name
            if (!StringUtils.isEmpty(template.getName())) {
                criteria.andLike("name", "%" + template.getName() + "%");
            }
            //specNum
            if (!StringUtils.isEmpty(template.getSpecNum())) {
                criteria.andEqualTo("specNum", template.getSpecNum());
            }
            //paraNum
            if (!StringUtils.isEmpty(template.getParaNum())) {
                criteria.andEqualTo("paraNum", template.getParaNum());
            }
        }
        return example;
    }

    /**
     * 根据categoryId查询模板
     *
     * @param categoryId
     * @return
     */
    @Override
    public Template findByCategoryId(Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        Integer templateId = category.getTemplateId();
        return templateMapper.selectByPrimaryKey(templateId);
    }

    /**
     * 查询所有模板
     *
     * @return
     */
    @Override
    public List<Template> findAll() {
        return templateMapper.selectAll();
    }

    /**
     * 根据id查询模板
     *
     * @param id
     * @return
     */
    @Override
    public Template findById(Integer id) {
        return templateMapper.selectByPrimaryKey(id);
    }

    /**
     * 保存模板
     *
     * @param template
     */
    @Override
    public void save(Template template) {
        templateMapper.insertSelective(template);
    }

    /**
     * 更新模板
     *
     * @param template
     */
    @Override
    public void update(Template template) {
        templateMapper.updateByPrimaryKey(template);
    }

    /**
     * 根据id删除模板
     *
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        templateMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据条件查询模板
     *
     * @param template
     * @return
     */
    @Override
    public List<Template> findByTemplate(Template template) {
        Example templateExample = getTemplateExample(template);
        List<Template> templates = templateMapper.selectByExample(templateExample);
        return templates;
    }

    /**
     * 分页查询所有模板
     *
     * @param pageNum
     * @param size
     * @return
     */
    @Override
    public PageInfo<Template> findPage(Integer pageNum, Integer size) {
        PageHelper.startPage(pageNum,size);
        List<Template> templates = templateMapper.selectAll();
        PageInfo<Template> pageInfo = new PageInfo<>(templates);
        return pageInfo;
    }

    /**
     * 根据条件分页查询模板
     *
     * @param template
     * @param pageNum
     * @param size
     * @return
     */
    @Override
    public PageInfo<Template> findPageByTemplate(Template template, Integer pageNum, Integer size) {
        PageHelper.startPage(pageNum,size);
        Example templateExample = getTemplateExample(template);
        List<Template> templates = templateMapper.selectByExample(templateExample);
        PageInfo<Template> pageInfo = new PageInfo<>(templates);
        return pageInfo;
    }
}
