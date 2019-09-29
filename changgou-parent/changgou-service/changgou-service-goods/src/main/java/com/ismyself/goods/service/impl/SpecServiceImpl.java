package com.ismyself.goods.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ismyself.goods.dao.CategoryMapper;
import com.ismyself.goods.dao.SpecMapper;
import com.ismyself.goods.pojo.Category;
import com.ismyself.goods.pojo.Spec;
import com.ismyself.goods.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * package com.ismyself.goods.service.impl;
 *
 * @auther txw
 * @create 2019-08-26  12:18
 * @description：规格的service实现类
 */
@Service
public class SpecServiceImpl implements SpecService {

    @Autowired
    private SpecMapper specMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 得到一个Example
     * @param spec
     * @return
     */
    public Example getSpecExample(Spec spec) {
        Example example = new Example(Spec.class);
        Example.Criteria criteria = example.createCriteria();
        if (spec != null) {
            //id
            if (!StringUtils.isEmpty(spec.getId())) {
                criteria.andEqualTo("id", spec.getId());
            }
            //name
            if (!StringUtils.isEmpty(spec.getName())) {
                criteria.andLike("name", "%" + spec.getName() + "%");
            }
            //options
            if (!StringUtils.isEmpty(spec.getOptions())) {
                criteria.andLike("options", "%" + spec.getOptions() + "%");
            }
            //seq
            if (!StringUtils.isEmpty(spec.getSeq())) {
                criteria.andEqualTo("seq", spec.getSeq());
            }
            //templateId
            if (!StringUtils.isEmpty(spec.getTemplateId())) {
                criteria.andEqualTo("templateId", spec.getTemplateId());
            }
        }
        return example;
    }

    /**
     * 根据categotyId查询所有规格
     *
     * @param categotyId
     * @return
     */
    @Override
    public List<Spec> findByCategotyId(Integer categotyId) {
        Category category = categoryMapper.selectByPrimaryKey(categotyId);
        Spec spec = new Spec();
        spec.setTemplateId(category.getTemplateId());
        return specMapper.select(spec);
    }

    /**
     * 查询所有规格
     *
     * @return
     */
    @Override
    public List<Spec> findAll() {
        return specMapper.selectAll();
    }

    /**
     * 根据id查询规格
     *
     * @param id
     * @return
     */
    @Override
    public Spec findById(Integer id) {
        return specMapper.selectByPrimaryKey(id);
    }

    /**
     * 保存规格
     *
     * @param spec
     */
    @Override
    public void save(Spec spec) {
        specMapper.insertSelective(spec);
    }

    /**
     * 更新规格
     *
     * @param spec
     */
    @Override
    public void update(Spec spec) {
        specMapper.updateByPrimaryKey(spec);
    }

    /**
     * 根据id删除规格
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
    specMapper.deleteByPrimaryKey(id);
    }

    /**
     * 分页查询所有规格
     *
     * @param pageNum
     * @param size
     * @return
     */
    @Override
    public PageInfo<Spec> findPage(Integer pageNum, Integer size) {
        PageHelper.startPage(pageNum,size);
        List<Spec> specs = specMapper.selectAll();
        return new PageInfo<>(specs);
    }

    /**
     * 根据条件查询规格
     *
     * @param spec
     * @return
     */
    @Override
    public List<Spec> findBySpec(Spec spec) {
        Example example = getSpecExample(spec);
        return specMapper.selectByExample(example);
    }

    /**
     * 根据条件分页查询规格
     *
     * @param spec
     * @param pageNum
     * @param size
     * @return
     */
    @Override
    public PageInfo<Spec> findPageBySpec(Spec spec, Integer pageNum, Integer size) {
        PageHelper.startPage(pageNum,size);
        Example example = getSpecExample(spec);
        List<Spec> specs = specMapper.selectByExample(example);
        return new PageInfo<>(specs);
    }
}
