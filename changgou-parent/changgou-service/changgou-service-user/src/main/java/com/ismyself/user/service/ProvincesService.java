package com.ismyself.user.service;



import com.github.pagehelper.PageInfo;
import com.ismyself.user.pojo.Provinces;

import java.util.List;

/**
 * @Author:txw
 * @Description:Provinces业务层接口
 */
public interface ProvincesService {

    /***
     * Provinces多条件分页查询
     * @param provinces
     * @param page
     * @param size
     * @return
     */
    PageInfo<Provinces> findPage(Provinces provinces, int page, int size);

    /***
     * Provinces分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Provinces> findPage(int page, int size);

    /***
     * Provinces多条件搜索方法
     * @param provinces
     * @return
     */
    List<Provinces> findList(Provinces provinces);

    /***
     * 删除Provinces
     * @param id
     */
    void delete(String id);

    /***
     * 修改Provinces数据
     * @param provinces
     */
    void update(Provinces provinces);

    /***
     * 新增Provinces
     * @param provinces
     */
    void add(Provinces provinces);

    /**
     * 根据ID查询Provinces
     * @param id
     * @return
     */
     Provinces findById(String id);

    /***
     * 查询所有Provinces
     * @return
     */
    List<Provinces> findAll();
}
