package com.ismyself.goods.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ismyself.goods.dao.CategoryMapper;
import com.ismyself.goods.dao.ParaMapper;
import com.ismyself.goods.pojo.Category;
import com.ismyself.goods.pojo.Para;
import com.ismyself.goods.service.ParaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * package com.ismyself.goods.service.impl;
 *
 * @auther txw
 * @create 2019-08-26  12:47
 * @description：参数的service实现类
 */
@Service
public class ParaServiceImpl implements ParaService {

    @Autowired
    private ParaMapper paraMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 得到一个Example
     * @param para
     * @return
     */
    public Example getParaExample(Para para) {
        Example example = new Example(Para.class);
        Example.Criteria criteria = example.createCriteria();
        if (para != null) {
            //id
            if (!StringUtils.isEmpty(para.getId())) {
                criteria.andEqualTo("id", para.getId());
            }
            //name
            if (!StringUtils.isEmpty(para.getName())) {
                criteria.andLike("name", "%" + para.getName() + "%");
            }
            //options
            if (!StringUtils.isEmpty(para.getOptions())) {
                criteria.andLike("options", "%" + para.getOptions() + "%");
            }
            //seq
            if (!StringUtils.isEmpty(para.getSeq())) {
                criteria.andEqualTo("seq", para.getSeq());
            }
            //templateId
            if (!StringUtils.isEmpty(para.getTemplateId())) {
                criteria.andEqualTo("templateId", para.getTemplateId());
            }
        }
        return example;
    }

    /**
     * 根据categoryId查询所有参数
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<Para> findParasByCategoryId(Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        Integer templateId = category.getTemplateId();
        Para para = new Para();
        para.setTemplateId(templateId);
        return paraMapper.select(para);
    }

    /**
     * 查询所有参数
     *
     * @return
     */
    @Override
    public List<Para> findAll() {
        return paraMapper.selectAll();
    }

    /**
     * 根据id查询参数
     *
     * @param id
     * @return
     */
    @Override
    public Para findById(Integer id) {
        return paraMapper.selectByPrimaryKey(id);
    }

    /**
     * 保存参数
     *
     * @param para
     */
    @Override
    public void save(Para para) {
        paraMapper.insertSelective(para);
    }

    /**
     * 更新参数
     *
     * @param para
     */
    @Override
    public void update(Para para) {
        paraMapper.updateByPrimaryKey(para);
    }

    /**
     * 根据id删除参数
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
        paraMapper.deleteByPrimaryKey(id);
    }

    /**
     * 分页查询所有参数
     *
     * @param pageNum
     * @param size
     * @return
     */
    @Override
    public PageInfo<Para> findPage(Integer pageNum, Integer size) {
        PageHelper.startPage(pageNum, size);
        List<Para> paras = paraMapper.selectAll();
        return new PageInfo<>(paras);
    }

    /**
     * 根据条件查询参数
     *
     * @param para
     * @return
     */
    @Override
    public List<Para> findByPara(Para para) {
        Example example = getParaExample(para);
        return paraMapper.selectByExample(example);
    }

    /**
     * 根据条件分页查询参数
     *
     * @param para
     * @param pageNum
     * @param size
     * @return
     */
    @Override
    public PageInfo<Para> findPageByPara(Para para, Integer pageNum, Integer size) {
        PageHelper.startPage(pageNum, size);
        Example example = getParaExample(para);
        List<Para> paras = paraMapper.selectByExample(example);
        return new PageInfo<>(paras);
    }

}
