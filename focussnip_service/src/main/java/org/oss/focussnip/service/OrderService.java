package org.oss.focussnip.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.oss.focussnip.model.Orders;


public interface OrderService {
    void insertOrder(Orders order);

    int updateOrder(Orders order);

    void deleteOrderByOrderId(int orderId);

    Orders findOrderById(int orderId);

    Page<Orders> findOrdersByUsername(String userName); // 查询用户的所有订单

    Page<Orders> findOrdersByDecriptionLike(String username, String key); // 根据关键字模糊查询用户订单
}
