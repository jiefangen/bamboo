package org.panda.service.payment.core.domain.gateway;

import org.panda.bamboo.core.beans.ContextInitializedBean;
import org.panda.service.payment.core.domain.model.PaymentDefinition;
import org.panda.service.payment.core.domain.model.PaymentRequest;
import org.panda.service.payment.core.domain.model.PaymentResult;
import org.panda.tech.core.spec.terminal.Terminal;
import org.panda.tech.core.web.mvc.servlet.http.HttpRequestDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.*;

/**
 * 支付管理器实现
 */
@Component
public class PaymentManagerImpl implements PaymentManager, ContextInitializedBean {

    private Map<String, PaymentGatewayAdapter> gateways = new HashMap<>();
    @Autowired // 必须有侦听器实现，否则支付业务一定不完整
    private PaymentListener listener;

    @Override
    public void afterInitialized(ApplicationContext context) throws Exception {
        Map<String, PaymentGatewayAdapter> adapters = context.getBeansOfType(PaymentGatewayAdapter.class);
        for (PaymentGatewayAdapter gateway : adapters.values()) {
            String name = gateway.getName();
            Assert.isNull(this.gateways.put(name, gateway), "More than one gateway named " + name);
        }
    }

    @Override
    public List<PaymentGateway> getGateways(Terminal terminal) {
        List<PaymentGateway> gateways = new ArrayList<>();
        for (PaymentGateway gateway : this.gateways.values()) {
            Terminal[] terminals = gateway.getTerminals();
            if (terminals != null) {
                for (Terminal t : terminals) {
                    if (t.supports(terminal)) {
                        gateways.add(gateway);
                    }
                }
            }
        }
        gateways.sort(Comparator.comparingInt(Ordered::getOrder));
        return gateways;
    }

    @Override
    public PaymentGateway getGateway(String gatewayName) {
        return this.gateways.get(gatewayName);
    }

    @Override
    public PaymentRequest prepareRequest(String gatewayName, PaymentDefinition definition) {
        PaymentGatewayAdapter adapter = this.gateways.get(gatewayName);
        if (adapter != null) {
            return adapter.prepareRequest(definition);
        }
        return null;
    }

    @Override
    public PaymentResult notifyResult(String gatewayName, boolean confirmed,
                                      HttpRequestDataProvider notifyDataProvider) {
        PaymentGatewayAdapter adapter = this.gateways.get(gatewayName);
        if (adapter != null) {
            PaymentResult result = adapter.parseResult(notifyDataProvider);
            if (confirmed && result != null && result.isSuccessful()) {
                this.listener.onPaid(adapter.getChannel(), result.getGatewayPaymentNo(), result.getOrderNo(),
                        result.getAmount());
            }
            return result;
        }
        return null;
    }

    @Override
    public void requestRefund(String gatewayName, String gatewayPaymentNo, BigDecimal paymentAmount, String refundNo,
            String refundAmount) {
        PaymentGatewayAdapter adapter = this.gateways.get(gatewayName);
        if (adapter != null) {
            String gatewayRefundNo = adapter.requestRefund(gatewayPaymentNo, paymentAmount, refundNo, refundAmount);
            if (this.listener != null) {
                this.listener.onRefunded(refundNo, gatewayRefundNo);
            }
        }
    }

}
