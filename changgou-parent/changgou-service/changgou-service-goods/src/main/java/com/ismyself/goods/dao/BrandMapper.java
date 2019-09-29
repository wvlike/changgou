package com.ismyself.goods.dao;


import com.ismyself.goods.pojo.Brand;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


/**
 * package com.ismyself.goods.dao;
 *
 * @auther txw
 * @create 2019-08-24  19:02
 * @description：品牌的dao
 */
public interface BrandMapper extends Mapper<Brand> {
    @Select("select * from tb_brand where id in (select brand_id from tb_category_brand where category_id = #{categoryId})")
    List<Brand> findBrandsByCategoryId(Integer categoryId);
}
