package org.oss.focussnip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.Null;
import org.oss.focussnip.constant.GoodsConstant;
import org.oss.focussnip.dto.GoodsDto;
import org.oss.focussnip.mapper.GoodsMapper;
import org.oss.focussnip.model.Goods;
import org.oss.focussnip.service.GoodsService;
import org.oss.focussnip.utils.RandomStringUtil;
import org.oss.focussnip.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.stream.events.DTD;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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
    public List<Goods> getGoodsByMarketTime(String marketTimeStr) {
        LocalDateTime marketTime = TimeUtil.StringToLocalDateTime(marketTimeStr);
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("market_time",marketTime);
        return goodsMapper.selectList(queryWrapper);
    }

    @Override
    public List<Goods> getGoodsByHoldTime(String holdTimeStr) {
        LocalDateTime holdTime = TimeUtil.StringToLocalDateTime(holdTimeStr);
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("hold_time",holdTime);
        return goodsMapper.selectList(queryWrapper);
    }

    @Override
    public Page<Goods> getGoodsByQuery(GoodsDto.GoodsQueryDto goodsQueryDto) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();

        String goodsId = goodsQueryDto.getGoodsId();
        if (!StringUtils.isBlank(goodsId)){
            queryWrapper.eq("goods_id",goodsId);
        }

        String goodsName = goodsQueryDto.getGoodsName();
        if (!StringUtils.isBlank(goodsName)){
            queryWrapper.like("goods_name" , goodsName);
        }

        String address = goodsQueryDto.getAddress();
        if (!StringUtils.isBlank(address)){
            queryWrapper.eq("address" , address);
        }

        int stock = goodsQueryDto.getStock();
        if (stock != -1){
            queryWrapper.gt("stock" , stock);
        }

        double priceStart = goodsQueryDto.getPriceStart();
        double priceEnd = goodsQueryDto.getPriceEnd();
        queryWrapper.between("price" , priceStart , priceEnd);

        String description = goodsQueryDto.getDescription();
        if (!StringUtils.isBlank(goodsName)){
            queryWrapper.like("description" , description);
        }

        LocalDateTime marketTimeStart = TimeUtil.StringToLocalDateTime(goodsQueryDto.getMarketTimeStartStr());
        LocalDateTime marketTimeEnd = TimeUtil.StringToLocalDateTime(goodsQueryDto.getMarketTimeEndStr());
        queryWrapper.between("market_time" , marketTimeStart , marketTimeEnd);

        LocalDateTime holeTimeStart = TimeUtil.StringToLocalDateTime(goodsQueryDto.getHoleTimeStartStr());
        LocalDateTime holeTimeEnd = TimeUtil.StringToLocalDateTime(goodsQueryDto.getHoleTimeEndStr());
        queryWrapper.between("hold_time" , holeTimeStart , holeTimeEnd);

        int category = goodsQueryDto.getCategory();
        if (category != 0){
            queryWrapper.eq("category" , category);
        }

        int starId = goodsQueryDto.getStarId();
        if (starId != -1){
            queryWrapper.eq("star_id" , starId);
        }

        queryWrapper.eq("status" , goodsQueryDto.getStatus());

        Page<Goods> goodsPage = new Page<>(goodsQueryDto.getPageNum(),goodsQueryDto.getPageSize());
        Page<Goods> goodsList = goodsMapper.selectPage(goodsPage,queryWrapper);
        return goodsList;
    }

    @Override
    public void insertGoodsFromCSV(MultipartFile file){
        if (ObjectUtils.isEmpty(file)){
            return;
        }

        try {
            CsvReader csvReader = new CsvReader();
            CsvContainer reader = csvReader.read(multipartFileToFile(file), StandardCharsets.UTF_8);
            List<CsvRow> csvRowList = reader.getRows();

            System.out.println("读取的行数："+csvRowList.size());
            for(int row = 1 ; row < csvRowList.size() ; ++row){
                CsvRow csvData = csvRowList.get(row);

                Goods goods = new Goods();
                goods.setGoodsId(csvData.getField(0));
                goods.setGoodsName(csvData.getField(1));
                goods.setAddress(csvData.getField(2));
                goods.setPicture(csvData.getField(3));
                goods.setStock(Integer.parseInt(csvData.getField(4)));
                goods.setPrice(Double.parseDouble(csvData.getField(5)));
                goods.setDescription(csvData.getField(6));
                goods.setTip(csvData.getField(7));
                goods.setCreateTime(TimeUtil.StringToLocalDateTime(csvData.getField(8)));
                goods.setUpdateTime(TimeUtil.StringToLocalDateTime(csvData.getField(9)));
                goods.setMarketTime(TimeUtil.StringToLocalDateTime(csvData.getField(10)));
                goods.setHoldTime(TimeUtil.StringToLocalDateTime(csvData.getField(11)));
                goods.setCategory(Integer.parseInt(csvData.getField(12)));
                goods.setStarId(Integer.parseInt(csvData.getField(13)));
                goods.setStatus(GoodsConstant.GOODS_STATUS_UP);
                this.save(goods);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Goods downGoodsByGoodsId(String goodsId) {
        Goods goods = getGoodsByGoodsId(goodsId);
        goods.setStatus(GoodsConstant.GOODS_STATUS_DOWN);
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id" , goodsId);
        goodsMapper.update(goods,queryWrapper);
        return goodsMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean downGoodsByStarId(String starId) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("star_id" , starId);
        List<Goods> goodsList = goodsMapper.selectList(queryWrapper);

        for (Goods goods : goodsList){
            QueryWrapper<Goods> queryWrapper2 = new QueryWrapper<>();
            goods.setStatus(GoodsConstant.GOODS_STATUS_DOWN);
            queryWrapper2.eq("goods_id" , goods.getGoodsId());
            goodsMapper.update(goods,queryWrapper2);
        }
        return true;
    }

    @Override
    public Goods upGoodsByGoodsId(String goodsId) {
        Goods goods = getGoodsByGoodsId(goodsId);
        goods.setStatus(GoodsConstant.GOODS_STATUS_UP);
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id" , goodsId);
        goodsMapper.update(goods,queryWrapper);
        return goodsMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean upGoodsByStarId(String starId) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("star_id" , starId);
        List<Goods> goodsList = goodsMapper.selectList(queryWrapper);

        for (Goods goods : goodsList){
            QueryWrapper<Goods> queryWrapper2 = new QueryWrapper<>();
            goods.setStatus(GoodsConstant.GOODS_STATUS_UP);
            queryWrapper2.eq("goods_id" , goods.getGoodsId());
            goodsMapper.update(goods,queryWrapper2);
        }
        return true;
    }

    @Override
    public Goods addOneGoods(GoodsDto.GoodsAddDto goodsAddDto) {
        Goods goods = new Goods();
        goods.setGoodsId(RandomStringUtil.getRandomString(GoodsConstant.GOODS_ID_LENGTH));
        goods.setGoodsName(goodsAddDto.getGoodsName());
        goods.setAddress(goodsAddDto.getAddress());
        goods.setPicture(goodsAddDto.getPicture());
        goods.setStock(goodsAddDto.getStock());
        goods.setPrice(goodsAddDto.getPrice());
        goods.setDescription(goodsAddDto.getDescription());
        goods.setTip(goodsAddDto.getTip());

        goods.setCreateTime(LocalDateTime.now());
        goods.setUpdateTime(LocalDateTime.now());
        goods.setMarketTime(TimeUtil.StringToLocalDateTime(goodsAddDto.getMarketTimeStr()));
        goods.setHoldTime(TimeUtil.StringToLocalDateTime(goodsAddDto.getHoleTimeStr()));

        goods.setCategory(goodsAddDto.getCategory());
        goods.setStarId(goodsAddDto.getStarId());
        goods.setStatus(goodsAddDto.getStatus());

        goodsMapper.insert(goods);

        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id" , goods.getGoodsId());
        Goods goods1 = goodsMapper.selectOne(queryWrapper);
        return goods1;
    }

    @Override
    public Goods updateOneGoods(GoodsDto.GoodsUpdateDto goodsUpdateDto) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id" , goodsUpdateDto.getGoodsId());
        Goods goods = goodsMapper.selectOne(queryWrapper);
        if (goods == null){
            return null;
        }
        goods.setGoodsName(goodsUpdateDto.getGoodsName());
        goods.setAddress(goodsUpdateDto.getAddress());
        goods.setPicture(goodsUpdateDto.getPicture());
        goods.setStock(goodsUpdateDto.getStock());
        goods.setPrice(goodsUpdateDto.getPrice());
        goods.setDescription(goodsUpdateDto.getDescription());
        goods.setTip(goodsUpdateDto.getTip());

        goods.setUpdateTime(LocalDateTime.now());
        goods.setMarketTime(TimeUtil.StringToLocalDateTime(goodsUpdateDto.getMarketTimeStr()));
        goods.setHoldTime(TimeUtil.StringToLocalDateTime(goodsUpdateDto.getHoleTimeStr()));

        goods.setCategory(goodsUpdateDto.getCategory());
        goods.setStarId(goodsUpdateDto.getStarId());
        goods.setStatus(goodsUpdateDto.getStatus());
        goodsMapper.update(goods,queryWrapper);

        return goods;
    }

    private static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
