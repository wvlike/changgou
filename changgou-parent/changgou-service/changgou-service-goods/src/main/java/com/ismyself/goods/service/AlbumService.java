package com.ismyself.goods.service;

import com.github.pagehelper.PageInfo;
import com.ismyself.goods.pojo.Album;

import java.util.List;

/**
 * package com.ismyself.goods.service;
 *
 * @auther txw
 * @create 2019-08-25  18:53
 * @description：相册的service
 */
public interface AlbumService {

    /**
     * 查询所有相册
     * @return
     */
    List<Album> findAll();

    /**
     * 根据id查询相册
     * @param id
     * @return
     */
    Album findById(Integer id);

    /**
     * 保存相册
     * @param album
     */
    void save(Album album);

    /**
     * 更新相册
     * @param album
     */
    void update(Album album);

    /**
     * 根据id删除相册
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据条件查询相册
     * @param album
     * @return
     */
    List<Album> findByAlbum(Album album);

    /**
     * 分页查询所有相册
     *
     * @param pageNum
     * @param size
     * @return
     */
    PageInfo<Album> findPage(Integer pageNum, Integer size);

    /**
     * 根据条件分页查询相册
     * @param album
     * @param pageNum
     * @param size
     * @return
     */
    PageInfo<Album> findPageByAlbum(Album album,Integer pageNum,Integer size);
}
