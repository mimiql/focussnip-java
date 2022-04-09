package org.oss.focussnip.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.oss.focussnip.dto.GoodsQueryDto;
import org.oss.focussnip.model.Goods;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GoodsService extends IService<Goods> {
    Goods getGoodsByGoodsId(String goodsId);

    List<Goods> getGoodsByCategory(int category);

    List<Goods> getGoodsByAddress(String address);

    List<Goods> getGoodsByMarketTime(String marketTime);

    List<Goods> getGoodsByHoldTime(String holdTime);

    Page<Goods> getGoodsByQuery(GoodsQueryDto goodsQueryDto);

    void insertGoodsFromCSV(MultipartFile file);

    Goods downGoodsByGoodsId(String goodsId);

    boolean downGoodsByStarId(String starId);

    Goods upGoodsByGoodsId(String goodsId);

    boolean upGoodsByStarId(String starId);

}
