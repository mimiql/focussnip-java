package org.oss.focussnip.api;

import com.alipay.api.response.AlipayTradeQueryResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
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
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class SnapController {
    @Autowired
    private SnapService snapService;
    @Autowired
    private SnapOrderService snapOrderService;
    @Autowired
    private AlipayService alipayService;
    @Resource
    private RedisUtil<String,SnapGoods> redisUtil;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/snap/init")
    public BaseResponse<String> initSnap(){
        List<SnapGoods> snapGoodsList = snapService.initSnap();
        snapOrderService.initSnapOrder();

        for(SnapGoods it : snapGoodsList){
            String idStr = String.valueOf(it.getId());
            redisUtil.putinList("snapList",it);
            redisUtil.putHash("snap",idStr,it);
            redisUtil.putValue("snap."+idStr+".stock", it.getStock());
        }

        return BaseResponse.getSuccessResponse("成功初始化snap");
    }

    @GetMapping("/snap")
    public BaseResponse<List<SnapGoods>> getSnapList(){
        List<SnapGoods> snapGoodList = redisUtil.getList("snapList");
        return BaseResponse.getSuccessResponse(snapGoodList);
    }

    @GetMapping("/snap/get/{id}")
    public BaseResponse<SnapGoods> getSnap(@PathVariable Long id){
        String idStr = String.valueOf(id);
        SnapGoods snap = redisUtil.getHash("snap",idStr);
        snap.setStock(redisUtil.getValue("snap."+idStr+".stock"));
        return BaseResponse.getSuccessResponse(snap);
    }

    @PostMapping("/snap/join")
    @RequiresAuthentication
    public BaseResponse<String> joinSnap(@RequestHeader("Authorization") String token){
        redisUtil.putValue("snap.token."+token, 0);
        return BaseResponse.getSuccessResponse("成功参与抢购");
    }

    @PostMapping("/snap/buy/{id}")
    public BaseResponse<String> buySnap(@RequestHeader("Authorization") String token, @PathVariable Long id){
        rabbitTemplate.convertAndSend("DIRCET_EXCHANGE","snapOrder",token+"："+ id);
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
        return redisUtil.incr("snap.token." + token) != 1;
    }

    @GetMapping("/snip/check/{id}")
    @RequiresAuthentication
    public BaseResponse<SnapOrders> checkSnap(@PathVariable Long id){
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        SnapOrders snapOrders = snapOrderService.getBySnapIdAndUsername(id,username);
        return BaseResponse.getSuccessResponse(snapOrders);
    }

    @PostMapping("/snip/confirm")
    public BaseResponse<String> confirmSnap(@RequestBody SnapIdOrderIdDto snapIdOrderIdDto){
        if(!redisUtil.optimismLock("snap."+snapIdOrderIdDto.getSnapId()+".stock",1)) {
            throw new BusinessErrorException("1111111","库存不足");
        }
        SnapOrders snapOrder = snapOrderService.getById(snapIdOrderIdDto.getOrderId());
        snapOrder.setStatus(OrderConstant.ORDER_PAYED);
        snapOrderService.updateById(snapOrder);
        return BaseResponse.getSuccessResponse("成功参与抢购");
    }

    @PostMapping("/snip/alipay/query/{orderId}")
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

    @PostMapping("/snip/cancel/{orderId}")
    public BaseResponse<String> cancelPay(@PathVariable Long orderId) throws Exception{
        SnapOrders snapOrder = snapOrderService.getById(orderId);
        long snapID = snapOrder.getSnapId();
        redisUtil.incr("snap."+snapID+".stock");
        snapOrder.setStatus(OrderConstant.ORDER_CANCEL);
        snapOrderService.updateById(snapOrder);
        return BaseResponse.getSuccessResponse("取消成功");
    }
}
