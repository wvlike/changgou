package com.ismyself.pay.service;

import java.util.Map;

/**
 * package com.ismyself.pay.service;
 *
 * @auther txw
 * @create 2019-09-09  21:46
 * @description：
 */
public interface WeChatPayService {

    /**
     * 创建二维码
     * @param parameterMap
     * @return
     */
    Map createNative(Map<String,String> parameterMap);

    /**
     * 查询订单是否支付成功
     * @param out_trade_no
     * @return
     */
    Map queryPayStatus(String out_trade_no);

    /**
     * 关闭订单
     * @param out_trade_no
     * @return
     */
    Map closeOrder(String out_trade_no);

}
