package com.ismyself.user.feign;
import com.github.pagehelper.PageInfo;
import com.ismyself.user.pojo.User;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author:txw
 * @Description:
 */
@FeignClient(name="user")
@RequestMapping("/user")
public interface UserFeign {

    /**
     * 给用户添加积分
     * feign调用时必须加@RequestParam注解
     * @param points
     * @return
     */
    @GetMapping(value = "/points/add")
    Result addUserPoints(@RequestParam(value = "points")Integer points);

    /***
     * User分页条件搜索实现
     * @param user
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    Result<PageInfo> findPage(@RequestBody(required = false) User user, @PathVariable int page, @PathVariable int size);

    /***
     * User分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    Result<PageInfo> findPage(@PathVariable int page, @PathVariable int size);

    /***
     * 多条件搜索品牌数据
     * @param user
     * @return
     */
    @PostMapping(value = "/search" )
    Result<List<User>> findList(@RequestBody(required = false) User user);

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    Result delete(@PathVariable String id);

    /***
     * 修改User数据
     * @param user
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    Result update(@RequestBody User user, @PathVariable String id);

    /***
     * 新增User数据
     * @param user
     * @return
     */
    @PostMapping
    Result add(@RequestBody User user);

    /***
     * 根据ID查询User数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<User> findById(@PathVariable String id);

    /***
     * 查询User全部数据
     * @return
     */
    @GetMapping
    Result<List<User>> findAll();
}