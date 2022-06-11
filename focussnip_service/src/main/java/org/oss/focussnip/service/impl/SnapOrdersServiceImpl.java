package org.oss.focussnip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.oss.focussnip.constant.OrderConstant;
import org.oss.focussnip.mapper.SnapOrdersMapper;
import org.oss.focussnip.model.SnapGoods;
import org.oss.focussnip.model.SnapOrders;
import org.oss.focussnip.service.SnapOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SnapOrdersServiceImpl extends ServiceImpl<SnapOrdersMapper, SnapOrders> implements SnapOrderService {

    @Override
    public Integer createOrder(SnapGoods snapGoods, String username) {
        SnapOrders snapOrders = new SnapOrders();
        snapOrders.setSnapId(snapGoods.getId());
        snapOrders.setUsername(username);
        snapOrders.setPrice(snapGoods.getPrice());
        snapOrders.setDescription(snapGoods.getDescription());
        snapOrders.setStatus(OrderConstant.ORDER_UNPAYED);
        if(this.save(snapOrders)){
            Long id = snapOrders.getId();
            QueryWrapper<SnapOrders> qw = new QueryWrapper<>();
            qw.eq("id",id);
            qw.eq("username",username);
            qw.eq("status",OrderConstant.ORDER_UNPAYED);
            // 重复就会报错,事务回滚
            this.getOne(qw);
            return id.intValue();
        }
        return null;
    }

    @Override
    public SnapOrders getBySnapIdAndUsername(Long id, String username) {
        QueryWrapper<SnapOrders> qw = new QueryWrapper<>();
        qw.eq("id",id);
        qw.eq("username",username);
        qw.eq("status",OrderConstant.ORDER_UNPAYED);
        return this.getOne(qw);
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
