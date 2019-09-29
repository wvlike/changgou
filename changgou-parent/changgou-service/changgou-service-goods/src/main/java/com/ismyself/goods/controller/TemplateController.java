package com.ismyself.goods.controller;

import com.github.pagehelper.PageInfo;
import com.ismyself.goods.pojo.Template;
import com.ismyself.goods.service.TemplateService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * package com.ismyself.goods.controller;
 *
 * @auther txw
 * @create 2019-08-25  21:52
 * @description：模板的controller
 */
@RestController
@RequestMapping("/template")
@CrossOrigin
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    /**
     * 根据categoryId查询模板
     * @param id
     * @return
     */
    @GetMapping("/category/{id}")
    public Result<Template> findByCategoryId(@PathVariable("id") Integer id) {
        Template template = templateService.findByCategoryId(id);
        return new Result<Template>(true, StatusCode.OK, "根据categoryId查询模板成功", template);
    }

    /**
     * 查询所有模板
     *
     * @return
     */
    @GetMapping
    public Result<List<Template>> findAll() {
        List<Template> templates = templateService.findAll();
        return new Result<List<Template>>(true, StatusCode.OK, "查询模板所有成功", templates);
    }

    /**
     * 根据ID查询模板
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Template> findById(@PathVariable("id") Integer id) {
        Template template = templateService.findById(id);
        return new Result<Template>(true, StatusCode.OK, "查询模板成功", template);
    }

    /**
     * 保存模板
     *
     * @param template
     * @return
     */
    @PostMapping
    public Result<Template> findById(@RequestBody Template template) {
        templateService.save(template);
        return new Result<Template>(true, StatusCode.OK, "保存模板成功");
    }

    /**
     * 更新模板
     *
     * @param template
     * @return
     */
    @PutMapping
    public Result<Template> update(@RequestBody Template template) {
        templateService.update(template);
        return new Result<Template>(true, StatusCode.OK, "更新模板成功");
    }

    /**
     * 根据id删除模板
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Template> delete(@PathVariable Long id) {
        templateService.deleteById(id);
        return new Result<Template>(true, StatusCode.OK, "删除模板成功");
    }

    /**
     * 分页查询所有模板
     *
     * @param pageNum
     * @param size
     * @return
     */
    @GetMapping("/search/{pageNum}/{size}")
    public Result<PageInfo<Template>> findPage(@PathVariable("pageNum") Integer pageNum, @PathVariable("size") Integer size) {
        PageInfo<Template> pageInfo = templateService.findPage(pageNum, size);
        return new Result<PageInfo<Template>>(true, StatusCode.OK, "分页查询所有模板成功",pageInfo);
    }

    /**
     * 条件查询所有模板
     *
     * @param template
     * @return
     */
    @PostMapping("/search")
    public Result<List<Template>> findByTemplate(@RequestBody Template template) {
        List<Template> templates = templateService.findByTemplate(template);
        return new Result<List<Template>>(true, StatusCode.OK, "条件查询所有模板成功", templates);
    }

    /**
     * 分页条件查询所有模板
     * @param template
     * @param pageNum
     * @param size
     * @return
     */
    @PostMapping("/search/{pageNum}/{size}")
    public Result<PageInfo<Template>> findPage(@RequestBody Template template, @PathVariable("pageNum") Integer pageNum, @PathVariable("size") Integer size) {
        PageInfo<Template> pageInfo = templateService.findPageByTemplate(template, pageNum, size);
        return new Result<PageInfo<Template>>(true, StatusCode.OK, "分页条件查询所有模板成功",pageInfo);
    }
}
