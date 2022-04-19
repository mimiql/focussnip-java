package org.oss.focussnip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.oss.focussnip.mapper.OrderMapper;
import org.oss.focussnip.model.Orders;
import org.oss.focussnip.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void insertOrder(Orders order) {
        orderMapper.insert(order);
    }

    @Override
    public int updateOrder(Orders order) {
        QueryWrapper<Orders> qw = new QueryWrapper<>();
        qw.eq("order_id", order.getOrderId());
        return orderMapper.update(order, qw);
    }

    @Override
    public void deleteOrderByOrderId(int orderId) {
        QueryWrapper<Orders> qw = new QueryWrapper<>();
        qw.eq("order_id", orderId);
        orderMapper.delete(qw);
    }

    @Override
    public Orders findOrderById(int orderId) {
        QueryWrapper<Orders> qw = new QueryWrapper<Orders>();
        qw.eq("order_id", orderId);
        return orderMapper.selectOne(qw);
    }

    @Override
    public Page<Orders> findOrdersByUserId(int userId) {
        QueryWrapper<Orders> qw = new QueryWrapper<Orders>();
        qw.eq("user_id", userId); // (当前页，每页显示多少条数据)
        return orderMapper.selectPage(new Page(1, 10), qw);
    }

    @Override
    public Page<Orders> findOrdersByDecriptionLike(int userId, String key) {
        QueryWrapper<Orders> qw = new QueryWrapper<Orders>();
        qw.eq("user_id", userId);
        qw.like("description", key);
        return orderMapper.selectPage(new Page<>(1, 10), qw);
    }
}
