package com.ismyself.goods.dao;


import com.ismyself.goods.pojo.Sku;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author:txw
 * @Description:Sku的Dao
 */
public interface SkuMapper extends Mapper<Sku> {

    /**
     * 使用sql语句，保证原子性
     *
     * @param skuId
     * @param num
     */
    @Update("update tb_sku set num = num - #{num} where id = #{id} and num >= ${num}")
    int decrCount(@Param(value = "id") Long skuId, @Param("num") Integer num);
    //                                   arg0                     arg1

    @Update("update tb_sku set num = num + #{num} where id = #{id}")
    void iecrCount(@Param(value = "id") Long skuId, @Param("num") Integer num);
}
