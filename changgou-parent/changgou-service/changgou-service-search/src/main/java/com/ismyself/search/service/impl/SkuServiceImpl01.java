package com.ismyself.search.service.impl;


import com.alibaba.fastjson.JSON;
import com.ismyself.goods.feign.SkuFeign;
import com.ismyself.goods.pojo.Sku;
import com.ismyself.search.dao.SkuEsMapper;
import com.ismyself.search.pojo.SkuInfo;
import com.ismyself.search.service.SkuService;
import entity.Result;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * package com.ismyself.search.service.impl;
 *
 * @auther txw
 * @create 2019-08-30  20:08
 * @description：SkuES操作的service实现类
 *
 * 优化前的代码
 */
//@Service("SkuService")
public class SkuServiceImpl01 implements SkuService {

    @Autowired
    private SkuEsMapper skuEsMapper;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 导入数据到索引库
     */
    @Override
    public void importSku() {
        //先是通过SkuFeign查询所需要的结果
        Result<List<Sku>> result = skuFeign.findByStatus("1");
        List<Sku> skuList = result.getData();
        //将List<Sku>转换成List<SkuInfo>
        List<SkuInfo> skuInfoList = JSON.parseArray(JSON.toJSONString(skuList), SkuInfo.class);
        //将SkuInfo中spec转换成对象的Map，于是就是显现标题字段搜索
        for (SkuInfo skuInfo : skuInfoList) {
            String spec = skuInfo.getSpec();
            Map<String, Object> specMap = JSON.parseObject(spec);
            skuInfo.setSpecMap(specMap);
        }
        //执行保存分词操作
        skuEsMapper.saveAll(skuInfoList);
    }

    /**
     * 根据条件进行搜索
     *
     * @param searchMap
     * @return
     */
    @Override
    public Map search(Map<String, String> searchMap) {
        //查询条件封装
        NativeSearchQueryBuilder nativeSearchQueryBuilder = buildBasicQuery(searchMap);
        //处理list结果，并将结果封装到map中
        Map<String, Object> resultMap = searchList(nativeSearchQueryBuilder);
        //查找分类集合，并将分类放入map中
        if (searchMap == null || StringUtils.isEmpty(searchMap.get("category"))) {
            List<String> categoryList = searchCategoryList(nativeSearchQueryBuilder);
            resultMap.put("categoryList", categoryList);
        }
        //查询品牌集合，将其封装到map中
        if (searchMap == null || StringUtils.isEmpty(searchMap.get("brand"))) {
            List<String> brandList = searchBrandList(nativeSearchQueryBuilder);
            resultMap.put("brandList", brandList);
        }
        //所有规格参数，将其封装到map中
        Map<String, Set<String>> specList = searchSpecList(nativeSearchQueryBuilder);
        resultMap.put("specList", specList);
        return resultMap;
    }

    /**
     * 所有规格数据的查询
     *
     * @param nativeSearchQueryBuilder
     * @return
     */
    public Map<String, Set<String>> searchSpecList(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        //定义一个别名
        String spec = "group_by_spec";
        nativeSearchQueryBuilder.addAggregation(
                //根据spec进行分组,此处注意是spec.keyword，不分词的查询，不然会报错
                AggregationBuilders.terms(spec).field("spec.keyword").size(10000)
        );
        //获得结果
        AggregatedPage<SkuInfo> specPage = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class);
        //获得一个存储了分类后的结果
        StringTerms stringTerms = specPage.getAggregations().get(spec);
        //存入结果的list
        List<String> specList = new ArrayList<>();
        for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
            //遍历获取每个分类名称，将其存入结果list
            specList.add(bucket.getKeyAsString());
        }
        //执行处理成Map<String, Set<String>>结果
        return specPutAll(specList);
    }

    /**
     * 执行处理成Map<String, Set<String>>
     *
     * @param specList
     * @return
     */
    public Map<String, Set<String>> specPutAll(List<String> specList) {
        //创建一个map
        Map<String, Set<String>> map = new HashMap<>();
        for (String spec : specList) {
            //遍历每条spec，将其转换成specMap
            Map<String, String> specMap = JSON.parseObject(spec, Map.class);
            for (String key : specMap.keySet()) {
                //遍历specMap，取出key与value
                String value = specMap.get(key);
                //根据key取出结果map中的set集合
                Set<String> stringSet = map.get(key);
                if (stringSet == null) {
                    //为空就新建
                    stringSet = new HashSet<>();
                }
                //将value添加到set集合中
                stringSet.add(value);
                //存入key和set集合
                map.put(key, stringSet);
            }
        }
        return map;
    }

    /**
     * 查询品牌集合
     *
     * @param nativeSearchQueryBuilder
     * @return
     */
    public List<String> searchBrandList(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        //定义一个别名
        String brand = "group_by_brand";
        nativeSearchQueryBuilder.addAggregation(
                //根据brand进行分组
                AggregationBuilders.terms(brand).field("brandName")
        );
        //获得结果
        AggregatedPage<SkuInfo> brandPage = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class);
        //获得一个存储了分类后的结果
        StringTerms stringTerms = brandPage.getAggregations().get(brand);
        //存入结果的list
        List<String> brandList = new ArrayList<>();
        for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
            //遍历获取每个分类名称，将其存入结果list
            brandList.add(bucket.getKeyAsString());
        }
        return brandList;
    }

    /**
     * 分类查找分类名集合
     *
     * @param nativeSearchQueryBuilder
     * @return
     */
    public List<String> searchCategoryList(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        //定义一个别名
        String category = "group_by_category";
        nativeSearchQueryBuilder.addAggregation(
                //根据category进行分组
                AggregationBuilders.terms(category).field("categoryName")
        );
        //获得结果
        AggregatedPage<SkuInfo> categoryPage = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class);
        //获得一个存储了分类后的结果
        StringTerms stringTerms = categoryPage.getAggregations().get(category);
        //存入结果的list
        List<String> categoryList = new ArrayList<>();
        for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
            //遍历获取每个分类名称，将其存入结果list
            categoryList.add(bucket.getKeyAsString());
        }
        return categoryList;
    }

    /**
     * 查询条件封装
     *
     * @param searchMap
     * @return
     */
    public NativeSearchQueryBuilder buildBasicQuery(Map<String, String> searchMap) {


        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        //实现对品牌和分类进行过滤
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (searchMap != null && searchMap.size() > 0) {
            //对于标题关键字搜索过滤
            String keywords = searchMap.get("keywords");
            if (!StringUtils.isEmpty(keywords)) {
                //nativeSearchQueryBuilder.withQuery(QueryBuilders.queryStringQuery(keywords).field("name"));
                boolQueryBuilder.must(QueryBuilders.queryStringQuery(keywords).field("name"));
            }

            //添加分类过滤
            String category = searchMap.get("category");
            if (!StringUtils.isEmpty(category)) {
                boolQueryBuilder.must(QueryBuilders.termQuery("categoryName", category));
            }

            //添加品牌过滤
            String brand = searchMap.get("brand");
            if (!StringUtils.isEmpty(brand)) {
                boolQueryBuilder.must(QueryBuilders.termQuery("brandName", brand));
            }
            //规格过滤
            for (String key : searchMap.keySet()) {
                //对分类条件进行过滤，此时我们需要对前端传过来的数据格式规定
                if (key.startsWith("spec_")) {
                    String value = searchMap.get(key).replace("\\","");
                    if (!StringUtils.isEmpty(value)) {
                        //索引库中的field为specMap.XXX.keyword格式
                        boolQueryBuilder.must(QueryBuilders.termQuery("specMap." + key.substring(5) + ".keyword", value));
                    }
                }
            }
            //价格区间查询
            String price = searchMap.get("price");
            if (!StringUtils.isEmpty(price)) {
                //截取元前面的部分
                price = price.substring(0, price.indexOf("元"));
                //截取成两个数组
                String[] prices = price.split("-");
                //大于第一个
                boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gt(prices[0]));
                if (prices.length == 2) {
                    //有两个参数就小于等于第二个
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("price").lte(prices[1]));
                }
            }

            //搜索排序sortRule和sortField
            String sortField = searchMap.get("sortField");
            String sortRule = searchMap.get("sortRule");
            if (!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(sortRule)) {
                sortRule = sortRule.toUpperCase();
                //根据那个feild进行什么规则的排序
                nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortField).order(SortOrder.valueOf(sortRule)));
            }
        }
        //分页
        Integer pageNum = pageConvert("pageNum", searchMap);
        Integer size = pageConvert("size", searchMap);
        nativeSearchQueryBuilder.withPageable(PageRequest.of(pageNum - 1, size));

        //将过滤queryBuilder添加到nativeSearchQueryBuilder
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);//坑,使用withFilter也能查出数据，但是高亮就没有，但是也不给报错
        return nativeSearchQueryBuilder;
    }


    /**
     * 获取分页条件
     *
     * @param name
     * @param searchMap
     * @return
     */
    private Integer pageConvert(String name, Map<String, String> searchMap) {
        try {
            if (!StringUtils.isEmpty(searchMap.get(name))) {
                return Integer.valueOf(searchMap.get(name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (name.equals("pageNum")) {
            return 1;
        } else {
            return 10;
        }
    }

    /**
     * 处理结果，并将结果封装到map中
     *
     * @param nativeSearchQueryBuilder
     * @return
     */
    public Map<String, Object> searchList(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        //1.指定高亮域，也就是设置哪个域需要高亮显示
        HighlightBuilder.Field field = new HighlightBuilder.Field("name");
        //设置高亮域的时候，需要指定前缀和后缀，也就是关键词用什么html标签包裹，再给该标签样式
        //前缀  <em style="color:red;">
        field.preTags("<em style=\"color:red;\">");
        //后缀 </em>
        field.postTags("</em>");
        //碎片长度  关键词数据的长度
        field.fragmentSize(100);
        //添加高亮
        nativeSearchQueryBuilder.withHighlightFields(field);

        //判断传过来的map集合非空
        //执行获得结果
//        AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class);

        AggregatedPage<SkuInfo> page = elasticsearchTemplate
                .queryForPage(
                        nativeSearchQueryBuilder.build(),       //搜索条件封装
                        SkuInfo.class,                         //数据集合要转换的类型的字节码
                        //SearchResultMapper);                 //执行搜索后，将数据结果集封装到该对象中
                        new SearchResultMapper() {
                            @Override
                            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                                //存储所有转换后的高亮数据对象
                                List<T> list = new ArrayList<T>();
                                //查询获取所有的结果集（非高亮|高亮）
                                for (SearchHit hit : response.getHits()) {
                                    //非高亮数据
                                    SkuInfo skuInfo = JSON.parseObject(hit.getSourceAsString(), SkuInfo.class);
                                    //获取高亮数据,高亮搜索实现
                                    HighlightField highlightField = hit.getHighlightFields().get("name");
                                    if (highlightField != null) {
                                        Text[] fragments = highlightField.getFragments();
                                        if (fragments != null && fragments.length > 0) {
                                            StringBuilder stringBuilder = new StringBuilder();
                                            for (Text text : fragments) {
                                                //拼接所有高亮数据
                                                stringBuilder.append(text.toString());
                                            }
                                            //将非高亮数据替换成高亮数据
                                            skuInfo.setName(stringBuilder.toString());
                                        }
                                    }
                                    list.add((T) skuInfo);
                                }
                                return new AggregatedPageImpl<T>(list, pageable, response.getHits().getTotalHits());
                            }
                        });

        //遍历结果
        Map<String, Object> resultMap = new HashMap<>();
        //集合
        List<SkuInfo> skuInfoList = page.getContent();
        //总记录数
        long totalElements = page.getTotalElements();
        //总页数
        int totalPages = page.getTotalPages();
        //将结果添加到map中
        resultMap.put("rows", skuInfoList);
        resultMap.put("total", totalElements);
        resultMap.put("totalPages", totalPages);


        NativeSearchQuery query = nativeSearchQueryBuilder.build();
        Pageable pageable = query.getPageable();
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();

        resultMap.put("pageSize", pageSize);
        resultMap.put("pageNumber", pageNumber);

        return resultMap;
    }
}
