package org.oss.focussnip.api;

import com.alipay.api.response.AlipayTradeQueryResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.benmanes.caffeine.cache.Cache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.oss.focussnip.common.BaseResponse;
import org.oss.focussnip.constant.OrderConstant;
import org.oss.focussnip.dto.SnapIdOrderIdDto;
import org.oss.focussnip.exception.BusinessErrorException;
import org.oss.focussnip.model.SnapGoods;
import org.oss.focussnip.model.SnapOrders;
import org.oss.focussnip.service.AlipayService;
import org.oss.focussnip.service.SnapService;
import org.oss.focussnip.service.SnapOrderService;
import org.oss.focussnip.utils.JWTUtil;
import org.oss.focussnip.utils.RedisUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Api("抢购Api")
@RestController
public class SnapController {
    @Autowired
    private SnapService snapService;
    @Autowired
    private SnapOrderService snapOrderService;
    @Autowired
    private AlipayService alipayService;
    @Autowired
    private RedisUtil<String,SnapGoods> redisUtil;
    @Autowired
    Cache<String, Object> caffeineCache;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @PostConstruct
    public void initSnap(){
        List<SnapGoods> snapGoodsList = snapService.initSnap();
        snapOrderService.initSnapOrder();
        caffeineCache.put("snap.list",snapGoodsList);
        for(SnapGoods it : snapGoodsList){
            String idStr = String.valueOf(it.getId());
            redisUtil.putHash("snap",idStr,it);
            redisUtil.putValue("snap."+idStr+".stock", it.getStock());
        }
    }

    @ApiOperation("/获取全部抢购商品信息")
    @GetMapping("/snap")
    public BaseResponse<List<SnapGoods>> getSnapList() throws JsonProcessingException {
        caffeineCache.getIfPresent("snap.list");
        List<SnapGoods> snapGoodsList = (List<SnapGoods>) caffeineCache.asMap().get("snap.list");
        return BaseResponse.getSuccessResponse(snapGoodsList);
    }

    @ApiOperation("/获取指定抢购商品信息")
    @GetMapping("/snap/get/{id}")
    public BaseResponse<SnapGoods> getSnap(@PathVariable Long id){
        String idStr = String.valueOf(id);
        SnapGoods snap = redisUtil.getHash("snap",idStr);
        snap.setStock(redisUtil.getValue("snap."+idStr+".stock"));
        return BaseResponse.getSuccessResponse(snap);
    }

    @ApiOperation("/参加抢购活动")
    @PostMapping("/snap/join")
    @RequiresAuthentication
    public BaseResponse<String> joinSnap(@RequestHeader("Authorization") String token){
        redisUtil.putValue("snap.token."+token, 0);
        return BaseResponse.getSuccessResponse("成功参与抢购");
    }

    @ApiOperation("/购买指定抢购商品")
    @PostMapping("/snap/buy/{id}")
    public BaseResponse<String> buySnap(@RequestHeader("Authorization") String token, @PathVariable Long id){
        rabbitTemplate.convertAndSend("DIRECT_EXCHANGE","snapOrder",token+"："+ id);
        return BaseResponse.getSuccessResponse("成功参与抢购");
    }

    @RabbitListener(queues = "SNAP_QUEUE")
    @RabbitHandler
    public void checkAndCreateSnap(String msg){
        String[] strList = StringUtils.split(msg,'：');
        String token = strList[0];
        String idStr = strList[1];
        long id = Long.parseLong(idStr);
        SnapGoods snapGoods = redisUtil.getHash("snap",idStr);
        if(timeCheck(snapGoods.getEndTime()) || stockCheck(idStr) || tokenCheck(token) || checkHasOrder(token)){
            //不符合要求
            redisUtil.putValue(token+".order",-1);
            return;
        }
        System.out.println("全false");
        String username = JWTUtil.getUsername(token);
        Integer orderId = snapOrderService.createOrder(snapGoods,username);
        if (null != orderId)
            redisUtil.putValue(token+".order",orderId);
    }

    private boolean timeCheck(LocalDateTime localDateTime){
        return (LocalDateTime.now().compareTo(localDateTime)>0);
    }

    private boolean stockCheck(String idStr){
        return (redisUtil.getValue("snap."+idStr+".stock")<1);
    }

    private boolean tokenCheck(String token){
        if (null == redisUtil.getValue("snap.token."+token)) return true;
        JWTUtil.getUsername(token);
        return false;
    }

    private boolean checkHasOrder(String token){
        return ! new Integer(redisUtil.incr("snap.token." + token).intValue()).equals(0);
    }

    @ApiOperation("/检验抢购下单结果")
    @GetMapping("/snap/check")
    @RequiresAuthentication
    public BaseResponse<SnapOrders> checkSnap(@RequestHeader("Authorization") String token){
        Integer orderId = redisUtil.getValue(token+".order");
        if(null == orderId){
            return BaseResponse.getErrorResponse("00000","继续排队");
        }
        if(orderId==-1){
            return BaseResponse.getErrorResponse("11111","抢购失败");
        }
        return BaseResponse.getSuccessResponse(snapOrderService.getById(orderId));
    }

    @ApiOperation("/确认订单")
    @PostMapping("/snap/confirm")
    public BaseResponse<String> confirmSnap(@RequestBody SnapIdOrderIdDto snapIdOrderIdDto){
        SnapOrders snapOrder = snapOrderService.getById(snapIdOrderIdDto.getOrderId());
        if(snapOrder.getStatus()!=OrderConstant.ORDER_UNPAYED){
            if(snapOrder.getStatus()==OrderConstant.ORDER_PAYED)
                return BaseResponse.getErrorResponse("111111","请勿重复下单");
            throw new BusinessErrorException("111111","该订单无法确认");
        }
        if(!redisUtil.optimismLock("snap."+snapIdOrderIdDto.getSnapId()+".stock",1)) {
            throw new BusinessErrorException("1111111","库存不足");
        }
        snapOrder.setStatus(OrderConstant.ORDER_PAYED);
        snapOrderService.updateById(snapOrder);
        return BaseResponse.getSuccessResponse("成功参与抢购");
    }

    @ApiOperation("/确认支付")
    @PostMapping("/snap/alipay/query/{orderId}")
    public BaseResponse<String> queryAlipay(@PathVariable Long orderId) throws Exception{
        AlipayTradeQueryResponse response = alipayService.queryOrder(orderId.toString());
        String tradeStatus = response.getTradeStatus();
        if("TRADE_SUCCESS".equals(tradeStatus)){

            SnapOrders snapOrder = snapOrderService.getById(orderId);
            snapOrder.setStatus(OrderConstant.ORDER_DONE);
            snapOrderService.updateById(snapOrder);
            return BaseResponse.getSuccessResponse("支付成功");
        }
        return BaseResponse.getErrorResponse("000","支付未完成");
    }

    @ApiOperation("/取消订单")
    @PostMapping("/snap/cancel/{orderId}")
    public BaseResponse<String> cancelPay(@PathVariable Long orderId) throws Exception{
        SnapOrders snapOrder = snapOrderService.getById(orderId);
        if(snapOrder.getStatus()!=OrderConstant.ORDER_PAYED){
            if(snapOrder.getStatus()==OrderConstant.ORDER_DONE){
                return BaseResponse.getErrorResponse("111111","订单已完成");
            }
            throw new BusinessErrorException("111111","该订单无法取消");
        }
        long snapID = snapOrder.getSnapId();
        redisUtil.incr("snap."+snapID+".stock");
        snapOrder.setStatus(OrderConstant.ORDER_CANCEL);
        snapOrderService.updateById(snapOrder);
        return BaseResponse.getSuccessResponse("取消成功");
    }
}
