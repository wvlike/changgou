package com.ismyself.search.controller;

import com.ismyself.search.feign.SkuFeign;
import com.ismyself.search.pojo.SkuInfo;
import entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * package com.ismyself.search.controller;
 *
 * @auther txw
 * @create 2019-09-02  20:45
 * @description：
 */
@Controller
@RequestMapping("/search")
public class SkuController {

    @Autowired
    private SkuFeign skuFeign;

    @GetMapping("/list")
    public String search(@RequestParam(required = false) Map<String, String> searchMap, Model model) {
        //替换特殊字符
        handlerSearchMap(searchMap);
        //搜索条件map集合
        Map<String, Object> map = skuFeign.search(searchMap);
        //查询所有的数据
        model.addAttribute("result", map);
        //搜索条件
        model.addAttribute("searchMap", searchMap);
        //获取url
        String[] urls = getUrl(searchMap);
        model.addAttribute("url", urls[0]);
        model.addAttribute("sortUrl", urls[1]);

        Page<SkuInfo> page = new Page<SkuInfo>(
                Long.parseLong(map.get("total").toString()),
                Integer.parseInt(map.get("pageNumber").toString()) + 1,
                Integer.parseInt(map.get("pageSize").toString())
        );
        model.addAttribute("page", page);


        return "search";
    }

    /**
     * 实现+加号的问题
     * @param searchMap
     */
    private void handlerSearchMap(Map<String, String> searchMap) {
        for (Map.Entry<String, String> entry : searchMap.entrySet()) {
            if (entry.getKey().startsWith("spec_")){
                entry.setValue(entry.getValue().replace("+","%2B"));
            }
        }
    }

    /**
     * 处理url
     * @param searchMap
     * @return
     */
    public String[] getUrl(Map<String, String> searchMap) {
        String url = "/search/list";
        String sortUrl = "/search/list";

        if (searchMap != null && searchMap.size() > 0) {
            url += "?";
            sortUrl += "?";
            for (Map.Entry<String, String> entry : searchMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equalsIgnoreCase("pageNum")){
                    continue;
                }
                url += key + "=" + value + "&";

                if (key.equals("sortRule") || key.equals("sortField")) {
                    continue;
                }
                sortUrl += key + "=" + value + "&";
            }
            url = url.substring(0, url.length() - 1);
            sortUrl = sortUrl.substring(0, sortUrl.length() - 1);
            if (url.contains("&&")) {
                url.replace("&&", "&");
            }
            if (sortUrl.contains("&&")) {
                sortUrl.replace("&&", "&");
            }
        }
        return new String[]{url, sortUrl};
    }

}
