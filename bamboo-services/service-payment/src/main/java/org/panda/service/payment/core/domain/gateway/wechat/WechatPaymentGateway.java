package org.panda.service.payment.core.domain.gateway.wechat;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.ExceptionUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.service.payment.core.domain.gateway.AbstractPaymentGateway;
import org.panda.ms.payment.core.domain.model.*;
import org.panda.service.payment.core.domain.model.*;
import org.panda.tech.core.spec.terminal.Device;
import org.panda.tech.core.spec.terminal.OS;
import org.panda.tech.core.spec.terminal.Terminal;
import org.panda.tech.core.web.mvc.servlet.http.HttpRequestDataProvider;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Base64Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.*;

/**
 * 支付网关：微信支付
 */
public class WechatPaymentGateway extends AbstractPaymentGateway implements DisposableBean {

    /**
     * 下单地址前缀
     */
    private static final String TRANS_URL_PREFIX = "https://api.mch.weixin.qq.com/v3/pay/transactions/";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    private final WechatPayClient client;
    private final WechatPayPlatformCertManager platformCertManager;

    public WechatPaymentGateway(WechatPayClient client) {
        this.client = client;
        this.platformCertManager = new WechatPayPlatformCertManager(client);
    }

    @Override
    public PaymentChannel getChannel() {
        return PaymentChannel.WECHAT;
    }

    @Override
    public PaymentRequest prepareRequest(PaymentDefinition definition) {
        Terminal terminal = definition.getTerminal();
        WechatPayProduct product = getProduct(terminal);
        if (product != null) {
            Map<String, Object> prepareParams = new HashMap<>();
            prepareParams.put("mchid", this.client.getMerchantId()); // 商户id
            prepareParams.put("appid", this.client.getAppId()); // 应用id
            prepareParams.put("out_trade_no", definition.getOrderNo()); // 商户订单号
            prepareParams.put("description", definition.getDescription()); // 商品描述
            prepareParams.put("notify_url", getResultConfirmUrl()); // 通知地址

            Map<String, Object> amount = new HashMap<>();
            amount.put("total", definition.getAmount().multiply(BigDecimal.valueOf(100)).intValue()); // 金额
            Currency currency = definition.getCurrency();
            if (currency == null) {
                currency = Currency.getInstance(Locale.getDefault());
            }
            amount.put("currency", currency.getCurrencyCode()); // 币种
            prepareParams.put("amount", amount);

            if (product == WechatPayProduct.JSAPI) { // 微信内嵌支付时，用户openId必填
                Map<String, Object> payer = new HashMap<>();
                payer.put("openid", definition.getTarget());
                prepareParams.put("payer", payer);
            } else if (product == WechatPayProduct.H5) { // 微信外部移动网页支付时，用户终端IP和H5场景类型必填
                Map<String, Object> scene = new HashMap<>();
                scene.put("payer_client_ip", definition.getPayerIp());
                Map<String, Object> h5 = new HashMap<>();
                h5.put("type", getH5Type(terminal.getOs()));
                scene.put("h5_info", h5);
                prepareParams.put("scene_info", scene);
            }
            return request(product, prepareParams);
        }
        return null;
    }

    private WechatPayProduct getProduct(Terminal terminal) {
        switch (terminal.getProgram()) {
            case WEB:
                // PC端网页使用二维码扫码支付
                if (terminal.getDevice() == Device.PC) {
                    return WechatPayProduct.NATIVE;
                }
                // 移动端（不论手机还是平板）网页使用H5方式
                return WechatPayProduct.H5;
            case NATIVE: // 原生APP
                return WechatPayProduct.APP;
            case MINI: // 微信小程序
            case HYBRID: // 微信内嵌网页
                return WechatPayProduct.JSAPI;
            default:
                return null;
        }
    }

    private String getH5Type(OS os) {
        if (os == OS.MAC) {
            return "iOS";
        }
        if (os == OS.ANDROID) {
            return "Android";
        }
        return "Wap";
    }

    private PaymentRequest request(WechatPayProduct product, Map<String, Object> prepareParams) {
        try {
            String uri = TRANS_URL_PREFIX + product.name().toLowerCase();
            HttpResponse response = this.client.post(uri, prepareParams);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_ACCEPTED) { // 202，服务器已接受请求，但尚未处理；使用原参数重复请求一遍
                return request(product, prepareParams);
            }
            String responseJson = EntityUtils.toString(response.getEntity());
            Map<String, Object> responseData = JsonUtil.json2Map(responseJson);
            if (statusCode == HttpStatus.SC_OK) {
                if (product == WechatPayProduct.NATIVE) {
                    String url = (String) responseData.get("code_url");
                    PaymentRequest paymentRequest = new PaymentRequest(url, PaymentRequestMode.QRCODE, null);
                    paymentRequest.setRequestData(JsonUtil.toJson(prepareParams));
                    paymentRequest.setResponseData(responseJson);
                    return paymentRequest;
                }
                Map<String, String> requestParams = new LinkedHashMap<>();
                requestParams.put("appId", this.client.getAppId());
                requestParams.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
                requestParams.put("nonceStr", StringUtil.uuid32());
                requestParams.put("package", "prepay_id=" + responseData.get("prepay_id"));
                requestParams.put("paySign",
                        generateSignature(requestParams.values(), this.client.getCertPrivateKey()));
                requestParams.put("signType", "RSA"); // 不参与签名
                PaymentRequest paymentRequest = new PaymentRequest(null, PaymentRequestMode.GET_LINK, requestParams);
                paymentRequest.setRequestData(JsonUtil.toJson(prepareParams));
                paymentRequest.setResponseData(responseJson);
                return paymentRequest;
            }
            String errorMessage = (String) responseData.get("message");
            throw new RuntimeException(errorMessage);
        } catch (Exception e) {
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

    private String generateSignature(Collection<String> data, PrivateKey privateKey) {
        StringBuilder token = new StringBuilder();
        for (String value : data) {
            token.append(value).append(Strings.ENTER);
        }
        try {
            Signature signer = Signature.getInstance(SIGNATURE_ALGORITHM);
            signer.initSign(privateKey);
            signer.update(token.toString().getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signer.sign());
        } catch (Exception e) {
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public PaymentResult parseResult(HttpRequestDataProvider notifyDataProvider) {
        try {
            String certSerialNo = notifyDataProvider.getHeader("Wechatpay-Serial");
            Certificate platformCert = this.platformCertManager.getCert(certSerialNo);

            String signature = notifyDataProvider.getHeader("Wechatpay-Signature");
            String timestamp = notifyDataProvider.getHeader("Wechatpay-Timestamp");
            String nonce = notifyDataProvider.getHeader("Wechatpay-Nonce");
            String body = notifyDataProvider.getBody();
            String token = timestamp + Strings.ENTER + nonce + Strings.ENTER + body + Strings.ENTER;

            Signature signer = Signature.getInstance(SIGNATURE_ALGORITHM);
            signer.initVerify(platformCert);
            signer.update(token.getBytes(StandardCharsets.UTF_8));
            if (signer.verify(Base64Utils.decodeFromString(signature))) {
                Map<String, Object> bodyMap = JsonUtil.json2Map(body);
                Map<String, Object> resource = (Map<String, Object>) bodyMap.get("resource");
                String resourceJson = this.platformCertManager.decryptResource(resource);
                resource = JsonUtil.json2Map(resourceJson);

                String tradeState = (String) resource.get("trade_state");
                if ("SUCCESS".equals(tradeState)) {
                    String gatewayPaymentNo = (String) resource.get("transaction_id");
                    String orderNo = (String) resource.get("out_trade_no");
                    Map<String, Object> amount = (Map<String, Object>) resource.get("amount");
                    BigDecimal amountTotal = new BigDecimal((Integer) amount.get("total"))
                            .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                    return new PaymentResult(orderNo, gatewayPaymentNo, amountTotal, Commons.RESULT_SUCCESS);
                }
            }
        } catch (Exception e) {
            Map<String, Object> error = Map.of("code", "FAIL", "message", e.getMessage());
            return new PaymentResult(HttpStatus.SC_INTERNAL_SERVER_ERROR, JsonUtil.toJson(error));
        }
        return null;
    }

    @Override
    public void destroy() throws Exception {
        this.client.close();
    }

}
