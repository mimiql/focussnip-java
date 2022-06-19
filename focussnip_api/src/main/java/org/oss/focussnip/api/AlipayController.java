package org.oss.focussnip.api;

import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import org.oss.focussnip.common.BaseResponse;
import org.oss.focussnip.service.AlipayService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin(origins = "*")
public class AlipayController {

    @Resource
    private AlipayService alipayService;

    @PostMapping("/alipay/{orderId}")
    public BaseResponse<String> alipay(@PathVariable Long orderId) throws Exception{
        AlipayTradePrecreateResponse response = alipayService.newAliOrder(orderId);
        String QrCodeStr = response.getQrCode();
        return BaseResponse.getSuccessResponse(QrCodeStr);
    }

    /**
     * TRADE_SUCCESS
     * WAIT_BUYER_PAY
     */
    @PostMapping("/alipay/query/{orderId}")
    public BaseResponse<String> queryAlipay(@PathVariable Long orderId) throws Exception{
        AlipayTradeQueryResponse response = alipayService.queryOrder(orderId.toString());
        String tradeStatus = response.getTradeStatus();
        return BaseResponse.getSuccessResponse(tradeStatus);
    }

    @PostMapping("/alipay/snapOrder/{snapOrderId}")
    public BaseResponse<String> anspOrderAlipay(@PathVariable Long snapOrderId) throws Exception{
        AlipayTradePrecreateResponse response = alipayService.newSnapAliOrder(snapOrderId);
        String QrCodeStr = response.getQrCode();
        return BaseResponse.getSuccessResponse(QrCodeStr);
    }

    /**
     * TRADE_SUCCESS
     * WAIT_BUYER_PAY
     */
    @PostMapping("/alipay/SnapQuery/{snapOrderId}")
    public BaseResponse<String> querySnapAlipay(@PathVariable Long snapOrderId) throws Exception{
        AlipayTradeQueryResponse response = alipayService.querySnapOrder(snapOrderId.toString());
        String tradeStatus = response.getTradeStatus();
        return BaseResponse.getSuccessResponse(tradeStatus);
    }
}
