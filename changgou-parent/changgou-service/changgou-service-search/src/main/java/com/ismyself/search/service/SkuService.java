package com.ismyself.search.service;

import java.util.Map;

/**
 * package com.ismyself.search.service;
 *
 * @auther txw
 * @create 2019-08-30  20:08
 * @description：SkuES操作的service
 */
public interface SkuService {

    /**
     * 导入数据到索引库
     */
    void importSku();

    /**
     * 根据条件进行搜索
     * @param searchMap
     * @return
     */
    Map search(Map<String, String> searchMap);
}
