package org.panda.service.payment.core.domain.model;

import org.panda.bamboo.common.annotation.Caption;

/**
 * 支付请求付款方式
 */
public enum PaymentRequestMode {

    @Caption("二维码")
    QRCODE,

    @Caption("以GET方式打开链接")
    GET_LINK,

    @Caption("以POST方式提交参数付款")
    POST_PAY,

}
