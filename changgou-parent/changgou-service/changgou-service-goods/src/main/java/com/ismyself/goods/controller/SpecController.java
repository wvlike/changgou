package com.ismyself.goods.controller;

import com.github.pagehelper.PageInfo;
import com.ismyself.goods.pojo.Spec;
import com.ismyself.goods.service.SpecService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * package com.ismyself.goods.controller;
 *
 * @auther txw
 * @create 2019-08-26  12:35
 * @description：规格的controller
 */
@RestController
@RequestMapping("/spec")
@CrossOrigin
public class SpecController {

    @Autowired
    private SpecService specService;

    /**
     * 根据categotyId查询所有规格
     * @param id
     * @return
     */
    @GetMapping("/categoty/{id}")
    public Result<List<Spec>> findByCategotyId(@PathVariable("id") Integer id){
        List<Spec> specs = specService.findByCategotyId(id);
        return new Result<List<Spec>>(true, StatusCode.OK, "根据categotyId查询所有规格成功", specs);
    }

    /**
     * 查询所有规格
     *
     * @return
     */
    @GetMapping
    public Result<List<Spec>> findAll() {
        List<Spec> specs = specService.findAll();
        return new Result<List<Spec>>(true, StatusCode.OK, "查询所有规格成功", specs);
    }

    /**
     * 根据id查询规格
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Spec> findById(@PathVariable("id") Integer id) {
        Spec spec = specService.findById(id);
        return new Result<Spec>(true, StatusCode.OK, "查询规格成功", spec);
    }

    /**
     * 保存规格
     *
     * @param spec
     * @return
     */
    @PostMapping
    public Result<Spec> saveSpec(@RequestBody Spec spec) {
        specService.save(spec);
        return new Result<Spec>(true, StatusCode.OK, "新增规格成功");
    }

    /**
     * 修改规格
     *
     * @param spec
     * @return
     */
    @PutMapping
    public Result<Spec> updateSpec(@RequestBody Spec spec) {
        specService.update(spec);
        return new Result<Spec>(true, StatusCode.OK, "修改规格成功");
    }

    /**
     * 根据id删除规格
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Spec> deleteSpec(@PathVariable("id") Integer id) {
        specService.delete(id);
        return new Result<Spec>(true, StatusCode.OK, "删除规格成功");
    }

    /**
     * 根据条件查询规格
     *
     * @param spec
     * @return
     */
    @PostMapping("/search")
    public Result<List<Spec>> findBySpec(@RequestBody Spec spec) {
        List<Spec> specs = specService.findBySpec(spec);
        return new Result<List<Spec>>(true, StatusCode.OK, "条件查询规格成功", specs);
    }

    /**
     * 分页查询所有规格
     *
     * @param pageNum
     * @param size
     * @return
     */
    @GetMapping("/search/{pageNum}/{size}")
    public Result<PageInfo<Spec>> findPage(@PathVariable("pageNum") Integer pageNum, @PathVariable("size") Integer size) {
        PageInfo<Spec> pageInfo = specService.findPage(pageNum, size);
        return new Result<PageInfo<Spec>>(true, StatusCode.OK, "分页查询规格成功", pageInfo);
    }

    /**
     * 条件分页查询规格
     *
     * @param spec
     * @param pageNum
     * @param size
     * @return
     */
    @PostMapping("/search/{pageNum}/{size}")
    public Result<PageInfo<Spec>> findPageBySpec(@RequestBody Spec spec, @PathVariable("pageNum") Integer pageNum, @PathVariable("size") Integer size) {
        PageInfo<Spec> pageInfo = specService.findPageBySpec(spec, pageNum, size);
        return new Result<PageInfo<Spec>>(true, StatusCode.OK, "条件分页查询规格成功", pageInfo);
    }
    
}
