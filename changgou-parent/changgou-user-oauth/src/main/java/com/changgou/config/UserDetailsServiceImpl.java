package com.changgou.config;
import com.changgou.util.UserJwt;
import com.ismyself.user.feign.UserFeign;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/*****
 * 自定义授权认证类
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    ClientDetailsService clientDetailsService;

    @Autowired
    private UserFeign userFeign;

    /****
     * 自定义授权认证
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //==============================客户端信息认证 start=======================

        //取出身份，如果身份为空说明没有认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //没有认证统一采用httpbasic认证，httpbasic中存储了client_id和client_secret，开始认证client_id和client_secret
        if (authentication == null) {
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
            if (clientDetails != null) {
                //秘钥
                String clientSecret = clientDetails.getClientSecret();
                //静态方式
//                return new User(username,new BCryptPasswordEncoder().encode(clientSecret), AuthorityUtils.commaSeparatedStringToAuthorityList(""));
                //数据库查找方式
                return new User(username,   //客户端id
                        clientSecret,       //客户端秘钥
                        AuthorityUtils.commaSeparatedStringToAuthorityList(""));    //权限
            }
        }
        //==============================客户端信息认证 end=======================


        //==============================用户账号密码信息认证 start=======================

        if (StringUtils.isEmpty(username)) {
            return null;
        }


        /***
         * 从数据库加载查询用户信息
         * 1:没有令牌，Feign调用之前，生成令牌(admin)
         * 2:Feign调用之前，令牌需要携带过去
         * 3:Feign调用之前，令牌需要存放到Header文件中
         * 4:请求->Feign调用->拦截器RequestInterceptor->Feign调用之前执行拦截
         */

        Result<com.ismyself.user.pojo.User> userResult = userFeign.findById(username);
        if (userResult == null || userResult.getData() == null) {
            return null;
        }

        //根据用户名查询用户信息
        String pwd = userResult.getData().getPassword();
//        String pwd = new BCryptPasswordEncoder().encode("szitheima");
        //创建User对象
        String permissions = "goods_list,seckill_list,admin,vip";
        UserJwt userDetails = new UserJwt(username, pwd, AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));


        //==============================用户账号密码信息认证 end=======================

        return userDetails;
    }
}
