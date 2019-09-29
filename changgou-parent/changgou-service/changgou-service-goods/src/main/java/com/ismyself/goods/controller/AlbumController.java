package com.ismyself.goods.controller;

import com.github.pagehelper.PageInfo;
import com.ismyself.goods.pojo.Album;
import com.ismyself.goods.service.AlbumService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * package com.ismyself.goods.controller;
 *
 * @auther txw
 * @create 2019-08-25  19:23
 * @description：相册的controller
 */
@RestController
@RequestMapping("/album")
@CrossOrigin
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    /**
     * 查询所有相册
     *
     * @return
     */
    @GetMapping
    public Result<List<Album>> findAll() {
        List<Album> albums = albumService.findAll();
        return new Result<List<Album>>(true, StatusCode.OK, "查询所有相册成功", albums);
    }

    /**
     * 根据id查询相册
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Album> findById(@PathVariable("id") Integer id) {
        Album album = albumService.findById(id);
        return new Result<Album>(true, StatusCode.OK, "查询相册成功", album);
    }

    /**
     * 保存
     *
     * @param album
     * @return
     */
    @PostMapping
    public Result<Album> save(@RequestBody Album album) {
        albumService.save(album);
        return new Result<Album>(true, StatusCode.OK, "保存相册成功");

    }

    /**
     * 更新
     *
     * @param album
     * @return
     */
    @PutMapping
    public Result<Album> update(@RequestBody Album album) {
        albumService.update(album);
        return new Result<Album>(true, StatusCode.OK, "更新相册成功");

    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Album> delete(@PathVariable("id") Long id) {
        albumService.deleteById(id);
        return new Result<Album>(true, StatusCode.OK, "删除相册成功");
    }

    /**
     * 条件查询相册
     *
     * @param album
     * @return
     */
    @PostMapping("/search")
    public Result<List<Album>> findByAlbum(@RequestBody Album album) {
        List<Album> albums = albumService.findByAlbum(album);
        return new Result<List<Album>>(true, StatusCode.OK, "条件查询相册成功", albums);
    }

    /**
     * 分页查询相册
     *
     * @param pageNum
     * @param size
     * @return
     */
    @GetMapping("/search/{pageNum}/{size}")
    public Result<PageInfo<Album>> findPage(@PathVariable("pageNum") Integer pageNum, @PathVariable("size") Integer size) {
        PageInfo<Album> pageInfo = albumService.findPage(pageNum, size);
        return new Result<PageInfo<Album>>(true, StatusCode.OK, "分页查询相册成功", pageInfo);
    }

    /**
     * 分页条件查询相册
     *
     * @param album
     * @param pageNum
     * @param size
     * @return
     */
    @PostMapping("/search/{pageNum}/{size}")
    public Result<PageInfo<Album>> findPage(@RequestBody Album album, @PathVariable("pageNum") Integer pageNum, @PathVariable("size") Integer size) {
        PageInfo<Album> pageInfo = albumService.findPageByAlbum(album, pageNum, size);
        return new Result<PageInfo<Album>>(true, StatusCode.OK, "分页条件查询相册成功", pageInfo);
    }

}
