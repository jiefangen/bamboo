package org.panda.service.payment.core.domain.gateway.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.ExceptionUtil;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.bamboo.common.util.lang.CollectionUtil;
import org.panda.bamboo.common.util.lang.MathUtil;
import org.panda.service.payment.core.domain.gateway.AbstractPaymentGateway;
import org.panda.ms.payment.core.domain.model.*;
import org.panda.service.payment.core.domain.model.*;
import org.panda.tech.core.spec.terminal.Device;
import org.panda.tech.core.spec.terminal.Program;
import org.panda.tech.core.spec.terminal.Terminal;
import org.panda.tech.core.web.mvc.servlet.http.HttpRequestDataProvider;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付网关：支付宝支付
 */
public class AlipayPaymentGateway extends AbstractPaymentGateway {

    private AlipayConfig config;
    private AlipayClient client;
    private Logger logger = LogUtil.getLogger(getClass());

    public AlipayPaymentGateway(AlipayConfig config) throws AlipayApiException {
        this.config = config;
        this.client = new DefaultAlipayClient(config);
    }

    @Override
    public PaymentChannel getChannel() {
        return PaymentChannel.ALIPAY;
    }

    @Override
    public PaymentRequest prepareRequest(PaymentDefinition definition) {
        Terminal terminal = definition.getTerminal();
        Program program = terminal.getProgram();
        Device device = terminal.getDevice();
        try {
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            request.setNeedEncrypt(this.config.getEncryptKey() != null);
            request.setNotifyUrl(getResultConfirmUrl());
            request.setReturnUrl(getResultShowUrl());
            if (program == Program.WEB) {
                if (device == Device.PC) { // 电脑网站支付
                    return pagePay(definition, request);
                } else { // 手机网站支付（含平板）
                    // TODO
                }
            } else if (program == Program.NATIVE) { // APP支付
                // TODO
            }
        } catch (Exception e) {
            throw ExceptionUtil.toRuntimeException(e);
        }
        return null;
    }

    private String getAttributeValue(String xml, int fromIndex, String attributeName) {
        String prefix = attributeName + Strings.EQUAL + Strings.DOUBLE_QUOTES;
        int attrIndex = xml.indexOf(prefix, fromIndex);
        if (attrIndex >= 0) {
            attrIndex += prefix.length();
            int quotesIndex = xml.indexOf(Strings.DOUBLE_QUOTES, attrIndex);
            return xml.substring(attrIndex, quotesIndex);
        }
        return null;
    }

    private PaymentRequest pagePay(PaymentDefinition definition, AlipayTradePagePayRequest request) throws AlipayApiException {
        Map<String, Object> prepareParams = new HashMap<>();
        prepareParams.put("out_trade_no", definition.getOrderNo());
        prepareParams.put("total_amount", definition.getAmount().doubleValue());
        prepareParams.put("subject", definition.getDescription());
        prepareParams.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(JsonUtil.toJson(prepareParams));
        AlipayTradePagePayResponse response = this.client.pageExecute(request);
        if (response.isSuccess()) {
            String body = response.getBody();
            // 由于action属性值中存在未转义的&符号，xml无法正确解析，而biz_content中又存在包含&的转义符，无法转义后再反转，
            // 此处通过简单字符串解析
            int formIndex = body.indexOf("<form ");
            String url = getAttributeValue(body, formIndex, "action");
            String method = getAttributeValue(body, formIndex, "method").toUpperCase();
            PaymentRequestMode mode = PaymentRequestMode.valueOf(method);
            int inputIndex = body.indexOf(Strings.GREATER_THAN, formIndex) + 1;
            String inputString = body.substring(inputIndex, body.indexOf("</form>")).trim();
            String[] inputs = inputString.split(Strings.ENTER);
            Map<String, String> params = new HashMap<>();
            for (String input : inputs) {
                String name = getAttributeValue(input, 0, "name");
                if (name != null) {
                    String value = getAttributeValue(input, 0, "value");
                    if (value != null) {
                        params.put(name, value);
                    }
                }
            }
            return new PaymentRequest(url, mode, params);
        } else {
            this.logger.error("====== Payment(orderNo={}) error: {}", definition.getOrderNo(), response.getSubMsg());
        }
        return null;
    }

    @Override
    public PaymentResult parseResult(HttpRequestDataProvider notifyDataProvider) {
        Map<String, Object> parameters = notifyDataProvider.getParameters();
        Map<String, String> params = CollectionUtil.toStringMap(parameters);
        String charset = this.config.getCharset();
        String signType = this.config.getSignType();
        try {
            if (AlipaySignature.rsaCheckV1(params, this.config.getAlipayPublicKey(), charset, signType)) {
                String orderNo = params.get("out_trade_no");
                String tradeStatus = params.get("trade_status");
                if (tradeStatus == null) { // 结果展示时交易状态为null
                    return new PaymentResult(orderNo, null, null, null);
                } else if ("TRADE_SUCCESS".equals(tradeStatus)) { // 结果通知时交易状态不为null
                    String gatewayPaymentNo = params.get("trade_no");
                    BigDecimal amount = MathUtil.parseDecimal(params.get("total_amount"));
                    return new PaymentResult(orderNo, gatewayPaymentNo, amount, Commons.RESULT_SUCCESS);
                }
            }
        } catch (Exception e) {
            throw ExceptionUtil.toRuntimeException(e);
        }
        return null;
    }

}
