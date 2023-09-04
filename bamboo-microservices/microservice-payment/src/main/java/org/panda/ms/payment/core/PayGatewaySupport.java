package org.panda.ms.payment.core;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.ms.payment.core.domain.gateway.PaymentManager;
import org.panda.ms.payment.core.domain.model.PaymentDefinition;
import org.panda.ms.payment.core.domain.model.PaymentRequest;
import org.panda.ms.payment.core.domain.model.PaymentResult;
import org.panda.tech.core.spec.terminal.Terminal;
import org.panda.tech.core.web.mvc.servlet.http.HttpRequestDataProvider;
import org.panda.tech.core.web.mvc.servlet.http.HttpServletRequestDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 抽象支付网关支持
 */
public abstract class PayGatewaySupport {

    @Autowired
    protected PaymentManager paymentManager;

    public PaymentRequest prepare(String gatewayName, PaymentDefinition definition) {
        return this.paymentManager.prepareRequest(gatewayName, definition);
    }

    public String confirmResult(String gatewayName, HttpServletRequest request, HttpServletResponse response) {
        HttpRequestDataProvider notifyDataProvider = new HttpServletRequestDataProvider(request);
        PaymentResult result = this.paymentManager.notifyResult(gatewayName, true, notifyDataProvider);
        if (result != null) {
            response.setStatus(result.getResponseStatus());
            return result.getResponseBody();
        }
        return Strings.EMPTY;
    }

    public ModelAndView showResult(String gatewayName, String terminal, HttpServletRequest request) {
        HttpRequestDataProvider notifyDataProvider = new HttpServletRequestDataProvider(request);
        PaymentResult result = this.paymentManager.notifyResult(gatewayName, false, notifyDataProvider);
        return resolveShowResult(request, result, Terminal.of(terminal));
    }

    protected abstract ModelAndView resolveShowResult(HttpServletRequest request, PaymentResult result,
                                                      Terminal terminal);
}
