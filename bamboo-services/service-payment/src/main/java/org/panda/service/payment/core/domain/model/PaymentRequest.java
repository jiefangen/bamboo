package org.panda.service.payment.core.domain.model;

import java.util.Map;

/**
 * 支付请求
 */
public class PaymentRequest {

    private String url;
    private PaymentRequestMode mode;
    private Map<String, String> params;

    private String requestData;
    private String responseData;

    public PaymentRequest(String url, PaymentRequestMode mode, Map<String, String> params) {
        this.url = url;
        this.mode = mode;
        this.params = params;
    }

    public String getUrl() {
        return this.url;
    }

    public PaymentRequestMode getMode() {
        return this.mode;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }
}
