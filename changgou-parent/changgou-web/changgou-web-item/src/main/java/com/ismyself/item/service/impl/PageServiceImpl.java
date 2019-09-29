package com.ismyself.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.ismyself.goods.feign.CategoryFeign;
import com.ismyself.goods.feign.SkuFeign;
import com.ismyself.goods.feign.SpuFeign;
import com.ismyself.goods.feign.TemplateFeign;
import com.ismyself.goods.pojo.Sku;
import com.ismyself.goods.pojo.Spu;
import com.ismyself.item.service.PageService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * package com.ismyself.item.service.impl;
 *
 * @auther txw
 * @create 2019-09-03  23:22
 * @description：
 */
@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private SpuFeign spuFeign;

    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private TemplateEngine templateEngine;

    //生成静态文件路径
    @Value("${pagepath}")
    private String pagepath;

    /**
     * 生成静态页面
     * @param spuId
     */
    @Override
    public void createPageHTML(Long spuId) {
        //1.上下文
        Context context = new Context();
        Map<String, Object> dataModel = buildDataModel(spuId);
        context.setVariables(dataModel);
        //2.准备文件
        File dir = new File(pagepath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        //3.生成页面
        File dest = new File(dir,spuId+".html");
        try (PrintWriter writer = new PrintWriter(dest,"UTF-8")){
            templateEngine.process("item",context,writer);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 构建数据模型
     * @param spuid
     * @return
     */
    public Map<String, Object> buildDataModel(Long spuid) {
        //构建数据模型
        Map<String,Object> dataMap = new HashMap<>();
        //获取spu和sku列表
        Result<Spu> result = spuFeign.findById(spuid);
        Spu spu = result.getData();

        //获取分类信息
        dataMap.put("category1",categoryFeign.findById(spu.getCategory1Id()).getData());
        dataMap.put("category2",categoryFeign.findById(spu.getCategory2Id()).getData());
        dataMap.put("category3",categoryFeign.findById(spu.getCategory3Id()).getData());

        if (spu.getImages() != null){
            dataMap.put("imageList",spu.getImages().split(","));
        }

        dataMap.put("specificationList", JSON.parseObject(spu.getSpecItems(),Map.class));
        dataMap.put("spu",spu);

        //根据spiId查询sku集合
        Sku skuCondition = new Sku();
        skuCondition.setSpuId(spu.getId());
        Result<List<Sku>> resultSku = skuFeign.findList(skuCondition);
        dataMap.put("skuList",resultSku.getData());
        return dataMap;
    }
}
