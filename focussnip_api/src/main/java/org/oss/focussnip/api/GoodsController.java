package org.oss.focussnip.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Range;
import org.oss.focussnip.common.BaseResponse;
import org.oss.focussnip.dto.GoodsAddDto;
import org.oss.focussnip.dto.GoodsDto;
import org.oss.focussnip.dto.GoodsQueryDto;
import org.oss.focussnip.dto.GoodsUpdateDto;
import org.oss.focussnip.model.Goods;
import org.oss.focussnip.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Api("商品api")
@RestController
@RequestMapping("/goods")
@Validated
@CrossOrigin(origins = "*")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    private static final String REGEXP = "^(((20[0-3][0-9]-(0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|(20[0-3][0-9]-(0[2469]|11)-(0[1-9]|[12][0-9]|30)))" +
            "\\s(20|21|22|23|[0-1][0-9]):[0-5][0-9]:[0-5][0-9])$";

    @ApiOperation("/根据查询得到商品信息")
    @PostMapping("/query")
    public BaseResponse getGoodsByQuery(@RequestBody @Valid GoodsQueryDto goodsQueryDto){
        Page<Goods> goodsList = goodsService.getGoodsByQuery(goodsQueryDto);
        return BaseResponse.getSuccessResponse(goodsList);
    }


    @ApiOperation("/批量插入商品")
    @PostMapping("/insert")
    public BaseResponse insertGoodsFromCSV(@RequestParam("file") MultipartFile file){
        goodsService.insertGoodsFromCSV(file);
        return BaseResponse.getSuccessResponse(null);
    }

    @ApiOperation("/根据GoodsID下架商品")
    @DeleteMapping("/goodsId")
    public BaseResponse downGoodsByGoodsId(@NotBlank(message = "goodsId不能为空") @RequestParam("goodsId") String goodsId){
        Goods goods = goodsService.downGoodsByGoodsId(goodsId);
        return BaseResponse.getSuccessResponse(goods);
    }

    @ApiOperation("/根据statId下架商品")
    @DeleteMapping("/starId")
    public BaseResponse downGoodsByStarId(@NotBlank(message = "starId不能为空") @RequestParam("starId") String starId){
        boolean ans = goodsService.downGoodsByStarId(starId);
        return BaseResponse.getSuccessResponse(ans);
    }

    @ApiOperation("/根据GoodsID上架商品")
    @PutMapping("/goodsId")
    public BaseResponse upGoodsByGoodsId(@NotBlank(message = "goodsId不能为空") @RequestParam("goodsId") String goodsId){
        Goods goods = goodsService.upGoodsByGoodsId(goodsId);
        return BaseResponse.getSuccessResponse(goods);
    }

    @ApiOperation("/根据statId上架商品")
    @PutMapping("/starId")
    public BaseResponse upGoodsByStarId(@NotBlank(message = "starId不能为空") @RequestParam("starId") String starId){
        boolean ans = goodsService.upGoodsByStarId(starId);
        return BaseResponse.getSuccessResponse(ans);
    }

    @ApiOperation("/明星增加单个商品")
    @PostMapping("/")
    public BaseResponse AddOneGoods(@RequestBody GoodsAddDto goodsAddDto){
        Goods goods = goodsService.addOneGoods(goodsAddDto);
        return BaseResponse.getSuccessResponse(goods);
    }

    @ApiOperation("/修改商品信息")
    @PutMapping("/")
    public BaseResponse updateOneGoods(@RequestBody GoodsUpdateDto goodsUpdateDto){
        Goods goods = goodsService.updateOneGoods(goodsUpdateDto);
        return BaseResponse.getSuccessResponse(goods);
    }
}
