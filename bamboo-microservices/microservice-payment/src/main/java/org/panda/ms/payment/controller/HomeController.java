package org.panda.ms.payment.controller;

import io.swagger.annotations.Api;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.ms.payment.core.domain.model.PaymentResult;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;

@Api(tags = "微服务问候语")
@Controller
@RequestMapping(value = "/home")
public class HomeController {

    @GetMapping
    @ResponseBody
    public RestfulResult<String> home() {
        return RestfulResult.success(getApplicationDesc());
    }

    @GetMapping(value = "/hello")
    public ModelAndView hello() {
        ModelAndView modelAndView = new ModelAndView("hello");
        modelAndView.addObject("appName", getApplicationDesc());
        return modelAndView;
    }

    @GetMapping(value = "/voucher")
    public ModelAndView voucher(@RequestParam(required = false) String orderNo,
                                @RequestParam(required = false) String gatewayPaymentNo,
                                @RequestParam(required = false) BigDecimal amount) {
        ModelAndView modelAndView = new ModelAndView("voucher");
        PaymentResult result = new PaymentResult(orderNo, gatewayPaymentNo, amount, Strings.STR_NULL);
        if (result != null && result.isSuccessful()) {
            modelAndView.addObject("appName", getApplicationDesc());
            modelAndView.setStatus(HttpStatus.resolve(result.getResponseStatus()));
            modelAndView.addObject("orderNo", result.getOrderNo());
            modelAndView.addObject("gatewayPaymentNo", result.getGatewayPaymentNo());
            modelAndView.addObject("amount", result.getAmount());
        } else {
            modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return modelAndView;
    }

    private String getApplicationDesc() {
        return "The Payment Microservice";
    }
}
