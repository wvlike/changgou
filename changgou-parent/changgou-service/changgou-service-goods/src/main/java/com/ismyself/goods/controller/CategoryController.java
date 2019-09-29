package com.ismyself.goods.controller;

import com.github.pagehelper.PageInfo;
import com.ismyself.goods.pojo.Category;
import com.ismyself.goods.service.CategoryService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * package com.ismyself.goods.controller;
 *
 * @auther txw
 * @create 2019-08-27  21:45
 * @description：种类的controller
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询所有种类
     *
     * @return
     */
    @GetMapping
    public Result<List<Category>> findAll() {
        List<Category> categories = categoryService.findAll();
        return new Result<List<Category>>(true, StatusCode.OK, "查询所有种类成功", categories);
    }

    /**
     * 根据id查询种类
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Category> findById(@PathVariable("id") Integer id) {
        Category category = categoryService.findById(id);
        return new Result<Category>(true, StatusCode.OK, "根据id查询种类成功", category);
    }

    /**
     * 保存种类
     *
     * @param category
     * @return
     */
    @PostMapping
    public Result<Category> save(@RequestBody Category category) {
        categoryService.save(category);
        return new Result<Category>(true, StatusCode.OK, "保存种类成功");
    }

    /**
     * 更新种类
     *
     * @param category
     * @return
     */
    @PutMapping
    public Result<Category> update(@RequestBody Category category) {
        categoryService.update(category);
        return new Result<Category>(true, StatusCode.OK, "更新种类成功");
    }

    /**
     * 删除种类
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Category> delete(@PathVariable("id") Integer id) {
        categoryService.delete(id);
        return new Result<Category>(true, StatusCode.OK, "删除种类成功");
    }

    /**
     * 根据父id查询种类
     *
     * @param pid
     * @return
     */
    @GetMapping("/list/{pid}")
    public Result<List<Category>> findByParentId(@PathVariable("pid") Integer pid) {
        List<Category> categories = categoryService.findByParentId(pid);
        return new Result<List<Category>>(true, StatusCode.OK, "根据父id查询种类成功", categories);
    }

    /**
     * 分页查询所有种类
     *
     * @param pageNum
     * @param size
     * @return
     */
    @GetMapping("/search/{pageNum}/{size}")
    public Result<PageInfo<Category>> findByParentId(@PathVariable("pageNum") Integer pageNum, @PathVariable("size") Integer size) {
        PageInfo<Category> pageInfo = categoryService.findPage(pageNum, size);
        return new Result<PageInfo<Category>>(true, StatusCode.OK, "分页查询所有种类成功", pageInfo);
    }

    /**
     * 条件查询所有种类
     *
     * @param category
     * @return
     */
    @PostMapping("/search")
    public Result<List<Category>> findByParentId(@RequestBody Category category) {
        List<Category> categories = categoryService.findByCategory(category);
        return new Result<List<Category>>(true, StatusCode.OK, "条件查询所有种类成功", categories);
    }


    /**
     * 分页条件查询种类
     * @param category
     * @param pageNum
     * @param size
     * @return
     */
    @PostMapping("/search/{pageNum}/{size}")
    public Result<List<Category>> findPageByParentId(@RequestBody Category category, @PathVariable("pageNum") Integer pageNum, @PathVariable("size") Integer size) {
        PageInfo<Category> pageInfo = categoryService.findPageByCategory(category, pageNum, size);
        return new Result<List<Category>>(true, StatusCode.OK, "分页条件查询种类成功", pageInfo);
    }
}
