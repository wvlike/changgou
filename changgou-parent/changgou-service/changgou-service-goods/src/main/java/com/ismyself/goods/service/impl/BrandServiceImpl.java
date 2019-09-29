package com.ismyself.goods.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ismyself.goods.dao.BrandMapper;
import com.ismyself.goods.dao.CategoryBrandMapper;
import com.ismyself.goods.pojo.Brand;
import com.ismyself.goods.pojo.CategoryBrand;
import com.ismyself.goods.service.BrandService;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * package com.ismyself.goods.service.impl;
 *
 * @auther txw
 * @create 2019-08-24  19:42
 * @description：品牌的service实现类
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    /**
     * 查询所有品牌
     *
     * @return
     */
    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }

    /**
     * 根据id查询品牌
     *
     * @param id
     * @return
     */
    @Override
    public Brand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 保存品牌
     *
     * @param brand
     */
    @Override
    public void save(Brand brand) {
        brandMapper.insertSelective(brand);
    }

    /**
     * 更新品牌
     *
     * @param brand
     */
    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKey(brand);
    }

    /**
     * 根据id删除品牌
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据条件查询品牌
     *
     * @param brand
     * @return
     */
    @Override
    public List<Brand> findByBrand(Brand brand) {

        Example example = createBrandExample(brand);
        List<Brand> brands = brandMapper.selectByExample(example);
        return brands;
    }

    /**
     * 抽取的公共的createExample方法
     *
     * @param brand
     * @return
     */
    public Example createBrandExample(Brand brand) {
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if (brand != null) {
            //品牌id
            if (!StringUtils.isEmpty(brand.getId())) {
                criteria.andEqualTo("id", brand.getId());
            }
            //品牌名称
            if (!StringUtils.isEmpty(brand.getName())) {
                criteria.andLike("name", "%" + brand.getName() + "%");
            }
            //品牌图片地址
            if (!StringUtils.isEmpty(brand.getImage())) {
                criteria.andLike("image", "%" + brand.getImage() + "%");
            }
            //品牌的首字母
            if (!StringUtils.isEmpty(brand.getLetter())) {
                criteria.andEqualTo("letter", brand.getLetter());
            }
            //排序
            if (!StringUtils.isEmpty(brand.getSeq())) {
                criteria.andEqualTo("seq", brand.getSeq());
            }
        }
        return example;
    }

    /**
     * CategoryBrandExample
     */
    public Example getCategoryBrandExample(CategoryBrand categoryBrand) {
        Example example = new Example(CategoryBrand.class);
        Example.Criteria criteria = example.createCriteria();
        if (categoryBrand != null) {
            if (!StringUtils.isEmpty(categoryBrand.getBrandId())) {
                criteria.andEqualTo("brandId", categoryBrand.getBrandId());
            }
            if (!StringUtils.isEmpty(categoryBrand.getCategoryId())) {
                criteria.andEqualTo("categoryId", categoryBrand.getCategoryId());
            }
        }
        return example;
    }

    /**
     * 分页查询所有品牌
     *
     * @param pageNum
     * @param size
     * @return
     */
    @Override
    public PageInfo<Brand> findPage(Integer pageNum, Integer size) {
        PageHelper.startPage(pageNum, size);
        List<Brand> brands = brandMapper.selectAll();
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        return pageInfo;
    }

    /**
     * 根据条件分页查询品牌
     *
     * @param brand
     * @param pageNum
     * @param size
     * @return
     */
    @Override
    public PageInfo<Brand> findPageByBrand(Brand brand, Integer pageNum, Integer size) {
        PageHelper.startPage(pageNum, size);
        Example example = createBrandExample(brand);
        List<Brand> brands = brandMapper.selectByExample(example);
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        return pageInfo;
    }

    /**
     * 根据分类ID查询Brand
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<Brand> findByCategotyId(Integer categoryId) {
        List<Brand> list = brandMapper.findBrandsByCategoryId(categoryId);

/*        CategoryBrand categoryBrand = new CategoryBrand();
        categoryBrand.setCategoryId(categoryId);
        Example categoryBrandExample = getCategoryBrandExample(categoryBrand);
        List<CategoryBrand> categoryBrands = categoryBrandMapper.selectByExample(categoryBrandExample);
        List<Brand> list = new ArrayList<>();
        for (CategoryBrand categorybrand : categoryBrands) {
            Integer brandId = categorybrand.getBrandId();
            Brand brand = brandMapper.selectByPrimaryKey(brandId);
            list.add(brand);
        }*/
        return list;
    }
}
