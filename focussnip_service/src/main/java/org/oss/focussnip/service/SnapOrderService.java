package org.oss.focussnip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.oss.focussnip.model.SnapGoods;
import org.oss.focussnip.model.SnapOrders;

public interface SnapOrderService extends IService<SnapOrders> {
    Integer createOrder(SnapGoods snapGoods, String username);

    void initSnapOrder();
}
