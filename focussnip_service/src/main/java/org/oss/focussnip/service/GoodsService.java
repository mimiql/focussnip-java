package org.oss.focussnip.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.oss.focussnip.dto.GoodsDto;
import org.oss.focussnip.model.Goods;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GoodsService extends IService<Goods> {
    Goods getGoodsByGoodsId(String goodsId);

    Page<Goods> getGoodsByQuery(GoodsDto.GoodsQueryDto goodsQueryDto);

    void insertGoodsFromCSV(MultipartFile file);

    Goods downGoodsByGoodsId(String goodsId);

    boolean downGoodsByStarId(String starId);

    Goods upGoodsByGoodsId(String goodsId);

    boolean upGoodsByStarId(String starId);

    Goods addOneGoods(GoodsDto.GoodsAddDto goodsAddDto);

    Goods updateOneGoods(GoodsDto.GoodsUpdateDto goodsUpdateDto);

    List<Goods> getGoodsByStarId(int starId);
}
