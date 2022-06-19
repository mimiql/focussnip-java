package org.oss.focussnip.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;


public interface AlipayService {

    AlipayTradePrecreateResponse newAliOrder(Long orderId) throws AlipayApiException;

    AlipayTradeQueryResponse queryOrder(String orderId) throws Exception;

    AlipayTradePrecreateResponse newSnapAliOrder(Long snapOrderId) throws AlipayApiException;

    AlipayTradeQueryResponse querySnapOrder(String snapOrderId) throws Exception;

}
