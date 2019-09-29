package com.ismyself.goods.controller;


import com.github.pagehelper.PageInfo;
import com.ismyself.goods.pojo.Brand;
import com.ismyself.goods.service.BrandService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * package com.ismyself.goods.controller;
 *
 * @auther txw
 * @create 2019-08-24  19:44
 * @description：品牌的controller
 */
@RestController
@RequestMapping("/brand")
@CrossOrigin
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 查询所有品牌
     *
     * @return
     */
    @GetMapping
    public Result<List<Brand>> findAll() {
        List<Brand> list = brandService.findAll();
        return new Result<List<Brand>>(true, StatusCode.OK, "查询所有品牌成功", list);
    }

    /**
     * 根据id查询品牌
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Brand> findById(@PathVariable("id") Integer id) {
        Brand byId = brandService.findById(id);
        return new Result<Brand>(true, StatusCode.OK, "查询品牌成功", byId);
    }

    /**
     * 保存品牌
     *
     * @param brand
     * @return
     */
    @PostMapping
    public Result<Brand> saveBrand(@RequestBody Brand brand) {
        brandService.save(brand);
        return new Result<Brand>(true, StatusCode.OK, "新增品牌成功");
    }

    /**
     * 修改品牌
     *
     * @param brand
     * @return
     */
    @PutMapping
    public Result<Brand> updateBrand(@RequestBody Brand brand) {
        brandService.update(brand);
        return new Result<Brand>(true, StatusCode.OK, "修改品牌成功");
    }

    /**
     * 根据id删除品牌
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Brand> deleteBrand(@PathVariable("id") Integer id) {
        brandService.delete(id);
        return new Result<Brand>(true, StatusCode.OK, "删除品牌成功");
    }

    /**
     * 根据条件查询品牌
     *
     * @param brand
     * @return
     */
    @PostMapping("/search")
    public Result<List<Brand>> findByBrand(@RequestBody Brand brand) {
        List<Brand> brands = brandService.findByBrand(brand);
        return new Result<List<Brand>>(true, StatusCode.OK, "条件查询品牌成功", brands);
    }

    /**
     * 分页查询所有品牌
     *
     * @param pageNum
     * @param size
     * @return
     */
    @GetMapping("/search/{pageNum}/{size}")
    public Result<PageInfo<Brand>> findPage(@PathVariable("pageNum") Integer pageNum, @PathVariable("size") Integer size) {
        PageInfo<Brand> pageInfo = brandService.findPage(pageNum, size);
        return new Result<PageInfo<Brand>>(true, StatusCode.OK, "分页查询品牌成功", pageInfo);
    }

    /**
     * 条件分页查询品牌
     *
     * @param brand
     * @param pageNum
     * @param size
     * @return
     */
    @PostMapping("/search/{pageNum}/{size}")
    public Result<PageInfo<Brand>> findPageByBrand(@RequestBody Brand brand, @PathVariable("pageNum") Integer pageNum, @PathVariable("size") Integer size) {
        PageInfo<Brand> pageInfo = brandService.findPageByBrand(brand, pageNum, size);
        return new Result<PageInfo<Brand>>(true, StatusCode.OK, "条件分页查询品牌成功", pageInfo);
    }

    /**
     * 根据category分类ID查询Brand
     *
     * @param id
     * @return
     */
    @GetMapping("/category/{id}")
    public Result<List<Brand>> findByCategotyId(@PathVariable("id") Integer id) {
        List<Brand> brands = brandService.findByCategotyId(id);
        return new Result<List<Brand>>(true, StatusCode.OK, "根据分类ID查询品牌成功", brands);
    }
}
