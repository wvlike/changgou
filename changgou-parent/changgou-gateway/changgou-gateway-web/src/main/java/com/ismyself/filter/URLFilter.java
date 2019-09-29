package com.ismyself.filter;

/**
 * package com.ismyself.filter;
 *
 * @auther txw
 * @create 2019-09-07  22:57
 * @description：
 */
public class URLFilter {

    public static final String orderFilterPath = "/api/cart/**,/api/categoryReport/**,/api/orderConfig/**,/api/order/**,/api/orderItem/**,/api/orderLog/**,/api/preferential/**,/api/returnCause/**,/api/returnOrder/**,/api/returnOrderItem/**";


    public static boolean hasAuthorize(String uri) {
        String[] urls = orderFilterPath.replace("**","").split(",");
        for (String url : urls) {
            if (url.equals(uri)) {
                return true;
            }
        }
        return false;
    }

}
