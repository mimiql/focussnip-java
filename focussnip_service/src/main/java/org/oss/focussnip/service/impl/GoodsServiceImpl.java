package org.oss.focussnip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.oss.focussnip.mapper.GoodsMapper;
import org.oss.focussnip.model.Goods;
import org.oss.focussnip.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public Goods getGoodsByGoodsId(String goodsId) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id",goodsId);
        return goodsMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Goods> getGoodsByCategory(int category) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category",category);
        return goodsMapper.selectList(queryWrapper);
    }

    @Override
    public List<Goods> getGoodsByAddress(String address) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address",address);
        return goodsMapper.selectList(queryWrapper);
    }

    @Override
    public List<Goods> getGoodsByMarketTime(LocalDateTime marketTime) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("market_time",marketTime);
        return goodsMapper.selectList(queryWrapper);
    }

    @Override
    public List<Goods> getGoodsByHoldTime(LocalDateTime holdTime) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("hold_time",holdTime);
        return goodsMapper.selectList(queryWrapper);
    }
}
