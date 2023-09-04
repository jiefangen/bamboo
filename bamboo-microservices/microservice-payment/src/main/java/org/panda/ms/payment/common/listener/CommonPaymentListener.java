package org.panda.ms.payment.common.listener;

import org.panda.ms.payment.core.domain.gateway.PaymentListener;
import org.panda.ms.payment.core.domain.model.PaymentChannel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 通用支付回调监听器实现
 *
 * @author fangen
 **/
@Component
public class CommonPaymentListener implements PaymentListener {
    @Override
    public void onPaid(PaymentChannel channel, String gatewayPaymentNo, String orderNo, BigDecimal amount) {

    }
}
