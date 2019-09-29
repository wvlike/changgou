package com.ismyself.goods.controller;

import com.github.pagehelper.PageInfo;
import com.ismyself.goods.pojo.Para;
import com.ismyself.goods.service.ParaService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * package com.ismyself.goods.controller;
 *
 * @auther txw
 * @create 2019-08-26  12:53
 * @description：参数的controller
 */
@RestController
@RequestMapping("/para")
@CrossOrigin
public class ParaController {
    
    @Autowired
    private ParaService paraService;

    /**
     * 根据categoryId查询所有参数
     * @param id
     * @return
     */
    @GetMapping("/category/{id}")
    public Result<List<Para>> findParasByCategoryId(@PathVariable("id") Integer id){
        List<Para> paras = paraService.findParasByCategoryId(id);
        return new Result<List<Para>>(true, StatusCode.OK, "根据categoryId查询所有参数成功", paras);
    }

    /**
     * 查询所有参数
     *
     * @return
     */
    @GetMapping
    public Result<List<Para>> findAll() {
        List<Para> paras = paraService.findAll();
        return new Result<List<Para>>(true, StatusCode.OK, "查询所有参数成功", paras);
    }

    /**
     * 根据id查询参数
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Para> findById(@PathVariable("id") Integer id) {
        Para para = paraService.findById(id);
        return new Result<Para>(true, StatusCode.OK, "查询参数成功", para);
    }

    /**
     * 保存参数
     *
     * @param para
     * @return
     */
    @PostMapping
    public Result<Para> savePara(@RequestBody Para para) {
        paraService.save(para);
        return new Result<Para>(true, StatusCode.OK, "新增参数成功");
    }

    /**
     * 修改参数
     *
     * @param para
     * @return
     */
    @PutMapping
    public Result<Para> updatePara(@RequestBody Para para) {
        paraService.update(para);
        return new Result<Para>(true, StatusCode.OK, "修改参数成功");
    }

    /**
     * 根据id删除参数
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Para> deletePara(@PathVariable("id") Integer id) {
        paraService.delete(id);
        return new Result<Para>(true, StatusCode.OK, "删除参数成功");
    }

    /**
     * 根据条件查询参数
     *
     * @param para
     * @return
     */
    @PostMapping("/search")
    public Result<List<Para>> findByPara(@RequestBody Para para) {
        List<Para> paras = paraService.findByPara(para);
        return new Result<List<Para>>(true, StatusCode.OK, "条件查询参数成功", paras);
    }

    /**
     * 分页查询所有参数
     *
     * @param pageNum
     * @param size
     * @return
     */
    @GetMapping("/search/{pageNum}/{size}")
    public Result<PageInfo<Para>> findPage(@PathVariable("pageNum") Integer pageNum, @PathVariable("size") Integer size) {
        PageInfo<Para> pageInfo = paraService.findPage(pageNum, size);
        return new Result<PageInfo<Para>>(true, StatusCode.OK, "分页查询参数成功", pageInfo);
    }

    /**
     * 条件分页查询参数
     *
     * @param para
     * @param pageNum
     * @param size
     * @return
     */
    @PostMapping("/search/{pageNum}/{size}")
    public Result<PageInfo<Para>> findPageByPara(@RequestBody Para para, @PathVariable("pageNum") Integer pageNum, @PathVariable("size") Integer size) {
        PageInfo<Para> pageInfo = paraService.findPageByPara(para, pageNum, size);
        return new Result<PageInfo<Para>>(true, StatusCode.OK, "条件分页查询参数成功", pageInfo);
    }




}
