package com.ismyself.goods.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ismyself.goods.dao.CategoryMapper;
import com.ismyself.goods.pojo.Category;
import com.ismyself.goods.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * package com.ismyself.goods.service.impl;
 *
 * @auther txw
 * @create 2019-08-27  21:08
 * @description：种类的service层实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public Example getExample(Category category) {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        if (category != null) {
            if (!StringUtils.isEmpty(category.getId())) {
                criteria.andEqualTo("id", category.getId());
            }
            if (!StringUtils.isEmpty(category.getName())) {
                criteria.andLike("name", "%" + category.getName() + "%");
            }
            if (!StringUtils.isEmpty(category.getGoodsNum())) {
                criteria.andEqualTo("goodsNum", category.getGoodsNum());
            }
            if (!StringUtils.isEmpty(category.getIsShow())) {
                criteria.andLike("isShow", "%" + category.getIsShow() + "%");
            }
            if (!StringUtils.isEmpty(category.getIsMenu())) {
                criteria.andLike("isMenu", "%" + category.getIsMenu() + "%");
            }
            if (!StringUtils.isEmpty(category.getSeq())) {
                criteria.andEqualTo("seq", category.getSeq());
            }
            if (!StringUtils.isEmpty(category.getParentId())) {
                criteria.andEqualTo("parentId", category.getParentId());
            }
            if (!StringUtils.isEmpty(category.getTemplateId())) {
                criteria.andEqualTo("templateId", category.getTemplateId());
            }
        }
        return example;
    }

    /**
     * 查询所有种类
     *
     * @return
     */
    @Override
    public List<Category> findAll() {
        return categoryMapper.selectAll();
    }

    /**
     * 根据id查询种类
     *
     * @param id
     * @return
     */
    @Override
    public Category findById(Integer id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    /**
     * 保存种类
     *
     * @param category
     */
    @Override
    public void save(Category category) {
        categoryMapper.insertSelective(category);
    }

    /**
     * 删除种类
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    /**
     * 更新种类
     *
     * @param category
     */
    @Override
    public void update(Category category) {
        categoryMapper.updateByPrimaryKey(category);
    }

    /**
     * 根据parentId父id查询所有
     *
     * @param parentId
     * @return
     */
    @Override
    public List<Category> findByParentId(Integer parentId) {
        Category category = new Category();
        category.setParentId(parentId);
        return categoryMapper.select(category);
//        Example example = getExample(category);
//        return categoryMapper.selectByExample(example);
    }

    /**
     * 根据条件查询
     *
     * @param category
     * @return
     */
    @Override
    public List<Category> findByCategory(Category category) {
        Example example = getExample(category);
        return categoryMapper.selectByExample(example);
    }

    /**
     * 分页查询所有
     *
     * @param pageNum
     * @param size
     * @return
     */
    @Override
    public PageInfo<Category> findPage(Integer pageNum, Integer size) {
        PageHelper.startPage(pageNum, size);
        List<Category> categories = categoryMapper.selectAll();
        PageInfo<Category> pageInfo = new PageInfo<>(categories);
        return pageInfo;
    }

    /**
     * 根据条件分页查询
     *
     * @param category
     * @param pageNum
     * @param size
     * @return
     */
    @Override
    public PageInfo<Category> findPageByCategory(Category category, Integer pageNum, Integer size) {
        PageHelper.startPage(pageNum, size);
        Example example = getExample(category);
        List<Category> categories = categoryMapper.selectByExample(example);
        PageInfo<Category> pageInfo = new PageInfo<>(categories);
        return pageInfo;
    }
}
