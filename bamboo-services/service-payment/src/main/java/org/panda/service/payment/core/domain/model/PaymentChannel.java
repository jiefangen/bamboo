package org.panda.service.payment.core.domain.model;

import org.panda.bamboo.common.annotation.Caption;
import org.panda.bamboo.common.annotation.EnumValue;

/**
 * 支付渠道
 */
public enum PaymentChannel {

    @Caption("支付宝")
    @EnumValue("alipay")
    ALIPAY,

    @Caption("微信支付")
    @EnumValue("wechat")
    WECHAT,

    @Caption("PayPal")
    @EnumValue("paypal")
    PAYPAL,

    @Caption("Apple Pay")
    @EnumValue("apple")
    APPLE,

    @Caption("Google支付")
    @EnumValue("google")
    GOOGLE;
}
