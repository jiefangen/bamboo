package org.panda.service.payment.core.domain.gateway;

import org.panda.service.payment.core.domain.model.PaymentDefinition;
import org.panda.service.payment.core.domain.model.PaymentRequest;
import org.panda.service.payment.core.domain.model.PaymentResult;
import org.panda.tech.core.web.mvc.servlet.http.HttpRequestDataProvider;

import java.math.BigDecimal;

/**
 * 支付网关适配器
 */
public interface PaymentGatewayAdapter extends PaymentGateway {

    /**
     * 预处理支付请求
     *
     * @param definition 支付定义
     * @return 支付请求
     */
    PaymentRequest prepareRequest(PaymentDefinition definition);

    /**
     * 解析支付结果
     *
     * @param notifyDataProvider 通知数据提供者
     * @return 支付结果
     */
    PaymentResult parseResult(HttpRequestDataProvider notifyDataProvider);

    /**
     * 发起退款请求
     *
     * @param gatewayPaymentNo 支付网关支付流水号
     * @param paymentAmount    支付金额
     * @param refundNo         退款单编号
     * @param refundAmount     退款金额
     * @return 支付网关退款流水号，返回null说明未成功申请退款
     */
    String requestRefund(String gatewayPaymentNo, BigDecimal paymentAmount, String refundNo, String refundAmount);

}
