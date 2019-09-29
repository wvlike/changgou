package com.ismyself.pay.feign;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * package com.ismyself.pay.feign;
 *
 * @auther txw
 * @create 2019-09-10  18:48
 * @description：
 */
@FeignClient(name = "pay")
@RequestMapping("/weixin/pay")
public interface WeChatPayFeign {

    /**
     * 根据商户订单号查询支付结果
     *
     * @param outtradeno
     * @return
     */
    @GetMapping("/status/query")
    Result<Map<String,String>> queryStatus(@RequestParam String outtradeno);

    /**
     * 删除订单
     *
     * @param outtradeno
     * @return
     */
    @GetMapping("/status/close")
    Result<Map> closeOrder(@RequestParam String outtradeno);
}
