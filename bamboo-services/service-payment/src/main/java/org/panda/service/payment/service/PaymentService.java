package org.panda.service.payment.service;

import org.panda.service.payment.core.domain.model.PaymentDefinition;
import org.panda.service.payment.core.domain.model.PaymentRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 支付服务
 *
 * @author fangen
 **/
public interface PaymentService {

    /**
     * 支付付款请求
     *
     * @param gatewayName 支付网关
     * @param definition 支付定义
     * @return 支付结果
     */
    PaymentRequest payment(String gatewayName, PaymentDefinition definition);

    /**
     * 支付回调确认通知
     *
     * @param gatewayName 支付网关
     * @param request 请求
     * @param response 响应
     * @return 回调结果
     */
    String callbackConfirm(String gatewayName, HttpServletRequest request, HttpServletResponse response);

    /**
     * 支付回调展示通知
     *
     * @param gatewayName
     * @param terminal
     * @param request
     * @return
     */
    ModelAndView callbackShow(String gatewayName, String terminal, HttpServletRequest request);

}
