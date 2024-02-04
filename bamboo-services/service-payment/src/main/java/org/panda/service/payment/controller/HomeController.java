package org.panda.service.payment.controller;

import io.swagger.annotations.Api;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.service.payment.core.domain.model.PaymentResult;
import org.panda.tech.core.web.controller.HomeControllerSupport;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;

@Api(tags = "匿名服务问候语")
@Controller
public class HomeController extends HomeControllerSupport {

    @GetMapping(value = "/voucher")
    public ModelAndView voucher(@RequestParam(required = false) String orderNo,
                                @RequestParam(required = false) String gatewayPaymentNo,
                                @RequestParam(required = false) BigDecimal amount) {
        ModelAndView modelAndView = new ModelAndView("voucher");
        PaymentResult result = new PaymentResult(orderNo, gatewayPaymentNo, amount, Strings.STR_NULL);
        if (result != null && result.isSuccessful()) {
            modelAndView.addObject("appDesc", super.getApplicationDesc());
            modelAndView.setStatus(HttpStatus.resolve(result.getResponseStatus()));
            modelAndView.addObject("orderNo", result.getOrderNo());
            modelAndView.addObject("gatewayPaymentNo", result.getGatewayPaymentNo());
            modelAndView.addObject("amount", result.getAmount());
        } else {
            modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return modelAndView;
    }

}
