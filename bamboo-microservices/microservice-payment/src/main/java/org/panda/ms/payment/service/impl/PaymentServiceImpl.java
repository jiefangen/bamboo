package org.panda.ms.payment.service.impl;

import org.panda.ms.payment.core.PayGatewaySupport;
import org.panda.ms.payment.core.domain.model.PaymentDefinition;
import org.panda.ms.payment.core.domain.model.PaymentRequest;
import org.panda.ms.payment.core.domain.model.PaymentResult;
import org.panda.ms.payment.model.entity.PayOrder;
import org.panda.ms.payment.repository.PayOrderRepo;
import org.panda.ms.payment.service.PaymentService;
import org.panda.tech.core.spec.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class PaymentServiceImpl extends PayGatewaySupport implements PaymentService {

    @Autowired
    protected PayOrderRepo payOrderRepo;

    @Override
    public PaymentRequest payment(String gatewayName, PaymentDefinition definition) {
        // 支付订单创建
        PayOrder order = createOrder(gatewayName, definition);
        // 支付请求结果回填
        PaymentRequest paymentRequest = super.prepare(gatewayName,definition);
        if (paymentRequest != null && paymentRequest.getParams() != null) {
            Map<String, String> params = paymentRequest.getParams();
            order.setChannelFlowNo(params.get("channelFlowNo"));
        }
        order.setStatus("processing");
        order.setUpdateTime(LocalDateTime.now());
        payOrderRepo.saveAndFlush(order);
        // 订单记录表更新

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
        payOrder.setStatus("pending");
        if (definition.getTerminal() != null) {
            payOrder.setTerminal(definition.getTerminal().toString());
        }
        payOrder.setPayerIp(definition.getPayerIp());
        payOrder.setTarget(definition.getTarget());
        payOrder.setCreateTime(LocalDateTime.now());
        return payOrderRepo.save(payOrder);
    }

    @Override
    public String callbackConfirm(String gatewayName, HttpServletRequest request, HttpServletResponse response) {
        return super.confirmResult(gatewayName, request, response);
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
