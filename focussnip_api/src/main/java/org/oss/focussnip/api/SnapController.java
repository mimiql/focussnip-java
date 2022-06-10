package org.oss.focussnip.api;

import com.alipay.api.response.AlipayTradeQueryResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.oss.focussnip.common.BaseResponse;
import org.oss.focussnip.model.SnapGoods;
import org.oss.focussnip.service.AlipayService;
import org.oss.focussnip.service.SnapService;
import org.oss.focussnip.utils.RedisUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class SnapController {
    @Autowired
    private SnapService snapService;
    @Autowired
    private AlipayService alipayService;
    @Resource
    private RedisUtil<String,SnapGoods> redisUtil;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/snap/init")
    public BaseResponse<String> initSnap(){
        List<SnapGoods> snapGoodsList = snapService.initSnap();

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
        SnapGoods snap = redisUtil.getHash("snap",String.valueOf(id));
        return BaseResponse.getSuccessResponse(snap);
    }

    @PostMapping("/snap/join")
    @RequiresAuthentication
    public BaseResponse<String> joinSnap(@RequestHeader("Authorization") String token){
        redisUtil.putValue("snap.token."+token, 0);
        return BaseResponse.getSuccessResponse("成功参与抢购");
    }

    @GetMapping("/snap/buy/{id}")
    public BaseResponse<String> buySnap(@PathVariable Long id){
        rabbitTemplate.convertAndSend("","");
        // todo: MQ长度验证
        // todo: 入MQ
        return BaseResponse.getSuccessResponse("成功参与抢购");
    }

    public BaseResponse<String> checkAndCreateSnap(){
        // todo: 出MQ
        // todo: 抢购过期
        // todo: 库存
        // todo: 鉴权
        // todo: 重复消费
        // todo: 生成订单
        // todo: 订单入redis
        return BaseResponse.getSuccessResponse("成功参与抢购");
    }

    @GetMapping("/snip/check")
    @RequiresAuthentication
    public BaseResponse<String> checkSnap(){
        // todo: 查redis订单
        return BaseResponse.getSuccessResponse("成功参与抢购");
    }

    @PostMapping("/snip/confirm/{id}")
    @RequiresAuthentication
    public BaseResponse<String> confirmSnap(@PathVariable Long id){
        // todo: redis乐观锁扣库存
        // todo: 订单状态已确认
        return BaseResponse.getSuccessResponse("成功参与抢购");
    }

    @PostMapping("/snip/alipay/query/{orderId}")
    public BaseResponse<String> queryAlipay(@PathVariable Long orderId) throws Exception{
        AlipayTradeQueryResponse response = alipayService.queryOrder(orderId.toString());
        String tradeStatus = response.getTradeStatus();
        if("TRADE_SUCCESS".equals(tradeStatus)){
            // todo: 订单状态已支付
            return BaseResponse.getSuccessResponse("支付成功");
        }
        return BaseResponse.getErrorResponse("000","支付未完成");
    }

    @PostMapping("/snip/cancel/{Id}")
    public BaseResponse<String> cancelPay(@PathVariable Long Id) throws Exception{
        // todo: 回滚库存
        if(true){
            // todo: 订单状态已废弃            return BaseResponse.getSuccessResponse("支付成功");
        }
        return BaseResponse.getErrorResponse("000","支付未完成");
    }


}
