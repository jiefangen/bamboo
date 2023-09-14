package org.panda.service.payment.core.domain.gateway.wechat;

/**
 * 微信支付产品
 */
public enum WechatPayProduct {

    /**
     * 在微信内嵌的网页或小程序中调起微信支付
     */
    JSAPI,

    /**
     * 生成支付二维码，通过微信扫描进行支付
     */
    NATIVE,

    /**
     * 在微信之外的移动端APP中调起微信APP，完成微信支付
     */
    APP,

    /**
     * 在微信之外的移动端网页中调起微信APP，完成微信支付
     */
    H5,

}
