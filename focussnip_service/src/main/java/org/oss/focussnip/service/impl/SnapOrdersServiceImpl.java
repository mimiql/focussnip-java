package org.oss.focussnip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.oss.focussnip.constant.OrderConstant;
import org.oss.focussnip.mapper.SnapOrdersMapper;
import org.oss.focussnip.model.SnapGoods;
import org.oss.focussnip.model.SnapOrders;
import org.oss.focussnip.service.SnapOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SnapOrdersServiceImpl extends ServiceImpl<SnapOrdersMapper, SnapOrders> implements SnapOrderService {

    @Autowired
    private SnapOrdersMapper snapOrdersMapper;

    @Override
    public Integer createOrder(SnapGoods snapGoods, String username) {
        SnapOrders snapOrders = new SnapOrders();
        snapOrders.setSnapId(snapGoods.getId());
        snapOrders.setUsername(username);
        snapOrders.setPrice(snapGoods.getPrice());
        snapOrders.setDescription(snapGoods.getDescription());
        snapOrders.setStatus(OrderConstant.ORDER_UNPAYED);
        int lines = snapOrdersMapper.insert(snapOrders);
        if(lines!=1){
            return null;
        }
        QueryWrapper<SnapOrders> qw = new QueryWrapper<>();
        qw.eq("snap_id",snapGoods.getId());
        qw.eq("username",username);
        qw.eq("status",OrderConstant.ORDER_UNPAYED);
        // 重复就会报错,事务回滚
        this.getOne(qw);
        return snapOrders.getId().intValue();
    }

    @Override
    public void initSnapOrder() {
        List<SnapOrders> snapOrders = this.list();
        for(SnapOrders it : snapOrders){
            switch (it.getStatus()) {
                case OrderConstant.ORDER_CANCEL:
                case OrderConstant.ORDER_DONE:
                    break;
                default:
                    it.setStatus(OrderConstant.ORDER_CANCEL);
            }
        }
        this.updateBatchById(snapOrders);
    }
}
