package org.panda.service.payment.controller;

import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.panda.service.payment.core.domain.model.PaymentDefinition;
import org.panda.service.payment.core.domain.model.PaymentRequest;
import org.panda.service.payment.service.PaymentService;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "支付管理服务")
@Controller
@RequestMapping(value = "/payment")
public class PaymentController {

    @Autowired
    protected PaymentService paymentService;

    @PostMapping("/prepare/{gatewayName}")
    @ResponseBody
    public RestfulResult prepare(@PathVariable("gatewayName") String gatewayName,
                                 @RequestBody PaymentDefinition definition) {
        PaymentRequest paymentResult = this.paymentService.payment(gatewayName, definition);
        return RestfulResult.success(paymentResult);
    }

    @RequestMapping(value = "/callback/confirm/{gatewayName}")
    @ResponseBody
    public RestfulResult callbackConfirm(@PathVariable("gatewayName") String gatewayName, HttpServletRequest request,
                                HttpServletResponse response) {
        String result = this.paymentService.callbackConfirm(gatewayName, request, response);
        if (StringUtils.isBlank(result)) {
            return RestfulResult.failure();
        }
        return RestfulResult.success(result);
    }

    @RequestMapping(value = "/callback/show/{gatewayName}")
    public ModelAndView callbackShow(@PathVariable("gatewayName") String gatewayName, HttpServletRequest request) {
        return callbackShow(gatewayName, null, request);
    }

    @RequestMapping(value = "/callback/show/{gatewayName}/{terminal}")
    public ModelAndView callbackShow(@PathVariable("gatewayName") String gatewayName,
                                     @PathVariable("terminal") String terminal, HttpServletRequest request) {
        return this.paymentService.callbackShow(gatewayName, terminal, request);
    }

}
