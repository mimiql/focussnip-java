package org.oss.focussnip.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.oss.focussnip.common.BaseResponse;
import org.oss.focussnip.constant.OrderConstant;
import org.oss.focussnip.model.Goods;
import org.oss.focussnip.model.Orders;
import org.oss.focussnip.service.GoodsService;
import org.oss.focussnip.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiresPermissions("user:all")
@Api("订单api")
@RestController
@RequestMapping("order")
@CrossOrigin(origins = "*")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    /**
     * 先创建订单，再付款（付款是请求另一个url）
     * @param goodId
     * @return
     */
    @ApiOperation("创建订单Api")
    @PostMapping("/create")
    public BaseResponse insertOrder(@RequestParam String goodId) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        Goods goods = goodsService.getGoodsByGoodsId(goodId);

        Orders order = new Orders();
        order.setUsername(username);
        LocalDateTime localDateTime = LocalDateTime.now();
        order.setCreatedTime(localDateTime);
        order.setStatus(OrderConstant.ORDER_UNPAYED);
        order.setDescription(goods.getDescription());
        order.setGoodsId(goodId);
        orderService.insertOrder(order);
        return BaseResponse.getSuccessResponse(order);
    }

    @ApiOperation("付款api")
    @PostMapping("/pay")
    public BaseResponse pay(@RequestParam("orderId") int orderId) {
        Orders order = orderService.findOrderById(orderId);
        order.setStatus(OrderConstant.ORDER_PAYED);
        order.setPayedTime(LocalDateTime.now());
        orderService.updateOrder(order);
        return BaseResponse.getSuccessResponse(order);
    }

    @ApiOperation("查询用户的所有订单")
    @GetMapping("selectAll")
    public BaseResponse selectOrdersByUserId() {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        Page<Orders> orders = orderService.findOrdersByUsername(username);
        return BaseResponse.getSuccessResponse(orders);
    }

    @ApiOperation("模糊查询用户订单")
    @GetMapping("selectLike")
    public BaseResponse selectOrdersByDescriptionLike(
            @RequestParam("key") String key) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        Page<Orders> orders = orderService.findOrdersByDecriptionLike(username, key);
        return BaseResponse.getSuccessResponse(orders);
    }

    @ApiOperation("根据Id删除订单")
    @DeleteMapping("deleteByOrderId")
    public BaseResponse deleteByOrderId(@RequestParam("orderId") int orderId) {
        orderService.deleteOrderByOrderId(orderId);
        return BaseResponse.getSuccessResponse(null);
    }

}
