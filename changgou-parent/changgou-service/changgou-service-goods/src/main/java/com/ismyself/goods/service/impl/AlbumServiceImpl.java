package com.ismyself.goods.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ismyself.goods.dao.AlbumMapper;
import com.ismyself.goods.pojo.Album;
import com.ismyself.goods.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * package com.ismyself.goods.service.impl;
 *
 * @auther txw
 * @create 2019-08-25  18:54
 * @description：相册的service实现类
 */
@Service
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    private AlbumMapper albumMapper;

    /**
     * 获取Example对象
     * @param album
     * @return
     */
    public Example getExample(Album album) {
        Example example = new Example(Album.class);
        Example.Criteria criteria = example.createCriteria();
        if (album != null) {
            //id
            if (!StringUtils.isEmpty(album.getId())) {
                criteria.andEqualTo("id", album.getId());
            }
            //title
            if (!StringUtils.isEmpty(album.getTitle())) {
                criteria.andLike("title", "%" + album.getTitle() + "%");
            }
            //image
            if (!StringUtils.isEmpty(album.getImage())) {
                criteria.andLike("image", "%" + album.getImage() + "%");
            }
            //imageItems
            if (!StringUtils.isEmpty(album.getImageItems())) {
                criteria.andLike("imageItems", "%" + album.getImageItems() + "%");
            }
        }
        return example;
    }

    /**
     * 查询所有相册
     * @return
     */
    @Override
    public List<Album> findAll() {
        return albumMapper.selectAll();
    }

    /**
     * 根据id查询相册
     * @param id
     * @return
     */
    @Override
    public Album findById(Integer id) {
        return albumMapper.selectByPrimaryKey(id);
    }

    /**
     * 保存相册
     * @param album
     */
    @Override
    public void save(Album album) {
        albumMapper.insertSelective(album);
    }

    /**
     * 更新相册
     * @param album
     */
    @Override
    public void update(Album album) {
        albumMapper.updateByPrimaryKey(album);
    }

    /**
     * 根据id删除相册
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        albumMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据条件查询相册
     *
     * @param album
     * @return
     */
    @Override
    public List<Album> findByAlbum(Album album) {
        Example example = getExample(album);
        return albumMapper.selectByExample(example);
    }

    /**
     * 分页查询所有相册
     *
     * @param pageNum
     * @param size
     * @return
     */
    @Override
    public PageInfo<Album> findPage(Integer pageNum, Integer size) {
        PageHelper.startPage(pageNum,size);
        List<Album> albums = albumMapper.selectAll();
        PageInfo<Album> pageInfo = new PageInfo<>(albums);
        return pageInfo;
    }

    /**
     * 根据条件分页查询相册
     *
     * @param album
     * @param pageNum
     * @param size
     * @return
     */
    @Override
    public PageInfo<Album> findPageByAlbum(Album album, Integer pageNum, Integer size) {
        PageHelper.startPage(pageNum,size);
        Example example = getExample(album);
        List<Album> albums = albumMapper.selectByExample(example);
        PageInfo<Album> pageInfo = new PageInfo<>(albums);
        return pageInfo;
    }
}
