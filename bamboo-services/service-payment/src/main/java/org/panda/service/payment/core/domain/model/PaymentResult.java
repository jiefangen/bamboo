package org.panda.service.payment.core.domain.model;

import org.apache.http.HttpStatus;

import java.math.BigDecimal;


/**
 * 支付响应结果
 */
public class PaymentResult {
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 支付网关支付流水号
     */
    private String gatewayPaymentNo;
    /**
     * 支付金额
     */
    private BigDecimal amount;
    /**
     * 响应状态码
     */
    private int responseStatus;
    /**
     * 返回给支付网关的响应体内容
     */
    private String responseBody;

    public PaymentResult(String orderNo, String gatewayPaymentNo, BigDecimal amount, String responseBody) {
        this.gatewayPaymentNo = gatewayPaymentNo;
        this.orderNo = orderNo;
        this.amount = amount;
        this.responseStatus = HttpStatus.SC_OK;
        this.responseBody = responseBody;
    }

    public PaymentResult(int responseStatus, String responseBody) {
        assert responseStatus != HttpStatus.SC_OK;
        this.responseStatus = responseStatus;
        this.responseBody = responseBody;
    }

    public String getOrderNo() {
        return this.orderNo;
    }

    public String getGatewayPaymentNo() {
        return this.gatewayPaymentNo;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public int getResponseStatus() {
        return this.responseStatus;
    }

    public String getResponseBody() {
        return this.responseBody;
    }

    public boolean isSuccessful() {
        return this.responseStatus == HttpStatus.SC_OK;
    }

}
