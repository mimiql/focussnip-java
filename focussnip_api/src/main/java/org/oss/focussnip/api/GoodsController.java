package org.oss.focussnip.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Range;
import org.oss.focussnip.common.BaseResponse;
import org.oss.focussnip.dto.GoodsQueryDto;
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
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    private static final String REGEXP = "^(((20[0-3][0-9]-(0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|(20[0-3][0-9]-(0[2469]|11)-(0[1-9]|[12][0-9]|30)))" +
            "\\s(20|21|22|23|[0-1][0-9]):[0-5][0-9]:[0-5][0-9])$";


    @ApiOperation("/获取所有商品信息")
    @GetMapping("/")
    public BaseResponse getAllGoods(){
        List<Goods> goodsList = goodsService.list();
        return BaseResponse.getSuccessResponse(goodsList);
    }

    @ApiOperation("/根据GoodsID获取商品信息")
    @GetMapping("/goodsId")
    public BaseResponse getGoodsById(@NotBlank(message = "goodsId不能为空") @RequestParam("goodsId") String goodsId){
        Goods goods = goodsService.getGoodsByGoodsId(goodsId);
        return BaseResponse.getSuccessResponse(goods);
    }

    @ApiOperation("/根据类别获取商品信息")
    @GetMapping("/category")
    public BaseResponse getGoodsByCategory(@Digits(message = "category必须为整数", integer = 0, fraction = 0) @Range(min = 0, max = 4, message = "范围1-4") @RequestParam("category")  int category){
        List<Goods> goodsList = goodsService.getGoodsByCategory(category);
        return BaseResponse.getSuccessResponse(goodsList);
    }

    @ApiOperation("/根据地点获取商品信息")
    @GetMapping("/address")
    public BaseResponse getGoodsByAddress(@NotBlank(message = "address不能为空") @RequestParam("address") String address){
        List<Goods> goodsList = goodsService.getGoodsByAddress(address);
        return BaseResponse.getSuccessResponse(goodsList);
    }

    @ApiOperation("/根据上市时间商品信息，返回大于该时间的所有商品")
    @GetMapping("/marketTime")
    public BaseResponse getGoodsByMarketTime(@Pattern(regexp = REGEXP, message = "日期格式yyyy-MM-dd HH:mm:ss") @RequestParam("marketTimeStr") String marketTimeStr){
        List<Goods> goodsList = goodsService.getGoodsByMarketTime(marketTimeStr);
        return BaseResponse.getSuccessResponse(goodsList);
    }

    @ApiOperation("/根据举办时间商品信息，返回大于该时间的所有商品")
    @GetMapping("/holdTime")
    public BaseResponse getGoodsByHoldTime(@Pattern(regexp = REGEXP, message = "日期格式yyyy-MM-dd HH:mm:ss")  @RequestParam("holdTimeStr") String holdTimeStr){
        List<Goods> goodsList = goodsService.getGoodsByHoldTime(holdTimeStr);
        return BaseResponse.getSuccessResponse(goodsList);
    }

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

}
