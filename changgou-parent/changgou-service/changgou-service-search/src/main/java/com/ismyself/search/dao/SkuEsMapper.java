package com.ismyself.search.dao;

import com.ismyself.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * package com.ismyself.search.dao;
 *
 * @auther txw
 * @create 2019-08-30  20:07
 * @description：SkuES操作的dao
 * 继承ElasticsearchRepository从而可以实现对ES操作
 */
@Repository
public interface SkuEsMapper extends ElasticsearchRepository<SkuInfo, Long> {
}
