package org.oss.focussnip.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.oss.focussnip.common.BaseResponse;
import org.oss.focussnip.constant.OrderConstant;
import org.oss.focussnip.model.Orders;
import org.oss.focussnip.service.OrderService;
import org.oss.focussnip.utils.OrderIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Api("订单api")
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 先创建订单，再付款（付款是请求另一个url）
     * @param order
     * @return
     */
    @ApiOperation("创建订单Api")
    @PostMapping("/create")
    public BaseResponse insertOrder(@RequestBody Orders order) {
        order.setOrderId(OrderIdUtil.getOrderId());

        LocalDateTime localDateTime = LocalDateTime.now();
        order.setCreatedTime(localDateTime);
        order.setStatus(OrderConstant.ORDER_UNPAYED);
        orderService.insertOrder(order);
        return BaseResponse.getSuccessResponse(order);
    }

    @ApiOperation("付款api")
    @PostMapping("/pay")
    public BaseResponse pay(@RequestParam("orderId") String orderId) {
        Orders order = orderService.findOrderById(orderId);
        order.setStatus(OrderConstant.ORDER_PAYED);
        order.setPayedTime(LocalDateTime.now());
        orderService.updateOrder(order);
        return BaseResponse.getSuccessResponse(order);
    }

    @ApiOperation("查询用户的所有订单")
    @GetMapping("selectAll")
    public BaseResponse selectOrdersByUserId(@RequestParam("userId") int userId) {
        Page<Orders> orders = orderService.findOrdersByUserId(userId);
        return BaseResponse.getSuccessResponse(orders);
    }

    @ApiOperation("模糊查询用户订单")
    @GetMapping("selectLike")
    public BaseResponse selectOrdersByDescriptionLike(
            @RequestParam("userId") int userId,
            @RequestParam("key") String key) {

        Page<Orders> orders = orderService.findOrdersByDecriptionLike(userId, key);
        return BaseResponse.getSuccessResponse(orders);
    }

    @ApiOperation("根据Id删除订单")
    @DeleteMapping("deleteByOrderId")
    public BaseResponse deleteByOrderId(@RequestParam("orderId") int orderId) {
        orderService.deleteOrderByOrderId(orderId);
        return BaseResponse.getSuccessResponse(null);
    }

}
