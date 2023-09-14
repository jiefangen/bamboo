package org.panda.service.payment.core.domain.gateway;

import org.panda.service.payment.core.domain.model.PaymentChannel;

import java.math.BigDecimal;

/**
 * 支付侦听器
 */
public interface PaymentListener {

    /**
     * 在完成支付后被调用
     *
     * @param channel          支付渠道
     * @param gatewayPaymentNo 支付网关支付流水号
     * @param orderNo          订单编号
     * @param amount           实际支付金额
     */
    void onPaid(PaymentChannel channel, String gatewayPaymentNo, String orderNo, BigDecimal amount);

    /**
     * 在完成退款请求后被调用
     *
     * @param refundNo        退款单编号
     * @param gatewayRefundNo 支付网关退款流水号
     */
    default void onRefunded(String refundNo, String gatewayRefundNo) {
        throw new UnsupportedOperationException();
    }

}
