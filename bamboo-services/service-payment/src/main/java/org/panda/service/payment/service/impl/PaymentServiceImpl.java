package org.panda.service.payment.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.service.payment.core.PayGatewaySupport;
import org.panda.service.payment.core.domain.model.PaymentDefinition;
import org.panda.service.payment.core.domain.model.PaymentRequest;
import org.panda.service.payment.core.domain.model.PaymentResult;
import org.panda.service.payment.model.entity.PayOrder;
import org.panda.service.payment.model.entity.PayOrderNotify;
import org.panda.service.payment.model.entity.PayRequestRecords;
import org.panda.service.payment.repository.PayOrderNotifyRepo;
import org.panda.service.payment.repository.PayOrderRepo;
import org.panda.service.payment.repository.PayRequestRecordsRepo;
import org.panda.service.payment.service.PaymentService;
import org.panda.tech.core.spec.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PaymentServiceImpl extends PayGatewaySupport implements PaymentService {

    @Autowired
    protected PayOrderRepo payOrderRepo;
    @Autowired
    protected PayRequestRecordsRepo payRequestRecordsRepo;
    @Autowired
    protected PayOrderNotifyRepo payOrderNotifyRepo;

    @Override
    public PaymentRequest payment(String gatewayName, PaymentDefinition definition) {
        // 支付订单创建
        PayOrder order = createOrder(gatewayName, definition);
        // 支付请求结果回填更新
        PaymentRequest paymentRequest = super.prepare(gatewayName,definition);
        if (paymentRequest != null && paymentRequest.getParams() != null) {
            Map<String, String> params = paymentRequest.getParams();
            order.setChannelFlowNo(params.get("channelFlowNo"));
        }
        order.setStatus("processing");
        order.setUpdateTime(LocalDateTime.now());
        payOrderRepo.save(order);
        // 订单记录表更新
        PayRequestRecords payRequestRecords = new PayRequestRecords();
        payRequestRecords.setOrderId(order.getOrderNo());
        payRequestRecords.setRequestMode(paymentRequest.getMode().name());
        if (paymentRequest.getParams() != null) {
            payRequestRecords.setPayParams(JsonUtil.toJson(paymentRequest.getParams()));
        }
        payRequestRecords.setPayUrl(paymentRequest.getUrl());
        payRequestRecords.setRequestData(paymentRequest.getRequestData());
        payRequestRecords.setResponseData(paymentRequest.getResponseData());
        payRequestRecords.setCreateTime(LocalDateTime.now());
        payRequestRecordsRepo.save(payRequestRecords);
        return paymentRequest;
    }

    private PayOrder createOrder(String gatewayName, PaymentDefinition definition) {
        // 支付请求创建订单
        PayOrder payOrder = new PayOrder();
        payOrder.setOrderNo(definition.getOrderNo());
        payOrder.setAmount(definition.getAmount());
        if (definition.getCurrency() != null) {
            payOrder.setCurrency(definition.getCurrency().getCurrencyCode());
        }
        payOrder.setChannel(gatewayName);
        if (definition.getTerminal() != null) {
            payOrder.setTerminal(definition.getTerminal().toString());
        }
        payOrder.setPayerIp(definition.getPayerIp());
        payOrder.setTarget(definition.getTarget());

        // 校验库中是否已存在该订单
        List<PayOrder> orders = payOrderRepo.findAll(Example.of(payOrder));
        if (CollectionUtils.isEmpty(orders)) {
            payOrder.setStatus("pending");
            payOrder.setCreateTime(LocalDateTime.now());
            return payOrderRepo.saveAndFlush(payOrder);
        } else {
            return orders.get(0);
        }
    }

    @Override
    public String callbackConfirm(String gatewayName, HttpServletRequest request, HttpServletResponse response) {
        PaymentResult paymentResult = super.confirmResult(gatewayName, request, response);
        if (paymentResult != null && paymentResult.isSuccessful()) {
            // 回调通知数据保存
            PayOrderNotify payOrderNotify = new PayOrderNotify();
            payOrderNotify.setOrderId(paymentResult.getOrderNo());
            payOrderNotify.setChannel(gatewayName);
            payOrderNotify.setChannelFlowNo(paymentResult.getGatewayPaymentNo());
            payOrderNotify.setAmount(paymentResult.getAmount());
            payOrderNotify.setResponseStatus(paymentResult.getResponseStatus());
            payOrderNotify.setNotifyData(paymentResult.getResponseBody());
            payOrderNotify.setCreateTime(LocalDateTime.now());
            payOrderNotifyRepo.save(payOrderNotify);
            return Commons.RESULT_SUCCESS;
        }
        return Commons.RESULT_FAILURE;
    }

    @Override
    public ModelAndView callbackShow(String gatewayName, String terminal, HttpServletRequest request) {
        return super.showResult(gatewayName, terminal, request);
    }

    @Override
    protected ModelAndView resolveShowResult(HttpServletRequest request, PaymentResult result, Terminal terminal) {
        ModelAndView modelAndView = new ModelAndView("voucher");
        if (result != null && result.isSuccessful()) {
            modelAndView.setStatus(HttpStatus.resolve(result.getResponseStatus()));
            modelAndView.addObject("gatewayPaymentNo", result.getGatewayPaymentNo());
            modelAndView.addObject("orderNo", result.getOrderNo());
            modelAndView.addObject("amount", result.getAmount());
            if (terminal != null) {
                modelAndView.addObject("terminal", terminal.toString());
            }
        } else {
            modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return modelAndView;
    }
}
