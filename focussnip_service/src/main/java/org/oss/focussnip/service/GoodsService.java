package org.oss.focussnip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.oss.focussnip.model.Goods;

import java.time.LocalDateTime;
import java.util.List;

public interface GoodsService extends IService<Goods> {
    Goods getGoodsByGoodsId(String goodsId);

    List<Goods> getGoodsByCategory(int category);

    List<Goods> getGoodsByAddress(String address);

    List<Goods> getGoodsByMarketTime(LocalDateTime marketTime);

    List<Goods> getGoodsByHoldTime(LocalDateTime holdTime);
}
