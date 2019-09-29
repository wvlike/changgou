package com.ismyself.order.controller;

import com.ismyself.order.pojo.OrderItem;
import com.ismyself.order.service.CartService;
import entity.Result;
import entity.StatusCode;
import entity.TokenDecode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * package com.ismyself.order.controller;
 *
 * @auther txw
 * @create 2019-09-07  19:32
 * @description：
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 添加购物车
     *
     * @param num
     * @param id
     * @return
     */
    @RequestMapping("/add")
    public Result add(Integer num, Long id) {
        Map<String, String> userInfo = TokenDecode.getUserInfo();
        String username = userInfo.get("username");

        try {

//            username = "wvlike";
            cartService.add(num, id, username);
            return new Result(true, StatusCode.OK, "添加购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "添加购物车失败");
        }

    }

    /**
     * 查询购物车
     *
     * @return
     */
    @GetMapping(value = "/list")
    public Result list() {
        Map<String, String> userInfo = TokenDecode.getUserInfo();
        String username = userInfo.get("username");

        try {
//            String username = "wvlike";
            List<OrderItem> orderItems = cartService.list(username);
            if (orderItems.size() > 0 && orderItems != null) {
                return new Result(true, StatusCode.OK, "查询购物车成功", orderItems);
            }else {
                return new Result(false, StatusCode.ERROR, "购物车为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "查询购物车失败");
        }
    }

}
