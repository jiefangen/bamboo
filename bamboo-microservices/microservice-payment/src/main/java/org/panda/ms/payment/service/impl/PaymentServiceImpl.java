package org.panda.ms.payment.service.impl;

import org.panda.ms.payment.core.PayGatewaySupport;
import org.panda.ms.payment.core.domain.model.PaymentDefinition;
import org.panda.ms.payment.core.domain.model.PaymentRequest;
import org.panda.ms.payment.core.domain.model.PaymentResult;
import org.panda.ms.payment.service.PaymentService;
import org.panda.tech.core.spec.terminal.Terminal;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class PaymentServiceImpl extends PayGatewaySupport implements PaymentService {

    @Override
    public PaymentRequest payment(String gatewayName, PaymentDefinition definition) {
        return super.prepare(gatewayName,definition);
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
