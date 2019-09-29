package com.ismyself.filter;

import com.ismyself.utils.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * package com.ismyself.filter;
 *
 * @auther txw
 * @create 2019-09-04  19:38
 * @description：
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {


    private static final String AUTHORIZE_TOKEN = "Authorization";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取request、response对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //获取请求的URI
        String path = request.getURI().getPath();

        String uri = request.getURI().toString();

        //如果是登录、goods等开放的微服务[这里的goods部分开放],则直接放行,这里不做完整演示，完整演示需要设计一套权限系统
        if (path.startsWith("/api/user/login") || path.startsWith("/api/brand/search") || path.startsWith("/api/brand")||!URLFilter.hasAuthorize(uri)) {
            //放行
            Mono<Void> filter = chain.filter(exchange);
            return filter;
        }


        //从头文件中获取令牌信息
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        //true: 令牌在头文件中  false：令牌不在头文件中->将令牌封装到头文件中，再传递给其他服务
        boolean hasToken = true;

        //从请求参数中获取令牌信息
        if (StringUtils.isEmpty(token)) {
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
            hasToken = false;
        }

        //从cookie中获取令牌信息
        if (StringUtils.isEmpty(token)) {
            HttpCookie cookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
            if (cookie != null) {
                token = cookie.getValue();
            }
        }

        //没有令牌就拦截
        if (StringUtils.isEmpty(token)) {
            //设置没有权限状态码，401
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //响应空数据
            return response.setComplete();
        } else {
            //将令牌封装到头文件中
            if (!hasToken) {
                //如果Authorization不是以bearer 开头，就给其添加
                if (!"bearer ".startsWith(token.toLowerCase())) {
                    token = "bearer " + token;
                }
                request.mutate().header(AUTHORIZE_TOKEN, "bearer " + token);
            }
        }

/*

        try {

            //如果有令牌，则校验
            JwtUtil.parseJWT(token);

        } catch (Exception e) {
            //设置没有权限状态码，401
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //响应空数据
            return response.setComplete();
        }
*/
        return chain.filter(exchange);
    }


    /**
     * 过滤器执行顺序
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority (somewhat
     * analogous to Servlet {@code load-on-startup} values).
     * <p>Same order values will result in arbitrary sort positions for the
     * affected objects.
     *
     * @return the order value
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
