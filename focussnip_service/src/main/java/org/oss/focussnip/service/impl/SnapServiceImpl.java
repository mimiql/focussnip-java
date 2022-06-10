package org.oss.focussnip.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.oss.focussnip.mapper.SnapMapper;
import org.oss.focussnip.model.SnapGoods;
import org.oss.focussnip.service.SnapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SnapServiceImpl extends ServiceImpl<SnapMapper, SnapGoods> implements SnapService {
    @Autowired
    private SnapMapper snapMapper;

    public boolean initSnap(){
        List<SnapGoods> snapGoodList = this.list();
        LocalDateTime now = LocalDateTime.now();
        for(SnapGoods it : snapGoodList){
            it.setMarketTime(now.minusMinutes(10));
            it.setEndTime(now.plusMinutes(30+(int)(Math.random()*10)));
        }
        return this.updateBatchById(snapGoodList);
    }


}
