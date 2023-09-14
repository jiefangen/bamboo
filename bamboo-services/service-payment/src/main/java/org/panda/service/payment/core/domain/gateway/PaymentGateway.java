package org.panda.service.payment.core.domain.gateway;

import org.panda.service.payment.core.domain.model.PaymentChannel;
import org.panda.tech.core.spec.terminal.Terminal;
import org.springframework.core.Ordered;

/**
 * 支付网关
 */
public interface PaymentGateway extends Ordered {

    String getName();

    boolean isActive();

    PaymentChannel getChannel();

    String getNationCode();

    Terminal[] getTerminals();

    boolean isRefundable();

}
