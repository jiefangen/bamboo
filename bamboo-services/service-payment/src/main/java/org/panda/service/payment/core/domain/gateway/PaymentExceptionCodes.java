package org.panda.service.payment.core.domain.gateway;

/**
 * 支付模块异常错误码常量类
 */
public class PaymentExceptionCodes {

    private PaymentExceptionCodes() {
    }

    /**
     * 支付失败
     */
    public static String PAYMENT_FAIL = "error.payment.fail";

    /**
     * 不是即时到账时返回失败结果
     */
    public static String NOT_INSTANT_TO_ACCOUNT = "error.payment.not_instant_to_account";

    /**
     * 签名验证失败
     */
    public static String SIGN_FAIL = "error.payment.sign_fail";

    /**
     * 有可能因为网络原因，请求已经处理，但未收到应答
     */
    public static String CONNECTION_FAIL = "error.payment.connection_fail";

    /**
     * 不确定的退款
     */
    public static String UNSURE_REFUND = "error.payment.refund.unsure";

    /**
     * 支付网关退款错误
     */
    public static String GATEWAY_REFUND_ERROR = "error.payment.refund.gateway";

    /**
     * 无效的退款收款账号
     */
    public static String INVALID_REFUND_RECIPIENT = "error.payment.refund.invalid_recipient";

}
